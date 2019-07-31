package in.anees.petapp;


import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.test.espresso.FailureHandler;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import in.anees.petapp.ui.fragment.AlertDialogFragment;
import in.anees.petapp.ui.fragment.PetListFragment;
import in.anees.petapp.utils.NetworkUtils;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.*;

/**
 * Created by Anees Thyrantakath on 2/17/19.
 * Run a simple test to check application's normal working flow.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    private MainActivity mMainActivity = null;
    private static final String TAG = "MainActivityTest";

    @Before
    public void setUp() throws Exception {
        mMainActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void launchActivityTest() {
        // Check whether activity is launched
        View view = mMainActivity.findViewById(R.id.main_container);

        assertNotNull(view);
        Log.d(TAG, "Activity launched!");
        FragmentManager fragmentManager = mMainActivity.getSupportFragmentManager();

        if (NetworkUtils.isNetworkConnected(mMainActivity)) {
            Log.d(TAG, "Internet is connected!");
            PetListFragment petListFragment = (PetListFragment) fragmentManager
                    .findFragmentByTag(PetListFragment.TAG_PET_LIST);
            Log.d(TAG, "PetListFragment instance is null? : " +(petListFragment == null));

            //Each button scenarios needs to be tested separately

            // Call button check
            final boolean[] buttonCallDisplayed = {true};
            onView(withId(R.id.buttonCall)).withFailureHandler(new FailureHandler() {
                @Override
                public void handle(Throwable error, Matcher<View> viewMatcher){
                    buttonCallDisplayed[0] = false;
                    Log.d(TAG, "Call feature is not enabled!");
                }
            }).check(matches(isDisplayed()));
            if (buttonCallDisplayed[0]) {
                onView(withId(R.id.buttonCall)).perform(click());
                onView(withText(R.string.ok)).perform(click());
            }

            // Chat button check
            final boolean[] buttonChatDisplayed = {true};
            onView(withId(R.id.buttonChat)).withFailureHandler(new FailureHandler() {
                @Override
                public void handle(Throwable error, Matcher<View> viewMatcher){
                    buttonChatDisplayed[0] = false;
                    Log.d(TAG, "Chat feature is not enabled!");
                }
            }).check(matches(isDisplayed()));
            if (buttonChatDisplayed[0]) {
                onView(withId(R.id.buttonChat)).perform(click());
                onView(withText(R.string.ok)).perform(click());
            }

            // Working hours should be displayed
            onView(withId(R.id.tvWorkingHours)).check(matches((isDisplayed())));

            // Click on listView check if network present, and WebView displayed
            onData(anything()).inAdapterView(withId(R.id.listViewPets)).atPosition(0).perform(click());
            if (NetworkUtils.isNetworkConnected(mMainActivity)) {
                onView(withId(R.id.webViewPetDetails)).check(matches((isDisplayed())));
            }
        } else {
            Log.d(TAG, "Internet is not connected!");
            AlertDialogFragment alertDialogFragment = (AlertDialogFragment) fragmentManager
                    .findFragmentByTag(AlertDialogFragment.TAG_ALERT_DIALOG);
            Log.d(TAG, "AlertDialogFragment instance is null? : " +(alertDialogFragment == null));
            assertNotNull(alertDialogFragment);
            onView(withText(R.string.no_network)).check(matches(isDisplayed()));
        }
    }

    @After
    public void tearDown() throws Exception {
        mMainActivity = null;
    }
}