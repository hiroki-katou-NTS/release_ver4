package nts.uk.ctx.at.record.infra.repository.monthly.vacation.dayoff;

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
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.MonthlyDayoffRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.dayoff.monthremaindata.MonthlyDayoffRemainDataRepository;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonMergePk;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonRemain;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

@Stateless
public class JpaMonthlyDayoffRemainDataRepository extends JpaRepository implements MonthlyDayoffRemainDataRepository{
	
	private static final String QUERY_BY_SID_YM_STATUS = "SELECT c FROM KrcdtMonRemain　c "
			+ " WHERE c.krcdtMonRemainPk.employeeId = :employeeId"
			+ " AND c.krcdtMonRemainPk.yearMonth = :ym"
			+ " AND c.closureStatus = :status"
			+ " ORDER BY c.endDate ASC";
	
	private static final String FIND_BY_YEAR_MONTH = "SELECT a FROM KrcdtMonRemain a "
			+ "WHERE a.krcdtMonRemainPk.employeeId = :employeeId "
			+ "AND a.krcdtMonRemainPk.yearMonth = :yearMonth "
			+ "ORDER BY a.startDate ";
	
	private static final String FIND_BY_SIDS_AND_MONTHS = "SELECT a FROM KrcdtMonRemain a "
			+ "WHERE a.krcdtMonRemainPk.employeeId IN :employeeIds "
			+ "AND a.krcdtMonRemainPk.yearMonth IN :yearMonths "
			+ "ORDER BY a.krcdtMonRemainPk.employeeId, a.startDate ";
	
	@Override
	public List<MonthlyDayoffRemainData> getDayOffDataBySidYmStatus(String employeeId, YearMonth ym,
			ClosureStatus status) {
		
		return  this.queryProxy().query(QUERY_BY_SID_YM_STATUS, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("ym", ym.v())
				.setParameter("status", status.value)
				.getList(c -> c.toDomainMonthlyDayoffRemainData());
	}
	
	@Override
	public Optional<MonthlyDayoffRemainData> find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		
		return this.queryProxy()
				.find(new KrcdtMonMergePk(
						employeeId,
						yearMonth.v(),
						closureId.value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0)),
						KrcdtMonRemain.class)
				.map(c -> c.toDomainMonthlyDayoffRemainData());
	}
	
	@Override
	public List<MonthlyDayoffRemainData> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth) {

		return this.queryProxy().query(FIND_BY_YEAR_MONTH, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.getList(c -> c.toDomainMonthlyDayoffRemainData());
	}
	
	@Override
	public List<MonthlyDayoffRemainData> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths) {
		
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
		return results.stream().map(c -> c.toDomainMonthlyDayoffRemainData()).collect(Collectors.toList());
	}
	
	@Override
	public void persistAndUpdate(MonthlyDayoffRemainData domain) {
		
		// キー
		val key = new KrcdtMonMergePk(
				domain.getSId(),
				domain.getYm().v(),
				domain.getClosureId(),
				domain.getClosureDay(),
				(domain.isLastDayis() ? 1 : 0));
		
		// 登録・更新
		KrcdtMonRemain entity = this.getEntityManager().find(KrcdtMonRemain.class, key);
		if (entity == null){
			entity = new KrcdtMonRemain();
			entity.setKrcdtMonRemainPk(key);
			entity.toEntityDayOffRemainDayAndTimes(domain);
			this.getEntityManager().persist(entity);
		}
		else {
			entity.toEntityDayOffRemainDayAndTimes(domain);
		}
	}
	
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
		if (entity != null) entity.deleteDayOffRemainDayAndTimes();
	}
}
