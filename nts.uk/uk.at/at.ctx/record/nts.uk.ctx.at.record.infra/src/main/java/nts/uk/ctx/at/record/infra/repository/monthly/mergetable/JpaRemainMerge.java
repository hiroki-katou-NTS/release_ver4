package nts.uk.ctx.at.record.infra.repository.monthly.mergetable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.monthly.mergetable.MonthMergeKey;
import nts.uk.ctx.at.record.dom.monthly.mergetable.RemainMerge;
import nts.uk.ctx.at.record.dom.monthly.mergetable.RemainMergeRepository;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.AbsenceLeaveRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.MonthlyDayoffRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.reserveleave.RsvLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.specialholiday.monthremaindata.SpecialHolidayRemainData;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonMergePk;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonRemain;

/**
 * リポジトリ実装：残数系
 * @author shuichi_ishida
 */
@Stateless
public class JpaRemainMerge extends JpaRepository implements RemainMergeRepository {

	private static final String DELETE_BY_PK = String.join(" ", "DELETE FROM KrcdtMonRemain a ",
			"WHERE  a.krcdtMonRemainPk.employeeId = :employeeId ",
			"AND    a.krcdtMonRemainPk.yearMonth = :yearMonth ",
			"AND    a.krcdtMonRemainPk.closureId = :closureId",
			"AND    a.krcdtMonRemainPk.closureDay = :closureDay",
			"AND    a.krcdtMonRemainPk.isLastDay = :isLastDay");
	
	private static final String FIND_BY_SIDS_AND_MONTHS = "SELECT a FROM KrcdtMonRemain a "
			+ "WHERE a.krcdtMonRemainPk.employeeId IN :employeeIds "
			+ "AND a.krcdtMonRemainPk.yearMonth IN :yearMonths "
			+ "ORDER BY a.krcdtMonRemainPk.employeeId, a.startDate ";
	//hoatt
	private static final String FIND_BY_YEAR_MONTH = "SELECT a FROM KrcdtMonRemain a "
			+ "WHERE a.krcdtMonRemainPk.employeeId = :employeeId "
			+ "AND a.krcdtMonRemainPk.yearMonth = :yearMonth "
			+ "ORDER BY a.startDate ";
	private static final String FIND_BY_LIST_YRMON = "SELECT a FROM KrcdtMonRemain a "
			+ "WHERE a.krcdtMonRemainPk.employeeId = :employeeId "
			+ "AND a.krcdtMonRemainPk.yearMonth IN :lstyrMon "
			+ "ORDER BY a.startDate ";
	/** 検索 */
	@Override
	public Optional<RemainMerge> find(MonthMergeKey key) {
		
		return this.queryProxy()
				.find(new KrcdtMonMergePk(
						key.getEmployeeId(),
						key.getYearMonth().v(),
						key.getClosureId().value,
						key.getClosureDate().getClosureDay().v(),
						(key.getClosureDate().getLastDayOfMonth() ? 1 : 0)),
						KrcdtMonRemain.class)
				.map(c -> c.toDomain());
	}
	
	/** 検索　（社員IDリストと年月リスト） */
	@Override
	public List<RemainMerge> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths) {
		
		val yearMonthValues = yearMonths.stream().map(c -> c.v()).collect(Collectors.toList());
		
		List<RemainMerge> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			results.addAll(this.queryProxy().query(FIND_BY_SIDS_AND_MONTHS, KrcdtMonRemain.class)
											.setParameter("employeeIds", splitData)
											.setParameter("yearMonths", yearMonthValues)
											.getList(c -> c.toDomain()));
		});
		return results;
	}
	
	/** 登録および更新 */
	@Override
	public void persistAndUpdate(MonthMergeKey key, RemainMerge domains) {
		
		// キー
		val entityKey = new KrcdtMonMergePk(
				key.getEmployeeId(),
				key.getYearMonth().v(),
				key.getClosureId().value,
				key.getClosureDate().getClosureDay().v(),
				(key.getClosureDate().getLastDayOfMonth() ? 1 : 0));
		
		// 登録・更新
		KrcdtMonRemain entity = this.getEntityManager().find(KrcdtMonRemain.class, entityKey);
		if (entity == null){
			entity = new KrcdtMonRemain();
			entity.setKrcdtMonRemainPk(entityKey);
			entity.toEntityRemainMerge(domains);
			this.getEntityManager().persist(entity);
		}
		else {
			entity.toEntityRemainMerge(domains);
		}
	}
	
	/** 削除 */
	@Override
	public void remove(MonthMergeKey key) {
		
		this.getEntityManager().createQuery(DELETE_BY_PK)
				.setParameter("employeeId", key.getEmployeeId())
				.setParameter("yearMonth", key.getYearMonth().v())
				.setParameter("closureId", key.getClosureId().value)
				.setParameter("closureDay", key.getClosureDate().getClosureDay().v())
				.setParameter("isLastDay", (key.getClosureDate().getLastDayOfMonth() ? 1 : 0))
				.executeUpdate();
	}
	/**
	 * @author hoatt
	 * Doi ung response KDR001 - RQ260
	 * @param employeeId
	 * @param yearMonth
	 * @return
	 */
	@Override
	public List<AbsenceLeaveRemainData> findByYearMonthRQ260(String employeeId, YearMonth yearMonth) {
		return this.queryProxy().query(FIND_BY_YEAR_MONTH, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.getList(c -> c.toDomainAbsenceLeaveRemainData());
	}
	/**
	 * @author hoatt
	 * Doi ung response KDR001 - RQ263
	 * @param employeeId
	 * @param yearMonth
	 * @return
	 */
	@Override
	public List<SpecialHolidayRemainData> findByYearMonthRQ263(String employeeId, YearMonth yearMonth) {
		val entitys = this.queryProxy().query(FIND_BY_YEAR_MONTH, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.getList();
		List<SpecialHolidayRemainData> results = new ArrayList<>();
		for (val entity : entitys) results.addAll(entity.toDomainSpecialHolidayRemainList());
		return results;
	}
	/**
	 * @author hoatt
	 * Doi ung response KDR001 - RQ259
	 * @param employeeId
	 * @param yearMonth
	 * @return
	 */
	@Override
	public List<MonthlyDayoffRemainData> findByYearMonthRQ259(String employeeId, YearMonth yearMonth) {
		return this.queryProxy().query(FIND_BY_YEAR_MONTH, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.getList(c -> c.toDomainMonthlyDayoffRemainData());
	}
	/**
	 * @author hoatt
	 * Doi ung response KDR001 - RQ258
	 * @param lstSID
	 * @param lstYrMon
	 * @return
	 */
	@Override
	public List<RsvLeaRemNumEachMonth> findByYearMonthRQ258(List<String> lstSID, List<YearMonth> lstYrMon) {
		if(lstSID.isEmpty() || lstYrMon.isEmpty()){
			return new ArrayList<>();
		}
		val yearMonthVal = lstYrMon.stream().map(c -> c.v()).collect(Collectors.toList());
		List<KrcdtMonRemain> results = new ArrayList<>();
		CollectionUtil.split(lstSID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			CollectionUtil.split(yearMonthVal, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstYearMonth -> {
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
		return results.stream().map(c -> c.toDomainRsvLeaRemNumEachMonth()).collect(Collectors.toList());
	}
	/**
	 * @author hoatt
	 * Doi ung response KDR001 - RQ255
	 * @param lstSID
	 * @param lstYrMon
	 * @return
	 */
	@Override
	public List<AnnLeaRemNumEachMonth> findByYearMonthRQ255(List<String> lstSID, List<YearMonth> lstYrMon) {
		if(lstSID.isEmpty() || lstYrMon.isEmpty()){
			return new ArrayList<>();
		}
		val yearMonthValues = lstYrMon.stream().map(c -> c.v()).collect(Collectors.toList());
		List<KrcdtMonRemain> results = new ArrayList<>();
		CollectionUtil.split(lstSID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
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
		return results.stream().map(c -> c.toDomainAnnLeaRemNumEachMonth()).collect(Collectors.toList());
	}

	@Override
	public Map<YearMonth, List<RemainMerge>> findBySidsAndYrMons(String employeeId, List<YearMonth> lstYrMon) {
		if(lstYrMon.isEmpty()){
			return new HashMap<>();
		}
		val lstYrMonVal = lstYrMon.stream().map(c -> c.v()).collect(Collectors.toList());
		Map<YearMonth, List<RemainMerge>> mapResult = new HashMap<>();
		List<RemainMerge> lstTmp = this.queryProxy().query(FIND_BY_LIST_YRMON, KrcdtMonRemain.class)
										.setParameter("employeeId", employeeId)
										.setParameter("lstyrMon", lstYrMonVal)
										.getList(c -> c.toDomain());
		for (YearMonth yearMon : lstYrMon) {
			List<RemainMerge> lstFl = lstTmp.stream()
					.filter(c -> c.getMonthMergeKey().getYearMonth().equals(yearMon))
					.collect(Collectors.toList());
			mapResult.put(yearMon, lstFl);
		}
		return mapResult;
	}
}
