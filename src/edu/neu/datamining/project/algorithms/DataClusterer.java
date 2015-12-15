package edu.neu.datamining.project.algorithms;

import java.util.List;
import java.util.Set;

import edu.neu.datamining.project.data.BugInfo;
import edu.neu.datamining.project.data.DeveloperInfo;

/**
 * This interface defines contract for all the classes implementing data
 * clustering algorithms
 * 
 * @author Ankur Shanbhag
 *
 */
public interface DataClusterer {

	/**
	 * This method will create clusters based on the algorithm used by the
	 * implementing class
	 * 
	 * @return
	 */
	List<Set<DeveloperInfo>> createClusters();

	/**
	 * Returns a list of all the
	 * 
	 * @return
	 */
	List<List<BugInfo>> getClusters();

	/**
	 * 
	 * @return
	 */
	List<Set<DeveloperInfo>> getDevelopers();
}
