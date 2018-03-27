package nts.uk.ctx.at.record.dom.monthlyprocess.aggr.export;

import java.util.List;

import nts.arc.time.YearMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

/**
 * 36協定時間の取得
 * @author shuichu_ishida
 */
public interface GetAgreementTime {

	/**
	 * 36協定時間の取得
	 * @param companyId 会社ID
	 * @param employeeIds 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @return 36協定時間一覧
	 */
	List<AgreementTimeDetail> get(String companyId, List<String> employeeIds, YearMonth yearMonth, ClosureId closureId);
}
