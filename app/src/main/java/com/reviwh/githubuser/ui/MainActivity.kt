package com.reviwh.githubuser.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.reviwh.githubuser.R
import com.reviwh.githubuser.adapter.UserListAdapter
import com.reviwh.githubuser.data.remote.response.ItemsItem
import com.reviwh.githubuser.databinding.ActivityMainBinding
import com.reviwh.githubuser.unit.MainViewModel
import com.reviwh.githubuser.unit.MainViewModelFactory
import com.reviwh.githubuser.utils.SettingPreferences

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private var isDarkMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.user.observe(this) { user ->
            setUserData(user)
        }

        mainViewModel.getThemeSetting().observe(this) {
            setAppTheme(it)
            isDarkMode = it
        }

        binding.materialToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_favorite -> {
                    val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.action_theme -> {
                    mainViewModel.saveThemeSetting(!isDarkMode)
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        mainViewModel.isLoading.observe(this) { showLoading(it) }
        mainViewModel.isFound.observe(this) { showUserNotFound(it) }

        mainViewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                showToast(errorMessage)
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.text = searchView.text
                searchView.hide()
                if (searchBar.text.isNullOrEmpty()) mainViewModel.getAllUser()
                else mainViewModel.findUser(searchView.text.toString())
                false
            }
        }
    }

    private fun showSelectedUser(username: String) {
        val intent = Intent(this@MainActivity, UserDetailsActivity::class.java)
        intent.putExtra(UserDetailsActivity.USERNAME, username)
        startActivity(intent)
    }

    private fun setUserData(users: List<ItemsItem?>?) {
        val adapter = UserListAdapter()
        adapter.submitList(users)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showSelectedUser(data.login.toString())
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showUserNotFound(isFound: Boolean) {
        binding.tvUserNotFound.visibility = if (isFound) View.GONE else View.VISIBLE
    }

    private fun setAppTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun showToast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}