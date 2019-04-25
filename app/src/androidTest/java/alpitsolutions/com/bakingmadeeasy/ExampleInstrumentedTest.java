package alpitsolutions.com.bakingmadeeasy;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import alpitsolutions.com.bakingmadeeasy.database.IngredientsDao;
import alpitsolutions.com.bakingmadeeasy.database.RecipeDatabase;
import alpitsolutions.com.bakingmadeeasy.database.RecipesDao;
import alpitsolutions.com.bakingmadeeasy.database.StepsDao;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {




    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("alpitsolutions.com.bakingmadeeasy", appContext.getPackageName());
    }
}
