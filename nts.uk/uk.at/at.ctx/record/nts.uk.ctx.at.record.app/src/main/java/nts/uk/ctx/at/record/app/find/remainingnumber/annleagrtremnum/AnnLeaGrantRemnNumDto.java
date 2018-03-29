package nts.uk.ctx.at.record.app.find.remainingnumber.annleagrtremnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.remainingnumber.annualleave.empinfo.grantremainingdata.AnnualLeaveGrantRemainingData;
import nts.uk.shr.pereg.app.PeregItem;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AnnLeaGrantRemnNumDto extends PeregDomainDto {

	/**
	 * 年休付与日
	 */
	@PeregItem("IS00385")
	private GeneralDate grantDate;

	/**
	 * 年休期限日
	 */
	@PeregItem("IS00386")
	private GeneralDate deadline;

	/**
	 * 年休期限切れ状態
	 */
	@PeregItem("IS00387")
	private int expirationStatus;

	/**
	 * 年休使用状況
	 */
	// @PeregItem("IS00388")
	// private String useStatus;

	/**
	 * 付与数
	 */
	// @PeregItem("IS00389")
	// private String grantNumber;

	/**
	 * 付与日数
	 */
	@PeregItem("IS00390")
	private Double grantDays;

	/**
	 * 付与時間
	 */
	@PeregItem("IS00391")
	private int grantMinutes;

	/**
	 * 使用数
	 */
	// @PeregItem("IS00392")
	// private String usedNumber;

	/**
	 * 使用日数
	 */
	@PeregItem("IS00393")
	private Double usedDays;

	/**
	 * 使用時間
	 */
	@PeregItem("IS00394")
	private int usedMinutes;

	/**
	 * 残数
	 */
	// @PeregItem("IS00395")
	// private String remainingNumber;

	/**
	 * 残日数
	 */
	@PeregItem("IS00396")
	private Double remainingDays;

	/**
	 * 残時間
	 */
	@PeregItem("IS00397")
	private int remainingMinutes;

	public static AnnLeaGrantRemnNumDto createFromDomain(AnnualLeaveGrantRemainingData domain) {
		AnnLeaGrantRemnNumDto dto = new AnnLeaGrantRemnNumDto();
		dto.grantDate = domain.getGrantDate();
		dto.deadline = domain.getDeadline();
		dto.expirationStatus = domain.getExpirationStatus().value;
		dto.grantDays = domain.getDetails().getGrantNumber().getDays().v();
		dto.grantMinutes = domain.getDetails().getGrantNumber().getMinutes().isPresent()
				? domain.getDetails().getGrantNumber().getMinutes().get().v() : 0;
		dto.usedDays = domain.getDetails().getUsedNumber().getDays().v();
		dto.usedMinutes = domain.getDetails().getUsedNumber().getMinutes().isPresent()
				? domain.getDetails().getUsedNumber().getMinutes().get().v() : 0;
		dto.remainingDays = domain.getDetails().getRemainingNumber().getDays().v();
		dto.remainingMinutes = domain.getDetails().getRemainingNumber().getMinutes().isPresent()
				? domain.getDetails().getRemainingNumber().getMinutes().get().v() : 0;
		return dto;
	}

}
