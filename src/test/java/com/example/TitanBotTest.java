package com.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TitanBotTest {
    /* Need to test :
     *  1. The TitanBot does not fire if it does not have enough energy
     *  2. The TitanBot does not fire if there is nothing to fire at
     *  3. The TitanBot only fires at nonempty districts
     *  4. The TitanBot does not fire more Jolts than allowed
     */

    /*  Common Case: TitanBot does not fire if there is not enough energy
     *  Blackbox
     */
    @Test
    void fireCannonNotEnoughEnergy() {
        TimBot t = new TitanBot(42, 3);
        t.startRound();
        int [] spressoSensed = {0, 10, 10, 10, 10};
        boolean [] botsSensed = {false, true, true, true, true};
        t.senseDistricts(spressoSensed, botsSensed);
        assertNull(t.fireCannon(), "Should not fire cannon");
        assertEquals(2, t.energyLevel, "Energy level should be 2");
    }

    /*  Common Case: TitanBot does not fire if there is nothing to fire at
     *  Blackbox
     */
    @Test
    void fireCannonNoTargets() {
        TimBot t = new TitanBot(42, 4);
        t.startRound();
        int [] spressoSensed = {0, 10, 10, 10, 10};
        boolean [] botsSensed = {false, false, false, false, false};
        t.senseDistricts(spressoSensed, botsSensed);
        assertNull(t.fireCannon(), "Should not fire cannon");
        assertEquals(3, t.energyLevel, "Energy level should be 3");
    }

    /*  Common Case: TitanBot does not use more jolts than allowed.  Should fire once
     *  Blackbox
     */
    @Test
    void fireCannonMoreTargetThanEnergy() {
        TimBot t = new TitanBot(42, 4);
        t.startRound();
        int [] spressoSensed = {0, 10, 10, 10, 10};
        boolean [] botsSensed = {false, true, true, true, true};
        t.senseDistricts(spressoSensed, botsSensed);
        int [] fire = t.fireCannon();
        assertNotNull(fire, "Should fire cannon");
        assertEquals(1, fire.length, "One shot should be fired");
        assertEquals(District.NORTH, fire[0], "Only NORTH should be attacked");
        assertEquals(2, t.energyLevel, "Energy level should be 2");
    }

    /*  Common Case: TitanBot only shoots at real targets.  Should fire twice
     *  Blackbox
     */
    @Test
    void fireCannonLessTargetThanEnergy() {
        TimBot t = new TitanBot(42, 10);
        t.startRound();
        int [] spressoSensed = {0, 10, 10, 10, 10};
        boolean [] botsSensed = {false, true, false, true, true};
        t.senseDistricts(spressoSensed, botsSensed);
        int [] fire = t.fireCannon();
        assertNotNull(fire, "Shouldfire cannon");
        assertEquals(3, fire.length, "One shot should be fired");
        assertEquals(District.NORTH, fire[0], "First, NORTH should be attacked");
        assertEquals(District.SOUTH, fire[1], "Second, SOUTH should be attacked");
        assertEquals(District.WEST, fire[2], "Third, WEST should be attacked");
        assertEquals(6, t.energyLevel, "Energy level should be 6");
    }


    //AngryBot Tests 
    /* Need to test ordering of cases:
     *  If AngryBot has less than 2 or less Jolts, behave like SpressoBot
     *  Otherwise, BullyBot uses the following ordering
     *  1. There are other TimBots in the district.
     *  2. The least number of rounds before Spresso plants are ready for harvest.
     *  3. The order the districts as listed above.
     *  Also test whether energy level is properly being adjusted
     * Case 1: bot has less than 3 jolts, behaves like SpressoBot
     *   Case 1a: bot stays in CURRENT => Energy level unchanged
     *   Case 1b bot moves => Energy level degremented
     *   Case 1c: bot follows spresso above all else
     *   Case 1d: spresso being equal, bot prefers to avoid other bots
     *   Case 1e: spress and bots being equal, bot prefers to avoid adjacent bots
     *   Case 1f: everything is equal so that bot prefers in order of districts
     * Case 2: bot has 3 or more jolts
     *   Case 2a: bot selects to attack regardless ot poor spresso count
     *   Case 2b bot selects to attack with best spresso count
     *   Case 2c: bot selects best spresso count
     *   Case 2d: everything is equal so that bot prefers in order of districts
     *
     */

     
    /* Case 1a: Bot stays in CURRENT => energy level does not change
     * Blackbox
     */
    @Test
    void getNextMoveCase1a() {
        TimBot t = new TitanBot(42, 3);
        t.startRound();
        int [] spressoSensed = {0, 10, 10, 10, 10};
        boolean [] botsSensed = {false, true, true, true, true};
        t.senseDistricts(spressoSensed, botsSensed);
        assertEquals(District.CURRENT, t.getNextMove(), "Should not move");
        assertTrue(t.useShield(), "Energy level should be at 2");
        assertTrue(t.useShield(), "Energy level should be at 1");
        assertFalse(t.useShield(), "Energy level should be at 0");
    }

    /* Case 1b: Bot moves out of CURRENT => energy level drops
     * Blackbox
     */
    @Test
    void getNextMoveCase1b() {
        TimBot t = new TitanBot(42, 3);
        t.startRound();
        int [] spressoSensed = {10, 0, 10, 10, 10};
        boolean [] botsSensed = {false, false, true, true, true};
        t.senseDistricts(spressoSensed, botsSensed);
        assertEquals(District.NORTH, t.getNextMove(), "Should not move");
        assertEquals(0, t.energyLevel, "Energy level should be at 1");
    }

    /* Case 1c: Bot should go to spresso most ready to be harvested
     * Blackbox
     */
    @Test
    void getNextMoveCase1c() {
        TimBot t = new TitanBot(42, 3);
        t.startRound();
        int [] spressoSensed = {10, 10, 0, 10, 10};
        boolean [] botsSensed = {false, false, true, false, false};
        t.senseDistricts(spressoSensed, botsSensed);
        assertEquals(District.EAST, t.getNextMove(), "Should follow spresso");
    }

    /* Case 1d: Bot should avoid other bots if spresso is equal
     * Blackbox
     */
    @Test
    void getNextMoveCase1d() {
        TimBot t = new TitanBot(42, 3);
        t.startRound();
        int [] spressoSensed = {10, 0, 0, 0, 0};
        boolean [] botsSensed = {false, true, true, false, true};
        t.senseDistricts(spressoSensed, botsSensed);
        assertEquals(District.SOUTH, t.getNextMove(), "Should avoid others");
    }

    /* Case 1e: Bot should avoid adjacent bots if possible
     * Blackbox
     */
    @Test
    void getNextMoveCase1e() {
        TimBot t = new TitanBot(42, 3);
        t.startRound();
        int [] spressoSensed = {0, 0, 0, 0, 0};
        boolean [] botsSensed = {false, true, true, true, false};
        t.senseDistricts(spressoSensed, botsSensed);
        assertEquals(District.WEST, t.getNextMove(), "Should avoid adjacent bots");
    }

    /* Case 1f: Bot should avoid adjacent bots if possible
     * Blackbox
     */
    @Test
    void getNextMoveCase1f() {
        TimBot t = new TitanBot(42, 3);
        t.startRound();
        int [] spressoSensed = {0, 0, 0, 0, 0};
        boolean [] botsSensed = {false, false, false, false, false};
        t.senseDistricts(spressoSensed, botsSensed);
        assertEquals(District.CURRENT, t.getNextMove(), "Should avoid adjacent bots");
    }

    /* Case 2a: Bot should attack regardless of low spresso count
     * Greybox (using energyLevel)
     */
    @Test
    void getNextMoveCase2a() {
        TimBot t = new TitanBot(42, 4);
        t.startRound();
        int [] spressoSensed = {0, 10, 0, 0, 0};
        boolean [] botsSensed = {false, true, false, false, false};
        t.senseDistricts(spressoSensed, botsSensed);
        assertEquals(District.NORTH, t.getNextMove(), "Should attack");
        assertEquals(2, t.energyLevel, "Energy level should be 2");
    }

    /* Case 2b: Bot should attack and choose best spresso level
     * Blackbox
     */
    @Test
    void getNextMoveCase2b() {
        TimBot t = new TitanBot(42, 4);
        t.startRound();
        int [] spressoSensed = {0, 10, 0, 5, 0};
        boolean [] botsSensed = {false, true, false, true, false};
        t.senseDistricts(spressoSensed, botsSensed);
        assertEquals(District.SOUTH, t.getNextMove(), "Should attack SOUTH");
    }

    /* Case 2c: No bots to attack, so select best spresso
     * Blackbox
     */
    @Test
    void getNextMoveCase2c() {
        TimBot t = new TitanBot(42, 4);
        t.startRound();
        int [] spressoSensed = {6, 10, 2, 5, 7};
        boolean [] botsSensed = {false, false, false, false, false};
        t.senseDistricts(spressoSensed, botsSensed);
        assertEquals(District.EAST, t.getNextMove(), "Should go to Spresso in EAST");
    }

    /* Case 2d: No bots to attack, all spresso equal, select in order
     * Blackbox
     */
    @Test
    void getNextMoveCase2d() {
        TimBot t = new TitanBot(42, 4);
        t.startRound();
        int [] spressoSensed = {1, 1, 1, 1, 1};
        boolean [] botsSensed = {false, false, false, false, false};
        t.senseDistricts(spressoSensed, botsSensed);
        assertEquals(District.CURRENT, t.getNextMove(), "Should stay in CURRENT");
    }
}