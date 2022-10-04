package no.hiof.discgolfapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.model.Disc

class DiscRecyclerAdapter(private val discs:List<Disc>) : RecycleView.Adapter<DiscRecycleAdapter.DiscViewHolder>(){

    override fun onCreateViewHolder(parent: Viewgroup, viewType:Int): DiscViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.disc_list_item, parent, false)

        return DiscViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DiscViewHolder, position: int){

        val currentDisc = discs[position]

        holder.bind(currentDisc)
    }

    override fun getItemCount(): Int{
        return discs.size
    }

    class DiscViewHolder (view: View) : RecyclerView.ViewHolder(view){
        private val discNameTextView : TextView = view.findViewById(R.id.discNameTextView)

        fun bind(disc: Disc){
            discNameTextView.text = disc.name
        }
    }


}
