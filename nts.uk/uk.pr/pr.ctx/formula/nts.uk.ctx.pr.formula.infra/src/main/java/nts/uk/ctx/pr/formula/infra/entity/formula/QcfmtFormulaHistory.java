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
@Table(name="QCFMT_FORMULA_HIST")
public class QcfmtFormulaHistory extends UkJpaEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	public QcfmtFormulaHistoryPK qcfmtFormulaHistoryPK;
	
	@Column(name ="STR_YM")
	public BigDecimal startDate;
	
	@Column(name ="END_YM")
	public BigDecimal endDate;

	@Override
	protected Object getKey() {
		return this.qcfmtFormulaHistoryPK;
	}
	
}
