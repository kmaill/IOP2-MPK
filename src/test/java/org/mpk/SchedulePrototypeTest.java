package org.mpk;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class SchedulePrototypeTest {

    @Test
    public void testCloneSchedule() {
        LocalDate validFrom = LocalDate.of(2023, 1, 1);
        Schedule original = new Schedule(1, validFrom, "Monday");

        Schedule clone = original.clone();

        assertNotNull(clone, "Clone should not be null");
        assertNotSame(original, clone, "Clone should be a different object instance");
        assertEquals(original.getId(), clone.getId());
        assertEquals(original.getValidFrom(), clone.getValidFrom());
        assertEquals(original.getDayOfWeek(), clone.getDayOfWeek());
    }

    @Test
    public void testModifyClone() {
        LocalDate validFrom = LocalDate.of(2023, 1, 1);
        Schedule original = new Schedule(1, validFrom, "Monday");
        Schedule clone = original.clone();

        clone.setDayOfWeek("Tuesday");
        clone.setId(2);

        assertEquals("Monday", original.getDayOfWeek());
        assertEquals(1, original.getId());

        assertEquals("Tuesday", clone.getDayOfWeek());
        assertEquals(2, clone.getId());
    }
}
