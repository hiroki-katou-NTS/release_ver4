package nts.uk.ctx.hr.develop.infra.entity.stresscheck;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author anhdt 履歴ID
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class PpedtStressHistoryPk implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 履歴ID
	 */
	@Column(name = "HIST_ID")
	public String historyID;
}
