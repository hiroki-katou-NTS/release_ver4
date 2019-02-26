package nts.uk.ctx.at.request.dom.application.common.service.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository_New;
import nts.uk.ctx.at.request.dom.application.Application_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.ApprovalRootStateAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalFrameImport_New;
import nts.uk.ctx.at.request.dom.application.common.adapter.workflow.dto.ApprovalPhaseStateImport_New;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.DetailScreenAppData;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class DetailScreenAppDataImpl implements DetailScreenAppDataService {

	@Inject
	private ApplicationRepository_New applicationRepository;
	
	@Inject
	private ApprovalRootStateAdapter approvalRootStateAdapter;
	
	@Override
	public DetailScreenAppData getDetailScreenAppData(String appID) {
		String companyID = AppContexts.user().companyId();
		String loginEmpID = AppContexts.user().employeeId();
		String authorComment = Strings.EMPTY;
		// ドメインモデル「申請」を取得する
		Optional<Application_New> opApp = applicationRepository.findByID(companyID, appID);
		if(!opApp.isPresent()){
			throw new BusinessException("Msg_198");
		}
		// 承認ルートの内容取得
		Map<String,List<ApprovalPhaseStateImport_New>> map = approvalRootStateAdapter.getApprovalRootContents(Arrays.asList(appID), companyID);
		List<ApprovalPhaseStateImport_New> approvalPhaseStateLst = new ArrayList<ApprovalPhaseStateImport_New>();
		map.entrySet().stream().forEach(x -> approvalPhaseStateLst.addAll(x.getValue()));
		// 承認フェーズListを5～1の逆順でループする
		approvalPhaseStateLst.sort(Comparator.comparing(ApprovalPhaseStateImport_New::getPhaseOrder).reversed());
		for(ApprovalPhaseStateImport_New approvalPhase : approvalPhaseStateLst){
			boolean find = false;
			for(ApprovalFrameImport_New approvalFrame : approvalPhase.getListApprovalFrame()){
				List<String> approverList = approvalFrame.getListApprover().stream().map(x -> x.getApproverID()).collect(Collectors.toList());
				// ループ中の承認枠．承認者＝ログイン社員の場合
				if(approverList.contains(loginEmpID)){
					authorComment = approvalFrame.getApprovalReason();
					find = true;
					break;
				}
			}
			if(find){
				break;
			}
		}
		return new DetailScreenAppData(authorComment);
	}

}
