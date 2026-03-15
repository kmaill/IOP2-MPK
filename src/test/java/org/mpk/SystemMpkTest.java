package org.mpk;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


class SystemMpkTest {
    @Test
    void instancesTest() {
        SystemMpk[] table = new SystemMpk[2];

        try {
            for(int i = 0; i < table.length; i++) {
                table[i] = SystemMpk.getInstance();
            }
            assertSame(table[0], table[1]);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
