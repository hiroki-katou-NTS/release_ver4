/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.worktime.predset;

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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtPredTimeSet.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_PRED_TIME_SET")
@AllArgsConstructor
public class KshmtPredTimeSet extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The kshmt pred time set PK. */
	@EmbeddedId
	protected KshmtPredTimeSetPK kshmtPredTimeSetPK;

	/** The exclus ver. */
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The range time day. */
	@Column(name = "RANGE_TIME_DAY")
	private int rangeTimeDay;

	/** The night shift atr. */
	@Column(name = "NIGHT_SHIFT_ATR")
	private int nightShiftAtr;

	/** The start date clock. */
	@Column(name = "START_DATE_CLOCK")
	private int startDateClock;

	/** The is include ot. */
	@Column(name = "IS_INCLUDE_OT")
	private int isIncludeOt;

	/** The morning end time. */
	@Column(name = "MORNING_END_TIME")
	private int morningEndTime;

	/** The afternoon start time. */
	@Column(name = "AFTERNOON_START_TIME")
	private int afternoonStartTime;

	/** The work add one day. */
	@Column(name = "WORK_ADD_ONE_DAY")
	private int workAddOneDay;

	/** The work add morning. */
	@Column(name = "WORK_ADD_MORNING")
	private int workAddMorning;

	/** The work add afternoon. */
	@Column(name = "WORK_ADD_AFTERNOON")
	private int workAddAfternoon;

	/** The pred one day. */
	@Column(name = "PRED_ONE_DAY")
	private int predOneDay;

	/** The pred morning. */
	@Column(name = "PRED_MORNING")
	private int predMorning;

	/** The pred afternoon. */
	@Column(name = "PRED_AFTERNOON")
	private int predAfternoon;

	/** The kshmt work time sheet sets. */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = true, updatable = true),
			@JoinColumn(name = "WORKTIME_CD", referencedColumnName = "WORKTIME_CD", insertable = true, updatable = true)})
	private List<KshmtWorkTimeSheetSet> kshmtWorkTimeSheetSets;

	/**
	 * Instantiates a new kshmt pred time set.
	 */
	public KshmtPredTimeSet() {
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
		hash += (kshmtPredTimeSetPK != null ? kshmtPredTimeSetPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtPredTimeSet)) {
			return false;
		}
		KshmtPredTimeSet other = (KshmtPredTimeSet) object;
		if ((this.kshmtPredTimeSetPK == null && other.kshmtPredTimeSetPK != null)
				|| (this.kshmtPredTimeSetPK != null
						&& !this.kshmtPredTimeSetPK.equals(other.kshmtPredTimeSetPK))) {
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
		return this.kshmtPredTimeSetPK;
	}

}
