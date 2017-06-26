package nts.uk.ctx.at.schedule.dom.budget.premium;

import java.util.List;

import nts.arc.time.GeneralDate;
import nts.uk.shr.com.primitive.Memo;
/**
 * 
 * @author Doan Duy Hung
 *
 */
public interface PersonCostCalculationDomainService {
	
	/**
	 * create new Person Cost Calculation by auto generate history ID
	 * @param companyID companyID
	 * @param startDate start date
	 * @param unitPrice unit price
	 * @param memo memo
	 * @param premiumSettings list 10 Premium Setting
	 * @return Person Cost Calculation
	 */
	public PersonCostCalculation createPersonCostCalculationFromJavaType(String companyID, GeneralDate startDate, 
			UnitPrice unitPrice, Memo memo, List<PremiumSetting> premiumSettings);
	
	/**
	 * insert Person Cost Calculation include validate start date and update interdependent
	 * @param personCostCalculation Person Cost Calculation
	 */
	public void insertPersonCostCalculation(PersonCostCalculation personCostCalculation);
	
	/**
	 * update Person Cost Calculation include validate start date and update interdependent
	 * @param personCostCalculation Person Cost Calculation
	 */
	public void updatePersonCostCalculation(PersonCostCalculation personCostCalculation);
	
	/**
	 * delete Person Cost Calculation include validate start date and update interdependent
	 * @param personCostCalculation Person Cost Calculation
	 */
	public void deletePersonCostCalculation(PersonCostCalculation personCostCalculation);
}
