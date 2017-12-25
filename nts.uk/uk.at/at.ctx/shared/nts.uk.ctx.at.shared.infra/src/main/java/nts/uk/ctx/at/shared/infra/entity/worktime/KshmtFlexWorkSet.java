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
 * The Class KshmtFlexWorkSet.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_FLEX_WORK_SET")
public class KshmtFlexWorkSet extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The kshmt flex work set PK. */
	@EmbeddedId
	protected KshmtFlexWorkSetPK kshmtFlexWorkSetPK;

	/** The exclus ver. */
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The core time str. */
	@Column(name = "CORE_TIME_STR")
	private int coreTimeStr;

	/** The core time end. */
	@Column(name = "CORE_TIME_END")
	private int coreTimeEnd;

	/** The coretime use atr. */
	@Column(name = "CORETIME_USE_ATR")
	private int coretimeUseAtr;

	/** The least work time. */
	@Column(name = "LEAST_WORK_TIME")
	private int leastWorkTime;

	/** The deduct from work time. */
	@Column(name = "DEDUCT_FROM_WORK_TIME")
	private int deductFromWorkTime;

	/** The especial calc. */
	@Column(name = "ESPECIAL_CALC")
	private int especialCalc;

	/** The use halfday shift. */
	@Column(name = "USE_HALFDAY_SHIFT")
	private int useHalfdayShift;

	/**
	 * Instantiates a new kshmt flex work set.
	 */
	public KshmtFlexWorkSet() {
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
		hash += (kshmtFlexWorkSetPK != null ? kshmtFlexWorkSetPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtFlexWorkSet)) {
			return false;
		}
		KshmtFlexWorkSet other = (KshmtFlexWorkSet) object;
		if ((this.kshmtFlexWorkSetPK == null && other.kshmtFlexWorkSetPK != null)
				|| (this.kshmtFlexWorkSetPK != null
						&& !this.kshmtFlexWorkSetPK.equals(other.kshmtFlexWorkSetPK))) {
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
		return this.kshmtFlexWorkSetPK;
	}

}
