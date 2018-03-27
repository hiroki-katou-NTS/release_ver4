/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;

/**
 * @author danpv
 * 計算実行設定情報
 */
@Getter
@NoArgsConstructor
public class CalExeSettingInfor extends DomainObject {
	
	/**
	 * 実行内容
	 */
	private ExecutionContent executionContent;
	
	/**
	 * 実行種別
	 * 
	 * 0 : 通常実行
	 * 1 : 再実行
	 */
	private ExecutionType executionType;

	private String calExecutionSetInfoID;

	public CalExeSettingInfor(ExecutionContent executionContent, ExecutionType executionType,
			String calExecutionSetInfoID) {
		super();
		this.executionContent = executionContent;
		this.executionType = executionType;
		this.calExecutionSetInfoID = calExecutionSetInfoID;
	}
	
}
