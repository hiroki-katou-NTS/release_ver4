package nts.uk.ctx.at.record.infra.repository.monthly.specialholiday;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialHolidayRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialHolidayRemainDataRepository;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonMergePk;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonRemain;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

@Stateless
public class JpaSpecialHolidayRemainDataRepo extends JpaRepository implements SpecialHolidayRemainDataRepository{
	
	private static final String SQL_BY_YM_STATUS = "SELECT c FROM KrcdtMonRemain c"
			+ " WHERE c.krcdtMonRemainPk.employeeId = :sid"
			+ " AND c.krcdtMonRemainPk.yearMonth = :ym"
			+ " AND c.closureStatus = :status";
	
	private static final String SQL_BY_YM_STATUS_CODE = "SELECT c FROM KrcdtMonRemain c"
			+ " WHERE c.krcdtMonRemainPk.employeeId = :sid"
			+ " AND c.krcdtMonRemainPk.yearMonth = :ym"
			+ " AND c.closureStatus = :status";
	private static final String SQL_BY_YM_CODE = "SELECT c FROM KrcdtMonRemain c"
			+ " WHERE c.krcdtMonRemainPk.employeeId = :sid"
			+ " AND c.krcdtMonRemainPk.yearMonth = :ym";
	
	private static final String FIND_BY_CLOSURE = "SELECT a FROM KrcdtMonRemain a "
			+ "WHERE a.krcdtMonRemainPk.employeeId = :employeeId "
			+ "AND a.krcdtMonRemainPk.yearMonth = :yearMonth "
			+ "AND a.krcdtMonRemainPk.closureId = :closureId "
			+ "AND a.krcdtMonRemainPk.closureDay = :closureDay "
			+ "AND a.krcdtMonRemainPk.isLastDay = :chkLastDay "
			+ "ORDER BY a.startDate ";
	
	private static final String FIND_BY_YEAR_MONTH = "SELECT a FROM KrcdtMonRemain a "
			+ "WHERE a.krcdtMonRemainPk.employeeId = :employeeId "
			+ "AND a.krcdtMonRemainPk.yearMonth = :yearMonth "
			+ "ORDER BY a.startDate ";

	private static final String FIND_BY_SIDS_AND_MONTHS = "SELECT a FROM KrcdtMonRemain a "
			+ "WHERE a.krcdtMonRemainPk.employeeId IN :employeeIds "
			+ "AND a.krcdtMonRemainPk.yearMonth IN :yearMonths "
			+ "ORDER BY a.krcdtMonRemainPk.employeeId, a.startDate ";

//	private static final String DELETE_BY_CLOSURE = "DELETE FROM KrcdtMonRemain a "
//			+ "WHERE a.krcdtMonRemainPk.employeeId = :employeeId "
//			+ "AND a.krcdtMonRemainPk.yearMonth = :yearMonth "
//			+ "AND a.krcdtMonRemainPk.closureId = :closureId "
//			+ "AND a.krcdtMonRemainPk.closureDay = :closureDay "
//			+ "AND a.krcdtMonRemainPk.isLastDay = :chkLastDay ";

//	private static final String DELETE_BY_YEAR_MONTH = "DELETE FROM KrcdtMonRemain a "
//			+ "WHERE a.krcdtMonRemainPk.employeeId = :employeeId "
//			+ "AND a.krcdtMonRemainPk.yearMonth = :yearMonth ";
	
	/** 取得 */
	@Override
	public List<SpecialHolidayRemainData> getByYmStatus(String sid, YearMonth ym, ClosureStatus status) {
		
		val entitys = this.queryProxy().query(SQL_BY_YM_STATUS, KrcdtMonRemain.class)
				.setParameter("sid", sid)
				.setParameter("ym", ym.v())
				.setParameter("status", status.value)				
				.getList();
		List<SpecialHolidayRemainData> results = new ArrayList<>();
		for (val entity : entitys) results.addAll(entity.toDomainSpecialHolidayRemainList());
		return results;
	}
	
	/** 検索 */
	// add 2018.9.13 shuichi_ishida
	@Override
	public List<SpecialHolidayRemainData> find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		
		val entityOpt = this.queryProxy().query(FIND_BY_CLOSURE, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.setParameter("closureId", closureId.value)
				.setParameter("closureDay", closureDate.getClosureDay().v())
				.setParameter("chkLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0))
				.getSingle();
		if (!entityOpt.isPresent()) return new ArrayList<>();
		return entityOpt.get().toDomainSpecialHolidayRemainList();
	}
	
	/** 検索 */
	// add 2018.8.24 shuichi_ishida
	@Override
	public List<SpecialHolidayRemainData> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth) {
		
		val entitys = this.queryProxy().query(FIND_BY_YEAR_MONTH, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.getList();
		List<SpecialHolidayRemainData> results = new ArrayList<>();
		for (val entity : entitys) results.addAll(entity.toDomainSpecialHolidayRemainList());
		return results;
	}
	
	/** 検索 */
	// add 2018.8.30 shuichi_ishida
	@Override
	public List<SpecialHolidayRemainData> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths) {
		
		val yearMonthValues = yearMonths.stream().map(c -> c.v()).collect(Collectors.toList());
		
		List<KrcdtMonRemain> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			CollectionUtil.split(yearMonthValues, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstYearMonth -> {
				results.addAll(this.queryProxy().query(FIND_BY_SIDS_AND_MONTHS, KrcdtMonRemain.class)
						.setParameter("employeeIds", splitData)
						.setParameter("yearMonths", lstYearMonth)
						.getList());
			});
		});
		results.sort((o1, o2) -> {
			int tmp = o1.getKrcdtMonRemainPk().getEmployeeId().compareTo(o2.getKrcdtMonRemainPk().getEmployeeId());
			if (tmp != 0) return tmp;
			return o1.getStartDate().compareTo(o2.getStartDate());
		});
		List<SpecialHolidayRemainData> items = new ArrayList<>();
		results.stream().forEach(c -> {
			items.addAll(c.toDomainSpecialHolidayRemainList());
		});
		return items;
	}

	/** 登録および更新 */
	// add 2018.8.22 shuichi_ishida
	@Override
	public void persistAndUpdate(SpecialHolidayRemainData domain) {
		
		// キー
		val key = new KrcdtMonMergePk(
				domain.getSid(),
				domain.getYm().v(),
				domain.getClosureId(),
				domain.getClosureDate().getClosureDay().v(),
				(domain.getClosureDate().getLastDayOfMonth() ? 1 : 0));
		
		// 登録・更新
		KrcdtMonRemain entity = this.getEntityManager().find(KrcdtMonRemain.class, key);
		if (entity == null){
			entity = new KrcdtMonRemain();
			entity.setKrcdtMonRemainPk(key);
			entity.toEntitySpeRemain(domain);
			this.getEntityManager().persist(entity);
		}
		else {
			entity.toEntitySpeRemain(domain);
		}
	}
	
	/** 登録および更新 */
	// add 2018.9.30 shuichi_ishida
	@Override
	public void persistAndUpdate(List<SpecialHolidayRemainData> domains) {
		
		if (domains.isEmpty()) return;
		
		// キー
		val key = new KrcdtMonMergePk(
				domains.get(0).getSid(),
				domains.get(0).getYm().v(),
				domains.get(0).getClosureId(),
				domains.get(0).getClosureDate().getClosureDay().v(),
				(domains.get(0).getClosureDate().getLastDayOfMonth() ? 1 : 0));
		
		// 登録・更新
		KrcdtMonRemain entity = this.getEntityManager().find(KrcdtMonRemain.class, key);
		if (entity == null){
			entity = new KrcdtMonRemain();
			entity.setKrcdtMonRemainPk(key);
			entity.toEntitySpeRemains(domains);
			this.getEntityManager().persist(entity);
		}
		else {
			entity.toEntitySpeRemains(domains);
		}
	}
	
	/** 削除 */
	// add 2018.8.22 shuichi_ishida
	@Override
	public void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate,
			int specialHolidayCode) {
		
//		this.getEntityManager().createQuery(DELETE_BY_CLOSURE)
//				.setParameter("employeeId", employeeId)
//				.setParameter("yearMonth", yearMonth.v())
//				.setParameter("closureId", closureId.value)
//				.setParameter("closureDay", closureDate.getClosureDay().v())
//				.setParameter("chkLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0))
//				.executeUpdate();
		
		// キー
		val key = new KrcdtMonMergePk(
				employeeId,
				yearMonth.v(),
				closureId.value,
				closureDate.getClosureDay().v(),
				(closureDate.getLastDayOfMonth() ? 1 : 0));
		
		// 削除
		KrcdtMonRemain entity = this.getEntityManager().find(KrcdtMonRemain.class, key);
		if (entity != null) entity.deleteSpeRemain(specialHolidayCode);
	}
	
	/** 削除 */
	// add 2018.8.24 shuichi_ishida
	@Override
	public void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate) {
		
//		this.getEntityManager().createQuery(DELETE_BY_CLOSURE)
//				.setParameter("employeeId", employeeId)
//				.setParameter("yearMonth", yearMonth.v())
//				.setParameter("closureId", closureId.value)
//				.setParameter("closureDay", closureDate.getClosureDay().v())
//				.setParameter("chkLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0))
//				.executeUpdate();
		
		// キー
		val key = new KrcdtMonMergePk(
				employeeId,
				yearMonth.v(),
				closureId.value,
				closureDate.getClosureDay().v(),
				(closureDate.getLastDayOfMonth() ? 1 : 0));
		
		// 削除
		KrcdtMonRemain entity = this.getEntityManager().find(KrcdtMonRemain.class, key);
		if (entity != null) entity.deleteAllSpeRemains();
	}
	
	/** 削除　（年月） */
	// add 2018.8.22 shuichi_ishida
	@Override
	public void removeByYearMonth(String employeeId, YearMonth yearMonth) {
		
//		this.getEntityManager().createQuery(DELETE_BY_YEAR_MONTH)
//				.setParameter("employeeId", employeeId)
//				.setParameter("yearMonth", yearMonth.v())
//				.executeUpdate();
		
		val entitys = this.queryProxy().query(FIND_BY_YEAR_MONTH, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.getList();
		for (val entity : entitys) entity.deleteAllSpeRemains();
	}

	@Override
	public List<SpecialHolidayRemainData> getByYmStatus(String sid, YearMonth ym, ClosureStatus status, int speCode) {
		
		val entitys = this.queryProxy().query(SQL_BY_YM_STATUS_CODE, KrcdtMonRemain.class)
				.setParameter("sid", sid)
				.setParameter("ym", ym.v())
				.setParameter("status", status.value)
				.getList();
		List<SpecialHolidayRemainData> results = new ArrayList<>();
		for (val entity : entitys){
			val data = entity.toDomainSpecialHolidayRemain(speCode);
			if (data.isPresent()) results.add(data.get());
		}
		return results;
	}

	@Override
	public List<SpecialHolidayRemainData> getByYmCode(String sid, YearMonth ym, int speCode) {
		val entitys = this.queryProxy().query(SQL_BY_YM_CODE, KrcdtMonRemain.class)
				.setParameter("sid", sid)
				.setParameter("ym", ym.v())
				.getList();
		List<SpecialHolidayRemainData> results = new ArrayList<>();
		for (val entity : entitys){
			val data = entity.toDomainSpecialHolidayRemain(speCode);
			if (data.isPresent()) results.add(data.get());
		}
		return results;
	}
}
