package nts.uk.ctx.pr.core.infra.entity.rule.employment.allot;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class QstmtStmtAllotEmPK implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	@Basic(optional = false)
	@Column(name = "CCD")
	public String companyCode;
	
	@Basic(optional = false)
	@Column(name = "HIST_ID")
	public String histId;
	
	@Basic(optional = false)
	@Column(name = "EMPCD")
	public String employeeCd;

}
