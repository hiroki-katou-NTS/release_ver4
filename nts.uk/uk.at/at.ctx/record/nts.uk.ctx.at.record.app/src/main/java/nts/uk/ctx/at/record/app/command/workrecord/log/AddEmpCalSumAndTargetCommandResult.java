package nts.uk.ctx.at.record.app.command.workrecord.log;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.enums.EnumConstant;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;

@Data
@NoArgsConstructor
public class AddEmpCalSumAndTargetCommandResult {
	
	private List<EnumConstant> enumComboBox;
	
	private String empCalAndSumExecLogID;
	
	/** 対象期間開始日 */
    private String periodStartDate;
    /** 対象期間終了日 */
    private String periodEndDate;
    
    private GeneralDateTime startTime;

    public AddEmpCalSumAndTargetCommandResult(String empCalAndSumExecLogID,
    		String periodStartDate, String periodEndDate) {
    	this.enumComboBox = EnumAdaptor.convertToValueNameList(ExecutionContent.class, ExecutionContent.DAILY_CREATION, ExecutionContent.DAILY_CALCULATION, ExecutionContent.MONTHLY_AGGREGATION);
		this.empCalAndSumExecLogID = empCalAndSumExecLogID;
		this.periodStartDate = periodStartDate;
		this.periodEndDate = periodEndDate;
		this.startTime = GeneralDateTime.now();
	}

}