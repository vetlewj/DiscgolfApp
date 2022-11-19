package no.hiof.discgolfapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.model.Disc

class DiscRecyclerAdapter : ListAdapter<Disc, RecyclerView.ViewHolder>(DiscDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_disc_list_item, parent, false)
        Log.d("onCreateViewHolder", "Creating view")
        return DiscViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int){
        val currentDisc = getItem(position)
        Log.d("onBindViewHolder", "Binding position $position")
        (holder as DiscViewHolder).bind(currentDisc)
    }


    class DiscViewHolder (view: View) : RecyclerView.ViewHolder(view){
        private val discNameTextView : TextView = view.findViewById(R.id.discNameTextView)
        private val discSpeedTextView : TextView = view.findViewById(R.id.discSpeedTextView)
        private val discGlideTextView : TextView = view.findViewById(R.id.discGlideTextView)
        private val discTurnTextView : TextView = view.findViewById(R.id.discTurnTextView)
        private val discFadeTextView : TextView = view.findViewById(R.id.discFadeTextView)
        private val discTypeTextView : TextView = view.findViewById(R.id.discTypeTextView)

        fun bind(disc: Disc){
            discNameTextView.text = disc.name
            discSpeedTextView.text = disc.speed.toString()
            discGlideTextView.text = disc.glide.toString()
            discTurnTextView.text = disc.turn.toString()
            discFadeTextView.text = disc.fade.toString()
            discTypeTextView.text = disc.type.toString()
        }
    }

    private class DiscDiffCallback : DiffUtil.ItemCallback<Disc>(){
        override fun areItemsTheSame(oldItem: Disc, newItem: Disc): Boolean {
            Log.d("Same item disc?", "oldItem.name = ${oldItem.name}")
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Disc, newItem: Disc): Boolean {
            Log.d("Same content disc?", "oldItem.name = $oldItem")
            return oldItem == newItem
        }
    }

}
