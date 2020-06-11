package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.confirm;

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
@Table(name="WWFDT_APV_MON_FR_CONFIRM")
public class WwfdtApvFrameConfirmMonthly extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvFrameConfirmMonthlyPK pk;
	
	@Column(name="CID")
	private String companyID;
	
	@Column(name="EMPLOYEE_ID")
	private String employeeID;
	
	@Column(name="YEARMONTH")
	private Integer yearMonth;
	
	@Column(name="CLOSURE_ID")
	private Integer closureID;
	
	@Column(name="CLOSURE_DAY")
	private Integer closureDay;
	
	@Column(name="LAST_DAY_FLG")
	private Integer lastDayFlg;
	
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
