package nts.uk.ctx.at.record.pub.monthly.agreement;

import nts.uk.ctx.at.record.dom.monthly.agreement.export.AgreementExcessInfo;
import nts.uk.ctx.at.shared.dom.common.Year;

/**
 * 年間超過回数の取得
 * @author shuichi_ishida
 */
public interface GetExcessTimesYearPub {

	/**
	 * 年間超過回数の取得
	 * @param employeeId 社員ID
	 * @param year 年度
	 * @return 年間超過回数
	 */
	// RequestList458
	AgreementExcessInfo algorithm(String employeeId, Year year);
}
