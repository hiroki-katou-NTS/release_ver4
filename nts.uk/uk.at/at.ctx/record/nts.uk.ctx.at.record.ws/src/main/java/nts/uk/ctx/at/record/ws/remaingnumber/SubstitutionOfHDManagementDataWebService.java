package nts.uk.ctx.at.record.ws.remaingnumber;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.app.command.remainingnumber.paymana.DeleteSubstitutionOfHDManaDataCommand;
import nts.uk.ctx.at.record.app.command.remainingnumber.paymana.DeleteSubstitutionOfHDManaDataCommandHandler;
import nts.uk.ctx.at.record.app.command.remainingnumber.paymana.UpdateSubstitutionOfHDManaDataCommand;
import nts.uk.ctx.at.record.app.command.remainingnumber.paymana.UpdateSubstitutionOfHDManaDataCommandHandler;
import nts.uk.ctx.at.record.app.find.remainingnumber.paymana.SubstitutionOfHDManagementDataDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.paymana.SubstitutionOfHDManagementDataFinder;
import nts.uk.ctx.at.record.app.find.remainingnumber.subhdmana.SubstitutionManagementDataFinder;
import nts.uk.ctx.at.record.app.find.remainingnumber.subhdmana.dto.ExtraHolidayManagementDataDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.subhdmana.dto.SubDataSearchConditionDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.subhdmana.dto.SubstituteDataManagementDto;

@Path("at/record/remainnumber/subhd")
@Produces("application/json")
public class SubstitutionOfHDManagementDataWebService extends WebService {

	@Inject
	private DeleteSubstitutionOfHDManaDataCommandHandler deleteSub;
	
	@Inject
	private UpdateSubstitutionOfHDManaDataCommandHandler updateSub;
	
	@Inject
	SubstitutionOfHDManagementDataFinder finder;
	
	@Inject
	private SubstitutionManagementDataFinder subManagementFinder;

	@POST
	@Path("update")
	public void update(UpdateSubstitutionOfHDManaDataCommand command){
		updateSub.handle(command);
	}
	
	@POST
	@Path("delete")
	public void delete(DeleteSubstitutionOfHDManaDataCommand command){
		deleteSub.handle(command);
	}
	
	@POST
	@Path("getBysiDRemCod/{empId}")
	// get SubstitutionOfHDManagement by SID and remainsDays > 0
	public List<SubstitutionOfHDManagementDataDto> getBysiDRemCod(@PathParam("empId")String employeeId) {
		return finder.getBysiDRemCod(employeeId);
	}
	
	@POST
	@Path("getBySidDatePeriodNoRemainDay/{empId}/{startDate}/{endDate}")
	// get SubstitutionOfHDManagement by SID and remainsDays > 0
	public List<SubstitutionOfHDManagementDataDto> getBySidDatePeriodNoRemainDay(@PathParam("empId")String employeeId, GeneralDate startDate, GeneralDate endDate) {
		return finder.getBySidDatePeriodNoRemainDay(employeeId, startDate, endDate);
	}
	
	@POST
	@Path("getBySidRemainDayAndInPayout/{empId}")
	// get SubstitutionOfHDManagement by SID and remainsDays != 0, and in PayoutMng 
	public List<SubstitutionOfHDManagementDataDto> getBySidRemainDayAndInPayout(@PathParam("empId")String employeeId) {
		return finder.getBySidRemainDayAndInPayout(employeeId);
	}

	@Path("getBySidDatePeriod/{empId}/{payoutID}")
	public List<SubstitutionOfHDManagementDataDto> getBySidDatePeriod(@PathParam("empId")String sid,@PathParam("payoutID")String payoutID) {
		return finder.getBySidDatePeriod(sid,payoutID);
	}
	
	@POST
	@Path("getSubsitutionData")
	public SubstituteDataManagementDto getSubsitutionData(SubDataSearchConditionDto dto){
		return subManagementFinder.getSubstituteManagementData(dto);
	}
	
	@POST
	@Path("getExtraHolidayData")
	public ExtraHolidayManagementDataDto getExtraHolidayData(SubDataSearchConditionDto dto){
		return subManagementFinder.getExtraHolidayManagementData(dto);
	}
}
