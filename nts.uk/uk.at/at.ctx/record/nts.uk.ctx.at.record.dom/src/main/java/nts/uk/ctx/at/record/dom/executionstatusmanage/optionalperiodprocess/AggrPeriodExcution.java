package nts.uk.ctx.at.record.dom.executionstatusmanage.optionalperiodprocess;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.at.record.dom.executionstatusmanage.optionalperiodprocess.periodexcution.ExecutionAtr;
import nts.uk.ctx.at.record.dom.executionstatusmanage.optionalperiodprocess.periodexcution.ExecutionStatus;
import nts.uk.ctx.at.record.dom.executionstatusmanage.optionalperiodprocess.periodexcution.PresenceOfError;
import nts.uk.ctx.at.record.dom.resultsperiod.optionalaggregationperiod.primitivevalue.AggrFrameCode;

/**
 * 任意期間集計実行ログ KRCMT_AGGR_PERIODEXCUTION
 * 
 * @author phongtq
 *
 */
@Getter
public class AggrPeriodExcution {

	/** 会社ID */
	private String companyId;

	/** 実行社員ID */
	private String executionEmpId;

	/** 集計枠コード */
	private AggrFrameCode aggrFrameCode;

	/** ID */
	private String aggrId;

	/** 開始日時 */
	private GeneralDateTime startDateTime;

	/** 終了日時 */
	private GeneralDateTime endDateTime;

	/** 実行区分 */
	private ExecutionAtr executionAtr;

	/** 実行状況 */
	private ExecutionStatus executionStatus;

	/** エラーの有無 */
	private PresenceOfError presenceOfError;

	/**
	 * Contructor
	 * @param companyId
	 * @param executionEmpId
	 * @param aggrFrameCode
	 * @param aggrId
	 * @param startDateTime
	 * @param endDateTime
	 * @param executionAtr
	 * @param executionStatus
	 * @param presenceOfError
	 */
	public AggrPeriodExcution(String companyId, String executionEmpId, AggrFrameCode aggrFrameCode, String aggrId,
			GeneralDateTime startDateTime, GeneralDateTime endDateTime, ExecutionAtr executionAtr,
			ExecutionStatus executionStatus, PresenceOfError presenceOfError) {
		super();
		this.companyId = companyId;
		this.executionEmpId = executionEmpId;
		this.aggrFrameCode = aggrFrameCode;
		this.aggrId = aggrId;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.executionAtr = executionAtr;
		this.executionStatus = executionStatus;
		this.presenceOfError = presenceOfError;
	}
	
	/**
	 * Create From Java Type of Aggr Period Excution
	 * @param companyId
	 * @param executionEmpId
	 * @param aggrFrameCode
	 * @param aggrId
	 * @param startDateTime
	 * @param endDateTime
	 * @param executionAtr
	 * @param executionStatus
	 * @param presenceOfError
	 * @return
	 */
	public static AggrPeriodExcution createFromJavaType(String companyId, String executionEmpId, String aggrFrameCode, String aggrId, GeneralDateTime startDateTime,
			GeneralDateTime endDateTime, int executionAtr, int executionStatus, int presenceOfError){
		
		return new AggrPeriodExcution(companyId, executionEmpId, new AggrFrameCode(aggrFrameCode), aggrId, startDateTime, endDateTime,
				EnumAdaptor.valueOf(executionAtr, ExecutionAtr.class),
				EnumAdaptor.valueOf(executionStatus, ExecutionStatus.class),
				EnumAdaptor.valueOf(presenceOfError, PresenceOfError.class));
	}

}
