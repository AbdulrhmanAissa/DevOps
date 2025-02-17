package com.example;

/**
   File: Cleaner.java
   Author: Alex Brodsky
   Date: August 26, 2015
   Purpose: CSCI 1110, Assignment 2 Solution

   Description: This program applies multiple filters to a gray scale image.
*/

import java.util.*;

/**
   This is the main (only) class of the program and contains all program code.
   The program starts running in the main() of this class.
*/
public class TimSim {
  /**
     This method is called when the program starts running. 
     It reads in a single integer from the console and then 
     determines its prime factorization by dividing the integer
     by all smaller values (greater than 1) until the result is
     $1$ or a prime number.  Each factor is printed out after it
     is computed.
  */
  public static void main( String [] args ) {
    // Instatiate Scanner to read in he timbot configuration
    // console and loading the image
    Scanner stdin = new Scanner( System.in );   

    // for debugging purposes (please ignore)
    boolean debug = ( args.length > 0 ) && args[0].equals( "debug" );

    // load configuration of simulation
    System.out.println("Please enter the number of rows for DohNat Planet: ");
    int rows = stdin.nextInt();             // Number of Rows
    System.out.println("Please enter the number of columns for DohNat Planet: ");
    int columns = stdin.nextInt();          // Number of Columns
    System.out.println("Please enter the number of jolts: ");
    int jolts = stdin.nextInt();            // Number of Jolts
    System.out.println("Please enter the number of growth: ");
    int growth = stdin.nextInt();           // Number of Growth
    System.out.println("Please enter the number of rounds: ");
    int rounds = stdin.nextInt();           // Number of Rounds
    System.out.println("Please enter the number of bots: ");
    int numBots = stdin.nextInt();          // Number of Bots
    System.out.println("Please enter the number of plant: ");
    int numPlant = stdin.nextInt();         // Number of Plant Configuration

    // Instantiate planet and array of Timbots with rows, columns, jolts and growth parameters
    DohNat planet = new DohNat( rows, columns, jolts, growth );
    TimBot [] bots = new TimBot[numBots];
    
    // Loading the Timbot configurations
    for( int i = 0; i < numBots; i++ ) {
      // Read in one timbot config
      System.out.println("Please enter the personality of bot number " + i + ": ");
      String personality = stdin.next();
      System.out.println("Please enter the id of the bot: ");
      int id = stdin.nextInt();
      System.out.println("Please enter the x axis of the district which the bot will be in: ");
      int x = stdin.nextInt();
      System.out.println("Please enter the y axis of the district which the bot will be in: ");
      int y = stdin.nextInt();
      System.out.println("Please enter the energy of the bot: ");
      int energy = stdin.nextInt();

      // Insantiate the corresponding bot object.
      switch( personality ) {
      case "chicken":
        bots[i] = new ChickenBot( id, energy );
        break;
      case "spresso":
        bots[i] = new SpressoBot( id, energy );
        break;
      case "angry":
        bots[i] = new AngryBot( id, energy );
        break;
      case "bully":
        bots[i] = new BullyBot( id, energy );
        break;
      case "titan":
        bots[i] = new TitanBot( id, energy );
        break;
      }

      // Error checking (unnecessary)
      if( !planet.setTimBot( bots[i], x, y ) ) {
        System.err.println( bots[i] + " not added" );
        return;
      } else if( debug ) {
        System.err.println( bots[i] + " added" );
      }
    }

    // Instantiate plants array of the Plant class with numPlant.
     Plant [] plants = new Plant[numPlant];

    // Load the plant configurations
    for( int i = 0; i < numPlant; i++ ) {
      // Read in one plant config
      System.out.println("Please enter the plant name: ");
      String plantName = stdin.next();               // Type of Plant e.g. Spresso, Mericano
      System.out.println("Please enter the x axis of the district which the plant will be in: ");
      int x = stdin.nextInt();                       // X - coordinates
      System.out.println("Please enter the y axis of the district which the plant will be in: ");
      int y = stdin.nextInt();                       // Y - coordinates
      System.out.println("Please enter the energy that the plant provide: ");
      int energy = stdin.nextInt();                  // Energy gained
      System.out.println("Please enter the rounds required to complete the growth of the plant: ");
      int roundForGrowth = stdin.nextInt();          // Rounds required for Completion of Growth

      // Insantiate the corresponding plant object.
      switch( plantName ) {
        case "spresso":
          plants[i] = new SpressoPlant( 0, energy, roundForGrowth );
          break;
        case "mericano":
          plants[i] = new MericanoPlant( roundForGrowth, energy, roundForGrowth );
        break;
      }
    }

      // Loop for a specified number of rounds
    for( int i = 0; i < rounds; i++ ) {
      // Perform the phases of a round
      planet.newRound();
      planet.doSensePhase();
      if( debug ) {
        System.err.println( "Sense Phase " + i );
        System.err.print( planet );
      }
      planet.doFirePhase();
      if( debug ) {
        System.err.println( "Fire Phase " + i );
        System.err.print( planet );
      }
      planet.doDefensePhase();
      if( debug ) {
        System.err.println( "Defense Phase " + i );
        System.err.print( planet );
      }
      planet.doMovePhase();
      if( debug ) {
        System.err.println( "Move Phase " + i );
        System.err.print( planet );
      }
      planet.doBattlePhase();
      if( debug ) {
        System.err.println( "Battle Phase " + i );
        System.err.print( planet );
      }
      planet.doHarvestPhase();

      // Output results
      System.out.println( "Round " + i );
      System.out.print( planet );
      
      // Check if the simulation is done by counting number of functional bots
      int remaining = 0;
      for( int j = 0; j < numBots; j++ ) { 
        if( bots[j].isFunctional() ) {
          remaining++;
        }
      }
    }
  }
}
