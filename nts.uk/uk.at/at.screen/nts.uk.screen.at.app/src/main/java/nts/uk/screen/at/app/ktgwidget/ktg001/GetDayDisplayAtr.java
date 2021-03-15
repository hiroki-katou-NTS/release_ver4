package nts.uk.screen.at.app.ktgwidget.ktg001;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.CheckTarget;
import nts.uk.ctx.at.record.dom.dailyperformanceprocessing.confirmationstatus.CheckTrackRecordApprovalDay;
import nts.uk.ctx.at.schedule.dom.plannedyearholiday.frame.NotUseAtr;
import nts.uk.ctx.at.shared.app.query.workrule.closure.ClosureIdPresentClosingPeriod;
import nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget.ApprovedAppStatusDetailedSetting;
import nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget.ApprovedApplicationStatusItem;

/**
 * UKDesign.UniversalK.就業.KTG_ウィジェット.KTG001_承認すべきデータ.アルゴリズム.2.日別実績の承認すべきデータの取得
 * 
 * @author tutt
 *
 */
@Stateless
public class GetDayDisplayAtr {
	
	@Inject
	private CheckTargetFinder checkTargetFinder;
	
	@Inject
	private CheckTrackRecordApprovalDay checkTrackRecordApprovalDay;

	// 日別実績の承認すべきデータの取得
	public Boolean get(List<ApprovedAppStatusDetailedSetting> approvedAppStatusDetailedSettingList,
			List<ClosureIdPresentClosingPeriod> closingPeriods, String employeeId, String companyId, int closureId) {

		ApprovedAppStatusDetailedSetting dailyPerformanceDataSetting = approvedAppStatusDetailedSettingList.stream()
				.filter(a -> a.getItem().value == ApprovedApplicationStatusItem.DAILY_PERFORMANCE_DATA.value)
				.collect(Collectors.toList()).get(0);

		if (dailyPerformanceDataSetting.getDisplayType().value == NotUseAtr.NOT_USE.value) {
			return false;

		} else {
			/*
			 * 利用する締め日を全てチェック対象とする 
			 * ※ユーザ固有情報のトップページ表示年月.締めIDをチェックして、Falseなら他の締めのチェックを行う
			 * ※結果がTrueとなった締めID、年月を保持する 日別/月別それぞれで保持
			 */
			ClosureIdPresentClosingPeriod closingPeriod = closingPeriods.stream().filter(f -> f.getClosureId().equals(closureId))
					.findAny().get();
			
			Integer yearMonth = closingPeriod.getCurrentClosingPeriod().getProcessingYm().v();

			// トップページの設定により対象年月と締めIDを取得する
			CheckTarget checkTarget = checkTargetFinder.getCheckTarget(closingPeriod, closureId, yearMonth);

			// 承認すべき日の実績があるかチェックする
			return checkTrackRecordApprovalDay.checkTrackRecordApprovalDayNew(companyId, employeeId, checkTarget);

		}
	}
}
