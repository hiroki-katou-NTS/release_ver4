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
import nts.uk.ctx.workflow.dom.resultrecord.AppFrameInstance;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="WWFDT_MON_APV_FR_INSTANCE")
@Getter
public class WwfdtApvFrameInstanceMonthly extends UkJpaEntity {
	
	@EmbeddedId
	private WwfdpApvFrameInstanceMonthlyPK pk;
	
	@Column(name="CONFIRM_ATR")
	private Integer confirmAtr;
	
	public List<WwfdtApvApproveInstanceMonthly> listWwfdtApvApproveInstanceMonthly;
	
	@Override
	protected Object getKey() {
		return pk;
	}
	
	public static WwfdtApvFrameInstanceMonthly fromDomain(String rootID, Integer phaseOrder, AppFrameInstance instance) {
		return new WwfdtApvFrameInstanceMonthly(
				new WwfdpApvFrameInstanceMonthlyPK(rootID, phaseOrder, instance.getFrameOrder()), 
				instance.isConfirmAtr() ? 1 : 0,
				instance.getListApprover().stream().map(t ->
						WwfdtApvApproveInstanceMonthly.fromDomain(
								rootID, 
								phaseOrder, 
								instance.getFrameOrder(), 
								t)
					)
				.collect(Collectors.toList())
				);
	}
	
}
