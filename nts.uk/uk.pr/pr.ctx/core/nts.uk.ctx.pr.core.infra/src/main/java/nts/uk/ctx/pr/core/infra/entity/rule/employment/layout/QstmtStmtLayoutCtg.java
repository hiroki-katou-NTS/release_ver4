package nts.uk.ctx.pr.core.infra.entity.rule.employment.layout;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QSTMT_STMT_LAYOUT_CTG")
public class QstmtStmtLayoutCtg implements Serializable{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public QstmtStmtLayoutCtgPK qstmtStmtLayoutCtgPk;
//Lanlt remove	
//	@Basic(optional = false)
//	@Column(name ="STR_YM")
//	public int strYm;
//
//	@Basic(optional = false)
//	@Column(name = "END_YM")
//	public int endYm;

	@Basic(optional = false)
	@Column(name = "CTG_POS")
	public int ctgPos;
}
