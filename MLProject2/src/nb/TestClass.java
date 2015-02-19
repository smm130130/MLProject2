package nb;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TestClass {
	public void findClass(HashMap<String, Object> hamHashMap, HashMap<String, Object> spamHashMap, HashMap<String, Double> priorProb, String path) {
		
		int hamClasses = 0, spamClasses = 0;
		double hamPrior = priorProb.get("ham");
		double spamPrior = priorProb.get("spam");
		
		File[] files = new File(path).listFiles();
		for (int i = 0; i < files.length; i++){
			String fname = files[i].getAbsolutePath();
			File textFile = new File(fname);
			Scanner fileScanner = null;
			HashMap<String, Integer> testMap = new HashMap<>();
			try {
				fileScanner = new Scanner(textFile);
				while(fileScanner.hasNext()) {
					String first = fileScanner.next();
					if(first.matches("[a-zA-Z]+")) {
						if (testMap.containsKey(first)) {
							testMap.put(first, testMap.get(first)+1);
						} else { 
							testMap.put(first,1);
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				fileScanner.close();
			}
				
			ConditionalProb condProb = new ConditionalProb();
			HashMap<String, Double> hamCondProb = condProb.computeConditionalProb(hamHashMap, testMap);
			HashMap<String, Double> spamCondProb = condProb.computeConditionalProb(spamHashMap, testMap);
			double finalHamVal = getMaxProbability(testMap, hamCondProb, hamPrior);
			double finalSpamVal = getMaxProbability(testMap, spamCondProb, spamPrior);
			if(finalHamVal >= finalSpamVal) {
				hamClasses++;
			}
			else {
				spamClasses++;
			}
		}
		
		System.out.println(path + " : hamClasses = "+ hamClasses + " spamClasses = "+ spamClasses);
	}

	private double getMaxProbability(HashMap<String, Integer> testMap, HashMap<String, Double> condProb, double prior) {
		double finalValue = 0.0;
		double priorLog = Math.log(prior);
		double secondPartValue = 0.0;
		
		for (Map.Entry<String, Integer> entry : testMap.entrySet()) {
			String key = entry.getKey();
			double eachKeyCount = entry.getValue();
			double condProbability = condProb.get(key);
			for(int i=0; i< eachKeyCount; i++) {
				secondPartValue = Math.log(condProbability) + secondPartValue;
			}
		}
		
		finalValue = priorLog + secondPartValue;
		return finalValue;
	}
}
