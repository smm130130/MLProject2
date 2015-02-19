package nb;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ComputePrior {
	public HashMap<String, Double> computePrior(String hamPath, String spamPath) {
		
		ArrayList<Integer> hamCount =  getCount(hamPath);
		ArrayList<Integer> spamCount =  getCount(spamPath);
		int hamLength = hamCount.get(0) + spamCount.get(0);
		int spamLength = hamCount.get(1) + spamCount.get(1);
		int totalCount = hamLength + spamLength;
		
		double hamPriorProb = (double)hamLength / totalCount;
		double spamPriorProb = (double)spamLength / totalCount;
		
		System.out.println(hamLength + " "+ spamLength + "=" + totalCount + " , "+ hamPriorProb + " , "+spamPriorProb);
		//getDirectLength(hamPath, spamPath);
		HashMap<String, Double> priorProb = new HashMap<>();
		priorProb.put("ham", hamPriorProb);
		priorProb.put("spam", spamPriorProb);
		
		return priorProb;
	}

	public void getDirectLength(String hamPath, String spamPath) {
		int hamLength = new File(hamPath).listFiles().length;
		int spamLength = new File(spamPath).listFiles().length;
		System.out.println(hamLength + " "+ spamLength);
	}

	private ArrayList<Integer> getCount(String hamPath) {
		ArrayList<Integer> counts = new ArrayList<>();
		int ham = 0; int spam = 0;
		File[] files = new File(hamPath).listFiles();
		for (int i = 0; i < files.length; i++){
			String fname = files[i].getName();
			if(fname.contains(".ham")) {
				ham++;
			} 
			else if(fname.contains(".spam")) {
				spam++;
			}
	    }
		counts.add(ham);
		counts.add(spam);
		return counts;
	}
}
