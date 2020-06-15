package nts.uk.ctx.workflow.infra.entity.resultrecord.monthly.instance;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
	
	@Column(name="APPROVAL_FORM")
	private Integer approvalForm;
	
	public List<WwfdtApvFrameInstanceMonthly> listWwfdtApvFrameInstanceMonthly;

	@Override
	protected Object getKey() {
		return pk;
	}
	
	public static WwfdtApvPhaseInstanceMonthly fromDomain(String rootID,AppPhaseInstance instance) {
		return new WwfdtApvPhaseInstanceMonthly(
				new WwfdpApvPhaseInstanceMonthlyPK(rootID, instance.getPhaseOrder()), 
				instance.getPhaseOrder(), 
				instance.getListAppFrame()
					.stream()
					.map(t -> WwfdtApvFrameInstanceMonthly.fromDomain(rootID, instance.getPhaseOrder(), t))
					.collect(Collectors.toList()));
	}
}
