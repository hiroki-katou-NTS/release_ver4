package nts.uk.ctx.at.schedule.app.command.schedule.basicschedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.layer.app.command.CommandHandlerWithResult;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.basicschedule.BasicScheduleRepository;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.worktime.WorkTime;
import nts.uk.ctx.at.shared.dom.worktime.WorkTimeRepository;
import nts.uk.ctx.at.shared.dom.worktype.DeprecateClassification;
import nts.uk.ctx.at.shared.dom.worktype.DisplayAtr;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author sonnh1
 * 
 *         Insert or Update data to DB BASIC_SCHEDULE. If error exist, return
 *         error
 *
 */
@RequestScoped
public class RegisterBasicScheduleCommandHandler
		extends CommandHandlerWithResult<List<RegisterBasicScheduleCommand>, List<String>> {

	@Inject
	private WorkTypeRepository workTypeRepo;

	@Inject
	private WorkTimeRepository workTimeRepo;

	@Inject
	private BasicScheduleRepository basicScheduleRepo;

	@Inject
	private BasicScheduleService basicScheduleService;

	@Override
	protected List<String> handle(CommandHandlerContext<List<RegisterBasicScheduleCommand>> context) {
		String companyId = AppContexts.user().companyId();
		List<String> errList = new ArrayList<String>();
		List<RegisterBasicScheduleCommand> bScheduleCommand = context.getCommand();
		List<String> listWorkTypeCode = bScheduleCommand.stream().map(x -> {
			return x.getWorkTypeCode();
		}).collect(Collectors.toList());
		List<String> listWorkTimeCode = bScheduleCommand.stream().map(x -> {
			return x.getWorkTimeCode();
		}).collect(Collectors.toList());

		List<WorkType> listWorkType = workTypeRepo.getPossibleWorkType(companyId, listWorkTypeCode);
		List<WorkTime> listWorkTime = workTimeRepo.findByCodeList(companyId, listWorkTimeCode);

		Map<String, WorkType> workTypeMap = listWorkType.stream().collect(Collectors.toMap(x -> {
			return x.getWorkTypeCode().v();
		}, x -> x));

		Map<String, WorkTime> workTimeMap = listWorkTime.stream().collect(Collectors.toMap(x -> {
			return x.getSiftCD().v();
		}, x -> x));

		for (RegisterBasicScheduleCommand bSchedule : bScheduleCommand) {
			BasicSchedule basicScheduleObj = BasicSchedule.createFromJavaType(bSchedule.getEmployeeId(),
					bSchedule.getDate(), bSchedule.getWorkTypeCode(), bSchedule.getWorkTimeCode());

			// Check WorkType
			WorkType workType = workTypeMap.get(bSchedule.getWorkTypeCode()); 

			if (workType == null) {
				// set error to list
				errList.add("Msg_436");
				continue;
			}

			if (workType.getDeprecate() == DeprecateClassification.Deprecated) {
				// set error to list
				errList.add("Msg_468");
				continue;
			}

			// Check WorkTime
			if (StringUtil.isNullOrEmpty(bSchedule.getWorkTimeCode(), true)) {
				continue;
			}

			WorkTime workTime = workTimeMap.get(bSchedule.getWorkTimeCode());

			if (workTime == null) {
				// Set error to list
				errList.add("Msg_437");
				continue;
			}

			if (workTime.getDispAtr().value == DisplayAtr.DisplayAtr_NotDisplay.value) {
				// Set error to list
				errList.add("Msg_469");
				continue;
			}

			// Check workType-workTime
			try {
				basicScheduleService.checkPairWorkTypeWorkTime(workType.getWorkTypeCode().v(),
						workTime.getSiftCD().v());
			} catch (RuntimeException ex) {
				errList.add(ex.getMessage());
				continue;
			}

			// Check exist of basicSchedule
			Optional<BasicSchedule> basicSchedule = basicScheduleRepo.find(bSchedule.getEmployeeId(),
					bSchedule.getDate());
			// Insert/Update
			if (basicSchedule.isPresent()) {
				basicScheduleRepo.update(basicScheduleObj);
			} else {
				basicScheduleRepo.insert(basicScheduleObj);
			}
		}

		return errList;
	}
}
