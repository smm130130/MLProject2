package lr;

import java.util.Random;

public class Main {
	public static void main(String[] args) {
		
		String[] arguments=new String[4];
		int some=0;
		for(String s:args)
		{
			arguments[some]=s;
			some++;
		}
		
		double eta = Double.parseDouble(arguments[2]);
		double lambda = Double.parseDouble(arguments[2]);;
		String testFolder = arguments[1];
		String trainFolder = arguments[0];

		String hamFolder = trainFolder+"/ham";
		String spamFolder = trainFolder+"/spam";
		
		BuildMatrix bm = new BuildMatrix();
		Integer[][] data = bm.buildMatrix(hamFolder, spamFolder);
		
		Double[] pr = new Double[bm.getTotalDocs()];
		Double[] w = new Double[bm.getTotalUnique()];
		for(int i=0 ;i<pr.length; i++) {
			pr[i] = generateRandomNumber();
		}
		for(int i=0 ;i<w.length; i++) {
			w[i] = generateRandomNumber();
		}
		
		Convergence con = new Convergence();
		Double[] weights = con.computeWeight(data, pr, w, eta, lambda);
		
		String[] allUniqueWords = bm.getAlluniqueWord();
		TestClass tc = new TestClass();
		tc.test(weights, allUniqueWords, testFolder);
	}

	private static double generateRandomNumber() {
		double rangeMin=-1;
		double rangeMax=1;
		Random r = new Random();
		double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		return randomValue;
	}
}
