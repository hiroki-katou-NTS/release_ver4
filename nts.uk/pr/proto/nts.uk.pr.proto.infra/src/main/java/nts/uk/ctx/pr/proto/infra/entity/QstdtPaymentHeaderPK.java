package nts.uk.ctx.pr.proto.infra.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class QstdtPaymentHeaderPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "CCD")
	public String companyCode;
	
	@Column(name = "PID")
	public String personId;
	
	@Column(name = "PROCESSING_NO")
	public int processingNo;
	
	@Column(name = "PAY_BONUS_ATR")
	public int payBonusAttribute;
	
	@Column(name = "PROCESSING_YM")
	public int processingYM;
	
	@Column(name = "SPARE_PAY_ATR")
	public int sparePayAttribute;
}
