package nts.uk.ctx.at.function.infra.entity.alarm.extractresult;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "KFNMT_ALEX_EMP_DATA")
public class KfnmtAlexEmpData extends UkJpaEntity {

	@EmbeddedId
	public KfnmtAlexEmpDataPK pk;

	/** 従業員コード */
	@Column(name = "EMPLOYEE_CODE", nullable = false)
	public String employeeCode;		
	
	/** 従業員名 */
	@Column(name = "EMPLOYEE_NAME", nullable = false)
	public String employeeName;		
	
	/** 職場名 */
	@Column(name = "WORKPLACE_NAME")
	public String workplaceName;
	
	/** 階層コード */
	@Column(name = "HIERARCHY_CD")
	public String hierarchyCode;	
	
	/** 職場開始日 */
	@Column(name = "WORKPLACE_WORK_START_DATE")
	public GeneralDate wpWorkStartDate;	
	
	/** 職場終了日 */
	@Column(name = "WORKPLACE_WORK_END_DATE")
	public GeneralDate wpWorkEndDate;
	
	@Override
	protected Object getKey() {
		return pk;
	}
}
