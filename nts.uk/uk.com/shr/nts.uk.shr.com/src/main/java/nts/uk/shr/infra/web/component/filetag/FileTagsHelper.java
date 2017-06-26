package nts.uk.shr.infra.web.component.filetag;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;

final class FileTagsHelper {

	static String buildPath(FacesContext context, String specifiedPath) {

		String filePath;

		if (specifiedPath.charAt(0) == '/') {
			String appPath = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();
			filePath = appPath + specifiedPath;
		} else {
			filePath = specifiedPath;
		}

		return appendVersionString(filePath);
	}

	private static String appendVersionString(String filePath) {

		return filePath + "?";
	}

	static String buildPathUsingComWeb(FacesContext context, String specifiedPath) {

		String filePath;

		if (specifiedPath.charAt(0) == '/') {
			String appPath = "/nts.uk.com.js.web";
			filePath = appPath + specifiedPath;
		} else {
			filePath = specifiedPath;
		}

		return appendVersionString(filePath);
	}
	static String buildPathOf(String appPath ,String specifiedPath) {

		String filePath;

		if (specifiedPath.charAt(0) == '/'&& !Strings.isEmpty(appPath)) {
			appPath =appPath.charAt(0)=='/'?appPath:"/"+appPath;
			filePath = appPath + specifiedPath;
		} else {
			filePath = specifiedPath;
		}

		return appendVersionString(filePath);
	}
}
