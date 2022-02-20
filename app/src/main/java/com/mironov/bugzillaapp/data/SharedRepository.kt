package com.mironov.bugzillaapp.data

interface SharedRepository {
    fun saveFilterOption(filter:String)

    fun getFilterOption():String
}