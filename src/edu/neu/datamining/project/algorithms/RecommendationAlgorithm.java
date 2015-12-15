package edu.neu.datamining.project.algorithms;

import java.util.Set;

import edu.neu.datamining.project.data.BugInfo;
import edu.neu.datamining.project.data.DeveloperInfo;

/**
 * This interface defines a contract for all the implementing classes which
 * implements algorithms for developer recommendation system.
 * 
 * @author Ankur Shanbhag
 */
public interface RecommendationAlgorithm {

	/**
	 * This method is invoked on the trained model which recommends top
	 * <tt>K</tt> developers for the given test bug
	 * 
	 * @param testBug
	 *            - The bug for which the system needs to recommend developers
	 * @param K
	 *            - Number of developers to be recommended
	 * @return A set of recommended developers
	 */
	Set<DeveloperInfo> recommendDevelopers(BugInfo testBug, int K);
}
