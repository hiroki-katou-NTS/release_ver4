package nts.uk.ctx.basic.infra.entity.organization.department;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CMNMT_DEP_MEMO")
@NoArgsConstructor
@AllArgsConstructor
public class CmnmtDepMemo implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CmnmtDepMemoPK cmnmtDepMemoPK;

	@Column(name = "MEMO")
	private String memo;

	public CmnmtDepMemoPK getCmnmtDepMemoPK() {
		return cmnmtDepMemoPK;
	}

	public void setCmnmtDepMemoPK(CmnmtDepMemoPK cmnmtDepMemoPK) {
		this.cmnmtDepMemoPK = cmnmtDepMemoPK;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
