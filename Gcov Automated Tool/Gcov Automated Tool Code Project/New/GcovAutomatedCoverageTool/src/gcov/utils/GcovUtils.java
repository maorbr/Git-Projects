package gcov.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GcovUtils {
	private static final String LEFT_SIDE_GCOV_REGEX = "^[^:]+";
	private static final String RIGHT_SIDE_GCOV_REGEX = ":+?.*";

	public static double CoverageCalculator(String filePath) throws IOException {

		List<String> list = FileTolistConvertor.convetFileToList(filePath);
		int allLineCounter = 0;
		int notZeroLineCounter = 0;

		for (int i = 0; i < list.size(); i++) {
			String value = RegexTool.getValueByRegex(LEFT_SIDE_GCOV_REGEX, list.get(i)).trim();
			if (!value.equals("-")) {
				allLineCounter++;

				if (!value.equals("00000000")) {
					notZeroLineCounter++;
				}
			}
		}

		return allLineCounter != 0 ? (double) notZeroLineCounter / allLineCounter : 0;
	}

	public static void GcovFileMerger(String gcovMergePath, String gcovLastPath, List<String> gcdaFilesList)
			throws IOException {

		for (String fileName : gcdaFilesList) {
			if (fileName != null) {
				File file = new File(gcovMergePath + "\\" + fileName + ".gcov");
				if (file.exists()) {
					List<String> gcovMergeList = FileTolistConvertor
							.convetFileToList(gcovMergePath + "\\" + fileName + ".gcov");

					List<String> gcovLastList = FileTolistConvertor
							.convetFileToList(gcovLastPath + "\\" + fileName + ".gcov");
					List<String> gcovFinal = new ArrayList<>();

					if (gcovLastList.size() == gcovMergeList.size()) {
						for (int i = 0; i < gcovLastList.size(); i++) {

							String gcovLastLeft = RegexTool.getValueByRegex(LEFT_SIDE_GCOV_REGEX, gcovLastList.get(i))
									.trim();
							String gcovLastRight = RegexTool.getValueByRegex(RIGHT_SIDE_GCOV_REGEX,
									gcovLastList.get(i));
							String gcovMergeLeft = RegexTool.getValueByRegex(LEFT_SIDE_GCOV_REGEX, gcovMergeList.get(i))
									.trim();
							String gcovMergeRight = RegexTool.getValueByRegex(RIGHT_SIDE_GCOV_REGEX,
									gcovMergeList.get(i));

							if (!gcovLastLeft.equals(gcovMergeLeft)) {
								if (gcovLastLeft.equals("-")) {
									gcovFinal.add(gcovMergeList.get(i));
								} else if (gcovMergeLeft.equals("-")) {
									gcovFinal.add(gcovLastList.get(i));
								} else {
									gcovFinal.add(gcovLastList.get(i));
								}
							} else if (!gcovLastRight.equals(gcovMergeRight)) {
								if (gcovLastRight.matches(".*//.*")) {
									gcovFinal.add(gcovMergeList.get(i));
								} else if (gcovMergeRight.matches(".*//.*")) {
									gcovFinal.add(gcovLastList.get(i));
								} else {
									gcovFinal.add(gcovLastList.get(i));
								}
							} else {
								gcovFinal.add(gcovLastList.get(i));
							}
						}
					}

					FileTolistConvertor.cleanFile(gcovMergePath + "\\" + fileName + ".gcov");
					FileTolistConvertor.printListToFile(gcovMergePath + "\\" + fileName + ".gcov", gcovFinal);
					gcovFinal.clear();
				} else {
					DirectoryUtils.CopyFileToDirectory(gcovLastPath + "\\" + fileName + ".gcov", gcovMergePath + "\\" + fileName + ".gcov");
				}
			}

		}
	}

}
