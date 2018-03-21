package nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.grantremainingdata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.remainingnumber.base.GrantRemainRegisterType;
import nts.uk.ctx.at.record.dom.remainingnumber.base.LeaveExpirationStatus;
import nts.uk.ctx.at.record.dom.remainingnumber.base.SpecialVacationCD;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// 特別休暇付与残数データ
public class SpecialLeaveGrantRemainingData extends AggregateRoot {

	// 特別休暇ID
	private String specialId;
	// 社員ID
	private String employeeId;
	// 特別休暇コード
	private SpecialVacationCD specialLeaveCode;
	// 付与日
	private GeneralDate grantDate;
	// 期限日
	private GeneralDate deadlineDate;
	// 期限切れ状態
	private LeaveExpirationStatus expirationStatus;
	// 登録種別
	private GrantRemainRegisterType registerType;
	// 明細
	private SpecialLeaveNumberInfo details;

	public static SpecialLeaveGrantRemainingData createFromJavaType(String specialId,String employeeId, int specialLeaveCode,
			GeneralDate grantDate, GeneralDate deadlineDate, int expirationStatus, int registerType,
			int dayNumberOfGrant, Integer timeOfGrant, Double dayNumberOfUse, Integer timeOfUse,
			Double numberOfDayUseToLose, int dayNumberOfExeeded, Integer timeOfExeeded, Double dayNumberOfRemain,
			Integer timeOfRemain) {
		SpecialLeaveGrantRemainingData domain = new SpecialLeaveGrantRemainingData();
		domain.specialId = specialId;
		domain.employeeId = employeeId;
		domain.specialLeaveCode = new SpecialVacationCD(specialLeaveCode);
		domain.grantDate = grantDate;
		domain.deadlineDate = deadlineDate;
		domain.expirationStatus = EnumAdaptor.valueOf(expirationStatus, LeaveExpirationStatus.class);
		domain.registerType = EnumAdaptor.valueOf(registerType, GrantRemainRegisterType.class);

		domain.details = new SpecialLeaveNumberInfo(dayNumberOfGrant, timeOfGrant, dayNumberOfUse, timeOfUse,
				numberOfDayUseToLose, dayNumberOfExeeded, timeOfExeeded, dayNumberOfRemain, timeOfRemain);

		return domain;
	}

}
