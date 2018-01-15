/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.pub.workrule.closure;

import java.util.Optional;

/**
 * The Interface ShWorkTimeSettingPub.
 */
public interface ShClosurePub {

	/**
	 * Find.
	 *
	 * @param cId the c id
	 * @param closureId the closure id
	 * @return the optional
	 */
	// 処理年月と締め期間を取得する
	Optional<PresentClosingPeriodExport> find(String cId, int closureId);
}
