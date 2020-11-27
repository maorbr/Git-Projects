package gcov.engine;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gcov.utils.FileTolListConvertor;
import gcov.utils.RegexTool;

public class GcovCoverageMerger {
	private static final String LEFT_SIDE_GCOV_REGEX = "^[^:]+";
	private static final String RIGHT_SIDE_GCOV_REGEX = ":+?.*";
	private static final String TIME_STAMP_GCOV_REGEX = "[$$$$$$$].+";
	
	public static double CoverageArrayCalculator(List<String> list) throws IOException {

		int allLineCounter = 0;
		int notZeroLineCounter = 0;

		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).contains("$$$$$$$")) {
				String value = RegexTool.getValueByRegex(LEFT_SIDE_GCOV_REGEX, list.get(i)).trim();
				if (!value.equals("-")) {
					allLineCounter++;

					if (!value.equals("00000000")) {
						notZeroLineCounter++;
					}
				}
			}
		}

		return allLineCounter != 0 ? (double) notZeroLineCounter / allLineCounter : 0;
	}
	
	public static double CoverageCalculator(String filePath) throws IOException {

		List<String> list = FileTolListConvertor.convetFileToList(filePath);
		int allLineCounter = 0;
		int notZeroLineCounter = 0;

		for (int i = 0; i < list.size(); i++) {
			if (!list.get(i).contains("$$$$$$$")) {
				String value = RegexTool.getValueByRegex(LEFT_SIDE_GCOV_REGEX, list.get(i)).trim();
				if (!value.equals("-")) {
					allLineCounter++;

					if (!value.equals("00000000")) {
						notZeroLineCounter++;
					}
				}
			}
		}

		return allLineCounter != 0 ? (double) notZeroLineCounter / allLineCounter : 0;
	}

	public static boolean GcovFileMerger(String gcovMergePath, String gcovLastPath, List<File> gcdaFilesList, String projectVerison, boolean isHybridMode) throws IOException {
	
		boolean mergeFlagValidity = true;
		
		for (File gcnaFile : gcdaFilesList) {
			if (gcnaFile.getName() != null) {
				File file = new File(gcovMergePath + "\\" + GcovDirectoryTool.gcdaFileRename(gcnaFile.getName()) + "_" + projectVerison +".gcov");
				if (file.exists()) /*Merge folder existed*/{
					List<String> gcovMergeList = FileTolListConvertor.convetFileToList(gcovMergePath + "\\" + GcovDirectoryTool.gcdaFileRename(gcnaFile.getName()) + "_" + projectVerison +".gcov");
					List<String> gcovLastList = FileTolListConvertor.convetFileToList(gcovLastPath + "\\" + GcovDirectoryTool.gcdaFileRenameToGcov(gcnaFile.getName()) + ".gcov");
					List<String> gcovFinal = new ArrayList<>();
					List<String> gcovTimeStampList = new ArrayList<>();
					for (int i = 0; i < gcovMergeList.size(); i++) {
						if (gcovMergeList.get(i).matches(TIME_STAMP_GCOV_REGEX)) {
							gcovTimeStampList.add(gcovMergeList.get(i));		
						}
					}
					
					int size = gcovMergeList.size() - 1;
					
					for (int i = 0; i < gcovTimeStampList.size(); i++) {
						gcovMergeList.remove(size - i);
					}

					if (gcovLastList.size() == gcovMergeList.size()) {
						for (int i = 0; i < gcovLastList.size(); i++) {

							String gcovLastLeft = RegexTool.getValueByRegex(LEFT_SIDE_GCOV_REGEX, gcovLastList.get(i)).trim();
							String gcovLastRight = RegexTool.getValueByRegex(RIGHT_SIDE_GCOV_REGEX, gcovLastList.get(i));
							String gcovMergeLeft = RegexTool.getValueByRegex(LEFT_SIDE_GCOV_REGEX, gcovMergeList.get(i)).trim();
							String gcovMergeRight = RegexTool.getValueByRegex(RIGHT_SIDE_GCOV_REGEX, gcovMergeList.get(i));

							if(gcovLastRight.contains("int main(void)") && !gcovLastLeft.equals("00000001")) {
								mergeFlagValidity = false;
								break;
							}
							
							if (gcovLastRight.equals(gcovMergeRight) || i <= 2) {
								if (gcovLastLeft.equals("-") || gcovMergeLeft.equals("-")) {
									if(gcovLastLeft.equals(gcovMergeLeft)) {
										gcovFinal.add(gcovLastList.get(i));
									}else {
										if (isHybridMode == false) {
											mergeFlagValidity = false;
											break;
										}else {/* fix in version 1.8 - check if in the last_list or in merge_list there is "-" sign.*/
											if(gcovLastLeft.equals("-")) {
												gcovFinal.add(gcovMergeList.get(i));	
											}	
											if(gcovMergeLeft.equals("-")) {
												gcovFinal.add(gcovLastList.get(i));	
											}	
										}
									}
								} else {
									gcovFinal.add(" " + sumHexNumber(gcovLastLeft,gcovMergeLeft) + " " + gcovMergeRight);
								}
							} else {
								mergeFlagValidity = false;
								break;
							}
						}
					}else {
						mergeFlagValidity = false;
						break;
					}
					
					if(mergeFlagValidity == true) {
						FileTolListConvertor.cleanFile(gcovMergePath + "\\" + GcovDirectoryTool.gcdaFileRename(gcnaFile.getName()) + "_" + projectVerison +".gcov");
						
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
						int averageCoverage = (int) (100 * CoverageArrayCalculator(gcovFinal));
						gcovTimeStampList.add("$$$$$$$$	[ " + gcnaFile.getName() + " was merged at " + LocalDate.now() +" " + timeOnly.format(cal.getTime()) + " ; Coverage: " + averageCoverage + "%"+ " ]	$$$$$$$$");
						
						for (String line : gcovTimeStampList) {
							gcovFinal.add(line);
						}
						
						FileTolListConvertor.printListToFile(gcovMergePath + "\\" + GcovDirectoryTool.gcdaFileRename(gcnaFile.getName()) + "_" + projectVerison +".gcov", gcovFinal);
						gcovFinal.clear();	
					}

				} else /*Merge folder not existed*/{
					GcovDirectoryTool.createNewGcovMergerDirectoryIfNotExisted(gcovMergePath);
					List<String> gcovLastList = FileTolListConvertor.convetFileToList(gcovLastPath + "\\" + GcovDirectoryTool.gcdaFileRenameToGcov(gcnaFile.getName()) + ".gcov");
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat timeOnly = new SimpleDateFormat("HH:mm:ss");
					int averageCoverage = (int) (100 * GcovCoverageMerger.CoverageCalculator(gcovLastPath + "\\" + GcovDirectoryTool.gcdaFileRenameToGcov(gcnaFile.getName()) + ".gcov"));
					gcovLastList.add("$$$$$$$$	[ " + gcnaFile.getName() + " was merged at " + LocalDate.now() +" " + timeOnly.format(cal.getTime()) + " ; Coverage: " + averageCoverage + "%"+ " ]	$$$$$$$$");
					FileTolListConvertor.printListToFile(gcovMergePath + "\\" + GcovDirectoryTool.gcdaFileRename(gcnaFile.getName()) + "_" + projectVerison + ".gcov", gcovLastList);
				}
			}

		}
		return mergeFlagValidity;
	}
	
	private static String sumHexNumber(String leftValueHex, String rightValueHex) {
		Long leftValue = Long.parseLong(leftValueHex, 16);
		Long rightValue = Long.parseLong(rightValueHex, 16);
		String Sum = Long.toHexString(leftValue + rightValue);
		StringBuilder zeroCharList = new StringBuilder();
		
		if (Sum.length() < 9) {
			for (int i = 0; i < 8 - Sum.length(); i++) {
				zeroCharList.append("0");
			}
		}else {
			zeroCharList.append("");
		}

		return zeroCharList.toString() + Sum;	
	}
	
}
