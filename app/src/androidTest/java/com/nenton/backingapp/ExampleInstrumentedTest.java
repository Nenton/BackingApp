package com.nenton.backingapp;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.nenton.backingapp.ui.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.nenton.backingapp.ui.activities.MainActivity.TAG_FRAGMENT_DETAILS;
import static com.nenton.backingapp.ui.activities.MainActivity.TAG_FRAGMENT_INGREDIENTS;
import static com.nenton.backingapp.ui.activities.MainActivity.TAG_FRAGMENT_RECIPE;
import static com.nenton.backingapp.ui.activities.MainActivity.TAG_FRAGMENT_STEP;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    FragmentManager mFragmentManager;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        if (mFragmentManager == null) {
            mFragmentManager = mActivityTestRule.getActivity().getSupportFragmentManager();
        }
    }

    @Test
    public void clickOnRecipeTest() {
        Espresso.onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Fragment fragment = mFragmentManager.findFragmentByTag(TAG_FRAGMENT_DETAILS);
        assertNotNull(fragment);
    }

    @Test
    public void clickOnIngredientsTest() {
        Espresso.onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Espresso.onView(withId(R.id.details_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Fragment fragment = mFragmentManager.findFragmentByTag(TAG_FRAGMENT_INGREDIENTS);
        assertNotNull(fragment);
    }

    @Test
    public void clickOnStepTest() {
        Espresso.onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Espresso.onView(withId(R.id.details_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        Fragment fragment = mFragmentManager.findFragmentByTag(TAG_FRAGMENT_STEP);
        assertNotNull(fragment);
    }

    @Test
    public void rotateScreenTest() {
        mActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Espresso.onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        mFragmentManager = mActivityTestRule.getActivity().getSupportFragmentManager();
        Fragment fragment = mFragmentManager.findFragmentByTag(TAG_FRAGMENT_DETAILS);
        assertNotNull(fragment);
    }

    @Test
    public void backBtnTest(){
        Espresso.onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Espresso.onView(withId(R.id.details_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        Espresso.onView(isRoot()).perform(pressBack());
        Espresso.onView(isRoot()).perform(pressBack());
        Fragment fragmentRoot = mFragmentManager.findFragmentByTag(TAG_FRAGMENT_RECIPE);
        Fragment fragmentStep = mFragmentManager.findFragmentByTag(TAG_FRAGMENT_STEP);
        Fragment fragmentDetail = mFragmentManager.findFragmentByTag(TAG_FRAGMENT_DETAILS);
        Fragment fragmentIngredients = mFragmentManager.findFragmentByTag(TAG_FRAGMENT_INGREDIENTS);
        assertNull(fragmentStep);
        assertNull(fragmentDetail);
        assertNull(fragmentIngredients);
        assertNotNull(fragmentRoot);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.nenton.backingapp", appContext.getPackageName());
    }
}
