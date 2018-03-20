package nts.uk.ctx.at.record.app.command.divergence.time.setting;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import nts.uk.ctx.at.record.dom.divergence.time.history.DivergenceType;
import nts.uk.ctx.at.record.dom.divergence.time.setting.DivergenceTimeErrorCancelMethod;
import nts.uk.ctx.at.record.dom.divergence.time.setting.DivergenceTimeGetMemento;
import nts.uk.ctx.at.record.dom.divergence.time.setting.DivergenceTimeName;
import nts.uk.ctx.at.record.dom.divergence.time.setting.DivergenceTimeUseSet;
import nts.uk.shr.com.context.AppContexts;

@AllArgsConstructor
public class DivergenceTimeSaveCommand implements DivergenceTimeGetMemento {

	/** The divergence time no. */

	private int divergenceTimeNo;

	/** The c id. */

	private String companyId;

	/** The Use classification. */

	private int divergenceTimeUseSet;

	/** The divergence time name. */

	private String divergenceTimeName;

	/** The divergence type. */

	private int divergenceType;

	/** The divergence time error cancel method. */

	private boolean reasonInput;

	/** The reason select. */
	private boolean reasonSelect;

	/** Attendance Item list. */
	private List<Double> attendanceId;

	/**
	 * Instantiates a new divergence time save command.
	 */
	public DivergenceTimeSaveCommand() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.divergence.time.setting.DivergenceTimeGetMemento
	 * #getDivTimeUseSet()
	 */
	@Override
	public DivergenceTimeUseSet getDivTimeUseSet() {
		return DivergenceTimeUseSet.valueOf(this.divergenceTimeUseSet);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.divergence.time.setting.DivergenceTimeGetMemento
	 * #getDivTimeName()
	 */
	@Override
	public DivergenceTimeName getDivTimeName() {
		return new DivergenceTimeName(this.divergenceTimeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.divergence.time.setting.DivergenceTimeGetMemento
	 * #getDivType()
	 */
	@Override
	public DivergenceType getDivType() {
		return DivergenceType.valueOf(this.divergenceType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.divergence.time.setting.DivergenceTimeGetMemento
	 * #getErrorCancelMedthod()
	 */
	@Override
	public DivergenceTimeErrorCancelMethod getErrorCancelMedthod() {
		DivergenceTimeErrorCancelMethod object = new DivergenceTimeErrorCancelMethod();

		object.setReasonInputed(this.reasonInput);
		object.setReasonSelected(this.reasonSelect);

		return object;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.divergence.time.setting.DivergenceTimeGetMemento
	 * #getTargetItems()
	 */
	@Override
	public List<Double> getTargetItems() {
		return this.attendanceId;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.divergence.time.setting.DivergenceTimeGetMemento
	 * #getDivergenceTimeNo()
	 */
	@Override
	public Integer getDivergenceTimeNo() {
		return this.divergenceTimeNo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.ctx.at.record.dom.divergence.time.setting.DivergenceTimeGetMemento
	 * #getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return AppContexts.user().companyId();
	}

}
