package nts.uk.ctx.pr.core.dom.paymentdata.service.internal;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.dom.enums.PayBonusAtr;
import nts.uk.ctx.pr.core.dom.paymentdata.Payment;
import nts.uk.ctx.pr.core.dom.paymentdata.repository.PaymentDataRepository;
import nts.uk.ctx.pr.core.dom.paymentdata.service.PaymentDataCheckService;

@Stateless
public class PaymentDataCheckServiceImpl implements PaymentDataCheckService {
	@Inject
	private PaymentDataRepository paymentDataRepo;
	
	@Override
	public boolean isExists(String companyCode, String personId, PayBonusAtr payBonusAtr,
			int processingYearMonth) {
		List<Payment> paymentHeaders = paymentDataRepo.findPaymentHeader(companyCode, personId, payBonusAtr.value, processingYearMonth);
		
		return paymentHeaders.size() > 0;
	}


}
