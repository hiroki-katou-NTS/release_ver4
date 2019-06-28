package nts.uk.ctx.at.function.dom.processexecution.listempautoexec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.employeebasic.SyEmployeeFnAdapter;
import nts.uk.ctx.at.function.dom.adapter.employment.EmploymentAdapter;
import nts.uk.ctx.at.function.dom.processexecution.ExecutionScopeClassification;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 更新処理自動実行の実行対象社員リストを取得する
 * @author tutk
 *
 */
@Stateless
public class ListEmpAutoExec {
	@Inject
	private SyEmployeeFnAdapter syEmployeeFnAdapter;
	
	@Inject
	private EmploymentAdapter employmentAdapter;
	
	public List<String> getListEmpAutoExec(String companyId,DatePeriod datePeriod,ExecutionScopeClassification execScopeCls,Optional<List<String>> listWorkPlaceId,Optional<List<String>> listEmploymentCode){
		List<String> listEmp = new ArrayList<>();
		if(execScopeCls == ExecutionScopeClassification.COMPANY ) {
			//期間内に特定の会社に所属している社員一覧を取得する
			listEmp = syEmployeeFnAdapter.filterSidByCidAndPeriod(companyId, datePeriod);
		}else {
			if(!listWorkPlaceId.isPresent() || listWorkPlaceId.get().isEmpty()) {
				return Collections.emptyList();
			}
			//【No.462】期間内に特定の職場（List）に所属している社員一覧を取得
			listEmp = syEmployeeFnAdapter.getListEmployeeId(listWorkPlaceId.get(), datePeriod);
		}
		List<String> listEmpByEmpCode = new ArrayList<>();
		if(listEmploymentCode.isPresent() && !listEmploymentCode.get().isEmpty()) {
			//[No.591]社員と雇用と期間から雇用履歴項目を取得する
			listEmpByEmpCode = employmentAdapter.getEmploymentBySidsAndEmploymentCds(listEmp, listEmploymentCode.get(), datePeriod);
			listEmp = listEmpByEmpCode;
		}
//		//【No.493】社員（List）と期間から１日でも在職している社員を取得する
//		List<String> listResult = syEmployeeFnAdapter.filterSidLstByDatePeriodAndSids(listEmp,datePeriod);
		return listEmp;
	}
}
