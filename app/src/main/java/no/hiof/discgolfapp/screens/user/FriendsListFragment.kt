package no.hiof.discgolfapp.screens.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import no.hiof.discgolfapp.R
import no.hiof.discgolfapp.adapter.FriendsListRecyclerAdapter
import no.hiof.discgolfapp.databinding.FragmentAcceptFriendBinding
import no.hiof.discgolfapp.databinding.FragmentFriendsListBinding
import no.hiof.discgolfapp.model.User


class FriendsListFragment : Fragment() {

    private var fragmentBinding: FragmentFriendsListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentFriendsListBinding.bind(view)
        fragmentBinding = binding
        
        
        binding.friendsListRecyclerView.adapter = FriendsListRecyclerAdapter (User.getUsers())  {
            Toast.makeText(context, "you clicked on a friend", Toast.LENGTH_SHORT ).show()
        }

        binding.friendsListRecyclerView.layoutManager = GridLayoutManager(view.context, 1)
    }


}