package com.mironov.bugzillaapp.ui.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mironov.bugzillaapp.databinding.ItemBugBinding
import com.mironov.bugzillaapp.domain.Bug

class BugsAdapter() : RecyclerView.Adapter<BugViewHolder>() {

    lateinit var listener: ItemClickListener<BugViewHolder>

    private var _binding: ItemBugBinding? = null

    private val binding get() = _binding!!

    var bugs: ArrayList<Bug> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BugViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBugBinding.inflate(inflater, parent, false)
        return BugViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BugViewHolder, position: Int) {
        holder.bind(bugs[position],listener,position)
    }

    override fun getItemCount(): Int {
        return bugs.size
    }

    interface ItemClickListener<I> : View.OnClickListener {
        fun onClickListener(item: BugViewHolder) {
        }

        override fun onClick(v: View?) {}
    }

}