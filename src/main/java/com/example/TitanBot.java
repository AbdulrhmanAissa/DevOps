package com.example;

/**
   File: TitanBot.java
   Author: Abdulrhman & Faisal
   Date: April 8, 2023
   Purpose: DevOps Project

   Description: This class specifies the TitanBot class.
*/


/**
   This is the TitanBot base class for representing BullyBots and their 
   behaviours on the planet DohNat.  
*/
public class TitanBot extends TimBot{
    /**
       This is the only constructor for this class.  This constructor
       initializes the Tibot and sets its ID and the initial amount of
       energy that it has.
  
       parameter: id    : ID of the TimBot
                  jolts : Initial amount of energy
     */
    public TitanBot( int id, int jolts ) {
      super( id, jolts );
      personality = 'T';
    }
  
    /** 
       This method is called during the Fire Cannon phase.  The TimBot
       creates an array of integers, each representing where it wishes
       to fire the ion cannon, and decreases its energy reserves by
       1 Jolt for each shot.  The integers can be one NORTH, EAST,
       SOUTH, or WEST.  If the TimBot does not wish to fire its cannon,
       it returns null;
  
       returns: array of integers representing shots from the cannon
     */
    public int [] fireCannon() {
      int count = 0;
      int [] fire = null;
  
      // Determine number of districts to shoot at
      for( int i = 0; i < botsSensed.length; i++ ) {
        if( ( i != District.CURRENT ) && botsSensed[i] ) {
          count++;
        }
      }
  
      // Reduce count if we do not have enough energy
      if( count > ( energyLevel - 2 ) ) {
        count = energyLevel - 2;
      }
  
      // If we are going to shoot, create the array of shots
      if( count > 0 ) {
        // allocate array and set index into it to 0.
        fire = new int[count];  
        int j = 0;
  
        // loop through all the districts, adding them to array if they
        // contain a timbot
        for( int i = District.NORTH; i < botsSensed.length; i++ ) {
          if( ( i != District.CURRENT ) && botsSensed[i] && ( j < count ) ) {
            fire[j] = i;
            j++;
            energyLevel--;
          }
        }
      }
      return fire;
    }

  //AngryBot Functions
  public int getNextMove() {
    // Create an array for scoring each of the possible moves
    // And set initial move to CURRENT
    int [] scores = new int[botsSensed.length];
    int move = District.CURRENT;

    // If we have enough energy, consider attacking another district
    if( energyLevel > 2 ) {
      // Compute scores for each of the districts, the lower scores are better
      // score = spressoCount + 2000 * (if district is empty)
      for( int i = 0; i < scores.length; i++ ) {
        scores[i] = plantSensed[i];
        if( !botsSensed[i] ) {
          scores[i] += EMPTY_DISTRICT_PENALTY;
        }
      }

      // Find the minimum over all scores
      int min = scores[District.CURRENT] + 1;
      for( int i = 0; i < scores.length; i++ ) {
        if( min > scores[i] ) {
          min = scores[i];
          move = i;
        }
      }

     useMoveEnergy(move);
    } else {
      // Else, if we do not have enough energy to attack, behave like spressobot
      move = super.getNextMove();
    }
    return move;
  }
}
  