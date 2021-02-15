package nts.uk.ctx.at.record.infra.entity.workrule.specific;

import java.io.Serializable;
import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The primary key class for the KRCST_CONSTRAINT_TIME_CAL database table.
 * @author HoangNDH
 */
@Embeddable
@Data
@NoArgsConstructor
public class KrcstConstraintTimeCalPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="CID")
	private String cid;

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof KrcstConstraintTimeCalPK)) {
			return false;
		}
		KrcstConstraintTimeCalPK castOther = (KrcstConstraintTimeCalPK)other;
		return 
			this.cid.equals(castOther.cid);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.cid.hashCode();
		
		return hash;
	}
}