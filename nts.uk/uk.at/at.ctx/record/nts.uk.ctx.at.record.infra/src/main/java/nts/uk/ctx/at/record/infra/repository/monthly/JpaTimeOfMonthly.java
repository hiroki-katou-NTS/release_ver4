package nts.uk.ctx.at.record.infra.repository.monthly;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
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
	private static final String FIND_BY_EMPLOYEES_AND_CLOSURE = String.join(" ", SEL_NO_WHERE,
			"WHERE a.krcdtMonMergePk.employeeId IN :employeeIds",
			"AND   a.krcdtMonMergePk.yearMonth =:yearMonth",
			"AND   a.krcdtMonMergePk.closureId =:closureId",
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
	@SneakyThrows
	public List<TimeOfMonthly> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth) {

		String sql = "select * from KRCDT_MON_MERGE"
				+ " where SID = ?"
				+ " and YM = ?"
				+ " order by START_YMD";
		try (val stmt = this.connection().prepareStatement(sql)) {
			stmt.setString(1, employeeId);
			stmt.setInt(2, yearMonth.v());
			
			List<KrcdtMonMerge> entities = new NtsResultSet(stmt.executeQuery()).getList(rec -> KrcdtMonMerge.MAPPER.toEntity(rec));
			return entities.stream().map(e -> toDomain(e)).collect(Collectors.toList());
		}
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
	
	@Override
	public List<TimeOfMonthly> findByEmployeesAndClorure(List<String> employeeIds, YearMonth yearMonth,
			int closureId) {
		if(employeeIds.isEmpty())
			return Collections.emptyList();
		List<TimeOfMonthly> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			results.addAll(this.queryProxy().query(FIND_BY_EMPLOYEES_AND_CLOSURE, KrcdtMonMerge.class)
					.setParameter("employeeIds", splitData)
					.setParameter("yearMonth", yearMonth.v())
					.setParameter("closureId", closureId)
					.getList(c -> toDomain(c)));
		});
		results.sort(Comparator.comparing(TimeOfMonthly::getEmployeeId));
		return results;
	}
	
	/** 検索　（社員IDリストと年月リスト） */
	@Override
	public List<TimeOfMonthly> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths) {
		
		val yearMonthValues = yearMonths.stream().map(c -> c.v()).collect(Collectors.toList());
		
		String sql = "select * from KRCDT_MON_MERGE"
				+ " where SID in @emps"
				+ " and YM in @yms";
		
		val results = NtsStatement.In.split(employeeIds, emps -> {
			return new NtsStatement(sql, this.jdbcProxy())
					.paramString("emps", emps)
					.paramInt("yms", yearMonthValues)
					.getList(rec -> KrcdtMonMerge.MAPPER.toEntity(rec));
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
	
	@Override
	public void verShouldUp(String employeeId, YearMonth yearMonth, int closureId, int closureDate, boolean lastOfMonth) {
		
		// キー
		val key = new KrcdtMonMergePk(
				employeeId,
				yearMonth.v(),
				closureId,
				closureDate,
				lastOfMonth ? 1 : 0);		
		
		dirtying(() -> key);
	}
	
	public void verShouldUp(String employeeId, YearMonth yearMonth, int closureId, int closureDate, boolean lastOfMonth, long version) {
		
		// キー
		val key = new KrcdtMonMergePk(
				employeeId,
				yearMonth.v(),
				closureId,
				closureDate,
				lastOfMonth ? 1 : 0);		
		
		this.queryProxy().find(key, KrcdtMonMerge.class).ifPresent(entity -> {
			entity.version = version;
			this.commandProxy().update(entity);
		});
	}
	
	@Override
	@SneakyThrows
	public long getVer(String employeeId, YearMonth yearMonth, int closureId, int closureDate, boolean lastOfMonth) {
		try (PreparedStatement stmtFindById = this.connection().prepareStatement(
				"SELECT EXCLUS_VER from KRCDT_MON_MERGE"
				+ " WHERE SID = ? AND YM = ? AND CLOSURE_ID = ? AND CLOSURE_DAY = ? AND IS_LAST_DAY = ?")) {
			stmtFindById.setString(1, employeeId);
			stmtFindById.setInt(2, yearMonth.v());
			stmtFindById.setInt(3, closureId);
			stmtFindById.setInt(4, closureDate);
			stmtFindById.setInt(5, lastOfMonth ? 1 : 0);

			return new NtsResultSet(stmtFindById.executeQuery()).getSingle(rec -> {
				return rec.getLong(1);
			}).orElse(0L);
		}
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

	public void dirtying(Supplier<Object> getKey) {
		
		this.queryProxy().find(getKey.get(), KrcdtMonMerge.class).ifPresent(entity -> {
			entity.dirtying();
			this.commandProxy().update(entity);
		});
	}
}
