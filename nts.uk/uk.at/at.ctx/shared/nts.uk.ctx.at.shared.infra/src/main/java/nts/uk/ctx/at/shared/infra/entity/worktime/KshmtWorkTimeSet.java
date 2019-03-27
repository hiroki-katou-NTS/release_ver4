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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtWorkTimeSet.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_WORK_TIME_SET")
@AllArgsConstructor
public class KshmtWorkTimeSet extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The kshmt work time set PK. */
	@EmbeddedId
	protected KshmtWorkTimeSetPK kshmtWorkTimeSetPK;

	/** The exclus ver. */
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The name. */
	@Column(name = "NAME")
	private String name;

	/** The abname. */
	@Column(name = "ABNAME")
	private String abname;

	/** The symbol. */
	@Column(name = "SYMBOL")
	private String symbol;

	/** The daily work atr. */
	@Column(name = "DAILY_WORK_ATR")
	private int dailyWorkAtr;

	/** The worktime set method. */
	@Column(name = "WORKTIME_SET_METHOD")
	private int worktimeSetMethod;

	/** The abolition atr. */
	@Column(name = "ABOLITION_ATR")
	private int abolitionAtr;

	/** The color. */
	@Column(name = "COLOR")
	private String color;

	/** The memo. */
	@Column(name = "MEMO")
	private String memo;

	/** The note. */
	@Column(name = "NOTE")
	private String note;

	/**
	 * Instantiates a new kshmt work time set.
	 */
	public KshmtWorkTimeSet() {
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
		hash += (kshmtWorkTimeSetPK != null ? kshmtWorkTimeSetPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtWorkTimeSet)) {
			return false;
		}
		KshmtWorkTimeSet other = (KshmtWorkTimeSet) object;
		if ((this.kshmtWorkTimeSetPK == null && other.kshmtWorkTimeSetPK != null)
				|| (this.kshmtWorkTimeSetPK != null
						&& !this.kshmtWorkTimeSetPK.equals(other.kshmtWorkTimeSetPK))) {
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
		return this.kshmtWorkTimeSetPK;
	}

}
