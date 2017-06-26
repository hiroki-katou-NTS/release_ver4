package nts.uk.ctx.pr.core.infra.entity.paymentdata;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
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

	public QstdtPaymentDetailPK(String companyCode, String personId, int processingNo, int payBonusAttribute,
			int processingYM, int sparePayAttribute, int categoryATR, String itemCode) {
		super();
		this.companyCode = companyCode;
		this.personId = personId;
		this.processingNo = processingNo;
		this.payBonusAttribute = payBonusAttribute;
		this.processingYM = processingYM;
		this.sparePayAttribute = sparePayAttribute;
		this.categoryATR = categoryATR;
		this.itemCode = itemCode;
	}
}
