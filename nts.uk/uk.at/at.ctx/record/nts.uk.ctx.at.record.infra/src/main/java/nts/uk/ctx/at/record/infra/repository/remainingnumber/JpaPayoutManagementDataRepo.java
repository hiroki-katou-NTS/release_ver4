package nts.uk.ctx.at.record.infra.repository.remainingnumber;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.remainingnumber.base.CompensatoryDayoffDate;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.PayoutManagementData;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.PayoutManagementDataRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.SubstitutionOfHDManagementData;
import nts.uk.ctx.at.record.infra.entity.remainingnumber.paymana.KrcmtPayoutManaData;


@Stateless
public class JpaPayoutManagementDataRepo extends JpaRepository implements PayoutManagementDataRepository {

	private String QUERY_BYSID = "SELECT p FROM KrcmtPayoutManaData p WHERE p.cID = :cid AND p.sID =:employeeId";
	
	private String QUERY_BY_SID_CID_DAYOFF = "SELECT p FROM KrcmtPayoutManaData p WHERE p.cID = :cid AND p.sID =:employeeId AND p.dayoffDate = dayoffDate";

	private String QUERY_BYSID_WITH_COND = String.join(" ", QUERY_BYSID, "AND p.stateAtr = :state");

	private final String QUERY_BY_SID_DATEPERIOD = "SELECT p FROM KrcmtPayoutManaData p WHERE p.sID =:sid "
			+ " AND (p.stateAtr = :state OR p.payoutId in (SELECT ps.krcmtPayoutSubOfHDManaPK.payoutId FROM KrcmtPayoutSubOfHDMana ps WHERE ps.krcmtPayoutSubOfHDManaPK.subOfHDID =:subOfHDID))";

	private final String  QUERY_BY_SID_STATE_AND_IN_SUB = "SELECT p FROM KrcmtPayoutManaData p WHERE p.sID = :sid AND (p.stateAtr = 0 OR p.payoutId in "
			+ "(SELECT ps.krcmtPayoutSubOfHDManaPK.payoutId FROM KrcmtPayoutSubOfHDMana ps inner join KrcmtSubOfHDManaData s on s.subOfHDID = ps.krcmtPayoutSubOfHDManaPK.subOfHDID where s.remainDays <> 0))";

	private final String  QUERY_BY_SID_PERIOD_AND_IN_SUB = "SELECT p FROM KrcmtPayoutManaData p WHERE p.sID = :sid AND ((p.dayOff >= :startDate AND p.dayOff <= :endDate) OR p.payoutId in "
			+ "(SELECT ps.krcmtPayoutSubOfHDManaPK.payoutId FROM KrcmtPayoutSubOfHDMana ps inner join KrcmtSubOfHDManaData s on s.subOfHDID = ps.krcmtPayoutSubOfHDManaPK.subOfHDID where s.dayOff >= :startDate AND s.dayOff <= :endDate))";
	
	private String DELETE_QUERY = "DELETE FROM KrcmtPayoutManaData a WHERE a.sID = :sID AND a.dayOff = :dayOff";

	private String QUERY_BY_SUBID = "SELECT * FROM KrcmtPayoutManaData p where p.payoutId (SELECT ps.krcmtPayoutSubOfHDManaPK.payoutId FROM KrcmtPayoutSubOfHDMana ps WHERE ps.krcmtPayoutSubOfHDManaPK.subOfHDID =:subOfHDID)";
	
	@Override
	public List<PayoutManagementData> getSid(String cid, String sid) {
		List<KrcmtPayoutManaData> list = this.queryProxy().query(QUERY_BYSID, KrcmtPayoutManaData.class)
				.setParameter("cid", cid).setParameter("employeeId", sid).getList();
		return list.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	@Override
	public List<PayoutManagementData> getSidWithCod(String cid, String sid, int state) {
		List<KrcmtPayoutManaData> list = this.queryProxy().query(QUERY_BYSID_WITH_COND, KrcmtPayoutManaData.class)
				.setParameter("cid", cid).setParameter("employeeId", sid).setParameter("state", state).getList();
		return list.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	/**
	 * Convert to domain
	 * 
	 * @param entity
	 * @return
	 */
	private PayoutManagementData toDomain(KrcmtPayoutManaData entity) {
		return new PayoutManagementData(entity.payoutId, entity.cID, entity.sID, entity.unknownDate, entity.dayOff,
				entity.expiredDate, entity.lawAtr, entity.unUsedDays, entity.occurredDays, entity.stateAtr);
	}

	@Override
	public void create(PayoutManagementData domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	/**
	 * Convert to entity
	 * 
	 * @param domain
	 * @return
	 */
	private KrcmtPayoutManaData toEntity(PayoutManagementData domain) {
		KrcmtPayoutManaData entity = new KrcmtPayoutManaData();
		entity.payoutId = domain.getPayoutId();
		entity.sID = domain.getSID();
		entity.cID = domain.getCID();
		entity.unknownDate = domain.getPayoutDate().isUnknownDate();
		if (domain.getPayoutDate().getDayoffDate().isPresent()) {
			entity.dayOff = domain.getPayoutDate().getDayoffDate().get();
		}
		entity.expiredDate = domain.getExpiredDate();
		entity.lawAtr = domain.getLawAtr().value;
		entity.occurredDays = domain.getOccurredDays().v();
		entity.unUsedDays = domain.getUnUsedDays().v();
		entity.stateAtr = domain.getStateAtr().value;
		return entity;
	}

	@Override
	public void delete(String payoutId) {
		this.commandProxy().remove(KrcmtPayoutManaData.class, payoutId);
	}

	@Override
	public void delete(String employeeId, GeneralDate dayOff) {
		this.getEntityManager().createQuery(DELETE_QUERY).setParameter("sID", employeeId).setParameter("dayOff", dayOff).executeUpdate();

	}

	@Override
	public void update(PayoutManagementData domain) {
		this.commandProxy().update(toEntity(domain));
	}

	@Override
	public Optional<PayoutManagementData> findByID(String ID) {
		Optional<KrcmtPayoutManaData> entity = this.queryProxy().find(ID, KrcmtPayoutManaData.class);
		if (entity.isPresent()) {
			return Optional.ofNullable(toDomain(entity.get()));
		}
		return Optional.empty();
	}

	@Override
	public List<PayoutManagementData> getBySidDatePeriod(String sid, String subOfHDID, int digestionAtr) {
		List<KrcmtPayoutManaData> listSubOfHD = this.queryProxy()
				.query(QUERY_BY_SID_DATEPERIOD, KrcmtPayoutManaData.class).setParameter("sid", sid)
				.setParameter("state", digestionAtr).setParameter("subOfHDID", subOfHDID).getList();
		return listSubOfHD.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	@Override
	public List<PayoutManagementData> getBySidStateAndInSub(String sid) {
		List<KrcmtPayoutManaData> listpayout = this.queryProxy()
				.query(QUERY_BY_SID_STATE_AND_IN_SUB, KrcmtPayoutManaData.class).setParameter("sid", sid).getList();
		return listpayout.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	public Optional<PayoutManagementData> find(String sID, String cID, CompensatoryDayoffDate dayoffDate) {
		return this.queryProxy().find(QUERY_BY_SID_CID_DAYOFF, KrcmtPayoutManaData.class).map(i -> toDomain(i));
	}
	
	public List<PayoutManagementData> getBySidPeriodAndInSub(String sid, GeneralDate startDate, GeneralDate endDate) {
		List<KrcmtPayoutManaData> listpayout = this.queryProxy()
				.query(QUERY_BY_SID_PERIOD_AND_IN_SUB, KrcmtPayoutManaData.class).setParameter("sid", sid)
				.setParameter("startDate", startDate).setParameter("endDate", endDate).getList();
		return listpayout.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	@Override
	public List<PayoutManagementData> getDayoffDateBysubOfHDID(String subOfHDID) {
		List<KrcmtPayoutManaData> listpayout = this.queryProxy().query(QUERY_BY_SUBID, KrcmtPayoutManaData.class ).setParameter("subOfHDID", subOfHDID).getList(); 
		return listpayout.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

}
