package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.approverstatemanagement.ApprovalRootState;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="WWFDT_APP_APV_RT_STATE")
@Builder
public class WwfdtAppApvRootState extends UkJpaEntity {

	@EmbeddedId
	public WwfdpAppApvRootStatePK wwfdpAppApvRootStatePK;
	
	@Column(name="CID")
	public String companyID;
	
	@Column(name="EMPLOYEE_ID")
	public String employeeID;
	
	@Column(name="APP_DATE")
	public GeneralDate appDate;
	
	public List<WwfdtAppApvPhaseState> listWwfdtAppApvPhaseState;

	@Override
	protected Object getKey() {
		return wwfdpAppApvRootStatePK; 
	}
	
	public static WwfdtAppApvRootState fromDomain(ApprovalRootState approvalRootState){
		return WwfdtAppApvRootState.builder()
				.wwfdpAppApvRootStatePK(new WwfdpAppApvRootStatePK(approvalRootState.getRootStateID()))
				.companyID(approvalRootState.getCompanyID())
				.employeeID(approvalRootState.getEmployeeID())
				.appDate(approvalRootState.getApprovalRecordDate())
				.listWwfdtAppApvPhaseState(approvalRootState.getListApprovalPhaseState()
						.stream()
						.map(p -> WwfdtAppApvPhaseState.fromDomain(approvalRootState.getCompanyID(), approvalRootState.getEmployeeID(), approvalRootState.getApprovalRecordDate(), p))
								.collect(Collectors.toList()))
				.build();
	}

}