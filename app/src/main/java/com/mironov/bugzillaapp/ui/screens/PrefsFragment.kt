package com.mironov.bugzillaapp.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.mironov.bugzillaapp.R
import com.mironov.bugzillaapp.appComponent
import com.mironov.bugzillaapp.data.Repository
import com.mironov.bugzillaapp.databinding.FragmentPrefsBinding
import com.mironov.bugzillaapp.domain.SortBy
import com.mironov.bugzillaapp.ui.BaseFragment
import javax.inject.Inject

class PrefsFragment : BaseFragment<FragmentPrefsBinding>() {

    companion object {
        const val TAG_PREFS_FRAGMENT = "TAG_PREFS_FRAGMENT"
    }

    @Inject
    protected lateinit var repository: Repository

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPrefsBinding =
        FragmentPrefsBinding.inflate(inflater, container, false)

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSpinner()
    }

    @SuppressLint("ResourceType")
    private fun initSpinner() {

        val osArray = resources.getStringArray(R.array.osFilter)
        val osAdapter: ArrayAdapter<*> =
            ArrayAdapter(requireContext(), R.layout.spinner_item, osArray)
        osAdapter.setDropDownViewResource(R.layout.spinner_item)

        val filterOption = repository.getFilterOption()

        binding.osSpinner.adapter = osAdapter

        var oSid = osArray.indexOf(filterOption)

        if (oSid < 0) {
            oSid = 0
        }

        binding.osSpinner.setSelection(oSid)

        binding.osSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                if (i == 0) {
                    saveFilterOption("")
                } else {
                    saveFilterOption(osArray[i])
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        val sortArray = resources.getStringArray(R.array.sortBy)
        val sortAdapter: ArrayAdapter<*> =
            ArrayAdapter(requireContext(), R.layout.spinner_item, sortArray)
        sortAdapter.setDropDownViewResource(R.layout.spinner_item)

        var sortBy = repository.getSortOption()

        val id = when (sortBy) {
            SortBy.STATUS -> 0
            SortBy.PRODUCT -> 1
            SortBy.TIME -> 2
        }

        binding.sortSpinner.adapter = sortAdapter

        binding.sortSpinner.setSelection(id)

        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                when (i) {
                    0 -> sortBy = SortBy.STATUS
                    1 -> sortBy = SortBy.PRODUCT
                    2 -> sortBy = SortBy.TIME
                    else -> sortBy = SortBy.TIME
                }
                saveSortOption(sortBy)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }


    fun saveFilterOption(filter: String) {
        repository.saveFilterOption(filter)
    }

    fun saveSortOption(sortOption: SortBy) {
        repository.saveSortOption(sortOption)
    }
}
