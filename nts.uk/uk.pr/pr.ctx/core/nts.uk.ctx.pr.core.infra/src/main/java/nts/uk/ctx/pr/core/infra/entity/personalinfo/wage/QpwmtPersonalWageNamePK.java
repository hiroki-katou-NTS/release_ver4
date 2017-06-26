package nts.uk.ctx.pr.core.infra.entity.personalinfo.wage;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class QpwmtPersonalWageNamePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="CCD")
	public String ccd;
	@Column(name = "CTG_ATR")
	public int ctgAtr;
	@Column(name = "P_WAGE_CD")
	public String pWageCd;
}
