package nts.uk.ctx.pr.formula.infra.entity.formula;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class QsvstSystemVariablePK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "VARIABLE_ID")
	public String variableId;

}
