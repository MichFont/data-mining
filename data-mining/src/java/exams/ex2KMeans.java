package java.exams;

import java.text.DecimalFormat;

public class ex2KMeans {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		double[][] points = new double[][] { 
			{1.52121,14.03,3.76,0.58,71.79,0.11,9.65,0,0}, 
			{1.51665,13.14,3.45,1.76,72.48,0.6,8.38,0,0.17}, 
			{1.51707,13.48,3.48,1.71,72.52,0.62,7.99,0,0},
			{1.51719,14.75,0,2,73.02,0,8.53,1.59,0.08}, 
			{1.51629,12.71,3.33,1.49,73.28,0.67,8.24,0,0}, 
			{1.51994,13.27,0,1.76,73.03,0.47,11.32,0,0}, 
			{1.51811,12.96,2.96,1.43,72.92,0.6,8.79,0.14,0},
			{1.52152,13.05,3.65,0.87,72.22,0.19,9.85,0,0.17}, 
			{1.52475,11.45,0,1.88,72.19,0.81,13.24,0,0.34}, 
			{1.51841,12.93,3.74,1.11,72.28,0.64,8.96,0,0.22}, 
			{1.51754,13.39,3.66,1.19,72.79,0.57,8.27,0,0.11},
			{1.52058,12.85,1.61,2.17,72.18,0.76,9.7,0.24,0.51}, 
			{1.51569,13.24,3.49,1.47,73.25,0.38,8.03,0,0}, 
			{1.5159,12.82,3.52,1.9,72.86,0.69,7.97,0,0}, 
			{1.51683,14.56,0,1.98,73.29,0,8.52,1.57,0.07},
			{1.51687,13.23,3.54,1.48,72.84,0.56,8.1,0,0}, 
			{1.5161,13.33,3.53,1.34,72.67,0.56,8.33,0,0}, 
			{1.51674,12.87,3.56,1.64,73.14,0.65,7.99,0,0}, 
			{1.51832,13.33,3.34,1.54,72.14,0.56,8.99,0,0},
			{1.51115,17.38,0,0.34,75.41,0,6.65,0,0} 
		};
		double[][] distances = calculateEuclidianDistance(points[0], points[1], points);
		printDistances(distances);
		double[][] newCenters = calculateCentroids(distances, points);
		calculateSSE(newCenters, distances, points);
	}

	private static void calculateSSE(double[][] centroids, double[][] dist, double[][] points) {
		double SSE = 0.0;
		for (int i = 0; i < dist.length; i++) {
			if (dist[i][0] < dist[i][1]) {
				for (int j = 0; j < points[i].length; j++) {
					SSE += Math.abs(centroids[0][j] - points[i][j]) * Math.abs(centroids[0][j] - points[i][j]);
				}
			} else {
				for (int j = 0; j < points[i].length; j++) {
					SSE += Math.abs(centroids[1][j] - points[i][j]) * Math.abs(centroids[1][j] - points[i][j]);
				}
			}
		}
		DecimalFormat df = new DecimalFormat("#.####");
		System.out.println("SSE: " + df.format(SSE) + "\n");
	}

	private static double[][] calculateCentroids(double[][] dist, double[][] points) {
		double[][] newCenters = new double[2][points[0].length];
		int[] numInClust = new int[2];
		for (int i = 0; i < dist.length; i++) {
			if (dist[i][0] < dist[i][1]) {
				for (int j = 0; j < points[i].length; j++) {
					newCenters[0][j] += points[i][j];
				}
				numInClust[0]++;
			} else {
				for (int j = 0; j < points[i].length; j++) {
					newCenters[1][j] += points[i][j];
				}
				numInClust[1]++;
			}
		}

		DecimalFormat df = new DecimalFormat("#.####");
		for (int i = 0; i < newCenters.length; i++) {
			System.out.print("Centroid " + i);
			for (int j = 0; j < newCenters[0].length; j++) {
				newCenters[i][j] = newCenters[i][j] / numInClust[i];
				System.out.print("\t" + df.format(newCenters[i][j]));
			}
			System.out.println("");
		}
		System.out.println("\n");
		return newCenters;
	}

	private static void printDistances(double[][] dist) {
		DecimalFormat df = new DecimalFormat("#.####");
		for (int i = 0; i < dist.length; i++) {
			char cluster;
			if (dist[i][0] < dist[i][1]) {
				cluster = 'a';
			} else {
				cluster = 'b';
			}
			System.out.println(df.format(dist[i][0]) + "\t" + df.format(dist[i][1]) + "\t" + cluster);
		}
		System.out.println("\n\n");

	}

	private static double[][] calculateEuclidianDistance(double[] c1, double[] c2, double[][] points) {
		double[][] distances = new double[20][2];
		for (int i = 0; i < points.length; i++) {
			double dist1 = 0.0;
			double dist2 = 0.0;
			for (int j = 0; j < 4; j++) {
				dist1 += Math.abs(c1[j] - points[i][j]) * Math.abs(c1[j] - points[i][j]);
				dist2 += Math.abs(c2[j] - points[i][j]) * Math.abs(c2[j] - points[i][j]);
			}
			distances[i][0] = Math.sqrt(dist1);
			distances[i][1] = Math.sqrt(dist2);
		}

		return distances;
	}

}
