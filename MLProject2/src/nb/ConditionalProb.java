package nb;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")

public class ConditionalProb {
	public HashMap<String, Double> computeConditionalProb(HashMap<String, Object> completeHashmap, HashMap<String, Integer> testMap) {
		
		HashMap<String, Double> conditionalProb = new HashMap<>();
		int uniqueWordCount = (int) completeHashmap.get("UniqueWordsCount");
		int totalWordCount = (int) completeHashmap.get("TotalWordsCount");
		HashMap<String, Integer> wordHashMap = (HashMap<String, Integer>) completeHashmap.get("completeMap");
		
		int denominator = uniqueWordCount + totalWordCount;
		
		for (Map.Entry<String, Integer> entry : testMap.entrySet()) {
			
			String eachWord = entry.getKey();
			int countCorrosWord = 0;
			if(wordHashMap.get(eachWord) != null) {
				countCorrosWord = wordHashMap.get(eachWord);
			}
			
		    double numerator = countCorrosWord + 1;
		    double condProb = numerator/denominator;
		    conditionalProb.put(entry.getKey(), condProb);
		}
		
		return conditionalProb;
	}
}
