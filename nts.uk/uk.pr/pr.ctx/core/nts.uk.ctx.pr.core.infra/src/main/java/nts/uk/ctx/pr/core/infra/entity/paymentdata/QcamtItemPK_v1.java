package nts.uk.ctx.pr.core.infra.entity.paymentdata;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class QcamtItemPK_v1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "CCD")
	public String ccd;
	
	@Column(name = "ITEM_CD")
	public String itemCd;
	
	@Column(name = "CTG_ATR")
	public int ctgAtr;
}
