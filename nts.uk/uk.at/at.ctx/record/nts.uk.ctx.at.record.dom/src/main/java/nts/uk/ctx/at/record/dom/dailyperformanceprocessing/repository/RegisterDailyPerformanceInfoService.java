package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.affiliationinformation.AffiliationInforOfDailyPerfor;
import nts.uk.ctx.at.record.dom.affiliationinformation.WorkTypeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.AffiliationInforOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.affiliationinformation.repository.WorkTypeOfDailyPerforRepository;
import nts.uk.ctx.at.record.dom.breakorgoout.BreakTimeOfDailyPerformance;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.BreakTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.breakorgoout.repository.OutingTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.calculationattribute.CalAttrOfDailyPerformance;
import nts.uk.ctx.at.record.dom.calculationattribute.repo.CalAttrOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.AttendanceLeavingGateOfDailyRepo;
import nts.uk.ctx.at.record.dom.daily.attendanceleavinggate.repo.PCLogOnInfoOfDailyRepo;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.output.ReflectStampOutput;
import nts.uk.ctx.at.record.dom.raisesalarytime.SpecificDateAttrOfDailyPerfor;
import nts.uk.ctx.at.record.dom.raisesalarytime.repo.SpecificDateAttrOfDailyPerforRepo;
import nts.uk.ctx.at.record.dom.shorttimework.repo.ShortTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.stamp.StampRepository;
import nts.uk.ctx.at.record.dom.workinformation.WorkInfoOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workinformation.service.updateworkinfo.InsertWorkInfoOfDailyPerforService;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.algorithm.CreateEmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.worktime.repository.TemporaryTimeOfDailyPerformanceRepository;
import nts.uk.ctx.at.record.dom.worktime.repository.TimeLeavingOfDailyPerformanceRepository;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Stateless
public class RegisterDailyPerformanceInfoService {

	@Inject
	private AffiliationInforOfDailyPerforRepository affiliationInforOfDailyPerforRepository;

	@Inject
	private TimeLeavingOfDailyPerformanceRepository timeLeavingOfDailyPerformanceRepository;

	@Inject
	private TemporaryTimeOfDailyPerformanceRepository temporaryTimeOfDailyPerformanceRepository;

	@Inject
	private BreakTimeOfDailyPerformanceRepository breakTimeOfDailyPerformanceRepository;

	@Inject
	private OutingTimeOfDailyPerformanceRepository outingTimeOfDailyPerformanceRepository;

	@Inject
	private StampRepository stampRepository;

	@Inject
	private InsertWorkInfoOfDailyPerforService insertWorkInfoOfDailyPerforService;

	@Inject
	private SpecificDateAttrOfDailyPerforRepo specificDateAttrOfDailyPerforRepo;

	@Inject
	private CalAttrOfDailyPerformanceRepository calAttrOfDailyPerformanceRepository;

	@Inject
	private AttendanceLeavingGateOfDailyRepo attendanceLeavingGateOfDailyRepo;

	@Inject
	private PCLogOnInfoOfDailyRepo pCLogOnInfoOfDailyRepo;

	@Inject
	private WorkTypeOfDailyPerforRepository workTypeOfDailyPerforRepository;

	@Inject
	private ShortTimeOfDailyPerformanceRepository shortTimeOfDailyPerformanceRepository;
	
	@Inject
	private CreateEmployeeDailyPerError createEmployeeDailyPerError;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registerDailyPerformanceInfo(String companyId, String employeeID, GeneralDate day,
			ReflectStampOutput stampOutput, AffiliationInforOfDailyPerfor affiliationInforOfDailyPerfor,
			WorkInfoOfDailyPerformance workInfoOfDailyPerformanceUpdate,
			SpecificDateAttrOfDailyPerfor specificDateAttrOfDailyPerfor,
			CalAttrOfDailyPerformance calAttrOfDailyPerformance, WorkTypeOfDailyPerformance workTypeOfDailyPerformance,
			BreakTimeOfDailyPerformance breakTimeOfDailyPerformance) {

		// 登録する - register - activity ⑤社員の日別実績を作成する
		// ドメインモデル「日別実績の勤務情報」を更新する - update
		// WorkInfoOfDailyPerformance - JDBC
		if (workInfoOfDailyPerformanceUpdate != null) {
			this.insertWorkInfoOfDailyPerforService.updateWorkInfoOfDailyPerforService(companyId, employeeID, day,
					workInfoOfDailyPerformanceUpdate);
		}

		// ドメインモデル「日別実績の勤務種別」を更新する (Update domain 「日別実績の勤務種別」)
		// workTypeOfDailyPerformance
		if (workTypeOfDailyPerformance != null) {
			if (this.workTypeOfDailyPerforRepository.findByKey(employeeID, day).isPresent()) {
				this.workTypeOfDailyPerforRepository.update(workTypeOfDailyPerformance);
			} else {
				this.workTypeOfDailyPerforRepository.add(workTypeOfDailyPerformance);
			}
		}

		// ドメインモデル「日別実績の所属情報」を更新する - update
		// AffiliationInforOfDailyPerformance
		if (affiliationInforOfDailyPerfor != null) {
			if (this.affiliationInforOfDailyPerforRepository.findByKey(employeeID, day).isPresent()) {
				this.affiliationInforOfDailyPerforRepository.updateByKey(affiliationInforOfDailyPerfor);
			} else {
				this.affiliationInforOfDailyPerforRepository.add(affiliationInforOfDailyPerfor);
			}
		}

		// ドメインモデル「日別実績の休憩時間帯」を更新する
		// BreakTimeOfDailyPerformance
		if (breakTimeOfDailyPerformance != null && !breakTimeOfDailyPerformance.getBreakTimeSheets().isEmpty()) {
			if (this.breakTimeOfDailyPerformanceRepository.find(employeeID, day, 1).isPresent()) {
				this.breakTimeOfDailyPerformanceRepository.updateForEachOfType(breakTimeOfDailyPerformance);
			} else {
				this.breakTimeOfDailyPerformanceRepository.insert(breakTimeOfDailyPerformance);
			}
		}

		// ドメインモデル「日別実績の特定日区分」を更新する (Update 「日別実績の特定日区分」)
		// specificDateAttrOfDailyPerfor - JDBC - new wave
		if (specificDateAttrOfDailyPerfor != null) {
			if (this.specificDateAttrOfDailyPerforRepo.find(employeeID, day).isPresent()) {
				this.specificDateAttrOfDailyPerforRepo.update(specificDateAttrOfDailyPerfor);
			} else {
				this.specificDateAttrOfDailyPerforRepo.add(specificDateAttrOfDailyPerfor);
			}
		}

		// ドメインモデル「日別実績の計算区分」を更新する (Update 「日別実績の計算区分」)
		// calAttrOfDailyPerformance - JDBC newwave
		if (calAttrOfDailyPerformance != null) {
			if (this.calAttrOfDailyPerformanceRepository.find(employeeID, day) != null) {
				this.calAttrOfDailyPerformanceRepository.update(calAttrOfDailyPerformance);
			} else {
				this.calAttrOfDailyPerformanceRepository.add(calAttrOfDailyPerformance);
			}
		}

		if (stampOutput != null) {

			// breakTimeOfDailyPerformance - JDBC only insert
			if (stampOutput.getBreakTimeOfDailyPerformance() != null
					&& !stampOutput.getBreakTimeOfDailyPerformance().getBreakTimeSheets().isEmpty()) {
				if (this.breakTimeOfDailyPerformanceRepository.find(employeeID, day, 0).isPresent()) {
					this.breakTimeOfDailyPerformanceRepository.updateForEachOfType(stampOutput.getBreakTimeOfDailyPerformance());
				} else {
					this.breakTimeOfDailyPerformanceRepository.insert(stampOutput.getBreakTimeOfDailyPerformance());
				}
			}

			// ドメインモデル「日別実績の外出時間帯」を更新する (Update 「日別実績の外出時間帯」) - JDBC only insert
			if (stampOutput.getOutingTimeOfDailyPerformance() != null) {
				if (this.outingTimeOfDailyPerformanceRepository.findByEmployeeIdAndDate(employeeID, day).isPresent()) {
					this.outingTimeOfDailyPerformanceRepository.update(stampOutput.getOutingTimeOfDailyPerformance());
				} else {
					this.outingTimeOfDailyPerformanceRepository.add(stampOutput.getOutingTimeOfDailyPerformance());
				}
			}

			// ドメインモデル「日別実績の臨時出退勤」を更新する (Update 「日別実績の臨時出退勤」) - JDBC - tín
			if (stampOutput.getTemporaryTimeOfDailyPerformance() != null) {
				if (this.temporaryTimeOfDailyPerformanceRepository.findByKey(employeeID, day).isPresent()) {
					this.temporaryTimeOfDailyPerformanceRepository
							.update(stampOutput.getTemporaryTimeOfDailyPerformance());
				} else {
					this.temporaryTimeOfDailyPerformanceRepository
							.insert(stampOutput.getTemporaryTimeOfDailyPerformance());
				}
			}

			// ドメインモデル「日別実績の出退勤」を更新する (Update 「日別実績の出退勤」) - JDBC only insert - tín
			if (stampOutput.getTimeLeavingOfDailyPerformance() != null
					&& stampOutput.getTimeLeavingOfDailyPerformance().getTimeLeavingWorks() != null
					&& !stampOutput.getTimeLeavingOfDailyPerformance().getTimeLeavingWorks().isEmpty()) {
				if (this.timeLeavingOfDailyPerformanceRepository.findByKey(employeeID, day).isPresent()) {
					this.timeLeavingOfDailyPerformanceRepository.update(stampOutput.getTimeLeavingOfDailyPerformance());
				} else {
					this.timeLeavingOfDailyPerformanceRepository.insert(stampOutput.getTimeLeavingOfDailyPerformance());
				}
			}
			// - JDBC - newwave
			if (stampOutput.getShortTimeOfDailyPerformance() != null) {
				if (this.shortTimeOfDailyPerformanceRepository.find(employeeID, day).isPresent()) {
					this.shortTimeOfDailyPerformanceRepository
							.updateByKey(stampOutput.getShortTimeOfDailyPerformance());
				} else {
					this.shortTimeOfDailyPerformanceRepository.insert(stampOutput.getShortTimeOfDailyPerformance());
				}
			}

			// ドメインモデル「打刻」を更新する (Update 「打刻」) - JDBC
			if (stampOutput.getLstStamp() != null && !stampOutput.getLstStamp().isEmpty()) {
				stampOutput.getLstStamp().forEach(stampItem -> {
					if(stampItem.getAttendanceTime().v() >= 1440) {
						stampItem.setAttendanceTime(new AttendanceTime(stampItem.getAttendanceTime().v() - 1440));
					}
					this.stampRepository.updateStampItem(stampItem);
				});
			}

			// ドメインモデル「日別実績の入退門」を更新する (Update 「日別実績の入退門」) - JDBC only insert - tín
			if (stampOutput.getAttendanceLeavingGateOfDaily() != null
					&& stampOutput.getAttendanceLeavingGateOfDaily().getAttendanceLeavingGates() != null
					&& !stampOutput.getAttendanceLeavingGateOfDaily().getAttendanceLeavingGates().isEmpty()) {
				if (this.attendanceLeavingGateOfDailyRepo.find(employeeID, day).isPresent()) {
					this.attendanceLeavingGateOfDailyRepo.update(stampOutput.getAttendanceLeavingGateOfDaily());
				} else {
					this.attendanceLeavingGateOfDailyRepo.add(stampOutput.getAttendanceLeavingGateOfDaily());
				}
			}

			// ドメインモデル「日別実績のPCログオン情報」を更新する (Update 「日別実績のPCログオン情報」)) - JDBC only insert - tín
			if (stampOutput.getPcLogOnInfoOfDaily() != null
					&& stampOutput.getPcLogOnInfoOfDaily().getLogOnInfo() != null
					&& !stampOutput.getPcLogOnInfoOfDaily().getLogOnInfo().isEmpty()) {
				if (this.pCLogOnInfoOfDailyRepo.find(employeeID, day).isPresent()) {
					this.pCLogOnInfoOfDailyRepo.update(stampOutput.getPcLogOnInfoOfDaily());
				} else {
					this.pCLogOnInfoOfDailyRepo.add(stampOutput.getPcLogOnInfoOfDaily());
				}
			}
			
			if (stampOutput.getEmployeeDailyPerErrorList() != null && !stampOutput.getEmployeeDailyPerErrorList().isEmpty()) {
				for(EmployeeDailyPerError dailyPerError : stampOutput.getEmployeeDailyPerErrorList()){
					if (dailyPerError != null) {
						this.createEmployeeDailyPerError.createEmployeeError(dailyPerError);
					}
					
				}
			}
		}
	}

}
