package nts.uk.ctx.pr.core.dom.payment.banktransfer.adapter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class BasicPersonBankAccountDto {
	@Getter
	private String companyCode;

	@Getter
	private String personID;

	@Getter
	private String histId;

	@Getter
	private int startYearMonth;

	@Getter
	private int endYearMonth;

	@Getter
	private BasicPersonUseSettingDto useSet1;

	@Getter
	private BasicPersonUseSettingDto useSet2;

	@Getter
	private BasicPersonUseSettingDto useSet3;

	@Getter
	private BasicPersonUseSettingDto useSet4;

	@Getter
	private BasicPersonUseSettingDto useSet5;

}
