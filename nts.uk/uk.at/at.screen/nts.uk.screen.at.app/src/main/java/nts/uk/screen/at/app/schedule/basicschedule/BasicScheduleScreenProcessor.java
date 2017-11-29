package nts.uk.screen.at.app.schedule.basicschedule;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.worktype.DeprecateClassification;
import nts.uk.ctx.at.shared.dom.worktype.DisplayAtr;
import nts.uk.shr.com.context.AppContexts;

/**
 * Get data DB BASIC_SCHEDULE, WORKTIME, WORKTYPE not through dom layer
 * 
 * @author sonnh1
 *
 */

@Stateless
public class BasicScheduleScreenProcessor {
	@Inject
	private BasicScheduleScreenRepository bScheduleScreenRepo;

	@Inject
	private BasicScheduleService bScheduleService;

	/**
	 * 
	 * @param params
	 * @return
	 */
	public List<BasicScheduleScreenDto> getByListSidAndDate(BasicScheduleScreenParams params) {
		return this.bScheduleScreenRepo.getByListSidAndDate(params.employeeId, params.startDate, params.endDate);
	}

	/**
	 * 
	 * @return
	 */
	public List<WorkTimeScreenDto> getListWorkTime() {
		String companyId = AppContexts.user().companyId();
		return this.bScheduleScreenRepo.getListWorkTime(companyId, DisplayAtr.DisplayAtr_Display.value);
	}

	/**
	 * Find by companyId and DeprecateClassification = Deprecated (added by
	 * sonnh1)
	 * 
	 * @return List WorkTypeDto
	 */
	public List<WorkTypeScreenDto> findByCIdAndDeprecateCls() {
		String companyId = AppContexts.user().companyId();
		return this.bScheduleScreenRepo.findByCIdAndDeprecateCls(companyId,
				DeprecateClassification.NotDeprecated.value);
	}

	/**
	 * Check state of list WorkTypeCode
	 * 
	 * @param lstWorkTypeCode
	 * @return List StateWorkTypeCodeDto
	 */
	public List<StateWorkTypeCodeDto> checkStateWorkTypeCode(List<String> lstWorkTypeCode) {
		List<StateWorkTypeCodeDto> lstStateWorkTypeCode = lstWorkTypeCode.stream()
				.filter(x -> bScheduleService.checkWorkDay(x) != null)
				.map(x -> new StateWorkTypeCodeDto(x, bScheduleService.checkWorkDay(x).value))
				.collect(Collectors.toList());
		return lstStateWorkTypeCode;
	}

	/**
	 * 
	 * @param params
	 * @return WorkEmpCombineDto
	 */
	public WorkEmpCombineDto getListWorkEmpCombine(ScheduleScreenSymbolParams params) {
		String companyId = AppContexts.user().companyId();
		return this.bScheduleScreenRepo.getListWorkEmpCobine(companyId, params.workTypeCode, params.workTimeCode);
	}

	/**
	 * 
	 * @return ScheduleDisplayControlDto
	 */
	public ScheduleDisplayControlDto getScheduleDisplayControl() {
		String companyId = AppContexts.user().companyId();
		return this.bScheduleScreenRepo.getListScheduleDisControl(companyId);
	}

	public List<BasicScheduleScreenDto> getDataWorkScheTimezone(BasicScheduleScreenParams params) {
		return this.bScheduleScreenRepo.getDataWorkScheTimezone(params.employeeId, params.startDate, params.endDate);
	}
}
