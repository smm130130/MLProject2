package lr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BuildMatrix {
	HashMap<String, Integer> wordCount = new HashMap<>();
	int arrayRowCount = 0;
	int hamUnique = 0;
	int spamUnique = 0;
	int totalUnique = 0;
	int totalDocs = 0;
	String[] alluniqueWord;

	public String[] getAlluniqueWord() {
		return alluniqueWord;
	}

	public void setAlluniqueWord(String[] alluniqueWord) {
		this.alluniqueWord = alluniqueWord;
	}

	public int getHamUnique() {
		return hamUnique;
	}

	public void setHamUnique(int hamUnique) {
		this.hamUnique = hamUnique;
	}

	public int getSpamUnique() {
		return spamUnique;
	}

	public void setSpamUnique(int spamUnique) {
		this.spamUnique = spamUnique;
	}

	public int getTotalUnique() {
		return totalUnique;
	}

	public void setTotalUnique(int totalUnique) {
		this.totalUnique = totalUnique;
	}

	public int getTotalDocs() {
		return totalDocs;
	}

	public void setTotalDocs(int totalDocs) {
		this.totalDocs = totalDocs;
	}
	
	public Integer[][] buildMatrix(String hamPath, String spamPath) {
		int totalDocs = getDirectLength(hamPath, spamPath);
		setTotalDocs(totalDocs);
		System.out.println("Total : "+ totalDocs);
		
		int hamUnique = getNumUniqueAttributes(hamPath);
		int spamUnique = getNumUniqueAttributes(spamPath);
		int totalUnique = hamUnique + spamUnique;
		setHamUnique(hamUnique); setSpamUnique(spamUnique); setTotalUnique(totalUnique);
		System.out.println("toalUnique :  "+ totalUnique);
		
		
		String [] allUniqueWords = new String[totalUnique];
		int i =0;
		for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
			allUniqueWords[i++] = entry.getKey();
		}
		setAlluniqueWord(allUniqueWords);
		
		Integer[][] data = new Integer[totalDocs][totalUnique+2];
		data = pupulateTheMatrix(data, allUniqueWords, hamPath, "ham");
		data = pupulateTheMatrix(data, allUniqueWords, spamPath, "spam");
		
		return data;
	}

	private Integer[][] pupulateTheMatrix(Integer[][] data, String[] allUniqueWords, String hamPath, String hamOrspam) {		
		
		HashMap<String, Integer> wordCountIndividual = new HashMap<>();
		File[] files = new File(hamPath).listFiles();
		for (int i = 0; i < files.length; i++){
			String fname = files[i].getAbsolutePath();
			File textFile = new File(fname);
			Scanner fileScanner = null;
			try {
				fileScanner = new Scanner(textFile);
				while(fileScanner.hasNext()) {
					String first = fileScanner.next();
					if(first.matches("[a-zA-Z]+")) {
						if (wordCountIndividual.containsKey(first)) {
							wordCountIndividual.put(first, wordCountIndividual.get(first)+1);
						} else { 
							wordCountIndividual.put(first,1);
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				fileScanner.close();
			}
			
			populateIndividualRows(data, allUniqueWords, wordCountIndividual, hamOrspam, arrayRowCount);
			arrayRowCount++;
		}
		return data;
	}

	private Integer[][] populateIndividualRows(Integer[][] data, String[] allUniqueWords, HashMap<String, Integer> wordCountIndividual, String hamOrSpam, int i) {
		
		int lastIndex = (data[0].length - 1);
		data[i][0] = 1;
		for(int j=0; j< (allUniqueWords.length); j++) {
			if(wordCountIndividual.get(allUniqueWords[j]) != null) {
				int val = wordCountIndividual.get(allUniqueWords[j]);
				data[i][j+1] = val;
			} 
			else {
				data[i][j+1] = 0;
			}
		}
		if(hamOrSpam.equalsIgnoreCase("ham")) {
			data[i][lastIndex] = 0;
		}
		else if(hamOrSpam.equalsIgnoreCase("spam")) {
			data[i][lastIndex] = 1;
		}
		return data;
	}

	private int getDirectLength(String hamPath, String spamPath) {
		int hamLength = new File(hamPath).listFiles().length;
		int spamLength = new File(spamPath).listFiles().length;
		return hamLength + spamLength;
	}
	
	public int getNumUniqueAttributes(String path) {
		
		int noOfTotalWordsCount = 0, noOfUniqueWordsCount = 0;
		File[] files = new File(path).listFiles();
		for (int i = 0; i < files.length; i++){
			String fname = files[i].getAbsolutePath();
			File textFile = new File(fname);
			Scanner fileScanner = null;
			try {
				fileScanner = new Scanner(textFile);
				while(fileScanner.hasNext()) {
					String first = fileScanner.next();
					if(first.matches("[a-zA-Z]+")) {
						noOfTotalWordsCount++;
						if (wordCount.containsKey(first)) {
							wordCount.put(first, wordCount.get(first)+1);
						} else { 
							noOfUniqueWordsCount++;
							wordCount.put(first,1);
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				fileScanner.close();
			}
		}
		System.out.println("total in a folder which is not required : "+ noOfTotalWordsCount);
		return noOfUniqueWordsCount;
	}
}
