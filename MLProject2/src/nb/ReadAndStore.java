package nb;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class ReadAndStore {
	public HashMap<String, Object> readFile(String hamPath) {
		
		HashMap<String, Object> returnHashMap = new HashMap<>();
		HashMap<String, Integer> wordCount = new HashMap<String, Integer>();
		int noOfUniqueWordsCount = 0;
		int noOfTotalWordsCount = 0;
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
		returnHashMap.put("UniqueWordsCount", noOfUniqueWordsCount);
		returnHashMap.put("TotalWordsCount", noOfTotalWordsCount);
		returnHashMap.put("completeMap", wordCount);
		System.out.println(noOfUniqueWordsCount+ " "+noOfTotalWordsCount);
		
		return returnHashMap;
	}
}
