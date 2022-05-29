package ir.hajhosseini.smartvideoplayer.ui

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.hajhosseini.smartvideoplayer.R
import ir.hajhosseini.smartvideoplayer.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbarLayout.title = getString(R.string.home_title)
        binding.toolbarLayout.expandedTitleGravity = Gravity.CENTER
        binding.toolbarLayout.collapsedTitleGravity = Gravity.CENTER
        binding.toolbarLayout.setCollapsedTitleTextColor(resources.getColor(R.color.black, null))
        binding.toolbarLayout.setExpandedTitleColor(resources.getColor(R.color.black, null))
        binding.toolbarLayout.setExpandedTitleTypeface(
            Typeface.create(
                binding.toolbarLayout.expandedTitleTypeface,
                Typeface.BOLD
            )
        )
        binding.toolbarLayout.setCollapsedTitleTypeface(
            Typeface.create(
                binding.toolbarLayout.expandedTitleTypeface,
                Typeface.BOLD
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController(R.id.mainFragmentContainer).navigate(
                    R.id.settingFragment
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setToolbarTitle(title: String) {
        binding.toolbarLayout.title = title
    }
}