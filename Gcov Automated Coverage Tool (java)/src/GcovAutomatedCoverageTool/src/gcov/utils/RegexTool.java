package gcov.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTool {
	public static String getValueByRegex(String regex, String text) {
		String value = "";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			value = matcher.group();
		}
		return value;
	}
}
