package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.reflectattdclock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.TimePrintDestinationOutput;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.ReflectWorkInformationDomainService;
import nts.uk.ctx.at.record.dom.require.RecordDomRequireService;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.stamp.Stamp;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.SetPreClockArt;
import nts.uk.ctx.at.record.dom.workrecord.stampmanagement.timestampsetting.prefortimestaminput.StampType;
import nts.uk.ctx.at.shared.dom.WorkInformation;
import nts.uk.ctx.at.shared.dom.calculationsetting.StampReflectionManagement;
import nts.uk.ctx.at.shared.dom.calculationsetting.repository.StampReflectionManagementRepository;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.schedule.WorkingDayCategory;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.TimeLeavingWork;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.attendancetime.WorkTimes;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.calcategory.CalAttrOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.TimeActualStamp;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.EngravingMethod;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.ReasonTimeChange;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.TimeChangeMeans;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkStamp;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.common.timestamp.WorkTimeInformation;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.dailyattendancework.IntegrationOfDaily;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.editstate.EditStateOfDailyAttd;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.editstate.EditStateSetting;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.function.algorithm.ChangeDailyAttendance;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.NotUseAttribute;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.workinfomation.WorkInfoOfDailyAttendance;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemRepository;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItemService;
import nts.uk.ctx.at.shared.dom.worktime.algorithm.getcommonset.GetCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.MultiStampTimePiorityAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.PrioritySetting;
import nts.uk.ctx.at.shared.dom.worktime.common.StampPiorityAtr;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneCommonSet;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimezoneStampSet;
import nts.uk.ctx.at.shared.dom.worktime.predset.WorkNo;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;

/**
 * ??????????????????????????????
 * @author tutk
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ReflectAttendanceClock {
	
	@Inject
	private ReflectWorkInformationDomainService reflectWorkInformationDomainService;
	
	@Inject
	private StampReflectionManagementRepository timePriorityRepository;
	
	@Inject
	private WorkTypeRepository workTypeRepository;
	
	@Inject
	private WorkingConditionItemRepository workingConditionItemRepository;
	
	
	@Inject 
	private RecordDomRequireService requireService;
	/**
	 * ?????????????????????????????? 
	 * @param attendanceAtr ???????????????
	 * @param actualStampAtr ???????????????
	 * @param workNo 1~2	
	 * @param integrationOfDaily 1~2
	 */
	public ReflectStampOuput reflect(String companyId, Stamp stamp, AttendanceAtr attendanceAtr,
			ActualStampAtr actualStampAtr, int workNo, IntegrationOfDaily integrationOfDaily,
			ChangeDailyAttendance changeDailyAtt) {
		//????????????????????????
			
		TimePrintDestinationOutput timePrintDestinationOutput = getDestination(attendanceAtr, actualStampAtr, workNo, integrationOfDaily, stamp);
		ReflectStampOuput reflectStampOuput = ReflectStampOuput.NOT_REFLECT;
		//??????????????????????????????????????????????????????
		reflectStampOuput = reflectAttd(companyId, stamp, attendanceAtr, timePrintDestinationOutput,
				actualStampAtr, integrationOfDaily,workNo);
		if(reflectStampOuput == ReflectStampOuput.REFLECT ) {
			//?????????????????????????????????????????????
			checkHolidayChange(integrationOfDaily, companyId,stamp);
			
			//????????????????????????
			if(!reflectDirectBounce(stamp, integrationOfDaily)) {
				// ?????????????????????
				reflectStampOuput =  reflectStamping(actualStampAtr, stamp, integrationOfDaily, attendanceAtr, workNo);
				changeDailyAtt.setAttendance(true);
			} else {
				
				/** ?????????????????????????????????????????????????????? */
				changeDailyAtt.setDirectBounceClassifi(true);
			}
		}
		Optional<TimeLeavingWork> timeLeavingWork = integrationOfDaily.getAttendanceLeave().flatMap(x ->x.getTimeLeavingWorks().stream()
				.filter(c -> c.getWorkNo().v().intValue() == workNo).findFirst());
		Optional<TimeActualStamp> timeActualStamp = Optional.empty();
		
		if(attendanceAtr == AttendanceAtr.GOING_TO_WORK) {
			timeActualStamp = timeLeavingWork.flatMap(x -> x.getAttendanceStamp());
		}else {
			timeActualStamp = timeLeavingWork.flatMap(x -> x.getLeaveStamp());
		}
		//?????????????????????????????????Update s??? l???n ph???n ??nh ?????? ??? 	
		if(timeActualStamp.isPresent())
		this.updateNumberStampReflect(actualStampAtr, timeActualStamp);
		
		return reflectStampOuput;
		
	}
	
	/**
	 * ????????????????????????
	 * @param stamp
	 * @param integrationOfDaily
	 * @return boolean (true : ????????????  / false : ??????????????????)
	 */
	public boolean reflectDirectBounce(Stamp stamp,IntegrationOfDaily integrationOfDaily ) {
		//???????????????
		if(stamp.getType().getSetPreClockArt() == SetPreClockArt.BOUNCE ) {
			//????????????=ON?????????
			integrationOfDaily.getWorkInformation().setBackStraightAtr(NotUseAttribute.Use);
			//??????.??????????????????.???????????????????????????????????????() 
			stamp.getImprintReflectionStatus().markAsReflected(integrationOfDaily.getYmd());
			//???????????????????????????
			Optional<EditStateOfDailyAttd> editState = integrationOfDaily.getEditState().stream()
					.filter(c->c.getAttendanceItemId() == 860).findFirst();
			if(!editState.isPresent()) {
				integrationOfDaily.getEditState().add(new EditStateOfDailyAttd(860, EditStateSetting.IMPRINT));
			}
			return true;
		
		//???????????????
		}else if(stamp.getType().getSetPreClockArt() == SetPreClockArt.DIRECT ) {
			//????????????=ON?????????
			integrationOfDaily.getWorkInformation().setGoStraightAtr(NotUseAttribute.Use);
			//??????.??????????????????.???????????????????????????????????????() 
			stamp.getImprintReflectionStatus().markAsReflected(integrationOfDaily.getYmd());
			//???????????????????????????
			Optional<EditStateOfDailyAttd> editState = integrationOfDaily.getEditState().stream()
					.filter(c->c.getAttendanceItemId() == 859).findFirst();
			if(!editState.isPresent()) {
				integrationOfDaily.getEditState().add(new EditStateOfDailyAttd(859, EditStateSetting.IMPRINT));
			}
			return true;
		}
		
		return false;
	}
	
	/**
	 * ????????????????????????
	 * @param attendanceAtr ?????????????????????or?????????
	 * @param actualStampAtr ????????????(???????????????)????????????or????????????
	 * @param workNo ??????????????????NO
	 * @param integrationOfDaily
	 * @return
	 */
	public TimePrintDestinationOutput getDestination(AttendanceAtr attendanceAtr,ActualStampAtr actualStampAtr,int workNo,IntegrationOfDaily integrationOfDaily, Stamp stamp) {
		//?????????????????????????????????
		if(integrationOfDaily.getAttendanceLeave().isPresent()) {
			//??????????????????????????????????????????
			Optional<TimeLeavingWork> timeLeavingWork = integrationOfDaily.getAttendanceLeave().get().getTimeLeavingWorks().stream()
					.filter(c->c.getWorkNo().v().intValue() == workNo ).findFirst();
			if(!timeLeavingWork.isPresent()) {
				return null;
			}
			Optional<TimeActualStamp> timeActualStamp = timeLeavingWork.get().getAttendanceStamp();
			if(attendanceAtr == AttendanceAtr.LEAVING_WORK) {
				timeActualStamp = timeLeavingWork.get().getLeaveStamp();
			}
			if(!timeActualStamp.isPresent()) {
				return null;
			}
			Optional<WorkStamp> workStamp = actualStampAtr == ActualStampAtr.STAMP_REAL ? timeActualStamp.get().getActualStamp() : timeActualStamp.get().getStamp();
			//fixbug 115441
//			if(actualStampAtr == ActualStampAtr.STAMP ) {
//				workStamp = timeActualStamp.get().getStamp();
//			}
			if(!workStamp.isPresent() 
//					|| !workStamp.get().getLocationCode().isPresent()
					|| !workStamp.get().getTimeDay().getTimeWithDay().isPresent()) {
				return null;
			}
			
			Optional<TimeWithDayAttr> timeDestination = workStamp.get().getTimeDay().getTimeWithDay();
			if (stamp.getStampDateTime().day() > integrationOfDaily.getYmd().day()) {
				timeDestination = Optional.of(new TimeWithDayAttr(workStamp.get().getTimeDay().getTimeWithDay().get().v() - 1440));
			}
			
			return new TimePrintDestinationOutput(workStamp.get().getLocationCode().isPresent()? workStamp.get().getLocationCode().get():null,
					workStamp.get().getTimeDay().getReasonTimeChange().getTimeChangeMeans(),
					timeDestination.get());
		}
		return null;
		
	}
	
	private Optional<WorkStamp> getWorkStamp(AttendanceAtr attendanceAtr, ActualStampAtr actualStampAtr,
			IntegrationOfDaily integrationOfDaily, int workNo) {
		if (integrationOfDaily.getAttendanceLeave().isPresent()) {
			// ??????????????????????????????????????????
			Optional<TimeLeavingWork> timeLeavingWork = integrationOfDaily.getAttendanceLeave().get()
					.getTimeLeavingWorks().stream().filter(c -> c.getWorkNo().v().intValue() == workNo).findFirst();
			if (!timeLeavingWork.isPresent()) {
				return Optional.empty();
			}
			Optional<TimeActualStamp> timeActualStamp = timeLeavingWork.get().getAttendanceStamp();
			if (attendanceAtr == AttendanceAtr.LEAVING_WORK) {
				timeActualStamp = timeLeavingWork.get().getLeaveStamp();
			}
			if (!timeActualStamp.isPresent()) {
				return Optional.empty();
			}
			Optional<WorkStamp> workStamp = timeActualStamp.get().getActualStamp();
			if (actualStampAtr == ActualStampAtr.STAMP) {
				workStamp = timeActualStamp.get().getStamp();
			}
			return workStamp;
		}
		return Optional.empty();

	}
	
	/**
	 * ??????????????????????????????????????? (new_2020)
	 * @param timePrintDestinationOutput
	 * @param actualStampAtr
	 * @return
	 */
	public ReflectStampOuput reflectAttd(String cid, Stamp stamp, AttendanceAtr attendanceAtr,TimePrintDestinationOutput timePrintDestinationOutput,ActualStampAtr actualStampAtr,
			IntegrationOfDaily integrationOfDaily,int workNo) {
		Optional<WorkStamp> workStamp = getWorkStamp(attendanceAtr, actualStampAtr, integrationOfDaily, workNo);
		
		//???????????????????????????Null??????????????? 
 		if(timePrintDestinationOutput == null) {
			return ReflectStampOuput.REFLECT;	
		}
		//??????????????????????????????????????????????????????
		if(actualStampAtr == ActualStampAtr.STAMP ) {
			ReasonTimeChange reasonTimeChangeNew = new ReasonTimeChange(TimeChangeMeans.REAL_STAMP,Optional.of(EngravingMethod.TIME_RECORD_ID_INPUT));
			//?????????????????????????????????????????????
			boolean check = workStamp
					.map(x -> x.isCanChangeTime(new RequireImpl(), cid, reasonTimeChangeNew.getTimeChangeMeans()))
					.orElse(false);
			if(!check) {
				return ReflectStampOuput.NOT_REFLECT;
			}
		}
		//??????????????????????????????????????????????????????
		return checkReflectByLookPriority(cid, stamp, attendanceAtr, timePrintDestinationOutput, integrationOfDaily);
	}
	
	/**
	 * ?????????????????????????????????????????????????????? (new_2020)
	 * @param stamp
	 * @param attendanceAtr
	 * @param timePrintDestinationOutput
	 * @param integrationOfDaily
	 * @return
	 */
	public ReflectStampOuput checkReflectByLookPriority(String companyId, Stamp stamp, AttendanceAtr attendanceAtr,TimePrintDestinationOutput timePrintDestinationOutput,
			IntegrationOfDaily integrationOfDaily) {
		if(timePrintDestinationOutput.getStampSourceInfo() != TimeChangeMeans.REAL_STAMP) {
			return ReflectStampOuput.REFLECT;
		}
		if (integrationOfDaily.getWorkInformation() != null) {
			//???????????????????????????
			WorkTimezoneStampSet stampSet = this.getStampSetting(companyId,
					integrationOfDaily.getEmployeeId(), integrationOfDaily.getYmd(),
					integrationOfDaily.getWorkInformation());
			//???????????????????????????
			StampPiorityAtr stampPiorityAtr = StampPiorityAtr.GOING_WORK;
			if(attendanceAtr == AttendanceAtr.LEAVING_WORK ) {
				stampPiorityAtr = StampPiorityAtr.LEAVE_WORK;
			}
			PrioritySetting prioritySetting= this.getPrioritySetting(stampSet, stampPiorityAtr);
			MultiStampTimePiorityAtr priorityAtr = null;
			if (prioritySetting == null) {
				priorityAtr = MultiStampTimePiorityAtr.valueOf(0);
			} else {
				priorityAtr = prioritySetting.getPriorityAtr();
			}

			AttendanceTime attendanceTime = stamp.getAttendanceTime().isPresent()?
					stamp.getAttendanceTime().get():new AttendanceTime(stamp.getStampDateTime().clockHourMinute().v());
			TimeWithDayAttr timeDestination = timePrintDestinationOutput.getTimeOfDay();
			if (priorityAtr.value == 0) {
				if (attendanceTime.v().intValue() >= timeDestination.v().intValue()) {
					return ReflectStampOuput.NOT_REFLECT;
				} else {
					return ReflectStampOuput.REFLECT;
				}

			} else {
				if (attendanceTime.v().intValue() >= timeDestination.v().intValue()) {
					return ReflectStampOuput.REFLECT;
				} else {
					return ReflectStampOuput.NOT_REFLECT;
				}
			}

		}
		
		return ReflectStampOuput.REFLECT;
		
	}
	
	@Inject
	private WorkingConditionItemService workingConditionItemService;
	/**
	 * ??????????????????????????? (2020)
	 * @param cid
	 * @param employeeId
	 * @param date
	 * @param workInformation
	 * @return
	 */
	public WorkTimezoneStampSet getStampSetting(String cid, String employeeId, GeneralDate date,WorkInfoOfDailyAttendance workInformation) {
		//???????????????????????????????????????????????????????????????????????????????????????????????????????????????
		Optional<WorkTimeCode> workTimeCode = workInformation.getRecordInfo().getWorkTimeCodeNotNull();
		if(!workTimeCode.isPresent()) {
			//???????????????????????????????????????
			Optional<WorkInformation> wi = workingConditionItemService.getHolidayWorkScheduleNew(cid, employeeId,
					date, workInformation.getRecordInfo().getWorkTypeCode().v(), WorkingDayCategory.workingDay);
			if(!wi.isPresent()) {
				throw new RuntimeException("Not exist WorkInfo"); 
			}
			workTimeCode = wi.get().getWorkTimeCodeNotNull();
			Optional<WorkTimezoneCommonSet> workTimezoneCommonSet = GetCommonSet.workTimezoneCommonSet(
					requireService.createRequire(), cid, workTimeCode.get().v());
			if (workTimezoneCommonSet.isPresent()) {
				return workTimezoneCommonSet.get().getStampSet();
			}
			throw new RuntimeException("Not exist ??????????????????????????????1");
		}
		Optional<WorkTimezoneCommonSet> workTimezoneCommonSet = GetCommonSet.workTimezoneCommonSet(
				requireService.createRequire(), cid, workTimeCode.get().v());
		if (workTimezoneCommonSet.isPresent()) {
			return workTimezoneCommonSet.get().getStampSet();
		}
			
		throw new RuntimeException("Not exist ??????????????????????????????2"); 
	}
	
	private PrioritySetting getPrioritySetting(WorkTimezoneStampSet stampSet, StampPiorityAtr stampPiorityAtr) {
			if (stampSet.getPrioritySets().stream().filter(item -> item.getStampAtr() == stampPiorityAtr)
					.findFirst() != null
					&& stampSet.getPrioritySets().stream().filter(item -> item.getStampAtr() == stampPiorityAtr)
							.findFirst().isPresent()) {
				return stampSet.getPrioritySets().stream().filter(item -> item.getStampAtr() == stampPiorityAtr)
						.findFirst().get();
			}
			return null;

	}
	
	/**
	 * ????????????????????????????????????????????? (new_2020)
	 * @param WorkInfo
	 * @param companyId
	 * @return
	 */
	private void checkHolidayChange(IntegrationOfDaily integrationOfDaily, String companyId,Stamp stamp) {
		if (integrationOfDaily.getWorkInformation() != null) {
			//????????????????????????????????????????????????????????????????????????????????????
			WorkInformation recordWorkInformation = integrationOfDaily.getWorkInformation().getRecordInfo();
			//??????????????????????????????????????????
			CalAttrOfDailyAttd calAttr = integrationOfDaily.getCalAttr();
			//??????????????????????????????
			boolean check = stamp.getType().changeWorkOnHolidays(
					new RequireStampTypeImpl(),
					calAttr.getHolidayTimeSetting(),
					recordWorkInformation.getWorkTypeCode().v(),
					integrationOfDaily.getEmployeeId(),
					integrationOfDaily.getYmd()
					);
			if(check) {
				// ???????????????????????????
				this.reflectWorkInformationDomainService.changeWorkInformation(integrationOfDaily,
						companyId, integrationOfDaily.getEmployeeId(), integrationOfDaily.getYmd());
			}
		}
	}
	
	/**
	 * ????????????????????? (new_2020)
	 */
	public ReflectStampOuput reflectStamping(ActualStampAtr actualStampAtr, Stamp stamp, IntegrationOfDaily integrationOfDaily,
			AttendanceAtr attendanceAtr, int workNo) {
		//????????????????????????????????????????????????????????????????????? (T??? ng??y ??ang x??? l?? v?? ng??y ?????? l???y ?????????)
		TimeWithDayAttr timeWithDayAttr = TimeWithDayAttr.convertToTimeWithDayAttr(integrationOfDaily.getYmd(),
				stamp.getStampDateTime().toDate(), stamp.getStampDateTime().clockHourMinute().v());
		
		//
		TimePrintDestinationOutput timePrintDestinationOutput = new TimePrintDestinationOutput();
		//????????????????????????????????????????????????????????? (put ?????? v?? ????????? v??o ??????)
		timePrintDestinationOutput.setTimeOfDay(timeWithDayAttr);
		//??????????????????????????????????????????????????????????????????????????? (Copy ???????????? c???a ?????? v??o ??????????????? c???a ???????????????)
		timePrintDestinationOutput.setLocationCode((stamp.getRefActualResults().getWorkInforStamp().isPresent() && stamp.getRefActualResults().getWorkInforStamp().get().getWorkLocationCD().isPresent())
				? stamp.getRefActualResults().getWorkInforStamp().get().getWorkLocationCD().get()
				: null);
		//??????????????????????????????????????????????????????
		timePrintDestinationOutput.setStampSourceInfo(TimeChangeMeans.REAL_STAMP);
		if(integrationOfDaily.getAttendanceLeave().isPresent() &&
			integrationOfDaily.getAttendanceLeave().get().getTimeLeavingWorks().stream()
				.filter(c -> c.getWorkNo().v().intValue() == workNo).findFirst().isPresent()) {
			//??????????????????????????????????????????????????????????????????
			TimeLeavingWork timeLeavingWork = integrationOfDaily.getAttendanceLeave().get().getTimeLeavingWorks().stream()
					.filter(c -> c.getWorkNo().v().intValue() == workNo).findFirst().get();
			Optional<TimeActualStamp> timeActualStamp = Optional.empty();
			if(attendanceAtr == AttendanceAtr.GOING_TO_WORK) {
				timeActualStamp = timeLeavingWork.getAttendanceStamp();
			}else {
				timeActualStamp = timeLeavingWork.getLeaveStamp();
			}
			if (!timeActualStamp.isPresent()) {
				timeActualStamp = Optional.of(new TimeActualStamp());
			}
			Optional<WorkStamp> workStamp = Optional.empty();
			
			if(actualStampAtr == ActualStampAtr.STAMP ) {
				workStamp = timeActualStamp.get().getStamp();
			}else if(actualStampAtr == ActualStampAtr.STAMP_REAL ) {
				workStamp = timeActualStamp.get().getActualStamp();
			}
			if(workStamp.isPresent()) {
				workStamp.get().getTimeDay().setTimeWithDay(Optional.of(timeWithDayAttr));
				workStamp.get().getTimeDay().getReasonTimeChange().setTimeChangeMeans(TimeChangeMeans.REAL_STAMP);
				workStamp.get().getTimeDay().getReasonTimeChange().setEngravingMethod(Optional.of(EngravingMethod.TIME_RECORD_ID_INPUT));
				workStamp.get().setLocationCode(Optional.ofNullable(timePrintDestinationOutput.getLocationCode()));
			}else {
				WorkStamp workStampNew = new WorkStamp();
				WorkTimeInformation timeDay = new WorkTimeInformation(
						new ReasonTimeChange(TimeChangeMeans.REAL_STAMP, Optional.of(EngravingMethod.TIME_RECORD_ID_INPUT)),
						timeWithDayAttr);
				workStampNew.setTimeDay(timeDay);
				workStampNew.setLocationCode(Optional.empty());
				workStamp = Optional.of(workStampNew);
				workStamp.get().setLocationCode(Optional.ofNullable(timePrintDestinationOutput.getLocationCode()));
				if(actualStampAtr == ActualStampAtr.STAMP ) {
					timeActualStamp.get().setStamp(workStamp);
				}else if(actualStampAtr == ActualStampAtr.STAMP_REAL ) {
					timeActualStamp.get().setActualStamp(workStamp);
				}
				
			}
			//?????????????????? (l??m tr??n ??????)
//			this.roundStamp(integrationOfDaily.getWorkInformation().getRecordInfo().getWorkTimeCode() !=null
//					?integrationOfDaily.getWorkInformation().getRecordInfo().getWorkTimeCode().v():null, workStamp.get(),
//					attendanceAtr, actualStampAtr);
			
			if(actualStampAtr == ActualStampAtr.STAMP ) {
				timeActualStamp.get().setStamp(workStamp);
			}else if(actualStampAtr == ActualStampAtr.STAMP_REAL ) {
				timeActualStamp.get().setActualStamp(workStamp);
			}
			//??????????????????????????????????????????????????????
			if(actualStampAtr == ActualStampAtr.STAMP_REAL ) {
				//???????????????????????????
				timeActualStamp.get().setOvertimeDeclaration(stamp.getRefActualResults().getOvertimeDeclaration());
			}
			
			if(attendanceAtr == AttendanceAtr.GOING_TO_WORK) {
				timeLeavingWork.setAttendanceStamp(timeActualStamp);
			}else {
				timeLeavingWork.setLeaveStamp(timeActualStamp);
			}
		}else {
			TimeActualStamp timeActualStamp = new TimeActualStamp();
			WorkStamp workStamp = new WorkStamp();
			WorkTimeInformation timeDay = new WorkTimeInformation(
					new ReasonTimeChange(TimeChangeMeans.REAL_STAMP, Optional.of(EngravingMethod.TIME_RECORD_ID_INPUT)),
					timeWithDayAttr);
			workStamp.setTimeDay(timeDay);
			workStamp.setLocationCode(Optional.empty());
			workStamp.setLocationCode(Optional.ofNullable(timePrintDestinationOutput.getLocationCode()));
			if(actualStampAtr == ActualStampAtr.STAMP ) {
				timeActualStamp.setStamp(Optional.of(workStamp));
			}else if(actualStampAtr == ActualStampAtr.STAMP_REAL ) {
				timeActualStamp.setActualStamp(Optional.of(workStamp));
			}
			
//				this.roundStamp(integrationOfDaily.getWorkInformation().getRecordInfo().getWorkTimeCode() !=null
//						? integrationOfDaily.getWorkInformation().getRecordInfo().getWorkTimeCode().v():null, workStamp,
//					attendanceAtr, actualStampAtr);
			//??????????????????????????????????????????????????????
			if(actualStampAtr == ActualStampAtr.STAMP_REAL ) {
				//???????????????????????????
				timeActualStamp.setOvertimeDeclaration(stamp.getRefActualResults().getOvertimeDeclaration());
			}
			
			List<TimeLeavingWork> timeLeavingWorks = new ArrayList<>();
			TimeLeavingWork timeLeavingWork = null;
			if(attendanceAtr == AttendanceAtr.GOING_TO_WORK) {
				timeLeavingWork = new TimeLeavingWork(new WorkNo(workNo), timeActualStamp, null);  //attendanceStamp
			}else {
				timeLeavingWork = new TimeLeavingWork(new WorkNo(workNo), null, timeActualStamp); //leaveStamp
			}
			timeLeavingWorks.add(timeLeavingWork);
			
			if(!integrationOfDaily.getAttendanceLeave().isPresent()) {
				TimeLeavingOfDailyAttd attendanceLeave = new TimeLeavingOfDailyAttd(timeLeavingWorks, new WorkTimes(0));
				integrationOfDaily.setAttendanceLeave(Optional.of(attendanceLeave));
			}else {
				integrationOfDaily.getAttendanceLeave().get().getTimeLeavingWorks().add(timeLeavingWork);
			}
			
		}
		
		//?????????????????????true????????? (?????????????????? = true)
		stamp.getImprintReflectionStatus().markAsReflected(integrationOfDaily.getYmd());
		return ReflectStampOuput.REFLECT;
	}
	
	/**
	 * ?????????????????? (new_2020)
	 */
	
//	public void roundStamp(String workTimeCode, WorkStamp workStamp,AttendanceAtr attendanceAtr,ActualStampAtr actualStampAtr) {
//		String companyId = AppContexts.user().companyId();
//		if (actualStampAtr == ActualStampAtr.STAMP) {
//			// ?????????????????????????????????????????????????????? (L???y ??????????????????)
//			RoundingSet roudingTime = workTimeCode != null ? this.getRoudingTime(companyId, workTimeCode,
//					attendanceAtr == AttendanceAtr.LEAVING_WORK ? Superiority.OFFICE_WORK : Superiority.ATTENDANCE)
//					: null;
//			
//			InstantRounding instantRounding = null;
//			if (roudingTime != null) {
//				instantRounding = new InstantRounding(roudingTime.getRoundingSet().getFontRearSection(),
//						roudingTime.getRoundingSet().getRoundingTimeUnit());
//			}
//			//????????????????????????????????? (L??m tr??n ????????????????????? )
//			if (instantRounding != null && workStamp.getTimeDay().getTimeWithDay().isPresent()) {
//				//block th???i gian theo e num ( 1,5,6,10,15,20,30,60)
//				int blockTime = new Integer(instantRounding.getRoundingTimeUnit().description).intValue();
//				//t???ng th???i gian tuy???n v??o
//				int numberMinuteTimeOfDay = workStamp.getTimeDay().getTimeWithDay().get().v().intValue();
//				//th???i gian d?? sau khi chia d?? cho block time
//				int modTimeOfDay = numberMinuteTimeOfDay % blockTime;
//				//thoi gian thay doi sau khi lam tron
//				int timeChange = 0;
//				//l??m tr??n l??n hay xu???ng
//				boolean isBefore = instantRounding.getFontRearSection() == FontRearSection.BEFORE;
//				if(isBefore) {
//					timeChange = (modTimeOfDay ==0)? numberMinuteTimeOfDay:numberMinuteTimeOfDay - modTimeOfDay;
//				}else {
//					timeChange = (modTimeOfDay ==0)? numberMinuteTimeOfDay:numberMinuteTimeOfDay - modTimeOfDay + blockTime;
//				}
//				//workStamp.getTimeDay().setTimeWithDay(Optional.of(new TimeWithDayAttr(timeChange)));
//				workStamp.setAfterRoundingTime(new TimeWithDayAttr(timeChange));
//			}//end : n???u time kh??c gi?? tr??? default
//		}
//	}
//	private RoundingSet getRoudingTime(String companyId, String workTimeCode, Superiority superiority) {
//		Optional<WorkTimezoneCommonSet> workTimezoneCommonSet = GetCommonSet.workTimezoneCommonSet(
//				requireService.createRequire(), companyId, workTimeCode);
//		if (workTimezoneCommonSet.isPresent()) {
//			WorkTimezoneStampSet stampSet = workTimezoneCommonSet.get().getStampSet();
//			return stampSet.getRoundingSets().stream().filter(item -> item.getSection() == superiority).findFirst().isPresent() ?
//					stampSet.getRoundingSets().stream().filter(item -> item.getSection() == superiority).findFirst().get() : null;
//		}
//		return null;
//	}
	
	
	/**
	 * ??????????????????????????? (new_2020)
	 */
	public void updateNumberStampReflect(ActualStampAtr actualStampAtr, Optional<TimeActualStamp> timeActualStamp) {
		if(!timeActualStamp.isPresent()) {
			return;
		}
		//??????????????????????????????????????????????????? (X??c nh???n param ?????????????????????)
		if (actualStampAtr == ActualStampAtr.STAMP_REAL) {
			//?????????????????????1????????? (S??? l???n ??????????????????(S??? l???n ph???n ??nh check tay) t??ng l??n 1)
			timeActualStamp.get().setPropertyTimeActualStamp(timeActualStamp.get().getActualStamp(), timeActualStamp.get().getStamp(),
					timeActualStamp.get().getNumberOfReflectionStamp() == null ? 1
							: timeActualStamp.get().getNumberOfReflectionStamp() + 1);
		}
	}
	
	public class RequireImpl implements WorkStamp.Require{

		@Override
		public Optional<StampReflectionManagement> findByCid(String companyId) {
			return timePriorityRepository.findByCid(companyId);
		}
		
		
	}
	
	public class RequireStampTypeImpl implements StampType.Require {
		
		@Override
		public Optional<WorkType> findByPK(String workTypeCd) {
			String companyId = AppContexts.user().companyId();
			return workTypeRepository.findByPK(companyId, workTypeCd);
		}

		@Override
		public Optional<WorkingConditionItem> getBySidAndStandardDate(String employeeId, GeneralDate ymd) {
			 return workingConditionItemRepository
					 	.getBySidAndStandardDate(employeeId, ymd);
		}

	}
}
