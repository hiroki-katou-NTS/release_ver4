package nts.uk.ctx.pr.core.infra.entity.retirement.payitem;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
/**
 * 
 * @author Doan Duy Hung
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QremtRetirePayItemPK {
	@Column(name="CCD")
	public String companyCode;
	
	@Column(name="CTG_ATR")
	public int category;
	
	@Column(name="ITEM_CD")
	public String itemCode;
}
