package com.example.hydrosmart.beforelogin

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.hydrosmart.R
import com.example.hydrosmart.auth.login.LoginActivity
import com.example.hydrosmart.auth.signup.Signup
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class WelcomeTest {

    @get:Rule
    val activity = ActivityScenarioRule(Welcome::class.java)

    @Test
    fun testToLoginScenario() {
        Intents.init()

        onView(withId(R.id.bt_login)).perform(click())
        intended(hasComponent(LoginActivity::class.java.name))
        onView(withText(R.string.title_login_page)).check(matches(isDisplayed()))
        pressBack()
    }

    @Test
    fun testToSignupScenario() {

        onView(withId(R.id.bt_signup)).perform(click())
        intended(hasComponent(Signup::class.java.name))
        onView(withText(R.string.title_signup_page)).check(matches(isDisplayed()))
        pressBack()
    }
}