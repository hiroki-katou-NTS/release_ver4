/**
 * 
 */
package nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing;

import lombok.Getter;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;

/**
 * @author danpv
 *
 *承認結果反映用の設定情報
 */
@Getter
public class SetInforReflAprResult extends CalExeSettingInfor{
	
	private boolean forciblyReflect;
	
	public SetInforReflAprResult() {
		super();
	}

	public SetInforReflAprResult(ExecutionContent executionContent, ExecutionType executionType,
			String calExecutionSetInfoID, boolean forciblyReflect) {
		super(executionContent, executionType, calExecutionSetInfoID);
		this.forciblyReflect = forciblyReflect;
	}



	

	

	
}
