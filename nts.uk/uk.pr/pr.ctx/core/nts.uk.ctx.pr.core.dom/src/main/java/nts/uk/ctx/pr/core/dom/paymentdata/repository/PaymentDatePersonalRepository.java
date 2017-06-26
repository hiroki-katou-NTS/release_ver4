package nts.uk.ctx.pr.core.dom.paymentdata.repository;

import java.util.Optional;

import nts.uk.ctx.pr.core.dom.paymentdata.PaymentDatePersonal;

public interface PaymentDatePersonalRepository {
	Optional<PaymentDatePersonal> find(String companyCode, String personId);
}
