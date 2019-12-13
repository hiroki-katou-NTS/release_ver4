package nts.uk.shr.infra.web.component.filetag;

import java.io.File;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.logging.log4j.util.Strings;

import nts.arc.time.GeneralDateTime;

final class FileTagsHelper {
	
//	public static String VERSION = GeneralDateTime.now().toString("yyyyMMddHHmmss");

	static String buildPath(FacesContext context, String specifiedPath) {

		String filePath;

		if (specifiedPath.charAt(0) == '/') {
			String appPath = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();
			filePath = appPath + specifiedPath;
		} else {
			filePath = specifiedPath;
		}

		return appendVersionString(context, filePath);
	}
	
	public static String getVersion(FacesContext context) {

		File file = new File(((ServletContext) context).getRealPath("/view/common/mark.js"));

		if (!file.exists()) {
			return "xxxxxxxxxxxxxx";
		}

		return GeneralDateTime.legacyDateTime(new Date(file.lastModified())).toString("yyyyMMddHHmmss");
	}

	private static String appendVersionString(FacesContext context, String filePath) {

		return filePath + "?v=" + getVersion(context);
	}

	static String buildPathUsingComWeb(FacesContext context, String specifiedPath) {

		String filePath;

		if (specifiedPath.charAt(0) == '/') {
			String appPath = "/nts.uk.com.js.web";
			filePath = appPath + specifiedPath;
		} else {
			filePath = specifiedPath;
		}

		return appendVersionString(context, filePath);
	}
	static String buildPathOf(FacesContext context, String appPath ,String specifiedPath) {

		String filePath;

		if (specifiedPath.charAt(0) == '/'&& !Strings.isEmpty(appPath)) {
			appPath =appPath.charAt(0)=='/'?appPath:"/"+appPath;
			filePath = appPath + specifiedPath;
		} else {
			filePath = specifiedPath;
		}

		return appendVersionString(context, filePath);
	}
}
