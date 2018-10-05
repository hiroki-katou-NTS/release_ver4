package nts.uk.ctx.at.record.infra.repository.monthly.vacation.absenceleave;

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
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.AbsenceLeaveRemainData;
import nts.uk.ctx.at.record.dom.monthly.vacation.absenceleave.monthremaindata.AbsenceLeaveRemainDataRepository;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonMergePk;
import nts.uk.ctx.at.record.infra.entity.monthly.mergetable.KrcdtMonRemain;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

@Stateless
public class JpaAbsenceLeaveRemainDataRepository extends JpaRepository implements AbsenceLeaveRemainDataRepository{
	
	private static final String QUERY_BY_SID_YM_STATUS = "SELECT c FROM KrcdtMonRemain c"
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
	public List<AbsenceLeaveRemainData> getDataBySidYmClosureStatus(String employeeId, YearMonth ym,
			ClosureStatus status) {
		return  this.queryProxy().query(QUERY_BY_SID_YM_STATUS, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("ym", ym.v())
				.setParameter("status", status.value)
				.getList(c -> c.toDomainAbsenceLeaveRemainData());
	}
	
	@Override
	public Optional<AbsenceLeaveRemainData> find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		
		return this.queryProxy()
				.find(new KrcdtMonMergePk(
						employeeId,
						yearMonth.v(),
						closureId.value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0)),
						KrcdtMonRemain.class)
				.map(c -> c.toDomainAbsenceLeaveRemainData());
	}
	
	@Override
	public List<AbsenceLeaveRemainData> findByYearMonthOrderByStartYmd(String employeeId, YearMonth yearMonth) {
		
		return this.queryProxy().query(FIND_BY_YEAR_MONTH, KrcdtMonRemain.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.getList(c -> c.toDomainAbsenceLeaveRemainData());
	}

	@Override
	public List<AbsenceLeaveRemainData> findBySidsAndYearMonths(List<String> employeeIds, List<YearMonth> yearMonths) {
		
		val yearMonthValues = yearMonths.stream().map(c -> c.v()).collect(Collectors.toList());
		
		List<AbsenceLeaveRemainData> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			results.addAll(this.queryProxy().query(FIND_BY_SIDS_AND_MONTHS, KrcdtMonRemain.class)
					.setParameter("employeeIds", splitData)
					.setParameter("yearMonths", yearMonthValues)
					.getList(c -> c.toDomainAbsenceLeaveRemainData()));
		});
		return results;
	}

	@Override
	public void persistAndUpdate(AbsenceLeaveRemainData domain) {
		
		// キー
		val key = new KrcdtMonMergePk(
				domain.getSId(),
				domain.getYm().v(),
				domain.getClosureId(),
				domain.getClosureDay(),
				(domain.isLastDayIs() ? 1 : 0));
		
		// 登録・更新
		KrcdtMonRemain entity = this.getEntityManager().find(KrcdtMonRemain.class, key);
		if (entity == null){
			entity = new KrcdtMonRemain();
			entity.setKrcdtMonRemainPk(key);
			entity.toEntityAbsenceLeaveRemainData(domain);
			this.getEntityManager().persist(entity);
		}
		else {
			entity.toEntityAbsenceLeaveRemainData(domain);
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
		if (entity != null) entity.deleteAbsenceLeaveRemainData();
	}
}
