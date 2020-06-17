package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_MON_APV_AP_INSTANCE")
@Getter
public class WwfdtApvApproveInstanceMonthly extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvApproveInstanceMonthlyPK pk;
	
	@Column(name="CID")
	private String companyID;
	
	@Column(name="EMPLOYEE_ID")
	private String employeeID;
	
	@Column(name="START_DATE")
	private GeneralDate startDate;
	
	@Override
	protected Object getKey() {
		return pk;
	}
	
	public static List<WwfdtApvApproveInstanceMonthly> fromDomain(AppRootInstance root) {
		List<WwfdtApvApproveInstanceMonthly> approver = new ArrayList<>();
		root.getListAppPhase().forEach(p -> {
			p.getListAppFrame().forEach(f -> {
				f.getListApprover().forEach(a -> {
					approver.add(fromDomain(root.getRootID(), p.getPhaseOrder(), f.getFrameOrder(), root.getCompanyID(), root.getEmployeeID(), root.getDatePeriod().start(), a));
				});
			});
		});
		return approver;
	}
	
	public static WwfdtApvApproveInstanceMonthly fromDomain(
			String rootID, Integer phaseOrder, Integer frameOrder, String companyID, String employeeID, GeneralDate startDate, String approverChildID) {
		return new WwfdtApvApproveInstanceMonthly(
				new WwfdpApvApproveInstanceMonthlyPK(rootID, phaseOrder, frameOrder, approverChildID),
				companyID, 
				employeeID, 
				startDate
			);
	}
	
}
