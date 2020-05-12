package java.clusters;

import java.text.DecimalFormat;

public class KMeansClustering {

	public static void main(String[] args) {
		double[][] points = new double[][] {
			{5.1, 3.5, 1.4, 0.2}, 
			{4.9, 3.0, 1.4, 0.2},
			{4.7, 3.2, 1.3, 0.2},
			{4.6, 3.1, 1.5, 0.2},
			{5.0, 3.6, 1.4, 0.2},
			{5.4, 3.9, 1.7, 0.4}, 
			{4.6, 3.4, 1.4, 0.3}, 
			{5.0, 3.4, 1.5, 0.2},
			{4.4, 2.9, 1.4, 0.2},
			{4.9, 3.1, 1.5, 0.1}, 
			{5.4, 3.7, 1.5, 0.2},
			{4.8, 3.4, 1.6, 0.2},
			{4.8, 3.0, 1.4, 0.1},
			{4.3, 3.0, 1.1, 0.1},
			{5.8, 4.0, 1.2, 0.2},
			{5.7, 4.4, 1.5, 0.4},
			{5.4, 3.9, 1.3, 0.4},
			{5.1, 3.5, 1.4, 0.3},
			{5.7, 3.8, 1.7, 0.3},
			{5.1, 3.8, 1.5, 0.3}
		};
		double[][] distances = calculateEuclidianDistance(points[0], points[1], points[2], points);
		printDistances(distances);
		double[][] newCenters = calculateCentroids(distances, points);
		calculateSSE(newCenters, distances, points);
		
		distances = calculateEuclidianDistance(newCenters[0], newCenters[1], newCenters[2], points);
		printDistances(distances);
		newCenters = calculateCentroids(distances, points);
		calculateSSE(newCenters, distances, points);
	}

	private static void calculateSSE(double[][] centroids, double[][] dist, double[][] points) {
		double SSE = 0.0;
		for(int i = 0; i < dist.length; i++) {
			if(dist[i][0]<dist[i][1] && dist[i][0]<dist[i][2]) {
				for(int j = 0; j < 4; j++) {
					SSE += Math.abs(centroids[0][j] - points[i][j])*Math.abs(centroids[0][j] - points[i][j]);
				}
			} else if(dist[i][1]<dist[i][2] && dist[i][1]<dist[i][0]) {
				for(int j = 0; j < 4; j++) {
					SSE += Math.abs(centroids[1][j] - points[i][j])*Math.abs(centroids[1][j]- points[i][j]);
				}
			} else {
				for(int j = 0; j < 4; j++) {
					SSE += Math.abs(centroids[2][j] - points[i][j])*Math.abs(centroids[2][j] - points[i][j]);
				}
			}
		}
		DecimalFormat df = new DecimalFormat("#.####");
		System.out.println("SSE: " + df.format(SSE) + "\n");
	}

	private static double[][] calculateCentroids(double[][] dist, double[][] points) {
		double[][] newCenters = new double[3][4];
		int[] numInClust = new int[3];
		for(int i = 0; i < dist.length; i++) {
			if(dist[i][0]<dist[i][1] && dist[i][0]<dist[i][2]) {
				for(int j = 0; j < 4; j++) {
					newCenters[0][j] += points[i][j];
				}
				numInClust[0]++;
			} else if(dist[i][1]<dist[i][2] && dist[i][1]<dist[i][0]) {
				for(int j = 0; j < 4; j++) {
					newCenters[1][j] += points[i][j];
				}
				numInClust[1]++;
			} else {
				for(int j = 0; j < 4; j++) {
					newCenters[2][j] += points[i][j];
				}
				numInClust[2]++;
			}
		}
		
		DecimalFormat df = new DecimalFormat("#.####");
		for(int i = 0; i < newCenters.length; i++) {
			System.out.print("Centroid " +  i);
			for(int j = 0; j < newCenters[0].length; j++) {
				newCenters[i][j] = newCenters[i][j]/numInClust[i];
				System.out.print("\t" + df.format(newCenters[i][j]));
			}
			System.out.println("");
		}
		System.out.println("\n");
		return newCenters;
	}

	private static void printDistances(double[][] dist) {
		DecimalFormat df = new DecimalFormat("#.####");
		for(int i = 0; i < dist.length; i++) {
			char cluster;
			if(dist[i][0]<dist[i][1] && dist[i][0]<dist[i][2]) {
				cluster = 'a';
			} else if(dist[i][1]<dist[i][2] && dist[i][1]<dist[i][0]) {
				cluster = 'b';
			} else {
				cluster = 'c';
			}
			System.out.println(df.format(dist[i][0]) + "\t" + df.format(dist[i][1]) + "\t" + df.format(dist[i][2]) + "\t" + cluster);
		}
		System.out.println("\n\n");
		
	}

	private static double[][] calculateEuclidianDistance(double[] c1, double[] c2, double[] c3, double[][] points) {
		double[][] distances = new double[20][3];
		for(int i = 0; i < points.length; i++) {
			double dist1 = 0.0;
			double dist2 = 0.0;
			double dist3 = 0.0;
			for(int j = 0; j < 4; j++) {
				dist1 += Math.abs(c1[j] - points[i][j])*Math.abs(c1[j] - points[i][j]);
				dist2 += Math.abs(c2[j] - points[i][j])*Math.abs(c2[j] - points[i][j]);
				dist3 += Math.abs(c3[j] - points[i][j])*Math.abs(c3[j] - points[i][j]);
			}
			distances[i][0] = Math.sqrt(dist1);
			distances[i][1] = Math.sqrt(dist2);
			distances[i][2] = Math.sqrt(dist3);
		}
		
		return distances;
	}

}
