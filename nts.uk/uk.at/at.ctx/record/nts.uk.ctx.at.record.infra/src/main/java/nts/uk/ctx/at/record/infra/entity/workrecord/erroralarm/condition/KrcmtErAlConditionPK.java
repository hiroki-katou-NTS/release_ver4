package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class KrcmtErAlConditionPK implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 36)
	@Column(name = "ERAL_CHECK_ID")
	public String eralCheckId;
	
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 17)
    @Column(name = "CID")
	public String cid;
}
