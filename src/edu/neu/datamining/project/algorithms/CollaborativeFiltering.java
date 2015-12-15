package edu.neu.datamining.project.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.neu.datamining.project.conf.BugFeatures;
import edu.neu.datamining.project.data.BugInfo;
import edu.neu.datamining.project.data.DataPoint;
import edu.neu.datamining.project.data.DeveloperInfo;
import edu.neu.datamining.project.utils.DataNormalization;

public class CollaborativeFiltering implements RecommendationAlgorithm {

	private static final double LAMBDA = 0.00001;
	private static final double ALPHA = 0.00001;
	private static final double EPSILON = 0.00001;

	private final List<List<BugInfo>> clusters = new ArrayList<>();
	private final List<Set<DeveloperInfo>> developers;
	private Set<DeveloperInfo> allDevs;
	private double[][] votesMatrix;
	private final Map<String, Integer> devIndexMapping = new HashMap<>();
	private final List<BugInfo> clustersInfo = new ArrayList<>();
	private double[] meanVotes;

	/**
	 * Uses DBSCAN algorithm to create initial clusters of the before learning
	 * the cluster and developer parameters
	 * 
	 * @param bugs
	 * @param eps
	 * @param minPoints
	 */
	public CollaborativeFiltering(List<BugInfo> bugs, double eps, int minPoints) {
		Map<Long, BugInfo> bugMap = new HashMap<>();
		for (BugInfo bug : bugs) {
			bugMap.put(bug.getBugID(), bug.clone());
		}

		DataNormalization.normalizeData(bugs);
		DataClusterer dbscan = new DBSCANCluster(bugs, eps, minPoints);
		this.developers = dbscan.createClusters();
		List<List<BugInfo>> dbscanClusters = dbscan.getClusters();

		init(bugMap, dbscanClusters);
	}

	/**
	 * Uses Kernel KMeans algorithm to create initial clusters of the before
	 * learning the cluster and developer parameters
	 * 
	 * @param bugs
	 * @param eps
	 * @param minPoints
	 */
	public CollaborativeFiltering(List<BugInfo> bugs, int K) {

		Map<Long, BugInfo> bugMap = new HashMap<>();
		for (BugInfo bug : bugs) {
			bugMap.put(bug.getBugID(), bug.clone());
		}

		DataNormalization.normalizeData(bugs);
		DataClusterer kkmeans = new KernelKMeans(bugs, K);
		this.developers = kkmeans.createClusters();
		List<List<BugInfo>> kkmeansClusters = kkmeans.getClusters();

		init(bugMap, kkmeansClusters);
	}

	/**
	 * Initializes the vote matrix and
	 * 
	 * @param bugMap
	 * @param clusters
	 */
	private void init(Map<Long, BugInfo> bugMap, List<List<BugInfo>> clusters) {

		for (int i = 0; i < clusters.size(); i++) {
			List<BugInfo> cluster = new ArrayList<>();
			this.clusters.add(cluster);

			for (BugInfo bug : clusters.get(i)) {
				cluster.add(bugMap.get(bug.getBugID()));
			}
		}

		this.allDevs = new HashSet<>();
		for (Set<DeveloperInfo> set : this.developers) {
			this.allDevs.addAll(set);
		}

		// initialize the feature matrix for CF
		this.votesMatrix = new double[this.clusters.size()][this.allDevs.size()];
		this.meanVotes = new double[this.clusters.size()];

		initMatrix();
		trainModel();
	}

	private void initMatrix() {

		for (int i = 0; i < this.clusters.size(); i++) {

			List<BugInfo> cluster = this.clusters.get(i);
			Set<DeveloperInfo> clusterDevs = this.developers.get(i);

			// average voting per cluster by the developers
			this.meanVotes[i] = 1.0 / clusterDevs.size();

			Map<String, Integer> devContibution = new HashMap<>();

			double[] clusterFeatures = new double[BugFeatures.NUM_ATTRIBUTES];
			clustersInfo.add(new BugInfo(System.currentTimeMillis(), clusterFeatures, null));

			for (BugInfo bug : cluster) {
				double[] bugFeatures = bug.getFeatures();

				// sum feature values for all the bugs in a cluster
				for (int j = 0; j < clusterFeatures.length; j++) {
					clusterFeatures[j] += bugFeatures[j];
				}

				// count number of bugs solved by all the devs in this cluster
				for (DeveloperInfo bugDev : bug.getDevelopers()) {
					Integer count = devContibution.get(bugDev.getDevID());
					if (null == count) {
						devContibution.put(bugDev.getDevID(), 1);
					} else {
						devContibution.put(bugDev.getDevID(), count + 1);
					}
				}
			}

			for (DeveloperInfo dev : clusterDevs) {
				Integer devIndex = devIndexMapping.get(dev.getDevID());

				if (null == devIndex) {
					devIndex = devIndexMapping.size();
					devIndexMapping.put(dev.getDevID(), devIndex);
				}

				this.votesMatrix[i][devIndex] = ((double) devContibution.get(dev.getDevID())) / cluster.size();
			}
		}

		DataNormalization.normalizeData(clustersInfo);
		DataNormalization.normalizeData(allDevs);
	}

	public void trainModel() {

		List<BugInfo> originalClusters = new ArrayList<>();
		List<DeveloperInfo> originalDevelopers = new ArrayList<>();

		int count = 0;
		do {
			count++;
			originalClusters.clear();
			originalDevelopers.clear();

			learnClusterFeatures(originalClusters);
			learnDeveloperFeatures(originalDevelopers, originalClusters);

		} while (!converged(originalClusters, this.clustersInfo) || !converged(originalDevelopers, this.allDevs));

//		System.out.println("Iterations : " + count);
	}

	private void learnDeveloperFeatures(List<DeveloperInfo> originalDevelopers, List<BugInfo> originalClusters) {

		for (DeveloperInfo dev : this.allDevs) {

			// create a copy for comparison
			DeveloperInfo orginalDev = dev.clone();
			originalDevelopers.add(orginalDev);

			for (int i = 0; i < originalClusters.size(); i++) {

				double[] clusterFeatures = originalClusters.get(i).getFeatures();

				double y = this.votesMatrix[i][devIndexMapping.get(orginalDev.getDevID())];
				double dotProduct = dotProduct(orginalDev.getFeatures(), clusterFeatures);
				double diff = dotProduct - y;

				double[] clusterFeaturesCopy = Arrays.copyOf(clusterFeatures, clusterFeatures.length);

				for (int j = 0; j < clusterFeaturesCopy.length; j++) {
					clusterFeaturesCopy[j] = (clusterFeaturesCopy[j] * diff) + (LAMBDA * orginalDev.getFeatures()[j]);
				}

				for (int j = 0; j < clusterFeaturesCopy.length; j++) {
					dev.getFeatures()[j] -= ALPHA * clusterFeaturesCopy[j];
				}
			}
		}
	}

	private void learnClusterFeatures(List<BugInfo> originalClusters) {

		for (int i = 0; i < this.clustersInfo.size(); i++) {
			double[] clusterFeatures = this.clustersInfo.get(i).getFeatures();

			// create a copy for comparison
			originalClusters.add(this.clustersInfo.get(i).clone());

			for (DeveloperInfo dev : allDevs) {// should be done only for devs
												// in this cluster
				double y = this.votesMatrix[i][devIndexMapping.get(dev.getDevID())];
				double dotProduct = dotProduct(dev.getFeatures(), originalClusters.get(i).getFeatures());
				double diff = dotProduct - y;

				double[] devFeaturesCopy = Arrays.copyOf(dev.getFeatures(), dev.getDimension());

				for (int j = 0; j < clusterFeatures.length; j++) {
					devFeaturesCopy[j] = (devFeaturesCopy[j] * diff)
							+ (LAMBDA * originalClusters.get(i).getFeatures()[j]);
				}

				for (int j = 0; j < clusterFeatures.length; j++) {
					clusterFeatures[j] -= ALPHA * devFeaturesCopy[j];
				}
			}
		}
	}

	private boolean converged(Collection<? extends DataPoint> originalPoints,
			Collection<? extends DataPoint> newPoints) {

		Iterator<? extends DataPoint> originalIterator = originalPoints.iterator();
		Iterator<? extends DataPoint> newIterator = newPoints.iterator();

		while (originalIterator.hasNext() && newIterator.hasNext()) {
			double[] originalFeatures = originalIterator.next().getFeatures();
			double[] newFeatures = newIterator.next().getFeatures();

			for (int j = 0; j < newFeatures.length; j++) {
				if (Math.abs(originalFeatures[j] - newFeatures[j]) > EPSILON)
					return false;
			}

		}

		return true;
	}

	private double dotProduct(double[] devFeatures, double[] clusterFeatures) {

		double val = 0;
		for (int i = 0; i < clusterFeatures.length; i++) {
			val += devFeatures[i] * clusterFeatures[i];
		}

		return val;
	}

	@Override
	public Set<DeveloperInfo> recommendDevelopers(BugInfo newBug, int K) {
		if (null == newBug)
			return null;

		class DevRanking implements Comparable<DevRanking> {
			int devIndex;
			double contribution;
			DeveloperInfo developer;

			DevRanking(int devIndex, double contribution, DeveloperInfo developer) {
				this.devIndex = devIndex;
				this.contribution = contribution;
				this.developer = developer;
			}

			DeveloperInfo getDeveloper() {
				return developer;
			}

			public void addContribution(double contribution) {
				this.contribution += contribution;
			}

			@Override
			public int compareTo(DevRanking dev) {

				if (this.contribution > dev.contribution)
					return -1;

				if (this.contribution < dev.contribution)
					return 1;

				return 0;
			}

			@Override
			public String toString() {
				return "(" + developer.getDevID() + " = " + contribution + ")";
			}
		}

		Map<Integer, DevRanking> contributions = new HashMap<>();

		for (int i = 0; i < this.votesMatrix.length; i++) {
			double similarity = newBug.similarity(this.clustersInfo.get(i));
			double mean = this.meanVotes[i];

			for (DeveloperInfo dev : this.developers.get(i)) {
				Integer devIndex = devIndexMapping.get(dev.getDevID());
				double vote = this.votesMatrix[i][devIndex];
				double contribution = similarity * (vote - mean);

				DevRanking devRanking = contributions.get(devIndex);
				if (null == devRanking) {
					contributions.put(devIndex, new DevRanking(devIndex, contribution, dev));
				} else {
					devRanking.addContribution(contribution);
				}
			}

		}

		List<DevRanking> devs = new ArrayList<>(contributions.values());
		Collections.sort(devs);

		Set<DeveloperInfo> recommendedDevs = new LinkedHashSet<>();

		for (int i = 0; i < K && i < devs.size(); i++) {
			recommendedDevs.add(devs.get(i).getDeveloper());
		}
		
		return recommendedDevs;
	}
}
