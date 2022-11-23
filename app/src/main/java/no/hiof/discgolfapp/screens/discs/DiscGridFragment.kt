package no.hiof.discgolfapp.screens.discs

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import no.hiof.discgolfapp.databinding.FragmentDiscGridBinding

class DiscGridFragment : Fragment()  {


    private var _binding: FragmentDiscGridBinding? = null
    private lateinit var binding: FragmentDiscGridBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDiscGridBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val width = 700
        val height = 1000

        // rectangle positions
        var left = 40.toFloat()
        var top = 60.toFloat()
        var right = 660.toFloat()
        var bottom = 840.toFloat()

        val gridWidth = right+left
        val gridHeight = bottom-top



        val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        var shapeDrawable: ShapeDrawable




        fun drawRectangle() {
            shapeDrawable = ShapeDrawable(RectShape())
            shapeDrawable.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
            shapeDrawable.getPaint().setColor(Color.parseColor("lightgray"))
            shapeDrawable.draw(canvas)
        }

        drawRectangle()

//        canvas.drawCircle(5,5,5, Paint(color.Red))

        canvas.drawCircle((gridWidth / 2).toFloat(), (gridHeight/2 ).toFloat(), 5F, Paint().apply { setARGB(255, 255, 0, 0)})

        val blackPaint = Paint()
        blackPaint.color = Color.BLACK
        blackPaint.strokeWidth = 2.5F

        var gridHeightNumber = 15
        var i = 0
        Log.d("canvas lines", "Drawing grid height lines")
        while (i <= gridHeightNumber){
            var heightSteps = top+((gridHeight / gridHeightNumber)*i)
            canvas.drawLine(left, heightSteps, right, heightSteps,blackPaint)
            i++
        }

        fun gridFrame() {
            canvas.drawLine(left, top, right, top, blackPaint)
            canvas.drawLine(left, top, left, bottom, blackPaint)
            canvas.drawLine(left, bottom, right, bottom, blackPaint)
            canvas.drawLine(right, top, right, bottom, blackPaint)
        }

//        gridFrame()

        val imageView = binding.imageView
        imageView.background = BitmapDrawable(getResources(), bitmap)


    }


}