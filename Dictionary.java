/*
 * 		Author: Kevin Lane
 * 		Lab 8
 * 		Last Modified 11/13/16
 * 
 * 		This class contains methods to:
 * 		
 * 		1) Construct a dictionary type class
 * 		2) Read a text file into a dictionary
 * 		3) Use binary search to see if there are
 * 			words in the dictionary that start with
 * 			a specific prefix
 * 		4) Use binary search to look for a word
 * 			in the dictionary class
 * 		5) Get the size of the dictionary
 *		6) Get the item at a specific index in the
 *			dictionary
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Dictionary {
	
	// initialize variables
	private ArrayList<String> dict;
	private int size;
	
	
	// Purpose: construct a Dictionary class
	//			object
	// Parameters: none
	// Return: none
	
	
	public Dictionary(){
		
		// get user input to find the filename
		Scanner scan = new Scanner(System.in);
		System.out.print("Please enter the name of "
				+ "the dictionary you would like to use,"
				+ "excluding the \".txt\" extension: ");
		
		String filename = scan.nextLine();
		
		// create an ArrayList and read the file
		// into that ArrayList
		dict = new ArrayList<String>();
		readDictionary(filename);
		
		// get some whitespace
		System.out.println();
		
		// print out the size of the dictionary
		System.out.println("Dictionary size is " + size);
	}
	
	
	// Purpose: read a file into a dictionary
	// Parameters: the file name
	// Return: none
	
	public void readDictionary(String fileName){
		
		try {
			// the dictionary file is found in a folder called
			// 'Files', so we need to look there
			String dictFileName = "Files/" + fileName + ".txt";
			Scanner scan = new Scanner(new File(dictFileName));
			
			while (scan.hasNext()){
				String word = scan.next();
				dict.add(word);
				
				size ++;
			}
			
			scan.close();
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	// Purpose: determine if there are any words
	//			with a given prefix within
	//			the dictionary
	// Parameters: the target prefix
	// Return: whether or not there are words with
	//			that prefix in the dictionary
	
	public boolean findPrefix(String targetPrefix){
		
		int low = 0;
		int high = dict.size() - 1;
		
		while (low <= high){
			int middle = (low + high) / 2;
			
			if (dict.get(middle).startsWith(targetPrefix)){
				return true;
			}
			
			else if (targetPrefix.compareToIgnoreCase(dict.get(middle)) < 0){
				high = middle - 1;
			}
			
			else{
				low = middle + 1;
			}
		}
		
		return false;
	}
	
	
	// Purpose: find if a word is in the dictionary
	// Parameters: the target word
	// Return: whether or not the word was found
	
	public boolean binarySearch(String targetWord){
		
		int low = 0;
		int high = dict.size() - 1;
		
		while (low <= high){
			int middle = (low + high) / 2;
			
			if (dict.get(middle).equals(targetWord)){
				return true;
			}
			
			else if (targetWord.compareToIgnoreCase(dict.get(middle)) < 0){
				high = middle - 1;
			}
			
			else{
				low = middle + 1;
			}
		}
		
		return false;
	}

	
	// Purpose: get the size of 
	//			the dictionary
	// Parameters: none
	// Return: the size


	public int getSize() {
		return size;
	}
	
	
	// Purpose: get an item in the dictionary
	//			at a specific index
	// Parameters: the index
	// Return: the item at said index
	
	public String getItem(int i){
		return dict.get(i);
	}
}
