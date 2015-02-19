package nb;

import java.util.HashMap;

public class Main {
	public static void main(String[] args) {
		ComputePrior cp = new ComputePrior();
		HashMap<String, Double> priorProb = cp.computePrior("D:/UTD/5thSemester/ML/HW/2/hw2_train/train/ham", "D:/UTD/5thSemester/ML/HW/2/hw2_train/train/spam");
		
		ReadAndStore rs = new ReadAndStore();
		HashMap<String, Object> hamHashMap =  rs.readFile("D:/UTD/5thSemester/ML/HW/2/hw2_train/train/ham");
		HashMap<String, Object> spamHashMap =  rs.readFile("D:/UTD/5thSemester/ML/HW/2/hw2_train/train/spam");
		
		TestClass tc = new TestClass();
		tc.findClass(hamHashMap, spamHashMap, priorProb, "D:/UTD/5thSemester/ML/HW/2/hw2_test/test/ham");
	}
}
