/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.workingcondition;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtWorkingCondItem.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_WORKING_COND_ITEM")
public class KshmtWorkingCondItem extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The exclus ver. */
	@Column(name = "EXCLUS_VER")
	private int exclusVer;

	/** The history id. */
	@Id
	@Column(name = "HIST_ID")
	private String historyId;

	/** The sid. */
	@Column(name = "SID")
	private String sid;
	
	/** The hourly pay atr */
	@Column(name = "HOURLY_PAY_ATR")
	private Integer hourlyPayAtr;

	/** The sche management atr. */
	@Column(name = "SCHE_MANAGEMENT_ATR")
	private int scheManagementAtr;

	/** The auto stamp set atr. */
	@Column(name = "AUTO_STAMP_SET_ATR")
	private int autoStampSetAtr;

	/** The auto interval set atr. */
	@Column(name = "AUTO_INTERVAL_SET_ATR")
	private int autoIntervalSetAtr;

	/** The vacation add time atr. */
	@Column(name = "VACATION_ADD_TIME_ATR")
	private int vacationAddTimeAtr;

	/** The contract time. */
	@Column(name = "CONTRACT_TIME")
	private int contractTime;

	/** The labor sys. */
	@Column(name = "LABOR_SYS")
	private int laborSys;

	/** The hd add time one day. */
	@Column(name = "HD_ADD_TIME_ONE_DAY")
	private Integer hdAddTimeOneDay;

	/** The hd add time morning. */
	@Column(name = "HD_ADD_TIME_MORNING")
	private Integer hdAddTimeMorning;

	/** The hd add time afternoon. */
	@Column(name = "HD_ADD_TIME_AFTERNOON")
	private Integer hdAddTimeAfternoon;
	
	/** The time apply. */
	@Column(name = "TIME_APPLY")
	private String timeApply;
	
	/** The monthly pattern. */
	@Column(name = "MONTHLY_PATTERN")
	private String monthlyPattern;

	/** The kshmt working cond item. */
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumns({
			@PrimaryKeyJoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID") })
	private KshmtWorkingCond kshmtWorkingCond;

	/** The kshmt working cond items. */
	@JoinColumns({
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = false, updatable = false) })
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private KshmtScheduleMethod kshmtScheduleMethod;

	/** The kshmt per work cats. */
	@JoinColumns({
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = true, updatable = true) })
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<KshmtPerWorkCat> kshmtPerWorkCats;

	/** The kshmt personal day of weeks. */
	@JoinColumns({
			@JoinColumn(name = "HIST_ID", referencedColumnName = "HIST_ID", insertable = true, updatable = true) })
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<KshmtPersonalDayOfWeek> kshmtPersonalDayOfWeeks;

	
	public static final JpaEntityMapper<KshmtWorkingCondItem> MAPPER = new JpaEntityMapper<>(KshmtWorkingCondItem.class); 
	
	
	/**
	 * Instantiates a new kshmt working cond item.
	 */
	public KshmtWorkingCondItem() {
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
		hash += (historyId != null ? historyId.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtWorkingCondItem)) {
			return false;
		}
		KshmtWorkingCondItem other = (KshmtWorkingCondItem) object;
		if ((this.historyId == null && other.historyId != null)
				|| (this.historyId != null && !this.historyId.equals(other.historyId))) {
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
		return this.historyId;
	}

}
