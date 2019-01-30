package nts.uk.shr.infra.web.component;

import java.io.IOException;
import java.util.Arrays;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;

import nts.arc.system.ServerSystemProperties;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;
import nts.uk.shr.com.context.loginuser.SelectedLanguage;
import nts.uk.shr.com.context.loginuser.role.LoginUserRoles;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.program.ProgramsManager;
import nts.uk.shr.com.program.WebAppId;
import nts.uk.shr.infra.i18n.resource.web.webapi.I18NResourcesWebService;
import nts.uk.shr.infra.web.component.env.ViewContextEnvWriter;

@FacesComponent(tagName = "viewcontext", createTag = true)
public class ViewContext extends UIComponentBase {

	@Override
	public String getFamily() {
		return this.getClass().getName();
	}

	/**
	 * Render beginning of component
	 * 
	 * @param context
	 *            FacesContext
	 * @throws IOException
	 *             IOException
	 */
	@Override
	public void encodeBegin(FacesContext context) throws IOException {

		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest(); 
		String requestedPath = request.getServletPath();
		String queryString = request.getQueryString();
		String applicationContextPath = context.getExternalContext().getApplicationContextPath();
		
		ResponseWriter rw = context.getResponseWriter();
		rw.write("<script type=\"text/javascript\">window.__viewContext = {");

		writeRootPath(requestedPath, rw);
		rw.write(",");

		writeProgramInfo(requestedPath, queryString, rw, applicationContextPath);
		rw.write(",");
		writeLoginPersonInfo(rw);
		rw.write(",");
		
		rw.write("};");
		rw.write("__viewContext.primitiveValueConstraints = __viewContext.primitiveValueConstraints || {};");

		CDI.current().select(ViewContextEnvWriter.class).get().write(rw);
		rw.write("</script>");
		
		rw.write(I18NResourcesWebService.getHtmlToLoadResources());

	}
	
	private void writeProgramInfo (String requestedPath, String queryString, ResponseWriter rw, String applicationContextPath) throws IOException {
		WebAppId webApi = Arrays.asList(WebAppId.values()).stream()
				.filter(w -> applicationContextPath.indexOf(w.name) >= 0).findFirst().orElse(WebAppId.COM);
		
		StringBuilder builder = new StringBuilder();
		ProgramsManager.find(webApi, requestedPath).ifPresent(pr -> {
			builder.append("webapi: '" + pr.getAppId().name + "', ");
			builder.append("programId: '" + pr.getPId() + "', ");
			String programName = TextResource.localize(pr.getPName());
			builder.append("programName: '" + programName + "', ");
			builder.append("path: '" + pr.getPPath() + "'");
			if (queryString != null) {
				builder.append(", queryString: '" + queryString + "'");
			}
		});
		
		if(builder.length() > 0){
			builder.append(", ");
		}
		
		builder.append("isDebugMode: " + ServerSystemProperties.isDebugMode());
		
		rw.write("program: {" + builder.toString() + "}");
	}
	
	private void writeLoginPersonInfo (ResponseWriter rw) throws IOException {
		LoginUserContext userInfo = AppContexts.user();
		StringBuilder builder = new StringBuilder();
//		if(userInfo.hasLoggedIn()){
			builder.append("contractCode: '" + userInfo.contractCode() + "', ");
			builder.append("companyId: '" + userInfo.companyId() + "', ");
			builder.append("companyCode: '" + userInfo.companyCode() + "', ");
			builder.append("isEmployee: '" + userInfo.isEmployee() + "', ");
			builder.append("employeeId: '" + userInfo.employeeId() + "', ");
			builder.append("employeeCode: '" + userInfo.employeeCode() + "', ");
			writeSelectedLanguage(userInfo.language(), builder);
			writeRole(userInfo.roles(), builder);
//		}
		
		rw.write("user: {" + builder.toString() + "}");
	}
	
	private void writeSelectedLanguage (SelectedLanguage language, StringBuilder builder) {
		builder.append("selectedLanguage: { ");
		if(language != null){
			builder.append("basicLanguageId: '" + language.basicLanguageId() + "', ");
			builder.append("personNameLanguageId: '" + language.personNameLanguageId() + "'");
		}
		builder.append(" }, ");
	}
	
	private void writeRole (LoginUserRoles role, StringBuilder builder) {
		builder.append("role: { ");
		if(role != null){
			builder.append("attendance: '" + role.forAttendance() + "', ");
			builder.append("companyAdmin: '" + role.forCompanyAdmin() + "', ");
			builder.append("groupCompanyAdmin: '" + role.forGroupCompaniesAdmin() + "', ");
			builder.append("officeHelper: '" + role.forOfficeHelper() + "', ");
			builder.append("payroll: '" + role.forPayroll() + "', ");
			builder.append("personalInfo: '" + role.forPersonalInfo() + "', ");
			builder.append("personnel: '" + role.forPersonnel() + "', ");
			builder.append("systemAdmin: '" + role.forSystemAdmin() + "'");
		}
		builder.append(" }");
	}

	private static void writeRootPath(String requestedPath, ResponseWriter rw) throws IOException {
		// convert "/hoge/fuga/piyo.xhtml" -> "../../"
//		String requestedPath = ((HttpServletRequest) context.getExternalContext().getRequest()).getServletPath();
		String rootPath = requestedPath.replaceAll("[^/]", "").substring(1).replaceAll("/", "../");

		rw.write("rootPath: '" + rootPath + "'");
	}
}
