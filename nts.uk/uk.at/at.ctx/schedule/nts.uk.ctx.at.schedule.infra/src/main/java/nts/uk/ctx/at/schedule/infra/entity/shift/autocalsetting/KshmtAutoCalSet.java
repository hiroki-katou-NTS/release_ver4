/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.entity.shift.autocalsetting;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * The Class KshmtAutoComCalSet.
 */

@Setter
@Getter
@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class KshmtAutoCalSet extends UkJpaEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The early ot time limit. */
	@Column(name = "EARLY_OT_TIME_LIMIT")
	private int earlyOtTimeLimit;

	/** The early mid ot time limit. */
	@Column(name = "EARLY_MID_OT_TIME_LIMIT")
	private int earlyMidOtTimeLimit;

	/** The normal ot time limit. */
	@Column(name = "NORMAL_OT_TIME_LIMIT")
	private int normalOtTimeLimit;

	/** The normal mid ot time limit. */
	@Column(name = "NORMAL_MID_OT_TIME_LIMIT")
	private int normalMidOtTimeLimit;

	/** The legal ot time limit. */
	@Column(name = "LEGAL_OT_TIME_LIMIT")
	private int legalOtTimeLimit;

	/** The legal mid ot time limit. */
	@Column(name = "LEGAL_MID_OT_TIME_LIMIT")
	private int legalMidOtTimeLimit;

	/** The flex ot time limit. */
	@Column(name = "FLEX_OT_TIME_LIMIT")
	private int flexOtTimeLimit;

	/** The flex ot night time limit. */
	@Column(name = "FLEX_OT_NIGHT_TIME_LIMIT")
	private int flexOtNightTimeLimit;

	/** The rest time limit. */
	@Column(name = "REST_TIME_LIMIT")
	private int restTimeLimit;

	/** The late night time limit. */
	@Column(name = "LATE_NIGHT_TIME_LIMIT")
	private int lateNightTimeLimit;

	/** The early ot time atr. */
	@Column(name = "EARLY_OT_TIME_ATR")
	private int earlyOtTimeAtr;

	/** The early mid ot time atr. */
	@Column(name = "EARLY_MID_OT_TIME_ATR")
	private int earlyMidOtTimeAtr;

	/** The normal ot time atr. */
	@Column(name = "NORMAL_OT_TIME_ATR")
	private int normalOtTimeAtr;

	/** The normal mid ot time atr. */
	@Column(name = "NORMAL_MID_OT_TIME_ATR")
	private int normalMidOtTimeAtr;

	/** The legal ot time atr. */
	@Column(name = "LEGAL_OT_TIME_ATR")
	private int legalOtTimeAtr;

	/** The legal mid ot time atr. */
	@Column(name = "LEGAL_MID_OT_TIME_ATR")
	private int legalMidOtTimeAtr;

	/** The flex ot time atr. */
	@Column(name = "FLEX_OT_TIME_ATR")
	private int flexOtTimeAtr;

	/** The flex ot night time atr. */
	@Column(name = "FLEX_OT_NIGHT_TIME_ATR")
	private int flexOtNightTimeAtr;

	/** The rest time atr. */
	@Column(name = "REST_TIME_ATR")
	private int restTimeAtr;

	/** The late night time atr. */
	@Column(name = "LATE_NIGHT_TIME_ATR")
	private int lateNightTimeAtr;

}
