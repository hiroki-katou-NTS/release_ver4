package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_MON_APV_RT_INSTANCE")
@Getter
public class WwfdtApvRootInstanceMonthly extends UkJpaEntity {
	
	@Id
	@Column(name="ROOT_ID")
	private String rootID;
	
	@Column(name="CID")
	private String companyID;
	
	@Column(name="EMPLOYEE_ID")
	private String employeeID;
	
	@Column(name="START_DATE")
	private GeneralDate startDate;
	
	@Column(name="END_DATE")
	private GeneralDate endDate;
	
	public List<WwfdtApvPhaseInstanceMonthly> listWwfdtApvPhaseInstanceMonthly;

	@Override
	protected Object getKey() {
		return rootID;
	}
	
	public static WwfdtApvRootInstanceMonthly fromDomain(AppRootInstance instance) {
		return new WwfdtApvRootInstanceMonthly(
				instance.getRootID(), 
				instance.getCompanyID(), 
				instance.getEmployeeID(), 
				instance.getDatePeriod().start(), 
				instance.getDatePeriod().end(), 
				instance.getListAppPhase()
					.stream()
					.map(t -> WwfdtApvPhaseInstanceMonthly.fromDomain(instance.getRootID(), instance.getCompanyID(), instance.getEmployeeID(), instance.getDatePeriod().start(),t))
					.collect(Collectors.toList())
				);
	}
	
}