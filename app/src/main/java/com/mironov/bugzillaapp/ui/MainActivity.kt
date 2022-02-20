package com.mironov.bugzillaapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.mironov.bugzillaapp.R
import com.mironov.bugzillaapp.appComponent
import com.mironov.bugzillaapp.ui.screens.BugsListFragment.Companion.TAG_BUGS_LIST_FRAGMENT
import com.mironov.bugzillaapp.ui.screens.PrefsFragment.Companion.TAG_PREFS_FRAGMENT
import com.mironov.bugzillaapp.ui.screens.BugsListFragment
import com.mironov.bugzillaapp.ui.screens.PrefsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        //Debug.waitForDebugger()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appComponent.inject(this)

        val checkNewBugsService = Intent(this, CheckNewBugsService::class.java)

        startService(checkNewBugsService)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    BugsListFragment(), TAG_BUGS_LIST_FRAGMENT
                )
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_set_filter -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, PrefsFragment(), TAG_PREFS_FRAGMENT)
                    .addToBackStack(TAG_BUGS_LIST_FRAGMENT)
                    .commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

