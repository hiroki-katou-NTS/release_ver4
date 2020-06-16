package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_DAY_APV_PH_CONFIRM")
@Builder
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
	
	public List<WwfdtApvFrameConfirmDaily> listWwfdtApvFrameConfirmDaily;

	@Override
	protected Object getKey() {
		return pk;
	}
	
	public static List<WwfdtApvPhaseConfirmDaily> fromDomain(AppRootConfirm root) {
		List<WwfdtApvPhaseConfirmDaily> phase = new ArrayList<>();
		root.getListAppPhase().forEach(p -> {
			
			phase.add(fromDomain(root, p));
		});
		return phase;
	}

	public static WwfdtApvPhaseConfirmDaily fromDomain(AppRootConfirm appRootConfirm, AppPhaseConfirm appPhaseConfirm){
		return WwfdtApvPhaseConfirmDaily.builder()
				.pk(new WwfdpApvPhaseConfirmDailyPK(appRootConfirm.getRootID(), appPhaseConfirm.getPhaseOrder()))
				.companyID(appRootConfirm.getCompanyID())
				.employeeID(appRootConfirm.getEmployeeID()) 
				.recordDate(appRootConfirm.getRecordDate())
				.appPhaseAtr(appPhaseConfirm.getAppPhaseAtr().value)
				.listWwfdtApvFrameConfirmDaily(appPhaseConfirm.getListAppFrame()
						.stream()
						.map(f -> WwfdtApvFrameConfirmDaily.fromDomain(appRootConfirm, appPhaseConfirm, f))
								.collect(Collectors.toList()))
				.build();
	}
}
