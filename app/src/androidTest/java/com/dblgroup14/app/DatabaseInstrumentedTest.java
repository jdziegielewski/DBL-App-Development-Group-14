package com.dblgroup14.app;

import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.dblgroup14.support.AppDatabase;
import com.dblgroup14.support.dao.AlarmDao;
import com.dblgroup14.support.dao.ChallengeDao;
import com.dblgroup14.support.dao.ChallengeSeriesDao;
import com.dblgroup14.support.dao.UserScoreDao;
import com.dblgroup14.support.entities.Alarm;
import com.dblgroup14.support.entities.Challenge;
import com.dblgroup14.support.entities.ChallengeSeries;
import com.dblgroup14.support.entities.UserScore;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {
    private static AppDatabase database;
    
    @BeforeClass
    public static void before() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppDatabase.createDatabase(appContext, true);
        database = AppDatabase.db();
    }
    
    @AfterClass
    public static void after() {
        database.clearAllTables();
        database.close();
    }
    
    @Test
    public void testAlarmEntity() {
        final int TEST_ID = 95;
        AlarmDao dao = database.alarmDao();
        
        // Assert that non-existing entity is retrieved as NULL
        assertNull(dao.get(TEST_ID));
        
        // Store new entity
        Alarm a = new Alarm("test alarm", 12, 0, true, 100, true, 0, 1, 3, 4);
        a.id = TEST_ID;
        dao.store(a);
        
        // Assert existing entity
        Alarm a2 = dao.get(TEST_ID);
        assertNotNull(a2);
        
        // Validate entity data
        assertEquals("test alarm", a.name);
        assertEquals(12, a.hours);
        assertEquals(0, a.minutes);
        assertEquals(true, a.enabled);
        assertEquals(100, a.volume);
        assertEquals(true, a.repeats);
        assertArrayEquals(new boolean[]{true, true, false, true, true, false, false}, a.days);
        
        // Delete entity
        dao.delete(a2);
        
        // Assert that entity does not exist
        assertNull(dao.get(TEST_ID));
    }
    
    @Test
    public void testChallengeEntity() {
        final int TEST_ID = 100;
        ChallengeDao dao = database.challengeDao();
        
        // Assert that non-existing entity is retrieved as NULL
        assertNull(dao.get(TEST_ID));
        
        // Store new entity
        Challenge c = new Challenge();
        c.id = TEST_ID;
        c.put("Test", "String");    // string
        c.put("Test1", "12");       // number
        c.put("Test2", "true");     // boolean
        dao.store(c);
        
        // Assert that entity exists now
        Challenge c2 = dao.get(TEST_ID);
        assertNotNull(c2);
        
        // Assert existing and non-existing keys
        assertTrue(c2.has("Test"));
        assertTrue(c2.has("Test1"));
        assertTrue(c2.has("Test2"));
        assertFalse(c2.has("String"));
        
        // Assert getXXX() results
        assertEquals("String", c2.getString("Test"));
        assertEquals(12, c2.getInt("Test1"));
        assertEquals(true, c2.getBoolean("Test2"));
        
        // Assert that non-existing keys throws exception
        try {
            c2.getString("NonExist");
            fail();
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
        }
        
        // Assert that parsing a non-number throws exception
        try {
            c2.getInt("Test");
            fail();
        } catch (NumberFormatException e) {
            assertNotNull(e.getMessage());
        }
        
        // Delete entity
        dao.delete(c2);
        
        // Assert that entity does not exist
        assertNull(dao.get(TEST_ID));
    }
    
    @Test
    public void testChallengeSeriesEntity() {
        final int TEST_ID = 37;
        ChallengeSeriesDao dao = database.challengeSeriesDao();
        
        // Assert that non-existing entity is retrieved as NULL
        assertNull(dao.get(TEST_ID));
        
        // Store new entity
        ChallengeSeries cs = new ChallengeSeries();
        cs.id = TEST_ID;
        cs.addChallenge(0);
        cs.addChallenge(10);
        cs.addChallenge(15);
        cs.removeChallenge(10);
        cs.swap(0, 1);
        dao.store(cs);
        
        // Assert that entity exists now
        ChallengeSeries cs2 = dao.get(TEST_ID);
        assertNotNull(cs2);
        
        // Assert entity data
        assertEquals(2, cs2.challengeIds.size());
        assertEquals((Integer) 15, cs2.challengeIds.get(0));
        assertEquals((Integer) 0, cs2.challengeIds.get(1));
    }
    
    @Test
    public void testUserScoreEntity() {
        final String TEST_USER_NAME = "markus157";
        UserScoreDao dao = database.userScoreDao();
        
        // Assert that non-existing entity is retrieved as NULL
        assertNull(dao.scoreOfUser(TEST_USER_NAME));
        
        // Store new entity
        UserScore u = new UserScore();
        u.setUsername(TEST_USER_NAME);
        u.addPoints(1024);
        u.subtractPoints(64);
        dao.store(u);
        
        // Assert existing entity
        UserScore u2 = dao.scoreOfUser(TEST_USER_NAME);
        assertNotNull(u2);
        
        // Validate entity data
        assertEquals(960, u2.score);
        
        // Delete entity
        dao.delete(u2);
        
        // Assert that entity does not exist
        assertNull(dao.scoreOfUser(TEST_USER_NAME));
    }
}
