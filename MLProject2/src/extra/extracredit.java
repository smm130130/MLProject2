package extra;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
class extracredit 
{
	public static void main(String[] args) throws Exception
	{
		String[] argu=new String[2];
		int i=0;
		for(String s:args)
		{
			argu[i]=s;
			System.out.println("argu[i] "+argu[i]);
			i++;
		}
		
		trainMultinomial t=new trainMultinomial(argu[0],argu[1]);
		
		
	}
}
class  trainMultinomial
{
	
	double totalTokensInHam = 0;
	double totalTokensInSpam = 0;
	HashMap<String,Integer> tVHM;
	double B=0;//Total Vocab
	double[] score = new double[2]; // For finding the best of Spam/Ham
	static double positive = 0;static double negative = 0;
	String[] testDatapaths=new String[2];
	String[] trainingDatapaths=new String[2];
	/*String directoryPath="C:\\Users\\Tejasri\\Desktop\\train";
	String testPath="C:\\Users\\Tejasri\\Desktop\\test";*/
	double NcSpam;double NcHam;
	
	HashMap<String,Integer> spamTrainingMap=new HashMap<String,Integer>();
	HashMap<String,Integer> hamTrainingMap=new HashMap<String,Integer>();
	HashMap<String,HashMap<String,Integer>> trainingMapClassToPaths=new HashMap<String,HashMap<String,Integer>>();
	HashMap<String,Double> trainingMapClassToTokens=new HashMap<String,Double>();
	HashMap<String,Double> trainingMapClassToNc=new HashMap<String,Double>();
	
	//double maxScore=-1;
	double prior;	
	//int finalClass;
		public trainMultinomial(String directoryPath,String testPath) throws Exception
		{
		/*Total vocabulary of the Test Data is found out!! */	
			
			int N=countDocs(directoryPath);
			String concatenatedDirectoryPath=extractVocabulary(directoryPath);
			tVHM=tokenizeVocabularytoHASHMap(concatenatedDirectoryPath);
			//delete("C:\\hw2_training\\dconcat.txt");
			B=tVHM.size();
			System.out.println("Total Vocabulary "+B);
		/* Total vocabulary:B  totalDocs:n */	
		
			//TestData and trainingData path arrays
			testDatapaths[0]=testPath+"/ham";	
			testDatapaths[1]=testPath+"/spam";
			trainingDatapaths[0]=directoryPath+"/ham";	
			trainingDatapaths[1]=directoryPath+"/spam";
			
			String concatTextHamTraining=concatenateTextDocsInClass(directoryPath+"/ham");
			hamTrainingMap=tokenize(concatTextHamTraining);
			
			// Tct1(ham)
			
			for(double fs:hamTrainingMap.values())
			{
			totalTokensInHam+=fs;
			}
			
			//After finding the Nc terms of spam and ham,put them in a map
			NcHam=countDocsInClass(directoryPath+"/ham");
			NcSpam=countDocsInClass(directoryPath+"/spam");
			
			//Now finding the HashMaps of tokens for Spam(Training) and Ham(Training)
			String concatTextSpamTraining=concatenateTextDocsInClass(directoryPath+"/spam");
			spamTrainingMap=tokenize(concatTextSpamTraining);
			
			// Tct1(spam)
			
			for(double f:spamTrainingMap.values())
			{
			totalTokensInSpam+=f;
			}
			
			trainingMapClassToPaths.put(directoryPath+"/ham",hamTrainingMap);
			trainingMapClassToPaths.put(directoryPath+"/spam",spamTrainingMap);
			
			
			//training Map classes to no of Tokens(may re-occur many times)
			trainingMapClassToTokens.put(directoryPath+"/ham",totalTokensInHam);
			trainingMapClassToTokens.put(directoryPath+"/spam",totalTokensInSpam);
			
			
			//int k=0;
			trainingMapClassToNc.put(directoryPath+"/ham", (NcHam/N));
			trainingMapClassToNc.put(directoryPath+"/spam", (NcSpam/N));
			
			
			String Class;
			long initTime=System.currentTimeMillis();
				for(String classp: testDatapaths)
					{
					System.out.println("classp "+classp);
					File testFile=new File(classp);
					File[] subFiles=testFile.listFiles();
					for(File subfile:subFiles)
					{
						System.out.println("The subfile "+subfile.getPath());
						HashMap<String,Integer> docMap=tokenize(subfile.getPath());
						int k=0;
						for(String trainPaths:trainingDatapaths)
						{
							int i=0;	double[] p=	new double[docMap.size()];
							for(Entry<String,Integer> DocMapE:docMap.entrySet())
							{
							 
								prior=(trainingMapClassToNc.get(trainPaths));
								int power=DocMapE.getValue();
								String key=DocMapE.getKey();
									if((trainingMapClassToPaths.get(trainPaths)).get(key)!=null)
									{
										//System.out.println("trainingMapClassToPaths.get(trainPaths)"+trainingMapClassToPaths.get(trainPaths));
										double Tct=	(trainingMapClassToPaths.get(trainPaths)).get(key);
										double num=(Tct+4);
										//System.out.println("(Tct+1) "+(Tct+1));
										//System.out.println("trainingMapClassToTokens.get(trainPaths) "+trainingMapClassToTokens.get(trainPaths));
										double den=(trainingMapClassToTokens.get(trainPaths)+B);
										p[i]=(num/den);
										p[i]=Math.pow((num/den), power);
										
										i++;
									}
							}
							score[k]=applyMultinomialB(prior,p);
							k++;
						}
						System.out.println("score[0] "+score[0]);
						System.out.println("score[1] "+score[1]);
							if(score[0]>score[1])
								{
								Class=testPath+"/ham";}
							else
								{Class=testPath+"/spam";}
							
									if(Class.equals(classp))
										{System.out.println("positive");
										//System.out.println("class"+":"+"C:\\hw2_test\\ham");
										//System.out.println("classp"+":"+classp);
										positive++;}
									else
										{
										System.out.println("negative");
										//System.out.println("class"+":"+"C:\\hw2_test\\spam");
									System.out.println("classp"+":"+classp);
										negative++;}
							//score comparison with maxScore
								/*	for(int i=0;i<score.length;i++)
									{
										if(score[i]>maxScore)
										{
											maxScore=score[i];
											finalClass=i;
										}
									}*/
						}
					}	
				/*System.out.println("The positive "+positive);
				System.out.println("The negative "+negative);*/
					double accuracy=(positive)/(positive+negative);
					System.out.println(" accuracy "+(accuracy*100));
					long endTime=System.currentTimeMillis();
					System.out.println("The time taken by Naive-Bayes on Test Set(Spam+Ham) "+(endTime-initTime));
					//System.out.println(" NcSpam "+NcSpam);System.out.println(" NcHam "+NcHam);
		}
		
		String extractVocabulary(String directoryPath) throws Exception
		{
			File df=new File(directoryPath);
			File[] dfiles=df.listFiles();
			String path="dconcat.txt";
			//String path="dconcat1.txt";
			PrintWriter pw=new PrintWriter(new FileOutputStream(path));
			for(File twoFiles:dfiles)
				{
				System.out.println("Processing subFile path "+twoFiles.getPath());
					File subFile=new File(twoFiles.getPath());
					{
						File[] trainingDocs=subFile.listFiles();
						BufferedReader br = null;
						for(int i=0;i<trainingDocs.length;i++)
						{
						br=new BufferedReader(new FileReader(trainingDocs[i].getPath()));
						String line=br.readLine();
							while(line!=null)
							{
							pw.println(line);	line=br.readLine();
							}
						}	
						br.close();
					}
				}
			pw.close();
		return path;
		}
		
		HashMap<String, Integer> tokenizeVocabularytoHASHMap(String concatenatedDirectoryPath) throws Exception 
		{
			HashMap<String,Integer> Vocabu_HashMap=new HashMap<String,Integer>();
			String strLine;
			String fileData="";
			
			BufferedReader br=new BufferedReader(new FileReader(concatenatedDirectoryPath));
				while((strLine=br.readLine())!=null)
				{
				fileData+=strLine+" ";
				}
				fileData=fileData.replaceAll("\\<.*?>",""); 
				fileData=fileData.replaceAll("'s", ""); 
				fileData=fileData.replaceAll("[-+^<>:,?';=%#&~`$!@*_)/(}{]","");
				fileData=fileData.replaceAll("[0-9]+","");
				fileData=fileData.replaceAll("\\.\\s", "");
				fileData=fileData.replaceAll(".$",""); 
				fileData=fileData.toLowerCase();
			StringTokenizer stk=new StringTokenizer(fileData);
				while(stk.hasMoreTokens())
				{
				String token=stk.nextToken();
				if(!token.equals(null))
				{
					if(Vocabu_HashMap.get(token)==null)
					{
						Vocabu_HashMap.put(token,1);
					}
					else
					{
						Vocabu_HashMap.put(token,(Vocabu_HashMap.get(token)+1));
					}
				}
				}
				//System.out.println("Vocabu_HashMap "+Vocabu_HashMap);
			return Vocabu_HashMap;
		}
		public static int countConcatenate=0;
		String concatenateTextDocsInClass(String classp) throws Exception {
			// TODO Auto-generated method stub
			String concatenateSpam;
			countConcatenate++;
			File df=new File(classp);
			File[] dfiles=df.listFiles();
			BufferedReader br;
			//String concatenateSpam=classp+"//concate.txt";
			if(countConcatenate==1)
			{concatenateSpam="concate1.txt";}
			else
			{concatenateSpam="concate2.txt";}
			PrintWriter pw=new PrintWriter(new FileOutputStream(concatenateSpam));
			for(int i=0;i<dfiles.length;i++)
			{
			br=new BufferedReader(new FileReader(dfiles[i].getPath()));
			String line=br.readLine();
				while(line!=null)
				{
				pw.println(line);	line=br.readLine();
				}
			br.close();
			}	
			pw.close();
			return concatenateSpam;
		}

		int countDocsInClass(String filePath)
		{
			File df=new File(filePath);
			File[] dfiles=df.listFiles();
			return dfiles.length;
		}
		int countDocs(String directoryPath)
		{
			System.out.println(" directoryPath "+directoryPath);
			File df=new File(directoryPath);
			File[] dfiles=df.listFiles();
			System.out.println("dfiles.length "+dfiles.length);
			int length=0;
			for(File twoFiles:dfiles)
			{
				System.out.println("twoFiles.getPath() "+twoFiles.getPath());	
				File subFile=new File(twoFiles.getPath());
				{
					File[] trainingDocs=subFile.listFiles();
					length+=trainingDocs.length;
				}
			}
			System.out.println("The no of docs "+length);
			return length;	
		}
		double applyMultinomialB(double prior, double[] p) 
		{
		// TODO Auto-generated method stub
		double score=(Math.log(prior)/Math.log(2));
		System.out.println("score "+score);
			for(double it:p)
			{
				if(it!=0)
			score+=(Math.log(it)/Math.log(2));
			}
			
			return (-score);
		}
		
		HashMap<String, Integer> tokenize(String concatenatedText) throws Exception {
			// TODO Auto-generated method stub
			HashMap<String,Integer> spam_HashMap=new HashMap<String,Integer>();
			String strLine;
			String fileData="";
			BufferedReader br=new BufferedReader(new FileReader(concatenatedText));
				while((strLine=br.readLine())!=null)
				{
				fileData+=strLine+" ";
				}
				fileData=fileData.replaceAll("\\<.*?>",""); 
				fileData=fileData.replaceAll("'s", ""); 
				fileData=fileData.replaceAll("[0-9]+","");
				fileData=fileData.replaceAll("[-+^<>:,?';=%#&~`$!@*_)/(}{]","");
				fileData=fileData.replaceAll("\\.\\s", "");
				fileData=fileData.replaceAll(".$",""); 
				fileData=fileData.toLowerCase();
			StringTokenizer stk=new StringTokenizer(fileData);
				while(stk.hasMoreTokens())
				{
				String token=stk.nextToken();
					if(spam_HashMap.get(token)==null)
					{
						spam_HashMap.put(token,1);
					}
					else
					{
					spam_HashMap.put(token,(spam_HashMap.get(token)+1));
					}
				}
				//System.out.println("spam_HashMap "+spam_HashMap);
			return spam_HashMap;
		}

}
					
					
					
				/*	double[] p = new double[(int) B];
					double totalTokensInSpam = 0;
					double Nc=countDocsInClass(classp);
					double prior=Nc/n;
					System.out.println("Nc "+Nc);
					System.out.println("n "+n);
					System.out.println("prior "+prior);
					String concatenatedText=concatenateTextDocsInClass(classp);
					HashMap<String,Integer> tSHM=tokenize(concatenatedText);
					//Total Tokens with repitions in Spam
						for(double f:tSHM.values())
						{
						totalTokensInSpam+=f;
						}
						
						System.out.println("Total tokens in sub-doc "+totalTokensInSpam);
						int i=0;
						System.out.println("Total tokens in vocab with repitition "+tVHM.size());
						
						for (String key : tVHM.keySet()) 
						{
						System.out.println("Key tVHM==================== " +key);
						break;
						}
						
						for(Entry<String, Integer> vocab:tVHM.entrySet())
						{
						int power=vocab.getValue();
						String key=vocab.getKey();

						System.out.println("the value of the term is the power "+power);
						System.out.println("the key of the term is "+key);
						System.out.println("tSHM.get(key) "+tSHM.get(key) );
						
						if(tSHM.get(key)!=null)
								{
								double countTokensOfTerm=tSHM.get(key);
								System.out.println("Tct "+countTokensOfTerm);
								System.out.println("(totalTokensInSpam+B) "+(totalTokensInSpam+B));
								double num=(countTokensOfTerm+1);
								double den=(totalTokensInSpam+B);
								p[i]=num/den;
								i++;
								}
						}
						
						score[k]=applyMultinomialB(prior,p);
						k++;
						}
				
						for(int i=0;i<score.length;i++)
						{
							if(score[i]>maxScore)
							{
								maxScore=score[i];
								finalClass=i;
							}
						}
						System.out.println("FinalClass "+finalClass);
			
		}
	
		void delete(String path)
		{
			File del=new File(path);
			del.delete();
		}*/
		
		

		

	
	
	
	
	
	
