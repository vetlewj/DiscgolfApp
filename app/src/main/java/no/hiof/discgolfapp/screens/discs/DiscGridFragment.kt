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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import no.hiof.discgolfapp.databinding.FragmentDiscGridBinding

class DiscGridFragment : Fragment()  {

    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
    private val args: DiscGridFragmentArgs by navArgs()

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


        Log.d("disc speed", "name!!!: ${args.nameArray}")
        Log.d("disc speed", "speed!!!: ${args.speedArray}")
        Log.d("disc speed", "turn!!!: ${args.turnArray}")
        Log.d("disc speed", "fade!!!: ${args.fadeArray}")
        Log.d("disc speed", "color!!!: ${args.colorArray}")

        val name = args.nameArray[0]
        val speed = args.speedArray[0]
        val turn = args.turnArray[0]
        val fade = args.fadeArray[0]
        val color = args.colorArray[0]


        var gridHeightNumber = 15
        var gridWidthNumber = 12


        val width = 700
        val height = 1000

        // rectangle positions
        var left = 40.toFloat()
        var top = 60.toFloat()
        var right = 660.toFloat()
        var bottom = 840.toFloat()

        val gridWidth = right-left
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

//        canvas.drawCircle((width / 2).toFloat(), (bottom/2 ).toFloat(), 5F, Paint().apply { setARGB(255, 255, 0, 0)})



        val blackPaint = Paint()
        blackPaint.color = Color.BLACK
        blackPaint.strokeWidth = 2.5F

        val blackText = Paint()
        blackText.textSize = 30F
        blackText.color = Color.BLACK

        val discColor = Paint()
        discColor.color = Color.RED







        fun stability(turn: Int, Fade: Int): Int {
            var stabilityNumberOffset = 6 //
            return (turn + fade) + stabilityNumberOffset
        }

//        while ()


        fun drawDiscCircle(speed: Int, turn : Int, fade: Int){
            var stability = stability(turn, fade)
            val speedValue = (top/2) +((gridHeight / gridHeightNumber) * (gridHeightNumber+1-speed))
            val stabilityValue = (left/2) + ((gridWidth / gridWidthNumber) * (gridWidthNumber+1-stability))
            canvas.drawCircle(stabilityValue, speedValue,15f, discColor)
        }

        drawDiscCircle(args.speedArray[1], args.turnArray[1], args.fadeArray[1] )
        drawDiscCircle(args.speedArray[0], args.turnArray[0], args.fadeArray[0] )



        fun drawGridHeightLines() {
            var i = 0
            Log.d("canvas lines", "Drawing grid height lines")
            while (i <= gridHeightNumber) {
                var heightSteps = top + ((gridHeight / gridHeightNumber) * i)
                Log.d("heightSteps", "heightSteps = $heightSteps")
                canvas.drawLine(left, heightSteps, right, heightSteps, blackPaint)
                i++
            }
        }

        fun drawGridWidthLines(){
            var i = 0
            while (i <= gridWidthNumber){
                var widthSteps = left + ((gridWidth / gridWidthNumber) * i)
                canvas.drawLine(widthSteps, top, widthSteps, bottom, blackPaint)
                i++
            }
        }

        fun drawGridSpeedNumbers(){
            var i = 0
            var x = gridHeightNumber
            while(i < gridHeightNumber){
                var heightSteps = top + ((gridHeight / gridHeightNumber) * i)
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
                var widthSteps = left + ((gridWidth / gridWidthNumber) * i)
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
//        drawDiscCircle()



        val imageView = binding.imageView
        imageView.background = BitmapDrawable(getResources(), bitmap)


    }


}