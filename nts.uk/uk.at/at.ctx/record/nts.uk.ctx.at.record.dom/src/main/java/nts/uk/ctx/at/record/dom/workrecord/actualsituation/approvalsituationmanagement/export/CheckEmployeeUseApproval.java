package nts.uk.ctx.at.record.dom.workrecord.actualsituation.approvalsituationmanagement.export;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
//import nts.uk.ctx.at.record.dom.jobtitle.affiliate.AffJobTitleAdapter;
//import nts.uk.ctx.at.record.dom.jobtitle.affiliate.AffJobTitleSidImport;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author thanhnx
 * 社員が日の承認処理を利用できるかチェックする
 *
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CheckEmployeeUseApproval {
	
//	@Inject
//	private AffJobTitleAdapter affJobTitleAdapter;
	
	@Inject
	private ApprovalProcessingUseSettingRepository approvalProcessingUseSettingRepository;
	
//	@Inject
//	private CheckPosstionApproval checkPosstionApproval;
	
   public boolean checkEmployeeUseApproval(String employeeId, GeneralDate date){
	   String companyId = AppContexts.user().companyId();
	   Optional<ApprovalProcessingUseSetting> appUseOpt = approvalProcessingUseSettingRepository.findByCompanyId(companyId);
	   if(appUseOpt.isPresent() && !appUseOpt.get().getUseDayApproverConfirm()) return false;
	   else return true;
//	   Optional<AffJobTitleSidImport>  affJobTitleSidOpt= affJobTitleAdapter.findByEmployeeId(employeeId, date);
//	   if(!affJobTitleSidOpt.isPresent()) return false;
//	   return checkPosstionApproval.checkPossitionApproval(affJobTitleSidOpt.get().getJobTitleId(), appUseOpt.get().getLstJobTitleNotUse());
   }
}
