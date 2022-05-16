package common.utils;

public class MessageUtil {
	public static String getMessage(String detailCode) {
		return "error";
	}
	public static boolean isError(String detailCode) {
		return !"000000".equals(detailCode);
	}
}
