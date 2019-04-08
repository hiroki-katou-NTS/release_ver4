package nts.uk.ctx.at.record.infra.repository.monthly;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.monthly.TimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.TimeOfMonthlyRepository;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonMerge;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonMergePk;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * リポジトリ実装：月別実績の勤怠時間
 * @author shuichi_ishida
 */
@Stateless
public class JpaTimeOfMonthly extends JpaRepository implements TimeOfMonthlyRepository {

	private static final String SEL_NO_WHERE = "SELECT a FROM KrcdtMonMerge a";
	private static final String FIND_BY_YEAR_MONTH = String.join(" ", SEL_NO_WHERE,
			"WHERE a.krcdtMonMergePk.employeeId =:employeeId",
			"AND   a.krcdtMonMergePk.yearMonth =:yearMonth",
			"ORDER BY a.startYmd");
	private static final String FIND_BY_YM_AND_CLOSURE_ID = String.join(" ", SEL_NO_WHERE,
			"WHERE a.krcdtMonMergePk.employeeId =:employeeId",
			"AND   a.krcdtMonMergePk.yearMonth =:yearMonth",
			"AND   a.krcdtMonMergePk.closureId =:closureId",
			"ORDER BY a.startYmd");
	private static final String FIND_BY_EMPLOYEES = String.join(" ", SEL_NO_WHERE,
			"WHERE a.krcdtMonMergePk.employeeId IN :employeeIds",
			"AND   a.krcdtMonMergePk.yearMonth =:yearMonth",
			"AND   a.krcdtMonMergePk.closureId =:closureId",
			"AND   a.krcdtMonMergePk.closureDay =:closureDay",
			"AND   a.krcdtMonMergePk.isLastDay =:isLastDay",
			"ORDER BY a.krcdtMonMergePk.employeeId");
	private static final String FIND_BY_SIDS_AND_YEARMONTHS = String.join(" ", SEL_NO_WHERE,
			"WHERE a.krcdtMonMergePk.employeeId IN :employeeIds",
			"AND   a.krcdtMonMergePk.yearMonth IN :yearMonths",
			"ORDER BY a.krcdtMonMergePk.employeeId, a.krcdtMonMergePk.yearMonth, a.startYmd");
	private static final String FIND_BY_PERIOD = String.join(" ", SEL_NO_WHERE,
			 "WHERE a.krcdtMonMergePk.employeeId = :employeeId ",
			 "AND a.startYmd <= :endDate ",
			 "AND a.endYmd >= :startDate ");
	private static final String DELETE_BY_PK = String.join(" ", "DELETE FROM KrcdtMonMerge a ",
			"WHERE  a.krcdtMonMergePk.employeeId = :employeeId ",
			"AND    a.krcdtMonMergePk.yearMonth = :yearMonth ",
			"AND    a.krcdtMonMergePk.closureId = :closureId",
			"AND    a.krcdtMonMergePk.closureDay = :closureDay",
			"AND    a.krcdtMonMergePk.isLastDay = :isLastDay");
	private static final String DELETE_BY_YEAR_MONTH = String.join(" ", "DELETE FROM KrcdtMonMerge a ",
			 "WHERE  a.krcdtMonMergePk.employeeId = :employeeId ",
			 "AND 	 a.krcdtMonMergePk.yearMonth = :yearMonth ");	
	private static final String FIND_BY_PERIOD_INTO_END = "SELECT a FROM KrcdtMonMerge a "
			+ "WHERE a.krcdtMonMergePk.employeeId = :employeeId "
			+ "AND a.endYmd >= :startDate "
			+ "AND a.endYmd <= :endDate "
			+ "ORDER BY a.startYmd ";

	/** 検索 */
	@Override
	public Optional<TimeOfMonthly> find(String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate) {
		val key = new KrcdtMonMergePk(
				employeeId,
				yearMonth.v(),
				closureId.value,
				closureDate.getClosureDay().v(),
				(closureDate.getLastDayOfMonth() ? 1 : 0));
		return this.queryProxy().find(key, KrcdtMonMerge.class)
				.map(c -> toDomain(c));
	}

	/** 検索　（年月） */
	@Override
	public List<TimeOfMonthly> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth) {
		return this.queryProxy().query(FIND_BY_YEAR_MONTH, KrcdtMonMerge.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.getList( c -> toDomain(c));
	}
	
	/** 検索　（年月と締めID） */
	@Override
	public List<TimeOfMonthly> findByYMAndClosureIdOrderByStartYmd(String employeeId, YearMonth yearMonth,
			ClosureId closureId) {
		return this.queryProxy().query(FIND_BY_YM_AND_CLOSURE_ID, KrcdtMonMerge.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.setParameter("closureId", closureId.value)
				.getList(c -> toDomain(c));
	}

	/** 検索　（社員IDリスト） */
	@Override
	public List<TimeOfMonthly> findByEmployees(List<String> employeeIds, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate) {
		
		List<TimeOfMonthly> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			results.addAll(this.queryProxy().query(FIND_BY_EMPLOYEES, KrcdtMonMerge.class)
					.setParameter("employeeIds", splitData)
					.setParameter("yearMonth", yearMonth.v())
					.setParameter("closureId", closureId.value)
					.setParameter("closureDay", closureDate.getClosureDay().v())
					.setParameter("isLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0))
					.getList(c -> toDomain(c)));
		});
		results.sort(Comparator.comparing(TimeOfMonthly::getEmployeeId));
		return results;
	}
	
	/** 検索　（社員IDリストと年月リスト） */
	@Override
	public List<TimeOfMonthly> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths) {
		
		val yearMonthValues = yearMonths.stream().map(c -> c.v()).collect(Collectors.toList());
		
		// List<TimeOfMonthly> results = new ArrayList<>();
		List<KrcdtMonMerge> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {			
			results.addAll(this.queryProxy().query(FIND_BY_SIDS_AND_YEARMONTHS, KrcdtMonMerge.class)
					.setParameter("employeeIds", splitData)
					.setParameter("yearMonths", yearMonthValues)
					.getList());
		});
		results.sort((o1, o2) -> {
			int tmp = o1.getKrcdtMonMergePk().getEmployeeId().compareTo(o2.getKrcdtMonMergePk().getEmployeeId());
			if (tmp != 0) return tmp;
			tmp = o1.getKrcdtMonMergePk().getYearMonth() - o2.getKrcdtMonMergePk().getYearMonth();
			if (tmp != 0) return tmp;
			return o1.getStartYmd().compareTo(o2.getStartYmd());
		});
		return results.stream().map(item -> toDomain(item)).collect(Collectors.toList());
	}
		
	/** 検索　（基準日） */
	@Override
	public List<TimeOfMonthly> findByDate(String employeeId, GeneralDate criteriaDate) {
		return this.queryProxy().query(FIND_BY_PERIOD, KrcdtMonMerge.class)
				.setParameter("employeeId", employeeId)
				.setParameter("startDate", criteriaDate)
				.setParameter("endDate", criteriaDate)
				.getList(c -> toDomain(c));
	}
	
	/** 検索　（終了日を含む期間） */
	@Override
	public List<TimeOfMonthly> findByPeriodIntoEndYmd(String employeeId, DatePeriod period) {
		
		return this.queryProxy().query(FIND_BY_PERIOD_INTO_END, KrcdtMonMerge.class)
				.setParameter("employeeId", employeeId)
				.setParameter("startDate", period.start())
				.setParameter("endDate", period.end())
				.getList(c -> toDomain(c));
	}
			
	/** 登録および更新 */
	@Override
	public void persistAndUpdate(TimeOfMonthly domain){

		// 締め日付
		val closureDate = domain.getClosureDate();
		
		// キー
		val key = new KrcdtMonMergePk(
				domain.getEmployeeId(),
				domain.getYearMonth().v(),
				domain.getClosureId().value,
				closureDate.getClosureDay().v(),
				(closureDate.getLastDayOfMonth() ? 1 : 0));		
		
		// 登録・更新を判断　および　キー値設定
		boolean isNeedPersist = false;
		KrcdtMonMerge entity = this.getEntityManager().find(KrcdtMonMerge.class, key);
		if (entity == null){
			isNeedPersist = true;
			entity = new KrcdtMonMerge();
			entity.krcdtMonMergePk = key;
		}
		if(domain.getAttendanceTime().isPresent()){
			entity.toEntityAttendanceTimeOfMonthly(domain.getAttendanceTime().get());
		}
		if(domain.getAffiliation().isPresent()){
			entity.toEntityAffiliationInfoOfMonthly(domain.getAffiliation().get());
		}
		
		// 登録が必要な時、登録を実行
		if (isNeedPersist) {
			this.getEntityManager().persist(entity);
		}
	}
	
	/** 削除 */
	@Override
	public void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate) {
		
		this.getEntityManager().createQuery(DELETE_BY_PK)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.setParameter("closureId", closureId.value)
				.setParameter("closureDay", closureDate.getClosureDay().v())
				.setParameter("isLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0))
				.executeUpdate();
	}
		
	/** 削除　（年月） */
	@Override
	public void removeByYearMonth(String employeeId, YearMonth yearMonth) {
		
		this.getEntityManager().createQuery(DELETE_BY_YEAR_MONTH)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.executeUpdate();
	}

	private TimeOfMonthly toDomain(KrcdtMonMerge c) {
		return new TimeOfMonthly(c.toDomainAttendanceTimeOfMonthly(), c.toDomainAffiliationInfoOfMonthly());
	}

	@Override
	public void removeAffiliation(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		
		// キー
		val key = new KrcdtMonMergePk(
				employeeId,
				yearMonth.v(),
				closureId.value,
				closureDate.getClosureDay().v(),
				(closureDate.getLastDayOfMonth() ? 1 : 0));		
		
		// 登録・更新を判断　および　キー値設定
		KrcdtMonMerge entity = queryProxy().find(key, KrcdtMonMerge.class).orElse(null);
		if (entity != null){
			entity.resetAffiliationInfo();
		}
	}

	@Override
	public void removeAttendanceTime(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		// キー
		val key = new KrcdtMonMergePk(
				employeeId,
				yearMonth.v(),
				closureId.value,
				closureDate.getClosureDay().v(),
				(closureDate.getLastDayOfMonth() ? 1 : 0));		
		
		// 登録・更新を判断　および　キー値設定
		KrcdtMonMerge entity = queryProxy().find(key, KrcdtMonMerge.class).orElse(null);
		if (entity != null){
			entity.resetAttendanceTime();
		}
		
	}

	@Override
	public void removeAffiliation(String employeeId, YearMonth yearMonth) {
		this.queryProxy().query(FIND_BY_YEAR_MONTH, KrcdtMonMerge.class)
						.setParameter("employeeId", employeeId)
						.setParameter("yearMonth", yearMonth.v())
						.getList().stream().forEach(c -> c.resetAffiliationInfo());
	}

	@Override
	public void removeAttendanceTime(String employeeId, YearMonth yearMonth) {
		this.queryProxy().query(FIND_BY_YEAR_MONTH, KrcdtMonMerge.class)
						.setParameter("employeeId", employeeId)
						.setParameter("yearMonth", yearMonth.v())
						.getList().stream().forEach(c -> c.resetAttendanceTime());
	}
}
