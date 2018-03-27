/*
 * 
 */
package nts.uk.ctx.at.record.app.find.divergence.time.message;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.divergence.time.message.ErrorAlarmMessage;
import nts.uk.ctx.at.record.dom.divergence.time.message.WorkTypeDivergenceTimeErrorAlarmMessageSetMemento;
import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Class WorkTypeDivergenceTimeErrorAlarmMessageDto.
 */
@Getter
@Setter
@AllArgsConstructor
public class WorkTypeDivergenceTimeErrorAlarmMessageDto implements WorkTypeDivergenceTimeErrorAlarmMessageSetMemento {

	/** The company id. */
	private CompanyId companyId;

	/** The work type code. */
	private String workTypeCode;

	/** The divergence time no. */
	private Integer divergenceTimeNo;

	/** The alarm message. */
	private String alarmMessage;

	/** The error message. */
	private String errorMessage;

	/**
	 * Instantiates a new work type divergence time error alarm message dto.
	 */
	public WorkTypeDivergenceTimeErrorAlarmMessageDto() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * WorkTypeDivergenceTimeErrorAlarmMessageSetMemento#setCId(nts.uk.ctx.at.
	 * shared.dom.common.CompanyId)
	 */
	@Override
	public void setCId(CompanyId cId) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * WorkTypeDivergenceTimeErrorAlarmMessageSetMemento#setWorkTypeCode(nts.uk.
	 * ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode)
	 */
	@Override
	public void setWorkTypeCode(BusinessTypeCode workTypeCode) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * WorkTypeDivergenceTimeErrorAlarmMessageSetMemento#setDivergenceTimeNo(
	 * java.lang.Integer)
	 */
	@Override
	public void setDivergenceTimeNo(Integer divergenceTimeNo) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * WorkTypeDivergenceTimeErrorAlarmMessageSetMemento#setAlarmMessage(java.
	 * util.Optional)
	 */
	@Override
	public void setAlarmMessage(Optional<ErrorAlarmMessage> alarmMessage) {
		this.alarmMessage = alarmMessage.get().v();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.message.
	 * WorkTypeDivergenceTimeErrorAlarmMessageSetMemento#setErrorMessage(java.
	 * util.Optional)
	 */
	@Override
	public void setErrorMessage(Optional<ErrorAlarmMessage> errorMessage) {
		this.errorMessage = errorMessage.get().v();
	}
}
