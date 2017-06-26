package nts.uk.ctx.basic.infra.entity.organization.workplace;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CMNMT_WORK_PLACE_MEMO")
@NoArgsConstructor
@AllArgsConstructor
public class CmnmtWorkPlaceMemo implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CmnmtWorkPlaceMemoPK cmnmtWorkPlaceMemoPK;

	@Column(name = "MEMO")
	private String memo;

	public CmnmtWorkPlaceMemoPK getCmnmtWorkPlaceMemoPK() {
		return cmnmtWorkPlaceMemoPK;
	}

	public void setCmnmtWorkPlaceMemoPK(CmnmtWorkPlaceMemoPK cmnmtWorkPlaceMemoPK) {
		this.cmnmtWorkPlaceMemoPK = cmnmtWorkPlaceMemoPK;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
