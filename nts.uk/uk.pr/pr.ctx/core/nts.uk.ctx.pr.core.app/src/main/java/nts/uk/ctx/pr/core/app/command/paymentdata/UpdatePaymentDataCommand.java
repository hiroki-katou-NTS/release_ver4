package nts.uk.ctx.pr.core.app.command.paymentdata;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.pr.core.app.command.paymentdata.base.PaymentDataCommandBase;
import nts.uk.ctx.pr.core.dom.paymentdata.Payment;

/**
 * Command: add payment data
 * 
 * @author vunv
 *
 */
@Getter
@Setter
public class UpdatePaymentDataCommand extends PaymentDataCommandBase {
	/** version */
	private long version;

	/**
	 * Convert to domain object.
	 * 
	 * @return domain
	 */
	public Payment toDomain(String companyCode) {
		Payment domain = super.getPaymentHeader().toDomain(companyCode);
		domain.setVersion(this.version);
		return domain;
	}
}
