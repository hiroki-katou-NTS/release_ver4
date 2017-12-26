package nts.uk.ctx.workflow.infra.entity.approverstatemanagement;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="WWFDT_APPROVAL_PHASE_ST")
@Builder
public class WwfdtApprovalPhaseState extends UkJpaEntity {
	
	@EmbeddedId
	public WwfdpApprovalPhaseStatePK wwfdpApprovalPhaseStatePK;
	
	@Column(name="APPROVAL_ATR")
	public Integer approvalAtr;
	
	@Column(name="APPROVAL_FORM")
	public Integer approvalForm;
	
	@ManyToOne
	@PrimaryKeyJoinColumns({
		@PrimaryKeyJoinColumn(name="ROOT_STATE_ID",referencedColumnName="ROOT_STATE_ID")
	})
	private WwfdtApprovalRootState wwfdtApprovalRootState;
	
	@OneToMany(targetEntity=WwfdtApprovalFrame.class, cascade = CascadeType.ALL, mappedBy = "wwfdtApprovalPhaseState", orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinTable(name = "WWFDT_APPROVAL_FRAME")
	public List<WwfdtApprovalFrame> listWwfdtApprovalFrame;

	@Override
	protected Object getKey() {
		return wwfdpApprovalPhaseStatePK;
	}
	
	public static WwfdtApprovalPhaseState fromDomain(ApprovalPhaseState approvalPhaseState){
		return WwfdtApprovalPhaseState.builder()
				.wwfdpApprovalPhaseStatePK(
						new WwfdpApprovalPhaseStatePK(
								approvalPhaseState.getRootStateID(), 
								approvalPhaseState.getPhaseOrder()))
				.approvalAtr(approvalPhaseState.getApprovalAtr().value)
				.approvalForm(approvalPhaseState.getApprovalForm().value)
				.listWwfdtApprovalFrame(
						approvalPhaseState.getListApprovalFrame().stream()
						.map(x -> WwfdtApprovalFrame.fromDomain(x)).collect(Collectors.toList()))
				.build();
	}
	
}
