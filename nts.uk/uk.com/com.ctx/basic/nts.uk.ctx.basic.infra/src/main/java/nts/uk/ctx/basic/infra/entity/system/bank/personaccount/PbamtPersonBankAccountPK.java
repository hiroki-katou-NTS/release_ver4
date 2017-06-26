package nts.uk.ctx.basic.infra.entity.system.bank.personaccount;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class PbamtPersonBankAccountPK implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "CCD")
	public String companyCode;
	
	@Column(name = "PID")
	public String personId;
	
	@Column(name = "HIST_ID")
	public String historyId;
	
	
}
