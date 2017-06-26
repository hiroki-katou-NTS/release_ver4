package nts.uk.ctx.pr.formula.infra.entity.formula;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name="QCFMT_FORMULA_EASY_HEAD")
public class QcfmtFormulaEasyHeader extends UkJpaEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    public QcfmtFormulaEasyHeaderPK qcfmtFormulaEasyHeaderPK;
	
	@Column(name = "CONDITION_ATR")
	public BigDecimal conditionAtr;

	@Override
	protected Object getKey() {
		return this.qcfmtFormulaEasyHeaderPK;
	}	
	
}
