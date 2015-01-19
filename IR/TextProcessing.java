/*
 * Author: Jwala Mohith Girisha
 * 
 */

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.io.IOException;
import java.io.File;
 
 
 
public class TextProcessing{

	/***************** START OF fnBeginTextProcessing METHOD *****************************/
	
	public static void fnBeginTextProcessing(){
		
		Scanner in = new Scanner(System.in);
		
		List<String> tokenList = new ArrayList();
		Map<String,Integer> tokenPair = new HashMap<String, Integer>();
		Map<String,Integer> twoGramPair = new HashMap<String, Integer>();
		Map<String,Integer> palindromePair = new HashMap<String, Integer>();

		
		/*
		 * Takes file path.
		 * 
		 * Calls functions to tokenize the file, count token frequency, compute two grams and count their frequencies and compute palindromes and count frequency.
		 * 
		 */
		while(true){
			String filePath;
			System.out.println("Enter the file path:");
		
			filePath = in.nextLine();
			File file = new File(filePath);
	
			if(file.exists() && !file.isDirectory()){
				tokenList = tokenizeFile(filePath);
				System.out.println("********File tokenized Successfully*********");
				break;
			}
			else{
				System.out.println("File not found. Try again. \nEnter the file path:");
			}			
		}
		
		System.out.println("\n" + tokenList.size() + " tokens found. Would you like to print the list? [y/n]");
		
		String userip = in.nextLine();
		if(userip.equals("y") || userip.equals("Y"))
			print(tokenList);
			
		while(true){
				
			System.out.println("\n----------------------------\nWhat would you like to do? \n1. Compute and print word frequencies.\n2. Compute Two Grams and print their frequency.\n3. Compute palindrome and print their frequency.\n4. All of the above. \n5. Quit\nYour choice?");
			int useripint = in.nextInt();		
		
			if(useripint == 5)
				return;
				
			if(useripint == 1 || useripint == 4){
				
				//PartB
				tokenPair = computeWordFrequencies(tokenList);
				printToken(tokenPair);
			}
			
			if(useripint == 2 || useripint == 4){
				
				//PartC
				twoGramPair = computeTwoGramFrequencies(tokenList);
				printTwoGram(twoGramPair);
			}
			
			if(useripint == 3 || useripint == 4){
				
				//PartD
				palindromePair = computePalindromeFrequencies(tokenList);
				printPalindrome(palindromePair);
			}
		}
	}
	
	/***************** END OF fnBeginTextProcessing METHOD *****************************/



	/***************** START OF tokenizeFile and print METHODS (PART A) *****************************/
	
	//Part A
	/*
	 * Tokenize input file.
	 * Print tokens.
	 *  
	 */
	public static List<String> tokenizeFile(String FileName){
		
		BufferedReader brSample = null;
		List<String> tokenList = new ArrayList();
		
		try {
 
			String strSample;
			brSample = new BufferedReader(new FileReader(FileName));
 
			while ((strSample = brSample.readLine()) != null) {
					
				for(String words: strSample.toLowerCase().split("[^a-z0-9]+")){
					if(words.length()>0)
						tokenList.add(words);
				}		
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				if (brSample != null)
					brSample.close();	
			}
			catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	
		return tokenList;
	}
	
	public static void print(List<String> tokenList){
		
		System.out.println("Tokens: " + tokenList);
		
	}
	
	/***************** END OF tokenizeFile and print METHODS (PART A) *****************************/

	
	
	/***************** START OF computeWordFrequencies and printToken METHODS (PART B) *****************************/

	//Part B
	/*
	 * Compute token frequencies
	 * Print token frequencies (Writes to file 'Token.txt')
	 * 
	 */
	public static Map<String, Integer> computeWordFrequencies(List<String> tokenList){
		
		Map<String,Integer> tokenPairTemp = new HashMap<String, Integer>();
		
		for (String token : tokenList) {
			Integer count = tokenPairTemp.get(token);
			count = (count == null) ? 1 : ++count;
			tokenPairTemp.put(token, count);
		}
		
		return tokenPairTemp;
	
	}
	
	public static void printToken(Map<String, Integer> tokenPair){
		
		try{
			
			File fileTokens = new File("./Files/Result/Tokens.txt");
	 
			if (!fileTokens.exists()) {
				fileTokens.createNewFile();
			}

			FileWriter fwSample = new FileWriter(fileTokens.getAbsoluteFile());
			BufferedWriter bwSample = new BufferedWriter(fwSample);

			
			int tokenCount = 0;
			
			//System.out.println("\n\tFrequency - Token\n\t---------   -----\n");
			
			Set<Entry<String, Integer>> tokenPairSet = tokenPair.entrySet();
			List<Entry<String, Integer>> tokenPairList = new ArrayList<Entry<String, Integer>>(tokenPairSet);
			Collections.sort( tokenPairList, new Comparator<Map.Entry<String, Integer>>()
			{
				public int compare( Map.Entry<String, Integer> mapEntry1, Map.Entry<String, Integer> mapEntry2 )
				{
					return (mapEntry2.getValue()).compareTo( mapEntry1.getValue() );
				}
			} );
			
			for(Map.Entry<String, Integer> mapEntry:tokenPairList){
				
				tokenCount += mapEntry.getValue();
			  //  System.out.println("\t" + mapEntry.getValue() + "\t" + mapEntry.getKey());
			  
				bwSample.write(mapEntry.getValue() + "\t" + mapEntry.getKey() + "\n");

			}
			
			System.out.println("\nTotal number of Tokens: " + tokenCount);
			System.out.println("Total number of Unique Tokens: " + tokenPair.size());

			bwSample.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	/***************** END OF computeWordFrequencies and printToken METHODS (PART B) *****************************/

	
	
	/***************** START OF computeTwoGramFrequencies and printTwoGrams METHODS (PART C) *****************************/

	//Part C
	/*
	 * Compute two grams
	 * Print two grams (Write to file 'TwoGrams.txt')
	 * 
	 */
	public static Map<String, Integer> computeTwoGramFrequencies(List<String> tokenList){
		
		Map<String,Integer> twoGramPairTemp = new HashMap<String, Integer>();
		
		String previous = "";
		for (String token : tokenList) {
			String tokenTemp = "";
			if(previous == ""){
				previous = token;
				continue;
			}
			else{
				tokenTemp = previous + " " + token;
				previous = token;
			}
			Integer count = twoGramPairTemp.get(tokenTemp);
			count = (count == null) ? 1 : ++count;
			twoGramPairTemp.put(tokenTemp, count);
			
		}
		
		return twoGramPairTemp;
	
	}
	
	public static void printTwoGram(Map<String, Integer> twoGramPair){
		
		try{
			File fileTwoGrams = new File("./Files/Result/TwoGrams.txt");
	 
			if (!fileTwoGrams.exists()) {
				fileTwoGrams.createNewFile();
			}

			FileWriter fwSample = new FileWriter(fileTwoGrams.getAbsoluteFile());
			BufferedWriter bwSample = new BufferedWriter(fwSample);

		
		
		int twoGramCount = 0;
		
		//System.out.println("\n\tFrequency - Two Grams\n\t---------   ---------\n");
		
		Set<Entry<String, Integer>> twoGramPairSet = twoGramPair.entrySet();
        List<Entry<String, Integer>> twoGramPairList = new ArrayList<Entry<String, Integer>>(twoGramPairSet);
        Collections.sort( twoGramPairList, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare( Map.Entry<String, Integer> mapEntry1, Map.Entry<String, Integer> mapEntry2 )
            {
                return (mapEntry2.getValue()).compareTo( mapEntry1.getValue() );
            }
        } );
        
        for(Map.Entry<String, Integer> mapEntry: twoGramPairList){
			
			twoGramCount += mapEntry.getValue();
            //System.out.println("\t" + mapEntry.getValue() + "\t" + mapEntry.getKey());
            
			bwSample.write(mapEntry.getValue() + "\t" + mapEntry.getKey() + "\n");

        }
        
        System.out.println("\nTotal number of Two Grams: " + twoGramCount);
		System.out.println("Total number of Unique Two Grams: " + twoGramPair.size());
		
		bwSample.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
						
	}
	
	/***************** END OF computeTwoGramFrequencies and printTwoGrams METHODS (PART C) *****************************/

	
	
	/***************** START OF computePalindromeFrequencies and printPalindromes METHODS (PART D) *****************************/

	//Part D
	/*
	 * Compute palindromes
	 * Print palindromes (Write to file 'Palindromes.txt')
	 * 
	 */
	public static Map<String, Integer> computePalindromeFrequencies(List<String> tokenList){

		Map<String,Integer> palindromePairTemp = new HashMap<String, Integer>();
		
		int indexToken = 0;	
		int indexNotIncl = -1;	
		
		for (String token : tokenList) {
						
			boolean palindrome = false;
				
			int index = indexToken;
			int indexPal = -1;
			
			int strlen = 0;
			String strTemp = "";
			String strPalindrome = "";
			
			while(strlen<=20 && index < tokenList.size()){
				
				strTemp += tokenList.get(index);
				
				palindrome = isPalindrome(strTemp);
			
				if(palindrome){	
					
					strPalindrome = strTemp;
					indexPal = index;
					
				}
				
				strlen = strTemp.length();
				index++;
			}
			
			if(strPalindrome.length() > 1 && indexPal > indexNotIncl){
				
				Integer count = palindromePairTemp.get(strPalindrome);
				count = (count == null) ? 1 : ++count;
				palindromePairTemp.put(strPalindrome, count);
				
				indexNotIncl = indexPal;
				
			}

			
			indexToken++;
		}
		
		return palindromePairTemp;
	
	}
	
	public static boolean isPalindrome(String str){
		
		
		int len = str.length();
			
			for(int i=0, j=len-1; i<j; i++, j--){
				if(str.charAt(i)==str.charAt(j))
					continue;
				else{
					return false;
				}
			}
			
			return true;
	}
	
	public static void printPalindrome(Map<String, Integer> palindromePair){
		
		try{
			File filePalindrome = new File("./Files/Result/Palindromes.txt");
	 
			if (!filePalindrome.exists()) {
				filePalindrome.createNewFile();
			}

			FileWriter fwSample = new FileWriter(filePalindrome.getAbsoluteFile());
			BufferedWriter bwSample = new BufferedWriter(fwSample);
		
		int palindromeCount = 0;
				
		//System.out.println("\n\tFrequency - Palindromes\n\t---------   -----------\n");
		
		Set<Entry<String, Integer>> palindromePairSet = palindromePair.entrySet();
        List<Entry<String, Integer>> palindromePairList = new ArrayList<Entry<String, Integer>>(palindromePairSet);
        Collections.sort( palindromePairList, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare( Map.Entry<String, Integer> mapEntry1, Map.Entry<String, Integer> mapEntry2 )
            {
                return (mapEntry2.getValue()).compareTo( mapEntry1.getValue() );
            }
        } );
        
        for(Map.Entry<String, Integer> mapEntry: palindromePairList){
			
			palindromeCount += mapEntry.getValue();
           // System.out.println("\t" + mapEntry.getValue() + "\t\t" + mapEntry.getKey());
           
   			bwSample.write(mapEntry.getValue() + "\t" + mapEntry.getKey() + "\n");

        }
        
        System.out.println("\nTotal number of Palindromes: " + palindromeCount);
		System.out.println("Total number of Unique Palindromes: " + palindromePair.size());

		bwSample.close();

		} catch (IOException e) {
			e.printStackTrace();
		}				
	}
	
	/***************** END OF computePalindromeFrequencies and printPalindromes METHODS (PART D) *****************************/

	
}
