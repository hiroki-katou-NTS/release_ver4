package nts.uk.screen.at.app.query.ksu.ksu001q;

import lombok.Data;

/**
 * 
 * @author thanhlv
 *
 */
@Data
public class DailyExternalBudget {

	/** 職場グループ */
	private String unit;

	/** ID */
	private String id;

	/** 開始日 */
	private String startDate;

	/** 終了日 */
	private String endDate;

	/** 項目コード */
	private String itemCode;

}
