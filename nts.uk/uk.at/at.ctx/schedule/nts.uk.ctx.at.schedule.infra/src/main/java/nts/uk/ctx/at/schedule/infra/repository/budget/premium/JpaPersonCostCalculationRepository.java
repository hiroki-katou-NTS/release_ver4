package nts.uk.ctx.at.schedule.infra.repository.budget.premium;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
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
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * 
 * @author Doan Duy Hung
 *
 */

@Stateless
public class JpaPersonCostCalculationRepository extends JpaRepository implements PersonCostCalculationRepository{

	private static final String SEL_BY_CID = "SELECT a FROM KmlmtPersonCostCalculation a WHERE a.kmlmpPersonCostCalculationPK.companyID = :companyID ORDER BY a.startDate ASC";
	
	private static final String SEL_ITEM_BY_DATE = "SELECT a FROM KmlmtPersonCostCalculation a WHERE a.kmlmpPersonCostCalculationPK.companyID = :companyID AND a.startDate = :startDate";
	
	private static final String SEL_ITEM_BY_HID = "SELECT a FROM KmlmtPersonCostCalculation a WHERE a.kmlmpPersonCostCalculationPK.companyID = :companyID AND a.kmlmpPersonCostCalculationPK.historyID = :historyID";
	
	private static final String SEL_ITEM_BEFORE = "SELECT a FROM KmlmtPersonCostCalculation a WHERE a.kmlmpPersonCostCalculationPK.companyID = :companyID AND a.startDate < :startDate ORDER BY a.startDate DESC";
	
	private static final String SEL_ITEM_AFTER = "SELECT a FROM KmlmtPersonCostCalculation a WHERE a.kmlmpPersonCostCalculationPK.companyID = :companyID AND a.startDate > :startDate";
	
	private static final String SEL_PRE_ATTEND = "SELECT a FROM KmldtPremiumAttendance a "
			+ "WHERE a.kmldpPremiumAttendancePK.companyID = :companyID "
			+ "AND a.kmldpPremiumAttendancePK.historyID = :historyID "
			+ "AND a.kmldpPremiumAttendancePK.displayNumber = :displayNumber "
			+ "AND a.kmldpPremiumAttendancePK.attendanceID = :attendanceID ";
//	private static final String FIND_BY_DISPLAY_NUMBER;
//	static{
//		StringBuilder query = new StringBuilder();
//		query.append("SELECT a FROM KmlmtPersonCostCalculation a WHERE a.kmlmpPersonCostCalculationPK.companyID = :companyID");
//		query.append(" AND a.startDate <= :date");
//		query.append(" AND a.endDate >= :date");
//		FIND_BY_DISPLAY_NUMBER = query.toString();
//	}
	@Override
	public void add(PersonCostCalculation personCostCalculation) {
		this.commandProxy().insert(toPersonCostCalculationEntity(personCostCalculation));
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<PersonCostCalculation> findByCompanyID(String companyID) {
		return this.queryProxy().query(SEL_BY_CID, KmlmtPersonCostCalculation.class).setParameter("companyID", companyID)
				.getList(x -> toSimpleDomain(x));
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<PersonCostCalculation> findItemByDate(String companyID, GeneralDate startDate) {
		return this.queryProxy().query(SEL_ITEM_BY_DATE, KmlmtPersonCostCalculation.class).setParameter("companyID", companyID).setParameter("startDate", startDate)
				.getSingle().map(x -> toDomainPersonCostCalculation(x));
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<PersonCostCalculation> findItemByHistoryID(String companyID, String historyID) {
		return this.queryProxy().query(SEL_ITEM_BY_HID, KmlmtPersonCostCalculation.class).setParameter("companyID", companyID).setParameter("historyID", historyID)
				.getSingle().map(x -> toDomainPersonCostCalculation(x));
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<PersonCostCalculation> findItemBefore(String companyID, GeneralDate startDate) {
		List<KmlmtPersonCostCalculation> result = this.queryProxy().query(SEL_ITEM_BEFORE, KmlmtPersonCostCalculation.class)
				.setParameter("companyID", companyID).setParameter("startDate", startDate).getList();
		if(result.isEmpty()) return Optional.ofNullable(null);
		return Optional.of(toDomainPersonCostCalculation(result.get(0)));
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<PersonCostCalculation> findItemAfter(String companyID, GeneralDate startDate) {
		List<KmlmtPersonCostCalculation> result = this.queryProxy().query(SEL_ITEM_AFTER, KmlmtPersonCostCalculation.class)
				.setParameter("companyID", companyID).setParameter("startDate", startDate).getList();
		if(result.isEmpty()) return Optional.empty();
		return Optional.of(toDomainPersonCostCalculation(result.get(0)));
	}

	@Override
	public void update(PersonCostCalculation personCostCalculation) {
		KmlmtPersonCostCalculation currentEntity = this.queryProxy()
				.find(new KmlmpPersonCostCalculationPK(personCostCalculation.getCompanyID(), personCostCalculation.getHistoryID()), KmlmtPersonCostCalculation.class)
				.get();
		currentEntity.setStartDate(personCostCalculation.getStartDate());
		currentEntity.setEndDate(personCostCalculation.getEndDate());
		currentEntity.setUnitPrice(personCostCalculation.getUnitPrice().value);
		currentEntity.setMemo(personCostCalculation.getMemo().v());
		if(personCostCalculation.getPremiumSettings()!=null){
			for(int i=0; i < personCostCalculation.getPremiumSettings().size(); i++){
				int id = personCostCalculation.getPremiumSettings().get(i).getDisplayNumber();
				Optional<KmlstPremiumSet> premiumSet = currentEntity.kmlstPremiumSets.stream().filter(x -> x.kmlspPremiumSet.displayNumber == id).findFirst();
				if(premiumSet.isPresent()){
					premiumSet.get().setPremiumRate(personCostCalculation.getPremiumSettings().get(i).getRate().v());
					premiumSet.get().setKmldtPremiumAttendances( 
							personCostCalculation.getPremiumSettings().get(i).getAttendanceItems()
							.stream()
							.map(x -> toPremiumAttendanceEntity(
									personCostCalculation.getCompanyID(), 
									personCostCalculation.getHistoryID(), 
									id, 
									x))
							.collect(Collectors.toList())
					);
				} else {
					PremiumSetting premiumSetting = new PremiumSetting(
							currentEntity.kmlmpPersonCostCalculationPK.companyID, 
							currentEntity.kmlmpPersonCostCalculationPK.historyID, 
							personCostCalculation.getPremiumSettings().get(i).getDisplayNumber(), 
							personCostCalculation.getPremiumSettings().get(i).getRate(), 
							personCostCalculation.getPremiumSettings().get(i).getName(), 
							personCostCalculation.getPremiumSettings().get(i).getUseAtr(), 
							personCostCalculation.getPremiumSettings().get(i).getAttendanceItems());
					currentEntity.kmlstPremiumSets.add(toPremiumSetEntity(premiumSetting)); 
				}
			}
		}
		this.commandProxy().update(currentEntity);
	}

	@Override
	public void delete(PersonCostCalculation personCostCalculation) {
		this.commandProxy().remove(KmlmtPersonCostCalculation.class, 
				new KmlmpPersonCostCalculationPK(personCostCalculation.getCompanyID(), personCostCalculation.getHistoryID()));
	}
	
	private PersonCostCalculation toSimpleDomain(KmlmtPersonCostCalculation kmlmtPersonCostCalculation){
		return new PersonCostCalculation(
				kmlmtPersonCostCalculation.kmlmpPersonCostCalculationPK.companyID, 
				kmlmtPersonCostCalculation.kmlmpPersonCostCalculationPK.historyID, 
				kmlmtPersonCostCalculation.startDate, 
				kmlmtPersonCostCalculation.endDate,
				EnumAdaptor.valueOf(kmlmtPersonCostCalculation.unitPrice, UnitPrice.class), 
				new Memo(kmlmtPersonCostCalculation.memo),
				Collections.emptyList());
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
				kmlstPremiumSet.kmlspPremiumSet.displayNumber, 
				new PremiumRate(kmlstPremiumSet.premiumRate),
				new PremiumName(kmlstPremiumSet.kmnmtPremiumItem.name),
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
						premiumSetting.getDisplayNumber()
				), 
				premiumSetting.getRate().v(),
				null,
				toPremiumItemEntity(premiumSetting),
				premiumSetting.getAttendanceItems().stream().map(x -> toPremiumAttendanceEntity(
								premiumSetting.getCompanyID(), 
								premiumSetting.getHistoryID(), 
								premiumSetting.getDisplayNumber(), 
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
	private KmldtPremiumAttendance toPremiumAttendanceEntity(String companyID, String historyID, Integer displayNumber, Integer attendanceID){
		Optional<KmldtPremiumAttendance> kmldtPremiumAttendance = this.queryProxy().query(SEL_PRE_ATTEND, KmldtPremiumAttendance.class)
				.setParameter("companyID", companyID)
				.setParameter("historyID", historyID)
				.setParameter("displayNumber", displayNumber)
				.setParameter("attendanceID", attendanceID)
				.getSingle();
		if(kmldtPremiumAttendance.isPresent()){
			return kmldtPremiumAttendance.get();
		} else {
			return new KmldtPremiumAttendance(
					new KmldpPremiumAttendancePK(
							companyID, 
							historyID, 
							displayNumber, 
							attendanceID), 
					null);
		}
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
						premiumSetting.getDisplayNumber()), 
				premiumSetting.getName().v(), 
				premiumSetting.getUseAtr().value,
				null);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<PersonCostCalculation> findByCompanyIDAndDisplayNumber(String companyID, GeneralDate date) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a, b, c FROM KmlmtPersonCostCalculation a ");
		query.append(" LEFT JOIN a.kmlstPremiumSets b ");
		query.append(" LEFT JOIN b.kmldtPremiumAttendances c ");
		query.append(" WHERE a.kmlmpPersonCostCalculationPK.companyID = :companyID");
		query.append(" AND a.startDate <= :date AND a.endDate >= :date");
		List<Object[]> entities = this.queryProxy().query(query.toString(), Object[].class)
													.setParameter("companyID", companyID)
													.setParameter("date", date).getList();
		List<KmnmtPremiumItem> premiumItem = getPremiumItems(companyID);
		List<PersonCostCalculation> personCostCals = entities.stream().collect(Collectors.groupingBy(c -> (KmlmtPersonCostCalculation) c[0], Collectors.toList()))
			.entrySet().stream().map(e -> {
				List<KmlstPremiumSet> premiumSet = e.getValue().stream().map(x -> (KmlstPremiumSet) x[1])
																.filter(ps -> ps != null && ps.kmlspPremiumSet.historyID.equals(e.getKey().kmlmpPersonCostCalculationPK.historyID))
																.distinct().collect(Collectors.toList());
				
				List<KmldtPremiumAttendance> preAttendamce = e.getValue().stream().map(x -> (KmldtPremiumAttendance) x[2])
						.filter(ps -> ps != null && ps.kmldpPremiumAttendancePK.historyID.equals(e.getKey().kmlmpPersonCostCalculationPK.historyID))
						.distinct().collect(Collectors.toList());
				return toDomainPersonCostCalculation(e.getKey(), premiumSet, preAttendamce, premiumItem);
			}).collect(Collectors.toList());
//		List<PersonCostCalculation> personCostCals = this.queryProxy().query(FIND_BY_DISPLAY_NUMBER, KmlmtPersonCostCalculation.class)
//				.setParameter("companyID", companyID)
//				.setParameter("date", date).getList(c -> toDomainPersonCostCalculation(c));
		if(CollectionUtil.isEmpty(personCostCals)){
			return null;
		}
		return personCostCals;
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<PersonCostCalculation> findByCompanyIDAndDisplayNumberNotFull(String companyID, DatePeriod date, List<Integer> itemNos) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a.HIS_ID, a.START_DATE, a.END_DATE, b.PREMIUM_RATE, b.PREMIUM_NO, c.ATTENDANCE_ID FROM KMLMT_COST_CALC_SET a ");
		query.append(" LEFT JOIN KMLST_PREMIUM_SET b ");
		query.append(" ON a.CID = b.CID AND a.HIS_ID = b.HIS_ID");
		query.append(" LEFT JOIN KMLDT_PREMIUM_ATTENDANCE c ");
		query.append(" ON c.CID = b.CID AND c.HIS_ID = b.HIS_ID AND c.PREMIUM_NO = b.PREMIUM_NO");
		query.append(" WHERE a.CID = ?");
		query.append(" AND a.START_DATE <= ? AND a.END_DATE >= ?");
		query.append(" and b.PREMIUM_NO in (" + itemNos.stream().map(s -> "?").collect(Collectors.joining(",")) + ")");
		try (PreparedStatement statement = this.connection().prepareStatement(query.toString())) {

			statement.setString(1, companyID);
			statement.setDate(2, Date.valueOf(date.end().localDate()));
			statement.setDate(3, Date.valueOf(date.start().localDate()));
			for(int i = 0; i < itemNos.size(); i++){
				statement.setInt(i + 4, itemNos.get(i));
			}
			
			return new NtsResultSet(statement.executeQuery()).getList(rec -> {
				Map<String, Object> val = new HashMap<>();
				val.put("HIS_ID", rec.getString("HIS_ID"));
				val.put("PREMIUM_NO", rec.getInt("PREMIUM_NO"));
				val.put("PREMIUM_RATE", rec.getInt("PREMIUM_RATE"));
				val.put("ATTENDANCE_ID", rec.getInt("ATTENDANCE_ID"));
				val.put("END_DATE", rec.getGeneralDate("END_DATE"));
				val.put("START_DATE", rec.getGeneralDate("START_DATE"));
				return val;
			}).stream().collect(Collectors.groupingBy(c -> (String) c.get("HIS_ID"), Collectors.toList())).entrySet()
			.stream().map(et -> {
				List<PremiumSetting> premiumSettings = et.getValue().stream().collect(Collectors.groupingBy(r -> (Integer) r.get("PREMIUM_NO"), 
																		Collectors.toList())).entrySet().stream().map(ps -> {
							return new PremiumSetting(companyID, et.getKey(), ps.getKey(), 
									new PremiumRate((Integer) ps.getValue().get(0).get("PREMIUM_RATE")), null, null, 
									 ps.getValue().stream().map(at -> (Integer) at.get("ATTENDANCE_ID")).filter(at -> at != null)
									 	.collect(Collectors.toList()));
						}).collect(Collectors.toList());
				return new PersonCostCalculation(companyID, et.getKey(), (GeneralDate) et.getValue().get(0).get("START_DATE"), 
						(GeneralDate) et.getValue().get(0).get("END_DATE"), null, null, premiumSettings);
			}).collect(Collectors.toList());
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<PremiumSetting> findPremiumSettingBy(String companyID, GeneralDate date) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT b, c FROM KmlmtPersonCostCalculation a ");
		query.append(" LEFT JOIN a.kmlstPremiumSets b ");
		query.append(" LEFT JOIN b.kmldtPremiumAttendances c ");
		query.append(" WHERE a.kmlmpPersonCostCalculationPK.companyID = :companyID");
		query.append(" AND a.startDate <= :date AND a.endDate >= :date");
		List<Object[]> entities = this.queryProxy().query(query.toString(), Object[].class)
													.setParameter("companyID", companyID)
													.setParameter("date", date).getList();
		List<KmnmtPremiumItem> premiumItem = getPremiumItems(companyID);
		List<PremiumSetting> premiumSettings = entities.stream().collect(Collectors.groupingBy(c -> (KmlstPremiumSet) c[0], Collectors.toList()))
			.entrySet().stream().map(e -> {
				List<KmldtPremiumAttendance> preAttendamce = e.getValue().stream().map(x -> (KmldtPremiumAttendance) x[1])
						.filter(ps -> ps != null && ps.kmldpPremiumAttendancePK.historyID.equals(e.getKey().kmlspPremiumSet.historyID))
						.distinct().collect(Collectors.toList());
				return toDomainPremiumSetting(e.getKey(), premiumItem, preAttendamce);
			}).collect(Collectors.toList());
//		List<PersonCostCalculation> personCostCals = this.queryProxy().query(FIND_BY_DISPLAY_NUMBER, KmlmtPersonCostCalculation.class)
//				.setParameter("companyID", companyID)
//				.setParameter("date", date).getList(c -> toDomainPersonCostCalculation(c));
		if(CollectionUtil.isEmpty(premiumSettings)){
			return null;
		}
		return premiumSettings;
	}

	private List<KmnmtPremiumItem> getPremiumItems(String comId) {
		try (PreparedStatement statement = this.connection().prepareStatement(
					"select * FROM KMNMT_PREMIUM_ITEM where CID = ? ")) {

			statement.setString(1, comId);
			List<KmnmtPremiumItem> krcdtTimeLeaveWorks = new NtsResultSet(statement.executeQuery()).getList(rec -> {
				val entity = new KmnmtPremiumItem();
				entity.kmnmpPremiumItemPK = new KmnmpPremiumItemPK();
				entity.kmnmpPremiumItemPK.companyID = comId;
				entity.kmnmpPremiumItemPK.displayNumber = rec.getInt("PREMIUM_NO");
				entity.name = rec.getString("PREMIUM_NAME");
				entity.useAtr = rec.getInt("USE_ATR");
				return entity;
			});
			
			return krcdtTimeLeaveWorks;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private PersonCostCalculation toDomainPersonCostCalculation(KmlmtPersonCostCalculation kmlmtPersonCostCalculation, 
			List<KmlstPremiumSet> premiumSet, List<KmldtPremiumAttendance> attendanceItems, List<KmnmtPremiumItem> premiumItem){
		return new PersonCostCalculation(
				kmlmtPersonCostCalculation.kmlmpPersonCostCalculationPK.companyID, 
				kmlmtPersonCostCalculation.kmlmpPersonCostCalculationPK.historyID, 
				kmlmtPersonCostCalculation.startDate, 
				kmlmtPersonCostCalculation.endDate,
				EnumAdaptor.valueOf(kmlmtPersonCostCalculation.unitPrice, UnitPrice.class), 
				new Memo(kmlmtPersonCostCalculation.memo),
				premiumSet.stream().map(x -> toDomainPremiumSetting(x, premiumItem, attendanceItems)).collect(Collectors.toList()));
	}
	
	/**
	 * convert PremiumSetting Entity Object to PremiumSetting Domain Object
	 * @param kmlstPremiumSet PremiumSetting Entity Object
	 * @return PremiumSetting Domain Object
	 */
	private PremiumSetting toDomainPremiumSetting(KmlstPremiumSet kmlstPremiumSet, List<KmnmtPremiumItem> premiumItem, List<KmldtPremiumAttendance> attendanceItems) {
		KmnmtPremiumItem item = premiumItem.stream().filter(x -> x.kmnmpPremiumItemPK.displayNumber == kmlstPremiumSet.kmlspPremiumSet.displayNumber)
				.findFirst().get();
		return new PremiumSetting(
				kmlstPremiumSet.kmlspPremiumSet.companyID, 
				kmlstPremiumSet.kmlspPremiumSet.historyID, 
				kmlstPremiumSet.kmlspPremiumSet.displayNumber, 
				new PremiumRate(kmlstPremiumSet.premiumRate),
				new PremiumName(item.name),
				EnumAdaptor.valueOf(item.useAtr, UseAttribute.class),
				attendanceItems.stream().filter(x -> x.kmldpPremiumAttendancePK.displayNumber == kmlstPremiumSet.kmlspPremiumSet.displayNumber)
										.map(x -> x.kmldpPremiumAttendancePK.attendanceID).collect(Collectors.toList()));
	}
}
