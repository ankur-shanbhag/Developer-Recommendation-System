package edu.neu.datamining.project.data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.math3.ml.clustering.Clusterable;

public class BugInfo implements DataPoint, Clusterable {

	private double[] features;
	private long bugID;
	private Set<DeveloperInfo> developers;

	public BugInfo(long bugID, double[] x, Set<DeveloperInfo> developers) {
		this.bugID = bugID;
		this.features = x;

		if (null != developers) {
			this.developers = developers;
		} else {
			this.developers = new HashSet<>();
		}
	}

	@Override
	public double[] getFeatures() {
		return features;
	}

	@Override
	public int getDimension() {
		return features.length;
	}

	public long getBugID() {
		return bugID;
	}

	public void setBugID(long bugID) {
		this.bugID = bugID;
	}

	public Set<DeveloperInfo> getDevelopers() {
		return developers;
	}

	public void setDevelopers(Set<DeveloperInfo> developers) {
		this.developers = developers;
	}

	/**
	 * Calculates euclidean distance of this point from given point
	 * 
	 * @param point
	 * @return
	 */
	@Override
	public double distance(DataPoint point) {

		// Return as dissimilar object when not of type bug-info
		if (point == null || point.getClass() != BugInfo.class)
			return Double.MAX_VALUE;

		double distance = 0.0;

		double[] x2 = point.getFeatures();

		for (int i = 0; i < features.length; i++) {
			// add square of differences
			distance += Math.pow(features[i] - x2[i], 2);
		}

		// take square root to get euclidean distance
		return Math.sqrt(distance);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (double feature : features) {
			builder.append(feature).append(",");
		}

		return builder.deleteCharAt(builder.length() - 1).toString().trim();
	}

	@Override
	public double[] getPoint() {
		return this.getFeatures();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (bugID ^ (bugID >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BugInfo other = (BugInfo) obj;
		if (bugID != other.bugID)
			return false;
		return true;
	}

	@Override
	public BugInfo clone() {
		return new BugInfo(bugID, Arrays.copyOf(features, features.length), developers);
	}

	@Override
	public double similarity(DataPoint point) {

		// Return as dissimilar object when not of type bug-info
		if (point == null || point.getClass() != BugInfo.class)
			return 0;

		double dotProduct = 0;
		for (int i = 0; i < getDimension(); i++) {
			dotProduct += features[i] * point.getFeatures()[i];
		}

		double sqr1 = 0;
		double sqr2 = 0;
		for (int i = 0; i < getDimension(); i++) {
			sqr1 += features[i] * features[i];
			sqr2 += point.getFeatures()[i] * point.getFeatures()[i];
		}

		return dotProduct / (Math.sqrt(sqr1) * Math.sqrt(sqr2));
	}

}
