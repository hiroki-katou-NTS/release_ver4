package nts.uk.ctx.at.schedule.dom.budget.premium;

import java.util.List;
import java.util.Optional;

import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;

/**
 * @author Doan Duy Hung
 */

public interface PersonCostCalculationRepository {

    /**
     * insert PersonCostCalculation
     *
     * @param personCostCalculation PersonCostCalculation
     */
    public void add(PersonCostCalculation personCostCalculation);

    /**
     * get list PersonCostCalculation by company ID
     *
     * @param companyID company ID
     * @return list PersonCostCalculation by company ID
     */
    public List<PersonCostCalculation> findByCompanyID(String companyID);

    /**
     * get single PersonCostCalculation by company ID and history ID
     *
     * @param companyID company ID
     * @param historyID history ID
     * @return single PersonCostCalculation by company ID and history ID
     */
    public Optional<PersonCostCalculation> findItemByDate(String companyID, GeneralDate startDate);

    /**
     * get single PersonCostCalculation by company ID and history ID
     *
     * @param companyID company ID
     * @param historyID history ID
     * @return single PersonCostCalculation by company ID and history ID
     */
    public Optional<PersonCostCalculation> findItemByHistoryID(String companyID, String historyID);

    /**
     * get single PersonCostCalculation before input date by company ID
     *
     * @param companyID company ID
     * @param date      input date
     * @return single PersonCostCalculation before input date by company ID
     */
    public Optional<PersonCostCalculation> findItemBefore(String companyID, GeneralDate startDate);

    /**
     * get single PersonCostCalculation after input date by company ID
     *
     * @param companyID company ID
     * @param date      input date
     * @return single PersonCostCalculation after input date by company ID
     */
    public Optional<PersonCostCalculation> findItemAfter(String companyID, GeneralDate startDate);

    /**
     * update PersonCostCalculation
     *
     * @param personCostCalculation PersonCostCalculation
     */
    public void update(PersonCostCalculation personCostCalculation);

    /**
     * delete PersonCostCalculation
     *
     * @param personCostCalculation PersonCostCalculation
     */
    public void delete(PersonCostCalculation personCostCalculation);

    /**
     * @param companyID
     * @param date
     * @param displayNumber
     * @return
     */
    public List<PersonCostCalculation> findByCompanyIDAndDisplayNumber(String companyID, GeneralDate date);

    public List<PersonCostCalculation> findByCompanyIDAndDisplayNumberNotFull(String companyID, DatePeriod date, List<Integer> itemNos);

    public List<PremiumSetting> findPremiumSettingBy(String companyID, GeneralDate date);

    //=============================Update KML 001

    public Optional<HistPersonCostCalculation> getHistPersonCostCalculation(String cid);

    public Optional<PersonCostCalculation> getLastPersonCost(String cid, String histId);

    public void createHistPersonCl(HistPersonCostCalculation domain);

    public void updateHistPersonCl(HistPersonCostCalculation domain);


}
