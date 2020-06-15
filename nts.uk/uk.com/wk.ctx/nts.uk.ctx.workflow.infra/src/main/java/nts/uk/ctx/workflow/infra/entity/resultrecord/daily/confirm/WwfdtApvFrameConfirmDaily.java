package nts.uk.ctx.workflow.infra.entity.resultrecord.daily.confirm;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppPhaseConfirm;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootConfirm;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_DAY_APV_FR_CONFIRM")
@Builder
@Getter
public class WwfdtApvFrameConfirmDaily extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvFrameConfirmDailyPK pk;
	
	@Column(name="CID")
	private String companyID;
	
	@Column(name="EMPLOYEE_ID")
	private String employeeID;
	
	@Column(name="RECORD_DATE")
	private GeneralDate recordDate;
	
	@Column(name="APPROVER_ID")
	private String approverID;
	
	@Column(name="REPRESENTER_ID")
	private String representerID;
	
	@Column(name="APPROVAL_DATE")
	private GeneralDate approvalDate;
	
	@Override
	protected Object getKey() {
		return pk;
	}
	
	public static List<WwfdtApvFrameConfirmDaily> fromDomain(AppRootConfirm root) {
		List<WwfdtApvFrameConfirmDaily> frame = new ArrayList<>();
		root.getListAppPhase().forEach(p -> {
			p.getListAppFrame().forEach(f -> {
				frame.add(fromDomain(root, p, f));
			});
		});
		return frame;
	}

	public static WwfdtApvFrameConfirmDaily fromDomain(AppRootConfirm appRootConfirm, AppPhaseConfirm appPhaseConfirm, AppFrameConfirm appFrameConfirm){
		return WwfdtApvFrameConfirmDaily.builder()
				.pk(new WwfdpApvFrameConfirmDailyPK(appRootConfirm.getRootID(), appPhaseConfirm.getPhaseOrder(), appFrameConfirm.getFrameOrder()))
				.companyID(appRootConfirm.getCompanyID())
				.employeeID(appRootConfirm.getEmployeeID()) 
				.recordDate(appRootConfirm.getRecordDate())
				.approverID(appFrameConfirm.getApproverID().orElse(null))
				.representerID(appFrameConfirm.getRepresenterID().orElse(null))
				.approvalDate(appFrameConfirm.getApprovalDate())
				.build();
	}
}
