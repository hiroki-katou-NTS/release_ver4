package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApproverState;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="WWFDT_APP_APV_AP_STATE")
@Builder
public class WwfdtAppApvApproverState extends UkJpaEntity {
	
	@EmbeddedId
	public WwfdpAppApvApproverStatePK wwfdpAppApvApproverStatePK;
	
	@Column(name="CID")
	public String companyID;
	
	@Column(name="EMPLOYEE_ID")
	public String employeeID;
	
	@Column(name="APP_DATE")
	public GeneralDate appDate;
	
	@Override
	protected Object getKey() {
		return wwfdpAppApvApproverStatePK;
	}
	
	public static List<WwfdtAppApvApproverState> fromDomain(ApprovalRootState root) {
		List<WwfdtAppApvApproverState> approver = new ArrayList<>();
		root.getListApprovalPhaseState().forEach(p -> {
			p.getListApprovalFrame().forEach(f -> {
				f.getListApproverState().forEach(a -> {
					approver.add(fromDomain(root.getCompanyID(), root.getEmployeeID() ,root.getApprovalRecordDate(), a));
				});
			});
		});
		return approver;
	}

	
	public static WwfdtAppApvApproverState fromDomain(String companyID, String employeeID, GeneralDate appDate, ApproverState approverState){
		return WwfdtAppApvApproverState.builder()
				.wwfdpAppApvApproverStatePK(
						new WwfdpAppApvApproverStatePK(
								approverState.getRootStateID(), 
								approverState.getPhaseOrder(), 
								approverState.getFrameOrder(), 
								approverState.getApproverID()))
				.companyID(companyID)
				.employeeID(employeeID)
				.appDate(appDate)
				.build();
	}

}