package edu.neu.datamining.project.utils;

import java.util.Collection;

import edu.neu.datamining.project.data.DataPoint;

public class DataNormalization {

	private DataNormalization() {
		// deny object creation
	}

	public static void normalizeData(Collection<? extends DataPoint> dataPoints) {

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

	private static double[] calculateMean(Collection<? extends DataPoint> dataPoints) {

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

}
