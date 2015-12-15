package edu.neu.datamining.project.conf;

/**
 * This utility class holds all the configuration constants used
 * 
 * @author Ankur Shanbhag
 */
public final class DataConfiguration {

	public static final String GROUP_ALIAS_FILE_PATH = "data/group-alias-email-ids.txt";

	public static final String BUG_DEV_MAP_FILE_PATH = "data/bugToDeveloperMapping.csv";

	/**
	 * Path to the data points used for training the model. These are bugs with
	 * status fixed (Around 3K bugs)
	 */
	public static final String BUGS_INFO_FILE_PATH_FIXED = "data/bug-data-fixed.json";

	/**
	 * Path to the data points used for training the model. These are bugs with
	 * status fixed (Around 27K bugs)
	 */
	public static final String BUGS_INFO_FILE_PATH_FIXED_VERIFIED = "data/bug-data-fixed-verified.json";

	private DataConfiguration() {
		// deny object creation for configuration files
	}
}
