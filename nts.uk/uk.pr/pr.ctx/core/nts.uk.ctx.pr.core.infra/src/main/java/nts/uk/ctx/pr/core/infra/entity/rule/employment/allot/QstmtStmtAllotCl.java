package nts.uk.ctx.pr.core.infra.entity.rule.employment.allot;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "QSTMT_STMT_ALLOT_CL")
@AllArgsConstructor
@NoArgsConstructor
public class QstmtStmtAllotCl implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public QstmtStmtAllotClPK qstmtStmtAllotClPK;

	@Basic(optional = false)
	@Column(name = "PAY_STMT_CD")
	public String paymentDetailCode;

	@Basic(optional = false)
	@Column(name = "BONUS_STMT_CD")
	public String bonusDetailCode;

}
