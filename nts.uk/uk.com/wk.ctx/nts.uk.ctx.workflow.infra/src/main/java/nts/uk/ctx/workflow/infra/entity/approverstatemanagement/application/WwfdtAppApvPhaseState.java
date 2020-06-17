package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalPhaseState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="WWFDT_APP_APV_PH_STATE")
@Builder
public class WwfdtAppApvPhaseState extends UkJpaEntity {
	
	@EmbeddedId
	public WwfdpAppApvPhaseStatePK wwfdpAppApvPhaseStatePK;
	
	@Column(name="CID")
	public String companyID;
	
	@Column(name="EMPLOYEE_ID")
	public String employeeID;
	
	@Column(name="APP_DATE")
	public GeneralDate appDate;
	
	@Column(name="APP_PHASE_ATR")
	public Integer approvalAtr;
	
	@Column(name="APPROVAL_FORM")
	public Integer approvalForm;
	
	@Transient
	public List<WwfdtAppApvFrameState> listWwfdtAppApvFrameState;

	@Override
	protected Object getKey() {
		return wwfdpAppApvPhaseStatePK;
	}
	
	public static List<WwfdtAppApvPhaseState> fromDomain(ApprovalRootState root) {
		List<WwfdtAppApvPhaseState> phase = new ArrayList<>();
		root.getListApprovalPhaseState().forEach(e -> {
			
			phase.add(fromDomain(root.getCompanyID(), root.getEmployeeID() ,root.getApprovalRecordDate(), e));
		});
		return phase;
	}
	
	public static WwfdtAppApvPhaseState fromDomain(String companyID, String employeeID, GeneralDate appDate, ApprovalPhaseState approvalPhaseState){
		return WwfdtAppApvPhaseState.builder()
				.wwfdpAppApvPhaseStatePK(new WwfdpAppApvPhaseStatePK(
						approvalPhaseState.getRootStateID(), 
						approvalPhaseState.getPhaseOrder()))
				.companyID(companyID)
				.employeeID(employeeID)
				.appDate(appDate)
				.approvalAtr(approvalPhaseState.getApprovalAtr().value)
				.approvalForm(approvalPhaseState.getApprovalForm().value)
				.listWwfdtAppApvFrameState(approvalPhaseState.getListApprovalFrame()
						.stream()
						.map(f -> WwfdtAppApvFrameState.fromDomain(companyID, employeeID, appDate, f))
								.collect(Collectors.toList()))
				.build();
	}
	
}
