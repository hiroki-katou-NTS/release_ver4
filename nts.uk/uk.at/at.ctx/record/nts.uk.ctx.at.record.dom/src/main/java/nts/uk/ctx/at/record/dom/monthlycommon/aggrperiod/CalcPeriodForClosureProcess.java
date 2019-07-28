package nts.uk.ctx.at.record.dom.monthlycommon.aggrperiod;

/**
 * 締め処理すべき集計期間を計算
 * @author shuichi_ishida
 */
public interface CalcPeriodForClosureProcess {

	/**
	 * 締め処理すべき集計期間を計算
	 * @param companyId 会社ID
	 * @param employeeId 社員ID
	 * @param closureId 締めID
	 * @return 戻り値：締め処理すべき集計期間を計算
	 */
	CalcPeriodForClosureProcValue algorithm(String companyId, String employeeId, int closureId);
}
