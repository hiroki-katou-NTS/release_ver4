package nts.uk.ctx.pr.proto.infra.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class QstdtPaymentDetailPK {
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
	
	@Column(name = "CTG_ATR")
	public int categoryATR;
	
	@Column(name = "ITEM_CD")
	public String itemCode;
	
}
