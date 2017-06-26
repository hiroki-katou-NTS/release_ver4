package nts.uk.ctx.pr.core.infra.entity.personalinfo.commute;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class PprmtPersonCommutePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "CCD")
	public String ccd;
	
	@Column(name = "PID")
	public String pId;
	
	@Column(name = "STR_YM")
	public int strYm;

	public PprmtPersonCommutePK(String ccd, String pId, int strYm) {
		super();
		this.ccd = ccd;
		this.pId = pId;
		this.strYm = strYm;
	}

}
