package nts.uk.ctx.sys.assist.infra.entity.deletedata;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.sys.assist.dom.deletedata.EmployeeDeletion;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "SSPDT_EMPLOYEES_DELETION")
@NoArgsConstructor
@AllArgsConstructor
public class SspdtEmployeesDeletion extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
    public SspdtEmployeesDeletionPK sspdtEmployeesDeletionPK;
	
	/**
	 * The employee code
	 * 社員コード
	 */
	@Basic(optional = false)
	@Column(name = "EMPLOYEE_CODE")
	public String employeeCode;
	
	/** The business name. */
	/** ビジネスネーム */
	@Basic(optional = false)
	@Column(name = "BUSINESS_NAME")
	public String businessName;
	
	@Override
	protected Object getKey() {
		return sspdtEmployeesDeletionPK;
	}

	public EmployeeDeletion toDomain() {
		return EmployeeDeletion.createFromJavatype(this.sspdtEmployeesDeletionPK.delId, 
				this.sspdtEmployeesDeletionPK.employeeId, this.employeeCode, this.businessName);
	}

	public static SspdtEmployeesDeletion toEntity(EmployeeDeletion employeeDeletion) {
		return new SspdtEmployeesDeletion(new SspdtEmployeesDeletionPK(
				employeeDeletion.getDelId(), employeeDeletion.getEmployeeId()), 
				employeeDeletion.getEmployeeCode().v(), employeeDeletion.getBusinessName().v());
	}
}
