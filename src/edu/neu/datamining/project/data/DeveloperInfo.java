package edu.neu.datamining.project.data;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.ml.clustering.Clusterable;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeveloperInfo implements DataPoint, Clusterable {

	private final String devID;
	private static final ObjectMapper mapper;
	
	static {
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	}
	
	@JsonIgnore
	private double[] features;

	public DeveloperInfo(String devID, double[] x) {
		this.devID = devID;
		this.features = x;
	}

	public String getDevID() {
		return devID;
	}

	@Override
	public double[] getFeatures() {
		return features;
	}

	@Override
	public int getDimension() {
		if (null != features)
			return features.length;

		return 0;
	}

	public void setFeatures(double[] x) {
		this.features = x;
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
		if (point == null || point.getClass() != DeveloperInfo.class)
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((devID == null) ? 0 : devID.hashCode());
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
		DeveloperInfo other = (DeveloperInfo) obj;
		if (devID == null) {
			if (other.devID != null)
				return false;
		} else if (!devID.equals(other.devID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (null == features)
			return devID + "[]";

		return devID + Arrays.toString(features);
	}

	@Override
	public double[] getPoint() {
		return this.getFeatures();
	}

	@Override
	public DeveloperInfo clone() {
		return new DeveloperInfo(devID, Arrays.copyOf(features, features.length));
	}

	@Override
	public double similarity(DataPoint point) {
		// TODO : implementation of similarity
		return 0;
	}
	
	public static String serialize(List<DeveloperInfo> list) throws JsonProcessingException {
		String jsonString = mapper.writeValueAsString(list);
		System.out.println(jsonString);
		return jsonString;
	}
}
