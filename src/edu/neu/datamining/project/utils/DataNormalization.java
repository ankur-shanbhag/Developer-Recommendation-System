package edu.neu.datamining.project.utils;

import java.util.Collection;

import edu.neu.datamining.project.data.DataPoint;

/**
 * This class implements several normalization techniques required to re-scaling
 * the feature values or to make them comparable
 * 
 * @author Ankur Shanbhag
 *
 */
public class DataNormalization {

	private DataNormalization() {
		// deny object creation
	}

	/**
	 * Performs z score normalization of all the feature values for the given
	 * data points
	 * 
	 * @param dataPoints
	 */
	public static void zScoreNormalize(
			Collection<? extends DataPoint> dataPoints) {

		double mean[] = calculateMean(dataPoints);

		double sd[] = calculateStandardDeviation(dataPoints, mean);

		for (DataPoint instance : dataPoints) {
			double[] x = instance.getFeatures();

			for (int i = 0; i < x.length; i++) {
				double val = (x[i] - mean[i]) / sd[i];

				if (Double.isNaN(val))
					x[i] = 0;
				else
					x[i] = val;
			}
		}
	}

	/**
	 * Computes mean values for all the features of the given data points
	 * 
	 * @param dataPoints
	 * @return
	 */
	private static double[] calculateMean(
			Collection<? extends DataPoint> dataPoints) {

		double mean[] = null;

		for (DataPoint instance : dataPoints) {
			double[] x = instance.getFeatures();

			if (null == mean)
				mean = new double[x.length];

			for (int i = 0; i < x.length; i++) {
				mean[i] += x[i];
			}
		}

		for (int i = 0; i < mean.length; i++) {
			mean[i] /= dataPoints.size();
		}

		return mean;
	}

	/**
	 * Computes standard deviation for all the features of the given data points
	 * 
	 * @param dataPoints
	 * @param mean
	 * @return
	 */
	private static double[] calculateStandardDeviation(
			Collection<? extends DataPoint> dataPoints, double[] mean) {

		double sd[] = new double[mean.length];

		for (DataPoint instance : dataPoints) {
			double[] x = instance.getFeatures();
			for (int i = 0; i < x.length; i++) {
				sd[i] += Math.pow(x[i] - mean[i], 2);
			}
		}

		for (int i = 0; i < sd.length; i++) {
			sd[i] /= dataPoints.size();
			sd[i] = Math.sqrt(sd[i]);
		}

		return sd;
	}

	/**
	 * Performs min-max normalization of the given feature value across all the
	 * data points
	 * 
	 * @param dataPoints
	 * @param pos
	 */
	public static void minMaxNormalize(Collection<DataPoint> dataPoints, int pos) {

		// initialize min/max to extreme values
		double max = Double.MIN_VALUE;
		double min = Double.MIN_VALUE;

		for (DataPoint instance : dataPoints) {
			double time = instance.getFeatures()[pos];
			if (max < time) {
				max = time;
			} else if (min > time) {
				min = time;
			}
		}

		double minMaxDiff = max - min;

		for (DataPoint instance : dataPoints) {
			double time = instance.getFeatures()[pos];
			instance.getFeatures()[pos] = (time - min) / minMaxDiff;
		}

	}

}
