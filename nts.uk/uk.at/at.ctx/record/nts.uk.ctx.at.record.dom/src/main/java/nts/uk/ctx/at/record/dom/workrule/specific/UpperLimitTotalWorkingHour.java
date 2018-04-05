package nts.uk.ctx.at.record.dom.workrule.specific;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * 総労働時間の上限値制御.
 *
 * @author HoangNDH
 */
@Data
@AllArgsConstructor
public class UpperLimitTotalWorkingHour {
	
	/** The company id. */
	// 会社ID
	private CompanyId companyId;
	
	/** The limit set. */
	// 設定
	private LimitControlOfTotalWorkingSet limitSet;
	
	/**
	 * Creates the from java type.
	 *
	 * @param companyId the company id
	 * @param limitSet the limit set
	 * @return the upper limit total working hour
	 */
	public static UpperLimitTotalWorkingHour createFromJavaType(String companyId, int limitSet) {
		return new UpperLimitTotalWorkingHour(new CompanyId(companyId), EnumAdaptor.valueOf(limitSet, LimitControlOfTotalWorkingSet.class));
	}
}
