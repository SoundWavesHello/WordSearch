/*
 * 
 * 		Author: Stephen Majercik?
 * 		Lab 8
 * 		Last Modified: not sure?
 * 
 * 		This class contains a main method to do the following:
 * 		
 * 		1) Create a new WordSearch class object
 * 
 * 		2) Solve the puzzle in 3 different ways and
 *			time how long it takes to solve it each
 *			way
 * 
 */


public class TestWordSearch {

	public static void main(String[] args) {

		System.out.println("Welcome to Word Search!");

		// Create the puzzle.
		WordSearch puzzle = new WordSearch();

		// Solve the puzzle using all three approaches and output the solution times.
		if (puzzle.isGoodPuzzle()) {
			System.out.println("Using naive approach:");
			double solutionTime = puzzle.solve(WordSearch.SolutionMethod.NAIVE);
			System.out.println("Solution time: " + solutionTime + " seconds.\n");

			System.out.println("Using binary search approach:");
			solutionTime = puzzle.solve(WordSearch.SolutionMethod.BINARY_SEARCH);
			System.out.println("Solution time: " + solutionTime + " seconds.\n");

			System.out.println("Using binary search approach with prefixes:");
			solutionTime = puzzle.solve(WordSearch.SolutionMethod.USE_PREFIXES);
			System.out.println("Solution time: " + solutionTime + " seconds.\n");
		}
	}

}
