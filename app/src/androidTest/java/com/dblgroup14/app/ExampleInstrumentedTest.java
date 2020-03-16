package com.dblgroup14.app;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.dao.ChallengeDao;
import com.dblgroup14.support.entities.Challenge;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

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
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.dblgroup14.app", appContext.getPackageName());
    }
    
    @Test
    public void testChallengeDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        
        // Instantiate database
        AppDatabase.createDatabase(context);
        ChallengeDao dao = AppDatabase.db().challengeDao();
    
        // Check that no challenge of certain ID exists
        Challenge c = dao.get(100);
        assertNull(c);
        
        // Create new challenge
        c = new Challenge();
        c.id = 100;
        c.put("Test", "ASDASD");
        c.put("Test1", "12");
        c.put("Test2", "true");
        dao.store(c);
        
        // Check that challenge exists now
        c = dao.get(100);
        assertNotNull(c);
        assertEquals("ASDASD", c.getString("Test"));
        assertEquals(12, c.getInt("Test1"));
        assertEquals(true, c.getBoolean("Test2"));
        
        // Check that exceptions are thrown correctly
        try {
            c.getString("NonExist");
            fail();
        } catch (IllegalArgumentException e) {
            // ignored
        }
        
        try {
            c.getInt("Test");
            fail();
        } catch (NumberFormatException e) {
            // ignored
        }
        
        // Delete challenge
        dao.delete(c);
        c = dao.get(100);
        assertNull(c);
    }
}
