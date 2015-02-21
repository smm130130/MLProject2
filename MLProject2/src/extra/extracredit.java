package extra;

class extracredit 
{
	public static void main(String[] args) throws Exception
	{
		String[] arguments=new String[2];
		int i=0;
		for(String s:args)
		{
			arguments[i]=s;
			System.out.println("argu[i] "+arguments[i]);
			i++;
		}
		new trainMultinomial(arguments[0],arguments[1]);
	}
}
		
		

		

	
	
	
	
	
	
