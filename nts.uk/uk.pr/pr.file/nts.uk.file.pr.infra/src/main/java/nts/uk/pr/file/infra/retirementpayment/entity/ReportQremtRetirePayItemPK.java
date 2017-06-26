/**
 * 
 */
package nts.uk.pr.file.infra.retirementpayment.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

/**
 * @author hungnm
 *
 */
@Data
@Embeddable
public class ReportQremtRetirePayItemPK implements Serializable {
	/** The Constant serialVersionUID. */
	public static final long serialVersionUID = 1L;

	/** The company code. */
	@Column(name = "CCD")
	public String ccd;

	/** The person id. */
	@Column(name = "CTG_ATR")
	public BigDecimal categoryAtr;

	@Column(name = "ITEM_CD")
	public String itemCode;
}
