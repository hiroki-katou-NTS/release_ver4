package nts.uk.ctx.at.shared.infra.repository.workrule.closure;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmpClosureEmploymentPK;
import nts.uk.ctx.at.shared.infra.entity.workrule.closure.KclmtClosureEmployment;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class JpaClosureEmploymentRepository extends JpaRepository implements ClosureEmploymentRepository {

	private static final String FIND;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KclmtClosureEmployment a ");
		builderString.append("WHERE a.kclmpClosureEmploymentPK.companyId = :companyId ");
		builderString.append("WHERE a.kclmpClosureEmploymentPK.employmentCD IN :employmentCDs ");
		FIND = builderString.toString();
	}

	@Override
	public Optional<ClosureEmployment> findByEmploymentCD(String companyID, String employmentCD) {
		return this.queryProxy()
				.find(new KclmpClosureEmploymentPK(companyID, employmentCD), KclmtClosureEmployment.class)
				.map(x -> convertToDomain(x));
	}

	/**
	 * get list by list employmentCD
	 * for KIF 001
	 */
	@Override
	public List<ClosureEmployment> findListEmployment(String companyId, List<String> employmentCDs) {
		return this.queryProxy().query(FIND, KclmtClosureEmployment.class).setParameter("companyId", companyId)
				.setParameter("employmentCDs", employmentCDs).getList(f -> convertToDomain(f));
	}

	private ClosureEmployment convertToDomain(KclmtClosureEmployment kclmtClosureEmployment) {
		return new ClosureEmployment(kclmtClosureEmployment.kclmpClosureEmploymentPK.companyId,
				kclmtClosureEmployment.kclmpClosureEmploymentPK.employmentCD, kclmtClosureEmployment.closureId);
	}
}
