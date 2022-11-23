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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.grpc.NameResolver.Args
import no.hiof.discgolfapp.databinding.FragmentDiscGridBinding
import no.hiof.discgolfapp.model.Disc
import no.hiof.discgolfapp.screens.discs.MyDiscsFragment

class DiscGridFragment : Fragment()  {

    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firestore = FirebaseFirestore.getInstance()
//    private val discList2: MutableList<Disc> = MyDiscsFragment
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




        firestore.collection("discs")
            .whereEqualTo("playerId", firebaseAuth.currentUser?.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("Fetch Disc", "${document.data}")
                    val discObject = document.toObject(Disc::class.java)!!
//                    discList.add(discObject)
//                    val speed = discList.get(0).speed.toString().toInt()
//                    Log.d("disc speed", "speed: $speed")

                }
            }
            .addOnFailureListener{ exception ->
                Log.w("Fetch disc", "Error fetching discs from Firestore: ", exception)
            }

//

//        lateinit val speed = discList.get(0).speed.toString().toInt()

        Log.d("disc speed", "speed!!!: ${args.speed}")


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

        canvas.drawCircle((width / 2).toFloat(), (bottom/2 ).toFloat(), 5F, Paint().apply { setARGB(255, 255, 0, 0)})

        val blackPaint = Paint()
        blackPaint.color = Color.BLACK
        blackPaint.strokeWidth = 2.5F

        val blackText = Paint()
        blackText.textSize = 30F
        blackText.color = Color.BLACK


        fun drawGridHeightLines() {
            var gridHeightNumber = 15
            var i = 0
            Log.d("canvas lines", "Drawing grid height lines")
            while (i <= gridHeightNumber) {
                var heightSteps = top + ((gridHeight / gridHeightNumber) * i)
                canvas.drawLine(left, heightSteps, right, heightSteps, blackPaint)
                i++
            }
        }

        fun drawGridWidthLines(){
            var gridWidthNumber = 12
            var i = 0
            while (i <= gridWidthNumber){
                var widthSteps = left + ((gridWidth / gridWidthNumber) * i)
                canvas.drawLine(widthSteps, top, widthSteps, bottom, blackPaint)
                i++
            }
        }

        fun drawGridSpeedNumbers(){
            var gridHeightNumber = 15
            var i = 0
            var x = gridHeightNumber
            while(i < gridHeightNumber){
                var heightSteps = top + ((gridHeight / gridHeightNumber) * i)
                canvas.drawText(x.toString(), (left/2)-15, ((gridHeight / gridHeightNumber)/2)+15+ heightSteps, blackText)
                i++
                x--
            }
        }

        fun drawGridStabilityNumbers(){
            var gridWidthNumber = 12
            var i = 0
            var x = gridWidthNumber
            while(i < gridWidthNumber){
                var widthSteps = left + ((gridWidth / gridWidthNumber) * i)
                canvas.drawText(x.toString(), ((gridWidth / gridWidthNumber)/2)-10+ widthSteps, top-5, blackText)
                i++
                x--
            }
        }

        drawGridHeightLines()
        drawGridWidthLines()
        drawGridSpeedNumbers()
        drawGridStabilityNumbers()



        val imageView = binding.imageView
        imageView.background = BitmapDrawable(getResources(), bitmap)


    }


}