/**
 * 9:59:33 AM Jun 13, 2017
 */
package nts.uk.ctx.at.schedule.infra.repository.shift.businesscalendar.holiday;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.holiday.PublicHoliday;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.holiday.PublicHolidayRepository;
import nts.uk.ctx.at.schedule.infra.entity.shift.businesscalendar.holiday.KsmmtPublicHoliday;
import nts.uk.ctx.at.schedule.infra.entity.shift.businesscalendar.holiday.KsmmtPublicHolidayPK;


/**
 * @author hungnm
 *
 */
@Stateless
public class JpaPublicHolidayRepository extends JpaRepository implements PublicHolidayRepository {

	private static final String SELECT_BY_LISTDATE = "SELECT a FROM KsmmtPublicHoliday a WHERE a.ksmmtPublicHolidayPK.companyId = :companyId AND a.ksmmtPublicHolidayPK.date IN :lstDate";
	private static final String SELECT_BY_DATE = "SELECT a FROM KsmmtPublicHoliday a "
			+ " WHERE a.ksmmtPublicHolidayPK.companyId = :companyId "
			+ " AND a.ksmmtPublicHolidayPK.date = :date ";
	private static final String SELECT_ALL = "SELECT a FROM KsmmtPublicHoliday a WHERE a.ksmmtPublicHolidayPK.companyId = :companyId";
//	private static final String SELECT_SINGLE = "SELECT a FROM KsmmtPublicHoliday a WHERE a.ksmmtPublicHolidayPK.companyId = :companyID AND a.ksmmtPublicHolidayPK.date = :date";
	private static final String SELECT_BY_SDATE_EDATE = "SELECT c FROM KsmmtPublicHoliday c"
			+ " WHERE c.ksmmtPublicHolidayPK.companyId = :companyId"
			+ " AND c.ksmmtPublicHolidayPK.date >= :strDate"
			+ " AND c.ksmmtPublicHolidayPK.date <= :endDate";

	@Override
	public List<PublicHoliday> getHolidaysByListDate(String companyId, List<GeneralDate> lstDate) {
		List<PublicHoliday> resultList = new ArrayList<>();
		CollectionUtil.split(lstDate, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(SELECT_BY_LISTDATE, KsmmtPublicHoliday.class)
				.setParameter("companyId", companyId)
				.setParameter("lstDate", subList)
				.getList().stream()
				.map(entity -> toDomain(entity)).collect(Collectors.toList()));
		});
		return resultList;
	}

	@Override
	public List<PublicHoliday> getAllHolidays(String companyId) {
		return this.queryProxy().query(SELECT_ALL, KsmmtPublicHoliday.class).setParameter("companyId", companyId)
				.getList().stream().map(entity -> toDomain(entity)).collect(Collectors.toList());
	}

	private PublicHoliday toDomain(KsmmtPublicHoliday entity) {
		return PublicHoliday.createFromJavaType(entity.ksmmtPublicHolidayPK.companyId, entity.ksmmtPublicHolidayPK.date,
				entity.holidayName);
	}

	@Override
	public Optional<PublicHoliday> getHolidaysByDate(String companyID, GeneralDate date) {
		return this.queryProxy().query(SELECT_BY_DATE, KsmmtPublicHoliday.class)
				.setParameter("companyId", companyID).setParameter("date", date).getSingle(c -> toDomain(c));
	}

	@Override
	public void remove(String companyID, GeneralDate date) {
		this.commandProxy().remove(KsmmtPublicHoliday.class, new KsmmtPublicHolidayPK(companyID, date));
		this.getEntityManager().flush();
	}

	@Override
	public void update(PublicHoliday publicHoliday) {
		KsmmtPublicHoliday newEntity = toEntity(publicHoliday);
		KsmmtPublicHoliday entity = this.queryProxy().find(newEntity.ksmmtPublicHolidayPK, KsmmtPublicHoliday.class)
				.get();
		entity.holidayName = newEntity.holidayName;

	}

	@Override
	public void add(PublicHoliday publicHoliday) {
		this.commandProxy().insert(toEntity(publicHoliday));

	}

	private KsmmtPublicHoliday toEntity(PublicHoliday domain) {
		return new KsmmtPublicHoliday(new KsmmtPublicHolidayPK(domain.getCompanyId(), domain.getDate()),
				domain.getHolidayName().v());

	}
	/**
	 * get pHoliday While Date
	 * @param strDate
	 * @param companyId
	 * @param endDate
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public List<PublicHoliday> getpHolidayWhileDate(String companyId,GeneralDate strDate, GeneralDate endDate) {
		return this.queryProxy().query(SELECT_BY_SDATE_EDATE, KsmmtPublicHoliday.class)
				.setParameter("companyId", companyId)
				.setParameter("strDate", strDate)
				.setParameter("endDate", endDate)
				.getList(c->toDomain(c));
	}
}
