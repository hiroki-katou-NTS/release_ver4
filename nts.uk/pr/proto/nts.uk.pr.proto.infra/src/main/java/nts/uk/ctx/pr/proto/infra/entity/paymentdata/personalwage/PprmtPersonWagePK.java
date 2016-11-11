package nts.uk.ctx.pr.proto.infra.entity.paymentdata.personalwage;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PprmtPersonWagePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4950620595442435692L;
	
	@Column(name = "CCD")
	public String ccd;
	
	@Column(name = "PID")
	public String pId;
	
	@Column(name = "CTG_ATR")
	public int ctgAtr;
	
	@Column(name = "P_WAGE_CD")
	public String pWageCd;
	
	@Column(name = "STR_YM")
	public BigDecimal strYm;

}
