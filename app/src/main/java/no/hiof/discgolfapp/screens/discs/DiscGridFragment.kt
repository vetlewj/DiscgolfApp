package no.hiof.discgolfapp.screens.discs

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
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

        val gridWidth = 700
        val gridHeight = 1000

        val bitmap: Bitmap = Bitmap.createBitmap(gridWidth, gridHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        var shapeDrawable: ShapeDrawable

        // rectangle positions
        var left = 30.toFloat()
        var top = 50.toFloat()
        var right = 670.toFloat()
        var bottom = 600.toFloat()

        // draw rectangle shape to canvas
        shapeDrawable = ShapeDrawable(RectShape())
        shapeDrawable.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        shapeDrawable.getPaint().setColor(Color.parseColor("lightgray"))
        shapeDrawable.draw(canvas)

        val paint = Paint()
        paint.color = Color.BLACK
        paint.strokeWidth = 2F


        fun gridFrame() {
            canvas.drawLine(left, top, right, top, paint)
            canvas.drawLine(left, top, left, bottom, paint)
            canvas.drawLine(left, bottom, right, bottom, paint)
            canvas.drawLine(right, top, right, bottom, paint)
        }

        gridFrame()

        val imageView = binding.imageView
        imageView.background = BitmapDrawable(getResources(), bitmap)


    }


}