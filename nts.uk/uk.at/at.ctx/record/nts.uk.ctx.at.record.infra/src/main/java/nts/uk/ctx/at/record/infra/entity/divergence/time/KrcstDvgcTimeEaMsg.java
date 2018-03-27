/*
 * 
 */
package nts.uk.ctx.at.record.infra.entity.divergence.time;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.uk.shr.infra.data.entity.UkJpaEntity;


/**
 * The persistent class for the KRCST_DVGC_TIME_EA_MSG database table.
 * 
 */
@Getter
@Setter
@Entity
@Table(name="KRCST_DVGC_TIME_EA_MSG")
public class KrcstDvgcTimeEaMsg extends UkJpaEntity implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@EmbeddedId
	private KrcstDvgcTimeEaMsgPK id;

	/** The alarm message. */
	@Column(name="ALARM_MESSAGE")
	private String alarmMessage;

	/** The error message. */
	@Column(name="ERROR_MESSAGE")
	private String errorMessage;

	/* (non-Javadoc)
	 * @see nts.arc.layer.infra.data.entity.JpaEntity#getKey()
	 */
	@Override
	protected Object getKey() {
		return this.id;
	}
}