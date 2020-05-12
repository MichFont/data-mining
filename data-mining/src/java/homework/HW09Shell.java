package java.homework;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.core.Instances;

public class HW09Shell {
	public static void main(String [] args)
	{
		try
		{
			Instances data = check("CombinedDataWithGraduateYN.arff");
		}
		catch(Exception e)
		{
			System.out.println("Error" + e);
		}
		
	}

	public static Instances check(String fileName) throws Exception
	{
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			//Get the data
			Instances data = new Instances(reader);
			System.out.println(data.numAttributes() + " " + data.numInstances());
			for (int i= 2000; i <2010; i++)
				System.out.println(data.instance(i));
			reader.close();
			return data;
		}
		catch(Exception e)
		{
			throw e;
		}

	}
}
