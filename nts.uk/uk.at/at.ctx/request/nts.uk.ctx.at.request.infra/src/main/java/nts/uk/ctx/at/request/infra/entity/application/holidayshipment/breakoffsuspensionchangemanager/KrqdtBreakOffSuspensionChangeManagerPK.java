package nts.uk.ctx.at.request.infra.entity.application.holidayshipment.breakoffsuspensionchangemanager;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable

/**
 * 振休申請休出変更管理PK
 * 
 * @author sonnlb
 */
public class KrqdtBreakOffSuspensionChangeManagerPK implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	/**
	 * 振出申請ID
	 */
	@Basic(optional = false)
	@Column(name = "REC_APP_ID")
	private String recAppID;

	/**
	 * 振休申請ID
	 */
	@Basic(optional = false)
	@Column(name = "ABSENCE_LEAVE_APP_ID")
	private String absenceLeaveAppID;
}
