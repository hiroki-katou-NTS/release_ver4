package nts.uk.pub.spr.approvalroot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.pub.spr.ApplicationSprPub;
import nts.uk.ctx.at.request.pub.spr.export.ApplicationSpr;
//import nts.uk.ctx.at.request.pub.spr.export.ApplicationSpr;
import nts.uk.ctx.bs.employee.pub.spr.EmployeeSprPub;
import nts.uk.ctx.bs.employee.pub.spr.export.EmpSprExport;
import nts.uk.ctx.bs.employee.pub.spr.export.PersonInfoSprExport;
import nts.uk.ctx.workflow.pub.resultrecord.EmpSprDailyConfirmExport;
import nts.uk.ctx.workflow.pub.resultrecord.IntermediateDataPub;
import nts.uk.ctx.workflow.pub.spr.SprApprovalSearchPub;
import nts.uk.ctx.workflow.pub.spr.export.ApprovalRootStateSprExport;
import nts.uk.ctx.workflow.pub.spr.export.JudgmentSprExport;
import nts.uk.pub.spr.approvalroot.output.ApprovalRootSpr;
import nts.uk.pub.spr.login.paramcheck.LoginParamCheck;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class SprApprovalRootImpl implements SprApprovalRootService {
	
	@Inject
	private EmployeeSprPub employeeSprPub;
	
	@Inject
	private SprApprovalSearchPub sprApprovalSearchPub;
	
	@Inject
	private ApplicationSprPub applicationSprPub;
	
	@Inject
	private LoginParamCheck loginParamCheck;
	
	@Inject
	private IntermediateDataPub intermediateDataPub;

	@Override
	public List<ApprovalRootSpr> getApprovalRoot(String employeeCD, String date) {
		String companyID = "000000000000-0001";
		this.checkParam(employeeCD, date);
		Optional<EmpSprExport> opEmployeeSpr = employeeSprPub.getEmployeeID(companyID, employeeCD);
		if(!opEmployeeSpr.isPresent()){
			throw new BusinessException("Msg_301");
		}
		return this.getApproverStatus(companyID, opEmployeeSpr.get().getEmployeeID(), loginParamCheck.getDate(date));
	}

	@Override
	public void checkParam(String employeeCD, String date) {
		if(Strings.isBlank(employeeCD)){
			throw new BusinessException("Msg_999", "Msg_1026");
		}
		employeeSprPub.validateEmpCodeSpr(employeeCD);
		if(Strings.isBlank(date)){
			throw new BusinessException("Msg_1009", "Msg_1026");
		}
		if(loginParamCheck.getDate(date)==null){
			throw new BusinessException("Msg_1009", date);
		}
	}

	@Override
	public List<ApprovalRootSpr> getApproverStatus(String companyID, String approverID, GeneralDate date) {
		List<ApprovalRootSpr> approvalRootSprList = new ArrayList<>();
		List<EmpSprDailyConfirmExport> empOnSettingList = new ArrayList<>();
		List<EmpSprDailyConfirmExport> empOnAppList = new ArrayList<>();
		List<String> empOnAppCDList = new ArrayList<>();
		List<EmpSprDailyConfirmExport> empOnConfirmList = new ArrayList<>();
		List<String> empOnConfirmCDList = new ArrayList<>();
		empOnAppCDList = this.getAppRootStateIDByType(companyID, approverID, date, 0);
		empOnAppCDList = empOnAppCDList.stream().distinct().sorted(Comparator.comparing(String::toString)).collect(Collectors.toList());
		empOnAppList = empOnAppCDList.stream().map(x -> new EmpSprDailyConfirmExport(x, 0)).collect(Collectors.toList()); 
		empOnConfirmList = intermediateDataPub.dailyConfirmSearch(companyID, approverID, date);
		empOnConfirmList.sort(Comparator.comparing(EmpSprDailyConfirmExport::getEmp));
		empOnConfirmCDList = empOnConfirmList.stream().map(x -> x.getEmp()).collect(Collectors.toList());
		empOnConfirmCDList = empOnConfirmCDList.stream().distinct().sorted(Comparator.comparing(String::toString)).collect(Collectors.toList());
		
		// merge list confirm
		for(EmpSprDailyConfirmExport empConfirm : empOnConfirmList){
			List<String> sumCDLst = empOnSettingList.stream().map(x -> x.getEmp()).collect(Collectors.toList());
			if(!sumCDLst.contains(empConfirm.getEmp())){
				empOnSettingList.add(empConfirm);
			}
		}
		// merge list application
		for(EmpSprDailyConfirmExport empApp : empOnAppList){
			List<String> sumCDLst = empOnSettingList.stream().map(x -> x.getEmp()).collect(Collectors.toList());
			if(!sumCDLst.contains(empApp.getEmp())){
				empOnSettingList.add(empApp);
			}
		}
		for(EmpSprDailyConfirmExport emp : empOnSettingList){
			Integer status1 = 0;
			Integer status2 = 0;
			if(empOnAppCDList.contains(emp.getEmp())){
				status1 = 1;
			}
			if(empOnConfirmCDList.contains(emp.getEmp())){
				status2 = emp.getConfirmAtr();
			}
			approvalRootSprList.add(new ApprovalRootSpr(emp.getEmp(), status1, status2));
		};
		return approvalRootSprList;
	}

	@Override
	public List<String> getAppRootStateIDByType(String companyID, String employeeID, GeneralDate date,
			Integer rootType) {
		List<String> empIDList = new ArrayList<>();
		// ドメインモデル「承認ルートインスタンス」を取得する
		List<ApprovalRootStateSprExport> approvalRootStateSprList = sprApprovalSearchPub.getAppByApproverDate(companyID, employeeID, date);
		approvalRootStateSprList.forEach(x -> {
			if(rootType==0){
				// ドメインモデル「申請」を取得する
				Optional<ApplicationSpr> opApplicationSpr = applicationSprPub.getAppByID(companyID, x.getRootStateID());
				if(!opApplicationSpr.isPresent()){
					return;
				} 
				// 申請種類!＝残業種類
				if(opApplicationSpr.get().getAppType()!=0){
					return;
				}
			}
			// （ワークフローExport）アルゴリズム「3.指定した社員が承認できるかの判断」を実行する
			JudgmentSprExport judgmentSprExport = sprApprovalSearchPub
					.judgmentTargetPersonCanApprove(companyID, x.getRootStateID(), employeeID, rootType);
			if(!(judgmentSprExport.getAuthorFlag()&&judgmentSprExport.getApprovalAtr()==0&&!judgmentSprExport.getExpirationAgentFlag())){
				return;
			}
			// （基幹・社員Export）アルゴリズム「社員IDから個人社員基本情報を取得」を実行する　RequestList No.1
			PersonInfoSprExport personInfoSpr = employeeSprPub.getPersonInfo(x.getEmployeeID());
			if(personInfoSpr==null){
				return;
			}
			empIDList.add(personInfoSpr.getEmployeeCode());
		});
		return empIDList;
	}
	
}
