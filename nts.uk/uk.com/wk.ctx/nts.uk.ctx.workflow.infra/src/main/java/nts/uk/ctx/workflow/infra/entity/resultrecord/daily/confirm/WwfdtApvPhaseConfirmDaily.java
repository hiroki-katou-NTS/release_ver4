package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm;

import java.util.List;
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
@Table(name="WWFDT_APV_DAY_PH_CONFIRM")
public class WwfdtApvPhaseConfirmDaily extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvPhaseConfirmDailyPK pk;
	
	@Column(name="CID")
	private String companyID;
	
	@Column(name="EMPLOYEE_ID")
	private String employeeID;
	
	@Column(name="RECORD_DATE")
	private GeneralDate recordDate;
	
	@Column(name="APP_PHASE_ATR")
	private Integer appPhaseAtr;
	
	public List<WwfdtApvFrameConfirmDaily> listWwfdtApvDayFrConfirm;

	@Override
	protected Object getKey() {
		return pk;
	}
}
