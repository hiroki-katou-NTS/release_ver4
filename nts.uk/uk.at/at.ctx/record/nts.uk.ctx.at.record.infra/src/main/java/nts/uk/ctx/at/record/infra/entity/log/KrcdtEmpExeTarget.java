package nts.uk.ctx.at.record.infra.entity.log;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.ComplStateOfExeContents;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.TargetPerson;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.EmployeeExecutionStatus;
import nts.uk.ctx.at.record.dom.workrecord.workperfor.dailymonthlyprocessing.enums.ExecutionContent;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author nampt 対象者
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_EMP_EXE_TARGET")
public class KrcdtEmpExeTarget extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcdtEmpExeTargetPK krcdtEmpExeTargetPK;

	@OneToMany(mappedBy="empExeTarget", cascade = CascadeType.ALL)
	@JoinTable(name = "KRCDT_EMP_EXE_TARGET_STT")
	public List<KrcdtEmpExeTargetStt> lstEmpExeTargetStt;
	
	@Override
	protected Object getKey() {
		return this.krcdtEmpExeTargetPK;
	}

	public static KrcdtEmpExeTarget toEntity(TargetPerson domain) {
		KrcdtEmpExeTarget entity = new KrcdtEmpExeTarget(
			new KrcdtEmpExeTargetPK(domain.getEmployeeId(), domain.getEmpCalAndSumExecLogId()),
			domain.getState().stream().map(state -> {
				return new KrcdtEmpExeTargetStt(
					new KrcdtEmpExeTargetSttPK(domain.getEmployeeId(), domain.getEmpCalAndSumExecLogId(), state.getExecutionContent().value),
					state.getStatus().value
				);
			}).collect(Collectors.toList())
		);
		return entity;
	}

	public TargetPerson toDomain() {
		return new TargetPerson(
			this.krcdtEmpExeTargetPK.employeeId,
			this.krcdtEmpExeTargetPK.empCalAndSumExecLogID,
			this.lstEmpExeTargetStt.stream().map(state -> {
				return new ComplStateOfExeContents(
					EnumAdaptor.valueOf(state.KrcdtEmpExeTargetSttPK.executionContent, ExecutionContent.class),
					EnumAdaptor.valueOf(state.executionState, EmployeeExecutionStatus.class)
				);
			}).collect(Collectors.toList())
		);
	}
	
}
