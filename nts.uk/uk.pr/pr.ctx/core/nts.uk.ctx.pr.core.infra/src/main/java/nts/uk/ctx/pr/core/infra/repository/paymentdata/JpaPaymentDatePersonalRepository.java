package nts.uk.ctx.pr.core.infra.repository.paymentdata;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.paymentdata.PaymentDatePersonal;
import nts.uk.ctx.pr.core.dom.paymentdata.repository.PaymentDatePersonalRepository;
import nts.uk.ctx.pr.core.infra.entity.paymentdata.QpdptPayday;
import nts.uk.ctx.pr.core.infra.entity.paymentdata.QpdptPaydayPK;

@RequestScoped
public class JpaPaymentDatePersonalRepository extends JpaRepository implements PaymentDatePersonalRepository {

	@Override
	public Optional<PaymentDatePersonal> find(String companyCode, String personId) {
		return this.queryProxy().find(new QpdptPaydayPK(companyCode, personId), QpdptPayday.class)
				.map(x -> new PaymentDatePersonal(companyCode, personId, x.processingNo));
	}

}
