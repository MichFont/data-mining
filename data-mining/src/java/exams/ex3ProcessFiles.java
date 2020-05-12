package java.exams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class ex3ProcessFiles {

	//This program will take in the original .arff file and convert the file to the corresponding files 
	//needed to solve the problems to be tackled by the final project 
	public static void main(String[] args) {
		String startingFileName = "CleanedProjectData.arff";
		String stage1Name = "RetentionData.arff";
		String sportsName = "FullAthleteRetentionData.arff";
		String sportsName2 = "AthleteRetentionData.arff";
		String ethnicName = "EthnicRetentionData.arff";
		String stateRetName = "HomeStateRetentionData.arff";
		
		//The method calls below should be commented out once they have processed the data
		removeInstancesWithNoSport(startingFileName, sportsName, 8);
		removeAdditionalTs(startingFileName, stage1Name);
		removeInstancesWithNoSport(stage1Name, sportsName2, 2);
		removeInstancesWithNoEthnic(stage1Name, ethnicName);
		removeInstancesWithNoState(stage1Name, stateRetName);

	}
	
	//This method scrubs instances that do not have a home state attached to their record
	private static void removeInstancesWithNoState(String inputFile, String outputFile) {
		try {
			BufferedReader dataFile = new BufferedReader(new FileReader(inputFile));
			Instances data = new Instances(dataFile);
			int toCheck = 0;
			for(int i = 0; i < data.numAttributes(); i++) {
				if(data.instance(0).attribute(i).name().equals("MAL-STATE")) {
					toCheck = i;
					break;
				} 
			}
			
			int removedCount = 0;
			for(int i = 0; i < data.numInstances(); i++) {
				if(data.instance(i).value(toCheck) == 0.0 || Double.isNaN(data.instance(i).value(toCheck))) {
					data.remove(i);
					removedCount++;
				}
			}
			System.out.println(removedCount + " instances were removed from the data");
			
			Instances dataSet = data;
			ArffSaver saver = new ArffSaver();
			saver.setInstances(dataSet);
			saver.setFile(new File(outputFile));
			saver.writeBatch();
			
			System.out.println("Write complete");
		} 
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}

	//This method scrubs instances that do not have an ethnicity attached to their record
	private static void removeInstancesWithNoEthnic(String inputFile, String outputFile) {
		try {
			BufferedReader dataFile = new BufferedReader(new FileReader(inputFile));
			Instances data = new Instances(dataFile);
			int toCheck = 0;
			for(int i = 0; i < data.numAttributes(); i++) {
				if(data.instance(0).attribute(i).name().equals("RACE-DESC")) {
					toCheck = i;
					break;
				} 
			}
			
			int removedCount = 0;
			for(int i = 0; i < data.numInstances(); i++) {
				if(data.instance(i).value(toCheck) == 0.0 || data.instance(i).value(toCheck) == 8.0) {
					data.remove(i);
					removedCount++;
				}
			}
			System.out.println(removedCount + " instances were removed from the data");
			
			Instances dataSet = data;
			ArffSaver saver = new ArffSaver();
			saver.setInstances(dataSet);
			saver.setFile(new File(outputFile));
			saver.writeBatch();
			
			System.out.println("Write complete");
		} 
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}

	//This method goes through and removes all instances that did not have any 1's in any sports column
	private static void removeInstancesWithNoSport(String inputFile, String outputFile, int numTs) {
		try {
			BufferedReader dataFile = new BufferedReader(new FileReader(inputFile));
			Instances data = new Instances(dataFile);
			
			int start = 0;
			int end = 0;
			for(int i = 0; i < data.numAttributes(); i++) {
				if(data.instance(0).attribute(i).name().equals("T1-BASEBALL")) {
					start = i; 
				} else if(data.instance(0).attribute(i).name().equals("T1-VOLLEYBALL")) {
					end = i; 
				} 
			}
			
			int removedCount = 0;
			for(int i = 0; i < data.numInstances(); i++) {
				int sportCount = 0;
				for(int j = 0; j < numTs-1; j++) {
					for(int k = start; k <= end; k++) {
						if(data.instance(i).value(k) == 1) {
							sportCount++;
						}
					}
					if(j != numTs-2 && start+25 < 234) {
						start += 25;
						end += 25;
					}
				}
				
				if(sportCount == 0) {
					data.remove(i);
					removedCount++;
				}
			}
			System.out.println(removedCount + " instances were removed from the data");
			
			Instances dataSet = data;
			ArffSaver saver = new ArffSaver();
			saver.setInstances(dataSet);
			saver.setFile(new File(outputFile));
			saver.writeBatch();
			
			System.out.println("Write complete");
		} 
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}

	/*This method extracts all global, T1, T2, and T3 data and writes it to the output file*/
	public static void removeAdditionalTs(String inputFile, String outputFile) {
		try {
			BufferedReader dataFile = new BufferedReader(new FileReader(inputFile));
			Instances data = new Instances(dataFile);
			
			
			int endPoint = 0;
			for(int i = 0; i < data.numAttributes(); i++) {
				if(data.instance(0).attribute(i).name().equals("T4-ATTEMPT-HRS")) {
					endPoint = i; 
					break;
				}
			}
			
			System.out.println("EndPoint: " + endPoint + " Number of attributes: " + data.numAttributes());
			
			//Before doing this next part, I kept track of the endPoint and numAttributes as an aside 
			//and then hard-coded them into options
			Remove remove = new Remove();
			String[] options = new String[2];
			options[0] = "-R";
			options[1] = "109-234";
			remove.setOptions(options);
			remove.setInputFormat(data);
			Instances newData = Filter.useFilter(data, remove);			
			
			Instances dataSet = newData;
			ArffSaver saver = new ArffSaver();
			saver.setInstances(dataSet);
			saver.setFile(new File(outputFile));
			saver.writeBatch();
			
			System.out.println("Write complete");
		} 
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
		
	}

}
