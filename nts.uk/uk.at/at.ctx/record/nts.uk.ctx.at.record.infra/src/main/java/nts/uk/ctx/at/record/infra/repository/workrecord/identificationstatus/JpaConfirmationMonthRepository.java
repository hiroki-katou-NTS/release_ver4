package nts.uk.ctx.at.record.infra.repository.workrecord.identificationstatus;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.month.ConfirmationMonth;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.ConfirmationMonthRepository;
import nts.uk.ctx.at.record.infra.entity.workrecord.identificationstatus.month.KrcdtConfirmationMonth;
import nts.uk.ctx.at.record.infra.entity.workrecord.identificationstatus.month.KrcdtConfirmationMonthPK;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;

@Stateless
public class JpaConfirmationMonthRepository  extends JpaRepository implements ConfirmationMonthRepository{
    
	private static final String DELETE_BY_PARENT_PK = "DELETE FROM KrcdtConfirmationMonth a "
			+ "WHERE a.krcdtConfirmationMonthPK.companyID = :companyID "
			+ "AND a.krcdtConfirmationMonthPK.employeeId = :employeeId "
			+ "AND a.krcdtConfirmationMonthPK.closureId = :closureId ";
	@Override
	public Optional<ConfirmationMonth> findByKey(String companyID, String employeeID, ClosureId closureId) {
		return this.queryProxy().find(new KrcdtConfirmationMonthPK(companyID, employeeID,
						closureId.value) , KrcdtConfirmationMonth.class).map(x -> x.toDomain());
	}

	@Override
	public void insert(ConfirmationMonth confirmationMonth) {
		this.commandProxy().insert(new KrcdtConfirmationMonth(
				new KrcdtConfirmationMonthPK(confirmationMonth.getCompanyID().v(), confirmationMonth.getEmployeeId(),
						confirmationMonth.getClosureId().value),
				confirmationMonth.getClosureDay().v(), confirmationMonth.getProcessYM().v(),
				confirmationMonth.getIndentifyYmd()));
	}

	@Override
	public void delete(String companyId, String employeeId, int closureId) {
		this.getEntityManager().createNamedQuery(DELETE_BY_PARENT_PK, KrcdtConfirmationMonth.class)
				.setParameter("companyID", companyId).setParameter("employeeId", employeeId)
				.setParameter("closureId", closureId).executeUpdate();
	}

}
