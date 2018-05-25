package nts.uk.ctx.at.request.dom.application.common.adapter.workplace;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.arc.time.GeneralDate;

/**
 * 
 * @author Anh.Bd
 *
 */
@Value
@AllArgsConstructor
public class EmployeeBasicInfoImport {
	/** The p id. */
	// 個人ID
	private String pId;

	/** The emp id. */
	// 社員ID
	private String employeeId;

	/** The p name. */
	// 個人名
	private String pName;

	/** The gender. */
	// 性別
	private int gender;

	/** The birth day. */
	// 生年月日
	private GeneralDate birthDay;

	/** The p mail addr. */
	// 個人メールアドレス
	private String pMailAddr;

	/** The emp code. */
	// 社員コード
	private String employeeCode;

	/** The entry date. */
	// 入社年月日
	private GeneralDate entryDate;

	/** The retired date. */
	// 退職年月日
	private GeneralDate retiredDate;

	/** The company mail addr. */
	// 会社メールアドレス
	private String companyMailAddr;
}
