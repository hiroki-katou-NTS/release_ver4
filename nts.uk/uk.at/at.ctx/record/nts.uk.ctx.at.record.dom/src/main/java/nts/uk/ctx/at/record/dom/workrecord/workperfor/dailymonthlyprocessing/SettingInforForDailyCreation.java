/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import java.util.Optional;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.DailyRecreateClassification;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;

/**
 * @author danpv
 *
 * 日別作成用の設定情報
 */
@Getter
public class SettingInforForDailyCreation extends CalExeSettingInfor {

	/**
	 * 作成区分
	 * 0 : もう一度作り直す 1 : 一部修正
	 */
	private DailyRecreateClassification creationType;

	/**
	 * check-boxes
	 */
	private Optional<PartResetClassification> partResetClassification;

	public SettingInforForDailyCreation(ExecutionContent executionContent, ExecutionType executionType,
			String calExecutionSetInfoID, DailyRecreateClassification creationType,
			Optional<PartResetClassification> partResetClassification) {
		super(executionContent, executionType, calExecutionSetInfoID);
		this.creationType = creationType;
		this.partResetClassification = partResetClassification;
	}


}
