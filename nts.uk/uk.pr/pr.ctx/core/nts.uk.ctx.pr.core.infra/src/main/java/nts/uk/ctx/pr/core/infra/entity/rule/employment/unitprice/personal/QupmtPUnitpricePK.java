package nts.uk.ctx.pr.core.infra.entity.rule.employment.unitprice.personal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author sonnh
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class QupmtPUnitpricePK implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	@Column(name="CCD")
	public String companyCode;
	
	@Column(name="P_UNITPRICE_CD")
	public String personalUnitPriceCode;
}
