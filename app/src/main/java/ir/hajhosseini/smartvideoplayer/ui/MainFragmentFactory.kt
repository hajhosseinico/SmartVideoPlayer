package ir.hajhosseini.smartvideoplayer.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import ir.hajhosseini.smartvideoplayer.ui.setting.SettingFragment
import ir.hajhosseini.smartvideoplayer.ui.videolist.VideoListFragment
import javax.inject.Inject

/**
 * MainFragmentFactory
 */

class MainFragmentFactory
@Inject
constructor() : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when (className) {
            VideoListFragment::class.java.name -> {
                VideoListFragment()
            }
            SettingFragment::class.java.name -> {
                SettingFragment()
            }
            else -> super.instantiate(classLoader, className)
        }
    }
}
