/******************************************************************
 * Copyright (c) 2018 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.file.at.app.export.monthlyschedule;

import lombok.Data;
import nts.uk.file.at.app.export.dailyschedule.WorkScheduleSettingTotalOutput;

/**
 * The Class MonthlyWorkScheduleCondition.
 */
@Data
public class MonthlyWorkScheduleCondition {
	
	public static final int EXPORT_BY_EMPLOYEE = 0;
	public static final int EXPORT_BY_DATE = 1;
	
	public static final int PAGE_BREAK_NOT_USE = 0;
	public static final int PAGE_BREAK_EMPLOYEE = 1;
	public static final int PAGE_BREAK_WORKPLACE = 2;
	

	/** The company id. */
	// 会社ID
	private String companyId;

	/** The user id. */
	// ユーザID
	private String userId;

	/** The output type. */
	// 帳票出力種類
	private Integer outputType;

	/** The page break indicator. */
	// 改ページ区分
	private Integer pageBreakIndicator;

	/** The total output setting. */
	// 月別勤務表用明細・合計出力設定
	private WorkScheduleSettingTotalOutput totalOutputSetting;
	
	/**
	 * Checks if is show workplace.
	 *
	 * @return the boolean
	 */
	public Boolean isShowWorkplace() {
		return this.totalOutputSetting.isDetails() || this.totalOutputSetting.isWorkplaceTotal()
				|| this.totalOutputSetting.isCumulativeWorkplace() || this.totalOutputSetting.isPersonalTotal();
	}

	/**
	 * Checks if is show personal.
	 *
	 * @return the boolean
	 */
	public Boolean isShowPersonal() {
		return this.totalOutputSetting.isDetails() || this.totalOutputSetting.isPersonalTotal();
	}
}
