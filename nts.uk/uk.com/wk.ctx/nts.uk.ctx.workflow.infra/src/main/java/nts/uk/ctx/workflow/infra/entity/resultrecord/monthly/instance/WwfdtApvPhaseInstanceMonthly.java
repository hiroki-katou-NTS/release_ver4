package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_MON_APV_PH_INSTANCE")
@Getter
public class WwfdtApvPhaseInstanceMonthly extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvPhaseInstanceMonthlyPK pk;
	
	@Column(name="CID")
	private String companyID;
	
	@Column(name="EMPLOYEE_ID")
	private String employeeID;
	
	@Column(name="START_DATE")
	private GeneralDate startDate;
	
	@Column(name="APPROVAL_FORM")
	private Integer approvalForm;
	
	public List<WwfdtApvFrameInstanceMonthly> listWwfdtApvFrameInstanceMonthly;

	@Override
	protected Object getKey() {
		return pk;
	}
	
	public static List<WwfdtApvPhaseInstanceMonthly> fromDomain(AppRootInstance root) {
		List<WwfdtApvPhaseInstanceMonthly> phase = new ArrayList<>();
		root.getListAppPhase().forEach(p -> {
			
			phase.add(fromDomain(root.getRootID(), root.getCompanyID(), root.getEmployeeID(), root.getDatePeriod().start(), p));
		});
		return phase;
	}
	
	public static WwfdtApvPhaseInstanceMonthly fromDomain(String rootID, String companyID, String employeeID, GeneralDate startDate, AppPhaseInstance instance) {
		return new WwfdtApvPhaseInstanceMonthly(
				new WwfdpApvPhaseInstanceMonthlyPK(rootID, instance.getPhaseOrder()), 
				companyID, 
				employeeID, 
				startDate, 
				instance.getPhaseOrder(), 
				instance.getListAppFrame()
					.stream()
					.map(t -> WwfdtApvFrameInstanceMonthly.fromDomain(rootID, instance.getPhaseOrder(), companyID, employeeID, startDate, t))
					.collect(Collectors.toList()));
	}
}
