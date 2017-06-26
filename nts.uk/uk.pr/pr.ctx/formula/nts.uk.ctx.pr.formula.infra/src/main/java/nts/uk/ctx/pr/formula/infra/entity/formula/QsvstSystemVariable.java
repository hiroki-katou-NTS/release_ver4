package nts.uk.ctx.pr.formula.infra.entity.formula;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="QSVST_SYSTEM_VARIABLE")
public class QsvstSystemVariable extends UkJpaEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public QsvstSystemVariablePK qsvstSystemVariablePK;
	
	@Column(name ="VARIABLE_NAME")
	public String variableName;
	
	@Column(name ="FROM_TABLE")
	public String fromTable;
	
	@Column(name ="FROM_COL_NAME")
	public String fromColumnName;
	
	@Column(name ="WHERE_CONDITION")
	public String whereCondition;

	@Override
	protected Object getKey() {
		return this.qsvstSystemVariablePK;
	}

}
