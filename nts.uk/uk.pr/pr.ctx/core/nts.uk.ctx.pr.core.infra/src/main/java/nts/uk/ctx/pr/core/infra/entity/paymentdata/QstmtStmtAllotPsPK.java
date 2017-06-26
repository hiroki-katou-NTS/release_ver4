package nts.uk.ctx.pr.core.infra.entity.paymentdata;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Embeddable
public class QstmtStmtAllotPsPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "CCD")
	public String companyCode;
	
	@Column(name = "PID")
	public String personId;
	
	@Column(name = "HIST_ID")
	public int histId;
	
	public QstmtStmtAllotPsPK() {}
}
