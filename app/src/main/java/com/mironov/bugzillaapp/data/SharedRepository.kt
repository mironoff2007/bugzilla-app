package com.mironov.bugzillaapp.data

import com.mironov.bugzillaapp.domain.SortBy

interface SharedRepository {
    fun saveFilterOption(filter:String)

    fun getFilterOption():String

    fun saveSortOption(sortOption: SortBy)

    fun getSortOption(): SortBy
}