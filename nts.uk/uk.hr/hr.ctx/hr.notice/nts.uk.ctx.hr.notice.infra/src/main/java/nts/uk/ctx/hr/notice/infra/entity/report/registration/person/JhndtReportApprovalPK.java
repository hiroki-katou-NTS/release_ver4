package nts.uk.ctx.hr.notice.infra.entity.report.registration.person;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class JhndtReportApprovalPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "RPTID")
	public int reportID; // 届出ID
	
	@NotNull
	@Column(name = "PHASE_NUM")
	public int phaseNum; // フェーズ通番

	@NotNull
	@Column(name = "APR_NUM")
	public int aprNum;//承認者通番

	@NotNull
	@Column(name = "CID")
	public String cid; // 会社ID
}
