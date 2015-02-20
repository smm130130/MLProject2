package lr;

public class Convergence {
	public Double[] computeWeight(Integer[][] data, Double[] probabilities, Double[] randomWeights, double eta, double lambda) {
		int attrColumns = randomWeights.length;
		int totalDocsRows = data.length;
		
		//Iteration should be around 4-5
		for(int i=0; i<4; i++) {
			probabilities = computeProbabilities(data, probabilities, randomWeights);
			
			Double[] dw = new Double[attrColumns];
			for(int j=0; j< dw.length; j++) {
				dw[j] = 0.0;
			}
			
			for(int m=0; m<attrColumns; m++) {
				for(int n=0; n<totalDocsRows; n++) {
					double lastTerm = (double)(data[n][attrColumns+1] - probabilities[n]);
					double secondLast = lastTerm * data[n][m];
					dw[m] =  dw[m] + secondLast;
				}
			}
			
			for(int k=0; k< attrColumns; k++) {
				double lambdaMultiplier = lambda * randomWeights[k];
				double etaMultiplier = eta * (dw[k] - lambdaMultiplier);
				randomWeights[k] = randomWeights[k] + etaMultiplier;
				//System.out.println(randomWeights[k]);
			}
		}
		return randomWeights;
	}

	private Double[] computeProbabilities(Integer[][] data, Double[] probabilities, Double[] randomWeights) {
		int row = data.length;
		int column = data[0].length;

		for(int i=0; i<row; i++) {
			
			double summationRow = 0.0;
			for(int j=1; j< column-1; j++) {
				double wxTerm = (double)data[i][j]*randomWeights[j-1];
				summationRow = summationRow + wxTerm;
			}
			double beforeExp = (double)data[i][0] + summationRow;
			double assignProbability = 0.0;
			if(beforeExp > 100) {
				assignProbability = 1.0;
			} 
			else if(beforeExp < -100) {
				assignProbability = 0.0;
			}
			else {
				double exp = Math.exp(beforeExp);
				double denominator = (double)1 + exp;
				if(data[i][column-1] == 0) {
					assignProbability = 1 / denominator;
				}
				else {
					assignProbability = exp / denominator;
				}
			}
			if(data[i][column-1] == 0) {
				probabilities[i] = assignProbability;
				//System.out.println("Class HAM "+probabilities[i]+ "beforeExp : "+ beforeExp);
			}
			else {
				probabilities[i] = assignProbability;
				//System.out.println("Class SPAM "+probabilities[i]+ "beforeExp : "+ beforeExp);
			}
		}
		return probabilities;
	}
}
