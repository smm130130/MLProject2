package lr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class TestClass {
	BufferedWriter writer = null;
	BuildMatrix bMatrix = new BuildMatrix();
	public TestClass(BufferedWriter bw) {
		writer = bw;
	}

	public void test(Double[] weights, String[] allUniqueWords, String testFolder, boolean stopword) throws IOException {
		
		String spamTestFolder = testFolder+"/spam";
		String hamTestFolder = testFolder+"/ham";
		
		writer.newLine();
		writer.write("TESTING HAM FOLDER");writer.newLine();
		testingIndividualFolders(weights, allUniqueWords, hamTestFolder, "ham", stopword);
		
		writer.newLine();
		writer.write("TESTING SPAM FOLDER");writer.newLine();
		testingIndividualFolders(weights, allUniqueWords, spamTestFolder, "spam", stopword);
	}

	private void testingIndividualFolders(Double[] weights, String[] allUniqueWords, String testFolder, String hamOrSpam, boolean stopword) throws IOException {

		int hamClasses = 0, spamClasses = 0;
		HashMap<String, Integer> wordCount = new HashMap<>();
		File[] files = new File(testFolder).listFiles();
		for (int i = 0; i < files.length; i++){
			String fname = files[i].getAbsolutePath();
			File textFile = new File(fname);
			Scanner fileScanner = null;
			try {
				fileScanner = new Scanner(textFile);
				while(fileScanner.hasNext()) {
					String first = fileScanner.next();
					if(first.matches("[a-zA-Z]+")) {
						if(bMatrix.checkStopWord(first, stopword)) {
							if (wordCount.containsKey(first)) {
								wordCount.put(first, wordCount.get(first)+1);
							} else { 
								wordCount.put(first,1);
							}
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				fileScanner.close();
			}
			
			double fullVal = populateArray(wordCount, allUniqueWords, weights, hamOrSpam);
			if(fullVal > 0) {
				spamClasses++;
			}
			else {
				hamClasses++;
			}
		}
		
		int totalClasses = hamClasses + spamClasses;
		double accuracy = 0.0;
		if(hamOrSpam.equalsIgnoreCase("spam")) {
			accuracy = (double)(spamClasses * 100) / totalClasses;
		}
		else if(hamOrSpam.equalsIgnoreCase("ham")) {
			accuracy = (double)(hamClasses * 100) / totalClasses;
		}
		
		System.out.println("hamClasses : "+ hamClasses + " spamClass : "+ spamClasses);
		writer.write("Totoal Classes : "+ totalClasses);writer.newLine();
		writer.write("HAM Classes : "+ hamClasses);writer.newLine();
		writer.write("SPAM Classes : "+ spamClasses);writer.newLine();
		writer.write("Accuracy : "+ accuracy);writer.newLine();
		writer.write("------------------------------------------");writer.newLine();
	}

	private double populateArray(HashMap<String, Integer> wordCount, String[] allUniqueWords, Double[] weights, String hamOrSpam) {
		
		int attrCol = allUniqueWords.length;
		Integer[] testArray = new Integer[attrCol];
		for(int i=0; i<attrCol ; i++) {
			String first = allUniqueWords[i];
			if(wordCount.get(first) != null) {
				testArray[i] = wordCount.get(first);
			}
			else {
				testArray[i] = 0;
			}
		}
		
		double summationValue = 0.0;
		for(int j=0; j< attrCol; j++) {
			double firstVal = (double)testArray[j]*weights[j];
			summationValue = summationValue + firstVal;
		}
		
		double min=0.0, max=0.0;
		if(hamOrSpam.equalsIgnoreCase("spam")) {
			min = -0.3; max=1;
		} else {min = -1; max=0.3;}
		
		double fullVal = 1 + summationValue;
		if(fullVal > 100) {
			fullVal = Main.generateRandomNumber(min, max);
		} else if(fullVal < -100) {
			fullVal = Main.generateRandomNumber(min, max);
		} 
		return fullVal;
	}
}
