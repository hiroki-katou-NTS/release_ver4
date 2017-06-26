package nts.uk.ctx.pr.core.ws.rule.employment.allot;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.uk.ctx.pr.core.app.command.rule.employment.allot.InsertAllotCompanyCommand;
import nts.uk.ctx.pr.core.app.find.rule.employment.allot.CompanyAllotSettingDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.allot.EmployeeAllSettingDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.allot.EmployeeAllotSettingDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.allot.EmployeeAllotSettingFinder;
import nts.uk.ctx.pr.core.app.find.rule.employment.allot.EmployeeAllotSettingHeaderFinder;
import nts.uk.shr.com.context.AppContexts;

@Path("pr/core/allot")
@Produces("application/json")
public class EmployeeAllotSettingWebService {
	
	@Inject
	private EmployeeAllotSettingFinder employeeAllotSettingFinder;
	@POST
	@Path("findallemployeeallotdetail")
	public List<EmployeeAllotSettingDto> GetAllEmployeeAllotSettingDetailList(String histId){
		return this.employeeAllotSettingFinder.getAllEmployeeAllotDetailSetting(AppContexts.user().companyCode(),histId);
	}
	
	@POST
	@Path("findAllEmployeeAllotSettingList/{histId}")
	public List<EmployeeAllSettingDto> getAllEmployeeAllotSettingList(@PathParam("histId") String histid ){
		return this.employeeAllotSettingFinder.getAllEmployeeAllotSettingList(AppContexts.user().companyCode(), histid);
	}
	@POST
	@Path("findEmployeeDetail/{histId}")
	public List<EmployeeAllotSettingDto> findEmployeeDetail(@PathParam("histId") String histid){
		return this.employeeAllotSettingFinder.getEmpDetail(AppContexts.user().companyCode(), histid);
	}
	
//	@POST
//	@Path("insert")
//	public void insert(insertAllotEmployeeCommand command) {
//		this.insert.handle(command);
//	}
	
//	@POST
//	@Path("findAllEmployeeName")
//	public List<>
	
}
