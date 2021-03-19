package nts.uk.ctx.at.shared.app.find.remainingnumber.rervleagrtremnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.daynumber.LeaveNumberInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.ReserveLeaveGrantRemainingData;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.empinfo.grantremainingdata.ReserveLeaveNumberInfo;
import nts.uk.shr.pereg.app.PeregItem;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResvLeaGrantRemNumDto {

	private String id;
	/**
	 * 積立年休付与日
	 */
	@PeregItem("IS00398")
	private GeneralDate grantDate;

	/**
	 * 積立年休期限日
	 */
	@PeregItem("IS00399")
	private GeneralDate deadline;

	/**
	 * 積立年休期限切れ状態
	 */
	@PeregItem("IS00400")
	private int expirationStatus;

	/**
	 * 積立年休使用状況
	 */
	//@PeregItem("IS00401")
	//private String useStatus;

	/**
	 * 付与数
	 */
	@PeregItem("IS00403")
	private Double grantDays;

	/**
	 * 使用数
	 */
	//@PeregItem("IS00404")
	//private String useNumber;

	/**
	 * 使用日数
	 */
	@PeregItem("IS00405")
	private Double useDays;

	/**
	 * 上限超過消滅日数
	 */
	@PeregItem("IS00406")
	private Double overLimitDays;

	/**
	 * 残数
	 */
	@PeregItem("IS00408")
	private Double remainingDays;

	public static ResvLeaGrantRemNumDto createFromDomain(ReserveLeaveGrantRemainingData domain) {
		ResvLeaGrantRemNumDto dto = new ResvLeaGrantRemNumDto();
		dto.id = domain.getLeaveID();
		dto.grantDate = domain.getGrantDate();
		dto.deadline = domain.getDeadline();
		dto.expirationStatus = domain.getExpirationStatus().value;
		LeaveNumberInfo details = domain.getDetails();
		dto.grantDays = details.getGrantNumber().getDays().v();
		dto.useDays = details.getUsedNumber().getDays().v();
		dto.overLimitDays = details.getUsedNumber().getLeaveOverLimitNumber().isPresent()
				? details.getUsedNumber().getLeaveOverLimitNumber().get().numberOverDays.v() : 0d;
		dto.remainingDays = details.getRemainingNumber().getDays().v();
		return dto;
	}


}
