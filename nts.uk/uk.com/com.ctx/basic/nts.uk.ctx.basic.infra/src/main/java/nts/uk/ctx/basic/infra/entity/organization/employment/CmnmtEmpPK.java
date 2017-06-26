package nts.uk.ctx.basic.infra.entity.organization.employment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CmnmtEmpPK implements Serializable {
	
	private static final long serialVersionUID = 2057071023975099159L;
	
	@NotNull
	@Column(name = "CCD")
	public String companyCode;
	@NotNull
	@Column(name = "EMPCD")
	public String employmentCode;
}
