package nts.uk.ctx.pr.core.infra.repository.paymentdata;

import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.paymentdata.paymentdatemaster.PaymentDateMaster;
import nts.uk.ctx.pr.core.dom.paymentdata.repository.PaymentDateMasterRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.processing.yearmonth.QpdmtPayday;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.processing.yearmonth.QpdmtPaydayPK;

@Stateless
public class JpaPaymentDateMasterRepository extends JpaRepository implements PaymentDateMasterRepository {

	@Override
	public Optional<PaymentDateMaster> find(String companyCode, int payBonusAtr, int processingYm, int sparePayAtr,
			int processingNo) {
		Optional<PaymentDateMaster> paymentDateMaster = this.queryProxy()
				.find(new QpdmtPaydayPK(companyCode, payBonusAtr, processingNo, processingYm, sparePayAtr),
						QpdmtPayday.class)
				.map(c -> toDomain(c));
		return paymentDateMaster;
	}

	private static PaymentDateMaster toDomain(QpdmtPayday entity) {
		PaymentDateMaster domain = PaymentDateMaster.createFromJavaType(entity.neededWorkDay,
				entity.qpdmtPaydayPK.processingNo, entity.qpdmtPaydayPK.processingYm, entity.qpdmtPaydayPK.sparePayAtr,
				entity.qpdmtPaydayPK.payBonusAtr, entity.stdDate);
		// entity.toDomain(domain);
		return domain;
	}

}
