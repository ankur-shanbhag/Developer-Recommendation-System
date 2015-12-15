package edu.neu.datamining.project.algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

import edu.neu.datamining.project.conf.DataConfiguration;
import edu.neu.datamining.project.data.BugInfo;
import edu.neu.datamining.project.data.DeveloperInfo;
import edu.neu.datamining.project.utils.DataNormalization;
import edu.neu.datamining.project.utils.FileUtils;

/**
 * Implementation for DBSCAN algorithm for clustering the data points with
 * n-dimensional features using Euclidean distance method
 * 
 * @author Ankur Shanbhag
 * 
 */
public class DBSCANCluster implements DataClusterer {

	private final List<BugInfo> dataPoints;

	/**
	 * maximum radius of the neighborhood
	 */
	private final double eps;

	/**
	 * Minimum number of points in Epsilon neighborhood to consider any point
	 * for clustering
	 */
	private final int minPoints;

	/**
	 * clusters formed by DBSCAN algorithm
	 */
	private List<List<BugInfo>> clusters = new ArrayList<>();

	public DBSCANCluster(List<BugInfo> dataPoints, double eps, int minPoints) {
		this.dataPoints = dataPoints;
		this.minPoints = minPoints;

		if (this.minPoints > dataPoints.size())
			throw new RuntimeException("MinPoints cannot have a value greater than total number of data points");

		// this.eps = calculateEps();
		this.eps = eps;
	}

	/**
	 * Calculates eps based on the mean of MinPoint neighbor values
	 * 
	 * @return calculated eps value
	 */
	private static double calculateEps(List<BugInfo> dataPoints, int minPoints) {

		double sum = 0;

		Map<String, Double> map = new HashMap<>();

		for (int i = 0; i < dataPoints.size(); i++) {

			List<Double> distances = new ArrayList<>();
			for (int j = 0; j < dataPoints.size(); j++) {

				if (i == j)
					continue;

				String key = null;
				if (i < j) {
					key = i + "," + j;
				} else {
					key = j + "," + i;
				}

				Double val = map.get(key);

				if (null == val) {
					val = dataPoints.get(i).distance(dataPoints.get(j));
					map.put(key, val);
				}

				distances.add(val);
			}

			// Collections.sort(distances);

			// consider only minpoint neighbor for eps calculation
			// sum += distances.get(minPoints - 1);
		}

		return sum / dataPoints.size();
	}

	private static void minMaxNormalization(List<BugInfo> dataPoints, int pos) {

		double max = dataPoints.get(0).getFeatures()[pos];
		double min = dataPoints.get(0).getFeatures()[pos];

		for (BugInfo instance : dataPoints) {
			double time = instance.getFeatures()[pos];
			if (max < time) {
				max = time;
			} else if (min > time) {
				min = time;
			}
		}

		System.out.println(max);
		System.out.println(min);

		double minMaxDiff = max - min;

		for (BugInfo instance : dataPoints) {
			double time = instance.getFeatures()[pos];
			instance.getFeatures()[pos] = (time - min) / minMaxDiff;
		}

	}

	@Override
	public List<Set<DeveloperInfo>> createClusters() {

		// DataNormalization.normalizeData(dataPoints);

		DBSCANClusterer<BugInfo> clustering = new DBSCANClusterer<>(this.eps, this.minPoints);

		List<Cluster<BugInfo>> dbscanClusters = clustering.cluster(dataPoints);

		for (Cluster<BugInfo> cluster : dbscanClusters) {
			this.clusters.add(cluster.getPoints());
		}

		List<Set<DeveloperInfo>> developers = getDevelopers();
		return developers;
	}

	@Override
	public List<Set<DeveloperInfo>> getDevelopers() {

		if (null == clusters) {
			return null;
		}

		List<Set<DeveloperInfo>> devClusters = new ArrayList<>();

		for (List<BugInfo> cluster : clusters) {
			Set<DeveloperInfo> developers = new HashSet<>();
			devClusters.add(developers);

			for (BugInfo bug : cluster) {
				// add developers for all the bugs in a cluster
				developers.addAll(bug.getDevelopers());
			}
		}

		return devClusters;
	}

	@Override
	public List<List<BugInfo>> getClusters() {
		return clusters;
	}

	public static void main(String[] args) throws IOException {

		List<BugInfo> dataPoints = FileUtils.readBugsInfo(DataConfiguration.BUGS_INFO_FILE_PATH_FIXED);

		System.out.println("Started..");
		long start = System.currentTimeMillis();

		DataNormalization.normalizeData(dataPoints);
		DBSCANCluster dbscan = new DBSCANCluster(dataPoints, 2, 30);

		List<Set<DeveloperInfo>> developers = dbscan.createClusters();
		List<List<BugInfo>> clusters = dbscan.getClusters();

		for (List<BugInfo> cluster : clusters) {
			System.out.println("Cluster : " + cluster.size());
		}

		// System.out.println(" ------------------------------------- ");
		//
		// for (Set<DeveloperInfo> dev : developers) {
		// System.out.println("Developers : " + dev.size());
		// }
		//
		// System.out.println("Time taken : "
		// + (System.currentTimeMillis() - start));
	}
}
