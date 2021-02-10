/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.worktime.flexset;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtWtFleStmpRefTs.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_WT_FLE_STMP_REF_TS")
public class KshmtWtFleStmpRefTs extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The kshmt flex stamp reflect PK. */
	@EmbeddedId
	protected KshmtFlexStampReflectPK kshmtFlexStampReflectPK;

	/** The exclus ver. */
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The start time. */
	@Column(name = "START_TIME")
	private int startTime;

	/** The end time. */
	@Column(name = "END_TIME")
	private int endTime;

	/**
	 * Instantiates a new kshmt flex stamp reflect.
	 */
	public KshmtWtFleStmpRefTs() {
		super();
	}

	/**
	 * Instantiates a new kshmt flex stamp reflect.
	 *
	 * @param kshmtFlexStampReflectPK the kshmt flex stamp reflect PK
	 */
	public KshmtWtFleStmpRefTs(KshmtFlexStampReflectPK kshmtFlexStampReflectPK) {
		super();
		this.kshmtFlexStampReflectPK = kshmtFlexStampReflectPK;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (kshmtFlexStampReflectPK != null ? kshmtFlexStampReflectPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtWtFleStmpRefTs)) {
			return false;
		}
		KshmtWtFleStmpRefTs other = (KshmtWtFleStmpRefTs) object;
		if ((this.kshmtFlexStampReflectPK == null && other.kshmtFlexStampReflectPK != null)
				|| (this.kshmtFlexStampReflectPK != null
						&& !this.kshmtFlexStampReflectPK.equals(other.kshmtFlexStampReflectPK))) {
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
		return this.kshmtFlexStampReflectPK;
	}

}
