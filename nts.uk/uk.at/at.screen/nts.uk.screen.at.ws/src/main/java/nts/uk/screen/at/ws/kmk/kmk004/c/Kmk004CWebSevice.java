package nts.uk.screen.at.ws.kmk.kmk004.c;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import nts.arc.layer.ws.WebService;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.monunit.MonthlyWorkTimeSet.LaborWorkTypeAttr;
import nts.uk.screen.at.app.kmk004.c.BasicSettingsForWorkplace;
import nts.uk.screen.at.app.query.kmk004.b.DisplayBasicSettingsDto;
import nts.uk.screen.at.app.query.kmk004.common.DisplayMonthlyWorkingDto;
import nts.uk.screen.at.app.query.kmk004.common.MonthlyWorkingHoursByWorkplace;
import nts.uk.screen.at.app.query.kmk004.common.WorkplaceIdDto;
import nts.uk.screen.at.app.query.kmk004.common.WorkplaceList;
import nts.uk.screen.at.app.query.kmk004.common.YearDto;
import nts.uk.screen.at.app.query.kmk004.common.YearlyListByWorkplace;

/**
 * 
 * @author chungnt
 *
 */
@Path("screen/at/kmk004")
@Produces("application/json")
public class Kmk004CWebSevice extends WebService{

	@Inject
	private WorkplaceList workplaceList;
	
	@Inject
	private BasicSettingsForWorkplace baseSetting;
	
	@Inject
	private YearlyListByWorkplace getYears;
	
	@Inject
	private MonthlyWorkingHoursByWorkplace getWorkTimes;
	
	
	@POST
	@Path("viewc/wkp/getWorkPlaceId")
	public List<WorkplaceIdDto> getWorkPlaceId() {
		return this.workplaceList.get(LaborWorkTypeAttr.REGULAR_LABOR);
	}
	
	
	@POST
	@Path("viewc/wkp/getBaseSetting/{wkpId}")
	public DisplayBasicSettingsDto getBaseSetting(@PathParam("wkpId") String wkpId) {
		return this.baseSetting.getSetting(wkpId);
	}
	
	@POST
	@Path("viewc/wkp/getYears/{wkpId}")
	public List<YearDto> getYears(@PathParam("wkpId") String wkpId) {
		return this.getYears.get(wkpId, LaborWorkTypeAttr.REGULAR_LABOR);
	}
	
	@POST
	@Path("viewc/wkp/getWorkTimes/{wkpId}/{year}")
	public List<DisplayMonthlyWorkingDto> getWorkTimes(@PathParam("wkpId") String wkpId, @PathParam("year") int year) {
		return this.getWorkTimes.get(wkpId, LaborWorkTypeAttr.REGULAR_LABOR, year);
	}
	
}
