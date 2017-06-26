package nts.uk.ctx.at.schedule.dom.budget.premium;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Value;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.shr.com.primitive.Memo;

/**
 * 
 * @author Doan Duy Hung
 *
 */

@Value
@EqualsAndHashCode(callSuper = false)
public class PersonCostCalculation extends AggregateRoot {

	private String companyID;

	private String historyID;
	
	private GeneralDate startDate;

	private GeneralDate endDate;

	private UnitPrice unitPrice;

	private Memo memo;
	
	private List<PremiumSetting> premiumSettings;

	public PersonCostCalculation(String companyID, String historyID, GeneralDate startDate, GeneralDate endDate, 
			UnitPrice unitPrice, Memo memo, List<PremiumSetting> premiumSettings) {
		super();
		this.companyID = companyID;
		this.historyID = historyID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.unitPrice = unitPrice;
		this.memo = memo;
		this.premiumSettings = premiumSettings;
	} 
}