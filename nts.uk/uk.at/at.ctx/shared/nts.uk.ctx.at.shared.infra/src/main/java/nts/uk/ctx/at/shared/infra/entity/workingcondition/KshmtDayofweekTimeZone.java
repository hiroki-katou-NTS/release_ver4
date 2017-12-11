/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.workingcondition;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtDayofweekTimeZone.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_DAYOFWEEK_TIME_ZONE")
public class KshmtDayofweekTimeZone extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The kshmt dayofweek time zone PK. */
	@EmbeddedId
	protected KshmtDayofweekTimeZonePK kshmtDayofweekTimeZonePK;

	/** The exclus ver. */
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The use atr. */
	@Column(name = "USE_ATR")
	private int useAtr;

	/** The cnt. */
	@Column(name = "CNT")
	private int cnt;

	/** The end time. */
	@Column(name = "END_TIME")
	private int endTime;

	/**
	 * Instantiates a new kshmt dayofweek time zone.
	 */
	public KshmtDayofweekTimeZone() {
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
		hash += (kshmtDayofweekTimeZonePK != null ? kshmtDayofweekTimeZonePK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtDayofweekTimeZone)) {
			return false;
		}
		KshmtDayofweekTimeZone other = (KshmtDayofweekTimeZone) object;
		if ((this.kshmtDayofweekTimeZonePK == null && other.kshmtDayofweekTimeZonePK != null)
				|| (this.kshmtDayofweekTimeZonePK != null
						&& !this.kshmtDayofweekTimeZonePK.equals(other.kshmtDayofweekTimeZonePK))) {
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
		return this.kshmtDayofweekTimeZonePK;
	}

}
