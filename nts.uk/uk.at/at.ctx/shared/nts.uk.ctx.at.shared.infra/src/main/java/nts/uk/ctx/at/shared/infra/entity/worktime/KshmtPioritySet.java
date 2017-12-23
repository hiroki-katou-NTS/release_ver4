/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.worktime;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtPioritySet.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_PIORITY_SET")
public class KshmtPioritySet extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The kshmt piority set PK. */
	@EmbeddedId
	protected KshmtPioritySetPK kshmtPioritySetPK;

	/** The exclus ver. */
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The piority atr. */
	@Column(name = "PIORITY_ATR")
	private short piorityAtr;

	/**
	 * Instantiates a new kshmt piority set.
	 */
	public KshmtPioritySet() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (kshmtPioritySetPK != null ? kshmtPioritySetPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtPioritySet)) {
			return false;
		}
		KshmtPioritySet other = (KshmtPioritySet) object;
		if ((this.kshmtPioritySetPK == null && other.kshmtPioritySetPK != null)
				|| (this.kshmtPioritySetPK != null
						&& !this.kshmtPioritySetPK.equals(other.kshmtPioritySetPK))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.kshmtPioritySetPK;
	}

}
