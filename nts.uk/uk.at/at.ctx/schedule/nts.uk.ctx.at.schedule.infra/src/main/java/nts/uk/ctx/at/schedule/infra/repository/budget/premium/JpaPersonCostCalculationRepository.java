package nts.uk.ctx.at.schedule.infra.repository.budget.premium;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.budget.premium.PersonCostCalculation;
import nts.uk.ctx.at.schedule.dom.budget.premium.PersonCostCalculationRepository;
import nts.uk.ctx.at.schedule.dom.budget.premium.PremiumName;
import nts.uk.ctx.at.schedule.dom.budget.premium.PremiumRate;
import nts.uk.ctx.at.schedule.dom.budget.premium.PremiumSetting;
import nts.uk.ctx.at.schedule.dom.budget.premium.UnitPrice;
import nts.uk.ctx.at.schedule.dom.budget.premium.UseAttribute;
import nts.uk.ctx.at.schedule.infra.entity.budget.premium.KmldpPremiumAttendancePK;
import nts.uk.ctx.at.schedule.infra.entity.budget.premium.KmldtPremiumAttendance;
import nts.uk.ctx.at.schedule.infra.entity.budget.premium.KmlmpPersonCostCalculationPK;
import nts.uk.ctx.at.schedule.infra.entity.budget.premium.KmlmtPersonCostCalculation;
import nts.uk.ctx.at.schedule.infra.entity.budget.premium.KmlspPremiumSetPK;
import nts.uk.ctx.at.schedule.infra.entity.budget.premium.KmlstPremiumSet;
import nts.uk.ctx.at.schedule.infra.entity.budget.premium.KmnmpPremiumItemPK;
import nts.uk.ctx.at.schedule.infra.entity.budget.premium.KmnmtPremiumItem;
import nts.uk.shr.com.primitive.Memo;

/**
 * 
 * @author Doan Duy Hung
 *
 */

@Stateless
public class JpaPersonCostCalculationRepository extends JpaRepository implements PersonCostCalculationRepository{

	private final String SEL_BY_CID = "SELECT a FROM KmlmtPersonCostCalculation a WHERE a.kmlmpPersonCostCalculationPK.companyID = :companyID ORDER BY a.startDate ASC";
	
	private final String SEL_ITEM_BY_DATE = "SELECT a FROM KmlmtPersonCostCalculation a WHERE a.kmlmpPersonCostCalculationPK.companyID = :companyID AND a.startDate = :startDate";
	
	private final String SEL_ITEM_BY_HID = "SELECT a FROM KmlmtPersonCostCalculation a WHERE a.kmlmpPersonCostCalculationPK.companyID = :companyID AND a.kmlmpPersonCostCalculationPK.historyID = :historyID";
	
	private final String SEL_ITEM_BEFORE = "SELECT a FROM KmlmtPersonCostCalculation a WHERE a.kmlmpPersonCostCalculationPK.companyID = :companyID AND a.startDate < :startDate ORDER BY a.startDate DESC";
	
	private final String SEL_ITEM_AFTER = "SELECT a FROM KmlmtPersonCostCalculation a WHERE a.kmlmpPersonCostCalculationPK.companyID = :companyID AND a.startDate > :startDate";
	
	@Override
	public void add(PersonCostCalculation personCostCalculation) {
		this.commandProxy().insert(toPersonCostCalculationEntity(personCostCalculation));
	}

	@Override
	public List<PersonCostCalculation> findByCompanyID(String companyID) {
		return this.queryProxy().query(SEL_BY_CID, KmlmtPersonCostCalculation.class).setParameter("companyID", companyID)
				.getList(x -> toDomainPersonCostCalculation(x));
	}
	
	@Override
	public Optional<PersonCostCalculation> findItemByDate(String companyID, GeneralDate startDate) {
		return this.queryProxy().query(SEL_ITEM_BY_DATE, KmlmtPersonCostCalculation.class).setParameter("companyID", companyID).setParameter("startDate", startDate)
				.getSingle().map(x -> toDomainPersonCostCalculation(x));
	}

	@Override
	public Optional<PersonCostCalculation> findItemByHistoryID(String companyID, String historyID) {
		return this.queryProxy().query(SEL_ITEM_BY_HID, KmlmtPersonCostCalculation.class).setParameter("companyID", companyID).setParameter("historyID", historyID)
				.getSingle().map(x -> toDomainPersonCostCalculation(x));
	}

	@Override
	public Optional<PersonCostCalculation> findItemBefore(String companyID, GeneralDate startDate) {
		List<KmlmtPersonCostCalculation> result = this.queryProxy().query(SEL_ITEM_BEFORE, KmlmtPersonCostCalculation.class)
				.setParameter("companyID", companyID).setParameter("startDate", startDate).getList();
		if(result.isEmpty()) return Optional.ofNullable(null);
		return Optional.of(toDomainPersonCostCalculation(result.get(0)));
	}

	@Override
	public Optional<PersonCostCalculation> findItemAfter(String companyID, GeneralDate startDate) {
		List<KmlmtPersonCostCalculation> result = this.queryProxy().query(SEL_ITEM_AFTER, KmlmtPersonCostCalculation.class)
				.setParameter("companyID", companyID).setParameter("startDate", startDate).getList();
		if(result.isEmpty()) return Optional.empty();
		return Optional.of(toDomainPersonCostCalculation(result.get(0)));
	}

	@Override
	public void update(PersonCostCalculation personCostCalculation) {
		this.commandProxy().update(toPersonCostCalculationEntity(personCostCalculation));
	}

	@Override
	public void delete(PersonCostCalculation personCostCalculation) {
		this.commandProxy().remove(KmlmtPersonCostCalculation.class, 
				new KmlmpPersonCostCalculationPK(personCostCalculation.getCompanyID(), personCostCalculation.getHistoryID()));
	}
	
	/**
	 * convert PersonCostCalculation Entity Object to PersonCostCalculation Domain Object
	 * @param kmlmtPersonCostCalculation PersonCostCalculation Entity Object
	 * @return PersonCostCalculation Domain Object
	 */
	private PersonCostCalculation toDomainPersonCostCalculation(KmlmtPersonCostCalculation kmlmtPersonCostCalculation){
		return new PersonCostCalculation(
				kmlmtPersonCostCalculation.kmlmpPersonCostCalculationPK.companyID, 
				kmlmtPersonCostCalculation.kmlmpPersonCostCalculationPK.historyID, 
				kmlmtPersonCostCalculation.startDate, 
				kmlmtPersonCostCalculation.endDate,
				EnumAdaptor.valueOf(kmlmtPersonCostCalculation.unitPrice, UnitPrice.class), 
				new Memo(kmlmtPersonCostCalculation.memo),
				kmlmtPersonCostCalculation.kmlstPremiumSets.stream().map(x -> toDomainPremiumSetting(x)).collect(Collectors.toList()));
	}
	
	/**
	 * convert PremiumSetting Entity Object to PremiumSetting Domain Object
	 * @param kmlstPremiumSet PremiumSetting Entity Object
	 * @return PremiumSetting Domain Object
	 */
	private PremiumSetting toDomainPremiumSetting(KmlstPremiumSet kmlstPremiumSet) {
		return new PremiumSetting(
				kmlstPremiumSet.kmlspPremiumSet.companyID, 
				kmlstPremiumSet.kmlspPremiumSet.historyID, 
				kmlstPremiumSet.kmlspPremiumSet.premiumID, 
				new PremiumRate(kmlstPremiumSet.premiumRate),
				kmlstPremiumSet.kmnmtPremiumItem.attendanceID,
				new PremiumName(kmlstPremiumSet.kmnmtPremiumItem.name),
				kmlstPremiumSet.kmnmtPremiumItem.displayNumber,
				EnumAdaptor.valueOf(kmlstPremiumSet.kmnmtPremiumItem.useAtr, UseAttribute.class),
				kmlstPremiumSet.kmldtPremiumAttendances.stream().map(x -> x.kmldpPremiumAttendancePK.attendanceID).collect(Collectors.toList()));
	}
	
	/**
	 * convert PersonCostCalculation Domain Object to PersonCostCalculation Entity Object
	 * @param personCostCalculation PersonCostCalculation Domain Object
	 * @return PersonCostCalculation Entity Object
	 */
	private KmlmtPersonCostCalculation toPersonCostCalculationEntity(PersonCostCalculation personCostCalculation) {
		return new KmlmtPersonCostCalculation(
				new KmlmpPersonCostCalculationPK(
						personCostCalculation.getCompanyID(), 
						personCostCalculation.getHistoryID()
				), 
				personCostCalculation.getStartDate(), 
				personCostCalculation.getEndDate(),
				personCostCalculation.getUnitPrice().value, 
				personCostCalculation.getMemo().v(), 
				personCostCalculation.getPremiumSettings().stream().map(x -> toPremiumSetEntity(x)).collect(Collectors.toList()));
	}
	
	/**
	 * convert PremiumSetting Domain Object to PremiumSetting Entity Object
	 * @param premiumSetting PremiumSetting Domain Object
	 * @return PremiumSetting Entity Object
	 */
	private KmlstPremiumSet toPremiumSetEntity(PremiumSetting premiumSetting) {
		return new KmlstPremiumSet(
				new KmlspPremiumSetPK(
						premiumSetting.getCompanyID(), 
						premiumSetting.getHistoryID(), 
						premiumSetting.getPremiumID()
				), 
				premiumSetting.getRate().v(),
				null,
				toPremiumItemEntity(premiumSetting),
				premiumSetting.getAttendanceItems().stream().map(x -> toPremiumAttendanceEntity(
								premiumSetting.getCompanyID(), 
								premiumSetting.getHistoryID(), 
								premiumSetting.getPremiumID(), 
								x)
						).collect(Collectors.toList())
				);
	}
	
	/**
	 * convert PremiumAttendance Domain Object to PremiumAttendance Entity Object
	 * @param companyID company ID
	 * @param historyID history ID
	 * @param premiumID premium ID
	 * @param attendanceID attendance ID
	 * @return PremiumAttendance Entity Object
	 */
	private KmldtPremiumAttendance toPremiumAttendanceEntity(String companyID, String historyID, Integer premiumID, Integer attendanceID){
		return new KmldtPremiumAttendance(
				new KmldpPremiumAttendancePK(companyID, historyID, premiumID, attendanceID),
				null);
	}
	
	/**
	 * create PremiumItem Entity Object from PremiumSetting Domain Object
	 * @param premiumSetting PremiumSetting Domain Object
	 * @return PremiumItem Entity Object
	 */
	private KmnmtPremiumItem toPremiumItemEntity(PremiumSetting premiumSetting){
		return new KmnmtPremiumItem(
				new KmnmpPremiumItemPK(
						premiumSetting.getCompanyID(), 
						premiumSetting.getPremiumID()), 
				premiumSetting.getAttendanceID(),
				premiumSetting.getName().v(), 
				premiumSetting.getDisplayNumber(), 
				premiumSetting.getUseAtr().value);
	}
}
