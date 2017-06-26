package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.core.dom.paymentdata.BonusDetailCode;
import nts.uk.ctx.pr.core.dom.paymentdata.PaymentDetailCode;

@Getter
@Setter
public class EmployeeAllotSettingHeader extends AggregateRoot {

	private String companyCode;

	private String historyId;
	
	private YearMonth startYm;

	private YearMonth endYm;

	

	/**
	 * 
	 */
	public EmployeeAllotSettingHeader (String companyCode,String historyId, YearMonth startYm, YearMonth endYm) {

		super();
		this.companyCode = companyCode;
		this.historyId = historyId;
		this.startYm = startYm;
		this.endYm = endYm;
		
	}
	public  static EmployeeAllotSettingHeader createFromJavaType(String companyCode,String historyId, int startYm, int endYm) {
		return new EmployeeAllotSettingHeader(
				companyCode, 
				historyId,
				new YearMonth(startYm), 			
				new YearMonth(endYm));
}
	
}
