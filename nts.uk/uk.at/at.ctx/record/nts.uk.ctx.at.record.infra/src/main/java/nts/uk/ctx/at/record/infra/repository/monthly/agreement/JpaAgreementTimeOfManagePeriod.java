package nts.uk.ctx.at.record.infra.repository.monthly.agreement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.record.dom.monthly.agreement.AgreementTimeOfManagePeriodRepository;
import nts.uk.ctx.at.record.infra.entity.monthly.agreement.KrcdtMonMngAgreTime;
import nts.uk.ctx.at.record.infra.entity.monthly.agreement.KrcdtMonMngAgreTimePK;

/**
 * リポジトリ実装：管理期間の36協定時間
 * @author shuichi_ishida
 */
@Stateless
public class JpaAgreementTimeOfManagePeriod extends JpaRepository implements AgreementTimeOfManagePeriodRepository {

	private static final String FIND_BY_EMPLOYEES = "SELECT a FROM KrcdtMonMngAgreTime a "
			+ "WHERE a.PK.employeeId IN :employeeIds "
			+ "AND a.PK.yearMonth = :yearMonth "
			+ "ORDER BY a.PK.employeeId ";
	
	private static final String FIND_BY_SIDS_AND_YEARMONTHS = "SELECT a FROM KrcdtMonMngAgreTime a "
			+ "WHERE a.PK.employeeId IN :employeeIds "
			+ "AND a.PK.yearMonth IN :yearMonths "
			+ "ORDER BY a.PK.employeeId, a.PK.yearMonth ";
	
	private static final String REMOVE_BY_PK =
			"DELETE FROM KrcdtMonMngAgreTime a "
			+ "WHERE a.PK.employeeId = :employeeId "
			+ "AND a.PK.yearMonth = :yearMonth ";
	
	/** 検索 */
	@Override
	public Optional<AgreementTimeOfManagePeriod> find(String employeeId, YearMonth yearMonth) {

		return this.queryProxy()
				.find(new KrcdtMonMngAgreTimePK(
						employeeId,
						yearMonth.v()),
						KrcdtMonMngAgreTime.class)
				.map(c -> c.toDomain());
	}

	/** 検索　（社員IDリスト） */
	@Override
	public List<AgreementTimeOfManagePeriod> findByEmployees(List<String> employeeIds, YearMonth yearMonth) {
		
		List<AgreementTimeOfManagePeriod> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			results.addAll(this.queryProxy().query(FIND_BY_EMPLOYEES, KrcdtMonMngAgreTime.class)
					.setParameter("employeeIds", splitData)
					.setParameter("yearMonth", yearMonth.v())
					.getList(c -> c.toDomain()));
		});
		results.sort(Comparator.comparing(AgreementTimeOfManagePeriod::getEmployeeId));
		return results;
	}
	
	/** 検索　（社員IDリストと年月リスト） */
	@Override
	public List<AgreementTimeOfManagePeriod> findBySidsAndYearMonths(List<String> employeeIds,
			List<YearMonth> yearMonths) {
		
		val yearMonthValues = yearMonths.stream().map(c -> c.v()).collect(Collectors.toList());
		
		List<AgreementTimeOfManagePeriod> results = new ArrayList<>();
		CollectionUtil.split(employeeIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, splitData -> {
			CollectionUtil.split(yearMonthValues, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, lstYearMonth -> {
				results.addAll(this.queryProxy().query(FIND_BY_SIDS_AND_YEARMONTHS, KrcdtMonMngAgreTime.class)
						.setParameter("employeeIds", splitData)
						.setParameter("yearMonths", lstYearMonth)
						.getList(c -> c.toDomain()));
			});
		});
		results.sort((o1, o2) -> {
			int tmp = o1.getEmployeeId().compareTo(o2.getEmployeeId());
			if (tmp != 0) return tmp;
			return o1.getYearMonth().compareTo(o2.getYearMonth());
		});
		return results;
	}
	
	/** 登録および更新 */
	@Override
	public void persistAndUpdate(AgreementTimeOfManagePeriod domain) {

		// キー
		val key = new KrcdtMonMngAgreTimePK(
				domain.getEmployeeId(),
				domain.getYearMonth().v());

		// 登録・更新を判断　および　キー値設定
		KrcdtMonMngAgreTime entity = this.getEntityManager().find(KrcdtMonMngAgreTime.class, key);
		if (entity == null){
			entity = new KrcdtMonMngAgreTime();
			entity.fromDomainForPersist(domain);
			this.getEntityManager().persist(entity);
		}
		else entity.fromDomainForUpdate(domain);
	}
	
	/** 削除 */
	@Override
	public void remove(String employeeId, YearMonth yearMonth) {

		this.getEntityManager().createQuery(REMOVE_BY_PK)
				.setParameter("employeeId", employeeId)
				.setParameter("yearMonth", yearMonth.v())
				.executeUpdate();
	}
}
