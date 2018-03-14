package nts.uk.ctx.at.shared.app.find.specialholiday;

import lombok.Data;

@Data
public class GrantSingleDto {
	/* 会社ID */
	private String companyId;

	/* 特別休暇コード */
	private int specialHolidayCode;

	/* 種類 */
	private int grantDaySingleType;

	/* 固定付与日数 */
	private Integer fixNumberDays;

	/* 忌引とする */
	private int makeInvitation;

	/* 休日除外区分 */
	private int holidayExclusionAtr;
}
