package nts.uk.ctx.at.record.app.find.remainingnumber.specialleavegrant.finder;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRemainingData;
import nts.uk.shr.pereg.app.PeregEmployeeId;
import nts.uk.shr.pereg.app.PeregItem;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

@Getter
@Setter
public class SpecialLeaveGrantDto3 extends PeregDomainDto {

	private String specialid;
	private String cid;
	private int specialLeaCode;
	
	@PeregEmployeeId
	private String sid;

	// 付与日
	@PeregItem("IS00439")
	private GeneralDate grantDate;

	// 期限日
	@PeregItem("IS00440")
	private GeneralDate deadlineDate;

	// 期限切れ状態
	@PeregItem("IS00441")
	private int expStatus;

	// 使用状況
	@PeregItem("IS00442")
	private int registerType;

	// 付与日数
	@PeregItem("IS00444")
	private double numberDayGrant;

	// 付与時間
	@PeregItem("IS00445")
	private int timeGrant;

	// 使用日数
	@PeregItem("IS00447")
	private double numberDayUse;

	// 使用時間
	@PeregItem("IS00448")
	private int timeUse;

	//
	private double useSavingDays;

	// 上限超過消滅日数
	@PeregItem("IS00449")
	private double numberDaysOver;

	// 残日数
	@PeregItem("IS00452")
	private int timeOver;

	// 残時間
	@PeregItem("IS00453")
	private double numberDayRemain;

	// 上限超過消滅時間
	@PeregItem("IS00450")
	private int timeRemain;

	public static SpecialLeaveGrantDto3 createFromDomain(SpecialLeaveGrantRemainingData domain) {
		SpecialLeaveGrantDto3 dto = new SpecialLeaveGrantDto3();
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
