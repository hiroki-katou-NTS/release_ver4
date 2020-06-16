package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.confirm;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_MON_APV_RT_CONFIRM")
@Builder
public class WwfdtApvRootConfirmMonthly extends UkJpaEntity {
	
	@Id
	@Column(name="ROOT_ID")
	private String rootID;
	
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
	
	public List<WwfdtApvPhaseConfirmMonthly> listWwfdtApvPhaseConfirmMonthly;

	@Override
	protected Object getKey() {
		return rootID;
	}
	
	public static WwfdtApvRootConfirmMonthly fromDomain(AppRootConfirm appRootConfirm){
		return WwfdtApvRootConfirmMonthly.builder()
				.rootID(appRootConfirm.getRootID())
				.companyID(appRootConfirm.getCompanyID())
				.employeeID(appRootConfirm.getEmployeeID())
				.yearMonth(appRootConfirm.getYearMonth().get().v())
				.closureID(appRootConfirm.getClosureID().get())
				.closureDay(appRootConfirm.getClosureDate().map(z -> z.getClosureDay().v()).get())
				.lastDayFlg(appRootConfirm.getClosureDate().map(z -> z.getLastDayOfMonth() ? 1 : 0).get())
				.listWwfdtApvPhaseConfirmMonthly(appRootConfirm.getListAppPhase()
						.stream()
						.map(p -> WwfdtApvPhaseConfirmMonthly.fromDomain(appRootConfirm, p))
								.collect(Collectors.toList()))
				.build();
	}
}
