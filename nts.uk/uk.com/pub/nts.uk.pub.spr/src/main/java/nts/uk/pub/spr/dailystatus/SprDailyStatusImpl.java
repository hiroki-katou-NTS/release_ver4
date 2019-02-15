package nts.uk.pub.spr.dailystatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.logging.log4j.util.Strings;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.pub.workrecord.identificationstatus.IndentificationPub;
import nts.uk.ctx.bs.employee.pub.spr.EmployeeSprPub;
import nts.uk.ctx.bs.employee.pub.spr.export.EmpSprExport;
import nts.uk.ctx.workflow.pub.resultrecord.IntermediateDataPub;
import nts.uk.ctx.workflow.pub.spr.export.AppRootStateStatusSprExport;
import nts.uk.pub.spr.dailystatus.output.DailyStatusSpr;
import nts.uk.pub.spr.login.paramcheck.LoginParamCheck;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class SprDailyStatusImpl implements SprDailyStatusService {
	
	@Inject
	private EmployeeSprPub employeeSprPub;
	
	@Inject
	private IndentificationPub indentificationPub;
	
	@Inject
	private LoginParamCheck loginParamCheck;
	
	@Inject
	private IntermediateDataPub intermediateDataPub;
	
	@Override
	public List<DailyStatusSpr> getStatusOfDaily(String loginEmpCD, String employeeCD, String startDate,
			String endDate) {
		String companyID = "000000000000-0001";
		// アルゴリズム「パラメータチェック」を実行する
		this.checkParam(loginEmpCD, employeeCD, startDate, endDate);
		// （基幹・社員Export）アルゴリズム「「会社ID」「社員コード」より社員基本情報を取得」を実行する　RequestList No.18
		Optional<EmpSprExport> opEmployeeSpr = employeeSprPub.getEmployeeID(companyID, employeeCD);
		if(!opEmployeeSpr.isPresent()){
			throw new BusinessException("Msg_301");
		}
		// アルゴリズム「実績残業報告の有無」を実行する
		return getEmpDailyStatus(
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
		// 取得期間の開始日(startdate)の形式をチェックする　日付型（yyyy/mm/dd）
		if(startD==null){
			throw new BusinessException("Msg_1001", startDate);
		}
		// フォームデータ「取得期間の終了日(enddate)」を取得する
		if(Strings.isBlank(endDate)){
			throw new BusinessException("Msg_1002", "Msg_1026");
		}
		GeneralDate endD = loginParamCheck.getDate(endDate);
		// 取得期間の終了日(enddate)の形式をチェックする　日付型（yyyy/mm/dd）
		if(endD==null){
			throw new BusinessException("Msg_1002", startDate);
		}
		// 取得期間の開始日、終了日の逆転チェック
		if(startD.after(endD)){
			throw new BusinessException("Msg_1003", startDate, endDate);
		}
		
	}

	@Override
	public List<DailyStatusSpr> getEmpDailyStatus(String employeeID, GeneralDate startDate, GeneralDate endDate) {
		List<DailyStatusSpr> resultList = new ArrayList<>();
		// 取得期間を日単位でループする（開始日～終了日）　MAX 31日
		for(int i = 0; startDate.addDays(i).compareTo(endDate) <= 0; i++){
			if(i==31){
				break;
			}
			GeneralDate loopDate = startDate.addDays(i);
			// 本人確認状況
			Integer status1 = this.getEmployeeStatus(loopDate, employeeID);
			// 上司承認状況
			Integer status2 = this.getManagerStatus(loopDate, employeeID);
			resultList.add(new DailyStatusSpr(loopDate, status1, status2));
		}
		return resultList;
	}

	@Override
	public Integer getEmployeeStatus(GeneralDate appDate, String employeeID) {
		List<GeneralDate> dateList = indentificationPub.getResovleDateIdentify(employeeID, new DatePeriod(appDate, appDate));
		if(CollectionUtil.isEmpty(dateList)){
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public Integer getManagerStatus(GeneralDate appDate, String employeeID) {
		// （ワークフローExport）アルゴリズム「承認対象者と期間から承認状況を取得する」を実行する
		List<AppRootStateStatusSprExport> appRootStateStatusSprList = intermediateDataPub.getAppRootStatusByEmpPeriod(
				employeeID, 
				new DatePeriod(appDate, appDate), 
				1);
		if(CollectionUtil.isEmpty(appRootStateStatusSprList)){
			return 0;
		}
		if(appRootStateStatusSprList.get(0).getDailyConfirmAtr()==2){
			// 承認済」の場合
			return 1;
		} else {
			// 未承認」又は「承認中」の場合
			return 0;
		}
	}

}
