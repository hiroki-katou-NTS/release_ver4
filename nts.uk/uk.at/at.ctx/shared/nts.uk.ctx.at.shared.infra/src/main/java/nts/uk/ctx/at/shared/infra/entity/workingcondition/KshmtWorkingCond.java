/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.workingcondition;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtWorkingCond.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_WORKING_COND")
public class KshmtWorkingCond extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The kshmt working cond PK. */
	@EmbeddedId
	protected KshmtWorkingCondPK kshmtWorkingCondPK;

	/** The exclus ver. */
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The cid. */
	@Column(name = "CID")
	private String cid;

	/** The str D. */
	@Column(name = "STR_D")
	@Temporal(TemporalType.DATE)
	private GeneralDate strD;

	/** The end D. */
	@Column(name = "END_D")
	@Temporal(TemporalType.DATE)
	private GeneralDate endD;

	/** The kshmt working cond items. */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "HISTORY_ID", referencedColumnName = "HISTORY_ID", insertable = false, updatable = false) })
	private KshmtWorkingCondItem kshmtWorkingCondItem;

	/**
	 * Instantiates a new kshmt working cond.
	 */
	public KshmtWorkingCond() {
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
		hash += (kshmtWorkingCondPK != null ? kshmtWorkingCondPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtWorkingCond)) {
			return false;
		}
		KshmtWorkingCond other = (KshmtWorkingCond) object;
		if ((this.kshmtWorkingCondPK == null && other.kshmtWorkingCondPK != null)
				|| (this.kshmtWorkingCondPK != null
						&& !this.kshmtWorkingCondPK.equals(other.kshmtWorkingCondPK))) {
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
		return this.kshmtWorkingCondPK;
	}

}
