package nts.uk.ctx.pr.formula.infra.entity.formula;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class QcfmtFormulaPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "CCD")
	public String ccd;
	
	@Column(name = "FORMULA_CD")
	public String formulaCd;
	
}
