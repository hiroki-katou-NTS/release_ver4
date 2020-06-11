package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="WWFDT_APPROVAL_ROOT_STATE")
@Builder
public class WwfdtApprovalRootState extends UkJpaEntity {
	
	@EmbeddedId
	public WwfdpApprovalRootStatePK wwfdpApprovalRootStatePK;
	
	@Column(name="HIST_ID")
	public String historyID;
	
	@Column(name="EMPLOYEE_ID")
	public String employeeID;
	
	@Column(name="APPROVAL_RECORD_DATE")
	public GeneralDate recordDate;
	
	@OneToMany(targetEntity=WwfdtApprovalPhaseState.class, cascade = CascadeType.ALL, mappedBy = "wwfdtApprovalRootState", orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinTable(name = "WWFDT_APPROVAL_PHASE_ST")
	public List<WwfdtApprovalPhaseState> listWwfdtApprovalPhaseState;

	@Override
	protected Object getKey() {
		return wwfdpApprovalRootStatePK; 
	}
	
	public static WwfdtApprovalRootState fromDomain(String companyID, ApprovalRootState approvalRootState){
		return WwfdtApprovalRootState.builder()
				.wwfdpApprovalRootStatePK(new WwfdpApprovalRootStatePK(approvalRootState.getRootStateID()))
				.employeeID(approvalRootState.getEmployeeID())
				.recordDate(approvalRootState.getApprovalRecordDate())
				.listWwfdtApprovalPhaseState(
						approvalRootState.getListApprovalPhaseState().stream()
						.map(x -> WwfdtApprovalPhaseState.fromDomain(companyID, approvalRootState.getApprovalRecordDate(), x))
						.collect(Collectors.toList()))
				.build();
	}
	
	public ApprovalRootState toDomain(){
		return ApprovalRootState.builder()
				.rootStateID(this.wwfdpApprovalRootStatePK.rootStateID)
				.approvalRecordDate(this.recordDate)
				.employeeID(this.employeeID)
				.listApprovalPhaseState(this.listWwfdtApprovalPhaseState.stream()
											.map(x -> x.toDomain()).collect(Collectors.toList()))
				.build();
	}
}
