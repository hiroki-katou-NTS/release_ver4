package nts.uk.ctx.at.record.dom.monthly.agreement.export;

import java.util.List;
import java.util.Map;

import nts.uk.ctx.at.shared.dom.common.Year;

/**
 * 年間超過回数の取得
 * @author shuichi_ishida
 */
public interface GetExcessTimesYear {

	/**
	 * 年間超過回数の取得
	 * @param employeeId 社員ID
	 * @param year 年度
	 * @return 年間超過回数
	 */
	AgreementExcessInfo algorithm(String employeeId, Year year);
	
	/**
	 * 年間超過回数の取得
	 * @param employeeId List 社員ID
	 * @param year 年度
	 * @return 年間超過回数
	 */
	Map<String,AgreementExcessInfo> algorithm(List<String> employeeIds, Year year);
}
