package edu.neu.datamining.project.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.opencsv.CSVParser;

import edu.neu.datamining.project.conf.BugFeatures;
import edu.neu.datamining.project.conf.DataConfiguration;
import edu.neu.datamining.project.data.BugInfo;
import edu.neu.datamining.project.data.DeveloperInfo;

public class FileUtils {

	private static final Set<String> GROUP_ALIAS_IDS = new HashSet<>();
	private static final Map<Long, Set<DeveloperInfo>> BUG_DEVELOPER_MAP = new HashMap<>();
	private static final Map<String, DeveloperInfo> ALL_DEVELOPERS = new HashMap<>();

	static {
		storeGroupAlias(DataConfiguration.GROUP_ALIAS_FILE_PATH);
		readBugDevMapping(DataConfiguration.BUG_DEV_MAP_FILE_PATH);
	}

	private static void storeGroupAlias(String filePath) {

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));

			String id = null;

			while ((id = reader.readLine()) != null) {
				if (!id.isEmpty())
					GROUP_ALIAS_IDS.add(id.trim().toLowerCase());
			}

		} catch (FileNotFoundException e) {
			// log exception. move ahead
		} catch (IOException e) {
			// log exception. move ahead
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					// Ignore
				}
			}
		}
	}

	private static void readBugDevMapping(String filePath) {

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(filePath));
			// Ignore header info
			String line = reader.readLine();

			CSVParser parser = new CSVParser();

			while ((line = reader.readLine()) != null) {
				if (line.isEmpty())
					continue;

				String[] strings = parser.parseLine(line.toLowerCase());
				addBugDeveloperEntry(strings);
			}
		} catch (IOException e) {
			// Log fatal exception
		} finally {
			if (null != reader)
				try {
					reader.close();
				} catch (IOException e) {
					// Ignore if exception occurs while closing
				}
		}
	}

	private static void addBugDeveloperEntry(String[] strings) {
		try {
			long bugID = Long.parseLong(strings[0]);

			Set<DeveloperInfo> developers = new HashSet<>();
			BUG_DEVELOPER_MAP.put(bugID, developers);

			for (int i = 1; i < strings.length; i++) {
				String id = strings[i];

				DeveloperInfo dev = ALL_DEVELOPERS.get(id);

				if (null == dev) {
					// add only developers. Ignore group-alias
					if (id.isEmpty() || isGroupAlias(id))
						continue;

					dev = new DeveloperInfo(id, new double[BugFeatures.NUM_ATTRIBUTES + 1]);
					ALL_DEVELOPERS.put(id, dev);
				}

				developers.add(dev);
			}

		} catch (NumberFormatException e) {
			// Ignore. Invalid bug-id
		}
	}

	private static boolean isGroupAlias(String id) {

		for (String alias : GROUP_ALIAS_IDS) {
			if (id.startsWith(alias) || id.endsWith(alias))
				// it is a group -alias
				return true;
		}

		// not a group-alias
		return false;
	}

	public static List<DeveloperInfo> getDevelopersInfo() {
		return new ArrayList<>(ALL_DEVELOPERS.values());
	}

	public static List<BugInfo> readBugsInfo(String filePath) throws FileNotFoundException, IOException {

		BufferedReader reader = new BufferedReader(new FileReader(filePath));

		List<BugInfo> list = new ArrayList<>();

		// ignore file line : contains header
		String line = reader.readLine();

		ObjectMapper jsonParser = new ObjectMapper();

		TypeReference<LinkedHashMap<String, String>> typeRef = new TypeReference<LinkedHashMap<String, String>>() {
		};

		while ((line = reader.readLine()) != null) {
			try {

				Map<String, String> map = jsonParser.readValue(line, typeRef);

				// ignore all non-fixed bugs
				if (Double.parseDouble(map.get(BugFeatures.STATUS_FIXED)) != 1.0)
					continue;

				long bugID = Long.parseLong(map.get(BugFeatures.BUG_ID));

				Set<DeveloperInfo> developers = BUG_DEVELOPER_MAP.get(bugID);

				// consider bugs which have at least one developer associated
				if (null == developers || developers.isEmpty())
					continue;

				// ignore bug-id and description
				double[] x = new double[BugFeatures.NUM_ATTRIBUTES];

				addPlatformDetails(map, x);

				// Severity of the bug
				addSeverityDetails(map, x);

				// Operating systems impacted by the bug
				addOperatingSystemDetails(map, x);

				// priority of the bug
				addBugPriority(map, x);

				// bug component such as UI, runtime etc.
				addComponent(map, x);

				for (DeveloperInfo dev : developers) {
					double[] features = dev.getFeatures();

					for (int i = 0; i < x.length; i++) {
						features[i] += x[i];
					}

					// bugs resolved by this developer is incremented each time
					features[features.length - 1] += 1;
				}

				list.add(new BugInfo(bugID, x, developers));

			} catch (NumberFormatException e) {
				// Ignore the record
			} catch (IOException e) {
				// Ignore the record
			}
		}

		reader.close();
		System.out.println("Total : " + list.size());
		return list;
	}

	private static void addComponent(Map<String, String> map, double[] x) {
		x[BugFeatures.IDX_COMPONENT_DEPRECATED7] = Double.parseDouble(map.get(BugFeatures.COMPONENT_DEPRECATED7));
		x[BugFeatures.IDX_COMPONENT_DOC] = Double.parseDouble(map.get(BugFeatures.COMPONENT_DOC));
		x[BugFeatures.IDX_COMPONENT_SEARCH] = Double.parseDouble(map.get(BugFeatures.COMPONENT_SEARCH));
		x[BugFeatures.IDX_COMPONENT_SWT] = Double.parseDouble(map.get(BugFeatures.COMPONENT_SWT));
		x[BugFeatures.IDX_COMPONENT_IDE] = Double.parseDouble(map.get(BugFeatures.COMPONENT_IDE));
		x[BugFeatures.IDX_COMPONENT_RUNTIME] = Double.parseDouble(map.get(BugFeatures.COMPONENT_RUNTIME));
		x[BugFeatures.IDX_COMPONENT_CORE] = Double.parseDouble(map.get(BugFeatures.COMPONENT_CORE));
		x[BugFeatures.IDX_COMPONENT_UI] = Double.parseDouble(map.get(BugFeatures.COMPONENT_UI));
		x[BugFeatures.IDX_COMPONENT_CDT_CORE] = Double.parseDouble(map.get(BugFeatures.COMPONENT_CDT_CORE));
		x[BugFeatures.IDX_COMPONENT_ANT] = Double.parseDouble(map.get(BugFeatures.COMPONENT_ANT));
		x[BugFeatures.IDX_COMPONENT_DEBUG] = Double.parseDouble(map.get(BugFeatures.COMPONENT_DEBUG));
		x[BugFeatures.IDX_COMPONENT_CDT_DEBUG] = Double.parseDouble(map.get(BugFeatures.COMPONENT_CDT_DEBUG));
		x[BugFeatures.IDX_COMPONENT_TEAM] = Double.parseDouble(map.get(BugFeatures.COMPONENT_TEAM));
		x[BugFeatures.IDX_COMPONENT_COMPARE] = Double.parseDouble(map.get(BugFeatures.COMPONENT_COMPARE));
		x[BugFeatures.IDX_COMPONENT_CDT_PARSER] = Double.parseDouble(map.get(BugFeatures.COMPONENT_CDT_PARSER));
		x[BugFeatures.IDX_COMPONENT_TEXT] = Double.parseDouble(map.get(BugFeatures.COMPONENT_TEXT));
		x[BugFeatures.IDX_COMPONENT_CDT_BUILD] = Double.parseDouble(map.get(BugFeatures.COMPONENT_CDT_BUILD));
		x[BugFeatures.IDX_COMPONENT_UPDATE] = Double.parseDouble(map.get(BugFeatures.COMPONENT_UPDATE));
		x[BugFeatures.IDX_COMPONENT_BUILD] = Double.parseDouble(map.get(BugFeatures.COMPONENT_BUILD));
		x[BugFeatures.IDX_COMPONENT_USER_ASSITANCE] = Double.parseDouble(map.get(BugFeatures.COMPONENT_USER_ASSITANCE));
		x[BugFeatures.IDX_COMPONENT_CSV] = Double.parseDouble(map.get(BugFeatures.COMPONENT_CSV));
		x[BugFeatures.IDX_COMPONENT_RELENG] = Double.parseDouble(map.get(BugFeatures.COMPONENT_RELENG));
		x[BugFeatures.IDX_COMPONENT_RESOURCES] = Double.parseDouble(map.get(BugFeatures.COMPONENT_RESOURCES));
	}

	private static void addBugPriority(Map<String, String> map, double[] x) {
		x[BugFeatures.IDX_PRIORITY_1] = Double.parseDouble(map.get(BugFeatures.PRIORITY_1));
		x[BugFeatures.IDX_PRIORITY_2] = Double.parseDouble(map.get(BugFeatures.PRIORITY_2));
		x[BugFeatures.IDX_PRIORITY_3] = Double.parseDouble(map.get(BugFeatures.PRIORITY_3));
		x[BugFeatures.IDX_PRIORITY_4] = Double.parseDouble(map.get(BugFeatures.PRIORITY_4));
		x[BugFeatures.IDX_PRIORITY_5] = Double.parseDouble(map.get(BugFeatures.PRIORITY_5));
		x[BugFeatures.IDX_PRIORITY_NONE] = Double.parseDouble(map.get(BugFeatures.PRIORITY_NONE));
	}

	private static void addOperatingSystemDetails(Map<String, String> map, double[] x) {
		x[BugFeatures.IDX_OPERATING_SYSTEMS_ALL] = Double.parseDouble(map.get(BugFeatures.OPERATING_SYSTEMS_ALL));
		x[BugFeatures.IDX_OPERATING_SYSTEMS_LINUX] = Double.parseDouble(map.get(BugFeatures.OPERATING_SYSTEMS_LINUX));
		x[BugFeatures.IDX_OPERATING_SYSTEMS_WINDOWS] = Double
				.parseDouble(map.get(BugFeatures.OPERATING_SYSTEMS_WINDOWS));
		x[BugFeatures.IDX_OPERATING_SYSTEMS_MAC] = Double.parseDouble(map.get(BugFeatures.OPERATING_SYSTEMS_MAC));
		x[BugFeatures.IDX_OPERATING_SYSTEMS_OTHERS] = Double.parseDouble(map.get(BugFeatures.OPERATING_SYSTEMS_OTHERS));
	}

	private static void addSeverityDetails(Map<String, String> map, double[] x) {

		x[BugFeatures.IDX_SEVERITY_BLOCKER] = Double.parseDouble(map.get(BugFeatures.SEVERITY_BLOCKER));
		x[BugFeatures.IDX_SEVERITY_CRITICAL] = Double.parseDouble(map.get(BugFeatures.SEVERITY_CRITICAL));
		x[BugFeatures.IDX_SEVERITY_MAJOR] = Double.parseDouble(map.get(BugFeatures.SEVERITY_MAJOR));
		x[BugFeatures.IDX_SEVERITY_NORMAL] = Double.parseDouble(map.get(BugFeatures.SEVERITY_NORMAL));
		x[BugFeatures.IDX_SEVERITY_MINOR] = Double.parseDouble(map.get(BugFeatures.SEVERITY_MINOR));
		x[BugFeatures.IDX_SEVERITY_TRIVIAL] = Double.parseDouble(map.get(BugFeatures.SEVERITY_TRIVIAL));
	}

	private static void addPlatformDetails(Map<String, String> map, double[] x) {
		// Platform for which the bug occurred
		x[BugFeatures.IDX_COMPONENT_PLATFORM] = Double.parseDouble(map.get(BugFeatures.COMPONENT_PLATFORM));
		x[BugFeatures.IDX_COMPONENT_JDT] = Double.parseDouble(map.get(BugFeatures.COMPONENT_JDT));
		x[BugFeatures.IDX_COMPONENT_CDT] = Double.parseDouble(map.get(BugFeatures.COMPONENT_CDT));
		x[BugFeatures.IDX_COMPONENT_PDE] = Double.parseDouble(map.get(BugFeatures.COMPONENT_PDE));
	}

	public static Map<Long, BugInfo> readBugsInfoMap(String filePath) throws FileNotFoundException, IOException {

		List<BugInfo> bugInfos = readBugsInfo(filePath);
		Map<Long, BugInfo> bugIdToBugInfo = new HashMap<Long, BugInfo>();

		for (int i = 0; i < bugInfos.size(); i++) {
			bugIdToBugInfo.put(bugInfos.get(i).getBugID(), bugInfos.get(i));
		}

		System.out.println("Total : " + bugIdToBugInfo.size());
		return bugIdToBugInfo;
	}

	private FileUtils() {
		// deny object creation for utility class
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		readBugsInfo(DataConfiguration.BUGS_INFO_FILE_PATH_FIXED);
	}
}
