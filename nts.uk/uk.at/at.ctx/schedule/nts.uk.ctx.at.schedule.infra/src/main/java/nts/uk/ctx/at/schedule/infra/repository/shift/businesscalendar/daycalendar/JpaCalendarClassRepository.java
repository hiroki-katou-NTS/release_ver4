package nts.uk.ctx.at.schedule.infra.repository.shift.businesscalendar.daycalendar;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.CalendarClass;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.daycalendar.CalendarClassRepository;
import nts.uk.ctx.at.schedule.infra.entity.shift.businesscalendar.daycalendar.KsmmtCalendarClass;
import nts.uk.ctx.at.schedule.infra.entity.shift.businesscalendar.daycalendar.KsmmtCalendarClassPK;

@Stateless
public class JpaCalendarClassRepository extends JpaRepository implements CalendarClassRepository {
	private final String SELECT_FROM_CLASS = "select l from KsmmtCalendarClass l";
	private final String SELECT_ALL_CLASS = SELECT_FROM_CLASS
			+ " where l.ksmmtCalendarClassPK.companyId = :companyId "
			+ " and l.ksmmtCalendarClassPK.classId = :classId ";
	private final String SELECT_CLASS_BY_DATE = SELECT_ALL_CLASS
			+ " and l.ksmmtCalendarClassPK.dateId = :dateId";
	private final String SELECT_BY_YEAR_MONTH = SELECT_ALL_CLASS + " and l.ksmmtCalendarClassPK.dateId >= :startYM and l.ksmmtCalendarClassPK.dateId <= :endYM";
	private final String DELETE_BY_YEAR_MONTH = "delete from KsmmtCalendarClass l where l.ksmmtCalendarClassPK.companyId = :companyId"
			+ " and l.ksmmtCalendarClassPK.classId = :classId "
			+" and l.ksmmtCalendarClassPK.dateId >= :startYM and l.ksmmtCalendarClassPK.dateId <= :endYM";
	/**
	 * class
	 * @param entity
	 * @return
	 */
	// toDomain calendar class
	private static CalendarClass toDomainCalendarClass(KsmmtCalendarClass entity){
		val domain = CalendarClass.createFromJavaType(
				entity.ksmmtCalendarClassPK.companyId,
				entity.ksmmtCalendarClassPK.classId,
				entity.ksmmtCalendarClassPK.dateId,
				entity.workingDayAtr);
		return domain;
	}
	//toEntity calendar Class
	private static KsmmtCalendarClass toEntityCalendarClass(CalendarClass domain){
		val entity = new KsmmtCalendarClass();
		entity.ksmmtCalendarClassPK = new KsmmtCalendarClassPK(
												domain.getCompanyId(),
												domain.getClassId().v(),
												domain.getDateId());
		entity.workingDayAtr = domain.getWorkingDayAtr().value;
		return entity;
	}
	
	/**
	 *  clendar Class
	 */
	//get all calendar Class
	@Override
	public List<CalendarClass> getAllCalendarClass(String companyId,String classId) {
		return this.queryProxy().query(SELECT_ALL_CLASS,KsmmtCalendarClass.class)
				.setParameter("companyId", companyId)
				.setParameter("classId", classId)
				.getList(c->toDomainCalendarClass(c));
	}

	@Override
	public void addCalendarClass(CalendarClass calendarClass) {
		this.commandProxy().insert(toEntityCalendarClass(calendarClass));
		
	}

	@Override
	public void updateCalendarClass(CalendarClass calendarClass) {
		KsmmtCalendarClass clendarCla = toEntityCalendarClass(calendarClass);
		KsmmtCalendarClass classUpdate = this.queryProxy()
				.find(clendarCla.ksmmtCalendarClassPK, KsmmtCalendarClass.class).get();
		classUpdate.workingDayAtr = calendarClass.getWorkingDayAtr().value;
		this.commandProxy().update(classUpdate);
		
	}

	@Override
	public void deleteCalendarClass(String companyId,String classId,BigDecimal dateId) {
		KsmmtCalendarClassPK ksmmtCalendarClassPK = new KsmmtCalendarClassPK(companyId,
														classId,
														dateId);  
		this.commandProxy().remove(KsmmtCalendarClass.class,ksmmtCalendarClassPK);
	}

	@Override
	public Optional<CalendarClass> findCalendarClassByDate(String companyId,String classId,BigDecimal dateId) {
		return this.queryProxy().query(SELECT_CLASS_BY_DATE,KsmmtCalendarClass.class)
				.setParameter("companyId",companyId)
				.setParameter("classId", classId)
				.setParameter("dateId", dateId)
				.getSingle(c->toDomainCalendarClass(c));
	}
	@Override
	public List<CalendarClass> getCalendarClassByYearMonth(String companyId, String classId, String yearMonth) {
		return this.queryProxy().query(SELECT_BY_YEAR_MONTH, KsmmtCalendarClass.class)
				.setParameter("companyId", companyId)
				.setParameter("classId", classId)
				.setParameter("startYM", Integer.valueOf(yearMonth+"01"))
				.setParameter("endYM", Integer.valueOf(yearMonth+"31"))
				.getList(x -> toDomainCalendarClass(x));
	}
	@Override
	public void deleteCalendarClassByYearMonth(String companyId, String classId, String yearMonth) {
		this.getEntityManager().createQuery(DELETE_BY_YEAR_MONTH)
				.setParameter("companyId", companyId)
				.setParameter("classId", classId)
				.setParameter("startYM", Integer.valueOf(yearMonth+"01"))
				.setParameter("endYM", Integer.valueOf(yearMonth+"31"))
				.executeUpdate();
	}
}
