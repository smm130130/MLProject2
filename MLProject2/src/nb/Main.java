package nb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Main {
	public static void main(String[] args) throws IOException {
		
		String trainFolder = "D:/UTD/5thSemester/ML/HW/2/hw2_train/train";
		String hamTrainFolder = trainFolder+"/ham";
		String spamTrainFolder = trainFolder+"/spam";
		
		String testFolder = "D:/UTD/5thSemester/ML/HW/2/hw2_test/test/";
		String hamTestFolder = testFolder+"/ham";
		String spamTestFolder = testFolder+"/spam";
		
		BufferedWriter bw = null;
		File file = new File("output.txt");
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
		ComputePrior cp = new ComputePrior();
		HashMap<String, Double> priorProb = cp.computePrior(hamTrainFolder, spamTrainFolder);
		
		bw.write("TESTING WITHOUT STOPWORDS"); bw.newLine();
		System.out.println("TESTING WITHOUT STOPWORDS");
		
		ReadAndStore rs = new ReadAndStore();
		HashMap<String, Object> hamHashMap =  rs.readFile(hamTrainFolder, false);
		HashMap<String, Object> spamHashMap =  rs.readFile(spamTrainFolder, false);
		
		TestClass tc = new TestClass(bw);
		tc.findClass(hamHashMap, spamHashMap, priorProb, hamTestFolder, "ham");
		tc.findClass(hamHashMap, spamHashMap, priorProb, spamTestFolder, "spam");
		
		bw.write("TESTING WITH STOPWORDS"); bw.newLine();
		
		System.out.println("TESTING WITH STOPWORDS");
		HashMap<String, Object> hamHashMap1 =  rs.readFile(hamTrainFolder, true);
		HashMap<String, Object> spamHashMap1 =  rs.readFile(spamTrainFolder, true);
		
		TestClass tc1 = new TestClass(bw);
		tc1.findClass(hamHashMap1, spamHashMap1, priorProb, hamTestFolder, "ham");
		tc1.findClass(hamHashMap1, spamHashMap1, priorProb, spamTestFolder, "spam");
		
		System.out.println("OUTPUT TEXTFILE IS IN FOLDER : "+ file.getAbsolutePath());
		bw.close();
	}
}
