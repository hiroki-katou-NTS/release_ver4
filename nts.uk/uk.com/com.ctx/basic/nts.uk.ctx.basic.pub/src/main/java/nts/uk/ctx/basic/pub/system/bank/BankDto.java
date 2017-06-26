package nts.uk.ctx.basic.pub.system.bank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BankDto {
	/**
	 * Company code
	 */
	@Getter
	private String companyCode;

	/**
	 * Bank code
	 */
	@Getter
	private String bankCode;

	/**
	 * Bank name
	 */
	@Getter
	private String bankName;

	/**
	 * Bank name Katakana
	 */
	@Getter
	private String bankNameKana;

	/**
	 * Memo
	 */
	@Getter
	private String memo;
}
