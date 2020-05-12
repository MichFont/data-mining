package java.exams;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.core.Instances;

public class ex1SoybeanProcessor {

	public static void main(String[] args) {
		
		try {
			BufferedReader inputFile = new BufferedReader(new FileReader("soybean.arff"));
			Instances data = new Instances(inputFile);
			System.out.println("Number of instances: " + data.numInstances());
			System.out.println("Number of attributes: " + data.numAttributes());
			
			//Setting the output class to be the last attribute column
			data.setClassIndex(data.numAttributes() - 1);
			System.out.println(data.classIndex());
			
			//Do the calculation once and store in memory
			double log2 = Math.log10(2);
			
			//Calculate the entropy for the original dataset
			int[] numInstancesBucket = new int[19];
			for(int i = 0; i < data.numInstances(); i++) {
				for(int j = 0; j < 19; j++) {
					if(data.instance(i).stringValue(data.classIndex()).equals(data.attribute(data.classIndex()).value(j))) {
						numInstancesBucket[j]++;
						break;
					}
				}
			}
			
			double entropy = 0.0;
			for(int i  = 0; i < 19; i++) {
				double outPercentage = (double)numInstancesBucket[i]/683.0;
				entropy += -1*(outPercentage)*(Math.log10(outPercentage)/log2);
				
			}
			System.out.println("Entropy: " + entropy + "\n");
		
			
			//Calculate the entropy and intrinsic value for the precip split
			int[] precipSplitBucket = new int[3];
			int[][]precipOutBuckets = new int[3][19];
			int precipMissingCount = 0;
			for(int i = 0; i < data.numInstances(); i++) {
				for(int j = 0; j < 3; j++) {
					if(data.instance(i).stringValue(2).equals("?")) {
						precipMissingCount++;
						break;
					}
					if(data.instance(i).stringValue(2).equals(data.attribute(2).value(j))) {
						precipSplitBucket[j]++;
						for(int k = 0; k < 19; k++) {
							if(data.instance(i).stringValue(data.classIndex()).equals(data.attribute(data.classIndex()).value(k))) {
								precipOutBuckets[j][k]++;
								break;
							}
						}
						break;
					}
				}
			}
			
			double precipIntrinsic = 0.0;
			double precipEntropy = 0.0;
			for(int i  = 0; i < 3; i++) {	
				precipIntrinsic += -1*((double)precipSplitBucket[i]/683.0)*(Math.log10((double)precipSplitBucket[i]/683.0)/log2);
				double nodeEntropy = 0.0;
				for(int j = 0; j < 19; j++) {
					if(precipOutBuckets[i][j] == 0) {
						continue;
					}
					double outPercentage = (double)precipOutBuckets[i][j]/precipSplitBucket[i];
					nodeEntropy += -1*(outPercentage)*(Math.log10(outPercentage)/log2);
				}
				System.out.println("Node " + i + " entropy: " + nodeEntropy);
				precipEntropy += ((double)precipSplitBucket[i]/683.0)*nodeEntropy;
				
			}
			System.out.println("Instances missing attribute: " + precipMissingCount);
			System.out.println("Entropy for precip: " + precipEntropy);
			System.out.println("Intrinsic Value for precip: " + precipIntrinsic + "\n");
			
			
			//Calculate the entropy and intrinsic value for the plant-growth split
			int[] plantSplitBucket = new int[2];
			int[][]plantOutBuckets = new int[2][19];
			int plantMissingCount = 0;
			for(int i = 0; i < data.numInstances(); i++) {
				for(int j = 0; j < 2; j++) {
					if(data.instance(i).stringValue(10).equals("?")) {
						plantMissingCount++;
						break;
					}
					if(data.instance(i).stringValue(10).equals(data.attribute(10).value(j))) {
						plantSplitBucket[j]++;
						for(int k = 0; k < 19; k++) {
							if(data.instance(i).stringValue(data.classIndex()).equals(data.attribute(data.classIndex()).value(k))) {
								plantOutBuckets[j][k]++;
								break;
							}
						}
						break;
					}
				}
			}
			
			double plantIntrinsic = 0.0;
			double plantEntropy = 0.0;
			for(int i  = 0; i < 2; i++) {	
				plantIntrinsic += -1*((double)plantSplitBucket[i]/683.0)*(Math.log10((double)plantSplitBucket[i]/683.0)/log2);
				
				double nodeEntropy = 0.0;
				for(int j = 0; j < 19; j++) {
					if(plantOutBuckets[i][j] == 0) {
						continue;
					}
					double outPercentage = (double)plantOutBuckets[i][j]/plantSplitBucket[i];
					nodeEntropy += -1*(outPercentage)*(Math.log10(outPercentage)/log2);
				}
				System.out.println("Node " + i + " entropy: " + nodeEntropy);
				plantEntropy += ((double)plantSplitBucket[i]/683.0)*nodeEntropy;
				
			}
			System.out.println("Instances missing attribute: " + plantMissingCount);
			System.out.println("Entropy for plant-growth: " + plantEntropy);
			System.out.println("Intrinsic Value for plant-growth: " + plantIntrinsic);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
