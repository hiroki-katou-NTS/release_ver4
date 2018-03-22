package nts.uk.ctx.sys.gateway.infra.entity.securitypolicy.lockoutdata;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the SGWMT_LOCKOUT_DATA database table.
 * 
 */
@Embeddable
public class SgwmtLockoutDataPK implements Serializable {
	
	/** The Constant serialVersionUID. */
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	/** The user id. */
	@Column(name="USER_ID")
	private String userId;

	/** The contract cd. */
	@Column(name="CONTRACT_CD")
	private String contractCd;

	/**
	 * Instantiates a new sgwmt lockout data PK.
	 */
	public SgwmtLockoutDataPK() {
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SgwmtLockoutDataPK)) {
			return false;
		}
		SgwmtLockoutDataPK castOther = (SgwmtLockoutDataPK)other;
		return 
			this.userId.equals(castOther.userId)
			&& this.contractCd.equals(castOther.contractCd);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId.hashCode();
		hash = hash * prime + this.contractCd.hashCode();
		
		return hash;
	}
}