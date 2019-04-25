package alpitsolutions.com.bakingmadeeasy;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class UI_ActivitiesTest {

    @Rule
    public ActivityTestRule<RecipesOverviewActivity> mActivityTestRule = new ActivityTestRule<>(RecipesOverviewActivity.class);

    public static final String RECIPE_NAME_POS_0 = "Nutella Pie";
    public static final String RECIPE_NAME_POS_1 = "Brownies";
    public static final String RECIPE_NAME_POS_2 = "Yellow Cake";
    public static final String RECIPE_NAME_POS_3 = "Cheesecake";

    public static final int RECIPE_LISTING_TEST_POSITION = 0;

    public static final int RECIPE_INGREDIENTS_MIN_COUNT = 1;
    public static final int RECIPE_DIRECTIONS_MIN_COUNT = 1;

    public static final int RECIPE_STEP_TESTING_TAG = 0;

    private IdlingResource idlingResourceUpdateUI;

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        idlingResourceUpdateUI = mActivityTestRule.getActivity().getIdlingResourceUpdateUI();
        IdlingRegistry.getInstance().register(idlingResourceUpdateUI);
    }

    @Test
    public void clickGridViewItem_OpensRecipeOverviewActivity() {

        onView(withId(R.id.rvwRecipesOverview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECIPE_LISTING_TEST_POSITION, click()));

        testRecipeOverviewActivity();


    }

    /**
     * testings for the activity loading a fragment with the single recipe overiew content
     */
    private void testRecipeOverviewActivity() {
        // checks the RecipeOverviewActivity with the correct name displayed
        onView(withId(R.id.txtRecipeTitle)).check(matches(withText(RECIPE_NAME_POS_0)));

        // checks the the ingredients are loaded correctly
        onView(withId(R.id.lilIngredients)).check(matches(hasMinimumChildCount(RECIPE_INGREDIENTS_MIN_COUNT)));

        // checks that the directions are loaded correctly
        onView(withId(R.id.tlyRecipeSteps)).check(matches(hasMinimumChildCount(RECIPE_DIRECTIONS_MIN_COUNT)));


        testRecipeStepActivity();
    }

    /**
     * testings for the activity loading a fragment with the single recipe step content
     */
    private void testRecipeStepActivity() {
        onView(allOf(withTagValue(is((Object) RECIPE_STEP_TESTING_TAG)), isDisplayed())).perform(click());

        // make sure the default isn't the content
        onView(withId(R.id.txtShortDescription)).check(matches(not(withText(getResourceString(R.string.default_short_description)))));
        onView(withId(R.id.txtDescription)).check(matches(not(withText(getResourceString(R.string.default_description)))));
    }

    // Unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (idlingResourceUpdateUI != null) {
            IdlingRegistry.getInstance().unregister(idlingResourceUpdateUI);
        }
    }

    private String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }

}
