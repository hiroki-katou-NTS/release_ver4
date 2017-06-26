package nts.uk.ctx.pr.core.infra.entity.rule.employment.allot;

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
@Table(name = "QSTMT_STMT_ALLOT_CP")
@AllArgsConstructor
@NoArgsConstructor
public class QstmtStmtAllotCp extends UkJpaEntity implements Serializable {
	
	public static final long serialVersionUID = 1L;
	
	@EmbeddedId
	public QstmtStmtAllotCpPK qstmtStmtAllotCpPK;

	@Column(name = "STR_YM")
	public int startDate;
	
	@Basic(optional = false)
	@Column(name = "END_YM")
	public int endDate;
	
	@Basic(optional = false)
	@Column(name = "PAY_STMT_CD")
	public String paymentDetailCode;
	
	@Basic(optional = false)
	@Column(name = "BONUS_STMT_CD")
	public String bonusDetailCode;

	@Override
	protected Object getKey() {
		return this.qstmtStmtAllotCpPK;
	}
}
