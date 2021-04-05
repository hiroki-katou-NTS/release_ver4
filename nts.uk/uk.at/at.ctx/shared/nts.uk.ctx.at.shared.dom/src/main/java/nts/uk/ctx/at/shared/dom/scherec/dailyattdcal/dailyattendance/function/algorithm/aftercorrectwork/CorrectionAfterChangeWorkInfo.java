package nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.aftercorrectwork;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.ctx.at.shared.dom.scherec.appreflectprocess.appreflectcondition.reflectprocess.ScheduleRecordClassifi;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.aftercorrectwork.startendwork.CorrectStartEndWorkForWorkInfo;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;

/**
 * @author ThanhNX
 *
 *         勤務情報変更後の補正
 */
@Stateless
public class CorrectionAfterChangeWorkInfo {

	@Inject
	private CorrectionShortWorkingHour correctShortWorkingHour;
	@Inject
	private PredetemineTimeSettingRepository predetemineTimeSettingRepo;
	@Inject
	private FixedWorkSettingRepository fixedWorkSettingRepo;
	@Inject
	private FlowWorkSettingRepository flowWorkSettingRepo;
	@Inject
	private FlexWorkSettingRepository flexWorkSettingRepo;
	@Inject
	private WorkTypeRepository workTypeRepo;
	@Inject
	private WorkTimeSettingRepository workTimeSettingRepo;
	@Inject
	private BasicScheduleService basicScheduleService;
	@Inject
	private TimeCorrectionProcess timeCorrectionProcess;

	public IntegrationOfDaily correction(String companyId, IntegrationOfDaily domainDaily,
			Optional<WorkingConditionItem> workCondition, ScheduleRecordClassifi classification) {

		// 短時間勤務の補正
		IntegrationOfDaily domainCorrect = correctShortWorkingHour.correct(companyId, domainDaily);

		/**始業終業時刻の補正 */
		CorrectStartEndWorkForWorkInfo.correctStartEndWork(createRequire(companyId), domainDaily);
		
		//時刻の補正
		timeCorrectionProcess.process(companyId, workCondition, domainDaily, classification);
		
		// fix 111738
		// remove TODO: ドメインモデル「予実反映」を取得 - mock new domain
		//  remove 予実反映処理の補正
		
		return domainCorrect;
	}
	
	private CorrectStartEndWorkForWorkInfo.Require createRequire(String companyId) {
		
		return new CorrectStartEndWorkForWorkInfo.Require() {
			
			@Override
			public PredetemineTimeSetting getPredetermineTimeSetting(WorkTimeCode wktmCd) {
				return predetemineTimeSettingRepo.findByWorkTimeCode(companyId, wktmCd.v()).get();
			}
			
			@Override
			public FlowWorkSetting getWorkSettingForFlowWork(WorkTimeCode code) {
				return flowWorkSettingRepo.find(companyId, code.v()).get();
			}
			
			@Override
			public FlexWorkSetting getWorkSettingForFlexWork(WorkTimeCode code) {
				return flexWorkSettingRepo.find(companyId, code.v()).get();
			}
			
			@Override
			public FixedWorkSetting getWorkSettingForFixedWork(WorkTimeCode code) {
				return fixedWorkSettingRepo.findByKey(companyId, code.v()).get();
			}
			
			@Override
			public Optional<WorkType> getWorkType(String workTypeCd) {
				return workTypeRepo.findNoAbolishByPK(companyId, workTypeCd);
			}
			
			@Override
			public Optional<WorkTimeSetting> getWorkTime(String workTimeCode) {
				return workTimeSettingRepo.findByCode(companyId, workTimeCode);
			}
			
			@Override
			public SetupType checkNeededOfWorkTimeSetting(String workTypeCode) {
				return basicScheduleService.checkNeededOfWorkTimeSetting(workTypeCode);
			}
		};
	}

}
