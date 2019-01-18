/**
 * 5:14:50 PM Dec 5, 2017
 */
package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.worktime;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author hungnm
 *
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class KrcstErAlWhPlanActualPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name ="ERAL_CHECK_ID")
	public String eralCheckId;
	
	@NotNull
	@Column(name ="WORK_TIME_CD")
	public String workTimeCode;
	
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 17)
    @Column(name = "CID")
	public String cid;

	
	
}
