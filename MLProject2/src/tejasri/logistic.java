package tejasri;

//***********author: Tejasri Pavuluri
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

class logisticR
{
	//For individual Docs
	Vector<HashMap<String,Integer>> array_HashMaps=new Vector<HashMap<String,Integer>>();
	static Vector<Integer> spamORHam=new Vector<Integer>();
	
	//For complete Vocabulary
	LinkedHashMap<String,Integer> vocab_Hashmap;
	Vector<String> uniqueTokens_vocab_array=new Vector<String>();
	//directoryPath
	/*String directoryPath="C:\\Users\\Tejasri\\Desktop\\train";
	String testPath="C:\\Users\\Tejasri\\Desktop\\test";*/
	 // Now matrix dimensions
	public static double[][] x;
	int rows;int cols;

	//weights
	public static double[] w;
	// while computing weights
	
	
	double d=1;
	String Class;public static int positive=0;public static int negative=0;double accuracy;
	logisticR(String directoryPath,String testPath,int iterations,double lambda,double eta) throws Exception
	{
		String concatenatedDirectoryPath=concatenated_file_Vocabulary(directoryPath);
		vocab_Hashmap=tokenizeVocabularytoHASHMap(concatenatedDirectoryPath);
		uniqueTokens_vocab_array.add("dummyString");
		for(Entry<String, Integer> vocabu_HashMap:vocab_Hashmap.entrySet())
		{
			uniqueTokens_vocab_array.add(vocabu_HashMap.getKey());
		}
		//spamORHam.add(3);
		create_Vector_Hashmaps_individual_Docs(directoryPath);
		// Now iterate through each doc and find out its hashMap and add that to array_HashMaps.
		
		//counting total No of Documents in the training sets=No of rows in matrix
		rows=countDocs(directoryPath);
		cols=(vocab_Hashmap.size()+1+2);
		initialize();
		
		//Now iterate through each doc and enter that doc_tokens into the matrix
		matrix_formation();
		//Thread.sleep(50);
		//display_matrix();
		
		// weights declaration
		w=new double[cols-2];
		init_weights(w);
		loop(iterations,lambda,eta);
		applyLogistic(testPath);
		
	}
	void loop(int iterations,double lambda,double eta) throws Exception
	{
	//iteration++;
	//System.out.println("iteration "+iteration);
	for(int i=0;i<iterations;i++)
	{
	compute_Probabilities();
	//display_matrix();
	compute_weights(lambda,eta);
	display_weights();
	}
	//Thread.sleep(5000);
	
	}
	
	void applyLogistic(String testPath) throws Exception
	{
	File df=new File(testPath);
	File[] dfiles=df.listFiles();
	for(File twofiles:dfiles)
		{
		String classp=twofiles.getPath();
		File[] subfiles=twofiles.listFiles();
		for(File subfile:subfiles)
			{
			LinkedHashMap<String,Integer> lhm=tokenizeVocabularytoHASHMap(subfile.getPath());
			double exp=0;
			for(Entry<String,Integer> entry:lhm.entrySet())
			{
				String token=entry.getKey();
				Integer value=entry.getValue();
				//*********************************** just see here if any prob occurs ********************************//
				for(int i=0;i<uniqueTokens_vocab_array.size();i++)
				{
					if(token.equals(uniqueTokens_vocab_array.get(i)))
					{
						if(w[i]<0)
						{
							exp+=((w[i]*value));	
						}
						else 
						{
						exp+=Math.log((w[i]*value)+1);
						}
					}
				}
			}
			System.out.println(" The exp values "+exp);
			if(exp>0)
				{
				Class=testPath+"/ham";
				}
			else
				{
				Class=testPath+"/spam";
				}
			if(Class.equals(classp))
				{positive++;}
			else
				{
				negative++;
				System.out.println("Class (negative) "+Class);
				System.out.println("classp (negative) "+classp);
				}
			}
		  }
	   
	System.out.println(" The positive values "+positive);
	System.out.println(" The negative values "+negative);
	double sum=(positive+negative);
	double accuracy=((positive)/sum);
	System.out.println(" accuracy "+(accuracy*100));
	}
	void display_weights()
	{
		for(int i=0;i<(cols-2);i++)
		{
			System.out.print(w[i]);System.out.print("\t");
		}
		System.out.print("\n");	
		
	}
	
	void compute_weights(double lambda,double eta) throws Exception
	{
		int counter=0;
		for(int i=0;i<(cols-2);i++)
		{
			
			double delta=0;
			for(int j=0;j<rows;j++)
			{
				
				delta+=(x[j][i]*(x[j][cols-2]-x[j][cols-1]));
				
			}
			//System.out.println("delta1 "+delta);
			delta=delta+(w[i]*lambda);
			//System.out.println("delta2 "+delta);
			w[i]=w[i]+(delta*eta);
			}
		}
	
	// Initializing weights
	void init_weights(double[] w)
	{
		Random r=new Random();
		for(int i=0;i<(cols-2);i++)
		{
			w[i]=0.4;
		}
	}
	
	void display_matrix()
	{
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<cols;j++)
			{
				System.out.print(x[i][j]);System.out.print("\t");
			}
			System.out.print("\n");
		}
	}
	void matrix_formation()
	{
		int doc_no=-1;
		for(HashMap<String,Integer> array_HashMaps1:array_HashMaps)
		{
			 doc_no++;
			 int Class_value=spamORHam.get(doc_no);
			 place_y_in_matrix(Class_value,doc_no,(cols-2));
			 for(Entry<String,Integer> doc1_map:array_HashMaps1.entrySet())
			 {
				 String token= doc1_map.getKey();int value=doc1_map.getValue();
				 //System.out.println(" value "+value);
				 int Enter=1;
				 for(int i=0;i<uniqueTokens_vocab_array.size();i++)
				 {
					if(Enter==1)
					{
						if((uniqueTokens_vocab_array.get(i)).equals(token))
						{
							Enter=0;
							place_token_in_matrix(doc_no,i, value);
						}
					}
				 }
			 }
		}
	}
	
	//
	void compute_Probabilities()
	{
		for(int i=0;i<rows;i++)
		{
		double w=compute_Z(i);
		double exp=Math.pow(Math.E,w);
		double prob=get_Prob(exp,i);
		x[i][cols-1]=prob;
		}
	}
	
	double get_Prob(double exp,int i)
	{
		
		
		 double	prob=(exp/(1+exp));
		
		
	return prob;	
	}
	//Computing exponential term
	double compute_Z(int doc_no)
	{
		double exp=0;
		for(int i=0;i<(cols-2);i++)
		{
			
		if(x[doc_no][i]!=0)
			{
				if(((w[i]*(x[doc_no][i]))+1)<0)
				{
					exp+=((w[i]*(x[doc_no][i])));	
				}
				else
				{
			exp+=Math.log((w[i]*(x[doc_no][i]))+1);
				}
			}
		}
		return (exp);
	}
	
	//Filling the contents of the matrix
	void place_token_in_matrix(int row,int col,int value)
	{
		x[row][col]=value;
		//System.out.println("x["+row+"]["+col+"]"+x[row][col]);
	}
	
	void place_y_in_matrix(int y,int row,int col)
	{
		x[row][col]=y;
	}
	//Initialize the matrix
	void initialize()
	{
		x=new double[rows][cols];
		for(int i=0;i<rows;i++)
			x[i][0]=1;
	}
	
	int countDocs(String directoryPath)
	{
		//System.out.println(" directoryPath "+directoryPath);
		File df=new File(directoryPath);
		File[] dfiles=df.listFiles();
		//System.out.println("dfiles.length "+dfiles.length);
		int length=0;
		for(File twoFiles:dfiles)
		{
			//System.out.println("twoFiles.getPath() "+twoFiles.getPath());	
			File subFile=new File(twoFiles.getPath());
			{
				File[] trainingDocs=subFile.listFiles();
				length+=trainingDocs.length;
			}
		}
		//System.out.println("The no of docs "+length);
		return length;	
	}
	
	void create_Vector_Hashmaps_individual_Docs(String directoryPath) throws Exception
	{
		File df=new File(directoryPath);
		File[] dfiles=df.listFiles();
		for(File twofiles:dfiles)
		{
			File[] subfiless=twofiles.listFiles();
			for(File subfiles:subfiless)
			{
				String p= concatenated_individual_docs(subfiles.getPath());
				HashMap<String,Integer> h=tokenizeVocabularytoHASHMap(p);
				array_HashMaps.add(h);
				if(twofiles.getPath().equals(directoryPath+"/ham"))
				{
					/*System.out.println("In create_Vector_Hashmaps_individual_Docs()");
					System.out.println("spamORHam.add(0) ");*/
					spamORHam.add(1);	
				}
				else if(twofiles.getPath().equals(directoryPath+"/spam"))  
				{
					spamORHam.add(0);
				}
			}
		}
	}
	
	String concatenated_individual_docs(String classp) throws Exception
	{
		// TODO Auto-generated method stub
		String concatenate_individualDoc="concate.txt";
		PrintWriter pw=new PrintWriter(new FileOutputStream(concatenate_individualDoc));
		BufferedReader br=new BufferedReader(new FileReader(classp));
		String line=br.readLine();
			while(line!=null)
			{
			pw.print(line);	pw.print(" ");line=br.readLine();
			}
		br.close();
		pw.close();
		return concatenate_individualDoc;
	}
	
	String concatenated_file_Vocabulary(String directoryPath) throws Exception
	{
		File df=new File(directoryPath);
		File[] dfiles=df.listFiles();
		//String path=directoryPath+"dconcat.txt";
		String path="dconcat.txt";
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
						pw.print(line); pw.print(" ");	line=br.readLine();
						}
					}	
					br.close();
				}
			}
		pw.close();
	return path;
	}
	
	
	LinkedHashMap<String, Integer> tokenizeVocabularytoHASHMap(String concatenatedDirectoryPath) throws Exception 
	{
		LinkedHashMap<String,Integer> Vocabu_HashMap=new LinkedHashMap<String,Integer>();
		String strLine;
		String fileData="";
		
		BufferedReader br=new BufferedReader(new FileReader(concatenatedDirectoryPath));
			while((strLine=br.readLine())!=null)
			{
			fileData+=strLine+" ";
			}
			fileData=fileData.replaceAll("\\<.*?>"," "); 
			fileData=fileData.replaceAll("'s", " "); 
			fileData=fileData.replaceAll("[-+^:,?';=%#&~`$!@*_)/(}{]"," ");
			fileData=fileData.replaceAll("\\.\\s", " ");
			fileData=fileData.replaceAll(".$"," "); 
		StringTokenizer stk=new StringTokenizer(fileData," ");
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

}

class logistic
{
	public static void main(String[] args) throws Exception
	{
		String[] argu=new String[5];
		int i=0;
		for(String s:args)
		{
			argu[i]=s;
			System.out.println("argu["+i+"] "+argu[i]);
			i++;
		}
		int it=Integer.parseInt(argu[2]);
		double lambda= Double.parseDouble(argu[3]);
		double eta= Double.parseDouble(argu[4]);
		long start=System.currentTimeMillis();
		logisticR l=new logisticR(argu[0],argu[1],it,lambda,eta);
		long end=System.currentTimeMillis();
		System.out.println("Time taken "+(end-start));
	}
}