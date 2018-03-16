/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.vacation.setting.acquisitionrule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KmfstAcquisitionRule.
 */
@Getter
@Setter
@Entity
@Table(name = "KARST_ACQUISITION_RULE")
public class KarstAcquisitionRule extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The cid. */
	@Id
	@Column(name = "CID")
	private String cid;

	/** The annual paid. */
	@Column(name = "ANNUAL_PAID")
	private int annualPaid;

	/** 代休を優先 */
	@Column(name = "COMPENSATORY_DAY_OFF")
	private int compensatoryDayOff;

	/** 振休を優先 */
	@Column(name = "SABSTITUTE_HOLIDAY")
	private int sabstituteHoliday;

	/** 60H超休を優先 */
	@Column(name = "FUNDED_PAID_HOLIDAY")
	private int fundedPaidHoliday;
	
	/** 代休を優先  */
	@Column(name = "EXCESS_HOLIDAY")
	private int excessHoliday;

	/** 60H超休を優先超休を優先  */
	@Column(name = "OVERRIDE_HOLIDAY")
	private int overrideHoliday;
	
	/** The setting classfication. */
	@Column(name = "MANAGE_ATR")
	private int category;

	public KarstAcquisitionRule() {
		super();
	}

	/**
	 * Instantiates a new kmfst acquisition rule.
	 *
	 * @param cid
	 *            the cid
	 */
	public KarstAcquisitionRule(String cid) {
		this.cid = cid;
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
		if (!(object instanceof KarstAcquisitionRule)) {
			return false;
		}
		KarstAcquisitionRule other = (KarstAcquisitionRule) object;
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