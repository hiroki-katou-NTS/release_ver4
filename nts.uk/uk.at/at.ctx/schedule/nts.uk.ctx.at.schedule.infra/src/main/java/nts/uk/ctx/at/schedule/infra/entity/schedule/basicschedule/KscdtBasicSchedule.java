/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.schedule.infra.entity.schedule.basicschedule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 勤務予定基本情報 -The Class KscdtBasicSchedule.
 * 
 * @author sonnh1
 *
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "KSCDT_BASIC_SCHEDULE")
public class KscdtBasicSchedule extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The kscdp B schedule PK. */
	@EmbeddedId
	public KscdtBasicSchedulePK kscdpBSchedulePK;

	/** The work type code. */
	@Column(name = "WORKTYPE_CD")
	public String workTypeCode;

	/** The work time code. */
	@Column(name = "WORKTIME_CD")
	public String workTimeCode;

	/** The confirmed atr. */
	@Column(name = "CONFIRMED_ATR")
	public int confirmedAtr;

	/** The working day atr. */
	@Column(name = "WORKING_DAY_ATR")
	public int workingDayAtr;
	

	@Override
	protected Object getKey() {
		return this.kscdpBSchedulePK;
	}
}
