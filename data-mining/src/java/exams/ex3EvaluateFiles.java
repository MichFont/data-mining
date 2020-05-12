package java.exams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class ex3EvaluateFiles {

	//This program will take in the corresponding .arff files and then use them to gather the results to the problems
	public static void main(String[] args) {
		String retName = "RetentionData.arff";
		String retTrainName = "RetTrainingData.arff";
		String retTestName = "RetTestingData.arff";
		
		String sportsName = "FullAthleteRetentionData.arff";
		
		String sportsName2 = "AthleteRetentionData.arff";
		String smallSportTrainName = "SmallSportTrainingData.arff";
		String smallSportTestName = "SmallSportTestingData.arff";
		
		String ethnicName = "EthnicRetentionData.arff";
		String ethnicTrainName = "EthnicTrainingData.arff";
		String ethnicTestName = "EthnicTestingData.arff";
		
		String stateRetName = "HomeStateRetentionData.arff";
		String stateTrainName = "StateTrainingData.arff";
		String stateTestName = "StateTestingData.arff";
		
		//Comment out create* methods once they have been run to prevent overwriting issues and manual processing
		createTestAndTrainData(retName, retTrainName, retTestName);
		createTestAndTrainData(sportsName2, smallSportTrainName, smallSportTestName);
		createTestAndTrainData(ethnicName, ethnicTrainName, ethnicTestName);
		createTestAndTrainData(stateRetName, stateTrainName, stateTestName);
		
		evaluateRetention(retName, retTrainName, retTestName);
		evaluateStateRetention(stateRetName, stateTrainName, stateTestName);
		evaluateEthnicRetention(ethnicName, ethnicTrainName, ethnicTestName);
		evaluateSmallSportRetention(sportsName2, smallSportTrainName, smallSportTestName);
		evaluateBigSportRetention(sportsName);
	}

	//This method looks at the totals of graduate athletes and the total number of athletes per year
	public static void evaluateBigSportRetention(String fullFile) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fullFile));
			Instances data = new Instances(reader);
			reader.close();
			
			int start = 0;
			int end = 0;
			for(int i = 0; i < data.numAttributes(); i++) {
				if(data.instance(0).attribute(i).name().equals("T1-BASEBALL")) {
					start = i; 
				} else if(data.instance(0).attribute(i).name().equals("T1-VOLLEYBALL")) {
					end = i; 
				} 
			}
			System.out.println("T1-Basesball attrib index: " + start + " T1-Volleyball attrib index: " + end);
			
			int graduateIndex = 0;
			for(int i = 0; i < data.numAttributes(); i++) {
				if(data.instance(0).attribute(i).name().equals("Graduate")) {
					graduateIndex = i;
					break;
				}
			}
			System.out.println("Graduate attrib index: " + graduateIndex) ;
			
			
			int startActual = start;
			int endActual = end;
			
			int[] numAthletes10 = new int[4];
			int[] numAthletes11 = new int[4];
			int[] numAthletes12 = new int[4];
			int[] numGradAthletes10 = new int[4];
			int[] numGradAthletes11 = new int[4];
			int[] numGradAthletes12 = new int[4];
			for(int i = 0; i < data.numInstances(); i++) {
				boolean isAthlete = false;
				for(int j = 0; j < 8; j++) {
					for(int k = start; k < end; k++) {
						if(data.instance(i).value(k) == 1.0) {
							isAthlete = true;
							if(data.instance(i).value(0) == 0.0) {
								numAthletes10[(int)(j/2)]++;
								if(data.instance(i).value(graduateIndex) == 0.0) {
									numGradAthletes10[(int)(j/2)]++;
								}
							} else if(data.instance(i).value(0) == 1.0) {
								numAthletes11[(int)(j/2)]++;
								if(data.instance(i).value(graduateIndex) == 0.0) {
									numGradAthletes11[(int)(j/2)]++;
								}
							} else if(data.instance(i).value(0) == 2.0) {
								numAthletes12[(int)(j/2)]++;
								if(data.instance(i).value(graduateIndex) == 0.0) {
									numGradAthletes12[(int)(j/2)]++;
								}
							}
						}
					}
					start += 25;
					end +=25;
				}
				start = startActual;
				end = endActual;
			}
			double percentGraduated2010 = (double)(numGradAthletes10[0] + numGradAthletes10[1] + numGradAthletes10[2] + numGradAthletes10[3])/(double)(numAthletes10[0] + numAthletes10[1] + numAthletes10[2] + numAthletes10[3]);
			double percentGraduated2011 = (double)(numGradAthletes11[0] + numGradAthletes11[1] + numGradAthletes11[2] + numGradAthletes11[3])/(double)(numAthletes11[0] + numAthletes11[1] + numAthletes11[2] + numAthletes11[3]);
			double percentGraduated2012 = (double)(numGradAthletes12[0] + numGradAthletes12[1] + numGradAthletes12[2] + numGradAthletes12[3])/(double)(numAthletes12[0] + numAthletes12[1] + numAthletes12[2] + numAthletes12[3]);
			double avergaePercentGrad = (percentGraduated2010+percentGraduated2011+percentGraduated2012)/3;
			System.out.println("The number of Athletes from the 2010 data in T1-T2 was: " + numAthletes10[0] +
					"\nThe number of Athletes from the 2010 data in T3-T4 was: " + numAthletes10[1] +
					"\nThe number of Athletes from the 2010 data in T5-T6 was: " + numAthletes10[2] +
					"\nThe number of Athletes from the 2010 data in T7-T8 was: " + numAthletes10[3] + 
					"\n\nThe number of Athletes from the 2010 data in T1-T2 was: " + numAthletes11[0] +
					"\nThe number of Athletes from the 2010 data in T3-T4 was: " + numAthletes11[1] +
					"\nThe number of Athletes from the 2010 data in T5-T6 was: " + numAthletes11[2] +
					"\nThe number of Athletes from the 2010 data in T7-T8 was: " + numAthletes11[3] + 
					"\n\nThe number of Athletes from the 2010 data in T1-T2 was: " + numAthletes12[0] +
					"\nThe number of Athletes from the 2010 data in T3-T4 was: " + numAthletes12[1] +
					"\nThe number of Athletes from the 2010 data in T5-T6 was: " + numAthletes12[2] +
					"\nThe number of Athletes from the 2010 data in T7-T8 was: " + numAthletes12[3] + 
					"\n\nThe total number of Athletes in T1-T2 was: " + (numAthletes10[0]+numAthletes11[0]+numAthletes12[0]) +
					"\nThe total number of Athletes in T3-T4 was: " + (numAthletes10[1]+numAthletes11[1]+numAthletes12[1]) +
					"\nThe total number of Athletes in T5-T6 was: " + (numAthletes10[2]+numAthletes11[2]+numAthletes12[2]) +
					"\nThe total number of Athletes in T7-T8 was: " + (numAthletes10[3]+numAthletes11[3]+numAthletes12[3]) +
					"\n\nThe percentage of 2010 athletes that graduated was: " + percentGraduated2010*100 + 
					"\nThe percentage of 2011 athletes that graduated was: " + percentGraduated2011*100 + 
					"\nThe percentage of 2012 athletes that graduated was: " + percentGraduated2012*100 +
					"\nThe average percentage of athletes that graduated was: " + avergaePercentGrad*100);
			
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}


	//This function takes in a .arff file and runs a Naive Bayes algorithm on the instances
	//With an output class of T3-Academic Standing
	//This function also looks at a simple statistic of how many people who were 
	//Classified by whether they played a sport or not, were classified as 0 or AD in T3
	public static void evaluateSmallSportRetention(String fullFile, String trainFile, String testFile) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(trainFile));
			Instances training = new Instances(reader);
			reader.close();
			
			BufferedReader reader2 = new BufferedReader(new FileReader(testFile));
			Instances testing = new Instances(reader2);
			reader2.close();
			
			//Run Naive Bayes Algorithm
			int start = 0;
			int end = 0;
			for(int i = 0; i < training.numAttributes(); i++) {
				if(training.instance(0).attribute(i).name().equals("T1-BASEBALL")) {
					start = i; 
				} else if(training.instance(0).attribute(i).name().equals("T1-VOLLEYBALL")) {
					end = i; 
				} 
			}
			System.out.println("T1-Basesball attrib index: " + start + " T1-Volleyball attrib index: " + end);
			
			
			
			int T3AcIndex = 0;
			for(int i = 0; i < training.numAttributes(); i++) {
				if(training.instance(0).attribute(i).name().equals("T3-ACADEMIC-STAND")) {
					T3AcIndex = i;
					break;
				}
			}
			System.out.println("T3-ACADEMIC-STAND attrib index: " + T3AcIndex) ;
						
			training.setClassIndex(T3AcIndex);
			testing.setClassIndex(T3AcIndex);
			
			NaiveBayes nbClassifier = new NaiveBayes();
			nbClassifier.buildClassifier(training);
			
			int nbnumCorrect = 0; //Naive Bayes correctness count
			for(int i = 0; i < testing.numInstances(); i++) {
				Instance testInstance = new DenseInstance(testing.numAttributes());
				testInstance.setDataset(testing);
				
				for(int j = 0; j < testing.numAttributes(); j++) {
					if(j == T3AcIndex) { 
						continue;
					}
					testInstance.setValue(testing.attribute(j), testing.instance(i).value(j));
				}
				
				//Take care of the NB printout
				int nbOutC = (int)nbClassifier.classifyInstance(testInstance);
				String nbDec = testing.attribute(testing.classIndex()).value(nbOutC);
				boolean nbCorrect = nbOutC == testing.instance(i).value(T3AcIndex);
				if(nbCorrect) {
					nbnumCorrect++;
				}
			}
			int nbnumWrong = testing.numInstances()-nbnumCorrect;
			System.out.println("Naive bayes: There were: " + nbnumCorrect + " correctly classified instances and " 
					+ nbnumWrong + " incorrectly classified instances");
			
			
			/////Now we run the simple statistic/////
			BufferedReader reader3 = new BufferedReader(new FileReader(fullFile));
			Instances fullData = new Instances(reader3);
			reader3.close();
			int[] numSport = new int[11];
			int[] numSportRet = new int[11];
			int numBadStandingEnd = 0;
			
			for(int i = 0; i < fullData.numInstances(); i++) {
				boolean inSport = false;
				for(int j = start; j < end; j++) {
					if(fullData.instance(i).value(j) == 1.0) {
						inSport = true;
						numSport[j-start]++;
						if(fullData.instance(i).value(T3AcIndex) == 0.0 || fullData.instance(i).value(T3AcIndex) == 1.0
								|| Double.isNaN(fullData.instance(i).value(T3AcIndex))) {
							numBadStandingEnd++;
						} else {
							numSportRet[j-start]++;
						}
					}
				}
				if(inSport) { 
					continue;
				}
				start += 25;
				end += 25;
				for(int j = start; j < end; j++) {
					if(fullData.instance(i).value(j) == 1.0) {
						numSport[j-start]++;
						if(fullData.instance(i).value(T3AcIndex) == 0.0 || fullData.instance(i).value(T3AcIndex) == 1.0
								|| Double.isNaN(fullData.instance(i).value(T3AcIndex))) {
							numBadStandingEnd++;
						} else {
							numSportRet[j-start]++;
						}
					}
				}
				start -= 25;
				end -= 25;
			}
			System.out.println(numSport[0] + ", " + numSportRet[0] + "The Percentage of Baseball Players retained was: " + (double)numSportRet[0]/(double)numSport[0]*100 + 
					"\n" + numSport[1] + ", " + numSportRet[1] + "The Percentage of Basketball Players retained was: " + (double)numSportRet[1]/(double)numSport[1]*100 +
					"\n" + numSport[2] + ", " + numSportRet[2] + "The Percentage of Crosscountry Players retained was: " + (double)numSportRet[2]/(double)numSport[2]*100 +
					"\n" + numSport[3] + ", " + numSportRet[3] + "The Percentage of Footbal Players retained was: " + (double)numSportRet[3]/(double)numSport[3]*100 +
					"\n" + numSport[4] + ", " + numSportRet[4] + "The Percentage of Golf Players retained was: " + (double)numSportRet[4]/(double)numSport[4]*100 +
					"\n" + numSport[5] + ", " + numSportRet[5] + "The Percentage of IceHockey Players retained was: " + (double)numSportRet[5]/(double)numSport[5]*100 +
					"\n" + numSport[6] + ", " + numSportRet[6] + "The Percentage of Soccer Players retained was: " + (double)numSportRet[6]/(double)numSport[6]*100 +
					"\n" + numSport[7] + ", " + numSportRet[7] + "The Percentage of Softball Players retained was: " + (double)numSportRet[7]/(double)numSport[7]*100 +
					"\n" + numSport[8] + ", " + numSportRet[8] + "The Percentage of Tennis Players retained was: " + (double)numSportRet[8]/(double)numSport[8]*100 +
					"\n" + numSport[9] + ", " + numSportRet[9] + "The Percentage of TrackIndoor Players retained was: " + (double)numSportRet[9]/(double)numSport[9]*100 +
					"\n" + numSport[10] + ", " + numSportRet[10] + "The Percentage of TrackOutdoor Players retained was: " + (double)numSportRet[10]/(double)numSport[10]*100 + "\n\n");
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}



	//This function takes in a .arff file and runs a Naive Bayes algorithm on the instances
	//With an output class of ethnicity
	//This function also looks at a simple statistic of how many people who were 
	//Classified by ethnicity, were classified as 0 or AD in T3
	public static void evaluateEthnicRetention(String fullFile, String trainFile, String testFile) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(trainFile));
			Instances training = new Instances(reader);
			reader.close();
			
			BufferedReader reader2 = new BufferedReader(new FileReader(testFile));
			Instances testing = new Instances(reader2);
			reader2.close();
			
			//Run Naive Bayes Algorithm
			int ethnicIndex = 0;
			for(int i = 0; i < training.numAttributes(); i++) {
				if(training.instance(0).attribute(i).name().equals("RACE-DESC")) {
					ethnicIndex = i;
					break;
				}
			}
			System.out.println("Ethnicity attrib index: " + ethnicIndex) ;
			
			int T3AcIndex = 0;
			for(int i = 0; i < training.numAttributes(); i++) {
				if(training.instance(0).attribute(i).name().equals("T3-ACADEMIC-STAND")) {
					T3AcIndex = i;
					break;
				}
			}
			System.out.println("T3-ACADEMIC-STAND attrib index: " + T3AcIndex) ;
						
			training.setClassIndex(T3AcIndex);
			testing.setClassIndex(T3AcIndex);
			
			NaiveBayes nbClassifier = new NaiveBayes();
			nbClassifier.buildClassifier(training);
			
			int nbnumCorrect = 0; //Naive Bayes correctness count
			for(int i = 0; i < testing.numInstances(); i++) {
				Instance testInstance = new DenseInstance(testing.numAttributes());
				testInstance.setDataset(testing);
				
				for(int j = 0; j < testing.numAttributes(); j++) {
					if(j == T3AcIndex) { 
						continue;
					}
					testInstance.setValue(testing.attribute(j), testing.instance(i).value(j));
				}
				
				//Take care of the NB printout
				int nbOutC = (int)nbClassifier.classifyInstance(testInstance);
				String nbDec = testing.attribute(testing.classIndex()).value(nbOutC);
				boolean nbCorrect = nbOutC == testing.instance(i).value(T3AcIndex);
				if(nbCorrect) {
					nbnumCorrect++;
				}
			}
			int nbnumWrong = testing.numInstances()-nbnumCorrect;
			System.out.println("Naive bayes: There were: " + nbnumCorrect + " correctly classified instances and " 
					+ nbnumWrong + " incorrectly classified instances");
			
			
			/////Now we run the simple statistic/////
			BufferedReader reader3 = new BufferedReader(new FileReader(fullFile));
			Instances fullData = new Instances(reader3);
			reader3.close();
			int numAmInAlNat = 0;
			int numAsian = 0;
			int numBlack = 0;
			int numHisp = 0;
			int numHawai = 0;
			int numNonResAl = 0;
			int numTwoOrMore = 0;
			int numWhite = 0;
			
			int numAmInAlNatRet = 0;
			int numAsianRet = 0;
			int numBlackRet = 0;
			int numHispRet = 0;
			int numHawaiRet = 0;
			int numNonResAlRet = 0;
			int numTwoOrMoreRet = 0;
			int numWhiteRet = 0;
			int numBadStandingEnd = 0;
			
			for(int i = 0; i < fullData.numInstances(); i++) {
				if(fullData.instance(i).value(ethnicIndex) == 1.0) {
					numAmInAlNat++;
					if(fullData.instance(i).value(T3AcIndex) == 0.0 || fullData.instance(i).value(T3AcIndex) == 1.0
							|| Double.isNaN(fullData.instance(i).value(T3AcIndex))) {
						numBadStandingEnd++;
					} else {
						numAmInAlNatRet++;
					}
				} else if(fullData.instance(i).value(ethnicIndex) == 2.0) {
					numAsian++;
					if(fullData.instance(i).value(T3AcIndex) == 0.0 || fullData.instance(i).value(T3AcIndex) == 1.0
							|| Double.isNaN(fullData.instance(i).value(T3AcIndex))) {
						numBadStandingEnd++;
					} else {
						numAsianRet++;
					}
				} else if(fullData.instance(i).value(ethnicIndex) == 3.0) {
					numBlack++;
					if(fullData.instance(i).value(T3AcIndex) == 0.0 || fullData.instance(i).value(T3AcIndex) == 1.0
							|| Double.isNaN(fullData.instance(i).value(T3AcIndex))) {
						numBadStandingEnd++;
					} else {
						numBlackRet++;
					}
				} else if(fullData.instance(i).value(ethnicIndex) == 4.0) {
					numHisp++;
					if(fullData.instance(i).value(T3AcIndex) == 0.0 || fullData.instance(i).value(T3AcIndex) == 1.0
							|| Double.isNaN(fullData.instance(i).value(T3AcIndex))) {
						numBadStandingEnd++;
					} else {
						numHispRet++;
					}
				} else if(fullData.instance(i).value(ethnicIndex) == 5.0) {
					numHawai++;
					if(fullData.instance(i).value(T3AcIndex) == 0.0 || fullData.instance(i).value(T3AcIndex) == 1.0
							|| Double.isNaN(fullData.instance(i).value(T3AcIndex))) {
						numBadStandingEnd++;
					} else {
						numHawaiRet++;
					}
				} else if(fullData.instance(i).value(ethnicIndex) == 6.0) {
					numNonResAl++;
					if(fullData.instance(i).value(T3AcIndex) == 0.0 || fullData.instance(i).value(T3AcIndex) == 1.0
							|| Double.isNaN(fullData.instance(i).value(T3AcIndex))) {
						numBadStandingEnd++;
					} else {
						numNonResAlRet++;
					}
				} else if(fullData.instance(i).value(ethnicIndex) == 7.0) {
					numTwoOrMore++;
					if(fullData.instance(i).value(T3AcIndex) == 0.0 || fullData.instance(i).value(T3AcIndex) == 1.0
							|| Double.isNaN(fullData.instance(i).value(T3AcIndex))) {
						numBadStandingEnd++;
					} else {
						numTwoOrMoreRet++;
					}
				} else if(fullData.instance(i).value(ethnicIndex) == 9.0) {
					numWhite++;
					if(fullData.instance(i).value(T3AcIndex) == 0.0 || fullData.instance(i).value(T3AcIndex) == 1.0
							|| Double.isNaN(fullData.instance(i).value(T3AcIndex))) {
						numBadStandingEnd++;
					} else {
						numWhiteRet++;
					}
				}
			}
			double AmInNatRetPerc = (double)numAmInAlNatRet/(double)numAmInAlNat;
			double AsianRetPerc = (double)numAsianRet/(double)numAsian;
			double BlackRetPerc = (double)numBlackRet/(double)numBlack;
			double HispRetPerc = (double)numHispRet/(double)numHisp;
			double HawaiRetPerc = (double)numHawaiRet/(double)numHawai;
			double NonResAlRetPerc = (double)numNonResAlRet/(double)numNonResAl;
			double TwoOrMoreRetPerc = (double)numTwoOrMoreRet/(double)numTwoOrMore;
			double WhiteRetPerc = (double)numWhiteRet/(double)numWhite;
			System.out.println(numAmInAlNat + ", " + numAmInAlNatRet + " The percentage of American Indian students retained was: " + AmInNatRetPerc*100 + 
					"\n" + numAsian + ", " + numAsianRet + " The Percentage of Asian Students students retained was: " + AsianRetPerc*100 + 
					"\n" + numBlack + ", " + numBlackRet + " The Percentage of Black Students students retained was: " + BlackRetPerc*100 + 
					"\n" + numHisp + ", " + numHispRet + " The Percentage of Hispanic Students students retained was: " + HispRetPerc*100 + 
					"\n" + numHawai + ", " + numHawaiRet + " The Percentage of Hawaiian Students students retained was: " + HawaiRetPerc*100 + 
					"\n" + numNonResAl + ", " + numNonResAlRet + " The Percentage of Alien-Resident Students students retained was: " + NonResAlRetPerc*100 + 
					"\n" + numTwoOrMore + ", " + numTwoOrMoreRet + " The Percentage of Two-or-More Students students retained was: " + TwoOrMoreRetPerc*100 + 
					"\n" + numWhite + ", " + numWhiteRet + " The Percentage of White Students students retained was: " + WhiteRetPerc*100 + "\n\n");
			
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
	}

	//This function takes in a .arff file and runs a Naive Bayes algorithm on the instances
	//With an output class of Home state
	//This function also looks at a simple statistic of how many people of each ethnicity
	//were classified as 0 or AD in T3
	public static void evaluateStateRetention(String fullFile, String trainFile, String testFile) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(trainFile));
			Instances training = new Instances(reader);
			reader.close();
			
			BufferedReader reader2 = new BufferedReader(new FileReader(testFile));
			Instances testing = new Instances(reader2);
			reader2.close();
			
			//Run Naive Bayes Algorithm
			int stateIndex = 0;
			for(int i = 0; i < training.numAttributes(); i++) {
				if(training.instance(0).attribute(i).name().equals("MAL-STATE")) {
					stateIndex = i;
					break;
				}
			}
			System.out.println("Home-state attrib index: " + stateIndex) ;
			
			int T3AcIndex = 0;
			for(int i = 0; i < training.numAttributes(); i++) {
				if(training.instance(0).attribute(i).name().equals("T3-ACADEMIC-STAND")) {
					T3AcIndex = i;
					break;
				}
			}
			System.out.println("T3-ACADEMIC-STAND attrib index: " + T3AcIndex) ;
						
			training.setClassIndex(T3AcIndex);
			testing.setClassIndex(T3AcIndex);
			
			NaiveBayes nbClassifier = new NaiveBayes();
			nbClassifier.buildClassifier(training);
			
			int nbnumCorrect = 0; //Naive Bayes correctness count
			for(int i = 0; i < testing.numInstances(); i++) {
				Instance testInstance = new DenseInstance(testing.numAttributes());
				testInstance.setDataset(testing);
				
				for(int j = 0; j < testing.numAttributes(); j++) {
					if(j == T3AcIndex) { 
						continue;
					}
					testInstance.setValue(testing.attribute(j), testing.instance(i).value(j));
				}
				
				//Take care of the NB printout
				int nbOutC = (int)nbClassifier.classifyInstance(testInstance);
				String nbDec = testing.attribute(testing.classIndex()).value(nbOutC);
				boolean nbCorrect = nbOutC == testing.instance(i).value(T3AcIndex);
				if(nbCorrect) {
					nbnumCorrect++;
				}
			}
			int nbnumWrong = testing.numInstances()-nbnumCorrect;
			System.out.println("Naive bayes: There were: " + nbnumCorrect + " correctly classified instances and " 
					+ nbnumWrong + " incorrectly classified instances");
			
			
			/////Now we run the simple statistic/////
			BufferedReader reader3 = new BufferedReader(new FileReader(fullFile));
			Instances fullData = new Instances(reader3);
			reader3.close();
			int numMN = 0;
			int numNonMN = 0;
			int numMNRetained = 0;
			int numBadStandingEnd = 0;
			int numNONMNRetained = 0;
			for(int i = 0; i < fullData.numInstances(); i++) {
				if(fullData.instance(i).value(stateIndex) == 21.0) {
					numMN++;
					if(fullData.instance(i).value(T3AcIndex) == 0.0 || fullData.instance(i).value(T3AcIndex) == 1.0
							|| Double.isNaN(fullData.instance(i).value(T3AcIndex))) {
						numBadStandingEnd++;
					} else {
						numMNRetained++;
					}
				} else {
					numNonMN++;
					if(fullData.instance(i).value(T3AcIndex) == 0.0 || fullData.instance(i).value(T3AcIndex) == 1.0
							|| Double.isNaN(fullData.instance(i).value(T3AcIndex))) {
						numBadStandingEnd++;
					} else {
						numNONMNRetained++;
					};
				}
			}
			double mnRetPerc = (double)numMNRetained/(double)numMN;
			double nonMNRetPerc = (double)numNONMNRetained/(double)numNonMN;
			System.out.println("Out of the " + numMN + " instances, " + numMNRetained + " were retained. \nThe percentage of MN students retained was: " + mnRetPerc*100 + 
					"\nOut of the " + numNonMN + " instances, " + numNONMNRetained + " were retained. \nThe Percentage of Non-MN students retained was: " + nonMNRetPerc*100 + "\n\n");
			
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
		
	}

	//This function takes in a .arff file and runs a Naive Bayes algorithm on the instances
	//With an output class of T3-Academic Standing
	//This function also looks at a simple statistic of how many people who were not
	//Classified as 0 or AD in T2, were classified as 0 or AD in T3
	public static void evaluateRetention(String fullFile, String trainFile, String testFile) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(trainFile));
			Instances training = new Instances(reader);
			reader.close();
			
			BufferedReader reader2 = new BufferedReader(new FileReader(testFile));
			Instances testing = new Instances(reader2);
			reader2.close();
			
			//Run Naive Bayes Algorithm
			int T2AcIndex = 0;
			for(int i = 0; i < training.numAttributes(); i++) {
				if(training.instance(0).attribute(i).name().equals("T2-ACADEMIC-STAND")) {
					T2AcIndex = i;
					break;
				}
			}
			System.out.println("T2-ACADEMIC-STAND attrib index: " + T2AcIndex) ;
			int T3AcIndex = 0;
			for(int i = 0; i < training.numAttributes(); i++) {
				if(training.instance(0).attribute(i).name().equals("T3-ACADEMIC-STAND")) {
					T3AcIndex = i;
					break;
				}
			}
			System.out.println("T3-ACADEMIC-STAND attrib index: " + T3AcIndex) ;
						
			training.setClassIndex(T3AcIndex);
			testing.setClassIndex(T3AcIndex);
			
			NaiveBayes nbClassifier = new NaiveBayes();
			nbClassifier.buildClassifier(training);
			
			int nbnumCorrect = 0; //Naive Bayes correctness count
			for(int i = 0; i < testing.numInstances(); i++) {
				Instance testInstance = new DenseInstance(testing.numAttributes());
				testInstance.setDataset(testing);
				
				for(int j = 0; j < testing.numAttributes(); j++) {
					if(j == T3AcIndex) { 
						continue;
					}
					testInstance.setValue(testing.attribute(j), testing.instance(i).value(j));
				}
				
				//Take care of the NB printout
				int nbOutC = (int)nbClassifier.classifyInstance(testInstance);
				String nbDec = testing.attribute(testing.classIndex()).value(nbOutC);
				boolean nbCorrect = nbOutC == testing.instance(i).value(T3AcIndex);
				if(nbCorrect) {
					nbnumCorrect++;
				}
			}
			int nbnumWrong = testing.numInstances()-nbnumCorrect;
			System.out.println("Naive bayes: There were: " + nbnumCorrect + " correctly classified instances and " 
					+ nbnumWrong + " incorrectly classified instances");
			
			
			/////Now we run the simple statistic/////
			BufferedReader reader3 = new BufferedReader(new FileReader(fullFile));
			Instances fullData = new Instances(reader3);
			reader3.close();
			int numBadStandingStart = 0;
			int numGoodStandingStart = 0;
			int numBadStandingEnd = 0;
			int numRetained = 0;
			for(int i = 0; i < fullData.numInstances(); i++) {
				if(fullData.instance(i).value(T2AcIndex) == 0.0 || fullData.instance(i).value(T2AcIndex) == 1.0
						|| Double.isNaN(fullData.instance(i).value(T2AcIndex))) {
					numBadStandingStart++;
				} else {
					numGoodStandingStart++;
				}
				if(fullData.instance(i).value(T3AcIndex) == 0.0 || fullData.instance(i).value(T3AcIndex) == 1.0
						|| Double.isNaN(fullData.instance(i).value(T3AcIndex))) {
					numBadStandingEnd++;
				} else {
					numRetained++;
				}
			}
			double percentRetained = (double)numRetained/(double)numGoodStandingStart;
			System.out.println("Out of the " + numGoodStandingStart + " students who finished with a non AD or 0 standing"
					+ " at the end of T2, " + numRetained + " were retained for T3. \nThis is a " + percentRetained*100
					+ " percent retention rate.\n\n");
			
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e);
		}
				
	}

	//This method splits the Data into both testing and training data off of the year
	public static void createTestAndTrainData(String inputFile, String training, String testing) {
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

}
