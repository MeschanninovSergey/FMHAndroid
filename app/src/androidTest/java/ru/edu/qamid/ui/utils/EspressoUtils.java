package ru.edu.qamid.ui.utils;

import android.view.View;
import android.view.ViewGroup;

import org.hamcrest.Matcher;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;

public final class EspressoUtils {
    private EspressoUtils() {}

    public static void waitForView(Matcher<View> matcher, long timeoutMillis) {
        long endTime = System.currentTimeMillis() + timeoutMillis;
        Throwable lastError = null;

        while (System.currentTimeMillis() < endTime) {
            try {
                Espresso.onView(matcher).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
                return;
            } catch (NoMatchingViewException | AssertionError e) {
                lastError = e;
            }
            Espresso.onIdle();
        }
        throw new AssertionError("View " + matcher + " не появился за " + timeoutMillis + " мс", lastError);
    }

    public static void waitForView(Matcher<View> matcher) {
        waitForView(matcher, 10000);
    }

    public static ViewAction clickOnChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(ViewGroup.class);
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id: " + id;
            }

            @Override
            public void perform(UiController uiController, View view) {
                View child = view.findViewById(id);
                if (child != null) {
                    child.performClick();
                } else {
                    throw new RuntimeException("Child view with id " + id + " not found!");
                }
            }
        };
    }
}