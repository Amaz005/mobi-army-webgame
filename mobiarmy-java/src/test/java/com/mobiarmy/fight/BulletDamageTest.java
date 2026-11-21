package com.mobiarmy.fight;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class BulletDamageTest {
    // Gunner shell: 280 damage, blast radius 21; players are 24 px wide (half width 12).

    @Test
    void directHitDoesFullDamage() {
        assertEquals(280, Bullet.damageAt(280, 0, 21, 12));
        assertEquals(280, Bullet.damageAt(280, 12, 21, 12));
    }

    @Test
    void damageFallsToHalfAtBlastEdge() {
        assertEquals(140, Bullet.damageAt(280, 12 + 21, 21, 12));
        int mid = Bullet.damageAt(280, 12 + 10, 21, 12);
        assertTrue(mid < 280 && mid > 140, "mid=" + mid);
    }

    @Test
    void outsideBlastDoesNothing() {
        assertEquals(0, Bullet.damageAt(280, 12 + 22, 21, 12));
        // A bullet with no blast radius only hurts on a direct hit.
        assertEquals(280, Bullet.damageAt(280, 5, 0, 12));
        assertEquals(0, Bullet.damageAt(280, 20, 0, 12));
    }
}
