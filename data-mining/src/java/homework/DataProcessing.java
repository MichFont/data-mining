package java.homework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import weka.core.Instances;

public class DataProcessing {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Please enter in the name of a .arff file:" );
		String fileName = in.nextLine();
		try {
			BufferedReader inputFile = new BufferedReader(new FileReader(fileName));
			Instances data = new Instances(inputFile);
			
			System.out.println("Enter the column number to normalize: ");
			int colNum = in.nextInt();
			
			System.out.println("Enter in the number of bins: ");
			int numBins = in.nextInt();
			
			double[] normalized = normalizeData(data, colNum);
			int[] binned = binnedData(data, colNum, numBins);
			double[] covariances = createCovarianceValues(data);
			
			//Print out the data
			System.out.println("Original\tNormalized\tBinned");
			for(int i = 0; i < data.numInstances(); i++) {
				double current = data.instance(i).value(colNum);
				System.out.println(current + "\t" + normalized[i] + "\t" + binned[i]);
			}
			System.out.println("Covariance: ");
			for(int i = 0; i < data.numAttributes() - 1; i++) {
				System.out.println(covariances[i]);
			}
			
			//Setting the output class to be the last attribute column
			data.setClassIndex(data.numAttributes() - 1);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * This method takes in the data and a column number and normalizes the data
	 */
	public static double[] normalizeData(Instances data, int colNum) {		
		//Find the Min and Max for the column
		double min = 1000000000.0;
		double max = -1000000000.0;
		for(int i = 0; i < data.numInstances(); i++) {
			double current = data.instance(i).value(colNum);
			if(current < min) {
				min = current;
			}
			if(current > max) {
				max = current;
			}
		}
		
		double[] normalizedData = new double[data.numInstances()];
		for(int i = 0; i < data.numInstances(); i++) {
			double current = data.instance(i).value(colNum);
			normalizedData[i] = (current - min)/(max - min);
		}
		
		return normalizedData;
	}
	
	/*
	 * This method takes in the data, a column number and the number of categories and then sorts the data into bins
	 */
	public static int[] binnedData(Instances data, int colNum, int numBins) {
		//Find the Min and Max for the column
		double min = 1000000000.0;
		double max = -1000000000.0;
		for(int i = 0; i < data.numInstances(); i++) {
			double current = data.instance(i).value(colNum);
			if(current < min) {
				min = current;
			}
			if(current > max) {
				max = current;
			}
		}
		
		double binSize = (max - min)/numBins;
		
		int[] binnedData = new int[data.numInstances()];
		for(int i = 0; i < data.numInstances(); i++) {
			double current = data.instance(i).value(colNum);
			double upperBound = binSize + min;
			int binNum = 0;
			while(upperBound < current) {
				upperBound += binSize;
				if(upperBound >= max) {
					binNum = numBins - 1;
					break;
				}
				binNum++;
			}
			binnedData[i] = binNum;
		}
		
		return binnedData;
	}
	
	/*
	 * This method takes in the data, normalizes the data and then calculates the covariance of each variable
	 */
	public static double[] createCovarianceValues(Instances data) {
		double[] averages = new double[data.numAttributes()];
		double[][] normalizedData = new double[data.numAttributes()][data.numInstances()];
		
		//First pass to get the normalizedData and averages
		for(int i = 0; i < data.numAttributes(); i++) {
			normalizedData[i] = normalizeData(data, i);
			double average = 0;
			for(int j = 0; j < data.numInstances(); j++) {
				average += normalizedData[i][j];
			}
			averages[i] = average/data.numInstances();
			System.out.println(averages[i]);
		}
		
		//Second pass to compute the covariances
		double[] covariances = new double[data.numAttributes() - 1];
		for(int i = 0; i < data.numAttributes() - 1; i++) {
			double covariance = 0;
			double xMean = averages[i];
			double yMean = averages[data.numAttributes() - 1];
			for(int j = 0; j < data.numInstances(); j++) {
				covariance += ((normalizedData[i][j] - xMean)*(normalizedData[data.numAttributes() - 1][j] - yMean));
			}
			covariances[i] = covariance/(data.numInstances() - 1);
		}
		
		return covariances;
	}
}
