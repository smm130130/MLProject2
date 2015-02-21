package lr;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
	public static void main(String[] args) throws IOException {
		
		String[] arguments=new String[4];
		int some=0;
		for(String s:args)
		{
			arguments[some]=s;
			some++;
		}

		String trainFolder = arguments[0];
		String testFolder = arguments[1];
		double eta = Double.parseDouble(arguments[2]);
		double lambda = Double.parseDouble(arguments[2]);

		String hamFolder = trainFolder+"/ham";
		String spamFolder = trainFolder+"/spam";
		
		BufferedWriter bw = null;
		File file = new File("outputLR.txt");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), false);
			bw = new BufferedWriter(fw);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("TESTING WITHOUT STOPWORD");
		bw.write("TESTING WITHOUT STOPWORD");bw.newLine();
		BuildMatrix bm = new BuildMatrix();
		Integer[][] data = bm.buildMatrix(hamFolder, spamFolder, false);
		
		Double[] pr = new Double[bm.getTotalDocs()];
		Double[] w = new Double[bm.getTotalUnique()];
		for(int i=0 ;i<pr.length; i++) {
			pr[i] = generateRandomNumber(-1, 1);
		}
		for(int i=0 ;i<w.length; i++) {
			w[i] = generateRandomNumber(-1, 1);
		}
		
		Convergence con = new Convergence();
		Double[] weights = con.computeWeight(data, pr, w, eta, lambda);
		
		String[] allUniqueWords = bm.getAlluniqueWord();
		TestClass tc = new TestClass(bw);
		tc.test(weights, allUniqueWords, testFolder, false);
		
		System.out.println("TESTING WITH STOPWORD");
		bw.write("TESTING WITH STOPWORD");bw.newLine();
		BuildMatrix bm1 = new BuildMatrix();
		Integer[][] data1 = bm1.buildMatrix(hamFolder, spamFolder, true);
		
		Double[] pr1 = new Double[bm1.getTotalDocs()];
		Double[] w1 = new Double[bm1.getTotalUnique()];
		for(int i=0 ;i<pr1.length; i++) {
			pr1[i] = generateRandomNumber(-1, 1);
		}
		for(int i=0 ;i<w1.length; i++) {
			w1[i] = generateRandomNumber(-1, 1);
		}
		
		Convergence con1 = new Convergence();
		Double[] weights1 = con1.computeWeight(data1, pr1, w1, eta, lambda);
		
		String[] allUniqueWords1 = bm1.getAlluniqueWord();
		TestClass tc1 = new TestClass(bw);
		tc1.test(weights1, allUniqueWords1, testFolder, true);
		
		System.out.println("File in folder : "+ file.getAbsolutePath());
		bw.close();
	}

	public static double generateRandomNumber(double d, double e) {
		double rangeMin=d;
		double rangeMax=e;
		Random r = new Random();
		double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		return randomValue;
	}
}
