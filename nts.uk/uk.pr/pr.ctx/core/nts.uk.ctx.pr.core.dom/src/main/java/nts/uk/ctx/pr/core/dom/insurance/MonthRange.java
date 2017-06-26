/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.insurance;

import java.io.Serializable;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.time.DateTimeConstraints;
import nts.arc.time.YearMonth;
import nts.uk.ctx.core.dom.util.PrimitiveUtil;

/**
 * The Class MonthRange.
 */
@Getter
public class MonthRange implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4492995458816732355L;

	/** The start month. */
	private YearMonth startMonth;

	/** The end month. */
	private YearMonth endMonth;

	/**
	 * Instantiates a new month range.
	 *
	 * @param startMonth
	 *            the start month
	 * @param endMonth
	 *            the end month
	 */
	private MonthRange(YearMonth startMonth, YearMonth endMonth) {
		super();

		// Check required.
		if (startMonth == null || endMonth == null) {
			throw new BusinessException("ER001");
		}

		this.startMonth = startMonth;
		this.endMonth = endMonth;
	}

	/**
	 * Forward.
	 *
	 * @param startMonth
	 *            the start month
	 * @return the month range
	 */
	public static MonthRange toMaxDate(YearMonth startMonth) {
		return new MonthRange(startMonth, YearMonth.of(DateTimeConstraints.LIMIT_YEAR.max(),
				DateTimeConstraints.LIMIT_MONTH.max()));
	}

	/**
	 * Behind.
	 *
	 * @param endMonth
	 *            the end month
	 * @return the month range
	 */
	public static MonthRange toMinDate(YearMonth endMonth) {
		return new MonthRange(YearMonth.of(DateTimeConstraints.LIMIT_YEAR.min(),
				DateTimeConstraints.LIMIT_MONTH.min()), endMonth);
	}

	/**
	 * New range.
	 *
	 * @param startMonth
	 *            the start month
	 * @param endMonth
	 *            the end month
	 * @return the month range
	 */
	public static MonthRange range(YearMonth startMonth, YearMonth endMonth) {
		return new MonthRange(startMonth, endMonth);
	}

	/**
	 * Range.
	 *
	 * @param startMonth
	 *            the start month
	 * @param endMonth
	 *            the end month
	 * @param separatorChar
	 *            the separator char
	 * @return the month range
	 */
	public static MonthRange range(String startMonth, String endMonth, String separatorChar) {
		return new MonthRange(PrimitiveUtil.toYearMonth(startMonth, separatorChar),
				PrimitiveUtil.toYearMonth(endMonth, separatorChar));
	}
}
