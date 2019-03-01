package nts.uk.ctx.at.function.dom.adapter.monthly.agreement;

import java.util.List;
import java.util.Map;

import nts.uk.ctx.at.shared.dom.common.Year;

public interface GetExcessTimesYearAdapter {
	/**
	 * 年間超過回数の取得
	 * @param employeeId 社員ID
	 * @param year 年度
	 * @return 年間超過回数
	 */
	int algorithm(String employeeId, Year year);
	
	/**
	 * 年間超過回数の取得
	 * @param employeeId List 社員ID
	 * @param year 年度
	 * @return 年間超過回数
	 */
	Map<String,Integer> algorithm(List<String> employeeIds, Year year);
}
