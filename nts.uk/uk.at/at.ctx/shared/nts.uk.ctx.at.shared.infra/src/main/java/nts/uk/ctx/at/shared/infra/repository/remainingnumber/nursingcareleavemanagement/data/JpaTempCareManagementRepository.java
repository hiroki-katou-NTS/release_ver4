package nts.uk.ctx.at.shared.infra.repository.remainingnumber.nursingcareleavemanagement.data;

import java.util.List;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.care.interimdata.TempCareManagement;
import nts.uk.ctx.at.shared.dom.remainingnumber.nursingcareleavemanagement.care.interimdata.TempCareManagementRepository;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.nursingcareleave.care.interimdata.KshdtInterimCareData;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.nursingcareleave.care.interimdata.KshdtInterimCareDataPK;
import nts.uk.shr.com.context.AppContexts;

/**
 * リポジトリ実装：暫定介護管理データ
 * @author yuri_tamakoshi
 */
@Stateless
public class JpaTempCareManagementRepository extends JpaRepository implements TempCareManagementRepository {

	private static final String SELECT_BY_PERIOD = "SELECT a FROM KshdtInterimCareData a "
			+ "WHERE a.pk.sid = :employeeId "
			+ "AND a.pk.ymd >= :startYmd "
			+ "AND a.pk.ymd <= :endYmd "
			+ "ORDER BY a.pk.ymd ";

	private static final String SELECT_BY_EMPLOYEEID_YMD = "SELECT a FROM KshdtInterimCareData a"
			+ " WHERE a.pk.sid = :employeeID"
			+ "AND a.pk.ymd =  : ymd "
			+ " ORDER BY a.pk.ymd ASC";

	private static final String DELETE_BY_SID_YMD = "DELETE FROM KshdtInterimCareData a"
			+ " WHERE a.pk.sid = :sid"
			+ "	AND a.pk.ymd =  :ymd ";


	/** 検索 */
	@Override
	public List<TempCareManagement> find(String employeeId, GeneralDate ymd){

		return this.queryProxy().query(SELECT_BY_EMPLOYEEID_YMD, KshdtInterimCareData.class)
				.setParameter("employeeId", employeeId)
				.setParameter("ymd",ymd)
				.getList(c -> c.toDomain());
	}

	/** 検索　（期間） */
	@Override
	public List<TempCareManagement> findByPeriodOrderByYmd(String employeeId, DatePeriod period) {

		return this.queryProxy().query(SELECT_BY_PERIOD, KshdtInterimCareData.class)
				.setParameter("employeeId", employeeId)
				.setParameter("startYmd", period.start())
				.setParameter("endYmd", period.end())
				.getList(c -> c.toDomain());
	}


	/** 登録および更新 */
	@Override
	public void persistAndUpdate(TempCareManagement domain) {

		KshdtInterimCareDataPK pk = new KshdtInterimCareDataPK(
				AppContexts.user().companyId(),
				domain.getSID(),
				domain.getYmd(),
				domain.getAppTimeType().map(x -> x.isHourlyTimeType() ? 1 : 0).orElse(0),
				domain.getAppTimeType().flatMap(c -> c.getAppTimeType()).map(c -> c.value).orElse(0));

		// 登録・更新
		this.queryProxy().find(pk, KshdtInterimCareData.class).ifPresent(entity -> {
			entity.fromDomainForUpdate(domain);
			this.getEntityManager().flush();
			return;
		});

		KshdtInterimCareData entity = new KshdtInterimCareData();
		entity.pk = pk;
		entity.fromDomainForPersist(domain);
		this.getEntityManager().persist(entity);
		this.getEntityManager().flush();
	}


	/** 削除 */
	@Override
	public void remove(String employeeId, GeneralDate ymd, TempCareManagement domain) {

		val key = domain.getRemainManaID();

		this.commandProxy().remove(KshdtInterimCareData.class, key);
	}

	/**
	 * 暫定介護管理データの取得
	 * @param 社員ID employeeId
	 * @param 期間 period
	 */
	@Override
	public List<TempCareManagement> findBySidPeriod(String employeeId, DatePeriod period){

		return queryProxy().query(SELECT_BY_PERIOD, KshdtInterimCareData.class)
				.setParameter("employeeId", employeeId)
				.setParameter("startYmd", period.start())
				.setParameter("endYmd", period.end())
				.getList(c -> c.toDomain());
	}

	@Override
	public void deleteBySidAndYmd(String sid, GeneralDate ymd) {
		this.getEntityManager().createQuery(DELETE_BY_SID_YMD)
		.setParameter("sid", sid)
		.setParameter("ymd", ymd)
		.executeUpdate();
	}

}
