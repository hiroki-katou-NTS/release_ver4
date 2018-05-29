package nts.uk.ctx.at.record.infra.repository.remainingnumber;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.error.BusinessException;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.remainingnumber.base.CompensatoryDayoffDate;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.SubstitutionOfHDManaDataRepository;
import nts.uk.ctx.at.record.dom.remainingnumber.paymana.SubstitutionOfHDManagementData;
import nts.uk.ctx.at.record.infra.entity.remainingnumber.paymana.KrcmtSubOfHDManaData;

@Stateless
public class JpaSubstitutionOfHDManaDataRepo extends JpaRepository implements SubstitutionOfHDManaDataRepository {
	
	private String QUERY_BY_SID_CID_HOLIDAYDATE = "SELECT p FROM KrcmtSubOfHDManaData p WHERE p.cID = :cid AND p.sID =:employeeId AND p.holidayDate = holidayDate";
	
	private String QUERY_BYSID = "SELECT s FROM KrcmtSubOfHDManaData s WHERE s.sID = :sid AND s.cID = :cid";

	private String QUERY_BYSID_REM_COD = String.join(" ", QUERY_BYSID, "AND s.remainDays > 0");

	private final String QUERY_BY_SID_DATEPERIOD = "SELECT s FROM KrcmtSubOfHDManaData s WHERE s.sID = :sid "
			+ " AND (s.remainDays <> :remainDays OR s.subOfHDID in "
			+ "(SELECT ps.krcmtPayoutSubOfHDManaPK.subOfHDID FROM KrcmtPayoutSubOfHDMana ps WHERE ps.krcmtPayoutSubOfHDManaPK.payoutId =:payoutID))";

	private final String QUERY_BY_SID_REMAIN_AND_IN_PAYOUT = "SELECT s FROM KrcmtSubOfHDManaData s WHERE s.sID = :sid AND (s.remainDays <> 0 OR s.subOfHDID in "
			+ "(SELECT ps.krcmtPayoutSubOfHDManaPK.subOfHDID FROM KrcmtPayoutSubOfHDMana ps inner join KrcmtPayoutManaData p on p.payoutId = ps.krcmtPayoutSubOfHDManaPK.payoutId where p.sID = :sid AND p.stateAtr = 0))";

	private final String QUERY_BY_SID_PERIOD_AND_IN_PAYOUT = "SELECT s FROM KrcmtSubOfHDManaData s WHERE s.sID = :sid AND ((s.dayOff >= :startDate AND s.dayOff <= :endDate) OR s.subOfHDID in "
			+ "(SELECT ps.krcmtPayoutSubOfHDManaPK.subOfHDID FROM KrcmtPayoutSubOfHDMana ps inner join KrcmtPayoutManaData p on p.payoutId = ps.krcmtPayoutSubOfHDManaPK.payoutId where p.sID = :sid AND p.dayOff >= :startDate AND p.dayOff <= :endDate))";
	
	private String DELETE_QUERY = "DELETE FROM KrcmtSubOfHDManaData a WHERE a.subOfHDID = :subOfHDID";
	
	private String QUERY_DELETE_SUB = "DELETE FROM KrcmtPayoutSubOfHDMana p WHERE p.krcmtPayoutSubOfHDManaPK.subOfHDID = :subOfHDID ";


	
	
	@Override
	public List<SubstitutionOfHDManagementData> getBysiD(String cid, String sid) {
		List<KrcmtSubOfHDManaData> list = this.queryProxy().query(QUERY_BYSID, KrcmtSubOfHDManaData.class)
				.setParameter("sid", sid).setParameter("cid", cid).getList();
		return list.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	@Override
	public List<SubstitutionOfHDManagementData> getBysiDRemCod(String cid, String sid) {
		List<KrcmtSubOfHDManaData> list = this.queryProxy().query(QUERY_BYSID_REM_COD, KrcmtSubOfHDManaData.class)
				.setParameter("sid", sid).setParameter("cid", cid).getList();
		return list.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	/**
	 * Convert to domain
	 * 
	 * @param entity
	 * @return
	 */
	private SubstitutionOfHDManagementData toDomain(KrcmtSubOfHDManaData entity) {
		return new SubstitutionOfHDManagementData(entity.subOfHDID, entity.cID, entity.sID, entity.unknownDate,
				entity.dayOff, entity.requiredDays, entity.remainDays);
	}

	@Override
	public void create(SubstitutionOfHDManagementData domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	private KrcmtSubOfHDManaData toEntity(SubstitutionOfHDManagementData domain) {
		KrcmtSubOfHDManaData entity = new KrcmtSubOfHDManaData();
		entity.subOfHDID = domain.getSubOfHDID();
		entity.sID = domain.getSID();
		entity.cID = domain.getCid();
		entity.unknownDate = domain.getHolidayDate().isUnknownDate();
		if (domain.getHolidayDate().getDayoffDate().isPresent()) {
			entity.dayOff = domain.getHolidayDate().getDayoffDate().get();
		}
		entity.requiredDays = domain.getRequiredDays().v();
		entity.remainDays = domain.getRemainDays().v();
		return entity;
	}

	@Override
	public void deletePayoutSubOfHDMana(String subOfHDID) {
		this.getEntityManager().createQuery(QUERY_DELETE_SUB).setParameter("subOfHDID", subOfHDID).executeUpdate();
	}

	@Override
	public void delete(String subOfHDID) {
		Optional<KrcmtSubOfHDManaData> entity = this.queryProxy().find(subOfHDID, KrcmtSubOfHDManaData.class);
		if(entity.isPresent()){
			this.getEntityManager().createQuery(DELETE_QUERY).setParameter("subOfHDID", subOfHDID).executeUpdate();
			this.getEntityManager().createQuery(QUERY_DELETE_SUB).setParameter("subOfHDID", subOfHDID).executeUpdate();
		}else{
			throw new  BusinessException("Msg_198");
		}
	}

	@Override
	public void update(SubstitutionOfHDManagementData domain) {
		this.commandProxy().update(toEntity(domain));

	}

	public Optional<SubstitutionOfHDManagementData> findByID(String Id) {
		Optional<KrcmtSubOfHDManaData> entity = this.queryProxy().find(Id, KrcmtSubOfHDManaData.class);
		if (entity.isPresent()) {
			return Optional.ofNullable(toDomain(entity.get()));
		}
		return Optional.empty();
	}


	public List<SubstitutionOfHDManagementData> getBySidDatePeriod(String sid, String payoutID, Double remainDays) {
		List<KrcmtSubOfHDManaData> listSubOfHD = this.queryProxy()
				.query(QUERY_BY_SID_DATEPERIOD, KrcmtSubOfHDManaData.class).setParameter("sid", sid)
				.setParameter("remainDays", remainDays).setParameter("payoutID", payoutID).getList();
		return listSubOfHD.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	public List<SubstitutionOfHDManagementData> getBySidRemainDayAndInPayout(String sid) {
		List<KrcmtSubOfHDManaData> listSubOfHD = this.queryProxy()
				.query(QUERY_BY_SID_REMAIN_AND_IN_PAYOUT, KrcmtSubOfHDManaData.class).setParameter("sid", sid)
				.getList();
		return listSubOfHD.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	@Override
	public List<SubstitutionOfHDManagementData> getBySidPeriodAndInPayout(String sid, GeneralDate startDate,
			GeneralDate endDate) {
		List<KrcmtSubOfHDManaData> listSubOfHD = this.queryProxy()
				.query(QUERY_BY_SID_PERIOD_AND_IN_PAYOUT, KrcmtSubOfHDManaData.class).setParameter("sid", sid)
				.setParameter("startDate", startDate).setParameter("endDate", endDate).getList();
		return listSubOfHD.stream().map(i -> toDomain(i)).collect(Collectors.toList());
	}

	@Override
	public Optional<SubstitutionOfHDManagementData> find(String sID, String cID, CompensatoryDayoffDate holidayDate) {
		return this.queryProxy().find(QUERY_BY_SID_CID_HOLIDAYDATE, KrcmtSubOfHDManaData.class).map(i -> toDomain(i));
	}



}
