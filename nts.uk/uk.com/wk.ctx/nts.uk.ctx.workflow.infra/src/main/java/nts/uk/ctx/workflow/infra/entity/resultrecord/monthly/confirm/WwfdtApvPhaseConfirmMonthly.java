package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.confirm;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_MON_APV_PH_CONFIRM")
public class WwfdtApvPhaseConfirmMonthly extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvPhaseConfirmMonthlyPK pk;
	
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
	
	@Column(name="APP_PHASE_ATR")
	private Integer appPhaseAtr;
	
	public List<WwfdtApvFrameConfirmMonthly> listWwfdtApvFrameConfirmMonthly;

	@Override
	protected Object getKey() {
		return pk;
	}
	
}
