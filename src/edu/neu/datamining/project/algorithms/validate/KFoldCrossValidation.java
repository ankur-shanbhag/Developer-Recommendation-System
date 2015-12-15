package edu.neu.datamining.project.algorithms.validate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import edu.neu.datamining.project.algorithms.CollaborativeFiltering;
import edu.neu.datamining.project.algorithms.KNearestNeighbors;
import edu.neu.datamining.project.algorithms.RecommendationAlgorithm;
import edu.neu.datamining.project.conf.DataConfiguration;
import edu.neu.datamining.project.data.BugInfo;
import edu.neu.datamining.project.data.DeveloperInfo;
import edu.neu.datamining.project.utils.FileUtils;

/**
 * This class implements K-fold cross validation for evaluating the
 * recommendation algorithms implemented
 * 
 * @author Ankur Shanbhag
 *
 */
public class KFoldCrossValidation {

	private final int K_FOLDS;

	public KFoldCrossValidation(int K) {
		this.K_FOLDS = K;
	}

	private double predictFoldAccuracy(List<BugInfo> bugTrainPoints, List<BugInfo> testPoints,
			List<DeveloperInfo> developers, AlgorithmType algorithm) {

		double accuratePredictions = 0;

		RecommendationAlgorithm recommender = null;

		switch (algorithm) {
		case KNN:
			recommender = new KNearestNeighbors(bugTrainPoints, developers, 5);
			break;

		case COLLABORATIVE_FILTERING_KKMEANS:
			recommender = new CollaborativeFiltering(bugTrainPoints, 10);
			break;

		case COLLABORATIVE_FILTERING_DBSCAN:
			recommender = new CollaborativeFiltering(bugTrainPoints, 3, 25);
			break;
		}

		// check accuracy of prediction
		for (BugInfo bugTestPoint : testPoints) {
			Set<DeveloperInfo> predictedDevelopers = recommender.recommendDevelopers(bugTestPoint, 10);

			for (DeveloperInfo actualDeveloper : bugTestPoint.getDevelopers()) {
				if (predictedDevelopers.contains(actualDeveloper)) {
					// any one should match
					accuratePredictions++;
					break;
				}
			}
		}

		return accuratePredictions;
	}

	/**
	 * Performs K-fold cross validation of the all the algorithm on given data
	 * points
	 * 
	 * @param dataPoints
	 *            - data points to validate the kNN algorithm
	 */
	public void crossValidate(List<BugInfo> dataPoints, List<DeveloperInfo> developers, AlgorithmType algorithm) {

		// fold size for computing folds of same size
		final int foldSize = dataPoints.size() / K_FOLDS;

		// list of k-folds
		List<List<BugInfo>> folds = createFolds(dataPoints, foldSize);

		// holds accuracy of predictions on all the folds
		double accuracyRate = 0.0;

		// create different fold combinations for creating train and test data
		// points in the ratio k-1:1 (folds)
		for (int i = 0; i < K_FOLDS; i++) {

			// holds all the train points for this particular iteration (size
			// k-1 folds)
			List<BugInfo> trainPoints = new ArrayList<>();

			// test set - 1 fold
			List<BugInfo> testPoints = null;

			for (int j = 0; j < K_FOLDS; j++) {
				if (j == i) {
					// use this as test set for this iteration
					testPoints = folds.get(j);
				} else {
					// treat rest of the folds as training set
					trainPoints.addAll(folds.get(j));
				}
			}

			double accuratePredictions = predictFoldAccuracy(trainPoints, testPoints, developers, algorithm);

			accuracyRate += (accuratePredictions / testPoints.size());

			System.out.println("Prediction accuracy for test data as fold" + (i + 1) + " : "
					+ (accuratePredictions / testPoints.size()));
		}

		// average accuracy across all the fold combinations
		System.out.println("Average accuracy :: " + (accuracyRate / K_FOLDS));

	}

	/**
	 * Creates k-folds of the data points (randomly) with all the folds of the
	 * same size as specified by the foldSize (except for the last one)
	 * 
	 * @param dataPoints
	 *            - data points to be folded
	 * @param foldSize
	 *            - size limit for every fold
	 * @return list of all the folds computed randomly by this method
	 */
	private List<List<BugInfo>> createFolds(List<BugInfo> dataPoints, int foldSize) {

		List<BugInfo> list = new ArrayList<>(dataPoints);

		// Shuffle the data points to generate randomized K partitions
		Collections.shuffle(list);

		List<List<BugInfo>> foldDataList = new ArrayList<>();

		// generate k-folds
		for (int i = 0; i < K_FOLDS; i++) {
			if (i == K_FOLDS - 1) {
				// k-1 folds of same size
				foldDataList.add(list.subList(i * foldSize, list.size()));
			} else {
				// last fold will utmost (k-1) elements more or less
				foldDataList.add(list.subList(i * foldSize, (i + 1) * foldSize));
			}
		}

		// return all the folds
		return foldDataList;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {

		// Read bug data from file
		List<BugInfo> bugsInfo = FileUtils.readBugsInfo(DataConfiguration.BUGS_INFO_FILE_PATH_FIXED);
		List<DeveloperInfo> developersInfo = FileUtils.getDevelopersInfo();

		// // Validate KNN algorithm
		// System.out.println("Starting KNN ...");
		// KFoldCrossValidation kNNValidator = new KFoldCrossValidation(5);
		// kNNValidator.crossValidate(bugsInfo, developersInfo,
		// AlgorithmType.KNN);

		// validate Collaborative filtering with Kernel KMeans
		System.out.println("Starting Collaborative filtering with Kernel KMeans ...");
		KFoldCrossValidation cfKKmeansValidator = new KFoldCrossValidation(5);
		cfKKmeansValidator.crossValidate(bugsInfo, developersInfo, AlgorithmType.COLLABORATIVE_FILTERING_KKMEANS);

		// // validate Collaborative filtering with DBSCAN
		// System.out.println("Starting Collaborative filtering with DBSCAN
		// ...");
		// KFoldCrossValidation cfDBScanValidator = new KFoldCrossValidation(5);
		// cfDBScanValidator.crossValidate(bugsInfo, developersInfo,
		// AlgorithmType.COLLABORATIVE_FILTERING_DBSCAN);

	}
}
