package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "WWFDT_DAY_APV_FR_INSTANCE")
@Getter
public class WwfdtApvFrameInstanceDaily extends UkJpaEntity {

	@EmbeddedId
	private WwfdpApvFrameInstanceDailyPK pk;

	@Column(name = "CID")
	private String companyID;

	@Column(name = "EMPLOYEE_ID")
	private String employeeID;

	@Column(name = "START_DATE")
	private GeneralDate startDate;

	@Column(name = "CONFIRM_ATR")
	private Integer confirmAtr;

	@Transient
	public List<WwfdtApvApproveInstanceDaily> listWwfdtApvApproveInstanceDaily;	

	@Override
	protected Object getKey() {
		return pk;
	}
	
	public static List<WwfdtApvFrameInstanceDaily> fromDomain(AppRootInstance root) {
		List<WwfdtApvFrameInstanceDaily> frame = new ArrayList<>();
		root.getListAppPhase().forEach(p -> {
			p.getListAppFrame().forEach(f -> {
				frame.add(fromDomain(root.getRootID(), p.getPhaseOrder(), root.getCompanyID(), root.getEmployeeID(), root.getDatePeriod().start(), f));
			});
		});
		return frame;
	}

	public static WwfdtApvFrameInstanceDaily fromDomain(String rootID, int phaseOrder, String companyID, String employeeID, GeneralDate startDate,
			AppFrameInstance instance) {
		return new WwfdtApvFrameInstanceDaily(
				new WwfdpApvFrameInstanceDailyPK(rootID, phaseOrder, instance.getFrameOrder()), 
				companyID, 
				employeeID, 
				startDate, 
				instance.isConfirmAtr()?1:0, 
				instance.getListApprover()
					.stream()
					.map(t -> WwfdtApvApproveInstanceDaily.fromDomain(
							rootID,
							phaseOrder,
							instance.getFrameOrder(),
							t,
							companyID,
							employeeID,
							startDate))
					.collect(Collectors.toList()));
	}
}