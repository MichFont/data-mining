package java.bayes;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class NaiveBayesExample {
	public static void main(String[] args) {
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("Weather.arff"));
			Instances data = new Instances(reader);
			reader.close();
			
			
			//Set the index of the output class
			data.setClassIndex(data.numAttributes()-1);
			
			//Create NaiveBayes object
			NaiveBayes nbClassifier = new NaiveBayes();
			nbClassifier.buildClassifier(data);
			Instance testInstance = new DenseInstance(4);
			testInstance.setDataset(data);
			testInstance.setValue(data.attribute(0), "rainy");
			testInstance.setValue(data.attribute(1), 60);
			testInstance.setValue(data.attribute(2), 65);
			testInstance.setValue(data.attribute(3), "TRUE");
			int outputClass = (int)nbClassifier.classifyInstance(testInstance);
			String className = data.attribute(data.classIndex()).value(outputClass);
			System.out.println(outputClass + ": " + className);
			
						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
