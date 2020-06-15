package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_DAY_APV_RT_CONFIRM")
@Builder
@Getter
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
	
	public List<WwfdtApvPhaseConfirmDaily> listWwfdtApvPhaseConfirmDaily;

	@Override
	protected Object getKey() {
		return rootID;
	}
	
	public static WwfdtApvRootConfirmDaily fromDomain(AppRootConfirm appRootConfirm){
		return WwfdtApvRootConfirmDaily.builder()
				.rootID(appRootConfirm.getRootID())
				.companyID(appRootConfirm.getCompanyID())
				.employeeID(appRootConfirm.getEmployeeID())
				.recordDate(appRootConfirm.getRecordDate())
				.listWwfdtApvPhaseConfirmDaily(appRootConfirm.getListAppPhase()
						.stream()
						.map(p -> WwfdtApvPhaseConfirmDaily.fromDomain(appRootConfirm, p))
								.collect(Collectors.toList()))
				.build();
	}
}
