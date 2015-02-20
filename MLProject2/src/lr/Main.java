package lr;

import java.util.Random;

public class Main {
	public static void main(String[] args) {
		
		double eta = 0.01;
		double lambda = 1.5;
		String hamFolder = "D:/UTD/5thSemester/ML/HW/2/hw2_train/train/ham";
		String spamFolder = "D:/UTD/5thSemester/ML/HW/2/hw2_train/train/spam";
		String testFolder = "D:/UTD/5thSemester/ML/HW/2/hw2_test/test";
		
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
		double rangeMin=-5;
		double rangeMax=5;
		Random r = new Random();
		double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		return randomValue;
	}
}
