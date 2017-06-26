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

@Entity
@Table(name="QSTMT_STMT_LAYOUT_HEAD")
@AllArgsConstructor
@NoArgsConstructor
public class QstmtStmtLayoutHead extends UkJpaEntity implements Serializable {
	
	public static final long serialVersionUID = 1L;

	@EmbeddedId
	public QstmtStmtLayoutHeadPK qstmtStmtLayoutHeadPK;
	
	@Basic(optional = true)
	@Column(name ="STMT_NAME")
	public String stmtName;
// @ Lanlt remove
//	@Basic(optional = false)
//	@Column(name ="STR_YM")
//	public int strYm;
//
//	@Basic(optional = false)
//	@Column(name ="END_YM")
//	public int endYm;
//	
//	@Basic(optional = false)
//	@Column(name ="LAYOUT_ATR")
//	public int layoutAtr;

	@Override
	protected Object getKey() {
		return qstmtStmtLayoutHeadPK;
	}
	
//	@OneToMany(cascade=CascadeType.ALL, mappedBy="layoutHead")
//	public List<QstmtStmtLayoutCtg> layoutCategories;
}
