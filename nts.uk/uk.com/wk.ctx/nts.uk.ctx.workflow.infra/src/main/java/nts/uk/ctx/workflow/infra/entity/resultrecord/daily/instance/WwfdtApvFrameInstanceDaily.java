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
@Table(name = "WWFDT_APP_DAY_FR_INSTANCE")
public class WwfdtApvFrameInstanceDaily extends UkJpaEntity {

	@EmbeddedId
	private WwfdpApvFrameInstanceDailyPK pk;

	@Column(name = "CID")
	private String companyID;

	@Column(name = "EMPLOYEE_ID")
	private String employeeID;

	@Column(name = "START_DATE")
	private GeneralDate startDate;

	@Column(name = "CONFIRM_ATR")
	private Integer confirmAtr;

	public List<WwfdtApvApproveInstanceDaily> listWwfdtApvApproveInstanceDaily;	

	@Override
	protected Object getKey() {
		return pk;
	}
}
