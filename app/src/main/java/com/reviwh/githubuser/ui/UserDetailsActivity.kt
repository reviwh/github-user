package com.reviwh.githubuser.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.reviwh.githubuser.R
import com.reviwh.githubuser.adapter.SectionsPagerAdapter
import com.reviwh.githubuser.data.local.entity.UserEntity
import com.reviwh.githubuser.data.remote.response.UserDetailResponse
import com.reviwh.githubuser.databinding.ActivityUserDetailsBinding
import com.reviwh.githubuser.loadImage
import com.reviwh.githubuser.unit.MainViewModelFactory
import com.reviwh.githubuser.unit.SettingsViewModel
import com.reviwh.githubuser.unit.UserDetailsViewModel
import com.reviwh.githubuser.unit.ViewModelFactory
import com.reviwh.githubuser.utils.SettingPreferences

class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailsBinding
    private lateinit var userEntity: UserEntity
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private var isDarkMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(USERNAME).toString()
        val userDetailsViewModel by viewModels<UserDetailsViewModel> {
            ViewModelFactory.getInstance(this)
        }
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        userDetailsViewModel.getUserData(username)
        userDetailsViewModel.userDetailResponse.observe(this) { userDetailResponse ->
            setUserData(userDetailResponse)
            userEntity = UserEntity(
                userDetailResponse.login.toString(), userDetailResponse.avatarUrl
            )
        }

        userDetailsViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        userDetailsViewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                showToast(errorMessage)
            }
        }

        checkIsFavorite(userDetailsViewModel.getFavoriteUserByUsername(username)) { isFavorite ->
            setFavoriteIcon(isFavorite)
            binding.fbFavorite.setOnClickListener {
                if (isFavorite) {
                    userDetailsViewModel.removeUserFromFavorite(username)
                    showToast(getString(R.string.removed_to_favorite, username))
                } else {
                    userDetailsViewModel.addUserToFavorite(userEntity)
                    showToast(getString(R.string.added_to_favorite, username))
                }
            }
        }

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingsViewModel =
            ViewModelProvider(this, MainViewModelFactory(pref))[SettingsViewModel::class.java]
        settingsViewModel.getThemeSetting().observe(this) {
            setAppTheme(it)
            isDarkMode = it
        }
        binding.materialToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_theme -> {
                    settingsViewModel.saveThemeSetting(!isDarkMode)
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }
        }
        binding.materialToolbar.setNavigationOnClickListener { finish() }
    }

    private fun setUserData(user: UserDetailResponse) {
        binding.apply {
            imgAvatar.loadImage(user.avatarUrl)
            tvName.text = user.name
            tvUsername.text = user.login
            tvFollowers.text = getString(R.string.sum_followers, user.followers)
            tvFollowing.text = getString(R.string.sum_following, user.following)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbUserDetails.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

    private fun setFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) binding.fbFavorite.setImageResource(R.drawable.round_favorite_24)
        else binding.fbFavorite.setImageResource(R.drawable.round_favorite_border_24)
    }

    private fun checkIsFavorite(userEntity: LiveData<UserEntity>, callback: (Boolean) -> Unit) {
        userEntity.observe(this) {
            callback(it != null)
        }
    }

    private fun setAppTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    companion object {
        const val USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1, R.string.tab_text_2
        )
    }

}