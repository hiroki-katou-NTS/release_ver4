package nts.uk.ctx.basic.infra.entity.organization.position;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Embeddable
public class CmnmtJobTitlePK implements Serializable{

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)	
	@Column(name = "CCD")
	public String companyCode;
	
	@Basic(optional = false)
	@Column(name = "HIST_ID")
	public String historyId;
	
	@Basic(optional = false)
	@Column(name = "JOBCD")
	public String jobCode;

	public CmnmtJobTitlePK(String companyCode, String historyId, String jobCode) {
		super();
		this.companyCode = companyCode;
		this.historyId = historyId;
		this.jobCode = jobCode;
	}

	public CmnmtJobTitlePK() {
		super();
	}

}