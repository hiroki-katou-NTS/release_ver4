package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.uk.ctx.workflow.dom.resultrecord.AppRootInstance;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_MON_APV_AP_INSTANCE")
@Getter
public class WwfdtApvApproveInstanceMonthly extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvApproveInstanceMonthlyPK pk;
	
	@Override
	protected Object getKey() {
		return pk;
	}
	
	public static WwfdtApvApproveInstanceMonthly fromDomain(
			String rootID, Integer phaseOrder, Integer frameOrder, String approverChildID) {
		return new WwfdtApvApproveInstanceMonthly(
				new WwfdpApvApproveInstanceMonthlyPK(rootID, phaseOrder, frameOrder, approverChildID)
			);
	}
	
}
