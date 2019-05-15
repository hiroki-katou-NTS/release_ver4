package nts.uk.ctx.at.record.dom.monthlyprocess.aggr;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.affiliation.AffiliationInfoOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.erroralarm.EmployeeMonthlyPerError;
import nts.uk.ctx.at.record.dom.monthly.information.care.MonCareHdRemain;
import nts.uk.ctx.at.record.dom.monthly.information.childnursing.MonChildHdRemain;
import nts.uk.ctx.at.record.dom.monthly.remarks.RemarksMonthlyRecord;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.AbsenceLeaveRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.MonthlyDayoffRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.RsvLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialHolidayRemainData;
import nts.uk.ctx.at.record.dom.weekly.AttendanceTimeOfWeekly;

/**
 * 月別実績(Work)
 * @author shuichi_ishida
 */
@Getter
public class IntegrationOfMonthly {

	/** 月別実績の勤怠時間 */
	@Setter
	private Optional<AttendanceTimeOfMonthly> attendanceTime;
	/** 月別実績の所属情報 */
	@Setter
	private Optional<AffiliationInfoOfMonthly> affiliationInfo;
	/** 月別実績の任意項目 */
	private List<AnyItemOfMonthly> anyItemList;
	/** 管理期間の36協定時間 */
	private List<AgreementTimeOfManagePeriod> agreementTimeList;
	/** 年休月別残数データ */
	@Setter
	private Optional<AnnLeaRemNumEachMonth> annualLeaveRemain;
	/** 積立年休残数月別データ */
	@Setter
	private Optional<RsvLeaRemNumEachMonth> reserveLeaveRemain;
	/** 振休月別残数データ */
	@Setter
	private Optional<AbsenceLeaveRemainData> absenceLeaveRemain;
	/** 代休残数月別データ */
	@Setter
	private Optional<MonthlyDayoffRemainData> monthlyDayoffRemain;
	/** 特別休暇残数月別データ */
	@Setter
	private List<SpecialHolidayRemainData> specialLeaveRemainList;
	/** 週別実績の勤怠時間 */
	private List<AttendanceTimeOfWeekly> attendanceTimeOfWeekList;
	/** 社員の月別実績エラー一覧 */
	private List<EmployeeMonthlyPerError> employeeMonthlyPerErrorList;
	/** 月別実績の備考 */
	@Setter
	private List<RemarksMonthlyRecord> remarks;
	/** 介護休暇月別残数データ */
	@Setter
	private Optional<MonCareHdRemain> care;
	/** 子の看護月別残数データ */
	@Setter
	private Optional<MonChildHdRemain> childCare;
	
	public IntegrationOfMonthly(){
		this.attendanceTime = Optional.empty();
		this.affiliationInfo = Optional.empty();
		this.anyItemList = new ArrayList<>();
		this.agreementTimeList = new ArrayList<>();
		this.annualLeaveRemain = Optional.empty();
		this.reserveLeaveRemain = Optional.empty();
		this.absenceLeaveRemain = Optional.empty();
		this.monthlyDayoffRemain = Optional.empty();
		this.specialLeaveRemainList = new ArrayList<>();
		this.attendanceTimeOfWeekList = new ArrayList<>();
		this.employeeMonthlyPerErrorList = new ArrayList<>();
		this.remarks = new ArrayList<>();
		this.care = Optional.empty();
		this.childCare = Optional.empty();
	}
	
	/**
	 * コンストラクタ
	 * @param attendanceTime 月別実績の勤怠時間
	 * @param affiliationInfo 月別実績の所属情報
	 * @param anyItemList 月別実績の任意項目
	 * @param agreementTime 管理期間の36協定時間
	 * @param annualLeaveRemain 年休月別残数データ
	 * @param reserveLeaveRemain 積立年休月別残数データ
	 * @param absenceLeaveRemain 振休月別残数データ
	 * @param monthlyDayoffRemain 代休月別残数データ
	 * @param specialLeaveRemainList 特別休暇月別残数データ
	 * @param remarks 月別実績の備考
	 * @param care 介護休暇月別残数データ
	 * @param childCare 子の看護月別残数データ
	 */
	public IntegrationOfMonthly(
			Optional<AttendanceTimeOfMonthly> attendanceTime,
			Optional<AffiliationInfoOfMonthly> affiliationInfo,
			List<AnyItemOfMonthly> anyItemList,
			Optional<AgreementTimeOfManagePeriod> agreementTime,
			Optional<AnnLeaRemNumEachMonth> annualLeaveRemain,
			Optional<RsvLeaRemNumEachMonth> reserveLeaveRemain,
			Optional<AbsenceLeaveRemainData> absenceLeaveRemain,
			Optional<MonthlyDayoffRemainData> monthlyDayoffRemain,
			List<SpecialHolidayRemainData> specialLeaveRemainList,
			List<RemarksMonthlyRecord> remarks,
			Optional<MonCareHdRemain> care,
			Optional<MonChildHdRemain> childCare){
	
		this.attendanceTime = attendanceTime;
		this.affiliationInfo = affiliationInfo;
		this.anyItemList = anyItemList;
		this.agreementTimeList = new ArrayList<>();
		if (agreementTime.isPresent()) this.agreementTimeList.add(agreementTime.get()); 
		this.annualLeaveRemain = annualLeaveRemain;
		this.reserveLeaveRemain = reserveLeaveRemain;
		this.absenceLeaveRemain = absenceLeaveRemain;
		this.monthlyDayoffRemain = monthlyDayoffRemain;
		this.specialLeaveRemainList = specialLeaveRemainList;
		this.remarks = remarks;
		this.care = care;
		this.childCare = childCare;
	}
	
	/**
	 * コンストラクタ
	 * @param attendanceTime 月別実績の勤怠時間
	 * @param affiliationInfo 月別実績の所属情報
	 * @param anyItemList 月別実績の任意項目
	 * @param agreementTimeList 管理期間の36協定時間
	 * @param annualLeaveRemain 年休月別残数データ
	 * @param reserveLeaveRemain 積立年休月別残数データ
	 * @param absenceLeaveRemain 振休月別残数データ
	 * @param monthlyDayoffRemain 代休月別残数データ
	 * @param specialLeaveRemainList 特別休暇月別残数データ
	 * @param remarks 月別実績の備考
	 * @param care 介護休暇月別残数データ
	 * @param childCare 子の看護月別残数データ
	 */
	public IntegrationOfMonthly(
			Optional<AttendanceTimeOfMonthly> attendanceTime,
			Optional<AffiliationInfoOfMonthly> affiliationInfo,
			List<AnyItemOfMonthly> anyItemList,
			List<AgreementTimeOfManagePeriod> agreementTimeList,
			Optional<AnnLeaRemNumEachMonth> annualLeaveRemain,
			Optional<RsvLeaRemNumEachMonth> reserveLeaveRemain,
			Optional<AbsenceLeaveRemainData> absenceLeaveRemain,
			Optional<MonthlyDayoffRemainData> monthlyDayoffRemain,
			List<SpecialHolidayRemainData> specialLeaveRemainList,
			List<RemarksMonthlyRecord> remarks,
			Optional<MonCareHdRemain> care,
			Optional<MonChildHdRemain> childCare){
	
		this.attendanceTime = attendanceTime;
		this.affiliationInfo = affiliationInfo;
		this.anyItemList = anyItemList;
		this.agreementTimeList = agreementTimeList;
		this.annualLeaveRemain = annualLeaveRemain;
		this.reserveLeaveRemain = reserveLeaveRemain;
		this.absenceLeaveRemain = absenceLeaveRemain;
		this.monthlyDayoffRemain = monthlyDayoffRemain;
		this.specialLeaveRemainList = specialLeaveRemainList;
		this.remarks = remarks;
		this.care = care;
		this.childCare = childCare;
	}
}
