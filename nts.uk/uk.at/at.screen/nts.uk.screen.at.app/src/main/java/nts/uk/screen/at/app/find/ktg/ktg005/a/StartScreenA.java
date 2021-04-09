package nts.uk.screen.at.app.find.ktg.ktg005.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.request.dom.application.ApplicationRepository;
import nts.uk.ctx.at.request.dom.application.common.service.smartphone.output.DeadlineLimitCurrentMonth;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.service.AppDeadlineSettingGet;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.ctx.sys.auth.pub.role.RoleExportRepo;
import nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget.ApplicationStatusDetailedSetting;
import nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget.ApplicationStatusWidgetItem;
import nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget.ApproveWidgetRepository;
import nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget.StandardWidget;
import nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget.StandardWidgetType;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.com.i18n.TextResource;

/**
 * 
 * @author sonnlb UKDesign.UniversalK.就業.KTG_ウィジェット.KTG005_申請件数.ユースケース.起動する.起動する
 */
@Stateless
public class StartScreenA {

	@Inject
	private ClosureRepository closureRepository;

	@Inject
	private ClosureEmploymentRepository closureEmploymentRepo;

	@Inject
	private ShareEmploymentAdapter shareEmploymentAdapter;

	@Inject
	private AppDeadlineSettingGet appDeadlineSettingGet;

	@Inject
	private ApplicationRepository appRepo;

	@Inject
	private RoleExportRepo roleExportRepo;

	@Inject
	private ApproveWidgetRepository approveWidgetRepo;

	// 申請件数起動
	/**
	 * 
	 * @param companyId  会社ID
	 * @param employeeId 社員ID
	 * @return
	 */
	public ExecutionResultNumberOfApplicationDto startScreenA(String companyId, DatePeriod period, String employeeId) {

		// 指定するウィジェットの設定を取得する
		// Input :標準ウィジェット種別＝申請状況
		Optional<StandardWidget> standardWidgetOpt = approveWidgetRepo
				.findByWidgetTypeAndCompanyId(StandardWidgetType.APPLICATION_STATUS, companyId);
		
		List<ApplicationStatusDetailedSetting> applicationStatusDetailedSettings = new ArrayList<ApplicationStatusDetailedSetting>();
		DeadlineLimitCurrentMonth deadLine = new DeadlineLimitCurrentMonth(false);
		NumberOfAppDto number = new NumberOfAppDto();
		String topPagePartName = TextResource.localize("KTG005_15") ;
		
		// ログイン者が担当者か判断する

		boolean isEmployeeCharge = roleExportRepo.getWhetherLoginerCharge(AppContexts.user().roles())
				.isEmployeeCharge();
		
		if (!standardWidgetOpt.isPresent()) {
			// 「申請件数の実行結果」に初期値をセットする

			for (ApplicationStatusWidgetItem value : ApplicationStatusWidgetItem.values()) {
				applicationStatusDetailedSettings.add(new ApplicationStatusDetailedSetting(NotUseAtr.USE, value));
			}

			return new ExecutionResultNumberOfApplicationDto(applicationStatusDetailedSettings, deadLine, number,
					topPagePartName, isEmployeeCharge);
		}

			StandardWidget standardWidget = standardWidgetOpt.get();
			applicationStatusDetailedSettings = standardWidget.getAppStatusDetailedSettingList();
			topPagePartName = standardWidget.getName().v();
			val cacheCarrier = new CacheCarrier();

			// 取得した「申請状況の詳細設定」．表示区分をチェックする
			// Input :項目＝今月の申請締め切り日
			
			Optional<ApplicationStatusDetailedSetting> itemOpt = standardWidget.getAppStatusDetailedSettingList()
					.stream().filter(item -> item.getItem().equals(ApplicationStatusWidgetItem.MONTH_APP_DEADLINE))
					.findFirst();

			if (itemOpt.isPresent()) {
				// 社員(list)に対応する処理締めを取得する

				List<Closure> closures = ClosureService
						.getClosureDataByEmployees(
								ClosureService.createRequireM7(closureRepository, closureEmploymentRepo,
										shareEmploymentAdapter),
								cacheCarrier, Arrays.asList(employeeId), GeneralDate.today());
				// 申請締切設定を取得する
				if (closures.size() > 0) {
					Closure closure = closures.get(0);
					deadLine = appDeadlineSettingGet.getApplicationDeadline(companyId, employeeId,
							closure.getClosureId().value);

				}
			}
			// 項目＝承認された件数 OR
			// 項目＝未承認件数 OR
			// 項目＝否認された件数 OR
			// 項目＝差し戻し件数
			// => 項目＝! 今月の申請締め切り日
			
			List<ApplicationStatusDetailedSetting> usedList = standardWidget.getAppStatusDetailedSettingList().stream()
					.filter(x -> !x.getItem().equals(ApplicationStatusWidgetItem.MONTH_APP_DEADLINE))
					.collect(Collectors.toList());

			for (int i = 0; i < usedList.size(); i++) {
				// いずれが表示する
				ApplicationStatusDetailedSetting detailSetting = usedList.get(i);
				if (detailSetting.getDisplayType().equals(NotUseAtr.USE)) {
					// アルゴリズム「申請件数取得」を実行する
					number = GetNumberOfApps.getNumberOfApps(GetNumberOfApps.createRequire(appRepo), companyId, period,
							employeeId);
				}
			}
		
	

		return new ExecutionResultNumberOfApplicationDto(applicationStatusDetailedSettings, deadLine, number,
				topPagePartName, isEmployeeCharge);
	}
}
