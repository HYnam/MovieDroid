package com.pec_acm.moviedroid.mainpage.list


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.pec_acm.moviedroid.databinding.FragmentCompletedListBinding
import com.pec_acm.moviedroid.firebase.ListItem

class CompletedListFragment : Fragment() {
    private var _binding: FragmentCompletedListBinding? = null
    private val binding get() = _binding!!
    private var listViewModel: ListViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedListBinding.inflate(inflater, container, false)
        val view: View = binding!!.root
        val completedList = binding!!.completedList
        listViewModel = ViewModelProvider(this)[ListViewModel::class.java]
        listViewModel!!.getUser(FirebaseAuth.getInstance().uid!!)
        val listAdapter = ListAdapter(requireContext(), listViewModel!!, this)
        completedList.adapter = listAdapter
        listViewModel!!.user.observe(viewLifecycleOwner, Observer { user ->
            if (user == null) return@Observer
            val itemList = ArrayList<ListItem>()
            for (i in user.userList.indices) {
                val listItem = user.userList[i]
                if (listItem.status == 2) itemList.add(listItem)
            }
            itemList.sortBy { it.name }
            if(itemList.isEmpty()) {
                binding.lottieAnimation.visibility = View.VISIBLE
                binding.noEntriesText.visibility = View.VISIBLE
            }
            else {
                binding.lottieAnimation.visibility = View.GONE
                binding.noEntriesText.visibility = View.GONE
            }
            listAdapter.setItemList(itemList)
        })
        return view
    }
}