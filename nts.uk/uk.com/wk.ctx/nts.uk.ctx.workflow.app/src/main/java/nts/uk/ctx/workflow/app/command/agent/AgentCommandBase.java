package nts.uk.ctx.workflow.app.command.agent;

import lombok.Value;
import nts.arc.time.GeneralDate;

/**
 * 
 * @author phongtq
 *
 */
@Value
public class AgentCommandBase {
	//A1_010
	private String employeeId;

	private String requestId;
	/** 開始日 */
	private GeneralDate startDate;
	
	/** 終了日 */
	private GeneralDate endDate;
	
	/** 就業承認: 社員ID */
	private String agentSid1;//A4_004
	
	/** 就業承認: 代行承認種類1 */
	private int agentAppType1;
	
	/** 人事承認: 社員ID */
	private String agentSid2;

	/** 人事承認: 代行承認種類2 */
	private int agentAppType2;

	/** 給与承認: 社員ID */
	private String agentSid3;
	
	/** 給与承認: 代行承認種類3 */
	private int agentAppType3;
	
	/** 経理承認: 社員ID */
	private String agentSid4;

	/** 経理承認: 代行承認種類4 */
	private int agentAppType4;
}
