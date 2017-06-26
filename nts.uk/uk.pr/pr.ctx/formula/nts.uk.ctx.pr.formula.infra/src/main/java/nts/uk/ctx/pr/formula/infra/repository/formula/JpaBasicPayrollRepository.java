package nts.uk.ctx.pr.formula.infra.repository.formula;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.formula.dom.formula.BasicPayroll;
import nts.uk.ctx.pr.formula.dom.repository.BasicPayrollRepository;
import nts.uk.ctx.pr.formula.infra.entity.formula.QclstBasicPayroll;
import nts.uk.ctx.pr.formula.infra.entity.formula.QclstBasicPayrollPK;

@Stateless
public class JpaBasicPayrollRepository extends JpaRepository implements BasicPayrollRepository {

	@Override
	public Optional<BasicPayroll> findAll(String companyCode) {
		return this.queryProxy().find(new QclstBasicPayrollPK(companyCode), QclstBasicPayroll.class)
				.map(q -> toDomain(q));
	}

	private BasicPayroll toDomain(QclstBasicPayroll qclstBasicPayroll) {
		BasicPayroll basicPayroll = BasicPayroll.createFromJavaType(qclstBasicPayroll.qclstBasicPayrollPK.companyCode,
				qclstBasicPayroll.timeNotationSetting, qclstBasicPayroll.baseDays, qclstBasicPayroll.baseHours);
		return basicPayroll;
	}
}
