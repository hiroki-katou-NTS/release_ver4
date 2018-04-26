package nts.uk.ctx.at.record.infra.repository.monthly.affiliation;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.affiliation.AffiliationInfoOfMonthly;
import nts.uk.ctx.at.record.dom.monthly.affiliation.AffiliationInfoOfMonthlyRepository;
import nts.uk.ctx.at.record.infra.entity.monthly.affiliation.KrcdtMonAffiliation;
import nts.uk.ctx.at.record.infra.entity.monthly.affiliation.KrcdtMonAffiliationPK;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureDate;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

/**
 * リポジトリ実装：月別実績の所属情報
 * @author shuichu_ishida
 */
@Stateless
public class JpaAffiliationInfoOfMonthly extends JpaRepository implements AffiliationInfoOfMonthlyRepository {

	private static final String FIND_BY_SID_AND_YEAR_MONTH = "SELECT a FROM KrcdtMonAffiliation a "
			+ "WHERE a.PK.employeeId = :employeeId "
			+ "AND a.PK.yearMonth = :yearMonth ";

	private static final String DELETE_BY_SID_AND_YEAR_MONTH = "DELETE FROM KrcdtMonAffiliation a "
			+ "WHERE a.PK.employeeId = :employeeId "
			+ "AND a.PK.yearMonth = :yearMonth ";
	
	/** 検索 */
	@Override
	public Optional<AffiliationInfoOfMonthly> find(String employeeId, YearMonth yearMonth, ClosureId closureId,
			ClosureDate closureDate) {
		
		return this.queryProxy()
				.find(new KrcdtMonAffiliationPK(
						employeeId,
						yearMonth.v(),
						closureId.value,
						closureDate.getClosureDay().v(),
						(closureDate.getLastDayOfMonth() ? 1 : 0)),
						KrcdtMonAffiliation.class)
				.map(c -> c.toDomain());
	}
	
	/** 検索　（社員IDと年月） */
	@Override
	public List<AffiliationInfoOfMonthly> findBySidAndYearMonth(String employeeId, YearMonth yearMonth) {
		
		return this.queryProxy().query(FIND_BY_SID_AND_YEAR_MONTH, KrcdtMonAffiliation.class)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.getList(c -> c.toDomain());
	}
	
	/** 登録および更新 */
	@Override
	public void persistAndUpdate(AffiliationInfoOfMonthly domain) {
		
		// キー
		val key = new KrcdtMonAffiliationPK(
				domain.getEmployeeId(),
				domain.getYearMonth().v(),
				domain.getClosureId().value,
				domain.getClosureDate().getClosureDay().v(),
				(domain.getClosureDate().getLastDayOfMonth() ? 1 : 0));
		
		// 登録・更新
		KrcdtMonAffiliation entity = this.getEntityManager().find(KrcdtMonAffiliation.class, key);
		if (entity == null){
			entity = new KrcdtMonAffiliation();
			entity.fromDomainForPersist(domain);
			this.getEntityManager().persist(entity);
		}
		else {
			entity.fromDomainForUpdate(domain);
		}
	}
	
	/** 削除 */
	@Override
	public void remove(String employeeId, YearMonth yearMonth, ClosureId closureId, ClosureDate closureDate) {

		this.commandProxy().remove(KrcdtMonAffiliation.class, new KrcdtMonAffiliationPK(
				employeeId,
				yearMonth.v(),
				closureId.value,
				closureDate.getClosureDay().v(),
				(closureDate.getLastDayOfMonth() ? 1 : 0)));
	}
	
	/** 削除　（社員IDと年月） */
	@Override
	public void removeBySidAndYearMonth(String employeeId, YearMonth yearMonth) {
		
		this.getEntityManager().createQuery(DELETE_BY_SID_AND_YEAR_MONTH)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.executeUpdate();
	}
}
