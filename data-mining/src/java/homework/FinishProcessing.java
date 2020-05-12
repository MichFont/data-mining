package java.homework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class FinishProcessing {

	public static void main(String[] args) {
		String startingFileName = "CleanedProjectData.arff";
		String stage1Name = "EssentialProjData.arff";
		String trainingSetName = "TrainingData.arff";
		String testingSetName = "TestingData.arff";
		String trainingFinal = "TrainingDataFinal.arff";
		String testingFinal = "TestingDataFinal.arff";
		
		
		//The methods have been commented out since the files have already been written.
		//Also due to having to add in the @relation section and @data by hand after the second method completed
//		removeAdditionalTs(startingFileName, stage1Name);
//		splitData(stage1Name, trainingSetName, testingSetName);
//		removeYears(trainingSetName, trainingFinal);
//		removeYears(testingSetName, testingFinal);
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(trainingFinal));
			Instances training = new Instances(reader);
			reader.close();
			
			BufferedReader reader2 = new BufferedReader(new FileReader(testingFinal));
			Instances testing = new Instances(reader2);
			reader2.close();
			
			
			int graduateIndex = 0;
			for(int i = 0; i < training.numAttributes(); i++) {
				if(training.instance(0).attribute(i).name().equals("Graduate")) {
					graduateIndex = i;
					break;
				}
			}
			System.out.println("Graduate attrib index: " + graduateIndex) ;
						
			training.setClassIndex(graduateIndex);
			testing.setClassIndex(graduateIndex);
			
			J48 tree = new J48();
			tree.buildClassifier(training);
			
			NaiveBayes nbClassifier = new NaiveBayes();
			nbClassifier.buildClassifier(training);
			
			IBk knnClassifier = new IBk(5);
			knnClassifier.buildClassifier(training);
			
			int tnumCorrect = 0; //Decision Tree correctness count
			int nbnumCorrect = 0; //Naive Bayes correctness count
			int knumCorrect = 0; //KNN correctness count
			int majnumCorrect = 0; //Majority correctness count
			
			for(int i = 0; i < testing.numInstances(); i++) {
				Instance testInstance = new DenseInstance(testing.numAttributes());
				testInstance.setDataset(testing);
				
				for(int j = 0; j < testing.numAttributes(); j++) {
					if(j == graduateIndex) { 
						continue;
					}
					testInstance.setValue(testing.attribute(j), testing.instance(i).value(j));
				}
				
				//Take care of the DT printout
				int tOutC = (int)tree.classifyInstance(testInstance);
				String decision = testing.attribute(testing.classIndex()).value(tOutC);
				boolean correct = tOutC == testing.instance(i).value(graduateIndex);
				if(correct) {
					tnumCorrect++;
				}
				System.out.println("DT: Instance " + i + " classified as " + decision + " correct: " +  correct);
				
				//Take care of the NB printout
				int nbOutC = (int)nbClassifier.classifyInstance(testInstance);
				String nbDec = testing.attribute(testing.classIndex()).value(nbOutC);
				boolean nbCorrect = nbOutC == testing.instance(i).value(graduateIndex);
				if(nbCorrect) {
					nbnumCorrect++;
				}
				System.out.println("NB: Instance " + i + " classified as " + nbDec + " correct: " +  nbCorrect);
				
				//Take care of the KNN printout
				int knnOutC = (int)knnClassifier.classifyInstance(testInstance);
				String knnDec = testing.attribute(testing.classIndex()).value(knnOutC);
				boolean knnCorrect = knnOutC == testing.instance(i).value(graduateIndex);
				if(knnCorrect) {
					knumCorrect++;
				}
				System.out.println("KNN: Instance " + i + " classified as " + knnDec + " correct: " +  knnCorrect);
				
				//Now we compute a simple majority to classify the Instance and then print it out
				int majOutC = getMajority(tOutC, nbOutC, knnOutC);
				String majDec = testing.attribute(testing.classIndex()).value(majOutC);
				boolean majCorrect = majOutC == testing.instance(i).value(graduateIndex);
				if(majCorrect) {
					majnumCorrect++;
				}
				System.out.println("Majority: Instance " + i + " classified as " + majDec + " correct: " + majCorrect);;
			}
			int tnumWrong = testing.numInstances()-tnumCorrect;
			System.out.println("\nDecision tree: There were: " + tnumCorrect + " correctly classified instances and " 
					+ tnumWrong + " incorrectly classified instances");
			
			int nbnumWrong = testing.numInstances()-nbnumCorrect;
			System.out.println("Naive bayes: There were: " + nbnumCorrect + " correctly classified instances and " 
					+ nbnumWrong + " incorrectly classified instances");
			
			int knumWrong = testing.numInstances()-knumCorrect;
			System.out.println("KNN: There were: " + knumCorrect + " correctly classified instances and " 
					+ knumWrong + " incorrectly classified instances");
			
			int majnumWrong = testing.numInstances()-majnumCorrect;
			System.out.println("Majority: There were: " + majnumCorrect + " correctly classified instances and " 
					+ majnumWrong + " incorrectly classified instances\n");
			
			///////////////////////////////////////////////////////////////////////////////////
			// Repeat the process from above but using T2-Academic-standing instead of Graduate
			///////////////////////////////////////////////////////////////////////////////////
			
			int t2StandingIndex = 0;
			for(int i = graduateIndex; i < training.numAttributes(); i++) {
				if(training.instance(0).attribute(i).name().equals("T2-ACADEMIC-STAND")) {
					t2StandingIndex = i;
					break;
				}
			}
			System.out.println("T2 Academic Standing attrib index: " + t2StandingIndex); 
			
			//Set class index to T2-academic standing
			training.setClassIndex(t2StandingIndex);
			testing.setClassIndex(t2StandingIndex);
			
//			//Rebuild classifiers
			tree.buildClassifier(training);
			nbClassifier.buildClassifier(training);
			System.out.println(nbClassifier + "\n");
			knnClassifier.buildClassifier(training);
//			
//			int tnumCorrect2 = 0; //Decision Tree correctness count
//			int nbnumCorrect2 = 0; //Naive Bayes correctness count
//			int knumCorrect2 = 0; //KNN correctness count
//			int majnumCorrect2 = 0; //Majority correctness count
//			
//			for(int i = 0; i < testing.numInstances(); i++) {
//				Instance testInstance = new DenseInstance(testing.numAttributes());
//				testInstance.setDataset(testing);
//				
//				for(int j = 0; j < testing.numAttributes(); j++) {
//					if(j == t2StandingIndex) { 
//						continue;
//					}
//					testInstance.setValue(testing.attribute(j), testing.instance(i).value(j));
//				}
//				
//				//Take care of the DT printout
//				int tOutC = (int)tree.classifyInstance(testInstance);
//				String decision = testing.attribute(testing.classIndex()).value(tOutC);
//				boolean correct = tOutC == testing.instance(i).value(t2StandingIndex);
//				if(correct) {
//					tnumCorrect2++;
//				}
//				System.out.println("DT: Instance " + i + " classified as " + decision + " correct: " +  correct);
//				
//				//Take care of the NB printout
//				int nbOutC = (int)nbClassifier.classifyInstance(testInstance);
//				String nbDec = testing.attribute(testing.classIndex()).value(nbOutC);
//				boolean nbCorrect = nbOutC == testing.instance(i).value(t2StandingIndex);
//				if(nbCorrect) {
//					nbnumCorrect2++;
//				}
//				System.out.println("NB: Instance " + i + " classified as " + nbDec + " correct: " +  nbCorrect);
//				
//				//Take care of the KNN printout
//				int knnOutC = (int)knnClassifier.classifyInstance(testInstance);
//				String knnDec = testing.attribute(testing.classIndex()).value(knnOutC);
//				boolean knnCorrect = knnOutC == testing.instance(i).value(t2StandingIndex);
//				if(knnCorrect) {
//					knumCorrect2++;
//				}
//				System.out.println("KNN: Instance " + i + " classified as " + knnDec + " correct: " +  knnCorrect);
//				
//				//Now we compute a simple majority to classify the Instance and then print it out
//				int majOutC = getMajority(tOutC, nbOutC, knnOutC);
//				String majDec = testing.attribute(testing.classIndex()).value(majOutC);
//				boolean majCorrect = majOutC == testing.instance(i).value(t2StandingIndex);
//				if(majCorrect) {
//					majnumCorrect2++;
//				}
//				System.out.println("Majority: Instance " + i + " classified as " + majDec + " correct: " + majCorrect);;
//			}
//			int tnumWrong2 = testing.numInstances()-tnumCorrect2;
//			System.out.println("\nDecision tree: There were: " + tnumCorrect2 + " correctly classified instances and " 
//					+ tnumWrong2 + " incorrectly classified instances");
//			
//			int nbnumWrong2 = testing.numInstances()-nbnumCorrect2;
//			System.out.println("Naive bayes: There were: " + nbnumCorrect2 + " correctly classified instances and " 
//					+ nbnumWrong2 + " incorrectly classified instances");
//			
//			int knumWrong2 = testing.numInstances()-knumCorrect2;
//			System.out.println("KNN: There were: " + knumCorrect2 + " correctly classified instances and " 
//					+ knumWrong2 + " incorrectly classified instances");
//			
//			int majnumWrong2 = testing.numInstances()-majnumCorrect2;
//			System.out.println("Majority: There were: " + majnumCorrect2 + " correctly classified instances and " 
//					+ majnumWrong2 + " incorrectly classified instances\n");
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}

	private static int getMajority(int tOutC, int nbOutC, int knnOutC) {
		if(knnOutC == nbOutC || knnOutC == tOutC) {
			return knnOutC;
		} else if (nbOutC == tOutC) {
			return nbOutC;
		} else {
			return tOutC; //Since tOutC guessed the most correct
		}
	}

	/*This method extracts all global, T1, and T2 data and writes it to the output file*/
	public static void removeAdditionalTs(String inputFile, String outputFile) {
		try {
			BufferedReader dataFile = new BufferedReader(new FileReader(inputFile));
			Instances data = new Instances(dataFile);
			
			
			int endPoint = 0;
			for(int i = 0; i < data.numAttributes(); i++) {
				if(data.instance(0).attribute(i).name().equals("T3-ATTEMPT-HRS")) {
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
			options[1] = "85-234";
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
	
	/*This method splits the input file into testing and training data based on years*/
	public static void splitData(String inputFile, String training, String testing) {
		try {
			BufferedReader dataFile = new BufferedReader(new FileReader(inputFile));
			Instances data = new Instances(dataFile);
			
			int endOfTrain = 0;
			for(int i = 1; i < data.numInstances(); i++) {
				if(data.instance(i).value(0) == 2.0) {
					endOfTrain = i;
					break;
				}
			}
			System.out.println("End of 2010-2011: " + endOfTrain);
			
			//Manually appended the @relation section and @data above the instances
			FileWriter fwriter = new FileWriter(training, false);
			for(int i = 0; i < endOfTrain; i++) {
				fwriter.write(data.instance(i).toString());
				fwriter.write("\n");
			}		
			fwriter.close();
			System.out.println("Train set created");
				
			//Manually appended the @relation section and @data above the instances
			FileWriter fwriter2 = new FileWriter(testing, false);
			for(int i = endOfTrain; i < data.numInstances(); i++) {
				fwriter2.write(data.instance(i).toString());
				fwriter2.write("\n");
			}		
			fwriter2.close();	
			System.out.println("Test set created");
			
		} 
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
			
		
	}
	
	/*this method removes the years from a file*/
	public static void removeYears(String inputFile, String outputFile) {
		try {
			BufferedReader dataFile = new BufferedReader(new FileReader(inputFile));
			Instances data = new Instances(dataFile);
			
			//Remove the Year 
			Remove remove = new Remove();
			String[] options = new String[2];
			options[0] = "-R";
			options[1] = "1";
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
