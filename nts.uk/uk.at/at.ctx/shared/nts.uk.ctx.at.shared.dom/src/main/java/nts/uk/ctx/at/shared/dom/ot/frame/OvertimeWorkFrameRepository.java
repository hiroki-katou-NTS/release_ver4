/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.ot.frame;

import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.shared.dom.common.CompanyId;

/**
 * The Interface OvertimeWorkFrameRepository.
 */
public interface OvertimeWorkFrameRepository {
	
	/**
	 * Find overtime work frame.
	 *
	 * @param companyId the company id
	 * @param overtimeWorkFrNo the overtime work fr no
	 * @return the optional
	 */
	Optional<OvertimeWorkFrame> findOvertimeWorkFrame(CompanyId companyId, int overtimeWorkFrNo);
	
	/**
	 * Add.
	 *
	 * @param overtimeWorkFrame the overtime work frame
	 */
	void add(OvertimeWorkFrame overtimeWorkFrame);
	
	/**
	 * Update.
	 *
	 * @param overtimeWorkFrame the overtime work frame
	 */
	void update(OvertimeWorkFrame overtimeWorkFrame);
	
	
	/**
	 * Gets the all overtime work frame.
	 *
	 * @param companyId the company id
	 * @return the all overtime work frame
	 */
	List<OvertimeWorkFrame> getAllOvertimeWorkFrame(String companyId);
}
