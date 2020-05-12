package java.trees;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class DecisionTrees {
	public static void main(String[] args) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("Weather.arff"));
			//BufferedReader reader = new BufferedReader(new FileReader("hypothyroid.arff"));
			Instances data = new Instances(reader);
			reader.close();
			
			
			//Set the index of the output class
			data.setClassIndex(data.numAttributes()-1);
			
			J48 tree = new J48();
			
			//Creates an unpruned tree
			/*String[] options = new String[1];
			options[0] = "-U";
			tree.setOptions(options);*/
			
			//Creates a pruned tree
			tree.buildClassifier(data);
			

			System.out.println(tree);
			
			//Instance is abstract so we can only create pointers
			Instance testInstance = new DenseInstance(4);
			testInstance.setDataset(data);
			testInstance.setValue(data.attribute(0), "rainy");
			testInstance.setValue(data.attribute(1), 60);
			testInstance.setValue(data.attribute(2), 65);
			testInstance.setValue(data.attribute(3), "FALSE");
			int outputClass = (int)tree.classifyInstance(testInstance);
			String className = data.attribute(data.classIndex()).value(outputClass);
			System.out.println(outputClass + ": " + className);
			
			int folds = 3;
			for(int i = 0; i < folds; i++) {
				//Test on 1/n folds of data, train on n-1/n folds
				Instances testFold = data.testCV(folds, i);
				Instances trainFold = data.trainCV(folds, i);
				//Set up a classifier
				Classifier cls = new J48();
				cls.buildClassifier(trainFold);
				//Create evaluation tree
				Evaluation treeEvaluation = new Evaluation(trainFold);
				treeEvaluation.evaluateModel(cls, testFold);
				double[][] conf = treeEvaluation.confusionMatrix();
				for(int j = 0; j < conf.length; j++) {
					for(int k = 0; k < conf[j].length; k++) {
						System.out.print(conf[j][k] + "\t");
					}
					System.out.println("");
				}
				//Complexity statistics: false
				System.out.println(treeEvaluation.toSummaryString("Results: ", false));
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
