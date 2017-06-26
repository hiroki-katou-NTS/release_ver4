package nts.uk.ctx.pr.core.infra.entity.paymentdata;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import nts.uk.shr.infra.data.entity.AggregateTableEntity;

@Entity
@Table(name = "QSTMT_STMT_ALLOT_PS")
public class QstmtStmtAllotPs extends AggregateTableEntity{
	
	@EmbeddedId
	public QstmtStmtAllotPsPK qstmtStmtAllotPsPK;
	
	@Column(name = "INV_SCD")
	public String employeeCode;
	
	@Basic(optional = false)
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
		return this.qstmtStmtAllotPsPK;
	}
}
