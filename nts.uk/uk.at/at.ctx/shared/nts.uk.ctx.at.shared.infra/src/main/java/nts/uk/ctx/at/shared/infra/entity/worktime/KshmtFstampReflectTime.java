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
 * The Class KshmtFstampReflectTime.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_FSTAMP_REFLECT_TIME")
public class KshmtFstampReflectTime extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The kshmt fstamp reflect time PK. */
	@EmbeddedId
	protected KshmtFstampReflectTimePK kshmtFstampReflectTimePK;

	/** The exclus ver. */
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The two reflect basic time. */
	@Column(name = "TWO_REFLECT_BASIC_TIME")
	private int twoReflectBasicTime;

	/**
	 * Instantiates a new kshmt fstamp reflect time.
	 */
	public KshmtFstampReflectTime() {
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
		hash += (kshmtFstampReflectTimePK != null ? kshmtFstampReflectTimePK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtFstampReflectTime)) {
			return false;
		}
		KshmtFstampReflectTime other = (KshmtFstampReflectTime) object;
		if ((this.kshmtFstampReflectTimePK == null && other.kshmtFstampReflectTimePK != null)
				|| (this.kshmtFstampReflectTimePK != null
						&& !this.kshmtFstampReflectTimePK.equals(other.kshmtFstampReflectTimePK))) {
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
		return this.kshmtFstampReflectTimePK;
	}

}
