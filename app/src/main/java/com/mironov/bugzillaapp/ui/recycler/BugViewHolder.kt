package com.mironov.bugzillaapp.ui.recycler

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.mironov.bugzillaapp.databinding.ItemBugBinding
import com.mironov.bugzillaapp.domain.Bug

class BugViewHolder(
    private val binding: ItemBugBinding,
) : RecyclerView.ViewHolder(binding.root){
    @SuppressLint("SetTextI18n")
    fun bind(bug: Bug,
             listener: BugsAdapter.ItemClickListener<BugViewHolder>
    ){
        with(binding) {
            product.text=bug.product
            os.text=bug.opSys
            classification.text=bug.classification
            status.text=bug.status
        }

        binding.root.setOnClickListener{listener.onClickListener(this)}
    }
}
