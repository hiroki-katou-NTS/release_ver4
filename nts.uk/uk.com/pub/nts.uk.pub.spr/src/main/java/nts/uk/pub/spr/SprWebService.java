package nts.uk.pub.spr;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import lombok.val;
import nts.uk.pub.spr.SprStubHelper.ApplicationTargetResult;
import nts.uk.pub.spr.SprStubHelper.RecordApplicationStatusResult;
import nts.uk.pub.spr.SprStubHelper.RequestApplicationStatusResult;
import nts.uk.pub.spr.approvalroot.SprApprovalRootService;
import nts.uk.pub.spr.appstatus.SprAppStatusService;
import nts.uk.pub.spr.dailystatus.SprDailyStatusService;
import nts.uk.pub.spr.login.SprLoginFormService;
import nts.uk.pub.spr.login.output.LoginUserContextSpr;
import nts.uk.pub.spr.login.output.RoleInfoSpr;
import nts.uk.pub.spr.login.paramcheck.LoginParamCheck;
import nts.uk.shr.com.context.loginuser.LoginUserContextManager;

@Path("public/spr")
public class SprWebService {
	
	@Inject
	private SprLoginFormService sprLoginFormService;
	
	@Inject
	private SprAppStatusService sprAppStatusService;
	
	@Inject
	private SprDailyStatusService sprDailyStatusService;
	
	@Inject
	private SprApprovalRootService sprApprovalRootService;
	
	@Inject
	private LoginUserContextManager loginUserContextManager;
	
	@Inject
	private LoginParamCheck loginParamCheck;

	@POST
	@Path("01/loginfromspr")
	@Produces("text/html")
	public String loginFromSpr(
			@FormParam("menu") String menuCode,
			@FormParam("loginemployeeCode") String loginEmployeeCode,
			@FormParam("employeeCode") String targetEmployeeCode,
			@FormParam("starttime") String startTime,
			@FormParam("endtime") String endTime,
			@FormParam("date") String targetDate,
			@FormParam("selecttype") String selectType,
			@FormParam("applicationID") String applicationID,
			@FormParam("reason") String reason,
			@FormParam("stampProtection") String stampProtection) {
		String menuCDReal = menuCode;
		String loginEmployeeCDReal = loginEmployeeCode;
		String targetEmployeeCDReal = targetEmployeeCode;
		String startTimeReal = startTime;
		String endTimeReal = endTime;
		String targetDateReal = targetDate;
		String selectTypeReal = selectType;
		String applicationIDReal = applicationID;
		String reasonReal = reason;
		String stampProtectionReal = stampProtection;
		LoginUserContextSpr loginUserContextSpr = sprLoginFormService.loginFromSpr(
				menuCDReal, 
				loginEmployeeCDReal, 
				targetEmployeeCDReal, 
				startTimeReal, 
				endTimeReal, 
				targetDateReal, 
				selectTypeReal, 
				applicationIDReal, 
				reasonReal,
				stampProtectionReal);
		loginUserContextManager.loggedInAsEmployee(
				loginUserContextSpr.getUserID(), 
				loginUserContextSpr.getPersonID(), 
				loginUserContextSpr.getContractCD(), 
				loginUserContextSpr.getCompanyID(), 
				loginUserContextSpr.getCompanyCD(), 
				loginUserContextSpr.getLoginEmployeeID(), 
				loginUserContextSpr.getEmployeeCD());
		for(RoleInfoSpr roleInfor : loginUserContextSpr.getRoleList()){
			switch (roleInfor.getRoleType()) {
			case COMPANY_MANAGER:
				loginUserContextManager.roleIdSetter().forCompanyAdmin(roleInfor.getRoleID());
				break;
			case EMPLOYMENT:
				loginUserContextManager.roleIdSetter().forAttendance(roleInfor.getRoleID());
				break;
			case GROUP_COMAPNY_MANAGER:
				loginUserContextManager.roleIdSetter().forGroupCompaniesAdmin(roleInfor.getRoleID());
				break;
			case HUMAN_RESOURCE:
				loginUserContextManager.roleIdSetter().forPersonnel(roleInfor.getRoleID());
				break;
			case MY_NUMBER:
				break;
			case OFFICE_HELPER:
				loginUserContextManager.roleIdSetter().forOfficeHelper(roleInfor.getRoleID());
				break;
			case PERSONAL_INFO:
				loginUserContextManager.roleIdSetter().forPersonalInfo(roleInfor.getRoleID());
				break;
			case SALARY:
				loginUserContextManager.roleIdSetter().forPayroll(roleInfor.getRoleID());
				break;
			case SYSTEM_MANAGER:
				loginUserContextManager.roleIdSetter().forSystemAdmin(roleInfor.getRoleID());
				break;
			default:
				break;
			}
		}
		val paramsMap = new LinkedHashMap<String, String>();
		paramsMap.put("menu", SprStubHelper.formatParam(menuCDReal));
		paramsMap.put("loginemployeeCode", SprStubHelper.formatParam(loginEmployeeCDReal));
		paramsMap.put("employeeCode", SprStubHelper.formatParam(targetEmployeeCDReal));
		paramsMap.put("", SprStubHelper.formatParamTime(startTimeReal));
		paramsMap.put("endtime", SprStubHelper.formatParamTime(endTimeReal));
		paramsMap.put("date", SprStubHelper.formatParam(targetDateReal));
		paramsMap.put("selecttype", SprStubHelper.formatParam(selectTypeReal));
		paramsMap.put("applicationID", SprStubHelper.formatParam(applicationIDReal));
		paramsMap.put("reason", SprStubHelper.formatParam(reasonReal));
		paramsMap.put("stampProtection", SprStubHelper.formatParam(stampProtectionReal));
		
		val paramsValue = new LinkedHashMap<String, String>();
		paramsValue.put("menu", menuCDReal);
		paramsValue.put("loginemployeeCode", loginEmployeeCDReal);
		paramsValue.put("employeeCode", targetEmployeeCDReal);
		paramsValue.put("starttime", startTimeReal);
		paramsValue.put("endtime", endTimeReal);
		paramsValue.put("date", loginParamCheck.getDate(targetDateReal).toString());
		paramsValue.put("selecttype", selectTypeReal);
		paramsValue.put("applicationID", applicationIDReal);
		paramsValue.put("reason", reasonReal);
		paramsValue.put("stampProtection", stampProtectionReal);
		paramsValue.put("userID", loginUserContextSpr.getUserID());
		paramsValue.put("contractCD", loginUserContextSpr.getContractCD());
		paramsValue.put("companyID", loginUserContextSpr.getCompanyID());
		paramsValue.put("companyCD", loginUserContextSpr.getCompanyCD());
		paramsValue.put("personID", loginUserContextSpr.getPersonID());
		paramsValue.put("loginEmployeeID", loginUserContextSpr.getLoginEmployeeID());
		paramsValue.put("roleID", "");
		paramsValue.put("employeeID", loginUserContextSpr.getEmployeeID());
		
		val html = new StringBuilder()
				.append("<!DOCTYPE html>")
				.append("<html><body>");
		paramsMap.forEach((name, value) -> {
			html.append(name + " : " + value + "<br/>");
			
		});
		
		val paramStringValue = new StringBuilder();
		paramsValue.forEach((name,value)->{
			paramStringValue.append(name+":'"+value+"',");
		});
		
		html.append("<script>");
		html.append("window.sessionStorage.setItem(\"paramSPR\", JSON.stringify({"+paramStringValue+"}));");
		html.append("window.location.href = '../../../../view/spr/index.xhtml'");
		html.append("</script>");
		html.append("</body></html>");
		
		
		return html.toString();
	}
	
	@POST
	@Path("02/getapplicationstatus")
	@Produces("application/json")
	public SprStubHelper.StatusContainer<SprStubHelper.RequestApplicationStatusResult> getRequestApplicationStatus(
			SprStubHelper.ApplicationStatusQuery query) {
		
		List<RequestApplicationStatusResult> requestAppStatusList = sprAppStatusService.getAppStatus(
				query.getLoginemployeeCode(), 
				query.getEmployeeCode(), 
				query.getStartdate(), 
				query.getEnddate())
				.stream()
				.map(x -> new SprStubHelper.RequestApplicationStatusResult(
						x.getDate().toString("yyyy/MM/dd"), 
						x.getStatus1(), 
						x.getStatus2(), 
						x.getApplicationID1().map(y -> y.toString()).orElse(null), 
						x.getApplicationID2().map(y -> y.toString()).orElse(null)))
				.collect(Collectors.toList());
		
		return new SprStubHelper.StatusContainer<>(requestAppStatusList);
	}
	
	@POST
	@Path("03/getstatusofdaily")
	@Produces("application/json")
	public SprStubHelper.StatusContainer<SprStubHelper.RecordApplicationStatusResult> getRecordApplicationStatus(
			SprStubHelper.ApplicationStatusQuery query) {

		List<RecordApplicationStatusResult> recordAppStatusList = sprDailyStatusService.getStatusOfDaily(
				query.getLoginemployeeCode(), 
				query.getEmployeeCode(), 
				query.getStartdate(), 
				query.getEnddate())
				.stream()
				.map(x -> new SprStubHelper.RecordApplicationStatusResult(
						x.getDate().toString("yyyy/MM/dd"), 
						x.getStatus1(), 
						x.getStatus2()))
				.collect(Collectors.toList());
		
		
		return new SprStubHelper.StatusContainer<>(recordAppStatusList);
	}
	
	@POST
	@Path("04/getapprovalroot")
	@Produces("application/json")
	public SprStubHelper.EmployeesContainer<SprStubHelper.ApplicationTargetResult> getApprovalRoot(
			SprStubHelper.ApplicationTargetQuery query) {
		List<ApplicationTargetResult> applicationTargetResultList = sprApprovalRootService.getApprovalRoot(
				query.getLoginemployeeCode(), 
				query.getDate())
				.stream()
				.map(x -> new SprStubHelper.ApplicationTargetResult(
						x.getEmployeeCD(), 
						x.getStatus1(), 
						x.getStatus2()))
				.collect(Collectors.toList());
		
		return new SprStubHelper.EmployeesContainer<>(applicationTargetResultList);
	}
}
