package nts.uk.ctx.at.function.infra.entity.alarm.extractresult;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class KfnmtAlexEmpEralDataPK implements Serializable{

	/***/
	private static final long serialVersionUID = 1L;

	/** 実行ID */
	@Column(name = "EXECUTE_ID")
	public String executeId;
	
	/** 従業員ID */
	@Column(name = "EMPLOYEE_ID")
	public String employeeId;
	
	/** レコードID */
	@Column(name = "RECORD_ID")
	public String recordId;
}
