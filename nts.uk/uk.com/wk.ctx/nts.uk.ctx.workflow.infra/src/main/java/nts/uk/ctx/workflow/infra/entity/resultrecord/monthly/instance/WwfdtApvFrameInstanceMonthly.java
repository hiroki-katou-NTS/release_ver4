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
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_MON_APV_FR_INSTANCE")
@Getter
public class WwfdtApvFrameInstanceMonthly extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvFrameInstanceMonthlyPK pk;
	
	@Column(name="CID")
	private String companyID;
	
	@Column(name="EMPLOYEE_ID")
	private String employeeID;
	
	@Column(name="START_DATE")
	private GeneralDate startDate;
	
	@Column(name="CONFIRM_ATR")
	private Integer confirmAtr;
	
	public List<WwfdtApvApproveInstanceMonthly> listWwfdtApvApproveInstanceMonthly;
	
	@Override
	protected Object getKey() {
		return pk;
	}
	
	public static List<WwfdtApvFrameInstanceMonthly> fromDomain(AppRootInstance root) {
		List<WwfdtApvFrameInstanceMonthly> frame = new ArrayList<>();
		root.getListAppPhase().forEach(p -> {
			p.getListAppFrame().forEach(f -> {
				frame.add(fromDomain(root.getRootID(), p.getPhaseOrder(), root.getCompanyID(), root.getEmployeeID(), root.getDatePeriod().start(), f));
			});
		});
		return frame;
	}
	
	public static WwfdtApvFrameInstanceMonthly fromDomain(String rootID, int phaseOrder, String companyID, String employeeID, GeneralDate startDate, AppFrameInstance instance) {
		return new WwfdtApvFrameInstanceMonthly(
				new WwfdpApvFrameInstanceMonthlyPK(rootID, phaseOrder, instance.getFrameOrder()), 
				companyID, 
				employeeID, 
				startDate, 
				instance.isConfirmAtr() ? 1 : 0,
				instance.getListApprover().stream().map(t ->
						WwfdtApvApproveInstanceMonthly.fromDomain(
								rootID, 
								phaseOrder, 
								instance.getFrameOrder(), 
								companyID, 
								employeeID, 
								startDate,
								t)
					)
				.collect(Collectors.toList())
				);
	}
	
}
