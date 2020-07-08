package nts.uk.ctx.at.request.dom.application.common.service.other;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.ApplicationType_Old;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsenceRepository;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.RecordWorkInfoImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedule.basicschedule.ScBasicScheduleImport;
import nts.uk.ctx.at.request.dom.application.common.adapter.schedule.schedulemanagementcontrol.ScheduleManagementControlAdapter;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.AchievementOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.WorkTimeOutput;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.WorkTypeOutput;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class CollectAchievementImpl implements CollectAchievement {
	
	@Inject
	private RecordWorkInfoAdapter recordWorkInfoAdapter;
	
	@Inject
	private ScheduleManagementControlAdapter scheduleManagementControlAdapter;

	@Inject
	private ScBasicScheduleAdapter scBasicScheduleAdapter;
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Inject
	private WorkTimeSettingRepository WorkTimeRepository;
	
	@Inject
	private ApplicationRepository_New applicationRepository;
	
	@Inject
	private AppAbsenceRepository appAbsenceRepository;
	
	@Override
	public AchievementOutput getAchievement(String companyID, String applicantID, GeneralDate appDate) {
		//Imported(申請承認)「勤務実績」を取得する - (lấy thông tin Imported(appAproval)「DailyPerformance」) - RQ5
		RecordWorkInfoImport recordWorkInfoImport = recordWorkInfoAdapter.getRecordWorkInfo(applicantID, appDate);
		WorkTypeOutput workTypeOutput = null;
		WorkTimeOutput workTimeOutput = null;
		Integer startTime1 = null, endTime1 = null, startTime2 = null, endTime2 = null;
		if(Strings.isBlank(recordWorkInfoImport.getWorkTypeCode()) && Strings.isBlank(recordWorkInfoImport.getWorkTimeCode())){
			//取得件数＝0件 ( số data lấy được  = 0)
			//スケジュールを管理するかチェックする(check có quản lý schedule hay không) - RQ536
//			取得条件：
//			・社員ID＝申請者の社員ID
//			・申請日
			if(!scheduleManagementControlAdapter.isScheduleManagementAtr(applicantID, appDate)){//管理しない
				return new AchievementOutput(appDate, new WorkTypeOutput("", ""), new WorkTimeOutput("", ""), null, null, null, null);
			}
			//管理する
			//Imported(申請承認)「勤務予定」を取得する(lấy thông tin imported(申請承認)「勤務予定」)
			Optional<ScBasicScheduleImport> opScBasicScheduleImport = scBasicScheduleAdapter.findByID(applicantID, appDate);
			if(!opScBasicScheduleImport.isPresent()){//取得件数＝0件 ( số data lấy được  = 0)
				return new AchievementOutput(appDate, new WorkTypeOutput("", ""), new WorkTimeOutput("", ""), null, null, null, null);
			}
			//取得件数＝1件(số data lấy được = 1)
			ScBasicScheduleImport scBasicScheduleImport = opScBasicScheduleImport.get();
			startTime1 = scBasicScheduleImport.getScheduleStartClock1();
			endTime1 = scBasicScheduleImport.getScheduleEndClock1();
			startTime2 = scBasicScheduleImport.getScheduleStartClock2();
			endTime2 = scBasicScheduleImport.getScheduleEndClock2();
			workTypeOutput = new WorkTypeOutput(scBasicScheduleImport.getWorkTypeCode(), "");
			workTimeOutput = new WorkTimeOutput(scBasicScheduleImport.getWorkTimeCode(), "");
		} else {//取得件数＝1件(số data lấy được = 1)
			startTime1 = recordWorkInfoImport.getAttendanceStampTimeFirst();
			endTime1 = recordWorkInfoImport.getLeaveStampTimeFirst();
			startTime2 = recordWorkInfoImport.getAttendanceStampTimeSecond();
			endTime2 = recordWorkInfoImport.getLeaveStampTimeSecond();
			workTypeOutput = new WorkTypeOutput(recordWorkInfoImport.getWorkTypeCode(), "");
			workTimeOutput = new WorkTimeOutput(recordWorkInfoImport.getWorkTimeCode(), "");
		}
		//ドメインモデル「勤務種類」を1件取得する - (lấy 1 dữ liệu của domain 「WorkType」)
		workTypeOutput = workTypeRepository.findByPK(companyID, workTypeOutput.getWorkTypeCode())
			.map(x -> new WorkTypeOutput(x.getWorkTypeCode().v(), x.getName().v()))
            .orElse(new WorkTypeOutput(
                    workTypeOutput.getWorkTypeCode() != "" ? workTypeOutput.getWorkTypeCode() : "", ""));
		//ドメインモデル「就業時間帯」を1件取得する - (lấy 1 dữ liệu của domain 「WorkTime」)
		workTimeOutput = WorkTimeRepository.findByCode(companyID, workTimeOutput.getWorkTimeCD())
				.map(x -> new WorkTimeOutput(x.getWorktimeCode().v(), x.getWorkTimeDisplayName().getWorkTimeName().v()))
                .orElse(new WorkTimeOutput(workTimeOutput.getWorkTimeCD() != null ? workTimeOutput.getWorkTimeCD() : "",
                        ""));
		return new AchievementOutput(appDate, workTypeOutput, workTimeOutput, startTime1, endTime1, startTime2, endTime2);
	}

	@Override
	public List<AchievementOutput> getAchievementContents(String companyID, String employeeID,
			List<GeneralDate> dateLst, ApplicationType_Old appType) {
		List<AchievementOutput> result = new ArrayList<>();
		// INPUT．申請対象日リストをチェックする
		if(CollectionUtil.isEmpty(dateLst)) {
			return Collections.emptyList();
		}
		// INPUT．申請対象日リストを先頭から最後へループする
		for(GeneralDate loopDate : dateLst) {
			// INPUT．申請種類をチェックする
			if(appType==ApplicationType_Old.OVER_TIME_APPLICATION || appType==ApplicationType_Old.BREAK_TIME_APPLICATION) {
				continue;
			}
			// 実績の取得
			AchievementOutput achievementOutput = this.getAchievement(companyID, employeeID, loopDate);
			// 取得した実績をOutput「表示する実績内容」に追加する
			result.add(achievementOutput);
		}
		return result;
	}

	@Override
	public List<AppDetailContent> getPreAppContents(String companyID, String employeeID, List<GeneralDate> dateLst,
			ApplicationType_Old appType) {
		List<AppDetailContent> result = new ArrayList<>();
		// INPUT．申請対象日リストをチェックする
		if(CollectionUtil.isEmpty(dateLst)) {
			return Collections.emptyList();
		}
		// INPUT．申請対象日リストを先頭から最後へループする
		for(GeneralDate loopDate : dateLst) {
			// ドメインモデル「申請」を取得
			if(appType == ApplicationType_Old.ABSENCE_APPLICATION) {
				// AppAbsence appAbsence = appAbsenceRepository.getAbsenceById(companyID, "").get();
				// result.add(appAbsence);
			}
		}
		return result;
	}

}
