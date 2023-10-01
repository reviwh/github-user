package com.reviwh.githubuser.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.reviwh.githubuser.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun changeTheme() {
        onView(withId(R.id.action_theme)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.action_theme)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.action_theme)).perform(click())
    }

    @Test
    fun navigationToFavorite() {
        onView(withId(R.id.action_favorite)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.action_favorite)).perform(click())
        pressBack()
    }
}