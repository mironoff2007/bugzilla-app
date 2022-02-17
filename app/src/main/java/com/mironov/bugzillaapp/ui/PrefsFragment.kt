package com.mironov.bugzillaapp.ui

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

        val array= resources.getStringArray(R.array.osFilter)
        val adapter: ArrayAdapter<*> = ArrayAdapter<String>(requireContext(), R.layout.spinner_item,array)
        adapter.setDropDownViewResource(R.layout.spinner_item)

        val filterOption=repository.getFilterOption()

        binding.osSpinner.adapter = adapter

        binding.osSpinner.setSelection(array.indexOf(filterOption))

        binding.osSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                if(i==0){
                    saveFilterOption("")
                }
                else{
                   saveFilterOption(array[i])
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    //в идеале нужно это делать асинхронно и через вьюмодель
    fun saveFilterOption(filter:String){
        repository.saveFilterOption(filter)
    }

}
