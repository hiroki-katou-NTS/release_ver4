package nts.uk.ctx.at.record.dom.dailyprocess.calc;

import java.util.List;
import java.util.Optional;
//import java.util.function.Consumer;

import nts.arc.layer.app.command.AsyncCommandHandlerContext;
//import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.repository.CreateDailyResultDomainServiceImpl.ProcessState;
import nts.uk.ctx.at.record.dom.workrecord.closurestatus.ClosureStatusManagement;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionType;
//import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 実績計算を呼び出す用のサービス
 * @author keisuke_hoshina
 *
 */
public interface CalculateDailyRecordServiceCenter{//

	//計算
	//old_process. Don't use!
	public List<IntegrationOfDaily> calculate(List<IntegrationOfDaily> integrationOfDaily);

	//計算(会社共通のマスタを渡せる場合)
	default public List<IntegrationOfDaily> calculatePassCompanySetting(List<IntegrationOfDaily> integrationOfDaily,Optional<ManagePerCompanySet> companySet,ExecutionType reCalcAtr) {
		return calculatePassCompanySetting(CalculateOption.asDefault(), integrationOfDaily, companySet,reCalcAtr);
	}

	//計算(会社共通のマスタを渡せる場合)
	public List<IntegrationOfDaily> calculatePassCompanySetting(CalculateOption calcOption, List<IntegrationOfDaily> integrationOfDaily,Optional<ManagePerCompanySet> companySet,ExecutionType reCalcAtr);
	
	//計算(就業計算と集計用)
	@SuppressWarnings("rawtypes")
	public ManageProcessAndCalcStateResult calculateForManageState(List<IntegrationOfDaily> integrationOfDaily,List<ClosureStatusManagement> closureList,ExecutionType reCalcAtr, String empCalAndSumExecLogID,ManagePerCompanySet setting);
	
	//エラーチェック
	public List<IntegrationOfDaily> errorCheck(List<IntegrationOfDaily> integrationList);

	//計算(更新処理自動実行用)
	public ManageProcessAndCalcStateResult calculateForclosure(List<IntegrationOfDaily> integrationOfDaily,ManagePerCompanySet companySet, List<ClosureStatusManagement> closureList,String executeLogId);

	//計算(スケジュールからの窓口)
	List<IntegrationOfDaily> calculateForSchedule(CalculateOption calcOption,List<IntegrationOfDaily> integrationOfDaily, Optional<ManagePerCompanySet> companySet);
	
}
