package nts.uk.ctx.at.request.dom.application.applist.service.detail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.i18n.I18NText;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsence;
import nts.uk.ctx.at.request.dom.application.appabsence.AppAbsenceRepository;
import nts.uk.ctx.at.request.dom.application.appabsence.appforspecleave.AppForSpecLeave;
import nts.uk.ctx.at.request.dom.application.appabsence.appforspecleave.AppForSpecLeaveRepository;
import nts.uk.ctx.at.request.dom.application.applist.service.OverTimeFrame;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectly;
import nts.uk.ctx.at.request.dom.application.gobackdirectly.GoBackDirectlyRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.absenceleaveapp.AbsenceLeaveAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentApp;
import nts.uk.ctx.at.request.dom.application.holidayshipment.recruitmentapp.RecruitmentAppRepository;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWork;
import nts.uk.ctx.at.request.dom.application.holidayworktime.AppHolidayWorkRepository;
import nts.uk.ctx.at.request.dom.application.holidayworktime.HolidayWorkInput;
import nts.uk.ctx.at.request.dom.application.overtime.AppOverTime;
import nts.uk.ctx.at.request.dom.application.overtime.AppOvertimeDetail;
import nts.uk.ctx.at.request.dom.application.overtime.AttendanceType;
import nts.uk.ctx.at.request.dom.application.overtime.OverTimeInput;
import nts.uk.ctx.at.request.dom.application.overtime.OvertimeRepository;
import nts.uk.ctx.at.request.dom.application.overtime.TimeItemTypeAtr;
import nts.uk.ctx.at.request.dom.application.workchange.AppWorkChange;
import nts.uk.ctx.at.request.dom.application.workchange.IAppWorkChangeRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.repository.BPTimeItemRepository;
import nts.uk.ctx.at.shared.dom.bonuspay.timeitem.BonusPayTimeItem;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrame;
import nts.uk.ctx.at.shared.dom.ot.frame.OvertimeWorkFrameRepository;
import nts.uk.ctx.at.shared.dom.relationship.repository.RelationshipRepository;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrame;
import nts.uk.ctx.at.shared.dom.workdayoff.frame.WorkdayoffFrameRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.time.TimeWithDayAttr;
/**
 * get info detail application
 * @author hoatt
 *
 */
@Stateless
public class AppDetailInfoImpl implements AppDetailInfoRepository{

	@Inject
	private GoBackDirectlyRepository repoGoBack;
	@Inject
	private OvertimeRepository repoOverTime;
	@Inject
	private BPTimeItemRepository repoBonusTime;
	@Inject
	private WorkdayoffFrameRepository repoWork;
	@Inject
	private OvertimeWorkFrameRepository repoOverTimeFr;
	@Inject
	private AppHolidayWorkRepository repoHolidayWork;
	@Inject
	private WorkTypeRepository repoWorkType;
	@Inject
	private WorkTimeSettingRepository repoworkTime;
	@Inject
	private IAppWorkChangeRepository repoworkChange;
	@Inject
	private AppAbsenceRepository repoAbsence;
	@Inject
	private RelationshipRepository repoRelationship;
	@Inject
	private AppForSpecLeaveRepository repoAppLeaveSpec;
	@Inject
	private AbsenceLeaveAppRepository absRepo;
	@Inject
	private RecruitmentAppRepository recRepo;
	/**
	 * 残業申請
	 * get Application Over Time Info
	 * appType = 0;
	 * @param companyID
	 * @param appId
	 * @return
	 */
	@Override
	public AppOverTimeInfoFull getAppOverTimeInfo(String companyId, String appId) {
		Map<String, AppOverTime> appOtOp = repoOverTime.getListAppOvertimeFrame(companyId, Arrays.asList(appId));
		AppOverTime appOt = appOtOp.get(appId);
		List<OverTimeInput> lstOverTimeInput = appOt.getOverTimeInput();
		List<OverTimeFrame> lstFrame = new ArrayList<>();
		for (OverTimeInput overTime : lstOverTimeInput) {
			List<Integer> lstFrameNo = new ArrayList<>();
			if(overTime.getAttendanceType().equals(AttendanceType.BONUSPAYTIME)){
				lstFrameNo.add(overTime.getFrameNo());
				if(overTime.getTimeItemTypeAtr().equals(TimeItemTypeAtr.NORMAL_TYPE)){
					List<BonusPayTimeItem> lstFramBonus = repoBonusTime.getListBonusPayTimeItemName(companyId, lstFrameNo);
					lstFrame.add(new OverTimeFrame(3, lstFramBonus.get(0).getId(),lstFramBonus.get(0).getTimeItemName().v(),
							lstFramBonus.get(0).getTimeItemTypeAtr().value,
							overTime.getApplicationTime() == null ? null : overTime.getApplicationTime().v(),
							overTime.getStartTime() == null ? null : overTime.getStartTime().v(),
							overTime.getEndTime() == null ? null : overTime.getEndTime().v()));
				}else{
					List<BonusPayTimeItem> specs = repoBonusTime
							.getListSpecialBonusPayTimeItemName(companyId, lstFrameNo);
					lstFrame.add(new OverTimeFrame(4, specs.get(0).getId(),specs.get(0).getTimeItemName().v(),
							specs.get(0).getTimeItemTypeAtr().value,
							overTime.getApplicationTime() == null ? null : overTime.getApplicationTime().v(),
							overTime.getStartTime() == null ? null : overTime.getStartTime().v(),
							overTime.getEndTime() == null ? null : overTime.getEndTime().v()));
				}
			}
			if(overTime.getAttendanceType().equals(AttendanceType.BREAKTIME)){
				lstFrameNo.add(overTime.getFrameNo());
				List<WorkdayoffFrame> lstFramWork = repoWork.getWorkdayoffFrameBy(companyId,lstFrameNo);
				lstFrame.add(new OverTimeFrame(2, lstFramWork.get(0).getWorkdayoffFrNo().v().intValue(),
						lstFramWork.get(0).getWorkdayoffFrName().v(), null,
						overTime.getApplicationTime() == null ? null : overTime.getApplicationTime().v(),
						overTime.getStartTime() == null ? null : overTime.getStartTime().v(),
						overTime.getEndTime() == null ? null : overTime.getEndTime().v()));
			}
			if(overTime.getAttendanceType().equals(AttendanceType.NORMALOVERTIME)){
				String name = "";
				if(overTime.getFrameNo() == 11){
					name = I18NText.getText("CMM045_270");
				}else if(overTime.getFrameNo() == 12){
					name = I18NText.getText("CMM045_271");
				}else{
				lstFrameNo.add(overTime.getFrameNo());
				List<OvertimeWorkFrame> lstFramOt = repoOverTimeFr.getOvertimeWorkFrameByFrameNos(companyId, lstFrameNo);
				name = lstFramOt.get(0).getOvertimeWorkFrName().v();
				}
				lstFrame.add(new OverTimeFrame(1, overTime.getFrameNo(),
						name, null,
						overTime.getApplicationTime() == null ? null : overTime.getApplicationTime().v(),
						overTime.getStartTime() == null ? null : overTime.getStartTime().v(),
						overTime.getEndTime() == null ? null : overTime.getEndTime().v()));
			}
		}
		AppOvertimeDetail timeDetail = appOt.getAppOvertimeDetail().isPresent() ? appOt.getAppOvertimeDetail().get() : null;
		TimeNo417 timeNo417 = timeDetail == null ? null : 
			new TimeNo417(timeDetail.getActualTime().v() + timeDetail.getApplicationTime().v(),
					timeDetail.getLimitErrorTime().v(),
					timeDetail.getNumOfYear36Over().v(),
					timeDetail.getYear36OverMonth().stream().map(c -> c.getOverMonth().v()).collect(Collectors.toList()));
		return new AppOverTimeInfoFull(appId,
				this.convertTime(appOt.getWorkClockFrom1()),
				this.convertTime(appOt.getWorkClockTo1()),
				this.convertTime(appOt.getWorkClockFrom2()),
				this.convertTime(appOt.getWorkClockTo2()),
				lstFrame, this.convertTime(appOt.getOverTimeShiftNight()),
				this.convertTime(appOt.getFlexExessTime()),
				timeNo417);
	}

	/**
	 * 直行直帰申請
	 * get Application Go Back Info
	 * appType = 4;
	 * @param companyID
	 * @param appId
	 * @return
	 */
	@Override
	public AppGoBackInfoFull getAppGoBackInfo(String companyID, String appId) {
		Optional<GoBackDirectly> appGoBackOp = repoGoBack.findByApplicationID(companyID, appId);
		GoBackDirectly appGoBack = appGoBackOp.get();
		return new AppGoBackInfoFull(appId, appGoBack.getGoWorkAtr1().value,
				this.convertTime(appGoBack.getWorkTimeStart1().map(x -> x.v()).orElse(null)),
				appGoBack.getBackHomeAtr1().value,
				this.convertTime(appGoBack.getWorkTimeEnd1().map(x -> x.v()).orElse(null)),
				appGoBack.getGoWorkAtr2().map(x -> x.value).orElse(null),
				this.convertTime(appGoBack.getWorkTimeStart2().map(x -> x.v()).orElse(null)),
				appGoBack.getBackHomeAtr2().map(x -> x.value).orElse(null),
				this.convertTime(appGoBack.getWorkTimeEnd2().map(x -> x.v()).orElse(null)));
	}
	/**
	 * 休日出勤時間申請
	 * get Application Holiday Work Info
	 * appType = 6;
	 * @param companyID
	 * @param appId
	 * @return
	 */
	@Override
	public AppHolidayWorkFull getAppHolidayWorkInfo(String companyId, String appId) {
		Map<String,AppHolidayWork> appHdWork = repoHolidayWork.getListAppHdWorkFrame(companyId, Arrays.asList(appId));
		AppHolidayWork hdWork = appHdWork.get(appId);
		List<HolidayWorkInput> lstOverTimeInput = hdWork.getHolidayWorkInputs();
		List<OverTimeFrame> lstFrame = new ArrayList<>();
		for (HolidayWorkInput overTime : lstOverTimeInput) {
			List<Integer> lstFrameNo = new ArrayList<>();
			if(overTime.getAttendanceType().equals(AttendanceType.BONUSPAYTIME)){
				lstFrameNo.add(overTime.getFrameNo());
				List<BonusPayTimeItem> lstFramBonus = repoBonusTime.getListBonusPayTimeItemName(companyId, lstFrameNo);
				lstFrame.add(new OverTimeFrame(3, lstFramBonus.get(0).getId(),lstFramBonus.get(0).getTimeItemName().v(),
						lstFramBonus.get(0).getTimeItemTypeAtr().value, overTime.getApplicationTime().v(),
						overTime.getStartTime() == null ? null : overTime.getStartTime().v(),
						overTime.getEndTime() == null ? null : overTime.getEndTime().v()));
			}
			if(overTime.getAttendanceType().equals(AttendanceType.BREAKTIME)){
				lstFrameNo.add(overTime.getFrameNo());
				List<WorkdayoffFrame> lstFramWork = repoWork.getWorkdayoffFrameBy(companyId,lstFrameNo);
				lstFrame.add(new OverTimeFrame(2, lstFramWork.get(0).getWorkdayoffFrNo().v().intValue(),
						lstFramWork.get(0).getWorkdayoffFrName().v(), null, overTime.getApplicationTime().v(),
						overTime.getStartTime() == null ? null : overTime.getStartTime().v(),
						overTime.getEndTime() == null ? null : overTime.getEndTime().v()));
			}
			if(overTime.getAttendanceType().equals(AttendanceType.NORMALOVERTIME)){
				String name = "";
				if(overTime.getFrameNo() == 11 || overTime.getFrameNo() == 12){
					continue;
				}
				lstFrameNo.add(overTime.getFrameNo());
				List<OvertimeWorkFrame> lstFramOt = repoOverTimeFr.getOvertimeWorkFrameByFrameNos(companyId, lstFrameNo);
				name = lstFramOt.get(0).getOvertimeWorkFrName().v();
				lstFrame.add(new OverTimeFrame(1, overTime.getFrameNo(),
						name, null, overTime.getApplicationTime().v(),
						overTime.getStartTime() == null ? null : overTime.getStartTime().v(),
						overTime.getEndTime() == null ? null : overTime.getEndTime().v()));
			}

		}
		String workTypeName = "";
		if(hdWork.getWorkTypeCode() != null && !Strings.isBlank(hdWork.getWorkTypeCode().v())){
			
			Optional<WorkType> wtOp = repoWorkType.findByPK(companyId, hdWork.getWorkTypeCode().v());
			workTypeName = wtOp.isPresent() ? wtOp.get().getName().v() : "";
		}
		String workTimeName = "";
		if(hdWork.getWorkTimeCode() != null && !hdWork.getWorkTimeCode().v().equals("000")){
			Optional<WorkTimeSetting> workTime =  repoworkTime.findByCode(companyId,hdWork.getWorkTimeCode().v());
			if(workTime.isPresent()){
				workTimeName = workTime.get().getWorkTimeDisplayName().getWorkTimeName().v();
			}
		}
		AppOvertimeDetail timeDetail = hdWork.getAppOvertimeDetail().isPresent() ? hdWork.getAppOvertimeDetail().get() : null;
		TimeNo417 timeNo417 = timeDetail == null ? null : 
				new TimeNo417(timeDetail.getActualTime().v() + timeDetail.getApplicationTime().v(),
						timeDetail.getLimitErrorTime().v(),
						timeDetail.getNumOfYear36Over().v(),
						timeDetail.getYear36OverMonth().stream().map(c -> c.getOverMonth().v()).collect(Collectors.toList()));
		return new AppHolidayWorkFull(appId, workTypeName,workTimeName,
				hdWork.getWorkClock1().getStartTime() == null ? "" : this.convertTime(hdWork.getWorkClock1().getStartTime().v()),
				hdWork.getWorkClock1().getEndTime() == null ? "" : this.convertTime(hdWork.getWorkClock1().getEndTime().v()),
				hdWork.getWorkClock2().getStartTime() == null ? "" : this.convertTime(hdWork.getWorkClock2().getStartTime().v()),
				hdWork.getWorkClock2().getEndTime() == null ? "" : this.convertTime(hdWork.getWorkClock2().getEndTime().v()),
				lstFrame, timeNo417);
	}
	/**
	 * 勤務変更申請
	 * get Application Work Change Info
	 * appType = 2;
	 * @param companyID
	 * @param appId
	 * @return
	 */
	@Override
	public AppWorkChangeFull getAppWorkChangeInfo(String companyID, String appId) {
		Optional<AppWorkChange> workChange = repoworkChange.getAppworkChangeById(companyID, appId);
		AppWorkChange appWkChange = workChange.get();
		String workTypeName = "";
		if(appWkChange.getWorkTypeCd() != null && !Strings.isBlank(appWkChange.getWorkTypeCd())){
			Optional<WorkType> wt = repoWorkType.findByPK(companyID, appWkChange.getWorkTypeCd());
			workTypeName = wt.isPresent() ? wt.get().getName().v() : "";
		}
		String workTimeName = "";
		if(appWkChange.getWorkTimeCd() != null && !appWkChange.getWorkTimeCd().equals("000")){
			Optional<WorkTimeSetting> workTime =  repoworkTime.findByCode(companyID,appWkChange.getWorkTimeCd());
			if(workTime.isPresent()){
				workTimeName = workTime.get().getWorkTimeDisplayName().getWorkTimeName().v();
			}
		}
		return new AppWorkChangeFull(appId, workTypeName,
				workTimeName,
				appWkChange.getGoWorkAtr1(),
				this.convertTime(appWkChange.getWorkTimeStart1()),
				appWkChange.getBackHomeAtr1(),
				this.convertTime(appWkChange.getWorkTimeEnd1()),
				appWkChange.getGoWorkAtr2(),
				this.convertTime(appWkChange.getWorkTimeStart2()),
				appWkChange.getBackHomeAtr2(),
				this.convertTime(appWkChange.getWorkTimeEnd2()),
				this.convertTime(appWkChange.getBreakTimeStart1()),
				this.convertTime(appWkChange.getBreakTimeEnd1()));
	}
	/**
	 * 休暇申請
	 * get Application Absence Info
	 * appType = 1;
	 * @param companyID
	 * @param appId
	 * @param day
	 * @return
	 */
	@Override
	public AppAbsenceFull getAppAbsenceInfo(String companyId, String appId, Integer day) {
		//get 休暇申請
		Optional<AppAbsence> absence = repoAbsence.getAbsenceById(companyId, appId);
		AppAbsence appAbsence = absence.get();
		//get 特別休暇申請
		Optional<AppForSpecLeave> appSpec = repoAppLeaveSpec.getAppForSpecLeaveById(companyId, appId);
		if(appSpec.isPresent()){
			appAbsence.setAppForSpecLeave(appSpec.get());
		}
		String workTimeName = "";
		if(appAbsence.getWorkTimeCode() != null){
			Optional<WorkTimeSetting> workTime =  repoworkTime.findByCode(companyId,appAbsence.getWorkTimeCode().v());
			if(workTime.isPresent()){
				workTimeName = workTime.get().getWorkTimeDisplayName().getWorkTimeName().v();
			}
		}
		String startTime1 = appAbsence.getStartTime1() == null ? "" : appAbsence.getStartTime1().getDayDivision().description
				+ appAbsence.getStartTime1().getInDayTimeWithFormat();
		String endTime1 = appAbsence.getEndTime1() == null ? "" : appAbsence.getEndTime1().getDayDivision().description
				+ appAbsence.getEndTime1().getInDayTimeWithFormat();
		String startTime2 = appAbsence.getStartTime2() == null ? "" : appAbsence.getStartTime2().getDayDivision().description
				+ appAbsence.getStartTime2().getInDayTimeWithFormat();
		String endTime2 = appAbsence.getEndTime2() == null ? "" : appAbsence.getEndTime2().getDayDivision().description
				+ appAbsence.getEndTime2().getInDayTimeWithFormat();
		AppForSpecLeave appForSpec = appAbsence.getAppForSpecLeave();
		String relaCode = appForSpec == null ? "" : appForSpec.getRelationshipCD() == null ? "" : appForSpec.getRelationshipCD().v();
		String relaName = relaCode.equals("") ? "" : repoRelationship.findByCode(companyId, relaCode).get().getRelationshipName().v();
		//ver39
		String workTypeName = "";
		if(appAbsence.getWorkTypeCode() != null && !Strings.isBlank(appAbsence.getWorkTypeCode().v())){
			Optional<WorkType> wt = repoWorkType.findByPK(companyId, appAbsence.getWorkTypeCode().v());
			workTypeName = wt.isPresent() ? wt.get().getName().v() : "";
		}
		return new AppAbsenceFull(appId, appAbsence.getHolidayAppType() == null ? null : appAbsence.getHolidayAppType().value, day,
				workTimeName, appAbsence.getAllDayHalfDayLeaveAtr().value, startTime1, endTime1,startTime2, endTime2,
				relaCode, relaName, appForSpec == null ? false : appForSpec.isMournerFlag(), workTypeName);
	}
	/**
	 * 振休振出申請
	 * get Application Complt Leave Info
	 * appType = 10;
	 * @param companyID
	 * @param appId
	 * @param type
	 * @return
	 */
	@Override
	public AppCompltLeaveFull getAppCompltLeaveInfo(String companyID, String appId, int type) {
		if(type == 0){//xin nghi
			AbsenceLeaveApp abs = absRepo.findByAppId(appId).get();
			Optional<WorkType> wta = repoWorkType.findByPK(companyID, abs.getWorkTypeCD().v());
			return new AppCompltLeaveFull(abs.getAppID(), type,
					wta.isPresent() ? wta.get().getName().v() : "",
					abs.getWorkTime1() == null ? null : this.convertTime(abs.getWorkTime1().getStartTime().v()),
					abs.getWorkTime1() == null ? null : this.convertTime(abs.getWorkTime1().getEndTime().v()));
		}
		//di lam
		RecruitmentApp rec = recRepo.findByAppId(appId).get();
		Optional<WorkType> wtr = repoWorkType.findByPK(companyID, rec.getWorkTypeCD().v());
		return new AppCompltLeaveFull(rec.getAppID(), type,
				wtr.isPresent() ? wtr.get().getName().v() : "",
				this.convertTime(rec.getWorkTime1().getStartTime().v()),
				this.convertTime(rec.getWorkTime1().getEndTime().v()));
	}
	/**
	 * convert time from integer to Time_Short_HM
	 * @param time
	 * @return
	 */
	@Override
	public String convertTime(Integer time){
		if(time == null){
			return "";
		}
		TimeWithDayAttr timeConvert = new TimeWithDayAttr(time);
		return timeConvert.getDayDivision().description + timeConvert.getInDayTimeWithFormat();
	}
	/**
	 * 残業申請
	 * get list Application Over Time Info
	 * appType = 0;
	 * @param companyID
	 * @param appId
	 * @return list AppOverTimeInfoFull
	 */
	@Override
	public List<AppOverTimeInfoFull> getListAppOverTimeInfo(String companyId, List<String> lstAppId) {
		List<AppOverTimeInfoFull> lstAppFull = new ArrayList<>();
		//1.加給時間 - BONUSPAYTIME
		//1.A NORMAL_TYPE
		List<BonusPayTimeItem> lstBonus = repoBonusTime.getListBonusPayTimeItem(companyId);
		//1.B SPECIAL_TYPE
		List<BonusPayTimeItem> lstSpBonus = repoBonusTime.getListSpecialBonusPayTimeItem(companyId);
		//2.休出時間 - BREAKTIME
		List<WorkdayoffFrame> lstWork = repoWork.getAllWorkdayoffFrame(companyId);
		//3.残業時間 - NORMALOVERTIME
		List<OvertimeWorkFrame> lstOtWork = repoOverTimeFr.getAllOvertimeWorkFrame(companyId);
		//get list appOverTime detail
		Map<String, AppOverTime> mapOvFrame = repoOverTime.getListAppOvertimeFrame(companyId, lstAppId);
		for (String appId : lstAppId) {
			AppOverTime appOt = mapOvFrame.get(appId);
			List<OverTimeInput> lstOverTimeInput = appOt.getOverTimeInput();
			List<OverTimeFrame> lstFrame = new ArrayList<>();
			for (OverTimeInput overTime : lstOverTimeInput) {
				int frameNo = overTime.getFrameNo();
				if(overTime.getAttendanceType().equals(AttendanceType.BONUSPAYTIME)){//加給時間
					if(overTime.getTimeItemTypeAtr().equals(TimeItemTypeAtr.NORMAL_TYPE)){//NORMAL_TYPE
						String frameName = this.findBonusItem(lstBonus, frameNo);
						lstFrame.add(new OverTimeFrame(3, frameNo, frameName, TimeItemTypeAtr.NORMAL_TYPE.value,
								overTime.getApplicationTime() == null ? null : overTime.getApplicationTime().v(),
								overTime.getStartTime() == null ? null : overTime.getStartTime().v(),
								overTime.getEndTime() == null ? null : overTime.getEndTime().v()));
					}else{//SPECIAL_TYPE
						String frameName = this.findBonusItem(lstSpBonus, frameNo);
						lstFrame.add(new OverTimeFrame(4, frameNo, frameName, TimeItemTypeAtr.SPECIAL_TYPE.value,
								overTime.getApplicationTime() == null ? null : overTime.getApplicationTime().v(),
								overTime.getStartTime() == null ? null : overTime.getStartTime().v(),
								overTime.getEndTime() == null ? null : overTime.getEndTime().v()));
					}
				}
				if(overTime.getAttendanceType().equals(AttendanceType.BREAKTIME)){//休出時間
					String frameName = this.findWorkItem(lstWork, frameNo);
					lstFrame.add(new OverTimeFrame(2, frameNo, frameName, null,
							overTime.getApplicationTime() == null ? null : overTime.getApplicationTime().v(),
							overTime.getStartTime() == null ? null : overTime.getStartTime().v(),
							overTime.getEndTime() == null ? null : overTime.getEndTime().v()));
				}
				if(overTime.getAttendanceType().equals(AttendanceType.NORMALOVERTIME)){//残業時間
					String name = "";
					if(overTime.getFrameNo() == 11){
						name = "時間外深夜時間";
					}else if(overTime.getFrameNo() == 12){
						name = "ﾌﾚｯｸｽ超過";
					}else{
						name = this.findOtWorkItem(lstOtWork, frameNo);
					}
					lstFrame.add(new OverTimeFrame(1, frameNo, name, null,
							overTime.getApplicationTime() == null ? null : overTime.getApplicationTime().v(),
							overTime.getStartTime() == null ? null : overTime.getStartTime().v(),
							overTime.getEndTime() == null ? null : overTime.getEndTime().v()));
				}
			}
			AppOvertimeDetail timeDetail = appOt.getAppOvertimeDetail().isPresent() ? appOt.getAppOvertimeDetail().get() : null;
			TimeNo417 timeNo417 = timeDetail == null ? null : 
				new TimeNo417(timeDetail.getActualTime().v() + timeDetail.getApplicationTime().v(),
						timeDetail.getLimitErrorTime().v(),
						timeDetail.getNumOfYear36Over().v(),
						timeDetail.getYear36OverMonth().stream().map(c -> c.getOverMonth().v()).collect(Collectors.toList()));
			lstAppFull.add(new AppOverTimeInfoFull(appId,
					this.convertTime(appOt.getWorkClockFrom1()),
					this.convertTime(appOt.getWorkClockTo1()),
					this.convertTime(appOt.getWorkClockFrom2()),
					this.convertTime(appOt.getWorkClockTo2()),
					lstFrame, this.convertTime(appOt.getOverTimeShiftNight()),
					this.convertTime(appOt.getFlexExessTime()),
					timeNo417));
		}
		return lstAppFull;
	}
	/**
	 * 休日出勤申請一覧
	 * get list Application Holiday Work Info
	 * appType = 6;
	 * @param companyID
	 * @param appId
	 * @return list AppHolidayWorkFull
	 */
	@Override
	public List<AppHolidayWorkFull> getListAppHdWorkInfo(String companyId, List<String> lstAppId) {
		List<AppHolidayWorkFull> lstAppFull = new ArrayList<>();
		//1.加給時間 - BONUSPAYTIME
		//1.A NORMAL_TYPE
		List<BonusPayTimeItem> lstBonus = repoBonusTime.getListBonusPayTimeItem(companyId);
		//2.休出時間 - BREAKTIME
		List<WorkdayoffFrame> lstWork = repoWork.getAllWorkdayoffFrame(companyId);
		//3.残業時間 - NORMALOVERTIME
		List<OvertimeWorkFrame> lstOtWork = repoOverTimeFr.getAllOvertimeWorkFrame(companyId);
		//get list appHoliday detail
		Map<String, AppHolidayWork> mapHdFrame = repoHolidayWork.getListAppHdWorkFrame(companyId, lstAppId);
		if(mapHdFrame.isEmpty()) {
			return new ArrayList<>();
		}
		Map<String, String> mapWorkTimeName = new HashMap<>();
		for (String appId : lstAppId) {
			AppHolidayWork hdWork = mapHdFrame.get(appId);
			List<HolidayWorkInput> lstHdInput = hdWork.getHolidayWorkInputs();
			List<OverTimeFrame> lstFrame = new ArrayList<>();
			for (HolidayWorkInput hd : lstHdInput) {
				int frameNo = hd.getFrameNo();
				if(hd.getAttendanceType().equals(AttendanceType.BONUSPAYTIME)){//加給時間
					String frameName = this.findBonusItem(lstBonus, frameNo);
					lstFrame.add(new OverTimeFrame(3, frameNo, frameName, TimeItemTypeAtr.NORMAL_TYPE.value,
							hd.getApplicationTime() == null ? null : hd.getApplicationTime().v(),
							hd.getStartTime() == null ? null : hd.getStartTime().v(),
							hd.getEndTime() == null ? null : hd.getEndTime().v()));
				}
				if(hd.getAttendanceType().equals(AttendanceType.BREAKTIME)){//休出時間
					String frameName = this.findWorkItem(lstWork, frameNo);
					lstFrame.add(new OverTimeFrame(2, frameNo, frameName, null,
							hd.getApplicationTime() == null ? null : hd.getApplicationTime().v(),
							hd.getStartTime() == null ? null : hd.getStartTime().v(),
							hd.getEndTime() == null ? null : hd.getEndTime().v()));
				}
				if(hd.getAttendanceType().equals(AttendanceType.NORMALOVERTIME)){//残業時間
					if(frameNo == 11 || frameNo == 12){//appHd khong co loai 11,12
						continue;
					}
					String name = this.findOtWorkItem(lstOtWork, frameNo);
					lstFrame.add(new OverTimeFrame(1, frameNo, name, null,
							hd.getApplicationTime() == null ? null : hd.getApplicationTime().v(),
							hd.getStartTime() == null ? null : hd.getStartTime().v(),
							hd.getEndTime() == null ? null : hd.getEndTime().v()));
				}
			}
			String workTypeName = "";
			if(hdWork.getWorkTypeCode() != null && !Strings.isBlank(hdWork.getWorkTypeCode().v())){
				Optional<WorkType> wt = repoWorkType.findByPK(companyId, hdWork.getWorkTypeCode().v());
				workTypeName = wt.isPresent() ? wt.get().getName().v() : "";
			}
			String workTimeName = "";
			if(hdWork.getWorkTimeCode() != null && !hdWork.getWorkTimeCode().v().equals("000")){
				String wkTimeCD = hdWork.getWorkTimeCode().v();
				if(mapWorkTimeName.containsKey(wkTimeCD)){
					workTimeName  = mapWorkTimeName.get(wkTimeCD);
				}else{
					Optional<WorkTimeSetting> workTime =  repoworkTime.findByCode(companyId, wkTimeCD);
					if(workTime.isPresent()){
						workTimeName = workTime.get().getWorkTimeDisplayName().getWorkTimeName().v();
					}
					mapWorkTimeName.put(wkTimeCD, workTimeName);
				}
			}
			AppOvertimeDetail timeDetail = hdWork.getAppOvertimeDetail().isPresent() ? hdWork.getAppOvertimeDetail().get() : null;
			TimeNo417 timeNo417 = timeDetail == null ? null : 
				new TimeNo417(timeDetail.getActualTime().v() + timeDetail.getApplicationTime().v(),
						timeDetail.getLimitErrorTime().v(),
						timeDetail.getNumOfYear36Over().v(),
						timeDetail.getYear36OverMonth().stream().map(c -> c.getOverMonth().v()).collect(Collectors.toList()));
			lstAppFull.add(new AppHolidayWorkFull(appId, workTypeName,workTimeName,
					hdWork.getWorkClock1().getStartTime() == null ? "" : this.convertTime(hdWork.getWorkClock1().getStartTime().v()),
					hdWork.getWorkClock1().getEndTime() == null ? "" : this.convertTime(hdWork.getWorkClock1().getEndTime().v()),
					hdWork.getWorkClock2().getStartTime() == null ? "" : this.convertTime(hdWork.getWorkClock2().getStartTime().v()),
					hdWork.getWorkClock2().getEndTime() == null ? "" : this.convertTime(hdWork.getWorkClock2().getEndTime().v()),
					lstFrame, timeNo417));
		}
		return lstAppFull;
	}
	/**
	 * 加給時間 - BONUSPAYTIME
	 * find bonus item by frame No
	 * @param lstBonus
	 * @param frameNo
	 * @return frame Name
	 */
	private String findBonusItem(List<BonusPayTimeItem> lstBonus, int frameNo){
		for (BonusPayTimeItem bonus : lstBonus) {
			if(bonus.getId() == frameNo){
				return bonus.getTimeItemName().v();
			}
		}
		return "";
	}
	/**
	 * 2.休出時間 - BREAKTIME
	 * find work item by frame No
	 * @param lstWork
	 * @param frameNo
	 * @return frame Name
	 */
	private String findWorkItem(List<WorkdayoffFrame> lstWork, int frameNo){
		for (WorkdayoffFrame work : lstWork) {
			if(work.getWorkdayoffFrNo().v().equals(new BigDecimal(frameNo))){
				return work.getWorkdayoffFrName().v();
			}
		}
		return "";
	}
	/**
	 * 残業時間 - NORMALOVERTIME
	 * find overtime work item by frame No
	 * @param lstOtWork
	 * @param frameNo
	 * @return frame Name
	 */
	private String findOtWorkItem(List<OvertimeWorkFrame> lstOtWork, int frameNo){
		for (OvertimeWorkFrame otWork : lstOtWork) {
			if(otWork.getOvertimeWorkFrNo().v().equals(new BigDecimal(frameNo))){
				return otWork.getOvertimeWorkFrName().v();
			}
		}
		return "";
	}
	/**
	 * 勤務変更申請一覧
	 * get list Application Work Change Info
	 * @param companyID
	 * @param lstAppId
	 * @return
	 */
	@Override
	public List<AppWorkChangeFull> getListAppWorkChangeInfo(String companyID, List<String> lstAppId) {
		List<AppWorkChangeFull> lstAppFull = new ArrayList<>();
		//get list app work change by lstId
		List<AppWorkChange> lstWorkChange = repoworkChange.getListAppWorkChangeByID(companyID, lstAppId);
		Map<String,String> mapWorkTypeName = new HashMap<>();
		Map<String,String> mapWorkTimeName = new HashMap<>();
		for (AppWorkChange appWkChange : lstWorkChange) {
			//find work type name
			String wkTypeCD = appWkChange.getWorkTypeCd();
			String workTypeName = "";
			if(wkTypeCD != null &&  !Strings.isBlank(wkTypeCD)){
				if(mapWorkTypeName.containsKey(wkTypeCD)){
					workTypeName = mapWorkTypeName.get(wkTypeCD);
				}else{
					Optional<WorkType> wt = repoWorkType.findByPK(companyID, appWkChange.getWorkTypeCd());
					workTypeName = wt.isPresent() ? wt.get().getName().v() : "";
					mapWorkTypeName.put(wkTypeCD, workTypeName);
				}
			}
			//find work time name
			String wkTimeCD = appWkChange.getWorkTimeCd();
			String workTimeName = "";
			if(wkTimeCD != null && !wkTimeCD.equals("000")){
				if(mapWorkTimeName.containsKey(wkTimeCD)){
					workTimeName = mapWorkTimeName.get(wkTimeCD);
				}else{
					Optional<WorkTimeSetting> workTime =  repoworkTime.findByCode(companyID,wkTimeCD);
					workTimeName = workTime.isPresent() ? workTime.get().getWorkTimeDisplayName().getWorkTimeName().v() : "";
					mapWorkTimeName.put(wkTimeCD, workTimeName);
				}
			}
			lstAppFull.add(new AppWorkChangeFull(appWkChange.getAppId(), workTypeName,
					workTimeName,
					appWkChange.getGoWorkAtr1(),
					this.convertTime(appWkChange.getWorkTimeStart1()),
					appWkChange.getBackHomeAtr1(),
					this.convertTime(appWkChange.getWorkTimeEnd1()),
					appWkChange.getGoWorkAtr2(),
					this.convertTime(appWkChange.getWorkTimeStart2()),
					appWkChange.getBackHomeAtr2(),
					this.convertTime(appWkChange.getWorkTimeEnd2()),
					this.convertTime(appWkChange.getBreakTimeStart1()),
					this.convertTime(appWkChange.getBreakTimeEnd1())));
		}
		return lstAppFull;
	}
	/**
	 * 直行直帰申請一覧
	 * get list Application Go Back Info
	 * @param companyID
	 * @param lstAppId
	 * @return
	 */
	@Override
	public List<AppGoBackInfoFull> getListAppGoBackInfo(String companyID, List<String> lstAppId) {
		List<AppGoBackInfoFull> lstAppFull = new ArrayList<>();
		List<GoBackDirectly> lstAppGoBack = repoGoBack.getListAppGoBack(companyID, lstAppId);
		for (GoBackDirectly appGoBack : lstAppGoBack) {
			lstAppFull.add(new AppGoBackInfoFull(appGoBack.getAppID(), appGoBack.getGoWorkAtr1().value,
					this.convertTime(appGoBack.getWorkTimeStart1().map(x -> x.v()).orElse(null)),
					appGoBack.getBackHomeAtr1().value,
					this.convertTime(appGoBack.getWorkTimeEnd1().map(x -> x.v()).orElse(null)),
					appGoBack.getGoWorkAtr2().map(x -> x.value).orElse(null),
					this.convertTime(appGoBack.getWorkTimeStart2().map(x -> x.v()).orElse(null)),
					appGoBack.getBackHomeAtr2().map(x -> x.value).orElse(null),
					this.convertTime(appGoBack.getWorkTimeEnd2().map(x -> x.v()).orElse(null))));
		}
		return lstAppFull;
	}
}
