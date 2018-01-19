/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.worktime.difftimeset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeDailyAtr;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeMethodSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.common.KshmtWorktimeCommonSet;
import nts.uk.ctx.at.shared.infra.entity.worktime.common.KshmtWorktimeCommonSetPK;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtDiffTimeWorkSet.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_DIFF_TIME_WORK_SET")
public class KshmtDiffTimeWorkSet extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The kshmt diff time work set PK. */
	@EmbeddedId
	protected KshmtDiffTimeWorkSetPK kshmtDiffTimeWorkSetPK;

	/** The exclus ver. */
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The dt calc method. */
	@Column(name = "DT_CALC_METHOD")
	private int dtCalcMethod;

	/** The dt common rest set. */
	@Column(name = "DT_COMMON_REST_SET")
	private int dtCommonRestSet;

	/** The use half day. */
	@Column(name = "USE_HALF_DAY")
	private int useHalfDay;

	/** The ot set. */
	@Column(name = "OT_SET")
	private int otSet;

	/** The change ahead. */
	@Column(name = "CHANGE_AHEAD")
	private int changeAhead;

	/** The change behind. */
	@Column(name = "CHANGE_BEHIND")
	private int changeBehind;

	/** The front rear atr. */
	@Column(name = "FRONT_REAR_ATR")
	private int frontRearAtr;

	/** The time rounding unit. */
	@Column(name = "TIME_ROUNDING_UNIT")
	private int timeRoundingUnit;

	/** The lst kshmt dt half rest time. */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = true, updatable = true),
			@JoinColumn(name = "WORKTIME_CD", referencedColumnName = "WORKTIME_CD", insertable = true, updatable = true) })
	private List<KshmtDtHalfRestTime> lstKshmtDtHalfRestTime;//ok

	/** The lst kshmt dt work time set. */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = true, updatable = true),
			@JoinColumn(name = "WORKTIME_CD", referencedColumnName = "WORKTIME_CD", insertable = true, updatable = true) })
	private List<KshmtDtWorkTimeSet> lstKshmtDtWorkTimeSet;//ok

	/** The lst kshmt dt ot time set. */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = true, updatable = true),
			@JoinColumn(name = "WORKTIME_CD", referencedColumnName = "WORKTIME_CD", insertable = true, updatable = true) })
	private List<KshmtDtOtTimeSet> lstKshmtDtOtTimeSet;//ok

	/** The lst kshmt dt hol rest time. */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = true, updatable = true),
			@JoinColumn(name = "WORKTIME_CD", referencedColumnName = "WORKTIME_CD", insertable = true, updatable = true) })
	private List<KshmtDtHolRestTime> lstKshmtDtHolRestTime;//ok
	
	/** The lst kshmt diff time hol set. */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = true, updatable = true),
			@JoinColumn(name = "WORKTIME_CD", referencedColumnName = "WORKTIME_CD", insertable = true, updatable = true) })
	private List<KshmtDiffTimeHolSet> lstKshmtDiffTimeHolSet;//ok
	
	/** The lst kshmt dt stamp reflect. */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	@PrimaryKeyJoinColumns({ @PrimaryKeyJoinColumn(name = "CID", referencedColumnName = "CID"),
			@PrimaryKeyJoinColumn(name = "WORKTIME_CD", referencedColumnName = "WORKTIME_CD") })
	private KshmtDtStampReflect kshmtDtStampReflect;//ok
	
	/** The lst kshmt worktime common set. */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false),
		@JoinColumn(name = "WORKTIME_CD", referencedColumnName = "WORKTIME_CD", insertable = false, updatable = false)
	})
	private List<KshmtWorktimeCommonSet> lstKshmtWorktimeCommonSet;
	
	
	/**
	 * Instantiates a new kshmt diff time work set.
	 */
	public KshmtDiffTimeWorkSet() {
		super();
	}

	/**
	 * Gets the kshmt worktime common set.
	 *
	 * @return the kshmt worktime common set
	 */
	public KshmtWorktimeCommonSet getKshmtWorktimeCommonSet() {
		if (CollectionUtil.isEmpty(this.lstKshmtWorktimeCommonSet)) {
			this.lstKshmtWorktimeCommonSet = new ArrayList<KshmtWorktimeCommonSet>();
		}
		return this.lstKshmtWorktimeCommonSet.stream()
				.filter(entityCommon -> {
					KshmtWorktimeCommonSetPK pk = entityCommon.getKshmtWorktimeCommonSetPK();
					return pk.getWorkFormAtr() == WorkTimeDailyAtr.REGULAR_WORK.value
							&& pk.getWorktimeSetMethod() == WorkTimeMethodSet.DIFFTIME_WORK.value;
				})
				.findFirst()
				.orElse(null);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (kshmtDiffTimeWorkSetPK != null ? kshmtDiffTimeWorkSetPK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtDiffTimeWorkSet)) {
			return false;
		}
		KshmtDiffTimeWorkSet other = (KshmtDiffTimeWorkSet) object;
		if ((this.kshmtDiffTimeWorkSetPK == null && other.kshmtDiffTimeWorkSetPK != null)
				|| (this.kshmtDiffTimeWorkSetPK != null
						&& !this.kshmtDiffTimeWorkSetPK.equals(other.kshmtDiffTimeWorkSetPK))) {
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
		return this.kshmtDiffTimeWorkSetPK;
	}

}
