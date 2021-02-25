package nts.uk.screen.at.ws.kcp.kcp013.cpn;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nts.arc.error.BusinessException;
import nts.arc.i18n.I18NText;
import nts.uk.ctx.at.shared.dom.workmanagementmultiple.WorkManagementMultiple;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.screen.at.app.query.kcp013.AcquireWorkHours;
import nts.uk.screen.at.app.query.kcp013.AcquireWorkingHoursDto;
import nts.uk.screen.at.app.query.kcp013.AcquireWorkingHoursRequest;
import nts.uk.screen.at.app.query.kcp013.GetAllWorkingHoursQuery;
import nts.uk.screen.at.app.query.kcp013.KCP013Result;

/**
 * 
 * @author thanhlv
 *
 */
@Path("at/record/stamp/workhours")
@Produces("application/json")
public class GetWorkHoursWs {

	@Inject
	private GetAllWorkingHoursQuery getAllWorkingHours;

	@POST
	@Path("workhours")
	public KCP013Result getWorkHours(AcquireWorkingHoursRequest request) {
		
		KCP013Result rs = new KCP013Result();

		Optional<WorkManagementMultiple> optional = getAllWorkingHours.getUseDistinction();

		AcquireWorkingHoursDto acquireWorkingHoursDto = getAllWorkingHours.getWorkingHours(request);

		List<WorkTimeSetting> listWorkTime = acquireWorkingHoursDto.getListWorkTime();

		Map<String, PredetemineTimeSetting> predetemineTimeSettings = acquireWorkingHoursDto
				.getPredetemineTimeSettings().stream().collect(Collectors.toMap(x -> x.getWorkTimeCode().v(), x -> x));

		List<AcquireWorkHours> workHours = listWorkTime.stream().map(i -> {
			PredetemineTimeSetting setting = predetemineTimeSettings.containsKey(i.getWorktimeCode().v())
					? predetemineTimeSettings.get(i.getWorktimeCode().v())
					: null;
			return new AcquireWorkHours(i.getWorktimeCode().v(), i.getWorkTimeDisplayName().getWorkTimeName().v(),
					setting.getPrescribedTimezoneSetting().getLstTimezone().stream().filter((x) -> x.getWorkNo() == 1)
							.findFirst().get().getStart().v(),
					setting.getPrescribedTimezoneSetting().getLstTimezone().stream().filter((x) -> x.getWorkNo() == 1)
							.findFirst().get().getEnd().v(),
					setting.getPrescribedTimezoneSetting().getLstTimezone().stream().filter((x) -> x.getWorkNo() == 2)
							.findFirst().get().getStart().v(),
					setting.getPrescribedTimezoneSetting().getLstTimezone().stream().filter((x) -> x.getWorkNo() == 2)
							.findFirst().get().getEnd().v(),
					String.valueOf(i.getWorkTimeDivision().getWorkTimeDailyAtr().description) == "フレックス勤務用" ? "フレックス勤務用"
							: String.valueOf(i.getWorkTimeDivision().getWorkTimeMethodSet().description),
					i.getNote().v(), 0,  i.getWorkTimeDisplayName().getWorkTimeAbName() == null ? "" : i.getWorkTimeDisplayName().getWorkTimeAbName().v());
		}).collect(Collectors.toList());
		if (optional != null) {
			workHours.forEach(x -> {
				x.setUseDistintion(optional.get().getUseATR().value);
			});
		} else {
			throw new BusinessException("");
		}
		
		rs.setListWorkTime(workHours);
		rs.setHasWorkTimeInModeWorkPlace(acquireWorkingHoursDto.isHasWorkTimeInModeWorkPlace());
		rs.setModeCompany(acquireWorkingHoursDto.isModeCompany());
		return rs;
	}

	@POST
	@Path("getallworkhours")
	public KCP013Result getAllWorkHours() {

		KCP013Result rs = new KCP013Result();
		
		Optional<WorkManagementMultiple> optional = getAllWorkingHours.getUseDistinction();

		AcquireWorkingHoursDto acquireWorkingHoursDto = getAllWorkingHours.getAllWorkingHoursDtos();

		List<WorkTimeSetting> listWorkTime = acquireWorkingHoursDto.getListWorkTime();

		Map<String, PredetemineTimeSetting> predetemineTimeSettings = acquireWorkingHoursDto
				.getPredetemineTimeSettings().stream().collect(Collectors.toMap(x -> x.getWorkTimeCode().v(), x -> x));

		List<AcquireWorkHours> workHours = listWorkTime.stream().map(i -> {
			PredetemineTimeSetting setting = predetemineTimeSettings.containsKey(i.getWorktimeCode().v())
					? predetemineTimeSettings.get(i.getWorktimeCode().v())
					: null;
			if (setting != null) {
				return new AcquireWorkHours(i.getWorktimeCode().v(), i.getWorkTimeDisplayName().getWorkTimeName().v(),
						setting.getPrescribedTimezoneSetting().getLstTimezone().stream()
								.filter((x) -> x.getWorkNo() == 1).findFirst().get().getStart().v(),
						setting.getPrescribedTimezoneSetting().getLstTimezone().stream()
								.filter((x) -> x.getWorkNo() == 1).findFirst().get().getEnd().v(),
						setting.getPrescribedTimezoneSetting().getLstTimezone().stream()
								.filter((x) -> x.getWorkNo() == 2).findFirst().get().getStart().v(),
						setting.getPrescribedTimezoneSetting().getLstTimezone().stream()
								.filter((x) -> x.getWorkNo() == 2).findFirst().get().getEnd().v(),
						i.getWorkTimeDivision().getWorkTimeDailyAtr() == WorkTimeDailyAtr.FLEX_WORK
								? I18NText.getText("KCP013_13")
								: String.valueOf(i.getWorkTimeDivision().getWorkTimeMethodSet().description),
						i.getNote().v(), 0, i.getWorkTimeDisplayName().getWorkTimeAbName() == null ? "" : i.getWorkTimeDisplayName().getWorkTimeAbName().v());
			} else {
				return null;
			}

		}).filter(i -> i != null).collect(Collectors.toList());

		if (optional != null) {
			workHours.forEach(x -> {
				x.setUseDistintion(optional.get().getUseATR().value);
			});
		} else {
			throw new BusinessException("");
		}

		rs.setListWorkTime(workHours);
		rs.setHasWorkTimeInModeWorkPlace(acquireWorkingHoursDto.isHasWorkTimeInModeWorkPlace());
		rs.setModeCompany(acquireWorkingHoursDto.isModeCompany());
		return rs;
	}

}
