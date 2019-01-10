/**
 * 2:05:22 PM Dec 6, 2017
 */
package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition;

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
public class KrcstErAlBusinessTypePK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 36)
	@Column(name = "ERAL_CHECK_ID")
	public String eralCheckId;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 10)
	@Column(name = "BUSINESS_TYPE_CD")
	public String businessTypeCd;
	
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 17)
    @Column(name = "CID")
	public String cid;
	
	
}
