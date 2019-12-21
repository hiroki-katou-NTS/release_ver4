package nts.uk.ctx.workflow.infra.entity.approvermanagement.workroot;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
/**
 * 承認フェーズ
 * @author hoatt
 *
 */
@Entity
@Table(name = "WWFMT_APPROVAL_PHASE")
@AllArgsConstructor
@NoArgsConstructor
public class WwfmtApprovalPhase extends UkJpaEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	/**主キー*/
	@EmbeddedId
	public WwfmtApprovalPhasePK wwfmtApprovalPhasePK;
	
	/**承認形態*/
	@Column(name = "APPROVAL_FORM")
	public int approvalForm;
	/**閲覧フェーズ*/
	@Column(name = "BROWSING_PHASE")
	public int browsingPhase;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="wwfmtApprovalPhase", orphanRemoval = true)
	public List<WwfmtAppover> wwfmtAppovers;
	
	@Override
	protected Object getKey() {
		return wwfmtApprovalPhasePK;
	}
}
