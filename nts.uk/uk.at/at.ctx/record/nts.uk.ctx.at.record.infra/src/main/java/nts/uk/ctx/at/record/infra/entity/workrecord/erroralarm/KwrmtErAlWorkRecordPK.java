/**
 * 5:09:55 PM Jul 24, 2017
 */
package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author hungnm
 *
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KwrmtErAlWorkRecordPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "CID")
	public String companyId;
	
	@NotNull
	@Column(name = "ERROR_ALARM_CD")
	public String errorAlarmCode;
	
}
