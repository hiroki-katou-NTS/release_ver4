package nts.uk.ctx.workflow.dom.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class AgentApprovalService {
	@Inject
	private AgentRepository agentRepository;
	
	/**
	 * 9.代行者の登録_期間のチェック
	 * @param agentInfor
	 */
	public void checkPeriodRegAgent(Agent agentInfor, boolean isInsert){
		String companyId = AppContexts.user().companyId();
		String employeeId = agentInfor.getEmployeeId();//A1_010
		String agentSid1 = agentInfor.getAgentSid1();//A4_004
		GeneralDate sDate = agentInfor.getStartDate();
		GeneralDate eDate = agentInfor.getEndDate();
		//終了日はシステム日付と比較する
		if(agentInfor.getEndDate().before(GeneralDate.today())){
			//エラーメッセージ(Msg_11)
			throw new BusinessException("Msg_11");
		}
		//EA修正履歴 No.3449
		List<Agent> agents = new ArrayList<>();
		if(isInsert){
			//ドメインモデル「代行承認」を取得する(lấy dữ liệu domain「代行承認」)
//			「代行承認」．代行依頼者 = 画面(A1_010)選択する代行承認者
			agents = agentRepository.findAllAgent(companyId, employeeId);
		}else{
			//ドメインモデル「代行承認」を取得する(lấy dữ liệu domain「代行承認」)
//			条件：
//			・会社ID
//			・代行依頼者
//			・代行依頼ID ≠ 更新対象の代行依頼ID
//			「代行承認」．代行依頼者 = 画面(A1_010)選択する代行依頼者
			agents = agentRepository.getListAgentBySidReqId(companyId, employeeId, agentInfor.getRequestId().toString());
		}
		if(!agents.isEmpty()){
			List<RangeDate> rangeDateList = agents.stream()
					.map(a -> new RangeDate(a.getStartDate(), a.getEndDate()))
					.collect(Collectors.toList());
			rangeDateList.stream().forEach(rangeDate -> {
				if (!(eDate.before(rangeDate.getStartDate()) || rangeDate.getEndDate().before(sDate))) {
					throw new BusinessException("Msg_12");
				}
			});
		}

		//ドメインモデル「代行承認」を取得する(lấy dữ liệu domain 「代行承認」)
//		「代行承認」．代行依頼者 = 画面(A4_004)選択する代行依頼者
		List<Agent> agentbyA4 = agentRepository.findAllAgent(companyId, agentSid1);
		if(!agentbyA4.isEmpty()){
			List<RangeDate> rangeDateA4 = agentbyA4.stream()
					.map(a -> new RangeDate(a.getStartDate(), a.getEndDate()))
					.collect(Collectors.toList());
			rangeDateA4.stream().forEach(rangeDate -> {
				if (!(eDate.before(rangeDate.getStartDate()) || rangeDate.getEndDate().before(sDate))) {
					throw new BusinessException("Msg_13");
				}
			});
		}
		//EA修正履歴 No.3444
		//ドメインモデル「代行承認」を取得する(lấy dữ liệu domain 「代行承認」)
//		「代行承認」．代行承認者 = 画面(A1_010)選択する代行依頼者
		List<Agent> agentSidList = agentRepository.getListByAgentType1(companyId, employeeId);
		if(!agentSidList.isEmpty()){
			List<RangeDate> rangeDateA4 = agentSidList.stream()
					.map(a -> new RangeDate(a.getStartDate(), a.getEndDate()))
					.collect(Collectors.toList());
			rangeDateA4.stream().forEach(rangeDate -> {
				if (!(eDate.before(rangeDate.getStartDate()) || rangeDate.getEndDate().before(sDate))) {
					throw new BusinessException("Msg_1534");
				}
			});
		}
	}
}
