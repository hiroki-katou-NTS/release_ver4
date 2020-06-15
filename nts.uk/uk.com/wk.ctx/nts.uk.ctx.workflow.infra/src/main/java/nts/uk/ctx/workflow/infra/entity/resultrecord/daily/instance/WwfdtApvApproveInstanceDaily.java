package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "WWFDT_DAY_APV_AP_INSTANCE")
@Getter
public class WwfdtApvApproveInstanceDaily extends UkJpaEntity {

	@EmbeddedId
	private WwfdpApvApproveInstanceDailyPK pk;

	@Column(name = "CID")
	private String companyID;

	@Column(name = "EMPLOYEE_ID")
	private String employeeID;

	@Column(name = "START_DATE")
	private GeneralDate startDate;

	@Override
	protected Object getKey() {
		return pk;
	}
	
	public static WwfdtApvApproveInstanceDaily fromDomain(String rootID, Integer phaseOrder,
			Integer frameOrder, String approverChildID,
			String companyID, String employeeID, GeneralDate startDate) {
		return new WwfdtApvApproveInstanceDaily(
				new WwfdpApvApproveInstanceDailyPK(rootID, phaseOrder, frameOrder, approverChildID), 
				companyID, 
				employeeID, 
				startDate);
	}
}
