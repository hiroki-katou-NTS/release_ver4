package nts.uk.ctx.at.shared.dom.worktime.workplace.service;

import nts.uk.ctx.at.shared.dom.worktime.workplace.WorkTimeWorkplace;
import nts.uk.ctx.at.shared.dom.worktime.workplace.WorkTimeWorkplaceRepository;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * 就業時間帯割り当て済みの職場を取得する.
 */
@Stateless
public class WorkTimeWorkplaceService {

	/**
	 * 設定済みの職場を取得する
	 */
	public List<WorkTimeWorkplace> getByCid(Require require) {
		return require.getByCId();
	}

	public static interface Require {

		List<WorkTimeWorkplace> getByCId();

	}

}
