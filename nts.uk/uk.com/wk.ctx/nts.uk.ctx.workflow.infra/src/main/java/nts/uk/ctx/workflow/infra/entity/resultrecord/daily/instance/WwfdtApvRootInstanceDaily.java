package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "WWFDT_DAY_APV_RT_INSTANCE")
@Getter
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

	
	@Transient
	public List<WwfdtApvPhaseInstanceDaily> listWwfdtApvPhaseInstanceDaily;

	@Override
	protected Object getKey() {
		return rootID;
	}
	
	public static WwfdtApvRootInstanceDaily fromDomain(AppRootInstance appRootInstance) {
		return new WwfdtApvRootInstanceDaily(
				appRootInstance.getRootID(), 
				appRootInstance.getCompanyID(), 
				appRootInstance.getEmployeeID(), 
				appRootInstance.getDatePeriod().start(), 
				appRootInstance.getDatePeriod().end(), 
				appRootInstance.getListAppPhase()
					.stream()
					.map(t -> WwfdtApvPhaseInstanceDaily.fromDomain(appRootInstance.getRootID(),
																	appRootInstance.getCompanyID(),
																	appRootInstance.getEmployeeID(),
																	appRootInstance.getDatePeriod().start(),
																	t))
					.collect(Collectors.toList())
				);
				
	}
}