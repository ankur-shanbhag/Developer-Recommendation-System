package edu.neu.datamining.project.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

import edu.neu.datamining.project.data.BugInfo;
import edu.neu.datamining.project.data.DeveloperInfo;

/**
 * Implementation for DBSCAN algorithm for clustering the data points with
 * n-dimensional features using Euclidean distance method.
 * 
 * This class internally uses
 * <tt>org.apache.commons.math3.ml.clustering.DBSCANClusterer</tt> to form
 * density based clusters<br>
 * 
 * @see <a
 *      href="https://commons.apache.org/proper/commons-math/apidocs/org/apache/commons/math3/ml/clustering/package-summary.html">DBSCANClusterer</a>
 *
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
			throw new RuntimeException(
					"MinPoints cannot have a value greater than total number of data points");

		// this.eps = calculateEps();
		this.eps = eps;
	}

	/**
	 * Creates density based clusters for all the data points using DBSCAN
	 * algorithm.<br>
	 * Note: Some of the data points which do not form part of any cluster will
	 * be considered as noise and hence ignored
	 */
	@Override
	public List<Set<DeveloperInfo>> createClusters() {

		DBSCANClusterer<BugInfo> clustering = new DBSCANClusterer<>(this.eps,
				this.minPoints);

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
}
