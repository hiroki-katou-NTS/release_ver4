package nts.uk.ctx.at.record.dom.divergence.time.message;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class WorkTypeDivergenceTimeErrorAlarmMessage.
 */
@Getter
@Setter
// 勤務種別ごとの乖離時間のエラーアラームメッセージ
public class WorkTypeDivergenceTimeErrorAlarmMessage extends AggregateRoot {

	/** The c id. */
	// 会社ID
	private CompanyId cId;

	/** The work type code. */
	// 勤務種別コード
	private BusinessTypeCode workTypeCode;

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
	 * Instantiates a new work type divergence time error alarm message.
	 */
	public WorkTypeDivergenceTimeErrorAlarmMessage(){
		super();
	}

	/**
	 * Instantiates a new divergence time error alarm message.
	 *
	 * @param memento
	 *            the memento
	 */
	public WorkTypeDivergenceTimeErrorAlarmMessage(WorkTypeDivergenceTimeErrorAlarmMessageGetMemento memento) {
		this.cId = memento.getCId();
		this.divergenceTimeNo = memento.getDivergenceTimeNo();
		this.workTypeCode = memento.getWorkTypeCode();
		this.alarmMessage = memento.getAlarmMessage();
		this.errorMessage = memento.getErrorMessage();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(WorkTypeDivergenceTimeErrorAlarmMessageSetMemento memento) {
		memento.setCId(this.cId);
		memento.setDivergenceTimeNo(this.divergenceTimeNo);
		memento.setWorkTypeCode(this.workTypeCode);
		memento.setAlarmMessage(this.alarmMessage);
		memento.setErrorMessage(this.errorMessage);
	}

	/**
	 * Generate message.
	 *
	 * @param time the time
	 * @param messageType the message type
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
		result = prime * result + ((workTypeCode == null) ? 0 : workTypeCode.hashCode());
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
		WorkTypeDivergenceTimeErrorAlarmMessage other = (WorkTypeDivergenceTimeErrorAlarmMessage) obj;
		if (divergenceTimeNo != other.divergenceTimeNo)
			return false;
		if (cId == null) {
			if (other.cId != null)
				return false;
		} else if (!cId.equals(other.cId))
			return false;
		if (workTypeCode == null) {
			if (other.workTypeCode != null)
				return false;
		} else if (!workTypeCode.equals(other.workTypeCode))
			return false;
		return true;
	}
}
