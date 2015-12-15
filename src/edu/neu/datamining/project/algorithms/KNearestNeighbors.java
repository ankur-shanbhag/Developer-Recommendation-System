package edu.neu.datamining.project.algorithms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import edu.neu.datamining.project.conf.DataConfiguration;
import edu.neu.datamining.project.data.BugInfo;
import edu.neu.datamining.project.data.DataPoint;
import edu.neu.datamining.project.data.DeveloperInfo;
import edu.neu.datamining.project.utils.DataNormalization;
import edu.neu.datamining.project.utils.FileUtils;

/**
 * Implementation for kNN algorithm for predicting the labels of data points
 * with n-dimensional features using Euclidean distance method
 * 
 * @author Ankur Shanbhag
 * @see CollaborativeFiltering
 * 
 */
public class KNearestNeighbors implements RecommendationAlgorithm {

	private final List<BugInfo> bugDataPoints;
	private final List<DeveloperInfo> devDataPoints;
	private final double threshold;

	/**
	 * Initializes the number of neighbors to be considered by the kNN algorithm
	 * 
	 * @param k
	 *            - number of neighbors
	 */
	public KNearestNeighbors(List<BugInfo> bugInfos,
			List<DeveloperInfo> devInfos) {

		// create a copy of the bugs
		this.bugDataPoints = new ArrayList<>();
		for (BugInfo bug : bugInfos)
			this.bugDataPoints.add(bug.clone());

		// create a copy of developers
		this.devDataPoints = new ArrayList<>();
		for (DeveloperInfo dev : devInfos)
			this.devDataPoints.add(dev.clone());

		DataNormalization.zScoreNormalize(bugDataPoints);
		DataNormalization.zScoreNormalize(devDataPoints);

		this.threshold = getThreshold();
	}

	/**
	 * Initializes the number of neighbors to be considered by the kNN algorithm
	 * 
	 * @param k
	 *            - number of neighbors
	 */
	public KNearestNeighbors(List<BugInfo> bugInfos,
			List<DeveloperInfo> devInfos, double threshold) {
		this.bugDataPoints = new ArrayList<>();
		for (BugInfo bug : bugInfos)
			this.bugDataPoints.add(bug.clone());

		this.devDataPoints = new ArrayList<>();
		for (DeveloperInfo dev : devInfos)
			this.devDataPoints.add(dev.clone());

		DataNormalization.zScoreNormalize(bugDataPoints);
		DataNormalization.zScoreNormalize(devDataPoints);

		this.threshold = threshold;
	}

	private double getThreshold() {

		double total = 0;
		for (int i = 0; i < bugDataPoints.size(); i++) {
			for (int j = i + 1; j < bugDataPoints.size(); j++) {
				double distance = bugDataPoints.get(i).distance(
						bugDataPoints.get(j));
				total += 2 * distance;
			}
		}

		return total / (bugDataPoints.size() * bugDataPoints.size());
	}

	/**
	 * Start point for kNN algorithm
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		List<BugInfo> bugDataPoints = FileUtils
				.readBugsInfo(DataConfiguration.BUGS_INFO_FILE_PATH_FIXED_VERIFIED);

		List<DeveloperInfo> developersInfo = FileUtils.getDevelopersInfo();

		KNearestNeighbors knn = new KNearestNeighbors(bugDataPoints,
				developersInfo);
	}

	/**
	 * Predicts label for the input data point based on k nearest neighbors from
	 * the training data points provided as input
	 * 
	 * @param bugsPoints
	 *            - data points with labels for calculating k neighbors
	 * @param dataPoint
	 *            - data point for which label is to be predicted
	 * @return - predicted label for the input data point
	 */
	private Set<DeveloperInfo> classifyBugInfo(BugInfo dataPoint, final int K) {

		// get the distance for all the points from given data point
		Map<Double, Set<DataPoint>> kNNMap = getNeighborsByDistance(
				bugDataPoints, dataPoint);

		// loop-up to store labels for k nearest neighbors
		Set<DeveloperInfo> developers = new HashSet<>();

		Iterator<Entry<Double, Set<DataPoint>>> iterator = kNNMap.entrySet()
				.iterator();

		while (iterator.hasNext()) {

			Entry<Double, Set<DataPoint>> entry = iterator.next();
			Set<DataPoint> bugs = entry.getValue();

			if (entry.getKey() > threshold)
				return developers;

			for (DataPoint bug : bugs) {
				Set<DeveloperInfo> devs = ((BugInfo) bug).getDevelopers();

				for (DeveloperInfo dev : devs) {
					developers.add(dev);

					if (developers.size() >= K) {
						return developers;
					}
				}
			}
		}

		return developers;

	}

	/**
	 * Predicts label for the input data point based on k nearest neighbors from
	 * the training data points provided as input
	 * 
	 * @param bugsPoints
	 *            - data points with labels for calculating k neighbors
	 * @param dataPoint
	 *            - data point for which label is to be predicted
	 * @return - predicted label for the input data point
	 */
	private Set<DeveloperInfo> classifyBugsInfo(List<BugInfo> trainPoints,
			BugInfo dataPoint, final int K, final double threshold) {

		// get the distance for all the points from given data point
		SortedMap<Double, Set<DataPoint>> kNNMap = getNeighborsByDistance(
				trainPoints, dataPoint);

		SortedMap<Double, Set<DataPoint>> map = new TreeMap<>();

		int neighborCount = 0;
		Iterator<Entry<Double, Set<DataPoint>>> neighborIterator = kNNMap
				.entrySet().iterator();

		while (neighborCount < K && neighborIterator.hasNext()) {

			Entry<Double, Set<DataPoint>> neighborEntry = neighborIterator
					.next();
			Double distance = neighborEntry.getKey();

			if (distance < threshold)
				break;

			Set<DataPoint> neighbors = map.get(distance);
			if (null == neighbors) {
				map.put(distance, neighborEntry.getValue());
			} else {
				neighbors.addAll(neighborEntry.getValue());
			}

			neighborCount += neighborEntry.getValue().size();
		}

		if (neighborCount < K) {

		}

		Set<DeveloperInfo> predictedDevelopers = new HashSet<>();
		return null;
	}

	/**
	 * Predicts label for the input data point based on k nearest neighbors from
	 * the training data points provided as input
	 * 
	 * @param bugsPoints
	 *            - data points with labels for calculating k neighbors
	 * @param dataPoint
	 *            - data point for which label is to be predicted
	 * @return - predicted label for the input data point
	 */
	private Set<DeveloperInfo> classifyDevInfo(
			List<DeveloperInfo> trainDevelopers,
			Set<DeveloperInfo> testDevelopers, final int K) {

		SortedMap<Double, Set<DataPoint>> kNNMap = new TreeMap<>();

		// get the distance for all the points from given data point
		for (DeveloperInfo dev : testDevelopers) {

			SortedMap<Double, Set<DataPoint>> map = getNeighborsByDistance(
					trainDevelopers, dev);

			for (Map.Entry<Double, Set<DataPoint>> entry : map.entrySet()) {
				Set<DataPoint> devs = kNNMap.get(entry.getKey());

				if (null == devs) {
					kNNMap.put(entry.getKey(), entry.getValue());
				} else {
					devs.addAll(entry.getValue());
				}
			}
		}

		// loop-up to store labels for k nearest neighbors
		Set<DeveloperInfo> developers = new LinkedHashSet<>();

		Iterator<Entry<Double, Set<DataPoint>>> iterator = kNNMap.entrySet()
				.iterator();

		int neighbourCount = 0;

		while (neighbourCount < K && iterator.hasNext()) {
			Set<DataPoint> devs = iterator.next().getValue();
			for (DataPoint dev : devs) {
				developers.add((DeveloperInfo) dev);
			}

			// count neighbor bugs not developers
			neighbourCount++;
		}

		return developers;
	}

	/**
	 * Calculates distance between the specified data point and every point
	 * specified in the training set
	 * 
	 * @param dataPoints
	 * 
	 * @param dataPoints
	 *            - training set
	 * @param dataPoint
	 *            - data point from which the distance needs to be calculated
	 * @return map representing distance of the given data point from every data
	 *         point in the training set
	 */
	private static SortedMap<Double, Set<DataPoint>> getNeighborsByDistance(
			List<? extends DataPoint> dataPoints, DataPoint dataPoint) {

		// map to store distance from every point in the given data set
		SortedMap<Double, Set<DataPoint>> kNNMap = new TreeMap<>();

		for (DataPoint neighbour : dataPoints) {

			// do not calculate distance between same data points
			if (neighbour.equals(dataPoint))
				continue;

			// calculate distance from all the points one by one
			double distance = neighbour.distance(dataPoint);

			// store them in the map. Map will also store multiple points which
			// are equi-distant from given data point
			Set<DataPoint> devs = kNNMap.get(distance);

			if (null == devs) {
				Set<DataPoint> set = new HashSet<>();
				set.add(neighbour);
				kNNMap.put(distance, set);
			} else {
				devs.add(neighbour);
			}
		}
		return kNNMap;
	}

	@Override
	public Set<DeveloperInfo> recommendDevelopers(BugInfo testBug, final int K) {
		Set<DeveloperInfo> predictedDevelopers = classifyBugInfo(testBug, K);

		if (null == predictedDevelopers || predictedDevelopers.isEmpty())
			return new HashSet<>();

		if (predictedDevelopers.size() < K) {

			Set<DeveloperInfo> allDevs = classifyDevInfo(devDataPoints,
					predictedDevelopers, K);

			int neighbourCount = predictedDevelopers.size();
			Iterator<DeveloperInfo> iterator = allDevs.iterator();

			while (neighbourCount < K && iterator.hasNext()) {
				DeveloperInfo dev = iterator.next();
				predictedDevelopers.add(dev);
				neighbourCount++;
			}
		}

		// System.out.println(predictedDevelopers.size());
		return predictedDevelopers;
	}
}
