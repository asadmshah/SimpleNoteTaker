package com.asadmshah.simplenotetaker.activities;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.Gravity;

import com.asadmshah.simplenotetaker.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void fab_openNoteDetailUi() {
        String title = "fab_openNoteDetailUi.Title";
        String text = "fab_openNoteDetailUi.Text";

        onView(withId(R.id.fab_add_notes)).perform(click());
        onView(withId(R.id.text_title))
                .check(matches(isDisplayed()))
                .perform(typeText(title));
        onView(withId(R.id.text_note))
                .check(matches(isDisplayed()))
                .perform(typeText(text), pressBack());
    }

    @Test
    public void notesList_verifyAppearsInList() {
        String title = "notesList_verifyAppearsInList.Title";
        String text = "notesList_verifyAppearsInList.Text";

        onView(withId(R.id.fab_add_notes))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.text_title))
                .check(matches(isDisplayed()))
                .perform(typeText(title));
        onView(withId(R.id.text_note))
                .check(matches(isDisplayed()))
                .perform(typeText(text));

        Espresso.closeSoftKeyboard();
        Espresso.pressBack();

        onView(withId(R.id.notes_list))
                .check(matches(hasDescendant(withText(title))))
                .check(matches(hasDescendant(withText(text))));
    }

    @Test
    public void notesList_updatesOccur() {
        String initialText = "notesList_updatesOccur.Initial";
        String updatedText = initialText + ".Updated";

        onView(withId(R.id.fab_add_notes))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.text_title))
                .check(matches(isDisplayed()))
                .perform(typeText(initialText));

        onView(withText(initialText))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.text_title))
                .check(matches(isDisplayed()))
                .perform(clearText(), typeText(updatedText), closeSoftKeyboard(), pressBack());

        onView(withText(initialText))
                .check(doesNotExist());

        onView(withText(updatedText))
                .check(matches(isDisplayed()));
    }

    @Test
    public void navigationDrawer_insertingNewTagUpdatesDrawer() {
        String tagLabel = "navigationDrawer_insertingNewTagUpdatesDrawer";

        onView(withId(R.id.fab_add_notes))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.action_tag))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.create_tag_label))
                .check(matches(isDisplayed()))
                .perform(typeText(tagLabel), closeSoftKeyboard());
        onView(withId(R.id.colors_list))
                .check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.create_tag_button))
                .check(matches(isDisplayed()))
                .perform(click());

        Espresso.pressBack();
        Espresso.pressBack();

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.START)))
                .perform(open());

        onView(withId(R.id.navigation_list))
                .check(matches(isDisplayed()))
                .check(matches(hasDescendant(withText(tagLabel))));
    }

    @Test
    public void navigationDrawer_selectTagLoadsNotesOfTag() {
        String expectNoteTitle = "navigationDrawer_selectTagLoadsNotesOfTag.ExpectNoteTitle";
        String randomNoteTitle = "navigationDrawer_selectTagLoadsNotesOfTag.RandomNoteTitle";

        String tagLabel = "navigationDrawer_selectTagLoadsNotesOfTag.TagName";

        onView(withId(R.id.fab_add_notes))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.text_title))
                .check(matches(isDisplayed()))
                .perform(typeText(expectNoteTitle), closeSoftKeyboard());
        onView(withId(R.id.action_tag))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.create_tag_label))
                .check(matches(isDisplayed()))
                .perform(typeText(tagLabel), closeSoftKeyboard());
        onView(withId(R.id.colors_list))
                .check(matches(isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.create_tag_button))
                .check(matches(isDisplayed()))
                .perform(click());

        Espresso.pressBack();
        Espresso.pressBack();

        onView(withId(R.id.fab_add_notes))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.text_title))
                .check(matches(isDisplayed()))
                .perform(typeText(randomNoteTitle), closeSoftKeyboard());

        Espresso.pressBack();

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.START)))
                .perform(open());

        onView(withText(tagLabel))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.START)));

        onView(withId(R.id.notes_list))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText(expectNoteTitle))
                .check(matches(isDisplayed()));

        onView(withText(randomNoteTitle))
                .check(doesNotExist());
    }

}
