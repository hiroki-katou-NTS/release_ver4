/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.entity.shift.estimate.commonset;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KscstEstGuideSetting.
 */
@Getter
@Setter
@Entity
@Table(name = "KSCST_EST_COM_SET")
public class KscstEstComSet extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cid. */
	@Id
	@Column(name = "CID")
	private String cid;

	/** The time yearly disp cond. */
	@Column(name = "TIME_YEARLY_DISP_COND")
	private int timeYearlyDispCond;

	/** The time monthly disp cond. */
	@Column(name = "TIME_MONTHLY_DISP_COND")
	private int timeMonthlyDispCond;

	/** The time alarm check cond. */
	@Column(name = "TIME_ALARM_CHECK_COND")
	private int timeAlarmCheckCond;

	/** The price yearly disp cond. */
	@Column(name = "PRICE_YEARLY_DISP_COND")
	private int priceYearlyDispCond;

	/** The price monthly disp cond. */
	@Column(name = "PRICE_MONTHLY_DISP_COND")
	private int priceMonthlyDispCond;

	/** The price alarm check cond. */
	@Column(name = "PRICE_ALARM_CHECK_COND")
	private int priceAlarmCheckCond;

	/** The num of day yearly disp cond. */
	@Column(name = "NUM_OF_DAY_YEARLY_DISP_COND")
	private int numOfDayYearlyDispCond;

	/** The num of day monthly disp cond. */
	@Column(name = "NUM_OF_DAY_MONTHLY_DISP_COND")
	private int numOfDayMonthlyDispCond;

	/** The num of day alarm check cond. */
	@Column(name = "NUM_OF_DAY_ALARM_CHECK_COND")
	private int numOfDayAlarmCheckCond;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "kscstEstGuideSetting", orphanRemoval = true)
	public List<KscstEstAlarmColor> kscstEstAlarmColors;

	/**
	 * Instantiates a new kscst est guide setting.
	 */
	public KscstEstComSet() {
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
		hash += (cid != null ? cid.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KscstEstComSet)) {
			return false;
		}
		KscstEstComSet other = (KscstEstComSet) object;
		if ((this.cid == null && other.cid != null)
				|| (this.cid != null && !this.cid.equals(other.cid))) {
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
		return this.cid;
	}

}
