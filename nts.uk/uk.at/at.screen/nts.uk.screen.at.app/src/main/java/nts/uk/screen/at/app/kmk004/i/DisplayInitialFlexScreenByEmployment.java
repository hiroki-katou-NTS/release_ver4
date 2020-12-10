package nts.uk.screen.at.app.kmk004.i;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.flex.GetFlexPredWorkTimeRepository;
import nts.uk.ctx.at.shared.dom.scherec.statutory.worktime.monunit.MonthlyWorkTimeSet.LaborWorkTypeAttr;
import nts.uk.screen.at.app.kmk004.g.GetFlexPredWorkTimeDto;
import nts.uk.screen.at.app.query.kmk004.common.EmploymentList;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author sonnlb
 *
 *         UKDesign.UniversalK.就業.KDW_日別実績.KMK_計算マスタ.KMK004_法定労働時間の登録（New）.I：雇用別法定労働時間の登録（フレックス勤務）.メニュー別OCD.雇用別法定労働時間の登録（フレックス勤務）の初期画面を表示する
 */
@Stateless
public class DisplayInitialFlexScreenByEmployment {

	@Inject
	private GetFlexPredWorkTimeRepository getFlexPredWorkTimeRepo;

	@Inject
	private EmploymentList employmentList;

	@Inject
	private SelectEmploymentFlex selectEmploymentFlex;

	public DisplayInitialFlexScreenByEmploymentDto displayInitialFlexScreenByEmployment() {

		DisplayInitialFlexScreenByEmploymentDto result = new DisplayInitialFlexScreenByEmploymentDto();
		String comId = AppContexts.user().companyId();
		// 1.ログイン会社ID

		this.getFlexPredWorkTimeRepo.find(comId).ifPresent(x -> {
			result.setGetFlexPredWorkTime(GetFlexPredWorkTimeDto.fromDomain(x));
		});

		// 職場リストを表示する
		// input： 勤務区分 ← 2：フレックス勤務
		result.setEmploymentCds(this.employmentList.get(LaborWorkTypeAttr.FLEX));
		if (!result.getEmploymentCds().isEmpty()) {

			// 雇用を選択する（フレックス勤務）
			// input：
			// 雇用コード ← 雇用リストの先頭の雇用コード
			// 勤務区分 ← 2：フレックス勤務
			result.setSelectWorkPlaceFlex(this.selectEmploymentFlex
					.selectEmploymentFlex(result.getEmploymentCds().get(0).employmentCode, LaborWorkTypeAttr.FLEX));
		}

		return result;
	}

}
