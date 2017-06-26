package nts.uk.ctx.pr.core.infra.entity.rule.employment.allot;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@Table(name="QSTMT_STMT_ALLOT_H_CL")
@AllArgsConstructor
@NoArgsConstructor
public class QstmtStmtAllotHCl implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	public QstmtStmtAllotHClPK qstmtStmtAllotHClPK ;
	
	@Basic (optional = false)
	@Column(name = "STR_YM")
	public BigDecimal startDateYM;
	
	@Basic (optional = false)
	@Column (name = "END_YM")
	public BigDecimal endDateYM;
	
	

}
