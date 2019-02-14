package nts.uk.ctx.at.record.dom.standardtime.export;

import java.util.List;

import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.shared.dom.common.Year;

/**
 * 管理期間の36協定時間を取得
 * @author shuichi_ishida
 */
public interface GetAgreementTimeOfMngPeriod {

	/**
	 * 管理期間の36協定時間を取得
	 * @param employeeId 社員ID
	 * @param year 年度
	 * @return 管理期間の36協定時間リスト
	 */
	List<AgreementTimeOfManagePeriod> algorithm(String employeeId, Year year);
}
