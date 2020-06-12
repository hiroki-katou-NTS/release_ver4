package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_DAY_APV_PH_INSTANCE")
public class WwfdtApvPhaseInstanceDaily extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvPhaseInstanceDailyPK pk;
	
	@Column(name="CID")
	private String companyID;
	
	@Column(name="EMPLOYEE_ID")
	private String employeeID;
	
	@Column(name="START_DATE")
	private GeneralDate startDate;
	
	@Column(name="APPROVAL_FORM")
	private Integer approvalForm;
	
	public List<WwfdtApvFrameInstanceDaily> listWwfdtApvFrameInstanceDaily;

	@Override
	protected Object getKey() {
		return pk;
	}
}
