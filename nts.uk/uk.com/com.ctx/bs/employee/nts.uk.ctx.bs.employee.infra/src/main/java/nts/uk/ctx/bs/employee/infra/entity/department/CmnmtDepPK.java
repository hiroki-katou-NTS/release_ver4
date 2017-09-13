package nts.uk.ctx.bs.employee.infra.entity.department;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CmnmtDepPK implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "CCD")
	@NotNull
	private String companyCode;

	@Column(name = "HIST_ID")
	@NotNull
	private String historyId;

	@Column(name = "DEPCD")
	@NotNull
	private String departmentCode;

}
