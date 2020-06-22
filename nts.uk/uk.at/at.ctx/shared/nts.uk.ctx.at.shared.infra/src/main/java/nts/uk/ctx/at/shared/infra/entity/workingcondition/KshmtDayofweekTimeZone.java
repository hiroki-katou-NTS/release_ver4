/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.workingcondition;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;

/**
 * The Class KshmtDayofweekTimeZone.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_DAYOFWEEK_TIME_ZONE")
public class KshmtDayofweekTimeZone extends KshmtTimeZone implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The kshmt dayofweek time zone PK. */
	@EmbeddedId
	protected KshmtDayofweekTimeZonePK kshmtDayofweekTimeZonePK;

	public static final JpaEntityMapper<KshmtDayofweekTimeZone> MAPPER = new JpaEntityMapper<>(KshmtDayofweekTimeZone.class);
	
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
