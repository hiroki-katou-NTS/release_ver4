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
public class SpecialLeaveGrantDto9 extends PeregDomainDto {

	private String specialid;
	private String cid;
	private int specialLeaCode;
	
	@PeregEmployeeId
	private String sid;

	// 付与日
	@PeregItem("IS00529")
	private GeneralDate grantDate;

	// 期限日
	@PeregItem("IS00530")
	private GeneralDate deadlineDate;

	// 期限切れ状態
	@PeregItem("IS00531")
	private int expStatus;

	// 使用状況
	@PeregItem("IS00532")
	private int registerType;

	// 付与日数
	@PeregItem("IS00534")
	private Double numberDayGrant;

	// 付与時間
	@PeregItem("IS00535")
	private Integer timeGrant;

	// 使用日数
	@PeregItem("IS00537")
	private Double numberDayUse;

	// 使用時間
	@PeregItem("IS00538")
	private Integer timeUse;

	//
	private Double useSavingDays;

	// 上限超過消滅日数
	@PeregItem("IS00539")
	private Double numberDaysOver;

	// 上限超過消滅時間
	@PeregItem("IS00540")
	private Integer timeOver;

	// 残時間
	@PeregItem("IS00542")
	private Double numberDayRemain;

	// 残日数
	@PeregItem("IS00543")
	private Integer timeRemain;

	public static SpecialLeaveGrantDto9 createFromDomain(SpecialLeaveGrantRemainingData domain) {
		SpecialLeaveGrantDto9 dto = new SpecialLeaveGrantDto9();
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
				: 0d;
		dto.numberDaysOver = domain.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().isPresent()
				? domain.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().get().getNumberOverDays().v()
				: 0d;
		dto.timeOver = (domain.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().isPresent()
				&& domain.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().get().getTimeOver().isPresent())
						? domain.getDetails().getUsedNumber().getSpecialLeaveOverLimitNumber().get().getTimeOver().get()
								.v()
						: 0;
		return dto;

	}
	
	public static SpecialLeaveGrantDto9 createFromDomain(Object[] domain) {
		SpecialLeaveGrantDto9 dto = new SpecialLeaveGrantDto9();
		dto.specialid = domain[0].toString();
		dto.cid = domain[1].toString();
		dto.sid = domain[2].toString();
		dto.specialLeaCode = (Integer)domain[3];
		dto.grantDate = (GeneralDate) domain[4];
		dto.deadlineDate = (GeneralDate) domain[5];
		dto.expStatus = (Integer) domain[6];
		dto.registerType = (Integer) domain[7];
		dto.numberDayGrant = (Double) domain[8];
		dto.timeGrant = domain[9] == null? 0: (Integer) domain[9];
		dto.numberDayRemain = (Double) domain[10];
		dto.timeRemain = domain[11] == null? 0: (Integer) domain[11];
		dto.numberDayUse = (Double) domain[12];
		dto.timeUse = domain[13] == null? 0: (Integer) domain[13];
		dto.useSavingDays = domain[14] == null? 0d : (Double) domain[14];
		dto.numberDaysOver =  domain[15] == null? 0d : (Double) domain[15];
		dto.timeOver = domain[16] == null? 0: (Integer) domain[16];;
		return dto;
	}

}
