package java.basic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import weka.core.Instances;

public class ReadingArffFiles {
	public static void main(String[] args) {
		try {
			BufferedReader inputFile = new BufferedReader(new FileReader("weather.arff"));
			Instances data = new Instances(inputFile);
			System.out.println("Number of instances: " + data.numInstances());
			System.out.println("Number of attributes: " + data.numAttributes());
			
			//Setting the output class to be the last attribute column
			data.setClassIndex(data.numAttributes() - 1);
			System.out.println(data.classIndex());
			
			
			//Extracting information about instance 4
			System.out.println("Attribute 0 for instance 4: " + data.instance(4).stringValue(0));
			
			for(int i = 0; i < data.numAttributes(); i++) {
				System.out.println(data.instance(0).value(i));
				
			}
			//Information about the attributes
			System.out.println("Number of values for attribute 0:" + data.attribute(0).numValues());
			
			System.out.println("Name of attribute 0: " + data.attribute(0).name());
			for(int i = 0; i < data.attribute(0).numValues(); i++) {
				System.out.println(data.attribute(0).value(i));
			}
			if(data.attribute(0).isNominal()) {
				System.out.println("It is a nominal attribute");
			} else {
				System.out.println("It is a numeric attribute");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
