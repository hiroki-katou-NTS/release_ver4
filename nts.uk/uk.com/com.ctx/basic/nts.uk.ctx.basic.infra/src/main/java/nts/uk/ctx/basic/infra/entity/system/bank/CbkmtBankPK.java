package nts.uk.ctx.basic.infra.entity.system.bank;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.basic.dom.company.CompanyCode;

/**
 * 
 * @author sonnh
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class CbkmtBankPK implements Serializable{
	
	static final long serialVersionUID = 1L;
	 
	@Column(name="CCD")
	public String companyCode;

	@Column(name="BANK_CD")
	public String bankCode;
}
