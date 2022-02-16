package com.mironov.bugzillaapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mironov.bugzillaapp.R
import com.mironov.bugzillaapp.databinding.FragmentBugsListBinding
import com.mironov.bugzillaapp.domain.Status
import com.mironov.bugzillaapp.ui.DetailsFragment.Companion.KEY_BUG
import com.mironov.bugzillaapp.ui.DetailsFragment.Companion.TAG_DETAILS_FRAGMENT
import com.mironov.bugzillaapp.ui.recycler.BugViewHolder
import com.mironov.bugzillaapp.ui.recycler.BugsAdapter

class BugsListFragment : BaseFragment<FragmentBugsListBinding>() {

    companion object {
        const val TAG_BUGS_LIST_FRAGMENT = "TAG_BUGS_LIST_FRAGMENT"
    }

    private lateinit var viewModel: BugListFragmentViewModel

    private var adapter: BugsAdapter?=null

    private var daysBack = 0
    private var daysBackLast = 0

    private var loading = false


    private val listener = object : BugsAdapter.ItemClickListener<BugViewHolder> {
        override fun onClickListener(item: BugViewHolder) {

            val fragment = DetailsFragment()
            val argumentsDetails = Bundle()
            argumentsDetails.putParcelable(KEY_BUG, adapter!!.bugs[item.position])

            fragment.arguments = argumentsDetails

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, TAG_DETAILS_FRAGMENT)
                .addToBackStack(TAG_BUGS_LIST_FRAGMENT)
                .commit()
        }
    }


    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBugsListBinding =
        FragmentBugsListBinding.inflate(inflater, container, false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(this).get(BugListFragmentViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //setupScrollEndListener()

        observe()

        if(adapter!=null){

            val layoutManager = LinearLayoutManager(this.requireContext())
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.adapter = adapter
            binding.recyclerView.addItemDecoration(
                DividerItemDecoration(
                    binding.recyclerView.context,
                    DividerItemDecoration.VERTICAL
                )
            )

            adapter!!.notifyDataSetChanged()
        }
        else{
            adapter = BugsAdapter()
            adapter!!.listener = listener

            val layoutManager = LinearLayoutManager(this.requireContext())
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.adapter = adapter
            binding.recyclerView.addItemDecoration(
                DividerItemDecoration(
                    binding.recyclerView.context,
                    DividerItemDecoration.VERTICAL
                )
            )
           viewModel.getTodayBugs()
        }

    }

    /*private fun search() {
        if(binding.searchField.text.isNotBlank()){
            viewModel.getNews(0)
        }
        else{
            viewModel.searchNews(binding.searchField.text.toString())
        }
    }*/

    private fun setupScrollEndListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && !loading) {


                }
                daysBackLast=daysBack
            }
        })
    }

    private fun observe() {
        viewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Status.DATA -> {
                    loading = false
                    binding.progressBar.visibility = View.GONE
                    adapter!!.bugs.addAll(status.articles!!)
                    adapter!!.notifyDataSetChanged()
                }
                is Status.LOADING -> {
                    loading = true
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Status.ERROR -> {
                    daysBack=daysBackLast
                    loading = false
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), status.message, Toast.LENGTH_LONG).show()
                }
                else -> {
                }
            }
        }
    }


}
