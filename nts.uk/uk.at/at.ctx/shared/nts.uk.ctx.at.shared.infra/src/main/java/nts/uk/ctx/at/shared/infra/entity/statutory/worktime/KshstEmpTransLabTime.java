/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nts.uk.ctx.at.shared.infra.entity.statutory.worktime;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.infra.entity.statutory.worktime_new.share.KshstTransLabTime;

/**
 * The Class KshstEmpTransLabTime.
 */

@Setter
@Getter
@Entity
@Table(name = "KSHST_EMP_TRANS_LAB_TIME")
public class KshstEmpTransLabTime extends KshstTransLabTime implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The kshst emp trans lab time PK. */
	@EmbeddedId
	protected KshstEmpTransLabTimePK kshstEmpTransLabTimePK;

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (kshstEmpTransLabTimePK != null ? kshstEmpTransLabTimePK.hashCode() : 0);
		return hash;
	}

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		// not set
		if (!(object instanceof KshstEmpTransLabTime)) {
			return false;
		}
		KshstEmpTransLabTime other = (KshstEmpTransLabTime) object;
		if ((this.kshstEmpTransLabTimePK == null && other.kshstEmpTransLabTimePK != null)
				|| (this.kshstEmpTransLabTimePK != null
						&& !this.kshstEmpTransLabTimePK.equals(other.kshstEmpTransLabTimePK))) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.kshstEmpTransLabTimePK;
	}

}
