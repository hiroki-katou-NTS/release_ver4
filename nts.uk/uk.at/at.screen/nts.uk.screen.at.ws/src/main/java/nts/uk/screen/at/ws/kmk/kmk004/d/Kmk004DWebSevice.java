package nts.uk.screen.at.ws.kmk.kmk004.d;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.monunit.MonthlyWorkTimeSet.LaborWorkTypeAttr;
import nts.uk.screen.at.app.kmk004.d.BasicSettingsByEmployment;
import nts.uk.screen.at.app.query.kmk004.b.DisplayBasicSettingsDto;
import nts.uk.screen.at.app.query.kmk004.common.DisplayMonthlyWorkingDto;
import nts.uk.screen.at.app.query.kmk004.common.EmploymentCodeDto;
import nts.uk.screen.at.app.query.kmk004.common.EmploymentList;
import nts.uk.screen.at.app.query.kmk004.common.MonthlyWorkingHoursByEmployment;
import nts.uk.screen.at.app.query.kmk004.common.YearDto;
import nts.uk.screen.at.app.query.kmk004.common.YearListByEmployment;

/**
 * 
 * @author chungnt
 *
 */

@Path("screen/at/kmk004")
@Produces("application/json")
public class Kmk004DWebSevice extends WebService {

	@Inject
	private EmploymentList employmentList;
	
	@Inject
	private BasicSettingsByEmployment baseSetting;
	
	@Inject
	private YearListByEmployment getYears;
	
	@Inject
	private MonthlyWorkingHoursByEmployment workTime;
	
	
	@POST
	@Path("viewd/emp/getEmploymentId")
	public List<EmploymentCodeDto> getEmploymentCode() {
		return this.employmentList.get(LaborWorkTypeAttr.DEFOR_LABOR);
	}
	
	@POST
	@Path("viewd/emp/getBaseSetting/{employmentCode}")
	public DisplayBasicSettingsDto getBaseSetting(@PathParam("employmentCode") String employmentCode) {
		return this.baseSetting.getSetting(employmentCode);
	}
	
	@POST
	@Path("viewd/emp/years/{employmentCode}")
	public List<YearDto> getYears(@PathParam("employmentCode") String employmentCode) {
		return this.getYears.get(employmentCode, LaborWorkTypeAttr.DEFOR_LABOR);
	}
	
	@POST
	@Path("viewd/emp/getWorkTimes/{employmentCode}/{year}")
	public List<DisplayMonthlyWorkingDto> getWorkTimes(@PathParam("employmentCode") String employmentCode, @PathParam("year") int year) {
		return this.workTime.get(employmentCode, LaborWorkTypeAttr.REGULAR_LABOR, year);
	}
	
}
