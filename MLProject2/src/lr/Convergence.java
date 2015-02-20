package lr;

public class Convergence {
	public void computeWeight(Integer[][] data, Double[] probabilities, Double[] randomWeights, double eta, double lambda) {
		
		//Iteration should be around 4-5
		for(int i=0; i<1; i++) {
			computeProbabilities(data, probabilities, randomWeights);
		}
	}

	private void computeProbabilities(Integer[][] data, Double[] probabilities, Double[] randomWeights) {
		int row = data.length;
		int column = data[0].length;

		for(int i=0; i<row; i++) {
			
			double summationRow = 0.0;
			for(int j=1; j< column-1; j++) {
				double wxTerm = randomWeights[j-1]*data[i][j];
				summationRow = summationRow + wxTerm;
			}
			double beforeExp = data[i][0] + summationRow;
			double exp = Math.exp(beforeExp);
			double denominator = 1 + exp;
			if(data[i][column-1] == 0) {
				probabilities[i] = 1 / denominator;
				System.out.println("Class HAM "+probabilities[i]);
			}
			else {
				probabilities[i] = exp / denominator;
				System.out.println("Class SPAM "+probabilities[i]);
			}
		}
	}
}
