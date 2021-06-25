package nts.uk.pub.spr.appstatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.request.pub.spr.ApplicationSprPub;
import nts.uk.ctx.at.request.pub.spr.export.AppOverTimeSprExport;
import nts.uk.ctx.at.request.pub.spr.export.AppOvertimeStatusSprExport;
import nts.uk.ctx.bs.employee.pub.spr.EmployeeSprPub;
import nts.uk.ctx.bs.employee.pub.spr.export.EmpSprExport;
import nts.uk.pub.spr.appstatus.output.AppStatusSpr;
import nts.uk.pub.spr.login.paramcheck.LoginParamCheck;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class SprAppStatusImpl implements SprAppStatusService {
	
	@Inject
	private EmployeeSprPub employeeSprPub;
	
	@Inject
	private ApplicationSprPub applicationSprService;
	
	@Inject
	private LoginParamCheck loginParamCheck;

	@Override
	public List<AppStatusSpr> getAppStatus(String loginEmpCD, String employeeCD, String startDate, String endDate) {
		String companyID = "000000000000-0001";
		// アルゴリズム「パラメータチェック」を実行する
		this.checkParam(loginEmpCD, employeeCD, startDate, endDate);
		// （基幹・社員Export）アルゴリズム「「会社ID」「社員コード」より社員基本情報を取得」を実行する　RequestList No.18
		Optional<EmpSprExport> opEmployeeSpr = employeeSprPub.getEmployeeID(companyID, employeeCD);
		if(!opEmployeeSpr.isPresent()){
			throw new BusinessException("Msg_301");
		}
		// アルゴリズム「事前残業申請の有無」を実行する
		return this.getOverTimeAppStatus(
				opEmployeeSpr.get().getEmployeeID(), 
				loginParamCheck.getDate(startDate), 
				loginParamCheck.getDate(endDate));
		
	}

	@Override
	public void checkParam(String loginEmpCD, String employeeCD, String startDate, String endDate) {
		// フォームデータ「ログイン社員コード(loginemployeeCode)」を取得する
		if(Strings.isBlank(loginEmpCD)){
			throw new BusinessException("Msg_999", "Msg_1026");
		}
		// ログイン社員コード(loginemployeeCode)をチェックする
		employeeSprPub.validateEmpCodeSpr(loginEmpCD);
		// フォームデータ「対象社員コード(employeeCode)」を取得する
		if(Strings.isBlank(employeeCD)){
			throw new BusinessException("Msg_1000", "Msg_1026");
		}
		// 対象社員コード(employeeCode)をチェックする
		employeeSprPub.validateEmpCodeSpr(employeeCD);
		// フォームデータ「取得期間の開始日(startdate)」を取得する
		if(Strings.isBlank(startDate)){
			throw new BusinessException("Msg_1001", "Msg_1026");
		}
		GeneralDate startD = loginParamCheck.getDate(startDate);
		// 取得期間の開始日(startdate)の形式をチェックする　日付型
		if(startD==null){
			throw new BusinessException("Msg_1001", startDate);
		}
		// フォームデータ「取得期間の終了日(enddate)」を取得する
		if(Strings.isBlank(endDate)){
			throw new BusinessException("Msg_1002", "Msg_1026");
		}
		GeneralDate endD = loginParamCheck.getDate(endDate);
		// 取得期間の終了日(enddate)の形式をチェックする　日付型
		if(endD==null){
			throw new BusinessException("Msg_1002", startDate);
		}
		// 取得期間の開始日、終了日の逆転チェック
		if(startD.after(endD)){
			throw new BusinessException("Msg_1003", startDate, endDate);
		}
	}

	@Override
	public List<AppStatusSpr> getOverTimeAppStatus(String employeeID, GeneralDate startDate, GeneralDate endDate) {
		List<AppStatusSpr> appOvertimeStatusSprList = new ArrayList<>();
		// 取得期間を日単位でループする（開始日～終了日）　MAX 31日
		for(int i = 0; startDate.addDays(i).compareTo(endDate) <= 0; i++){
			if(i==31){
				break;
			}
			GeneralDate loopDate = startDate.addDays(i);
			// 早出残業申請状況確認
			AppOvertimeStatusSprExport appOvertimeStatusSpr1 = this.getOverTimeAppInfo(loopDate, employeeID, 0);
			// 通常残業申請状況確認
			AppOvertimeStatusSprExport appOvertimeStatusSpr2 = this.getOverTimeAppInfo(loopDate, employeeID, 1);
			//早出残業・通常残業申請状況確認
			AppOvertimeStatusSprExport appOvertimeStatusSpr3 = this.getOverTimeAppInfo(loopDate, employeeID, 2);
			appOvertimeStatusSprList.add(new AppStatusSpr(
					loopDate, 
					appOvertimeStatusSpr1.getStatus(), 
					appOvertimeStatusSpr2.getStatus(), 
					appOvertimeStatusSpr3.getStatus(), 
					Optional.ofNullable(appOvertimeStatusSpr1.getApplicationID()), 
					Optional.ofNullable(appOvertimeStatusSpr2.getApplicationID()),
					Optional.ofNullable(appOvertimeStatusSpr3.getApplicationID())));
		}
		return appOvertimeStatusSprList;
	}

	@Override
	public AppOvertimeStatusSprExport getOverTimeAppInfo(GeneralDate appDate, String employeeID, Integer overTimeAtr) {
		// ドメインモデル「申請」を取得する
		Optional<AppOverTimeSprExport> opAppOverTimeSpr = applicationSprService.getAppOvertimeByDate(appDate, employeeID, overTimeAtr);
		if(!opAppOverTimeSpr.isPresent()){
			return new AppOvertimeStatusSprExport(0, null);
		}
		AppOverTimeSprExport appOverTimeSpr = opAppOverTimeSpr.get();
		
		// ドメインモデル「申請.反映情報.実績反映状態」をチェックする
		// 取得残業区分をチェック
		//if(overTimeAtr==0||overTimeAtr==1){
			switch (appOverTimeSpr.getStateReflectionReal()) {
			case 1:
				return new AppOvertimeStatusSprExport(2, appOverTimeSpr.getAppID());
			case 2:
				return new AppOvertimeStatusSprExport(2, appOverTimeSpr.getAppID());
			case 3:
				return new AppOvertimeStatusSprExport(2, appOverTimeSpr.getAppID());
			case 4:
				return new AppOvertimeStatusSprExport(2, appOverTimeSpr.getAppID());
			case 6:
				return new AppOvertimeStatusSprExport(3, appOverTimeSpr.getAppID());
			case 5:
				return new AppOvertimeStatusSprExport(4, appOverTimeSpr.getAppID());
			default:
				return new AppOvertimeStatusSprExport(1, appOverTimeSpr.getAppID());
			}
		//}
		//return new AppOvertimeStatusSprExport(0, null);
	}

}
