package nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.monthlyperformance.createperapprovalmonthly;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.createdailyapprover.AppRootInsContentFnImport;
import nts.uk.ctx.at.record.dom.adapter.createdailyapprover.CreateDailyApproverAdapter;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.ErrorMessageRC;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.monthlyperformance.AppDataInfoMonthly;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.monthlyperformance.AppDataInfoMonthlyRepository;
import nts.uk.shr.com.i18n.TextResource;

@Stateless
public class CreateperApprovalMonthlyDefault implements CreateperApprovalMonthlyService {
	
	@Inject 
	private AppDataInfoMonthlyRepository appDataInfoMonthlyRepo;
	
	@Inject
	private CreateDailyApproverAdapter createDailyApproverAdapter;
	
	@Override
	public boolean createperApprovalMonthly(String companyId, String executionId, List<String> employeeIDs,
			int processExecType, GeneralDate endDateClosure) {
		
			/**パラメータ.社員ID（List）の数だけループする*/
			for(String employeeID : employeeIDs) {
				
				/**アルゴリズム「指定社員の中間データを作成する」を実行する*/
				AppRootInsContentFnImport appRootInsContentFnImport =  createDailyApproverAdapter.createDailyApprover(employeeID, 2, endDateClosure,endDateClosure);
				
				boolean flagError = appRootInsContentFnImport.getErrorFlag().intValue()==0?false:true;
				String errorMessage = appRootInsContentFnImport.getErrorMsgID();
				//取得したエラーフラグ != エラーなし
				if(flagError) {
					/**ドメインモデル「承認中間データエラーメッセージ情報（日別実績）」を追加する*/
					AppDataInfoMonthly appDataInfoMonthly = new AppDataInfoMonthly(employeeID, executionId, new ErrorMessageRC(TextResource.localize(errorMessage)));
					appDataInfoMonthlyRepo.addAppDataInfoMonthly(appDataInfoMonthly);
				}
				
				
			}//end for listEmployee
			/**ドメインモデル「承認中間データエラーメッセージ情報（月別実績）」を取得する*/
			List<AppDataInfoMonthly> listAppDataInfoMonthly = appDataInfoMonthlyRepo.getAppDataInfoMonthlyByExeID(executionId);
			if(!listAppDataInfoMonthly.isEmpty()) {//取得できた場合
				return true;
			}else {
				return false;
			}
			
	}



}
