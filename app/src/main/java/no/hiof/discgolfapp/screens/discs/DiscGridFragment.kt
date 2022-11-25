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
import androidx.navigation.fragment.navArgs
import no.hiof.discgolfapp.databinding.FragmentDiscGridBinding

class DiscGridFragment : Fragment()  {

    private val args: DiscGridFragmentArgs by navArgs()
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


        val max = args.speedArray.max()
        val gridHeightNumber = max   //Max disc speed = 15
        val gridWidthNumber = 12


        val width = 700
        val height = 1000

        // rectangle positions
        val left = 40.toFloat()
        val top = 60.toFloat()
        val right = 680.toFloat()
        val bottom = 840.toFloat()

        val gridWidth = right-left
        val gridHeight = bottom-top


        val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        var shapeDrawable: ShapeDrawable


        fun drawRectangle() {
            shapeDrawable = ShapeDrawable(RectShape())
            shapeDrawable.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
            shapeDrawable.paint.color = Color.parseColor("lightgray")
            shapeDrawable.draw(canvas)
        }
        drawRectangle()


        //Colors:
        val gridLinesColor = Paint()
        gridLinesColor.color = Color.WHITE
        gridLinesColor.strokeWidth = 2.5F

        val blackText = Paint()
        blackText.textSize = 30F

        val discText = Paint()
        discText.textSize = 25F
        discText.color = Color.BLACK

        val discOuterColor = Paint()
        discOuterColor.color = Color.BLACK


        fun discColor (colors: String): Paint {
            var color = colors
            color.lowercase()
            Log.d("Color String", "color: $color")
            if (color == ""){
                color = "lightgray"
            }
            val colorDisc = Paint()
            try {
                colorDisc.color = Color.parseColor(color)
            } catch (e: IllegalArgumentException){
                Log.e("ParseColor error", "IllegalArgumentException, color= $color not found")
                colorDisc.color = Color.BLACK
            }finally{
            }
            return colorDisc
        }


        fun drawGridHeightLines() {
            var i = 0
            Log.d("canvas lines", "Drawing grid height lines")
            while (i <= gridHeightNumber) {
                val heightSteps = top + ((gridHeight / gridHeightNumber) * i)
                Log.d("heightSteps", "heightSteps = $heightSteps")
                canvas.drawLine(left, heightSteps, right, heightSteps, gridLinesColor)
                i++
            }
        }

        fun drawGridWidthLines(){
            var i = 0
            while (i <= gridWidthNumber){
                val widthSteps = left + ((gridWidth / gridWidthNumber) * i)
                canvas.drawLine(widthSteps, top, widthSteps, bottom, gridLinesColor)
                i++
            }
        }

        fun drawGridSpeedNumbers(){
            var i = 0
            var x = gridHeightNumber
            while(i < gridHeightNumber){
                val heightSteps = top + ((gridHeight / gridHeightNumber) * i)
                canvas.drawText(x.toString(), (left/2)-15, ((gridHeight / gridHeightNumber)/2)+15+ heightSteps, blackText)
                i++
                x--
            }
        }

        val stabilityNumbers = listOf(6, 5, 4, 3, 2, 1, 0, -1, -2, -3, -4, -5)

        //Stability = (Turn+Fade)
        fun drawGridStabilityNumbers(){
            var i = 0
//            var x = gridWidthNumber
            while(i < gridWidthNumber){
                val widthSteps = left + ((gridWidth / gridWidthNumber) * i)
//                canvas.drawText(x.toString(), ((gridWidth / gridWidthNumber)/2)-10+ widthSteps, top-5, blackText)
                canvas.drawText(stabilityNumbers[i].toString(), ((gridWidth / gridWidthNumber)/2)-10+ widthSteps, top-5, blackText)
                i++
//                x--
            }
        }

        drawGridHeightLines()
        drawGridWidthLines()
        drawGridSpeedNumbers()
        drawGridStabilityNumbers()

        fun stability(turn: Int, fade: Int): Int {
            val stabilityNumberOffset = 6 // Offset to match grid stabilityNumbers
            return (turn + fade) + stabilityNumberOffset
        }


        fun drawDiscCircle(speed: Int, turn : Int, fade: Int, name: String, color: String){
            val stability = stability(turn, fade)
            val speedValue = (top/2) +((gridHeight / gridHeightNumber) * (gridHeightNumber+1-speed))
            val stabilityValue = (left/2) + ((gridWidth / gridWidthNumber) * (gridWidthNumber+1-stability))

            canvas.drawCircle(stabilityValue-6, speedValue+4,15f, discOuterColor)
            canvas.drawCircle(stabilityValue-6, speedValue+4,12f, discColor(color))
            canvas.drawText("($name)", stabilityValue+10, speedValue+29, discText)
        }

        var y = 0
        while (y < args.nameArray.size) {
            drawDiscCircle(args.speedArray[y], args.turnArray[y], args.fadeArray[y], args.nameArray[y], args.colorArray[y])
            y++
        }

        val imageView = binding.imageView
        imageView.background = BitmapDrawable(resources, bitmap)
    }
}