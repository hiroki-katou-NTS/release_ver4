package nts.uk.ctx.at.record.infra.entity.log;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author lamdt
 *
 */
@NoArgsConstructor
@Entity
@Table(name = "KRCDT_EXEC_TARGET_STS")
public class KrcdtExecTargetSts extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	public KrcdtEmpExeTargetSttPK KrcdtEmpExeTargetSttPK;
	
	@Column(name = "EXECUTION_STATE")
	public int executionState;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="SID", referencedColumnName="SID", insertable = false, updatable = false),
		@JoinColumn(name="EMP_EXECUTION_LOG_ID", referencedColumnName="EMP_EXECUTION_LOG_ID", insertable = false, updatable = false)
	})
	public KrcdtExecTarget empExeTarget;

	public KrcdtExecTargetSts(KrcdtEmpExeTargetSttPK krcdtEmpExeTargetSttPK, int executionState) {
		super();
		KrcdtEmpExeTargetSttPK = krcdtEmpExeTargetSttPK;
		this.executionState = executionState;
	}
	
	@Override
	protected Object getKey() {
		return this.KrcdtEmpExeTargetSttPK;
	}

}