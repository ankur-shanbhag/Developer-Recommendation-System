package edu.neu.datamining.project.conf;

import java.util.HashMap;
import java.util.Map;

public class BugFeatures {

	public static final int NUM_FEATURES = 5;
	public static final int NUM_ATTRIBUTES = 44;

	public static final Map<Integer, String> INDEX_FEATURE_MAPPING = new HashMap<>();

	public static final String BUG_ID = "bugid";

	// Different components of the Eclipse project
	public static final String COMPONENT_PLATFORM = "platform";
	public static final String COMPONENT_JDT = "jdt";
	public static final String COMPONENT_CDT = "cdt";
	public static final String COMPONENT_PDE = "pde";
	// Component index in feature matrix
	public static final int IDX_COMPONENT_PLATFORM = 0;
	public static final int IDX_COMPONENT_JDT = 1;
	public static final int IDX_COMPONENT_CDT = 2;
	public static final int IDX_COMPONENT_PDE = 3;

	// Bug severity
	public static final String SEVERITY_BLOCKER = "blocker";
	public static final String SEVERITY_CRITICAL = "critical";
	public static final String SEVERITY_MAJOR = "major";
	public static final String SEVERITY_NORMAL = "normal";
	public static final String SEVERITY_MINOR = "minor";
	public static final String SEVERITY_TRIVIAL = "trivial";
	// Bug severity indices
	public static final int IDX_SEVERITY_BLOCKER = 4;
	public static final int IDX_SEVERITY_CRITICAL = 5;
	public static final int IDX_SEVERITY_MAJOR = 6;
	public static final int IDX_SEVERITY_NORMAL = 7;
	public static final int IDX_SEVERITY_MINOR = 8;
	public static final int IDX_SEVERITY_TRIVIAL = 9;

	// Operating Systems used
	public static final String OPERATING_SYSTEMS_ALL = "all";
	public static final String OPERATING_SYSTEMS_OTHERS = "other";
	public static final String OPERATING_SYSTEMS_MAC = "mac";
	public static final String OPERATING_SYSTEMS_WINDOWS = "windows";
	public static final String OPERATING_SYSTEMS_LINUX = "linux";
	// OS Indices
	public static final int IDX_OPERATING_SYSTEMS_ALL = 10;
	public static final int IDX_OPERATING_SYSTEMS_OTHERS = 11;
	public static final int IDX_OPERATING_SYSTEMS_MAC = 12;
	public static final int IDX_OPERATING_SYSTEMS_WINDOWS = 13;
	public static final int IDX_OPERATING_SYSTEMS_LINUX = 14;

	// Bug priority
	public static final String PRIORITY_1 = "p1";
	public static final String PRIORITY_2 = "p2";
	public static final String PRIORITY_3 = "p3";
	public static final String PRIORITY_4 = "p4";
	public static final String PRIORITY_5 = "p5";
	public static final String PRIORITY_NONE = "none";
	// Bug priority indices
	public static final int IDX_PRIORITY_1 = 15;
	public static final int IDX_PRIORITY_2 = 16;
	public static final int IDX_PRIORITY_3 = 17;
	public static final int IDX_PRIORITY_4 = 18;
	public static final int IDX_PRIORITY_5 = 19;
	public static final int IDX_PRIORITY_NONE = 20;

	// Components
	public static final String COMPONENT_DOC = "doc";
	public static final String COMPONENT_SEARCH = "search";
	public static final String COMPONENT_SWT = "swt";
	public static final String COMPONENT_IDE = "ide";
	public static final String COMPONENT_RUNTIME = "runtime";
	public static final String COMPONENT_CORE = "core";
	public static final String COMPONENT_UI = "ui";
	public static final String COMPONENT_CDT_CORE = "cdt-core";
	public static final String COMPONENT_ANT = "ant";
	public static final String COMPONENT_DEBUG = "debug";
	public static final String COMPONENT_CDT_DEBUG = "cdt-debug";
	public static final String COMPONENT_TEAM = "team";
	public static final String COMPONENT_COMPARE = "compare";
	public static final String COMPONENT_CDT_PARSER = "cdt-parser";
	public static final String COMPONENT_TEXT = "text";
	public static final String COMPONENT_CDT_BUILD = "cdt-build";
	public static final String COMPONENT_UPDATE = "update";
	public static final String COMPONENT_BUILD = "build";
	public static final String COMPONENT_USER_ASSITANCE = "user assistance";
	public static final String COMPONENT_CSV = "cvs";
	public static final String COMPONENT_RELENG = "releng";
	public static final String COMPONENT_RESOURCES = "resources";
	public static final String COMPONENT_DEPRECATED7 = "deprecated7";
	// Index of the component in feature
	public static final int IDX_COMPONENT_DOC = 21;
	public static final int IDX_COMPONENT_SEARCH = 22;
	public static final int IDX_COMPONENT_SWT = 23;
	public static final int IDX_COMPONENT_IDE = 24;
	public static final int IDX_COMPONENT_RUNTIME = 25;
	public static final int IDX_COMPONENT_CORE = 26;
	public static final int IDX_COMPONENT_UI = 27;
	public static final int IDX_COMPONENT_CDT_CORE = 28;
	public static final int IDX_COMPONENT_ANT = 29;
	public static final int IDX_COMPONENT_DEBUG = 30;
	public static final int IDX_COMPONENT_CDT_DEBUG = 31;
	public static final int IDX_COMPONENT_TEAM = 32;
	public static final int IDX_COMPONENT_COMPARE = 33;
	public static final int IDX_COMPONENT_CDT_PARSER = 34;
	public static final int IDX_COMPONENT_TEXT = 35;
	public static final int IDX_COMPONENT_CDT_BUILD = 36;
	public static final int IDX_COMPONENT_UPDATE = 37;
	public static final int IDX_COMPONENT_BUILD = 38;
	public static final int IDX_COMPONENT_USER_ASSITANCE = 39;
	public static final int IDX_COMPONENT_CSV = 40;
	public static final int IDX_COMPONENT_RELENG = 41;
	public static final int IDX_COMPONENT_RESOURCES = 42;
	public static final int IDX_COMPONENT_DEPRECATED7 = 43;

	// Eclipse versions
	public static final String VERSION = "2";
	// Version Index
	public static final int IDX_VERSION = 44;

	// resolution time
	public static final String RESOLUTION_TIME = "resolution-time";
	public static final int IDX_RESOLUTION_TIME = 45;

	// Bug status
	public static final String STATUS_FIXED = "fixed";
	public static final String STATUS_REOPENED = "reopened";
	// re-opened index
	public static final int IDX_STATUS_REOPENED = 46;

	static {
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_PLATFORM, COMPONENT_PLATFORM);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_JDT, COMPONENT_JDT);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_CDT, COMPONENT_CDT);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_PDE, COMPONENT_PDE);
		INDEX_FEATURE_MAPPING.put(IDX_SEVERITY_BLOCKER, SEVERITY_BLOCKER);
		INDEX_FEATURE_MAPPING.put(IDX_SEVERITY_CRITICAL, SEVERITY_CRITICAL);
		INDEX_FEATURE_MAPPING.put(IDX_SEVERITY_MAJOR, SEVERITY_MAJOR);
		INDEX_FEATURE_MAPPING.put(IDX_SEVERITY_NORMAL, SEVERITY_NORMAL);
		INDEX_FEATURE_MAPPING.put(IDX_SEVERITY_MINOR, SEVERITY_MINOR);
		INDEX_FEATURE_MAPPING.put(IDX_SEVERITY_TRIVIAL, SEVERITY_TRIVIAL);
		INDEX_FEATURE_MAPPING.put(IDX_OPERATING_SYSTEMS_ALL, OPERATING_SYSTEMS_ALL);
		INDEX_FEATURE_MAPPING.put(IDX_OPERATING_SYSTEMS_OTHERS, OPERATING_SYSTEMS_OTHERS);
		INDEX_FEATURE_MAPPING.put(IDX_OPERATING_SYSTEMS_MAC, OPERATING_SYSTEMS_MAC);
		INDEX_FEATURE_MAPPING.put(IDX_OPERATING_SYSTEMS_WINDOWS, OPERATING_SYSTEMS_WINDOWS);
		INDEX_FEATURE_MAPPING.put(IDX_OPERATING_SYSTEMS_LINUX, OPERATING_SYSTEMS_LINUX);
		INDEX_FEATURE_MAPPING.put(IDX_PRIORITY_1, PRIORITY_1);
		INDEX_FEATURE_MAPPING.put(IDX_PRIORITY_2, PRIORITY_2);
		INDEX_FEATURE_MAPPING.put(IDX_PRIORITY_3, PRIORITY_3);
		INDEX_FEATURE_MAPPING.put(IDX_PRIORITY_4, PRIORITY_4);
		INDEX_FEATURE_MAPPING.put(IDX_PRIORITY_5, PRIORITY_5);
		INDEX_FEATURE_MAPPING.put(IDX_PRIORITY_NONE, PRIORITY_NONE);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_DOC, COMPONENT_DOC);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_SEARCH, COMPONENT_SEARCH);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_SWT, COMPONENT_SWT);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_IDE, COMPONENT_IDE);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_RUNTIME, COMPONENT_RUNTIME);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_CORE, COMPONENT_CORE);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_UI, COMPONENT_UI);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_CDT_CORE, COMPONENT_CDT_CORE);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_ANT, COMPONENT_ANT);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_DEBUG, COMPONENT_DEBUG);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_CDT_DEBUG, COMPONENT_CDT_DEBUG);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_TEAM, COMPONENT_TEAM);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_COMPARE, COMPONENT_COMPARE);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_CDT_PARSER, COMPONENT_CDT_PARSER);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_TEXT, COMPONENT_TEXT);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_CDT_BUILD, COMPONENT_CDT_BUILD);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_UPDATE, COMPONENT_UPDATE);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_BUILD, COMPONENT_BUILD);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_USER_ASSITANCE, COMPONENT_USER_ASSITANCE);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_CSV, COMPONENT_CSV);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_RELENG, COMPONENT_RELENG);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_RESOURCES, COMPONENT_RESOURCES);
		INDEX_FEATURE_MAPPING.put(IDX_COMPONENT_DEPRECATED7, COMPONENT_DEPRECATED7);
	}

	private BugFeatures() {
	}
}
