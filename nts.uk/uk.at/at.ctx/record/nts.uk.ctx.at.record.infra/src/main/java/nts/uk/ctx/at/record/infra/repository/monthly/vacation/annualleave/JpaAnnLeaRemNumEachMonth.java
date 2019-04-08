package nts.uk.ctx.at.record.infra.repository.monthly.vacation.annualleave;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnLeaRemNumEachMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.annualleave.AnnLeaRemNumEachMonthRepository;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonMergePk;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonRemain;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

/**
 * リポジトリ実装：年休月別残数データ
 * @author shuichu_ishida
 */
@Stateless
public class JpaAnnLeaRemNumEachMonth extends JpaRepository implements AnnLeaRemNumEachMonthRepository {

	private static final String FIND_BY_YEAR_MONTH = "SELECT a FROM KrcdtMonRemain a "
			+ "WHERE a.krcdtMonRemainPk.employeeId = :employeeId "
			+ "AND a.krcdtMonRemainPk.yearMonth = :yearMonth "
			+ "ORDER BY a.startDate ";

	private static final String FIND_BY_YM_AND_CLOSURE_ID = "SELECT a FROM KrcdtMonRemain a "
			+ "WHERE a.krcdtMonRemainPk.employeeId = :employeeId "
			+ "AND a.krcdtMonRemainPk.yearMonth = :yearMonth "
			+ "AND a.krcdtMonRemainPk.closureId = :closureId "
			+ "ORDER BY a.startDate ";

	private static final String FIND_BY_SIDS = "SELECT a FROM KrcdtMonRemain a "
			+ "WHERE a.krcdtMonRemainPk.employeeId IN :employeeIds "
			+ "AND a.krcdtMonRemainPk.yearMonth = :yearMonth "
			+ "AND a.krcdtMonRemainPk.closureId = :closureId "
			+ "AND a.krcdtMonRemainPk.closureDay = :closureDay "
			+ "AND a.krcdtMonRemainPk.isLastDay = :isLastDay "
			+ "ORDER BY a.krcdtMonRemainPk.employeeId, a.startDate ";

	private static final String FIND_BY_SIDS_AND_MONTHS = "SELECT a FROM KrcdtMonRemain a "
			+ "WHERE a.krcdtMonRemainPk.employeeId IN :employeeIds "
			+ "AND a.krcdtMonRemainPk.yearMonth IN :yearMonths "
			+ "ORDER BY a.krcdtMonRemainPk.employeeId, a.startDate ";

	private static final String FIND_BY_CLOSURE_PERIOD = "SELECT a FROM KrcdtMonRemain a "
			+ "WHERE a.krcdtMonRemainPk.employeeId = :employeeId "
			+ "AND a.startDate >= :startDate "
			+ "AND a.endDate <= :endDate "
			+ "AND a.closureStatus = 1 "
			+ "ORDER BY a.startDate ";
	
//	private static final String DELETE_BY_YEAR_MONTH = "DELETE FROM KrcdtMonRemain a "
//			+ "WHERE a.krcdtMonRemainPk.employeeId = :employeeId "
//			+ "AND a.krcdtMonRemainPk.yearMonth = :yearMonth ";

	/** 検索 */
	@Override
	public Optional<AnnLeaRemNumEachMonth> find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		
		return this.queryProxy()
				.find(new KrcdtMonMergePk(
						employeeId,
						yearMonth.v(),
						closureId.value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0)),
						KrcdtMonRemain.class)
				.map(c -> c.toDomainAnnLeaRemNumEachMonth());
	}
	
	/** 検索　（年月） */
	@Override
	public List<AnnLeaRemNumEachMonth> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth) {
		
		return this.queryProxy().query(FIND_BY_YEAR_MONTH, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.getList(c -> c.toDomainAnnLeaRemNumEachMonth());
	}
	
	/** 検索　（年月と締めID） */
	@Override
	public List<AnnLeaRemNumEachMonth> findByYMAndClosureIdOrderByStartYmd(String employeeId, YearMonth yearMonth,
			ClosureId closureId) {
		
		return this.queryProxy().query(FIND_BY_YM_AND_CLOSURE_ID, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.setParameter("closureId", closureId.value)
				.getList(c -> c.toDomainAnnLeaRemNumEachMonth());
	}
	
	/** 検索　（社員IDリスト） */
	@Override
	public List<AnnLeaRemNumEachMonth> findbyEmployees(List<String> employeeIds, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate) {
		
		List<KrcdtMonRemain> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			results.addAll(this.queryProxy().query(FIND_BY_SIDS, KrcdtMonRemain.class)
					.setParameter("employeeIds", splitData)
					.setParameter("yearMonth", yearMonth.v())
					.setParameter("closureId", closureId.value)
					.setParameter("closureDay", closureDate.getClosureDay().v())
					.setParameter("isLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0))
					.getList());
		});
		results.sort((o1, o2) -> {
			int tmp = o1.getKrcdtMonRemainPk().getEmployeeId().compareTo(o2.getKrcdtMonRemainPk().getEmployeeId());
			if (tmp != 0) return tmp;
			return o1.getStartDate().compareTo(o2.getStartDate());
		});
		return results.stream().map(c -> c.toDomainAnnLeaRemNumEachMonth()).collect(Collectors.toList());
	}
	
	/** 検索　（社員IDリストと年月リスト） */
	@Override
	public List<AnnLeaRemNumEachMonth> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths) {
		
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
		return results.stream().map(c -> c.toDomainAnnLeaRemNumEachMonth()).collect(Collectors.toList());
	}

	/** 検索　（社員IDと締め期間、条件＝締め済み） */
	@Override
	public List<AnnLeaRemNumEachMonth> findByClosurePeriod(String employeeId, DatePeriod closurePeriod) {
		
		return this.queryProxy().query(FIND_BY_CLOSURE_PERIOD, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("startDate", closurePeriod.start())
				.setParameter("endDate", closurePeriod.end())
				.getList(c -> c.toDomainAnnLeaRemNumEachMonth());
	}
	
	/** 登録および更新 */
	@Override
	public void persistAndUpdate(AnnLeaRemNumEachMonth domain) {
		
		// キー
		val key = new KrcdtMonMergePk(
				domain.getEmployeeId(),
				domain.getYearMonth().v(),
				domain.getClosureId().value,
				domain.getClosureDate().getClosureDay().v(),
				(domain.getClosureDate().getLastDayOfMonth() ? 1 : 0));
		
		// 登録・更新
		KrcdtMonRemain entity = this.getEntityManager().find(KrcdtMonRemain.class, key);
		if (entity == null){
			entity = new KrcdtMonRemain();
			entity.setKrcdtMonRemainPk(key);
			entity.toEntityMonAnnleaRemain(domain);
			this.getEntityManager().persist(entity);
		}
		else {
			entity.toEntityMonAnnleaRemain(domain);
		}
	}
	
	/** 削除 */
	@Override
	public void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate) {
		
//		this.commandProxy().remove(KrcdtMonRemain.class,
//				new KrcdtMonMergePk(
//						employeeId,
//						yearMonth.v(),
//						closureId.value,
//						closureDate.getClosureDay().v(),
//						(closureDate.getLastDayOfMonth() ? 1 : 0)));
		
		// キー
		val key = new KrcdtMonMergePk(
				employeeId,
				yearMonth.v(),
				closureId.value,
				closureDate.getClosureDay().v(),
				(closureDate.getLastDayOfMonth() ? 1 : 0));
		
		// 削除
		KrcdtMonRemain entity = this.getEntityManager().find(KrcdtMonRemain.class, key);
		if (entity != null) entity.deleteMonAnnleaRemain();
	}
	
	/** 削除　（年月） */
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
		for (val entity : entitys) entity.deleteMonAnnleaRemain();
	}
}
