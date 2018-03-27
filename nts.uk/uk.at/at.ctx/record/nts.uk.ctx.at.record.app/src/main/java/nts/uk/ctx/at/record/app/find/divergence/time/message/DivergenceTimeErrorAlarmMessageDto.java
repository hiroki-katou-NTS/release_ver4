package nts.uk.ctx.at.record.app.find.divergence.time.message;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.divergence.time.message.DivergenceTimeErrorAlarmMessageSetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.message.ErrorAlarmMessage;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class DivergenceTimeErrorAlarmMessageDto.
 */
@Getter
@Setter
@AllArgsConstructor
public class DivergenceTimeErrorAlarmMessageDto implements DivergenceTimeErrorAlarmMessageSetMemento {

	/** The company id. */
	private CompanyId companyId;

	/** The divergence time no. */
	private Integer divergenceTimeNo;

	/** The alarm message. */
	private String alarmMessage;

	/** The error message. */
	private String errorMessage;

	/**
	 * Instantiates a new divergence time error alarm message dto.
	 */
	public DivergenceTimeErrorAlarmMessageDto() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * DivergenceTimeErrorAlarmMessageSetMemento#setCId(nts.uk.ctx.at.shared.dom
	 * .common.CompanyId)
	 */
	@Override
	public void setCId(CompanyId cId) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * DivergenceTimeErrorAlarmMessageSetMemento#setAlarmMessage(java.util.
	 * Optional)
	 */
	@Override
	public void setAlarmMessage(Optional<ErrorAlarmMessage> alarmMessage) {
		this.alarmMessage = alarmMessage.get().v();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * DivergenceTimeErrorAlarmMessageSetMemento#setErrorMessage(java.util.
	 * Optional)
	 */
	@Override
	public void setErrorMessage(Optional<ErrorAlarmMessage> errorMessage) {
		this.errorMessage = errorMessage.get().v();
	}

}
