package com.mironov.bugzillaapp.ui.recycler

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.mironov.bugzillaapp.R
import com.mironov.bugzillaapp.databinding.ItemBugBinding
import com.mironov.bugzillaapp.domain.Bug

class BugViewHolder(
    private val binding: ItemBugBinding,
) : RecyclerView.ViewHolder(binding.root){
    @SuppressLint("SetTextI18n")
    fun bind(
        bug: Bug,
        listener: BugsAdapter.ItemClickListener<BugViewHolder>,
        position: Int
    ){
        with(binding) {
            val  context=root.context
            index.text= (position+1).toString()
            product.text=context.resources.getString(R.string.product)+bug!!.product
            os.text=context.resources.getString(R.string.os)+bug.opSys
            classification.text=context.resources.getString(R.string.classification)+bug.classification
            status.text=context.resources.getString(R.string.status)+bug.status
        }

        binding.root.setOnClickListener{listener.onClickListener(this)}
    }
}
