package nts.uk.ctx.at.record.infra.repository.monthly.anyitem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.monthly.AttendanceTimeOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.anyitem.AnyItemOfMonthlyRepository;
import nts.uk.ctx.at.record.infra.entity.monthly.KrcdtMonAttendanceTime;
import nts.uk.ctx.at.record.infra.entity.monthly.anyitem.KrcdtMonAnyItemValue;
import nts.uk.ctx.at.record.infra.entity.monthly.anyitem.KrcdtMonAnyItemValuePK;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

/**
 * リポジトリ実装：月別実績の任意項目
 * @author shuichu_ishida
 */
@Stateless
public class JpaAnyItemOfMonthly extends JpaRepository implements AnyItemOfMonthlyRepository {

	private static final String FIND_BY_MONTHLY_AND_CLOSURE = "SELECT a FROM KrcdtMonAnyItemValue a "
			+ "WHERE a.PK.employeeId = :employeeId "
			+ "AND a.PK.yearMonth = :yearMonth "
			+ "AND a.PK.closureId = :closureId "
			+ "AND a.PK.closureDay = :closureDay "
			+ "AND a.PK.isLastDay = :isLastDay ";

	private static final String FIND_BY_MONTHLY = "SELECT a FROM KrcdtMonAnyItemValue a "
			+ "WHERE a.PK.employeeId = :employeeId "
			+ "AND a.PK.yearMonth = :yearMonth ";

	private static final String FIND_BY_EMPLOYEES = "SELECT a FROM KrcdtMonAnyItemValue a "
			+ "WHERE a.PK.employeeId IN :employeeIds "
			+ "AND a.PK.yearMonth = :yearMonth "
			+ "AND a.PK.closureId = :closureId "
			+ "AND a.PK.closureDay = :closureDay "
			+ "AND a.PK.isLastDay = :isLastDay "
			+ "AND a.PK.anyItemId = :anyItemId ";

	private static final String DELETE_BY_MONTHLY_AND_CLOSURE = "DELETE FROM KrcdtMonAnyItemValue a "
			+ "WHERE a.PK.employeeId = :employeeId "
			+ "AND a.PK.yearMonth = :yearMonth "
			+ "AND a.PK.closureId = :closureId "
			+ "AND a.PK.closureDay = :closureDay "
			+ "AND a.PK.isLastDay = :isLastDay ";

	private static final String DELETE_BY_MONTHLY = "DELETE FROM KrcdtMonAnyItemValue a "
			+ "WHERE a.PK.employeeId = :employeeId "
			+ "AND a.PK.yearMonth = :yearMonth ";
	
	/** 検索 */
	@Override
	public Optional<AnyItemOfMonthly> find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate, int anyItemId) {

		return this.queryProxy()
				.find(new KrcdtMonAnyItemValuePK(
						employeeId,
						yearMonth.v(),
						closureId.value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0),
						anyItemId),
						KrcdtMonAnyItemValue.class)
				.map(c -> c.toDomain());
	}
	
	/** 検索　（月度と締め） */
	@Override
	public List<AnyItemOfMonthly> findByMonthlyAndClosure(String employeeId, YearMonth yearMonth,
			ClosureId closureId, ClosureDate closureDate) {
		
		return this.queryProxy().query(FIND_BY_MONTHLY_AND_CLOSURE, KrcdtMonAnyItemValue.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.setParameter("closureId", closureId.value)
				.setParameter("closureDay", closureDate.getClosureDay().v())
				.setParameter("isLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0))
				.getList(c -> c.toDomain());
	}
	
	/** 検索　（月度） */
	@Override
	public List<AnyItemOfMonthly> findByMonthly(String employeeId, YearMonth yearMonth) {
		
		return this.queryProxy().query(FIND_BY_MONTHLY, KrcdtMonAnyItemValue.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.getList(c -> c.toDomain());
	}

	/** 検索　（社員リスト） */
	@Override
	public List<AnyItemOfMonthly> findByEmployees(List<String> employeeIds, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate, int anyItemId) {
		
		List<AnyItemOfMonthly> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			results.addAll(this.queryProxy().query(FIND_BY_EMPLOYEES, KrcdtMonAnyItemValue.class)
					.setParameter("employeeIds", splitData)
					.setParameter("yearMonth", yearMonth.v())
					.setParameter("closureId", closureId.value)
					.setParameter("closureDay", closureDate.getClosureDay().v())
					.setParameter("isLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0))
					.setParameter("anyItemId", anyItemId)
					.getList(c -> c.toDomain()));
		});
		return results;
	}
	
	/** 登録および更新 */
	@Override
	public void persistAndUpdate(AnyItemOfMonthly domain) {
		
		// キー
		val key = new KrcdtMonAnyItemValuePK(
				domain.getEmployeeId(),
				domain.getYearMonth().v(),
				domain.getClosureId().value,
				domain.getClosureDate().getClosureDay().v(),
				(domain.getClosureDate().getLastDayOfMonth() ? 1 : 0),
				domain.getAnyItemId());
		
		// 登録・更新
		KrcdtMonAnyItemValue entity = this.getEntityManager().find(KrcdtMonAnyItemValue.class, key);
		if (entity == null){
			entity = new KrcdtMonAnyItemValue();
			entity.fromDomainForPersist(domain);
			this.getEntityManager().persist(entity);
		}
		else {
			entity.fromDomainForUpdate(domain);
		}
	}
	
	/** 削除 */
	@Override
	public void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate,
			int anyItemId) {
		
		this.commandProxy().remove(KrcdtMonAnyItemValue.class, new KrcdtMonAnyItemValuePK(
				employeeId,
				yearMonth.v(),
				closureId.value,
				closureDate.getClosureDay().v(),
				(closureDate.getLastDayOfMonth() ? 1 : 0),
				anyItemId));
	}

	/** 削除　（月度と締め） */
	@Override
	public void removeByMonthlyAndClosure(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {

		this.getEntityManager().createQuery(DELETE_BY_MONTHLY_AND_CLOSURE)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.setParameter("closureId", closureId.value)
				.setParameter("closureDay", closureDate.getClosureDay().v())
				.setParameter("isLastDay", (closureDate.getLastDayOfMonth() ? 1 : 0))
				.executeUpdate();
	}

	/** 削除　（月度） */
	@Override
	public void removeByMonthly(String employeeId, YearMonth yearMonth) {

		this.getEntityManager().createQuery(DELETE_BY_MONTHLY)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.executeUpdate();
	}
}
