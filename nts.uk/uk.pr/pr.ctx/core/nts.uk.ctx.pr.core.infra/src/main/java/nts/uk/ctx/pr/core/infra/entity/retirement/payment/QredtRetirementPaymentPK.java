package nts.uk.ctx.pr.core.infra.entity.retirement.payment;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QredtRetirementPaymentPK {
	@Column(name="CCD")
	public String companyCode;
	@Column(name="PID")
	public String personId;
	@Column(name="PAY_DATE")
	public GeneralDate payDate;
}
