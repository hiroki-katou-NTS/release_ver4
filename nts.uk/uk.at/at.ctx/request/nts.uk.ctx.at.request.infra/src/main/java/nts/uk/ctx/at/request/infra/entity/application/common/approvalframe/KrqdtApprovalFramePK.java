package nts.uk.ctx.at.request.infra.entity.application.common.approvalframe;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 
 * @author hieult
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class KrqdtApprovalFramePK implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "CID")
	public String companyID;

	@NotNull
	@Column(name = "PHASE_ID")
	public String phaseID;

	@NotNull
	@Column(name = "DISPORDER")
	public int dispOrder;

	@NotNull
	@Column(name = "APPROVER_SID")
	public String approverSID;
}
