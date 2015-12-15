package edu.neu.datamining.project.data;

/**
 * This interface specifies contract for all the classes representing data model
 * 
 * @author Ankur Shanbhag
 */
public interface DataPoint {

	/**
	 * Every data point must provide its features to be used for implementing
	 * data mining algorithms
	 * 
	 * @return array of feature values
	 */
	double[] getFeatures();

	/**
	 * Gets the dimension of the features associated with the data point
	 * 
	 * @return number of features representing data point
	 */
	int getDimension();

	/**
	 * Implementation of distance measure to computes the dissimilarity of this
	 * data point from the specified data point
	 * 
	 * @param point
	 *            - other data point
	 * @return distance computed from given data point
	 */
	double distance(DataPoint point);

	/**
	 * Implementation of similarity measure to computes the similarity of this
	 * data point from the specified data point
	 * 
	 * @param point
	 *            - other data point
	 * @return similarity computed from given data point
	 */
	double similarity(DataPoint point);
}
