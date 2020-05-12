package java.homework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import weka.core.Instances;

public class Weather {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter in the name of a .arff file:" );
		String fileName = in.nextLine();
		try {
			BufferedReader inputFile = new BufferedReader(new FileReader(fileName));
			Instances data = new Instances(inputFile);
			
			//Setting the output class to be the last attribute column
			data.setClassIndex(data.numAttributes() - 1);
			
			for(int i = 0; i < data.classIndex(); i++) {
				//Print out attribute name and whether it is nominal or numeric
				if(data.attribute(i).isNominal()) {
					System.out.println(data.attribute(i).name() + " is a nominal attribute");
					System.out.println("\tyes\tno");
					calculateNominalData(data, i);
				} else {
					System.out.println(data.attribute(i).name() + " is a numeric attribute");
					calculateNumericData(data, i);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Instances data is the data we are working with
	 * int i is the attribute number
	 * 
	 * This method finds the maximum, minimum, and the average values for this attribute						
	 */
	private static void calculateNumericData(Instances data, int i) {
		double min = 12000000000.0;
		double max = 0.0;
		double average = 0.0;
		for(int j = 0; j < data.numInstances(); j++) {
			double current = data.instance(j).value(i);
			if(current < min) {
				min = current;
			}
			if(current > max) {
				max = current;
			}
			average += current;
		}
		average = average/data.numInstances();
		System.out.println("Minimum: " + min);
		System.out.println("Maximum: " + max);
		System.out.println("Average: " + average);
	}

	/*
	 * Instances data is the data we are working with
	 * int i is the attribute number
	 * 
	 * This method create a table of size m x n, and report on the number of instances 
	 * have each of the m values with each of the n output classes. For example, in the Weather 
	 * data set, there are 2 instances that have an attribute value of sunny and an output class of the yes.
	 */
	private static void calculateNominalData(Instances data, int i) {
		data.setClassIndex(data.numAttributes()-1);
		for(int j = 0; j < data.attribute(i).numValues(); j++) {
			System.out.print(data.attribute(i).value(j) + ":\t");
			int matchCount = 0;
			int outputCount = 0;
			for(int k = 0; k < data.numInstances(); k++) {
				if((int)data.instance(k).value(i) == j) {
					matchCount++;
					if(data.instance(k).value(data.classIndex()) == 0) {
						outputCount++;
					}
				}
			}
			System.out.println(outputCount + "\t" + (matchCount - outputCount));
			
		}
	}

	
}
