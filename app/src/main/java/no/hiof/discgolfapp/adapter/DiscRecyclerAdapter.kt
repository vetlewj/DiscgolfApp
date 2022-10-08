package no.hiof.discgolfapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.model.Disc

class DiscRecyclerAdapter(private val discs:List<Disc>) : RecyclerView.Adapter<DiscRecyclerAdapter.DiscViewHolder>(){
//RecycleView.Adapter<DiscRecycleAdapter.DiscViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): DiscViewHolder {
    //override fun onCreateViewHolder(parent: View)

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.disc_list_item, parent, false)

        return DiscViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DiscViewHolder, position: Int){

        val currentDisc = discs[position]

        holder.bind(currentDisc)
    }

    override fun getItemCount(): Int{
        return discs.size
    }

    class DiscViewHolder (view: View) : RecyclerView.ViewHolder(view){
        private val discNameTextView : TextView = view.findViewById(R.id.discNameTextView)
        private val discSpeedTextView : TextView = view.findViewById(R.id.discSpeedTextView)
        private val discGlideTextView : TextView = view.findViewById(R.id.discGlideTextView)
        private val discTurnTextView : TextView = view.findViewById(R.id.discTurnTextView)
        private val discFadeTextView : TextView = view.findViewById(R.id.discFadeTextView)


        fun bind(disc: Disc){
            discNameTextView.text = disc.name
            discSpeedTextView.text = disc.speed.toString()
            discGlideTextView.text = disc.glide.toString()
            discTurnTextView.text = disc.turn.toString()
            discFadeTextView.text = disc.fade.toString()
        }
    }


}
