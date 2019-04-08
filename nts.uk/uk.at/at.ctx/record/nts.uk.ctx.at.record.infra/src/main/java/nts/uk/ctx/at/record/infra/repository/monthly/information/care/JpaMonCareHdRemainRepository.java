package nts.uk.ctx.at.record.infra.repository.monthly.information.care;

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
import nts.uk.ctx.at.record.dom.monthly.information.care.MonCareHdRemain;
import nts.uk.ctx.at.record.dom.monthly.information.care.MonCareHdRemainRepository;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonMergePk;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonRemain;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

/**
 * 
 * @author phongtq
 */
@Stateless
public class JpaMonCareHdRemainRepository extends JpaRepository implements MonCareHdRemainRepository{

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

//	private static final String DELETE_BY_YEAR_MONTH = "DELETE FROM KrcdtMonRemain a "
//			+ "WHERE a.krcdtMonRemainPk.employeeId = :employeeId "
//			+ "AND a.krcdtMonRemainPk.yearMonth = :yearMonth ";
	
	@Override
	public Optional<MonCareHdRemain> find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		
		return this.queryProxy()
				.find(new KrcdtMonMergePk(
						employeeId,
						yearMonth.v(),
						closureId.value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0)),
						KrcdtMonRemain.class)
				.map(c -> c.toDomainMonCareHdRemain());
	}

	@Override
	public List<MonCareHdRemain> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth) {
		
		return this.queryProxy().query(FIND_BY_YEAR_MONTH, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.getList(c -> c.toDomainMonCareHdRemain());
	}

	@Override
	public List<MonCareHdRemain> findByYMAndClosureIdOrderByStartYmd(String employeeId, YearMonth yearMonth,
			ClosureId closureId) {
		
		return this.queryProxy().query(FIND_BY_YM_AND_CLOSURE_ID, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.setParameter("closureId", closureId.value)
				.getList(c -> c.toDomainMonCareHdRemain());
	}

	@Override
	public List<MonCareHdRemain> findByEmployees(List<String> employeeIds, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		
		List<MonCareHdRemain> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			results.addAll(this.queryProxy().query(FIND_BY_SIDS, KrcdtMonRemain.class)
					.setParameter("employeeIds", splitData)
					.setParameter("yearMonth", yearMonth.v())
					.setParameter("closureId", closureId.value)
					.setParameter("closureDay", closureDate.getClosureDay().v())
					.setParameter("isLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0))
					.getList(c -> c.toDomainMonCareHdRemain()));
		});
		results.sort((o1, o2) -> {
			int tmp = o1.getEmployeeId().compareTo(o2.getEmployeeId());
			if (tmp != 0) return tmp;
			return o1.getStartDate().compareTo(o2.getStartDate());
		});
		return results;
	}

	@Override
	public List<MonCareHdRemain> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths) {
		
		val yearMonthValues = yearMonths.stream().map(c -> c.v()).collect(Collectors.toList());
		
		List<MonCareHdRemain> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			CollectionUtil.split(yearMonthValues, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstYearMonth -> {
				results.addAll(this.queryProxy().query(FIND_BY_SIDS_AND_MONTHS, KrcdtMonRemain.class)
						.setParameter("employeeIds", splitData)
						.setParameter("yearMonths", lstYearMonth)
						.getList(c -> c.toDomainMonCareHdRemain()));
			});
		});
		results.sort((o1, o2) -> {
			int tmp = o1.getEmployeeId().compareTo(o2.getEmployeeId());
			if (tmp != 0) return tmp;
			return o1.getStartDate().compareTo(o2.getStartDate());
		});
		return results;
	}

	@Override
	public void persistAndUpdate(MonCareHdRemain domain) {
		
		// キー
		val key = new KrcdtMonMergePk(
				domain.getEmployeeId(),
				domain.getYearMonth().v(),
				domain.getClosureId().value,
				domain.getClosureDay().v(),
				domain.getIsLastDay());
		
		// 登録・更新
		KrcdtMonRemain entity = this.getEntityManager().find(KrcdtMonRemain.class, key);
		if (entity == null){
			entity = new KrcdtMonRemain();
			entity.setKrcdtMonRemainPk(key);
			entity.toEntityCareRemainData(domain);
			this.getEntityManager().persist(entity);
		}
		else {
			entity.toEntityCareRemainData(domain);
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
		if (entity != null) entity.deleteCareRemainData();
	}

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
		for (val entity : entitys) entity.deleteCareRemainData();
	}
}
