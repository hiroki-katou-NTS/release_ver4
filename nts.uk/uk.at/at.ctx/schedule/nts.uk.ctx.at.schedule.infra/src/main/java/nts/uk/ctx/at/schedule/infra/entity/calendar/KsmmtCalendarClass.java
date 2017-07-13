package nts.uk.ctx.at.schedule.infra.entity.calendar;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KSMMT_CALENDAR_CLASS")
@Setter
public class KsmmtCalendarClass extends UkJpaEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KsmmtCalendarClassPK ksmmtCalendarClassPK;
	
	@Column(name = "WORKING_DAY_ATR")
	public int workingDayAtr;	

	@Override
	protected Object getKey() {
		// TODO Auto-generated method stub
		return this.ksmmtCalendarClassPK;
	}
	
}
