package nts.uk.ctx.pr.core.infra.repository.paymentdata;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.enums.DisplayAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.paymentdatemaster.PaymentDateProcessingMaster;
import nts.uk.ctx.pr.core.dom.paymentdata.repository.PaymentDateProcessingMasterRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.processing.yearmonth.QpdmtPaydayProcessing;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.processing.yearmonth.QpdmtPaydayProcessingPK;

@Stateless
public class JpaPaymentDateProcessingMasterRepository extends JpaRepository
		implements PaymentDateProcessingMasterRepository {

	private final String SELECT_NOT_WITH_PROCESSINGNO = "SELECT c FROM QpdmtPaydayProcessing c "
			+ "WHERE c.qpdmtPaydayProcessingPK.ccd = :CCD and c.qpdmtPaydayProcessingPK.payBonusAtr = :payBonusAtr and c.dispAtr = :dispAtr ";

	@Override
	public Optional<PaymentDateProcessingMaster> find(String companyCode, int paymentBonusAtribute,
			int processingNo) {
		try {
			return this.queryProxy()
					.find(new QpdmtPaydayProcessingPK(companyCode, processingNo), QpdmtPaydayProcessing.class).map(c -> toDomain(c));
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		
	}

	@Override
	public List<PaymentDateProcessingMaster> findAll(String companyCode, int paymentBonusAtribute) {
		return this.queryProxy()
				.query(SELECT_NOT_WITH_PROCESSINGNO, QpdmtPaydayProcessing.class).setParameter("CCD", companyCode)
				.setParameter("payBonusAtr", paymentBonusAtribute)
				.setParameter("dispAtr", DisplayAtr.DISPLAY.value)
				.getList(c -> toDomain(c));
	}

	private static PaymentDateProcessingMaster toDomain(QpdmtPaydayProcessing entity) {
		PaymentDateProcessingMaster domain = PaymentDateProcessingMaster.createFromJavaType(
				0, entity.qpdmtPaydayProcessingPK.processingNo,
				entity.processingName, entity.currentProcessingYm, entity.dispSet);
		//entity.toDomain(domain);
		return domain;
	}
	@Override
	public Optional<PaymentDateProcessingMaster> find(String companyCode, int processingNo) {
		return this.queryProxy()
				.find(new QpdmtPaydayProcessingPK(companyCode, processingNo), QpdmtPaydayProcessing.class)
				.map(c -> toDomain(c));
	}

}
