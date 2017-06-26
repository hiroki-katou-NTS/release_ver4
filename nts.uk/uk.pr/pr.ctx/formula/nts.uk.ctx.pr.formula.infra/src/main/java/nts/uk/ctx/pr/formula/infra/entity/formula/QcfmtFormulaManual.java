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

/**
 * @author nampt
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="QCFMT_FORMULA_MANUAL")
public class QcfmtFormulaManual extends UkJpaEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    public QcfmtFormulaManualPK qcfmtFormulaManualPK;	
	
	@Column(name = "FORMULA_CONTENT")
	public String formulaContent;
	
	@Column(name = "REFER_MONTH_ATR")
	public BigDecimal referMonthAtr;
	
	@Column(name = "ROUND_DIGIT")
	public BigDecimal roundDigit;
	
	@Column(name = "ROUND_ATR")
	public BigDecimal roundAtr;

	@Override
	protected Object getKey() {
		return this.qcfmtFormulaManualPK;
	}
	
}
