package nts.uk.ctx.at.record.app.find.divergence.time;

import java.util.List;

import lombok.Data;

@Data
public class DivergenceTimeInputMethodDto {

	/** The divergence time no. */

	private int divergenceTimeNo;

	/** The c id. */

	private String companyId;

	/** The Use classification. */

	private int divergenceTimeUseSet;

	/** The divergence time name. */

	private String divergenceTimeName;

	/** The divergence type. */

	private String divergenceType;

	/** The divergence time error cancel method. */

	private boolean reasonInput;

	/** The reason select. */
	private boolean reasonSelect;

	/** The divergence reason inputed. */

	private boolean divergenceReasonInputed;

	/** The divergence reason selected. */

	private boolean divergenceReasonSelected;

	/** Attendance Item list. */
	private List<Integer> targetItems;

	/**
	 * Instantiates a new divergence time input method dto.
	 */
	public DivergenceTimeInputMethodDto() {
		super();
	}

	/**
	 * Instantiates a new divergence time input method dto.
	 *
	 * @param divergenceTimeNo
	 *            the divergence time no
	 * @param companyId
	 *            the company id
	 * @param divTimeUseSet
	 *            the div time use set
	 * @param divergenceTimeName
	 *            the divergence time name
	 * @param divType
	 *            the div type
	 * @param reasonInput
	 *            the reason input
	 * @param reasonSelect
	 *            the reason select
	 * @param divergenceReasonInputed
	 *            the divergence reason inputed
	 * @param divergenceReasonSelected
	 *            the divergence reason selected
	 * @param attendanceId
	 *            the attendance id
	 */
	public DivergenceTimeInputMethodDto(int divergenceTimeNo, String companyId, int divTimeUseSet,
			String divergenceTimeName, String divType, boolean reasonInput, boolean reasonSelect,
			boolean divergenceReasonInputed, boolean divergenceReasonSelected, List<Integer> attendanceId) {
		super();
		this.divergenceTimeNo = divergenceTimeNo;
		this.companyId = companyId;
		this.divergenceTimeUseSet = divTimeUseSet;
		this.divergenceTimeName = divergenceTimeName;
		this.divergenceType = divType;
		this.reasonInput = reasonInput;
		this.reasonSelect = reasonSelect;
		this.divergenceReasonInputed = divergenceReasonInputed;
		this.divergenceReasonSelected = divergenceReasonSelected;
		this.targetItems = attendanceId;
	}

}
