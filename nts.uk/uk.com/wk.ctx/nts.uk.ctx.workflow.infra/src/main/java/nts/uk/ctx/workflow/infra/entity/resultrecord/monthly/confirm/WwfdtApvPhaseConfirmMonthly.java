package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.confirm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm.WwfdtApvPhaseConfirmDaily;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_MON_APV_PH_CONFIRM")
@Builder
@Getter
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
	
	public static List<WwfdtApvPhaseConfirmMonthly> fromDomain(AppRootConfirm root) {
		List<WwfdtApvPhaseConfirmMonthly> phase = new ArrayList<>();
		root.getListAppPhase().forEach(p -> {
			
			phase.add(fromDomain(root, p));
		});
		return phase;
	}

	public static WwfdtApvPhaseConfirmMonthly fromDomain(AppRootConfirm appRootConfirm, AppPhaseConfirm appPhaseConfirm){
		return WwfdtApvPhaseConfirmMonthly.builder()
				.pk(new WwfdpApvPhaseConfirmMonthlyPK(appRootConfirm.getRootID(), appPhaseConfirm.getPhaseOrder()))
				.companyID(appRootConfirm.getCompanyID())
				.employeeID(appRootConfirm.getEmployeeID()) 
				.yearMonth(appRootConfirm.getYearMonth().get().v())
				.closureID(appRootConfirm.getClosureID().get())
				.closureDay(appRootConfirm.getClosureDate().map(z -> z.getClosureDay().v()).get())
				.lastDayFlg(appRootConfirm.getClosureDate().map(z -> z.getLastDayOfMonth() ? 1 : 0).get())
				.appPhaseAtr(appPhaseConfirm.getAppPhaseAtr().value)
				.listWwfdtApvFrameConfirmMonthly(appPhaseConfirm.getListAppFrame()
						.stream()
						.map(f -> WwfdtApvFrameConfirmMonthly.fromDomain(appRootConfirm, appPhaseConfirm, f))
								.collect(Collectors.toList()))
				.build();
	}
}
