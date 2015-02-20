package lr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class TestClass {
	public void test(Double[] weights, String[] allUniqueWords, String testFolder) {
		
		String spamTestFolder = testFolder+"/spam";
		String hamTestFolder = testFolder+"/ham";
		
		System.out.println("spam folder "+ spamTestFolder + " ham folder : "+ hamTestFolder);
		testingIndividualFolders(weights, allUniqueWords, spamTestFolder);
	}

	private void testingIndividualFolders(Double[] weights, String[] allUniqueWords, String testFolder) {
		
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
						if (wordCount.containsKey(first)) {
							wordCount.put(first, wordCount.get(first)+1);
						} else { 
							wordCount.put(first,1);
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				fileScanner.close();
			}
			
			populateArray(wordCount, allUniqueWords, weights);
		}
	}

	private void populateArray(HashMap<String, Integer> wordCount, String[] allUniqueWords, Double[] weights) {
		
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
		double fullVal = 1 + summationValue;
		if(fullVal > 0) {
			//System.out.println("its a SPAM Class");
		}
		else {
			//System.out.println("Its is a HAM class");
		}
	}
}
