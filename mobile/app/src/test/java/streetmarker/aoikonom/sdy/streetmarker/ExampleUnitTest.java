package streetmarker.aoikonom.sdy.streetmarker;

import org.junit.Test;

import streetmarker.aoikonom.sdy.streetmarker.model.Coordinates;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testDoubleConversions() throws Exception {
        assertEquals("34.52839231012", Coordinates.doubleToStr(34.52839231012));
        assertEquals(Coordinates.strToDouble("34.52839231012"), 34.52839231012, 0.001);
    }
}