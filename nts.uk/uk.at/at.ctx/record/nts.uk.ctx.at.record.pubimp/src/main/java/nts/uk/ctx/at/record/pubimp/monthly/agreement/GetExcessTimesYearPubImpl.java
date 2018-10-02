package nts.uk.ctx.at.record.pubimp.monthly.agreement;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.monthly.agreement.export.AgreementExcessInfo;
import nts.uk.ctx.at.record.dom.monthly.agreement.export.GetExcessTimesYear;
import nts.uk.ctx.at.record.pub.monthly.agreement.GetExcessTimesYearPub;
import nts.uk.ctx.at.shared.dom.common.Year;

/**
 * 実装：年間超過回数の取得
 * @author shuichi_ishida
 */
@Stateless
public class GetExcessTimesYearPubImpl implements GetExcessTimesYearPub {

	/** 年間超過回数の取得 */
	@Inject
	private GetExcessTimesYear getExcessTimesYear;
	
	/** 年間超過回数の取得 */
	@Override
	public AgreementExcessInfo algorithm(String employeeId, Year year) {
		return this.getExcessTimesYear.algorithm(employeeId, year);
	}
}
