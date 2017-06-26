package nts.uk.ctx.basic.infra.entity.organization.classification;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CmnmtClassPK implements Serializable {

	private static final long serialVersionUID = 2057071023975099159L;

	@NotNull
	@Column(name = "CCD")
	private String companyCode;

	@NotNull
	@Column(name = "CLSCD")
	private String classificationCode;
}
