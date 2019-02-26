package nts.uk.ctx.workflow.pub.resultrecord;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@Getter
public class EmpSprDailyConfirmExport {
	
	/**
	 * 社員コード
	 */
	private String emp;
	
	/**
	 * ステータス（0:未処理、1:処理済）
	 */
	private Integer confirmAtr;
	
}
