/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.workingcondition;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtPersonalDayOfWeek.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_PERSONAL_DAY_OF_WEEK")
public class KshmtPersonalDayOfWeek extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The kshmt personal day of week PK. */
	@EmbeddedId
	protected KshmtPersonalDayOfWeekPK kshmtPersonalDayOfWeekPK;

	/** The exclus ver. */
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** SID */
	@Column(name = "SID")
	private String sid;

	/** The work type code. */
	@Column(name = "WORK_TYPE_CODE")
	private String workTypeCode;

	/** The work time code. */
	@Column(name = "WORK_TIME_CODE")
	private String workTimeCode;

	/** The kshmt per work cats. */
	@JoinColumns({
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = true, updatable = true),
			@JoinColumn(name = "PER_WORK_DAY_OFF_ATR", referencedColumnName = "PER_WORK_DAY_OFF_ATR", insertable = true, updatable = true) })
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<KshmtDayofweekTimeZone> kshmtDayofweekTimeZones;

	public static final JpaEntityMapper<KshmtPersonalDayOfWeek> MAPPER = new JpaEntityMapper<>(KshmtPersonalDayOfWeek.class);
	
	/**
	 * Instantiates a new kshmt personal day of week.
	 */
	public KshmtPersonalDayOfWeek() {
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
		hash += (kshmtPersonalDayOfWeekPK != null ? kshmtPersonalDayOfWeekPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtPersonalDayOfWeek)) {
			return false;
		}
		KshmtPersonalDayOfWeek other = (KshmtPersonalDayOfWeek) object;
		if ((this.kshmtPersonalDayOfWeekPK == null && other.kshmtPersonalDayOfWeekPK != null)
				|| (this.kshmtPersonalDayOfWeekPK != null
						&& !this.kshmtPersonalDayOfWeekPK.equals(other.kshmtPersonalDayOfWeekPK))) {
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
		return this.kshmtPersonalDayOfWeekPK;
	}
}
