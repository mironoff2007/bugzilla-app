package com.mironov.bugzillaapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.mironov.bugzillaapp.databinding.FragmentBugsListBinding
import com.mironov.newsapp.ui.screens.BaseFragment
import javax.inject.Inject

class BugsListFragment : BaseFragment<FragmentBugsListBinding>() {

    companion object {
        const val TAG_BUGS_LIST_FRAGMENT = "TAG_BUGS_LIST_FRAGMENT"
    }

    private lateinit var viewModel: FragmentBugsListBinding

    private var adapter: BugsAdapter?=null

    private var daysBack = 0
    private var daysBackLast = 0

    private var loading = false


    private val listener = object : BugsAdapter.ItemClickListener<ArticleViewHolder> {
        override fun onClickListener(item: ArticleViewHolder) {

            val fragment = DetailsFragment()
            val argumentsDetails = Bundle()
            argumentsDetails.putParcelable(KEY_ARTICLE, adapter!!.articles[item.position])

            fragment.arguments = argumentsDetails

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, TAG_DETAILS_FRAGMENT)
                .addToBackStack(TAG_NEWS_LIST_FRAGMENT)
                .commit()
        }
    }


    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewsListBinding =
        FragmentNewsListBinding.inflate(inflater, container, false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            requireContext().appComponent.factory.create(NewsListFragmentViewModel::class.java)

        setupScrollEndListener()

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
            //viewModel.getBugs
        }

        binding.searchButton.setOnClickListener { search() }
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
                    loading = true
                    daysBack++
                    viewModel.getNews(daysBack)

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
                    adapter!!.articles.addAll(status.articles!!)
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
