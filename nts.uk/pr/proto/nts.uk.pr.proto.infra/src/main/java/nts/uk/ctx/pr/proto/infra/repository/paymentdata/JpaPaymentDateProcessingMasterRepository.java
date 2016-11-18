package nts.uk.ctx.pr.proto.infra.repository.paymentdata;

import java.util.Optional;

import javax.enterprise.context.RequestScoped;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.pr.proto.dom.paymentdata.paymentdatemaster.PaymentDateProcessingMaster;
import nts.uk.ctx.pr.proto.dom.paymentdata.repository.PaymentDateProcessingMasterRepository;
import nts.uk.ctx.pr.proto.infra.entity.paymentdata.QpdmtPaydayProcessing;
import nts.uk.ctx.pr.proto.infra.entity.paymentdata.QpdmtPaydayProcessingPK;

@RequestScoped
public class JpaPaymentDateProcessingMasterRepository extends JpaRepository
		implements PaymentDateProcessingMasterRepository {

	private final String SELECT_NOT_WITH_PROCESSINGNO = "SELECT c FROM QPDMT_PAYDAY_PROCESSING WHERE c.qpdmtPaydayProcessingPK.ccd = :CCD and c.qpdmtPaydayProcessingPK.payBonusAtr = :payBonusAtr";

	@Override
	public Optional<PaymentDateProcessingMaster> find(String companyCode, int paymentBonusAtribute,
			int processingNo) {
		return this.queryProxy()
				.find(new QpdmtPaydayProcessingPK(companyCode, paymentBonusAtribute, processingNo), QpdmtPaydayProcessing.class).map(c -> toDomain(c));
	}

	@Override
	public Optional<PaymentDateProcessingMaster> find(String companyCode, int paymentBonusAtribute) {
		return this.queryProxy()
				.query(SELECT_NOT_WITH_PROCESSINGNO, QpdmtPaydayProcessing.class).setParameter("CCD", companyCode)
				.setParameter("payBonusAtr", paymentBonusAtribute)
				.getSingle(c -> toDomain(c));
	}

	private static PaymentDateProcessingMaster toDomain(QpdmtPaydayProcessing entity) {
		PaymentDateProcessingMaster domain = PaymentDateProcessingMaster.createFromJavaType(
				entity.qpdmtPaydayProcessingPK.payBonusAtr, entity.qpdmtPaydayProcessingPK.processingNo,
				entity.processingName, entity.currentProcessingYm, entity.dispAtr);
		entity.toDomain(domain);
		return domain;
	}

}
