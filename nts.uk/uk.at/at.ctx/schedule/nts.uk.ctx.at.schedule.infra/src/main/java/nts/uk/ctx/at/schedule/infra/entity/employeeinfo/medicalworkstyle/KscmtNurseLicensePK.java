package nts.uk.ctx.at.schedule.infra.entity.employeeinfo.medicalworkstyle;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KscmtNurseLicensePK implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 会社ID									
	 */
	@Column(name = "CID")
	private String companyId;

	@Column(name = "CD")
	private String code;
}
