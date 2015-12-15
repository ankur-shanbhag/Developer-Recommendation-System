package edu.neu.datamining.project.algorithms.validate;

/**
 * Determines the type of algorithm to be used for cross validation
 * 
 * @author Ankur Shanbhag
 *
 */
public enum AlgorithmType {
	/**
	 * KNN algorithm for recommending developers
	 */
	KNN,

	/**
	 * Collaborative Filtering with Kernel KMeans algorithm for clustering
	 * similar bugs
	 */
	COLLABORATIVE_FILTERING_KKMEANS,

	/**
	 * Collaborative Filtering with DBSCAN algorithm for clustering similar bugs
	 */
	COLLABORATIVE_FILTERING_DBSCAN
}
