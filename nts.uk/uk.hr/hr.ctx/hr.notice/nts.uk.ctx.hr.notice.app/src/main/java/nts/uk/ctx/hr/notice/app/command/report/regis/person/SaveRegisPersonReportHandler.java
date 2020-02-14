/**
 * 
 */
package nts.uk.ctx.hr.notice.app.command.report.regis.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.hr.notice.dom.report.registration.person.ApprovalPersonReport;
import nts.uk.ctx.hr.notice.dom.report.registration.person.RegistrationPersonReport;
import nts.uk.ctx.hr.notice.dom.report.registration.person.RegistrationPersonReportRepository;
import nts.uk.ctx.hr.notice.dom.report.registration.person.ReportItem;
import nts.uk.ctx.hr.notice.dom.report.registration.person.ReportItemRepository;
import nts.uk.ctx.hr.notice.dom.report.registration.person.enu.ApprovalStatusForRegis;
import nts.uk.ctx.hr.notice.dom.report.registration.person.enu.LayoutItemType;
import nts.uk.ctx.hr.notice.dom.report.registration.person.enu.RegistrationStatus;
import nts.uk.ctx.hr.notice.dom.report.registration.person.enu.ReportType;
import nts.uk.ctx.hr.shared.dom.adapter.EmployeeInfo;
import nts.uk.ctx.hr.shared.dom.approval.rootstate.ApprovalFrameHrExport;
import nts.uk.ctx.hr.shared.dom.approval.rootstate.ApprovalPhaseStateHrExport;
import nts.uk.ctx.hr.shared.dom.approval.rootstate.ApprovalRootContentHrExport;
import nts.uk.ctx.hr.shared.dom.approval.rootstate.IApprovalRootStateAdaptor;
import nts.uk.ctx.hr.shared.dom.create.approval.rootstate.ICreateApprovalStateAdaptor;
import nts.uk.ctx.hr.shared.dom.employee.EmployeeInformationAdaptor;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.pereg.app.ItemValue;
import nts.uk.shr.pereg.app.command.ItemsByCategory;

/**
 * @author laitv
 *
 */
@Stateless
public class SaveRegisPersonReportHandler extends CommandHandler<SaveReportInputContainer>{
	
	@Inject
	private RegistrationPersonReportRepository repo;
	
	@Inject
	private ReportItemRepository reportItemRepo;
	
	@Inject
	private EmployeeInformationAdaptor empInfoAdaptor;
	
	@Inject
	private IApprovalRootStateAdaptor approvalRootStateAdaptor;
	
	@Inject
	private ICreateApprovalStateAdaptor createApprovalStateAdaptor;
	
	/** The Constant TIME_DAY_START. */
	public static final String TIME_DAY_START = " 00:00:00";

	/** The Constant DATE_TIME_FORMAT. */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String MAX_DATE = "9999/12/31";
	public static final String MIN_DATE = "1900/01/01";

	@Override
	protected void handle(CommandHandlerContext<SaveReportInputContainer> context) {
		SaveReportInputContainer command = context.getCommand();
		ValidateDataCategoryHistory.validate(command);
		if (command.reportID == null) {
			// insert
			insertData(command);
		}else {
			// update
			updateData(command);
		}
	}
	
	public void insertData(SaveReportInputContainer data) {
		String sid = AppContexts.user().employeeId();
		String cid = AppContexts.user().companyId();
		String rootSateId = data.rootSateId;
		Integer reportIDNew = repo.getMaxReportId(sid, cid) + 1;
		
		if (rootSateId == null) {
			
			rootSateId = IdentifierUtil.randomUniqueId();
			
			// アルゴリズム[[No.309]承認ルートを取得する/1.社員の対象申請の承認ルートを取得する]を実行する
			//(Thực hiện thuật toán [[No.309] Get approval route/1. Get Approval route for employee application])
			ApprovalRootContentHrExport export = approvalRootStateAdaptor.getApprovalRootHr(cid, sid, String.valueOf(data.reportLayoutID), GeneralDate.today(), Optional.empty());
			
			// アルゴリズム[[RQ637]承認ルートインスタンスを新規作成する]を実行する(Thực hiện thuật toán [[RQ637] Create new approval route instance])
			createApprovalStateAdaptor.createApprStateHr(export, rootSateId, sid, GeneralDate.today());
			
			// [人事届出の登録.ルートインスタンスID]、[人事届出の承認.ルートインスタンスID]を登録する。[人事届出の承認．メール送信区分]=メールするに設定
			//(Đăng ký [Registration of HR report. Root instance ID], [Approval of HR report. Root instance ID]. [Approval of HR report. Category gửi mail] = Setting mail)
			/*List<ApprovalPersonReport> listDomainApproval = new ArrayList<>();
			if (!export.getApprovalRootState().getListApprovalPhaseState().isEmpty()) {
				List<ApprovalPhaseStateHrExport> listApprovalPhaseState = export.getApprovalRootState().getListApprovalPhaseState();
				for (int i = 0; i < listApprovalPhaseState.size(); i++) {
					ApprovalPhaseStateHrExport approvalPhaseStateHrExport = listApprovalPhaseState.get(i); 
					if (!approvalPhaseStateHrExport.getListApprovalFrame().isEmpty()) {
						List<ApprovalFrameHrExport> listApprovalFrame = approvalPhaseStateHrExport.getListApprovalFrame();
						for (int j = 0; j < listApprovalFrame.size(); j++) {
							ApprovalFrameHrExport approvalFrameHrExport = listApprovalFrame.get(j);
							ApprovalPersonReport domainApproval = new ApprovalPersonReport();
							domainApproval.setCid(cid);
							domainApproval.setRootSatteId(rootSateId);
							domainApproval.setReportID(reportIDNew);
							domainApproval.setReportName(data.reportName);
							domainApproval.setRefDate(GeneralDateTime.now());
							domainApproval.setInputDate(GeneralDateTime.now());
							domainApproval.setAppDate(GeneralDateTime.now());
							domainApproval.setAprDate(approvalFrameHrExport.getListApprover().isEmpty() ? null : GeneralDateTime.legacyDateTime(approvalFrameHrExport.getListApprover().get(0).getApprovalDate().date()));;
							domainApproval.setAprSid(approvalFrameHrExport.getListApprover().get(0).getApproverID());
							
						}
					}
				}
			} */
			
			
			
		}
		
		EmployeeInfo employeeInfo = this.getPersonInfo();
		 
		RegistrationPersonReport personReport = RegistrationPersonReport.builder()
				.cid(cid)
				.rootSateId(data.isSaveDraft == 1 ? null : rootSateId)
				.workId(data.workId)
				.reportID(reportIDNew)
				.reportLayoutID(data.reportLayoutID)
				.reportCode(data.reportCode)
				.reportName(data.reportName)
				.reportDetail(null) // chưa đặt hàng lần này
				.regStatus(data.isSaveDraft == 1 ? RegistrationStatus.Save_Draft : RegistrationStatus.Registration)
				.aprStatus(ApprovalStatusForRegis.Not_Started)
				.draftSaveDate(GeneralDateTime.now())
				.missingDocName(data.missingDocName)
				.inputPid(employeeInfo.inputPid)
				.inputSid(employeeInfo.inputSid)
				.inputScd(employeeInfo.inputScd)
				.inputBussinessName(employeeInfo.inputBussinessName)
				.inputDate(GeneralDateTime.now())
				.appPid(employeeInfo.appliPerId)
				.appSid(employeeInfo.appliPerSid)
				.appScd(employeeInfo.appliPerScd)
				.appBussinessName(employeeInfo.appliPerBussinessName)
				.appDate(GeneralDateTime.now())
				.appDevId(employeeInfo.appDevId)
				.appDevCd(employeeInfo.appDevCd)
				.appDevName(employeeInfo.appDevName)
				.appPosId(employeeInfo.appPosId)
				.appPosCd(employeeInfo.appPosCd)
				.appPosName(employeeInfo.appPosName)
				.reportType(EnumAdaptor.valueOf(data.reportType, ReportType.class))
				.sendBackSID(data.sendBackSID)
				.sendBackComment(data.sendBackComment)
				.delFlg(false)
				.build();
		
		List<ReportItem> listReportItem = creatDataReportItem(data, reportIDNew);
		
		repo.add(personReport);
	    reportItemRepo.addAll(listReportItem);
	}
	
	public List<ReportItem> creatDataReportItem(SaveReportInputContainer data, Integer reportId) {

		List<ReportItem> listReportItem = new ArrayList<ReportItem>();
		List<ItemDfCommand> listItemDf = data.listItemDf;
		String cid = AppContexts.user().companyId();
		String contractCode = AppContexts.user().contractCode();
		
		for (int i = 0; i < data.inputs.size(); i++) {
			ItemsByCategory itemsByCtg = data.inputs.get(i);
			List<ItemValue> items = itemsByCtg.getItems();
			for (int j = 0; j < items.size(); j++) { 
				ItemValue itemValue = items.get(j);
				Optional<ItemDfCommand> itemDfCommandOpt = listItemDf.stream().filter(it -> it.itemDefId.equals(itemValue.definitionId())).findFirst();
				
				ItemDfCommand itemDfCommand = itemDfCommandOpt.get();
				
				int layoutItemType = 0;
				switch (itemDfCommand.layoutItemType) {
				case "ITEM":
					layoutItemType = 0;
					break;
				case "LIST":
					layoutItemType = 1;
					break;
				case "SeparatorLine":
					layoutItemType = 2;
					break;
				}
				
				ReportItem reportItem = ReportItem.builder().cid(cid).workId(0).reportID(reportId)
						.reportLayoutID(data.reportLayoutID).reportName(data.reportName)
						.layoutItemType(EnumAdaptor.valueOf(layoutItemType, LayoutItemType.class))
						.categoryId(itemDfCommand.categoryId).ctgCode(itemDfCommand.categoryCode)
						.ctgName(itemDfCommand.categoryName).fixedAtr(true).itemId(itemDfCommand.itemDefId)
						.itemCd(itemDfCommand.itemCode).itemName(itemDfCommand.itemName)
						.saveDataAtr(itemValue.saveDataType().value).dspOrder(itemDfCommand.dispOrder)
						.layoutDisOrder(itemDfCommand.layoutDisOrder).contractCode(contractCode).reflectID(0).build();

				switch (itemValue.saveDataType().value) {
				case 1:
					reportItem.setStringVal(itemValue.value());
					break;
				case 2:
					reportItem.setIntVal(itemValue.value());
					break;
				case 3:
					reportItem.setDateVal(itemValue.value());
					break;
				}

				listReportItem.add(reportItem);
			}
		}
		return listReportItem;
	}
	

	public void updateData(SaveReportInputContainer data) {
		Integer reportId = data.reportID;
		String cid = AppContexts.user().companyId();
		Optional<RegistrationPersonReport> domainReportOpt = repo.getDomainByReportId(cid, reportId);
		if (!domainReportOpt.isPresent()) {
			return;
		}
		
		EmployeeInfo employeeInfo = this.getPersonInfo();
		
		RegistrationPersonReport domainReport = domainReportOpt.get();
		domainReport.setCid(cid);
		domainReport.setReportLayoutID(data.reportLayoutID);
		domainReport.setReportCode(data.reportCode);
		domainReport.setReportName(data.reportName);
		domainReport.setRegStatus(data.isSaveDraft == 1 ? RegistrationStatus.Save_Draft : RegistrationStatus.Registration);
		domainReport.setDraftSaveDate(GeneralDateTime.now());
		domainReport.setMissingDocName(data.missingDocName);
		
		domainReport.setInputPid(employeeInfo.inputPid);
		domainReport.setInputSid(employeeInfo.inputSid);
		domainReport.setInputScd(employeeInfo.inputScd);
		domainReport.setInputBussinessName(employeeInfo.inputBussinessName);
		domainReport.setInputDate(GeneralDateTime.now());
		
		domainReport.setAppSid(employeeInfo.appliPerSid);
		domainReport.setAppScd(employeeInfo.appliPerScd);
		domainReport.setAppPid(employeeInfo.appliPerId);
		domainReport.setAppBussinessName(employeeInfo.appliPerBussinessName);
		domainReport.setAppDate(GeneralDateTime.now());
		
		domainReport.setAppDevId(employeeInfo.appDevId);
		domainReport.setAppDevCd(employeeInfo.appDevCd);
		domainReport.setAppDevName(employeeInfo.appDevName);
		
		domainReport.setAppPosId(employeeInfo.appPosId);
		domainReport.setAppPosCd(employeeInfo.appPosCd);
		domainReport.setAppPosName(employeeInfo.appPosName);
		
		domainReport.setReportType(EnumAdaptor.valueOf(data.reportType, ReportType.class));
		domainReport.setSendBackSID(data.sendBackSID);
		domainReport.setSendBackComment(data.sendBackComment);
		
		domainReport.setDelFlg(false);
		
		// remove list reportItem
		reportItemRepo.deleteByReportId(cid, reportId);
		
		List<ReportItem> listReportItem = creatDataReportItem(data, reportId);
		
		repo.update(domainReport);
	    reportItemRepo.addAll(listReportItem);
	}
	
	private EmployeeInfo getPersonInfo(){
		String sid = AppContexts.user().employeeId();
		String cid = AppContexts.user().companyId();
		GeneralDate baseDate = GeneralDate.today();
		EmployeeInfo empInfo = empInfoAdaptor.getInfoEmp(sid, cid, baseDate);
		return empInfo;
	}
}
