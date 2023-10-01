package com.reviwh.githubuser.unit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.reviwh.githubuser.utils.SettingPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSetting(): LiveData<Boolean> = pref.getThemeSetting().asLiveData()
    fun saveThemeSetting(isDarkMode: Boolean){
        viewModelScope.launch { pref.saveThemeSetting(isDarkMode) }
    }
}