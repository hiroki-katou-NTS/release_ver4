package nts.uk.ctx.at.schedule.infra.repository.shift.businesscalendar.daycalendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.CalendarCompany;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.CalendarCompanyRepository;
import nts.uk.ctx.at.schedule.infra.entity.shift.businesscalendar.daycalendar.KsmmtCalendarCompany;
import nts.uk.ctx.at.schedule.infra.entity.shift.businesscalendar.daycalendar.KsmmtCalendarCompanyPK;

@Stateless
public class JpaCalendarCompanyRepository  extends JpaRepository implements CalendarCompanyRepository {
	
	private final String SELECT_FROM_COMPANY = "SELECT c FROM KsmmtCalendarCompany c";
	private final String SELECT_ALL_COMPANY = SELECT_FROM_COMPANY
			+ " WHERE c.ksmmtCalendarCompanyPK.companyId = :companyId";
	private final String SELECT_COMPANY_BY_DATE = SELECT_ALL_COMPANY
			+ " AND c.ksmmtCalendarCompanyPK.date = :date";
	private final String SELECT_BY_YEAR_MONTH = SELECT_ALL_COMPANY 
			+ " AND c.ksmmtCalendarCompanyPK.date >= :startDate "
			+ " AND c.ksmmtCalendarCompanyPK.date <= :endDate";
	private final String DELETE_BY_YEAR_MONTH = "DELETE FROM KsmmtCalendarCompany c "
			+ " WHERE c.ksmmtCalendarCompanyPK.companyId = :companyId"
			+ " AND c.ksmmtCalendarCompanyPK.date >= :startDate "
			+ " AND c.ksmmtCalendarCompanyPK.date <= :endDate";
	
	/**
	 * toDomanin calendar company
	 * @param entity
	 * @return
	 */
	private static CalendarCompany toDomainCalendarCompany(KsmmtCalendarCompany entity){
		val domain = CalendarCompany.createFromJavaType(
				entity.ksmmtCalendarCompanyPK.companyId,
				entity.ksmmtCalendarCompanyPK.date, 
				entity.workingDayAtr);
		return domain;
	}
	/**
	 * toEntity calendar Company
	 * @param domain
	 * @return
	 */
	private static KsmmtCalendarCompany toEntityCalendarCompany(CalendarCompany domain){
		val entity = new KsmmtCalendarCompany();
		entity.ksmmtCalendarCompanyPK = new KsmmtCalendarCompanyPK(
												domain.getCompanyId(),
												domain.getDate());
		entity.workingDayAtr = domain.getWorkingDayAtr().value;
		return entity;
	}
	
	@Override
	public List<Integer> getCalendarCompanySetByYear(String companyId, String year) {
		List<Integer> monthSet =  new ArrayList<>();
		for(int i=1;i<=12;i++){
			List<CalendarCompany> result = getCalendarCompanyByYearMonth(companyId, String.format(year+"%02d",i));
			if(!result.isEmpty()){
				monthSet.add(i);
			}
		}
		return monthSet;
	}
	
	/**
	 *  get all calendar company
	 */
	@Override
	public List<CalendarCompany> getAllCalendarCompany(String companyId) {
		return this.queryProxy().query(SELECT_ALL_COMPANY,KsmmtCalendarCompany.class)
				.setParameter("companyId", companyId)
				.getList(c -> toDomainCalendarCompany(c));
	}
	/**
	 * add calendar company
	 */
	@Override
	public void addCalendarCompany(CalendarCompany calendarCompany) {
		this.commandProxy().insert(toEntityCalendarCompany(calendarCompany));
		
	}
	/**
	 * update calendar company
	 */
	@Override
	public void updateCalendarCompany(CalendarCompany calendarCompany) {
		KsmmtCalendarCompany calendarCom = toEntityCalendarCompany(calendarCompany);
		KsmmtCalendarCompany companyUpdate = this.queryProxy()
				.find(calendarCom.ksmmtCalendarCompanyPK, KsmmtCalendarCompany.class).get();
		companyUpdate.workingDayAtr = calendarCompany.getWorkingDayAtr().value;
		this.commandProxy().update(companyUpdate);
		
	}
	/**
	 * delete calendar company
	 */
	@Override
	public void deleteCalendarCompany(String companyId,GeneralDate date) {
		KsmmtCalendarCompanyPK ksmmtCalendarCompanyPK = new KsmmtCalendarCompanyPK(
														companyId,date);
		this.commandProxy().remove(KsmmtCalendarCompany.class,ksmmtCalendarCompanyPK);
	}
	/**
	 * find clendar company by dateId
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<CalendarCompany> findCalendarCompanyByDate(String companyId, GeneralDate date) {
		return this.queryProxy().query(SELECT_COMPANY_BY_DATE,KsmmtCalendarCompany.class)
				.setParameter("companyId", companyId)
				.setParameter("date", date)
				.getSingle(c->toDomainCalendarCompany(c));
	}
	/**
	 * get calendar company by yearmonth
	 */
	@Override
	public List<CalendarCompany> getCalendarCompanyByYearMonth(String companyId, String yearMonth) {
		return this.queryProxy().query(SELECT_BY_YEAR_MONTH, KsmmtCalendarCompany.class)
				.setParameter("companyId", companyId)
				.setParameter("startDate", GeneralDate.fromString((String.format(Integer.parseInt(yearMonth)/100 +"/" +"%02d",Integer.parseInt(yearMonth)%100) +"/01"),"yyyy/MM/dd"))
				.setParameter("endDate", GeneralDate.fromString((String.format(Integer.parseInt(yearMonth)/100 +"/" +"%02d",Integer.parseInt(yearMonth)%100) +"/31"),"yyyy/MM/dd"))
				.getList(x -> toDomainCalendarCompany(x));
	}
	/**
	 * delete calendar company by yearmonth
	 */
	@Override
	public void deleteCalendarCompanyByYearMonth(String companyId, String yearMonth) {
		this.getEntityManager().createQuery(DELETE_BY_YEAR_MONTH)
			.setParameter("companyId", companyId)
			.setParameter("startDate", GeneralDate.fromString((String.format(Integer.parseInt(yearMonth)/100 +"/" +"%02d",Integer.parseInt(yearMonth)%100) +"/01"),"yyyy/MM/dd"))
			.setParameter("endDate", GeneralDate.fromString((String.format(Integer.parseInt(yearMonth)/100 +"/" +"%02d",Integer.parseInt(yearMonth)%100) +"/31"),"yyyy/MM/dd")).executeUpdate();
	}
	

}
