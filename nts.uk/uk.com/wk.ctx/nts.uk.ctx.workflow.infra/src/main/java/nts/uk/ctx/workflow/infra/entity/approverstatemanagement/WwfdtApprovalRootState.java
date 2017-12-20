package nts.uk.ctx.workflow.infra.entity.approverstatemanagement;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
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
@Table(name="WWFDT_APPROVAL_ROOT_STATE")
public class WwfdtApprovalRootState extends UkJpaEntity {
	
	@EmbeddedId
	public WwfdpApprovalRootStatePK wwfdpApprovalRootStatePK;
	
	@Column(name="ROOT_TYPE")
	public Integer rootType;
	
	@Column(name="HIS_ID")
	public String historyID;
	
	@Column(name="EMPLOYEE_ID")
	public String employeeID;
	
	@Column(name="APPROVAL_RECORD_DATE")
	public GeneralDate recordDate;
	
	@OneToMany(targetEntity=WwfdtApprovalPhaseState.class, cascade = CascadeType.ALL, mappedBy = "wwfdtApprovalRootState", orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinTable(name = "WWFDT_APPROVAL_PHASE_ST")
	public List<WwfdtApprovalPhaseState> listWwfdtApprovalPhaseState;

	@Override
	protected Object getKey() {
		return wwfdpApprovalRootStatePK; 
	}
}
