package nts.uk.ctx.at.shared.app.command.specialholiday;

import lombok.Value;
import nts.uk.ctx.at.shared.app.command.specialholiday.grantcondition.SpecialLeaveRestrictionCommand;
import nts.uk.ctx.at.shared.app.command.specialholiday.grantinformation.GrantRegularCommand;
import nts.uk.ctx.at.shared.app.command.specialholiday.periodinformation.GrantPeriodicCommand;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHoliday;
import nts.uk.ctx.at.shared.dom.specialholiday.TargetItem;
import nts.uk.ctx.at.shared.dom.specialholiday.grantcondition.AgeRange;
import nts.uk.ctx.at.shared.dom.specialholiday.grantcondition.AgeStandard;
import nts.uk.ctx.at.shared.dom.specialholiday.grantcondition.SpecialLeaveRestriction;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.FixGrantDate;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.GrantRegular;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.GrantTime;
import nts.uk.ctx.at.shared.dom.specialholiday.periodinformation.AvailabilityPeriod;
import nts.uk.ctx.at.shared.dom.specialholiday.periodinformation.GrantDeadline;
import nts.uk.ctx.at.shared.dom.specialholiday.periodinformation.SpecialVacationDeadline;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.time.calendar.MonthDay;

@Value
public class SpecialHolidayCommand {
	/** 会社ID */
	private String companyId;

	/** 特別休暇コード */
	private int specialHolidayCode;

	/** 特別休暇名称 */
	private String specialHolidayName;

	/** 付与情報 */
	private GrantRegularCommand regularCommand;

	/** 期限情報 */
	private GrantPeriodicCommand periodicCommand;

	/** 特別休暇利用条件 */
	private SpecialLeaveRestrictionCommand leaveResCommand;

	/** 対象項目 */
	private TargetItemCommand targetItemCommand;

	/**自動付与区分 */
	private int autoGrant;

	/** メモ */
	private String memo;

	public SpecialHoliday toDomain(String companyId) {
		return  SpecialHoliday.of(companyId,
				this.specialHolidayCode,
				this.specialHolidayName,
				this.toDomainGrantRegular(companyId),
				//this.toDomainGrantPeriodic(companyId),
				this.toDomainSpecLeaveRest(companyId),
				this.toDomainTargetItem(companyId),
				this.autoGrant,
				this.memo,
				NotUseAtr.NOT_USE);
	}

	private TargetItem toDomainTargetItem(String companyId2) {
		if(this.targetItemCommand == null) {
			return null;
		}

		return TargetItem.createFromJavaType(this.targetItemCommand.getAbsenceFrameNo(), this.targetItemCommand.getFrameNo());
	}

	private SpecialLeaveRestriction toDomainSpecLeaveRest(String companyId2) {
		if(this.leaveResCommand == null) {
			return null;
		}

		return SpecialLeaveRestriction.createFromJavaType(companyId, this.specialHolidayCode,
				this.leaveResCommand.getRestrictionCls(),
				this.leaveResCommand.getAgeLimit(),
				this.leaveResCommand.getGenderRest(),
				this.leaveResCommand.getRestEmp(),
				this.leaveResCommand.getListCls(),
				this.toDomainAgeStandard(),
				this.toDomainAgeRange(),
				this.leaveResCommand.getGender(),
				this.leaveResCommand.getListEmp());
	}

	private AgeRange toDomainAgeRange() {
		if(this.leaveResCommand.getAgeRange().getAgeHigherLimit() == null || this.leaveResCommand.getAgeRange().getAgeLowerLimit() == null) {
			return null;
		}

		return AgeRange.createFromJavaType(this.leaveResCommand.getAgeRange().getAgeLowerLimit(), this.leaveResCommand.getAgeRange().getAgeHigherLimit());
	}

	private AgeStandard toDomainAgeStandard() {
		if(this.leaveResCommand.getAgeStandard() == null) {
			return null;
		}

		MonthDay ageBaseDate = new MonthDay(this.leaveResCommand.getAgeStandard().getAgeBaseDate() / 100, this.leaveResCommand.getAgeStandard().getAgeBaseDate() % 100);

		return AgeStandard.createFromJavaType(this.leaveResCommand.getAgeStandard().getAgeCriteriaCls(), ageBaseDate);
	}

	private GrantDeadline toDomainGrantPeriodic(String companyId) {
		if(this.periodicCommand == null) {
			return null;
		}

		return GrantDeadline.createFromJavaType(
				this.periodicCommand.getTimeSpecifyMethod(),
				//new AvailabilityPeriod(this.periodicCommand.getAvailabilityPeriod().getStartDateValue(), this.periodicCommand.getAvailabilityPeriod().getEndDateValue()),
				//this.toDomainSpecialVacationDeadline(),
				this.periodicCommand.getExpirationDate().getYears(),
				this.periodicCommand.getExpirationDate().getMonths(),
				this.periodicCommand.getLimitCarryoverDays());
	}

//	private SpecialVacationDeadline toDomainSpecialVacationDeadline() {
//		if(this.periodicCommand.getExpirationDate() == null) {
//			return null;
//		}
//
//		return SpecialVacationDeadline.createFromJavaType(this.periodicCommand.getExpirationDate().getMonths(), this.periodicCommand.getExpirationDate().getYears());
//	}

	private GrantRegular toDomainGrantRegular(String companyId) {
		if(this.regularCommand == null) {
			return null;
		}

		//KMF004A 特別休暇情報の登録　実装時にこの処理を実装すること
		return new GrantRegular();
//		return GrantRegular.createFromJavaType(
//		companyId,
//		this.specialHolidayCode,
//		regularCommand.getTypeTime(),
//		regularCommand.getGrantDate(),
//		regularCommand.isAllowDisappear(),
//		this.toDomainGrantTime());
	}

	private GrantTime toDomainGrantTime() {
		if(this.regularCommand.getGrantTime() == null) {
			return null;
		}

		return GrantTime.createFromJavaType(this.toDomainFixGrantDate(), null);
	}

	private FixGrantDate toDomainFixGrantDate() {
		if(this.regularCommand.getGrantTime().getFixGrantDate() == null) {
			return null;
		}

		//KMF004A 特別休暇情報の登録　実装時にこの処理を実装すること
//		return new FixGrantDate();
//		return FixGrantDate.createFromJavaType(
//				this.regularCommand.getGrantTime().getFixGrantDate().getInterval(),
//				this.regularCommand.getGrantTime().getFixGrantDate().getGrantDays());
		return null;
	}
}
