package nts.uk.ctx.at.shared.app.find.remainingnumber.specialleavegrant.finder;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.shr.pereg.app.PeregEmployeeId;
import nts.uk.shr.pereg.app.PeregItem;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

@Getter
@Setter
public class SpecialLeaveGrantDto18 extends PeregDomainDto {

	private String specialid;
	private String cid;
	private int specialLeaCode;
	
	@PeregEmployeeId
	private String sid;

	// 付与日
	@PeregItem("IS00734")
	private GeneralDate grantDate;

	// 期限日
	@PeregItem("IS00735")
	private GeneralDate deadlineDate;

	// 期限切れ状態
	@PeregItem("IS00736")
	private int expStatus;

	// 使用状況
	@PeregItem("IS00737")
	private int registerType;

	// 付与日数
	@PeregItem("IS00739")
	private double numberDayGrant;

	// 付与時間
	@PeregItem("IS00740")
	private int timeGrant;

	// 使用日数
	@PeregItem("IS00742")
	private double numberDayUse;

	// 使用時間
	@PeregItem("IS00743")
	private int timeUse;

	//
	private double useSavingDays;

	// 上限超過消滅日数
	@PeregItem("IS00744")
	private double numberDaysOver;

	// 残日数
	@PeregItem("IS00747")
	private int timeOver;

	// 残時間
	@PeregItem("IS00748")
	private double numberDayRemain;

	// 上限超過消滅時間
	@PeregItem("IS00745")
	private int timeRemain;

	public static SpecialLeaveGrantDto18 createFromDomain(SpecialLeaveGrantRemainingData domain) {
		SpecialLeaveGrantDto18 dto = new SpecialLeaveGrantDto18();
		dto.specialid = domain.getSpecialId();
		dto.cid = domain.getCId();
		dto.sid = domain.getEmployeeId();
		dto.specialLeaCode = domain.getSpecialLeaveCode().v();
		dto.grantDate = domain.getGrantDate();
		dto.deadlineDate = domain.getDeadlineDate();
		dto.expStatus = domain.getExpirationStatus().value;
		dto.registerType = domain.getRegisterType().value;
		dto.numberDayGrant = domain.getDetails().getGrantNumber().getDayNumberOfGrant().v();
		dto.timeGrant = domain.getDetails().getGrantNumber().getTimeOfGrant().isPresent()
				? domain.getDetails().getGrantNumber().getTimeOfGrant().get().v()
				: 0;
		dto.numberDayRemain = domain.getDetails().getRemainingNumber().getDayNumberOfRemain().v();
		dto.timeRemain = domain.getDetails().getRemainingNumber().getTimeOfRemain().isPresent()
				? domain.getDetails().getRemainingNumber().getTimeOfRemain().get().v()
				: 0;
		dto.numberDayUse = domain.getDetails().getUsedNumber().getDayNumberOfUse().v();
		dto.timeUse = domain.getDetails().getUsedNumber().getTimeOfUse().isPresent()
				? domain.getDetails().getUsedNumber().getTimeOfUse().get().v()
				: 0;
		dto.useSavingDays = domain.getDetails().getUsedNumber().getUseSavingDays().isPresent()
				? domain.getDetails().getUsedNumber().getUseSavingDays().get().v()
				: 0;
		dto.numberDaysOver = domain.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().isPresent()
				? domain.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().get().getNumberOverDays().v()
				: 0;
		dto.timeOver = (domain.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().isPresent()
				&& domain.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().get().getTimeOver().isPresent())
						? domain.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().get().getTimeOver().get()
								.v()
						: 0;
		return dto;

	}

}
