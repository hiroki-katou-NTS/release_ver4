package nts.uk.ctx.pr.formula.infra.entity.formula;

import java.io.Serializable;

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
@Table(name="QCFMT_FORMULA_EASY_A")
public class QcfmtFormulaEasyStandardItem extends UkJpaEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    public QcfmtFormulaEasyStandardItemPK qcfmtFormulaEasyStandardItemPK;

	@Override
	protected Object getKey() {
		return this.qcfmtFormulaEasyStandardItemPK;
	}
	
}
