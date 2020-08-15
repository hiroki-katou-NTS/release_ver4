package nts.uk.screen.at.app.ksu.ksu001q.command;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外部予算日次を登録する
 * 
 * @author thanhlv
 *
 */
@Data
@NoArgsConstructor
public class RegisterExternalBudgetDailyCommand {

	/** 職場グループ */
	private String unit;

	/** ID */
	private String id;

	/** 入力対象（項目コード） */
	private String itemCode;

	/** 年月日（項目） */
	private List<DateAndValueMap> dateAndValues;

	private String type;

}
