package com.pixelma.calculator.Activities;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.pixelma.calculator.R;

@RunWith(AndroidJUnit4.class)
public class GameActivityTest {

    @Rule
    public ActivityScenarioRule<GameActivity> activityRule =
            new ActivityScenarioRule<>(GameActivity.class);

    @Test
    public void test_allViewsAreDisplayed() {
        // Vérifier que le minuteur et les scores sont affichés
        onView(withId(R.id.timer)).check(matches(isDisplayed()));
        onView(withId(R.id.right_answer)).check(matches(isDisplayed()));
        onView(withId(R.id.wrong_answer)).check(matches(isDisplayed()));

        // Vérifier que les vues de calcul sont affichées
        onView(withId(R.id.tv_digit_1)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_operator)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_digit_2)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_equal)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_result)).check(matches(isDisplayed()));

        // Vérifier que les boutons du pavé numérique sont affichés
        onView(withId(R.id.btn_0)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_1)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_2)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_3)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_4)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_5)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_6)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_7)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_8)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_9)).check(matches(isDisplayed()));

        // Vérifier que les boutons d'action sont affichés
        onView(withId(R.id.btn_erase)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_valid)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_pause)).check(matches(isDisplayed()));
    }
}
