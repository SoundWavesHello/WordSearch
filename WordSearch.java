/*
 * 
 * 		Author: Kevin Lane
 * 		Lab 8
 * 		Last Modified: 11/16/16 (thanks, .metadata errors)
 * 
 * 		This class contains methods to do the following:
 * 
 * 		1)	Construct a WordSearch type object
 * 
 * 		2)	Read a file into a puzzle board
 * 
 * 		3)	Check that the puzzle board is valid
 * 
 * 		4)	Find the words in the puzzle in one of 
 * 			three different ways
 * 
 * 		4) 	Solve the puzzle by iterating through the
 * 			dictionary and checking to see if each word
 * 			in the dictionary can be found on the board
 * 
 * 		5) 	Iterate through the board and treat each location
 * 			as a starting point.  Check to see what words can
 * 			be found from that starting point by checking if 
 * 			each possible String can be found in the dictionary
 * 
 *  	6)	Iterate through the board and treat each location 
 *  		as a starting point.  Check to see if each String 
 *  		can be found in the dictionary as a prefix to see
 *  		whether or not its worth continuing in that 
 *  		direction.  If that does exist as a prefix, then
 *  		the String is checked using binarySearch to see
 *  		if it is in the dictionary
 * 
 */


import java.io.File;
import java.util.Scanner;


public class WordSearch {

	// To identify the different solution methods
	public static enum SolutionMethod {
		NAIVE, BINARY_SEARCH, USE_PREFIXES
	}

	// To hold the puzzle. 
	private static final int MAX_ROWS = 50;
	private static final int MAX_COLS = 50;
	char[][] board = new char[MAX_ROWS][MAX_COLS];

	// The actual number of rows and columns.
	int numRows;
	int numCols;

	// To signal that the puzzle is in a legal format
	boolean goodPuzzle;

	// The user gets to specify the minimum word length
	int minWordLength;

	// Holds the words that we are searching for
	Dictionary dictionary;

	// To get input from the user
	private static Scanner scan = new Scanner(System.in);



	// The constructor creates the dictionary and reads the puzzle.
	// If the puzzle is good, it prints it out and gets the minimum 
	// length from the user
	public WordSearch() {

		dictionary = new Dictionary();

		goodPuzzle = readPuzzle();
		if (goodPuzzle) {
			printPuzzle();
			System.out.println("Read puzzle with " + numRows + " rows and " + numCols + " columns");

			System.out.print("Minimum word length: ");
			minWordLength = scan.nextInt();
			System.out.println();
		}

	}


	// Purpose: This method reads in a Word Search puzzle from a file.
	// Parameters: None.
	// Return Value: True, if the puzzle was in a legal format; 
	//				 otherwise, false.
	//
	public boolean readPuzzle() {

		try {

			System.out.println();
			System.out.print("Please enter the puzzle file name without the \".txt\" extension: ");
			String boardFileName = scan.next();

			// The boards are in folder called "files" and the
			// user will enter the filename without the .txt file
			// extension, so we must add those.
			boardFileName = "files/" + boardFileName + ".txt";
			Scanner fileScan = new Scanner(new File(boardFileName));

			// Make sure the file has something in it.
			if (!fileScan.hasNext()) {
				System.out.println("Error: puzzle file is empty");
				numRows = 0;
				numCols = 0;
				return false;
			}

			// Get the first line; all the other lines should be
			// the same length (which is the number of columns).
			String line = fileScan.nextLine();
			numCols = line.length();
			// Put the first line in the board array.
			for (int i = 0; i < numCols; i++) {
				board[0][i] = Character.toLowerCase(line.charAt(i));
			}

			// Read the rest of the puzzle lines and put them in 
			// the board array; keep track of the number of lines (rows)
			for (numRows = 1; fileScan.hasNext(); ++numRows) {
				line = fileScan.nextLine();

				// Make sure it's the right length.
				if (line.length() != numCols) {
					System.out.println("Error: puzzle is not rectangular");
					numRows = 0;
					numCols = 0;
					return false;
				}

				// Put this puzzle line in board.
				for (int i = 0; i < numCols; i++) {
					board[numRows][i] = Character.toLowerCase(line.charAt(i));
				}
			}

			fileScan.close();

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Puzzle was legal.
		return true;
	}



	// Purpose: This method returns the value of goodPuzzle,
	//          which indicates whether the current puzzle is
	//          in a legal format.
	//
	public boolean isGoodPuzzle() {
		return goodPuzzle;
	}



	// Purpose: This method solves the puzzle with the specified method.
	// Parameters: The solution method to use.
	// Return Value: The time taken to solve the puzzle.
	//
	public double solve(SolutionMethod method) {

		long startTime = System.nanoTime();

		int numMatches = 0;

		if (method == SolutionMethod.NAIVE) {
			numMatches = solveNaively();
		}
		else if (method == SolutionMethod.BINARY_SEARCH) {
			numMatches = solveWithBinarySearch();
		}
		else if (method == SolutionMethod.USE_PREFIXES) {
			numMatches = solveWithPrefixes();
		}

		long endTime = System.nanoTime();
		double timeInSeconds = (endTime - startTime) / 1e9; // The number of nanoseconds in a second is 1e9


		System.out.println("Found " + numMatches + " matches");

		return timeInSeconds;
	}



	// Purpose: Find the words in the puzzle by 
	//			calling the checkWord method to see 
	//			if each word in the dictionary is on
	//			the board
	// Parameters: None
	// Return: The number of words found
	
	public int solveNaively() {
		
		// create a counter for the number of words found
		
		int wordsFound = 0;
		
		// cycle through words in the dictionary
		
		for (int i = 0; i < dictionary.getSize(); i ++){
			
			// check to see if the word is long enough
			
			if (dictionary.getItem(i).length() >= minWordLength){
				
				// iterate through the indices on the board
				
				for (int r = 0; r < numRows; r++){
					for (int c = 0; c < numCols; c++){
						
						// check to make sure that the fist letter
						// is actually the first letter on the board that
						// we're checking
						
						if (dictionary.getItem(i).charAt(0) == board[r][c]){
							
							// iterate through the possible directions
							
							for (int rOff = -1; rOff <= 1; rOff ++){
								for(int cOff = -1; cOff <= 1; cOff ++){
									if (rOff != 0 || cOff != 0){
										
										// call checkWord to see if the word
										// can be found in this location in
										// this direction
										
										wordsFound += checkWord(r, c, rOff,
												cOff, dictionary.getItem(i));
									}
								}
							}		

						}
						
					}
				}
			}
		}
		
		return wordsFound;
	}



	// Purpose: Checks to see if a specific word can
	//			be found from a starting location when
	//			going in one direction
	// Parameters: The coordinates of the starting location,
	//			the changes in row and column, and the
	//			word to look for
	// Return: How many words were found (1 if found, 0 if
	//			not found)
	
	
	public int checkWord(int baseRow, int baseCol,
			int rowDelta, int colDelta, String word) {
		
		// create variables to keep track of where we
		// actually are
		
		int actRow = baseRow;
		int actCol = baseCol;
		
		for (int i = 0; i < word.length(); i ++){
			
			// define actRow and actCol

			actRow = baseRow + rowDelta * i;
			actCol = baseCol + colDelta * i;
			
			// check to see if we're off the board
			
			if (actRow < 0 || actRow >= numRows 
					|| actCol < 0 || actCol >= numCols){
				
				// if it's out of bounds, no word was found
				
				return 0;
			}
			
			
			// check to see if the characters match up
			
			if (word.charAt(i) != board[actRow][actCol]){
				
				// then the word isn't here
				
				return 0;
			}
		}
		
		// if we reach this point, then we didn't go off of the
		// board ands all of the characters matched up; thus
		// a word was found
		
		System.out.println("Found: \"" + word + "\" at (" + baseRow + ", " 
				+ baseCol + ") to (" + actRow + ", " + actCol + ")");
		
		return 1;
	}


	// Purpose: Find how many words are in the puzzle by
	//			iterating through the board and calling
	//			checkDirectionUsingBinarySearch to see if
	//			any words can be found at a given location
	// Parameters: None
	// Return:	The number of words found
	
	public int solveWithBinarySearch() {
		
		// create a counter for the number of words found
		
		int wordsFound = 0;
		
		
		// iterate through the indices on the board
			
		for (int r = 0; r < numRows; r++){
			for (int c = 0; c < numCols; c++){
				
				// iterate through the possible directions
				
				for (int rOff = -1; rOff <= 1; rOff ++){
					for(int cOff = -1; cOff <= 1; cOff ++){
						if (rOff != 0 || cOff != 0){
							wordsFound += checkDirectionUsingBinarySearch
									(r, c, rOff, cOff);
						}
					}
				}		
			}
		}
		
		return wordsFound;
	}



	// Purpose: Check to see which words can be found on the board
	//			from a starting location in a specific direction
	// Parameters: The coordinates of the starting location and the 
	//			change in row and column
	// Return: How many words were found
	
	public int checkDirectionUsingBinarySearch(int baseRow, int baseCol,
			int rowDelta, int colDelta) {
		
		// create variables to keep track of the current location
		
		int actRow = baseRow;
		int actCol = baseCol;
		
		// create a String to keep track of the current word
		
		String currWord = "";
		
		// keep track of how many times we've moved
		
		int move = 0;
		
		// keep track of the number of words found
		
		int wordsFound = 0;
		
		// while we're not off of the board, keep checking in this direction
		
		while(actRow >= 0 && actRow < numRows &&
				actCol >= 0 && actCol < numCols){
			
			// update the current word
			currWord += board[actRow][actCol];
			
			// check to see if it's long enough
			// then check to see if it's in the 
			// dictionary
			
			if (currWord.length() >= minWordLength && 
					dictionary.binarySearch(currWord)){
				
				// print out the word found
				
				System.out.println("Found: \"" + currWord +
						"\" at (" + baseRow + ", " + baseCol +
						") to (" + actRow + ", " + actCol + ")");
				
				// increment wordsFound
				
				wordsFound ++;
			}
			
			move ++;
			
			// update current location
			
			actRow = baseRow + rowDelta * move;
			actCol = baseCol + colDelta * move;
		}

		return wordsFound;
	}



	// Purpose: Finds how many word are in the puzzle by 
	//			iterating through the board and calling the
	//			checkDirectionUsingPrefixes method to see
	//			how many words can be found at said location
	// Parameters: None
	// Return: The number of words found
	
	public int solveWithPrefixes() {
		
		// create a counter for the number of words found
		
		int wordsFound = 0;
		
		// iterate through the indices on the board
			
		for (int r = 0; r < numRows; r++){
			for (int c = 0; c < numCols; c++){
				
				// iterate through the possible directions
				
				for (int rOff = -1; rOff <= 1; rOff ++){
					for(int cOff = -1; cOff <= 1; cOff ++){
						if (rOff != 0 || cOff != 0){
							wordsFound += checkDirectionUsingPrefixes
									(r, c, rOff, cOff);
						}
					}
				}		
			}
		}
		
		return wordsFound;
	}


	// Purpose: Find the number of words that start
	//			at a specific location in a specific
	//			direction by checking to see if any
	//			words in the dictionary have that prefix
	//			to see if it's worth continuing to check
	//			and then checking to see if the word is
	//			in the dictionary
	// Parameters: The starting coordinates and the change in
	//			row and column
	// Return: The number of words found
	
	public int checkDirectionUsingPrefixes(int baseRow, int baseCol, 
			int rowDelta, int colDelta) {
		
		// create variables to keep track of the current location
		
		int actRow = baseRow;
		int actCol = baseCol;
		
		// create a String to keep track of the current word
		
		String currWord = "";
		
		// keep track of how many times we've moved
		
		int move = 0;
		
		// keep track of the number of words found
		
		int wordsFound = 0;
		
		// while we're not off of the board, keep checking in this direction
		
		while(actRow >= 0 && actRow < numRows &&
				actCol >= 0 && actCol < numCols){
			
			// update the current word
			currWord += board[actRow][actCol];
			
			// check to see if it's a prefix of
			// anything; if not, exit the loop
			
			if (!dictionary.findPrefix(currWord)){
				return wordsFound;
			}
			
			
			// check to see if it's long enough
			// then check to see if it's in the 
			// dictionary
			if (currWord.length() >= minWordLength && 
					dictionary.binarySearch(currWord)){
				
				// print out the word found
				
				System.out.println("Found: \"" + currWord +
						"\" at (" + baseRow + ", " + baseCol +
						") to (" + actRow + ", " + actCol + ")");
				
				// increment wordsFound
				
				wordsFound ++;
			}
			
			move ++;
			
			// update current location
			
			actRow = baseRow + rowDelta * move;
			actCol = baseCol + colDelta * move;
		}

		return wordsFound;
	}


	// Purpose: This method prints out the puzzle.
	// Parameters: None.
	// Return Value: None.
	//
	public void printPuzzle() {

		System.out.println();
		System.out.println("The Board: ");
		for (int r = 0; r < numRows; r++){
			for (int c = 0; c < numCols; c++){
				if (board[r][c] == 0)
					System.out.print("_ ");
				else {
					String nextChar = board[r][c] + " ";
					System.out.print(nextChar.toUpperCase());
				}
			}
			System.out.println();
		}
		System.out.println();

	}
}
