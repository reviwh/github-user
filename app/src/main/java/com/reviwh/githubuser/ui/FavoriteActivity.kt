package com.reviwh.githubuser.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.reviwh.githubuser.R
import com.reviwh.githubuser.adapter.FavoriteAdapter
import com.reviwh.githubuser.data.local.entity.UserEntity
import com.reviwh.githubuser.databinding.ActivityFavoriteBinding
import com.reviwh.githubuser.unit.FavoriteViewModel
import com.reviwh.githubuser.unit.MainViewModelFactory
import com.reviwh.githubuser.unit.SettingsViewModel
import com.reviwh.githubuser.unit.ViewModelFactory
import com.reviwh.githubuser.utils.SettingPreferences

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private var isDarkMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavorite.addItemDecoration(itemDecoration)

        val favoriteViewModel by viewModels<FavoriteViewModel> {
            ViewModelFactory.getInstance(this)
        }

        favoriteViewModel.getFavoriteUsers().observe(this) { users ->
            showLoading(true)
            setUserData(users)
            showNoFavorite(users.isNotEmpty())
            showLoading(false)
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

    private fun showSelectedUser(username: String) {
        val intent = Intent(this@FavoriteActivity, UserDetailsActivity::class.java)
        intent.putExtra(UserDetailsActivity.USERNAME, username)
        startActivity(intent)
    }

    private fun setUserData(userEntities: List<UserEntity>) {
        val adapter = FavoriteAdapter()
        adapter.submitList(userEntities)
        binding.rvFavorite.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserEntity) {
                showSelectedUser(data.username)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbFavorite.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showNoFavorite(isFound: Boolean) {
        binding.tvNoFavorite.visibility = if (isFound) View.GONE else View.VISIBLE
    }

    private fun setAppTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}