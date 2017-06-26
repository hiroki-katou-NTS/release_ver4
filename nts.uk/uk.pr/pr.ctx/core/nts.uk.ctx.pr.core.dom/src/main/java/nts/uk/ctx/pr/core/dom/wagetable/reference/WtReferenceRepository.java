/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.dom.wagetable.reference;

import java.util.List;

import nts.arc.time.YearMonth;

/**
 * The Interface WtReferenceRepository.
 */
public interface WtReferenceRepository {
	/**
	 * Gets the master ref item.
	 *
	 * @param masterRef the master ref
	 * @return the master ref item
	 */
	List<WtCodeRefItem> getMasterRefItem(WtMasterRef masterRef, YearMonth startMonth);

	/**
	 * Gets the code ref item.
	 *
	 * @param codeRef the code ref
	 * @return the code ref item
	 */
	List<WtCodeRefItem> getCodeRefItem(WtCodeRef codeRef);

}
