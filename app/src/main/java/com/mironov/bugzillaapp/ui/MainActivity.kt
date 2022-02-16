package com.mironov.bugzillaapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mironov.bugzillaapp.R
import com.mironov.bugzillaapp.ui.BugsListFragment.Companion.TAG_BUGS_LIST_FRAGMENT

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        //Debug.waitForDebugger()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    BugsListFragment(), TAG_BUGS_LIST_FRAGMENT
                )
                .commit()

        }
    }

}

