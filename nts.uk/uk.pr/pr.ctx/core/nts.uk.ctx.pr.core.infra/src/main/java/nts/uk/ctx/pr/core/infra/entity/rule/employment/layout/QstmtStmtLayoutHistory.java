/**
 * 
 */
package nts.uk.ctx.pr.core.infra.entity.rule.employment.layout;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * @author lanlt
 *
 */
@Entity
@Table(name = "QSTMT_STMT_LAYOUT_HIST")
@AllArgsConstructor
@NoArgsConstructor
public class QstmtStmtLayoutHistory extends UkJpaEntity implements Serializable {
	public static final long serialVersionUID = 1L;

	@EmbeddedId
	public QstmtStmtLayoutHistoryPK qstmtStmtLayoutHistPK;

	@Basic(optional = false)
	@Column(name = "STR_YM")
	public int startYear;
	
	@Basic(optional = false)
	@Column(name = "END_YM")
	public int endYear;
	
	@Basic(optional = false)
	@Column(name = "LAYOUT_ATR")
	public int layoutAttr;

	@Override
	protected Object getKey() {
		return qstmtStmtLayoutHistPK;
	}

}
