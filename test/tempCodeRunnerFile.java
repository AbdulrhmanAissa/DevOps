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