package nts.uk.ctx.pr.core.infra.entity.personalinfo.wage;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
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
	public int strYm;

	public PprmtPersonWagePK(String ccd, String pId, int ctgAtr, String pWageCd, int strYm) {
		super();
		this.ccd = ccd;
		this.pId = pId;
		this.ctgAtr = ctgAtr;
		this.pWageCd = pWageCd;
		this.strYm = strYm;
	}
	
	

}
