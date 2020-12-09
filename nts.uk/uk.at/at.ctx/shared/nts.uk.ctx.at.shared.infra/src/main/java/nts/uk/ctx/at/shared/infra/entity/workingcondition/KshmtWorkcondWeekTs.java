/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.entity.workingcondition;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.workingcondition.NotUseAtr;
import nts.uk.ctx.at.shared.dom.workingcondition.SingleDaySchedule;
import nts.uk.ctx.at.shared.dom.workingcondition.TimeZone;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;

/**
 * The Class KshmtWorkcondWeekTs.
 */
@Getter
@Setter
@Entity
@Table(name = "KSHMT_DAYOFWEEK_TIME_ZONE")
public class KshmtWorkcondWeekTs extends KshmtTimeZone implements Serializable {


	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The kshmt dayofweek time zone PK. */
	@EmbeddedId
	protected KshmtDayofweekTimeZonePK kshmtDayofweekTimeZonePK;

	/**
	 * Instantiates a new kshmt dayofweek time zone.
	 */
	public KshmtWorkcondWeekTs() {
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
		hash += (kshmtDayofweekTimeZonePK != null ? kshmtDayofweekTimeZonePK.hashCode() : 0);
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof KshmtWorkcondWeekTs)) {
			return false;
		}
		KshmtWorkcondWeekTs other = (KshmtWorkcondWeekTs) object;
		if ((this.kshmtDayofweekTimeZonePK == null && other.kshmtDayofweekTimeZonePK != null)
				|| (this.kshmtDayofweekTimeZonePK != null
						&& !this.kshmtDayofweekTimeZonePK.equals(other.kshmtDayofweekTimeZonePK))) {
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
		return this.kshmtDayofweekTimeZonePK;
	}
	
	public TimeZone toDomain() {
		return new TimeZone(NotUseAtr.valueOf(this.getUseAtr()), this.kshmtDayofweekTimeZonePK.getCnt(), this.getStartTime(), this.getEndTime());
		
	}
	
	public static KshmtWorkcondWeekTs toEntity(TimeZone timeZone,String historyId,int perWorkDayOffAtr) {
		KshmtWorkcondWeekTs data =  new KshmtWorkcondWeekTs(new KshmtDayofweekTimeZonePK(historyId, perWorkDayOffAtr, timeZone.getCnt()));
		data.setUseAtr(timeZone.getUseAtr().value);
		data.setStartTime(timeZone.getStart().valueAsMinutes());
		data.setEndTime(timeZone.getEnd().valueAsMinutes());
		return data;
	}
	
	public KshmtWorkcondWeekTs(KshmtDayofweekTimeZonePK kshmtDayofweekTimeZonePK) {
		super();
		this.kshmtDayofweekTimeZonePK = kshmtDayofweekTimeZonePK;
	}

}
