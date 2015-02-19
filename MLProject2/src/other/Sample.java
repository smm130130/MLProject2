package other;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Sample {
	public static void main(String[] args) {
		
		HashMap<String, Integer> wordCount = new HashMap<String, Integer>();
		File textFile = new File("D:/UTD/5thSemester/ML/HW/2/hw2_train/train/ham/0004.1999-12-14.farmer.ham.txt");
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
		
		for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
	}
}
