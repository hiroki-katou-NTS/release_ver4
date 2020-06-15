package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.confirm;

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
@Table(name="WWFDT_MON_APV_FR_CONFIRM")
@Builder
@Getter
public class WwfdtApvFrameConfirmMonthly extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvFrameConfirmMonthlyPK pk;
	
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
	
	public static List<WwfdtApvFrameConfirmMonthly> fromDomain(AppRootConfirm root) {
		List<WwfdtApvFrameConfirmMonthly> frame = new ArrayList<>();
		root.getListAppPhase().forEach(p -> {
			p.getListAppFrame().forEach(f -> {
				frame.add(fromDomain(root, p, f));
			});
		});
		return frame;
	}

	public static WwfdtApvFrameConfirmMonthly fromDomain(AppRootConfirm appRootConfirm, AppPhaseConfirm appPhaseConfirm, AppFrameConfirm appFrameConfirm){
		return WwfdtApvFrameConfirmMonthly.builder()
				.pk(new WwfdpApvFrameConfirmMonthlyPK(appRootConfirm.getRootID(), appPhaseConfirm.getPhaseOrder(), appFrameConfirm.getFrameOrder()))
				.companyID(appRootConfirm.getCompanyID())
				.employeeID(appRootConfirm.getEmployeeID()) 
				.yearMonth(appRootConfirm.getYearMonth().get().v())
				.closureID(appRootConfirm.getClosureID().get())
				.closureDay(appRootConfirm.getClosureDate().map(z -> z.getClosureDay().v()).get())
				.lastDayFlg(appRootConfirm.getClosureDate().map(z -> z.getLastDayOfMonth() ? 1 : 0).get())
				.approverID(appFrameConfirm.getApproverID().orElse(null))
				.representerID(appFrameConfirm.getRepresenterID().orElse(null))
				.approvalDate(appFrameConfirm.getApprovalDate())
				.build();
	}
}
