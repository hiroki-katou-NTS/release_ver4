package nts.uk.ctx.pr.core.dom.bank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BasicBankDto {
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
