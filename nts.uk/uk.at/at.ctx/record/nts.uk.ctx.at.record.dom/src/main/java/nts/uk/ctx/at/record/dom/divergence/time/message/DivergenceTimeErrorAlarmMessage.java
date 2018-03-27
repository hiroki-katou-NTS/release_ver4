package nts.uk.ctx.at.record.dom.divergence.time.message;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * Gets the error message.
 *
 * @return the error message
 */
@Getter
@Setter
// 乖離時間のエラーアラームメッセージ
public class DivergenceTimeErrorAlarmMessage extends AggregateRoot {
	/** The c id. */
	// 会社ID
	private CompanyId cId;

	/** The divergence time no. */
	// 乖離時間NO
	private Integer divergenceTimeNo;

	/** The alarm message. */
	// アラームメッセージ
	private Optional<ErrorAlarmMessage> alarmMessage;

	/** The error message. */
	// エラーメッセージ
	private Optional<ErrorAlarmMessage> errorMessage;

	/**
	 * Instantiates a new divergence time error alarm message.
	 */
	public DivergenceTimeErrorAlarmMessage() {
		super();
	}

	/**
	 * Instantiates a new divergence time error alarm message.
	 *
	 * @param memento
	 *            the memento
	 */
	public DivergenceTimeErrorAlarmMessage(DivergenceTimeErrorAlarmMessageGetMemento memento) {
		this.cId = memento.getCId();
		this.divergenceTimeNo = memento.getDivergenceTimeNo();
		this.alarmMessage = memento.getAlarmMessage();
		this.errorMessage = memento.getErrorMessage();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(DivergenceTimeErrorAlarmMessageSetMemento memento) {
		memento.setCId(this.cId);
		memento.setDivergenceTimeNo(this.divergenceTimeNo);
		memento.setAlarmMessage(this.alarmMessage);
		memento.setErrorMessage(this.errorMessage);
	}

	/**
	 * Generate message.
	 *
	 * @param time
	 *            the time
	 * @param messageType
	 *            the message type
	 */
	public void generateMessage(Integer time, MessageType messageType) {
		String message = messageType.isAlarm() ? this.alarmMessage.get().v() : this.errorMessage.get().v();
		throw new BusinessException("Msg_1062", time.toString(), message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((divergenceTimeNo == null) ? 0 : divergenceTimeNo.hashCode());
		result = prime * result + ((cId == null) ? 0 : cId.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DivergenceTimeErrorAlarmMessage other = (DivergenceTimeErrorAlarmMessage) obj;
		if (divergenceTimeNo != other.divergenceTimeNo)
			return false;
		if (cId == null) {
			if (other.cId != null)
				return false;
		} else if (!cId.equals(other.cId))
			return false;
		return true;
	}
}
