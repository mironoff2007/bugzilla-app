package com.mironov.bugzillaapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mironov.bugzillaapp.R
import com.mironov.bugzillaapp.databinding.FragmentDetailsBinding
import com.mironov.bugzillaapp.domain.Bug

class DetailsFragment : BaseFragment<FragmentDetailsBinding>() {

    companion object {
        const val TAG_DETAILS_FRAGMENT = "TAG_DETAILS_FRAGMENT"
        const val KEY_BUG = "KEY_BUG"
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsBinding =
        FragmentDetailsBinding.inflate(inflater, container, false)

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bug = requireArguments().getParcelable<Bug>(KEY_BUG)

        with(binding) {
            id.text = resources.getString(R.string.id) + bug!!.id
            product.text = resources.getString(R.string.product) + bug!!.product
            os.text = resources.getString(R.string.os) + bug.opSys
            classification.text = resources.getString(R.string.classification) + bug.classification
            status.text = resources.getString(R.string.status) + bug.status
            summary.text = resources.getString(R.string.summary) + bug.summary
            time.text = resources.getString(R.string.time) + bug.creationTime
        }
    }

}