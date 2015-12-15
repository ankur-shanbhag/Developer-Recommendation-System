package edu.neu.datamining.project.algorithms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.neu.datamining.project.conf.DataConfiguration;
import edu.neu.datamining.project.data.BugInfo;
import edu.neu.datamining.project.data.DeveloperInfo;
import edu.neu.datamining.project.utils.DataNormalization;
import edu.neu.datamining.project.utils.FileUtils;

public class KernelKMeans implements DataClusterer {

	private static final double SIGMA = 0.00001;

	private final List<BugInfo> dataPoints;
	private final double[][] kernelMatrix;
	private double[][] weightMatrix;
	private BugInfo[] clusterCenter;

	public KernelKMeans(List<BugInfo> dataPoints, int k) {
		this.dataPoints = dataPoints;
		// creation of kernel matrix from above dataPoints
		this.kernelMatrix = createKernelMatrix();

		clusterCenter = new BugInfo[k];
		Random rand = new Random();
		// Random selection of cluster centers from the given dataPoints
		for (int i = 0; i < clusterCenter.length; i++) {
			int randomNum = rand.nextInt(dataPoints.size());
			BugInfo temp = dataPoints.get(randomNum);
			clusterCenter[i] = new BugInfo(temp.getBugID(),
					Arrays.copyOf(temp.getFeatures(), temp.getFeatures().length), temp.getDevelopers());
		}

		// Random assignment of dataPoints to cluster
		weightMatrix = new double[k][dataPoints.size()];
		for (int i = 0; i < weightMatrix[0].length; i++) {
			int randomNum = rand.nextInt(k);
			weightMatrix[randomNum][i] = 1;
		}
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {

		List<BugInfo> dataPoints = FileUtils.readBugsInfo(DataConfiguration.BUGS_INFO_FILE_PATH_FIXED);

		System.out.println("Kernel K-Means Started..");
		long start = System.currentTimeMillis();

		DataNormalization.normalizeData(dataPoints);
		KernelKMeans kkmeans = new KernelKMeans(dataPoints, 10);

		List<Set<DeveloperInfo>> developers = kkmeans.createClusters();
		List<List<BugInfo>> clusters = kkmeans.getClusters();

		for (List<BugInfo> cluster : clusters) {
			System.out.println(cluster.size());
		}

		for (Set<DeveloperInfo> dev : developers) {
			System.out.println("Developers : " + dev.size());
		}
	}

	/**
	 * Gaussian kernel function
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private static double kernelFunction(BugInfo x, BugInfo y) {
		double dist = x.distance(y);
		dist *= dist;
		return (Math.exp(-dist) / (2 * SIGMA * SIGMA));
	}

	private double[][] createKernelMatrix() {
		double[][] kernelMatrix = new double[dataPoints.size()][dataPoints.size()];
		for (int i = 0; i < dataPoints.size(); i++) {
			for (int j = 0; j < dataPoints.size(); j++) {
				kernelMatrix[i][j] = kernelFunction(dataPoints.get(i), dataPoints.get(j));
			}
		}
		return kernelMatrix;
	}

	private static boolean centersEqual(BugInfo[] newCenter, BugInfo[] oldCenter) {
		int i = 0, j = 0;
		for (i = 0; i < oldCenter.length; i++) {
			for (j = 0; j < oldCenter[i].getDimension(); j++) {
				if (Math.abs(oldCenter[i].getFeatures()[j] - newCenter[i].getFeatures()[j]) > 0) {
					return false;
				}
			}
		}
		return true;
	}

	private void populateWeightMatrix() {
		double[][] weightData = new double[weightMatrix.length][weightMatrix[0].length];
		double[] secondOrderArray = getSecondOrder();
		for (int dataIndex = 0; dataIndex < dataPoints.size(); dataIndex++) {
			double min = Double.MAX_VALUE;
			double distance = min;
			int center = 0;
			for (int clusterIndex = 0; clusterIndex < clusterCenter.length; clusterIndex++) {
				distance = kernelMatrix[dataIndex][dataIndex] - getFirstOrder(dataIndex, clusterIndex)
						+ secondOrderArray[clusterIndex];
				if (distance < min) {
					center = clusterIndex;
					min = distance;
				}
			}
			weightData[center][dataIndex] = 1;
		}
		weightMatrix = weightData;
	}

	private double getFirstOrder(int dataIndex, int clusterIndex) {
		double firstOrder = 0.0d;
		double sum = 0.0d;
		for (int i = 0; i < weightMatrix[clusterIndex].length; i++) {
			firstOrder += (weightMatrix[clusterIndex][i] * kernelMatrix[dataIndex][i]);
			sum += weightMatrix[clusterIndex][i];
		}
		return ((2 * firstOrder) / sum);
	}

	private double[] getSecondOrder() {
		double[] secondOrderArray = new double[weightMatrix.length];
		for (int k = 0; k < secondOrderArray.length; k++) {
			double secondOrder = 0.0d;
			double sum = 0.0d;
			for (int i = 0; i < weightMatrix[k].length; i++) {
				if (weightMatrix[k][i] > 0) {
					for (int j = i; j < weightMatrix[k].length; j++) {
						if (weightMatrix[k][j] > 0) {
							secondOrder += (weightMatrix[k][i] * weightMatrix[k][j] * kernelMatrix[i][j]);
							sum += weightMatrix[k][i];
						}
					}
				}
			}
			secondOrderArray[k] = secondOrder / (sum * sum);
		}
		return secondOrderArray;
	}

	private void updateClusterCenters() {
		for (int clusterIndex = 0; clusterIndex < weightMatrix.length; clusterIndex++) {
			List<BugInfo> bugList = new ArrayList<BugInfo>();
			for (int dataIndex = 0; dataIndex < weightMatrix[clusterIndex].length; dataIndex++) {
				if (weightMatrix[clusterIndex][dataIndex] > 0) {
					BugInfo temp = dataPoints.get(dataIndex);
					bugList.add(new BugInfo(temp.getBugID(),
							Arrays.copyOf(temp.getFeatures(), temp.getFeatures().length), temp.getDevelopers()));
				}
			}
			updateCenterOf(bugList, clusterIndex);
		}
	}

	private void updateCenterOf(List<BugInfo> bugList, int clusterIndex) {
		BugInfo sum = clusterCenter[clusterIndex];
		double[] featureSum = sum.getFeatures();
		double weightSum = 0.0d;
		for (int i = 0; i < weightMatrix[clusterIndex].length; i++) {
			weightSum += weightMatrix[clusterIndex][i];
		}
		for (int i = 0; i < bugList.size(); i++) {
			double[] currentBugFeature = bugList.get(i).getFeatures();
			addFeatures(featureSum, currentBugFeature);
		}
		for (int j = 0; j < featureSum.length; j++) {
			featureSum[j] = featureSum[j] / weightSum;
		}
	}

	private static void addFeatures(double[] feature1, double[] feature2) {
		for (int i = 0; i < feature1.length; i++) {
			feature1[i] += feature2[i];
		}
	}

	private static BugInfo[] moveOldCenters(BugInfo[] oldArray) {
		BugInfo[] newArray = new BugInfo[oldArray.length];
		for (int i = 0; i < oldArray.length; i++) {
			newArray[i] = new BugInfo(oldArray[i].getBugID(),
					Arrays.copyOf(oldArray[i].getFeatures(), oldArray[i].getFeatures().length),
					oldArray[i].getDevelopers());
		}
		return newArray;
	}

	@Override
	public List<Set<DeveloperInfo>> createClusters() {
		BugInfo[] oldCenters = null;
		do {
			populateWeightMatrix();
			oldCenters = moveOldCenters(clusterCenter);
			updateClusterCenters();
		} while (!centersEqual(clusterCenter, oldCenters));

		List<Set<DeveloperInfo>> listOfClusters = getDevelopers();

		return listOfClusters;
	}

	@Override
	public List<Set<DeveloperInfo>> getDevelopers() {
		List<Set<DeveloperInfo>> listOfClusters = new ArrayList<Set<DeveloperInfo>>();
		for (int clusterIndex = 0; clusterIndex < weightMatrix.length; clusterIndex++) {
			Set<DeveloperInfo> devInfo = new HashSet<>();
			for (int dataIndex = 0; dataIndex < weightMatrix[clusterIndex].length; dataIndex++) {
				if (weightMatrix[clusterIndex][dataIndex] > 0) {
					devInfo.addAll(dataPoints.get(dataIndex).getDevelopers());
				}
			}
			listOfClusters.add(devInfo);
		}
		return listOfClusters;
	}

	@Override
	public List<List<BugInfo>> getClusters() {
		List<List<BugInfo>> listOfClusters = new ArrayList<List<BugInfo>>();

		for (int clusterIndex = 0; clusterIndex < weightMatrix.length; clusterIndex++) {
			List<BugInfo> bugInfo = new ArrayList<>();

			for (int dataIndex = 0; dataIndex < weightMatrix[clusterIndex].length; dataIndex++) {
				if (weightMatrix[clusterIndex][dataIndex] > 0) {
					bugInfo.add(dataPoints.get(dataIndex));
				}
			}

			listOfClusters.add(bugInfo);
		}

		return listOfClusters;
	}
}
