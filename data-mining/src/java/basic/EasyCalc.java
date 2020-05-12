package java.basic;

import java.util.Scanner;

public class EasyCalc {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Calculate Entropy?:");
		boolean entropy = in.nextBoolean();
		if(entropy) {
			System.out.println("How many attributes?:");
			calculateEntropy();
		}
		
		System.out.println("Calculate Confusion Matrix?:");
		boolean confMatrix = in.nextBoolean();
		if(confMatrix) {
			System.out.println("a:");
			double a = in.nextDouble();
			System.out.println("b:");
			double b = in.nextDouble();
			System.out.println("c:");
			double c = in.nextDouble();
			System.out.println("d:");
			double d = in.nextDouble();
			processConfusionMatrix(a, b, c, d);
		}
		
		in.close();

	}
	
	//Confusion Matrix 
	public static void processConfusionMatrix(double a, double b, double c, double d) {
		//TODO Process Confusion Matrix
		
	}
	
	public static double calculateEntropy() {
		//TODO create this
		
		return 0.0;
	}

}
