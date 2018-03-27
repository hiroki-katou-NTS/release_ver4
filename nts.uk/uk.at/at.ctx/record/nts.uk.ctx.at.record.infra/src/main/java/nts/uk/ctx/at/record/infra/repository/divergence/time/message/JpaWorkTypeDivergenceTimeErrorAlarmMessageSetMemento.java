package nts.uk.ctx.at.record.infra.repository.divergence.time.message;

import java.util.Optional;

import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.message.ErrorAlarmMessage;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageSetMemento;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstWtdvgcTimeEaMsg;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstWtdvgcTimeEaMsgPK;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class JpaWorkTypeDivergenceTimeErrorAlarmMessageSetMemento.
 */
public class JpaWorkTypeDivergenceTimeErrorAlarmMessageSetMemento
		implements WorkTypeDivergenceTimeErrorAlarmMessageSetMemento {

	/** The entity. */
	private KrcstWtdvgcTimeEaMsg entity;

	/**
	 * Instantiates a new jpa work type divergence time error alarm message set
	 * memento.
	 */
	public JpaWorkTypeDivergenceTimeErrorAlarmMessageSetMemento() {
	}

	/**
	 * Instantiates a new jpa work type divergence time error alarm message set
	 * memento.
	 *
	 * @param entity
	 *            the entity
	 */
	public JpaWorkTypeDivergenceTimeErrorAlarmMessageSetMemento(KrcstWtdvgcTimeEaMsg entity) {
		this.entity = entity;
	}

	/**
	 * Sets the c id.
	 *
	 * @param cId
	 *            the new c id
	 */
	@Override
	public void setCId(CompanyId cId) {
		KrcstWtdvgcTimeEaMsgPK pk = new KrcstWtdvgcTimeEaMsgPK();
		pk.setCid(cId.v());

		this.entity.setId(pk);
	}

	/**
	 * Sets the work type code.
	 *
	 * @param workTypeCode
	 *            the new work type code
	 */
	@Override
	public void setWorkTypeCode(BusinessTypeCode workTypeCode) {
		this.entity.getId().setWorktypeCd(workTypeCode.v());
	}

	/**
	 * Sets the divergence time no.
	 *
	 * @param divergenceTimeNo
	 *            the new divergence time no
	 */
	@Override
	public void setDivergenceTimeNo(Integer divergenceTimeNo) {
		this.entity.getId().setDvgcTimeNo(divergenceTimeNo);
	}

	/**
	 * Sets the alarm message.
	 *
	 * @param alarmMessage
	 *            the new alarm message
	 */
	@Override
	public void setAlarmMessage(Optional<ErrorAlarmMessage> alarmMessage) {
		this.entity.setAlarmMessage(alarmMessage.isPresent() ? alarmMessage.get().v() : null);
	}

	/**
	 * Sets the error message.
	 *
	 * @param errorMessage
	 *            the new error message
	 */
	@Override
	public void setErrorMessage(Optional<ErrorAlarmMessage> errorMessage) {
		this.entity.setErrorMessage(errorMessage.isPresent() ? errorMessage.get().v() : null);
	}

}
