package nts.uk.ctx.pr.core.dom.bank.branch.adapter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BasicBankBranchDto {
	/**
	 * Company code
	 */
	@Getter
	private String companyCode;

	/**
	 * Branch id
	 */
	@Getter
	private String branchId;

	/**
	 * Bank code
	 */
	@Getter
	private String bankCode;
	/**
	 * Bank branch code
	 */
	@Getter
	private String bankBranchCode;
	/**
	 * Bank branch name
	 */
	@Getter
	private String bankBranchName;
	/**
	 * Bank branch name katakana
	 */
	@Getter
	private String bankBranchNameKana;
	/**
	 * Memo
	 */
	@Getter
	private String memo;
}
