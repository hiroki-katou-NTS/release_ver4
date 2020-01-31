package nts.uk.ctx.at.shared.infra.repository.remainingnumber.interimremain;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.SneakyThrows;
import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.interimremain.KrcmtInterimRemainMng;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaInterimRemainRepository extends JpaRepository implements InterimRemainRepository {
	private static final String DELETE_BY_SID_PRIOD_TYPE = "DELETE FROM KrcmtInterimRemainMng c"
			+ " WHERE c.sId = :employeeId" + " AND c.ymd >= :startDate" + " AND c.ymd <= :endDate"
			+ " AND c.remainType = :remainType";
	private static final String DELETE_BY_SID_PRIOD = "DELETE FROM KrcmtInterimRemainMng c"
			+ " WHERE c.sId = :employeeId" + " AND c.ymd >= :startDate" + " AND c.ymd <= :endDate";
	private static final String DELETE_BY_ID = "DELETE FROM KrcmtInterimRemainMng c"
			+ " WHERE c.remainMngId = :remainMngId";

	@SneakyThrows
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<InterimRemain> getRemainBySidPriod(String employeeId, DatePeriod dateData, RemainType remainType) {
		try (PreparedStatement sql = this.connection().prepareStatement("SELECT * FROM KRCMT_INTERIM_REMAIN_MNG"
				+ " WHERE SID = ?" + " AND YMD >= ?" + " AND YMD <= ?" + " AND REMAIN_TYPE = ?");) {
			sql.setString(1, employeeId);
			sql.setDate(2, Date.valueOf(dateData.start().localDate()));
			sql.setDate(3, Date.valueOf(dateData.end().localDate()));
			sql.setInt(4, remainType.value);
			List<InterimRemain> entities = new NtsResultSet(sql.executeQuery()).getList(x -> toDomain(x));
			if (entities.isEmpty()) {
				return new ArrayList<>();
			}
			return entities;
		}
	}

	private InterimRemain toDomain(NtsResultRecord record) {
		return new InterimRemain(record.getString("REMAIN_MNG_ID"), record.getString("SID"),
				record.getGeneralDate("YMD"), record.getEnum("CREATOR_ATR", CreateAtr.class),
				record.getEnum("REMAIN_TYPE", RemainType.class), record.getEnum("REMAIN_ATR", RemainAtr.class));
	}

	private InterimRemain convertToDomainSet(KrcmtInterimRemainMng c) {
		return new InterimRemain(c.remainMngId, c.sId, c.ymd, EnumAdaptor.valueOf(c.createrAtr, CreateAtr.class),
				EnumAdaptor.valueOf(c.remainType, RemainType.class), EnumAdaptor.valueOf(c.remainAtr, RemainAtr.class));
	}

	@Override
	public Optional<InterimRemain> getById(String remainId) {
		return this.queryProxy().find(remainId, KrcmtInterimRemainMng.class).map(x -> convertToDomainSet(x));
	}

	@Override
	public void persistAndUpdateInterimRemain(InterimRemain domain) {

		// キー
		val key = domain.getRemainManaID();

		// 登録・更新
		KrcmtInterimRemainMng entity = this.getEntityManager().find(KrcmtInterimRemainMng.class, key);
		if (entity == null) {
			entity = new KrcmtInterimRemainMng();
			entity.remainMngId = domain.getRemainManaID();
			entity.sId = domain.getSID();
			entity.ymd = domain.getYmd();
			entity.createrAtr = domain.getCreatorAtr().value;
			entity.remainType = domain.getRemainType().value;
			entity.remainAtr = domain.getRemainAtr().value;
			this.getEntityManager().persist(entity);
		} else {
			entity.sId = domain.getSID();
			entity.ymd = domain.getYmd();
			entity.createrAtr = domain.getCreatorAtr().value;
			entity.remainType = domain.getRemainType().value;
			entity.remainAtr = domain.getRemainAtr().value;
			this.commandProxy().update(entity);
		}
		this.getEntityManager().flush();
	}

	@Override
	public void deleteById(String mngId) {
		this.getEntityManager().createQuery(DELETE_BY_ID).setParameter("remainMngId", mngId).executeUpdate();
	}

	@Override
	public void deleteBySidPeriodType(String employeeId, DatePeriod dateData, RemainType remainType) {
		this.getEntityManager().createQuery(DELETE_BY_SID_PRIOD_TYPE).setParameter("employeeId", employeeId)
				.setParameter("startDate", dateData.start()).setParameter("endDate", dateData.end())
				.setParameter("remainType", remainType.value).executeUpdate();

	}

	@Override
	public void deleteBySidPeriod(String employeeId, DatePeriod dateData) {
		this.getEntityManager().createQuery(DELETE_BY_SID_PRIOD).setParameter("employeeId", employeeId)
				.setParameter("startDate", dateData.start()).setParameter("endDate", dateData.end()).executeUpdate();
	}

	@SneakyThrows
	@Override
	public List<InterimRemain> getDataBySidDates(String sid, List<GeneralDate> baseDates) {
		List<InterimRemain> resultList = new ArrayList<>();
		CollectionUtil.split(baseDates, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			try (PreparedStatement sql = this.connection().prepareStatement("SELECT * FROM KRCMT_INTERIM_REMAIN_MNG"
					+ " WHERE SID = ?" + " AND YMD  IN (" + NtsStatement.In.createParamsString(subList) + ")");

			) {
				sql.setString(1, sid);
				for (int i = 0; i < subList.size(); i++) {
					GeneralDate loopDate = subList.get(i);
					sql.setDate(i + 2, Date.valueOf(loopDate.localDate()));
				}
				List<InterimRemain> entities = new NtsResultSet(sql.executeQuery()).getList(x -> toDomain(x));
				resultList.addAll(entities);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});
		return resultList;
	}

	// Fix bug 109524
	/** 検索 */
	@Override
	public Optional<InterimRemain> find(String sId, GeneralDate ymd) {
		String QUERY_BY_ID = "SELECT s FROM KrcmtInterimRemainMng s WHERE s.sId = :sId" + " s.ymd = :ymd";
		Optional<KrcmtInterimRemainMng> entity = this.queryProxy().query(QUERY_BY_ID, KrcmtInterimRemainMng.class)
				.setParameter("sId", sId).setParameter("ymd", ymd).getSingle();
		if (entity.isPresent()) {
			return Optional.ofNullable(convertToDomainSet(entity.get()));
		}
		return Optional.empty();
	}

	// Fix bug 109524
	/** 検索 （期間） */
	@SneakyThrows
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<InterimRemain> findByPeriodOrderByYmd(String employeeId, DatePeriod period) {
		try (PreparedStatement sql = this.connection().prepareStatement("SELECT * FROM KRCMT_INTERIM_REMAIN_MNG"
				+ " WHERE SID = ?" + " AND YMD >= ?" + " AND YMD <= ?" + " ORDER BY YMD");) {
			sql.setString(1, employeeId);
			sql.setDate(2, Date.valueOf(period.start().localDate()));
			sql.setDate(3, Date.valueOf(period.end().localDate()));
			List<InterimRemain> entities = new NtsResultSet(sql.executeQuery()).getList(x -> toDomain(x));
			if (entities.isEmpty()) {
				return new ArrayList<>();
			}
			return entities;
		}
	}

	// Fix bug 109524
	/** 削除 */
	@Override
	public void remove(String sId, GeneralDate ymd) {
		String sql = "delete  from KrcmtInterimRemainMng c + WHERE c.sId = :sId" + " AND c.ymd >= :ymd";
		this.getEntityManager().createQuery(sql).setParameter("sId", sId).setParameter("ymd", ymd).executeUpdate();
	}

	// Fix bug 109524
	/** 削除 （期間） */
	@Override
	public void removeByPeriod(String sId, DatePeriod period) {
		String sql = "delete  from KrcmtInterimRemainMng c + WHERE c.sId = :sId" + " AND c.ymd >= :startYmd"
				+ " AND c.ymd <= :endYmd";
		this.getEntityManager().createQuery(sql).setParameter("sId", sId).setParameter("startYmd", period.start())
				.setParameter("endYmd", period.end()).executeUpdate();
	}

	
	// Fix bug 109524
	/** 削除 （基準日以前） */
	@Override
	public void removePastYmd(String sId, GeneralDate criteriaDate) {
		String sql = "delete  from KrcmtInterimRemainMng c + WHERE c.sId = :sId" + " AND c.ymd <= :criteriaDate";
		this.getEntityManager().createQuery(sql).setParameter("sId", sId).setParameter("criteriaDate", criteriaDate)
				.executeUpdate();
	}

	// Fix bug 109524
	@SneakyThrows
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<InterimRemain> findBySidWorkTypePeriod(String sId, String workTypeCode, DatePeriod period) {
		try (PreparedStatement sql = this.connection().prepareStatement(
				"SELECT * FROM KRCMT_INTERIM_REMAIN_MNG a1" + " INNER JOIN KRCMT_INTERIM_ANNUAL_MNG a2 "
						+ " ON a1.REMAIN_MNG_ID = a2.ANNUAL_MNG_ID" + " WHERE a1.SID = ? " + " AND a2.WORKTYPE_CODE = ?"
						+ " AND a1.YMD >= ?" + " AND a1.YMD <= ?" + " ORDER BY YMD");) {
			sql.setString(1, sId);
			sql.setDate(2, Date.valueOf(period.start().localDate()));
			sql.setDate(3, Date.valueOf(period.end().localDate()));
			List<InterimRemain> entities = new NtsResultSet(sql.executeQuery()).getList(x -> toDomain(x));
			if (entities.isEmpty()) {
				return new ArrayList<>();
			}
			return entities;
		}
	}

	// Fix bug 109524
	@SneakyThrows
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<InterimRemain> findByEmployeeID(String sId) {
		try (PreparedStatement sql = this.connection()
				.prepareStatement("SELECT * FROM KRCMT_INTERIM_REMAIN_MNG a1" + " WHERE SID = ?");) {
			sql.setString(1, sId);
			List<InterimRemain> entities = new NtsResultSet(sql.executeQuery()).getList(x -> toDomain(x));
			if (entities.isEmpty()) {
				return new ArrayList<>();
			}
			return entities;
		}
	}
	
	// Fix bug 109524 - KrcdtRsvleaMngTemp

}
