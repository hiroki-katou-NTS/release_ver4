package nts.uk.ctx.at.record.infra.repository.workrecord.identificationstatus;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.record.infra.entity.workrecord.identificationstatus.KrcdtIdentificationStatus;

@Stateless
public class JpaIdentificationRepository extends JpaRepository implements IdentificationRepository {

	private static final String GET_BY_EMPLOYEE_ID = "SELECT c from KrcdtIdentificationStatus c "
			+ " WHERE c.krcdtIdentificationStatusPK.employeeId = :employeeId "
			+ " AND c.krcdtIdentificationStatusPK.processingYmd BETWEEN :startDate AND :endDate  ";

	private static final String GET_BY_CODE = "SELECT c from KrcdtIdentificationStatus c "
			+ " WHERE c.krcdtIdentificationStatusPK.employeeId = :employeeId "
			+ " AND c.krcdtIdentificationStatusPK.processingYmd = :processingYmd ";

	private static final String REMOVE_BY_KEY = "DELETE FROM KrcdtIdentificationStatus c "
			+ " WHERE c.krcdtIdentificationStatusPK.employeeId = :employeeId "
			+ " AND c.krcdtIdentificationStatusPK.processingYmd = :processingYmd "
			+ " AND c.krcdtIdentificationStatusPK.companyID = :companyID ";

	@Override
	public List<Identification> findByEmployeeID(String employeeID, GeneralDate startDate, GeneralDate endDate) {
		return this.queryProxy().query(GET_BY_EMPLOYEE_ID, KrcdtIdentificationStatus.class)
				.setParameter("employeeId", employeeID).setParameter("startDate", startDate)
				.setParameter("endDate", endDate).getList(c -> c.toDomain());
	}

	@Override
	public Optional<Identification> findByCode(String employeeID, GeneralDate processingYmd) {
		return this.queryProxy().query(GET_BY_CODE, KrcdtIdentificationStatus.class)
				.setParameter("employeeID", employeeID).setParameter("processingYmd", processingYmd)
				.getSingle(c -> c.toDomain());
	}

	@Override
	public void insert(Identification identification) {
		this.commandProxy().insert(KrcdtIdentificationStatus.toEntity(identification));
	}

	@Override
	public void remove(String companyId, String employeeId, GeneralDate processingYmd) {
		this.getEntityManager().createQuery(REMOVE_BY_KEY, KrcdtIdentificationStatus.class)
				.setParameter("employeeId", employeeId).setParameter("processingYmd", processingYmd)
				.setParameter("companyID", companyId).executeUpdate();
	}

}
