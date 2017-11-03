package nts.uk.ctx.bs.employee.infra.entity.jobtitle;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

<<<<<<< HEAD
<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
=======
>>>>>>> 6dffe644598113d7f684312f8f7a470fbc9b91d9
=======
>>>>>>> 6dffe644598113d7f684312f8f7a470fbc9b91d9
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.infra.entity.department.BsymtCurrAffiDept;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="BSYMT_SUB_JOB_POSITION")
public class BsymtSubJobPosition extends UkJpaEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**the sub job position id*/
	@Id
	@Basic(optional = false)
	@Column(name = "SUB_JOB_POSITION_ID")
	public String subJobPosId;
	
	/** The dep id. */
	@Column(name = "AFFI_DEPT_ID")
	public String affiDeptId;
	
	/** The dep id. */
	@Column(name = "JOB_TITLE_ID")
	public String jobTitleId;
	
	/** The str D. */
	@Column(name = "STR_D")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate strD;

	/** The end D. */
	@Column(name = "END_D")
	@Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate endD;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "AFFI_DEPT_ID", referencedColumnName = "AFFI_DEPT_ID", insertable = false, updatable = false)
	})
	public BsymtCurrAffiDept bsymtCurrAffiDept;

	@Override
	protected Object getKey() {
		return subJobPosId;
	}

}
