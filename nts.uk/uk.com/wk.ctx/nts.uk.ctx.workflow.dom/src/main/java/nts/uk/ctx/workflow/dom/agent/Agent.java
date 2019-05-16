package nts.uk.ctx.workflow.dom.agent;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.gul.text.StringUtil;

@Getter
//代行承認
public class Agent extends AggregateRoot {

	private String companyId;
	/**代行依頼者: A1_010*/
	private String employeeId;
	
	private UUID requestId;
	
	/**開始日*/
	private GeneralDate startDate;

	/**終了日*/
	private GeneralDate endDate;

	/**就業承認: 社員ID*/
	/** 就業承認．基本設定.承認代行者: A4_004*/
	private String agentSid1;

	/**就業承認: 代行承認種類1*/
	private AgentAppType agentAppType1;

	/**人事承認: 社員ID*/
	private String agentSid2;

	/**人事承認: 代行承認種類2*/
	private AgentAppType agentAppType2;

	/**給与承認: 社員ID*/
	private String agentSid3;

	/**給与承認: 代行承認種類3*/
	private AgentAppType agentAppType3;

	/**経理承認: 社員ID*/
	private String agentSid4;

	/**経理承認: 代行承認種類4*/
	private AgentAppType agentAppType4;

	public Agent(String companyId, String employeeId, UUID requestId, GeneralDate startDate, GeneralDate endDate,
			String agentSid1, AgentAppType agentAppType1, String agentSid2, AgentAppType agentAppType2,
			String agentSid3, AgentAppType agentAppType3, String agentSid4, AgentAppType agentAppType4) {
		super();

		this.companyId = companyId;
		this.employeeId = employeeId;
		this.requestId = requestId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.agentSid1 = agentSid1;
		this.agentAppType1 = agentAppType1;
		this.agentSid2 = agentSid2;
		this.agentAppType2 = agentAppType2;
		this.agentSid3 = agentSid3;
		this.agentAppType3 = agentAppType3;
		this.agentSid4 = agentSid4;
		this.agentAppType4 = agentAppType4;

	}
	
	/**
	 * Create from java type of agent
	 * @param companyId
	 * @param employeeId
	 * @param endDate
	 * @param startDate
	 * @param agentSid1
	 * @param agentAppType1
	 * @param agentSid2
	 * @param agentAppType2
	 * @param agentSid3
	 * @param agentAppType3
	 * @param agentSid4
	 * @param agentAppType4
	 * @return
	 */
	public static Agent createFromJavaType(
			String companyId, 
			String employeeId, 
			String requestId,
			GeneralDate endDate, 
			GeneralDate startDate,
			String agentSid1,
			int agentAppType1,
			String agentSid2,
			int agentAppType2,
			String agentSid3,
			int agentAppType3,
			String agentSid4,
			int agentAppType4
			) {
		return new Agent(
				companyId, 
				employeeId, 
				UUID.fromString(requestId),
				endDate, 
				startDate,
				agentSid1,
				EnumAdaptor.valueOf(agentAppType1, AgentAppType.class),
				agentSid2,
				EnumAdaptor.valueOf(agentAppType2, AgentAppType.class),
				agentSid3,
				EnumAdaptor.valueOf(agentAppType3, AgentAppType.class),
				agentSid4,
				EnumAdaptor.valueOf(agentAppType4, AgentAppType.class)
				);
	}
	
	/**
	 * create new request id
	 * @return
	 */
	public static UUID createRequestId() {
		return UUID.randomUUID();
	}
	
	/**
	 * Validate date 
	 * @param rangeDateList list range date ordered
	 */
	public void validateDate(List<RangeDate> rangeDateList) {
		if (this.startDate == null || this.endDate == null) {
			throw new RuntimeException("Start date and End date are required");
		}
		
		if (this.startDate.after(this.endDate)) {
			throw new RuntimeException("Start date must before End date");
		}
		
		GeneralDate toDay = GeneralDate.today();
		
		if (this.endDate.before(toDay)) {
			throw new BusinessException("Msg_11");
		}
		
		if (rangeDateList == null) {
			return;
		}
		
		rangeDateList.stream().forEach(rangeDate -> {
			if (!checkStartDate(rangeDate)) {
				throw new BusinessException("Msg_12");
			}
		});
	}
	
	/**
	 * check start date by range date latest
	 * @param rangeDateLatest range date latest
	 * @return false if start date before end date in range date latest, else true
	 */
	private boolean checkStartDate(RangeDate rangeDate) {		
		if (!(this.endDate.before(rangeDate.getStartDate()) || rangeDate.getEndDate().before(this.startDate))) {
			return false;
		}
		return true;
	}
	
	/**
	 * check agentSid with RageDate
	 * @param rangeDateList1
	 * @param rangeDateList2
	 * @param rangeDateList3
	 * @param rangeDateList4
	 */
	public void checkAgentSid(List<Agent> agentSidList){
		this.checkSameAgentRequest();
		this.checkAgentRequest();
		
		List<RangeDate> rangeDateList1 = agentSidList.stream()
				.filter(x->this.agentSid1.equals(x.getEmployeeId()) && !this.requestId.equals(x.getRequestId()))
				.map(a -> new RangeDate(a.getStartDate(), a.getEndDate()))
				.collect(Collectors.toList());
		
		List<RangeDate> rangeDateList2 = agentSidList.stream()
				.filter(x->this.agentSid2.equals(x.getEmployeeId()) && !this.requestId.equals(x.getRequestId()))
				.map(a -> new RangeDate(a.getStartDate(), a.getEndDate()))
				.collect(Collectors.toList());
		
		List<RangeDate> rangeDateList3 = agentSidList.stream()
				.filter(x->this.agentSid3.equals(x.getEmployeeId()) && !this.requestId.equals(x.getRequestId()))
				.map(a -> new RangeDate(a.getStartDate(), a.getEndDate()))
				.collect(Collectors.toList());
		
		List<RangeDate> rangeDateList4 = agentSidList.stream()
				.filter(x->this.agentSid4.equals(x.getEmployeeId()) && !this.requestId.equals(x.getRequestId()))
				.map(a -> new RangeDate(a.getStartDate(), a.getEndDate()))
				.collect(Collectors.toList());
		
		if (this.agentAppType1 == AgentAppType.SUBSTITUTE_DESIGNATION ) {
			validateAgentRequest(this.agentSid1, rangeDateList1);
		}
		
		if (this.agentAppType2 == AgentAppType.SUBSTITUTE_DESIGNATION) {
			validateAgentRequest(this.agentSid2, rangeDateList2);
		}
		
		if (this.agentAppType3 == AgentAppType.SUBSTITUTE_DESIGNATION) {
			validateAgentRequest(this.agentSid3, rangeDateList3);
		}
		
		if (this.agentAppType4 == AgentAppType.SUBSTITUTE_DESIGNATION) {
			validateAgentRequest(this.agentSid4, rangeDateList4);
		}
	}
	
	/**
	 * validate agent of approval
	 * @param sid
	 * @param rangeDateList
	 */
	private void validateAgentRequest(String sid, List<RangeDate> rangeDateList) {
		if (StringUtil.isNullOrEmpty(sid, true) || CollectionUtil.isEmpty(rangeDateList)) {
			return;
		}
		
		rangeDateList.stream().forEach(rangeDate -> {
			if (!checkStartDate(rangeDate)) {
				throw new BusinessException("Msg_13");
			}
		});
	}
	
	/**
	 * check same agentAppType 
	 */
	public void checkSameAgentRequest() {
		if (this.agentAppType1 == AgentAppType.NO_SETTINGS 
		&&  this.agentAppType2 == AgentAppType.NO_SETTINGS
		&&  this.agentAppType3 == AgentAppType.NO_SETTINGS
		&&  this.agentAppType4 == AgentAppType.NO_SETTINGS) {
			throw new BusinessException("Msg_225");
		}
	}
	
	/**
	 * 代行承認種類が代行者指定の場合、承認代行者を指定しなければならない。（ID:Msg_141） - DOM
	 * check agentAppType by agentSid
	 */
	public void checkAgentRequest() {
		if ((this.agentAppType1 == AgentAppType.SUBSTITUTE_DESIGNATION &&  this.agentSid1 == "")
		|| ( this.agentAppType2 == AgentAppType.SUBSTITUTE_DESIGNATION &&  this.agentSid2 == "")
		|| (this.agentAppType3 == AgentAppType.SUBSTITUTE_DESIGNATION &&  this.agentSid3 == "")
		|| ( this.agentAppType4 == AgentAppType.SUBSTITUTE_DESIGNATION &&  this.agentSid4 == "")) {
			throw new BusinessException("Msg_141");
		}
	}
	
}
