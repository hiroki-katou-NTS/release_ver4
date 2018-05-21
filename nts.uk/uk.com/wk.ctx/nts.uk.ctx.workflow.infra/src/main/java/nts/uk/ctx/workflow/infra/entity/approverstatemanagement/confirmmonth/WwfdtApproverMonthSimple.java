package nts.uk.ctx.workflow.infra.entity.approverstatemanagement.confirmmonth;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="WWFDT_APPROVER_MONTH")
@Builder
public class WwfdtApproverMonthSimple extends UkJpaEntity {
	
	@EmbeddedId
	public WwfdpApproverMonthPK wwfdpApproverMonthPK;
	
	@Column(name="CID")
	public String companyID;
	
	@Column(name="APPROVAL_RECORD_DATE")
	public GeneralDate recordDate;

	@Override
	protected Object getKey() {
		return wwfdpApproverMonthPK;
	}
	
}
