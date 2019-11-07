package nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.createperapprovaldaily;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.adapter.company.AffCompanyHistImport;
import nts.uk.ctx.at.record.dom.adapter.company.SyCompanyRecordAdapter;
import nts.uk.ctx.at.record.dom.adapter.createdailyapprover.AppRootInsContentFnImport;
import nts.uk.ctx.at.record.dom.adapter.createdailyapprover.CreateDailyApproverAdapter;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.AppDataInfoDaily;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.AppDataInfoDailyRepository;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.AppInterrupDaily;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.AppInterrupDailyRepository;
import nts.uk.ctx.at.record.dom.workrecord.actualsituation.createapproval.dailyperformance.ErrorMessageRC;
import nts.uk.shr.com.i18n.TextResource;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class CreateperApprovalDailyDefault implements CreateperApprovalDailyService {

	@Inject
	private SyCompanyRecordAdapter syCompanyRecordAdapter;

	@Inject

	private AppDataInfoDailyRepository appDataInfoDailyRepo;
	@Inject
	private CreateDailyApproverAdapter createDailyApproverAdapter;

	@Inject
	private ManagedParallelWithContext parallel;

	@Inject
	private AppInterrupDailyRepository appInterrupDailyRepository;

	@Override
	public OutputCreatePerApprovalDaily createperApprovalDaily(String companyId, String executionId,
			List<String> employeeIDs, int processExecType, Integer createNewEmp, GeneralDate startDateClosure,
			GeneralDate endDateClosure) {
		AtomicBoolean checkStop = new AtomicBoolean(false);
		/** パラメータ.実行種別をチェック */
<<<<<<< HEAD
		// 通常実行の場合 : processExecType = 0(通常実行) - 再作成の場合 : processExecType = 1(再作成)
		// RequestList211
		List<AffCompanyHistImport> listAffCompanyHistImport = syCompanyRecordAdapter
				.getAffCompanyHistByEmployee(employeeIDs, new DatePeriod(startDateClosure, GeneralDate.today()));
		this.parallel.forEach(employeeIDs, employeeID -> {
			if (checkStop.get())
				return;
			// 年月日 ←「システム日付の前日」
			GeneralDate ymd = GeneralDate.today().addDays(-1);
			if (createNewEmp == 1) {
				/** Imported「所属会社履歴（社員別）」を取得する(lấy thông tin Imported「所属会社履歴（社員別）」) */
				AffCompanyHistImport affComHist = new AffCompanyHistImport();
				boolean checkAffComHist = false;
				for (AffCompanyHistImport affCompanyHistImport : listAffCompanyHistImport) {
					if (affCompanyHistImport.getEmployeeId().equals(employeeID)) {
						affComHist = affCompanyHistImport;
						checkAffComHist = true;
						break;
=======
		// 通常実行の場合 : processExecType = 0(通常実行) - // 再作成の場合 : processExecType = 1(再作成)
//		if (processExecType == 0) {
			// RequestList211
			List<AffCompanyHistImport> listAffCompanyHistImport = syCompanyRecordAdapter
					.getAffCompanyHistByEmployee(employeeIDs,
							new DatePeriod(startDateClosure, GeneralDate.today()));
			
			this.parallel.forEach(employeeIDs, employeeID -> {
				if(employeeID ==null) {
					return;
				}
				// 年月日　←「システム日付の前日」
				GeneralDate ymd = GeneralDate.today().addDays(-1);
				if (createNewEmp == 1) {	
					/** Imported「所属会社履歴（社員別）」を取得する(lấy thông tin Imported「所属会社履歴（社員別）」) */
					AffCompanyHistImport affComHist = new AffCompanyHistImport();
					boolean checkAffComHist = false;
					for (AffCompanyHistImport affCompanyHistImport : listAffCompanyHistImport) {
						if (affCompanyHistImport.getEmployeeId().equals(employeeID)) {
							affComHist = affCompanyHistImport;
							checkAffComHist = true;
							break;
						}
>>>>>>> cab2d51... fixbug kbt002 :#109103
					}
				}
				// 年月日 ← 取得した「所属会社履歴（社員別）.所属期間.開始日」
				if (checkAffComHist) {
					if (!affComHist.getLstAffComHistItem().isEmpty()) {
						ymd = affComHist.getLstAffComHistItem().get(0).getDatePeriod().start();
					}
				}
			}
			if (checkStop.get())
				return;
			/** アルゴリズム「指定社員の中間データを作成する」を実行する */
			AppRootInsContentFnImport appRootInsContentFnImport = createDailyApproverAdapter
					.createDailyApprover(employeeID, 1, ymd, startDateClosure);

			boolean flagError = appRootInsContentFnImport.getErrorFlag().intValue() == 0 ? false : true;
			String errorMessage = appRootInsContentFnImport.getErrorMsgID();
			// 取得したエラーフラグ != エラーなし
			if (flagError) {
				/** ドメインモデル「承認中間データエラーメッセージ情報（日別実績）」を追加する */
				AppDataInfoDaily appDataInfoDaily = new AppDataInfoDaily(employeeID, executionId,
						new ErrorMessageRC(TextResource.localize(errorMessage)));
				appDataInfoDailyRepo.addAppDataInfoDaily(appDataInfoDaily);
			}
			if (checkStop.get())
				return;
			// ドメインモデル「承認中間データ中断管理（日別実績）」を取得する
			Optional<AppInterrupDaily> appInterrupDaily = appInterrupDailyRepository
					.getAppInterrupDailyByID(executionId);
			if (appInterrupDaily.isPresent() && appInterrupDaily.get().isSuspendedState()) {
				checkStop.set(true);
				return;
			}

		}); // end for listEmployee
		if (checkStop.get()) {
			return new OutputCreatePerApprovalDaily(false, true);
		}

		/** ドメインモデル「承認中間データエラーメッセージ情報（日別実績）」を取得する */
		List<AppDataInfoDaily> listAppDataInfoDaily = appDataInfoDailyRepo.getAppDataInfoDailyByExeID(executionId);
		if (!listAppDataInfoDaily.isEmpty()) {// 取得できた場合
			return new OutputCreatePerApprovalDaily(true, false);
		} else {
			return new OutputCreatePerApprovalDaily(false, false);
		}
	}

}
