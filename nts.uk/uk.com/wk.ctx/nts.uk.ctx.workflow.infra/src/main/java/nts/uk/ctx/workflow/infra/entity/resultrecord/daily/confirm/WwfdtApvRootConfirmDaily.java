package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_DAY_APV_RT_CONFIRM")
public class WwfdtApvRootConfirmDaily extends UkJpaEntity {
	@Id
	@Column(name="ROOT_ID")
	private String rootID;
	
	@Column(name="CID")
	private String companyID;
	
	@Column(name="EMPLOYEE_ID")
	private String employeeID;
	
	@Column(name="RECORD_DATE")
	private GeneralDate recordDate;
	
	public List<WwfdtApvPhaseConfirmDaily> listWwfdtApvDayPhConfirm;

	@Override
	protected Object getKey() {
		return rootID;
	}
}
