package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "WWFDT_APV_DAY_RT_INSTANCE")

public class WwfdtApvRootInstanceDaily extends UkJpaEntity {
	@Id
	@Column(name = "ROOT_ID")
	private String rootID;

	@Column(name = "CID")
	private String companyID;

	@Column(name = "EMPLOYEE_ID")
	private String employeeID;

	@Column(name = "START_DATE")
	private GeneralDate startDate;

	@Column(name = "END_DATE")
	private GeneralDate endDate;

	public List<WwfdtApvPhaseInstanceDaily> listWwfdtApvPhaseInstanceDaily;

	@Override
	protected Object getKey() {
		return rootID;
	}
}