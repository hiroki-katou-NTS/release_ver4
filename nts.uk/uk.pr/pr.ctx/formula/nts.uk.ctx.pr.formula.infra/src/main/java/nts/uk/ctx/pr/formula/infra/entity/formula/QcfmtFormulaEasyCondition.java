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
@Table(name="QCFMT_FORMULA_EASY_CONDI")
public class QcfmtFormulaEasyCondition extends UkJpaEntity implements Serializable{
	
private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    public QcfmtFormulaEasyConditionPK qcfmtFormulaEasyConditionPK;
	
	@Column(name = "FIX_FORMULA_ATR")
	public BigDecimal fixFormulaAtr;
	
	@Column(name = "FIX_MNY")
	public BigDecimal fixMny;
	
	@Column(name = "EASY_FORMULA_CD")
	public String easyFormulaCd;

	@Override
	protected Object getKey() {
		return this.qcfmtFormulaEasyConditionPK;
	}

}
