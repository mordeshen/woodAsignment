package com.mor.homeassignmenteat.presentation

interface UICommunicationListener {

    fun displayProgressBar(isLoading: Boolean)

    fun hideSoftKeyboard()

    fun isStoragePermissionGranted(): Boolean

//example to expand
//    fun expandAppBar()
}