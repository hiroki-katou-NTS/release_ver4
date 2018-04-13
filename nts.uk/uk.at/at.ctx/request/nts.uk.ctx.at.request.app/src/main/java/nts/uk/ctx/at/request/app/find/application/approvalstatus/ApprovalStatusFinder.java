package nts.uk.ctx.at.request.app.find.application.approvalstatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.request.dom.application.approvalstatus.ApprovalStatusMailTemp;
import nts.uk.ctx.at.request.dom.application.approvalstatus.ApprovalStatusMailTempRepository;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.ApprovalStatusService;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.ApprovalSttAppOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.DailyStatus;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.SendMailResultOutput;
import nts.uk.ctx.at.request.dom.application.approvalstatus.service.output.WorkplaceInfor;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.application.realitystatus.RealityStatusAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.application.realitystatus.UseSetingImport;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.ApprovalComfirmDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.ClosureHistoryForComDto;
import nts.uk.ctx.at.shared.app.find.workrule.closure.dto.ClosuresDto;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmployment;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
/**
 * 承認状況メールテンプレート
 * 
 * @author dat.lh
 */
public class ApprovalStatusFinder {

	@Inject
	private ApprovalStatusMailTempRepository approvalStatusMailTempRepo;
	
	@Inject
	private ClosureService closureService;
	
	/** The repository. */
	@Inject
	private ClosureRepository repository;
	
	@Inject
	ClosureEmploymentRepository closureEmpRepo;
	
	@Inject
	private ApprovalStatusService appSttService;
	
	@Inject RealityStatusAdapter realityStatusAdapter;

	/**
	 * アルゴリズム「承認状況本文起動」を実行する
	 */
	public List<ApprovalStatusMailTempDto> findBySetting() {
		// 会社ID
		String cid = AppContexts.user().companyId();
		List<ApprovalStatusMailTempDto> listMail = new ArrayList<ApprovalStatusMailTempDto>();
		UseSetingImport useSetting = realityStatusAdapter.getUseSetting(cid);
		listMail.add(this.getApprovalStatusMailTemp(cid, 0));
		if (useSetting.isUsePersonConfirm()) {
			listMail.add(this.getApprovalStatusMailTemp(cid, 1));
		}
		if (useSetting.isUseBossConfirm()) {
			listMail.add(this.getApprovalStatusMailTemp(cid, 2));
		}
		if (useSetting.isMonthlyConfirm()) {
			listMail.add(this.getApprovalStatusMailTemp(cid, 3));
		}
		listMail.add(this.getApprovalStatusMailTemp(cid, 4));
		return listMail;
	}

	/**
	 * 承認状況メール本文取得
	 */
	private ApprovalStatusMailTempDto getApprovalStatusMailTemp(String cid, int mailType) {
		Optional<ApprovalStatusMailTemp> domain = approvalStatusMailTempRepo.getApprovalStatusMailTempById(cid, mailType);
		return domain.isPresent() ? ApprovalStatusMailTempDto.fromDomain(domain.get())
				: new ApprovalStatusMailTempDto(mailType, 1, 1, 1, "", "", 0);
	}
	
	/**
	 * 承認状況メールテスト送信
	 */
	public String confirmSenderMail(){
		// アルゴリズム「承認状況送信者メール確認」を実行する
		return appSttService.confirmApprovalStatusMailSender();
	}

	public SendMailResultOutput sendTestMail(int mailType) {
		//アルゴリズム「承認状況メールテスト送信実行」を実行する
		return appSttService.sendTestMail(mailType);
	}	

	/**
	 * アルゴリズム「承認状況指定締め日取得」を実行する
	 * Acquire approval situation designated closing date
	 * @return approval situation
	 */
	public ApprovalComfirmDto findAllClosure() {
		// Get companyID.
		String companyId = AppContexts.user().companyId();
		GeneralDate startDate = null;
		GeneralDate endDate = null;
		int processingYm = 0;
		//ドメインモデル「就業締め日」を取得する　<shared>
		List<Closure> closureList = this.repository.findAllUse(companyId);
		int selectedClosureId = 0;
		List<ClosuresDto> closureDto = closureList.stream().map(x -> {
			int closureId = x.getClosureId().value;
			List<ClosureHistoryForComDto> closureHistoriesList = x.getClosureHistories().stream().map(x1 -> {
				return new ClosureHistoryForComDto( x1.getClosureName().v(), x1.getClosureId().value, x1.getEndYearMonth().v().intValue(), x1.getClosureDate().getClosureDay().v().intValue(), x1.getStartYearMonth().v().intValue());
			}).collect(Collectors.toList());
			ClosureHistoryForComDto closureHistories = closureHistoriesList.stream()
					.filter(x2 -> x2.getClosureId() == closureId).findFirst().orElse(null);
			return new ClosuresDto(closureId, closureHistories.getCloseName(), closureHistories.getClosureDate());
		}).collect(Collectors.toList());
		
		//ユーザー固有情報「選択中の就業締め」を取得する
		//TODO neeed to get closureId init
		
		
		//就業締め日（リスト）の先頭の締めIDを選択
		List<String> listEmpCode = new ArrayList<>();
		Optional<ClosuresDto> closure = closureDto.stream().findFirst();
		if (closure.isPresent()) {
			val closureId = closure.get().getClosureId();
			selectedClosureId = closureId;
			val closureOpt = this.repository.findById(companyId, closureId);
			if (closureOpt.isPresent()) {
				val closureItem = closureOpt.get();
				// 当月の期間を算出する
				val yearMonth = closureItem.getClosureMonth().getProcessingYm();
				processingYm = yearMonth.v();
				//アルゴリズム「承認状況指定締め期間設定」を実行する
				//アルゴリズム「当月の期間を算出する」を実行する
				DatePeriod closurePeriod = this.closureService.getClosurePeriod(closureId, yearMonth);
				startDate = closurePeriod.start();
				endDate = closurePeriod.end();
				//ドメインモデル「雇用に紐づく就業締め」より、雇用コードと締めIDを取得する
				List<ClosureEmployment> listEmployee = closureEmpRepo.findByClosureId(companyId, closureId);
				for(ClosureEmployment emp: listEmployee) {
					listEmpCode.add(emp.getEmploymentCD());
				}
			} else {
				throw new RuntimeException("Could not find closure");
			}
		}
		return new ApprovalComfirmDto(selectedClosureId, closureDto, startDate, endDate, processingYm, listEmpCode);
	}
	
	/**
	 * アルゴリズム「承認状況指定締め期間設定」を実行する
	 * @param closureId
	 * @param closureDate
	 * @return
	 */
	public ApprovalStatusPeriorDto getApprovalStatusPerior(int closureId, int closureDate) {
		// Get companyID.
		String companyId = AppContexts.user().companyId();
		GeneralDate startDate = null;
		GeneralDate endDate = null;
		int processingYmNew = 0;
		// 当月の期間を算出する
		YearMonth processingYm = new YearMonth(closureDate);
		
		List<ClosureEmployment> listEmployee = new ArrayList<>();
		Optional<Closure> closure = repository.findById(companyId, closureId);
		if(!closure.isPresent()){
			throw new RuntimeException("Could not find closure");
		}
		
		val yearMonth = closure.get().getClosureMonth().getProcessingYm();
		processingYmNew = yearMonth.v();
		//アルゴリズム「当月の期間を算出する」を実行する
		DatePeriod closurePeriod = this.closureService.getClosurePeriod(closureId, processingYm);
		startDate = closurePeriod.start();
		endDate = closurePeriod.end();
		//ドメインモデル「雇用に紐づく就業締め」より、雇用コードと締めIDを取得する
		listEmployee = closureEmpRepo.findByClosureId(companyId, closureId);
		List<String> listEmpCode = new ArrayList<>();
		for(ClosureEmployment emp: listEmployee) {
			listEmpCode.add(emp.getEmploymentCD());
		}
		return new ApprovalStatusPeriorDto(startDate, endDate, listEmpCode, processingYmNew);
	}
	
	/**
	 * アルゴリズム「承認状況職場別起動」を実行する
	 * @param appStatus
	 */
	public List<ApprovalSttAppOutput> getAppSttByWorkpace(ApprovalStatusActivityData appStatus) {
		List<ApprovalSttAppOutput> listAppSttApp = new ArrayList<>();
		/*startDate = appStatus.getStartDate();
		endDate = appStatus.getEndDate();
		for (WorkplaceInfor wkp : appStatus.getListWorkplaceInfor()) {
			//String wkpName = this.
			List<ApprovalStatusEmployeeOutput> listAppStatusEmp = appSttService.getApprovalStatusEmployee(wkp.getWkpId(), startDate, endDate, appStatus.getListEmpCd());
			 approvalSttApp = appSttService.getApprovalSttApp(wkp, listAppStatusEmp);
			 listAppSttApp.add(approvalSttApp);
		}*/
		List<WorkplaceInfor> listWorkPlaceInfor = appStatus.getListWorkplace();
		for (WorkplaceInfor wkp : listWorkPlaceInfor) {
			listAppSttApp.add(new ApprovalSttAppOutput(wkp.getCode(), wkp.getName(), true, true, 0, 0, 0, 0, 0));
		}
/*		listAppSttApp.add(new ApprovalSttAppOutput("01", "経理課", true, false, 1, 12, 3, 4, 0));
		listAppSttApp.add(new ApprovalSttAppOutput("02", "人事課", true, true, 3, 15, 6, 8, 0));
		listAppSttApp.add(new ApprovalSttAppOutput("03", "管理部", true, false, 2, 22, 4, 6, 0));*/
		return listAppSttApp;
	}
	
	/**
	 * アルゴリズム「承認状況未承認メール送信」を実行する
	 */
	public List<String> getAppSttSendingUnapprovedMail(List<ApprovalSttAppOutput> listAppSttApp) {
		return appSttService.getAppSttSendingUnapprovedMail(listAppSttApp);

	}
	
	/**
	 * アルゴリズム「承認状況未承認メール送信実行」を実行する
	 */
	public void exeSendUnconfirmedMail(UnAppMailTransmisDto unAppMail) {
		appSttService.exeSendUnconfirmedMail(unAppMail.getListWkpId(), unAppMail.getClosureStart(), unAppMail.getClosureEnd(), unAppMail.getListEmpCd());
	}
	
	/**
	 * アルゴリズム「承認状況社員別起動」を実行する
	 */
	public List<DailyStatus> initApprovalSttByEmployee(ApprovalStatusByIdDto appSttById) {
		return appSttService.getApprovalSttById(appSttById.getSelectedWkpId(), appSttById.getListWkpId(),
				appSttById.getStartDate(), appSttById.getEndDate(), appSttById.getListEmpCode());
	}
}
