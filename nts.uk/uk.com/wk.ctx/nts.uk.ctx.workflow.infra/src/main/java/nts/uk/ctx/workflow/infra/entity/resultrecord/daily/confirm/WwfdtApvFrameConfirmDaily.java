package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_DAY_APV_FR_CONFIRM")
public class WwfdtApvFrameConfirmDaily extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvFrameConfirmDailyPK pk;
	
	@Column(name="CID")
	private String companyID;
	
	@Column(name="EMPLOYEE_ID")
	private String employeeID;
	
	@Column(name="RECORD_DATE")
	private GeneralDate recordDate;
	
	@Column(name="APPROVER_ID")
	private String approverID;
	
	@Column(name="REPRESENTER_ID")
	private String representerID;
	
	@Column(name="APPROVAL_DATE")
	private GeneralDate approvalDate;
	
	@Override
	protected Object getKey() {
		return pk;
	}
}
