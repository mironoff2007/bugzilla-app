package com.mironov.bugzillaapp.domain

sealed class SortBy {
    object STATUS : SortBy()
    object PRODUCT : SortBy()
    object TIME : SortBy()
}
