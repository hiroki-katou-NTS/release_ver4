package nts.uk.screen.at.app.kaf021.query.c_d;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.dom.monthly.agreement.monthlyresult.specialprovision.*;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.SyEmployeeAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.bs.dto.SyEmployeeImport;
import nts.uk.query.model.employee.EmployeeInformation;
import nts.uk.query.model.employee.EmployeeInformationQuery;
import nts.uk.query.model.employee.EmployeeInformationRepository;
import nts.uk.query.model.workplace.WorkplaceModel;
import nts.uk.screen.at.app.workrule.closure.ClosurePeriodForAllQuery;
import nts.uk.screen.at.app.workrule.closure.CurrentClosurePeriod;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * UKDesign.UniversalK.就業.KAF_申請.KAF021_36協定特別条項の適用申請.C：36協定特別条項の適用申請（申請一覧）.メニュー別OCD
 *
 * @author Le Huu Dat
 */
@Stateless
public class SpecialProvisionOfAgreementAppListQuery {
    @Inject
    private ClosurePeriodForAllQuery closurePeriodForAllQuery;
    @Inject
    private SpecialProvisionsOfAgreementRepo specialProvisionsOfAgreementRepo;
    @Inject
    private SyEmployeeAdapter syEmpAdapter;
    @Inject
    private EmployeeInformationRepository employeeInformationRepo;

    /**
     * 初期表示を行う
     */
    public SpecialProvisionOfAgreementAppListDto initDisplay(List<Integer> status) {
        String cid = AppContexts.user().companyId();
        // 全ての締めの処理年月と締め期間を取得する
        List<CurrentClosurePeriod> closurePeriods = closurePeriodForAllQuery.get(cid);
        if (CollectionUtil.isEmpty(closurePeriods)) throw new BusinessException("Msg_1843");
        CurrentClosurePeriod closurePeriod = closurePeriods.get(0);

        return search(closurePeriod.getClosureStartDate(), closurePeriod.getClosureEndDate(), status);
    }

    /**
     * 36協定特別条項の適用申請を検索する
     */
    public SpecialProvisionOfAgreementAppListDto search(GeneralDate startDate, GeneralDate endDate, List<Integer> status) {
        String sid = AppContexts.user().employeeId();
        List<ApprovalStatus> listApprove = status.stream().map(x -> EnumAdaptor.valueOf(x, ApprovalStatus.class)).collect(Collectors.toList());
        specialProvisionsOfAgreementRepo.getByApproverSID(sid, startDate, endDate, listApprove);
        // specialProvisionsOfAgreementRepo.getByConfirmerSID(sid, startDate, endDate, listApprove);
        // TODO Q&A
        List<SpecialProvisionsOfAgreement> agreements = new ArrayList<>();

        // 社員IDから個人社員基本情報を取得
        List<String> enteredPersonSIDs = new ArrayList<>();
        List<String> approverSIDs = new ArrayList<>();
        List<String> confirmSIDs = new ArrayList<>();
        List<String> applicantsSIDs = new ArrayList<>();
        for (SpecialProvisionsOfAgreement agreement : agreements) {
            enteredPersonSIDs.add(agreement.getEnteredPersonSID());

            ApprovalStatusDetails approval = agreement.getApprovalStatusDetails();
            if (approval != null && approval.getApproveSID().isPresent()) {
                approverSIDs.add(approval.getApproveSID().get());
            }

            for (ConfirmationStatusDetails confirm : agreement.getConfirmationStatusDetails()) {
                confirmSIDs.add(confirm.getConfirmerSID());
            }

            applicantsSIDs.add(agreement.getApplicantsSID());
        }

        enteredPersonSIDs = enteredPersonSIDs.stream().distinct().collect(Collectors.toList());
        approverSIDs = approverSIDs.stream().distinct().collect(Collectors.toList());
        confirmSIDs = confirmSIDs.stream().distinct().collect(Collectors.toList());
        applicantsSIDs = applicantsSIDs.stream().distinct().collect(Collectors.toList());

        // 入力者の社員情報
        Map<String, SyEmployeeImport> enteredPersonInfoAll = syEmpAdapter.getPersonInfor(enteredPersonSIDs)
                .stream().collect(Collectors.toMap(SyEmployeeImport::getEmployeeId, x -> x));
        // 承認者の社員情報
        Map<String, SyEmployeeImport>  approvalInfoAll = syEmpAdapter.getPersonInfor(approverSIDs)
                .stream().collect(Collectors.toMap(SyEmployeeImport::getEmployeeId, x -> x));
        // 確認者の社員情報
        Map<String, SyEmployeeImport>  confirmerInfoAll = syEmpAdapter.getPersonInfor(confirmSIDs)
                .stream().collect(Collectors.toMap(SyEmployeeImport::getEmployeeId, x -> x));

        // <<Public>> 社員の情報を取得する
        EmployeeInformationQuery employeeInformationQuery = EmployeeInformationQuery.builder()
                .employeeIds(applicantsSIDs)
                .referenceDate(GeneralDate.today())
                .toGetWorkplace(true)
                .toGetDepartment(false)
                .toGetPosition(false)
                .toGetEmployment(false)
                .toGetClassification(false)
                .toGetEmploymentCls(false).build();
        Map<String, EmployeeInformation> empInfoAll = employeeInformationRepo.find(employeeInformationQuery)
                .stream().collect(Collectors.toMap(EmployeeInformation::getEmployeeId, x -> x));

        // mapping result
        SpecialProvisionOfAgreementAppListDto result = new SpecialProvisionOfAgreementAppListDto();
        result.setStartDate(startDate);
        result.setEndDate(endDate);
        List<ApplicationListDto> applications = new ArrayList<>();
        for (SpecialProvisionsOfAgreement agreement : agreements) {
            ApplicationListDto app = new ApplicationListDto();
            String employeeId = agreement.getApplicantsSID();
            app.setApplicantId(agreement.getApplicationID());

            if (empInfoAll.containsKey(employeeId)){
                EmployeeInformation empInfo = empInfoAll.get(employeeId);
                Optional<WorkplaceModel> workplaceOpt = empInfo.getWorkplace();
                if (workplaceOpt.isPresent()){
                    WorkplaceModel workplace = workplaceOpt.get();
                    app.setWorkplaceName(workplace.getWorkplaceName());
                }
                app.setEmployeeCode(empInfo.getEmployeeCode());
                app.setEmployeeName(empInfo.getBusinessName());
            }

            // 申請時間
            app.setApplicationTime(new ApplicationTimeDto(agreement.getApplicationTime()));

            // 画面表示情報
            app.setScreenDisplayInfo(new ScreenDisplayInfoDto(agreement.getScreenDisplayInfo()));

            // 申請理由
            app.setReason(agreement.getReasonsForAgreement().v());

            // コメント
            Optional<AgreementApprovalComments> approvalCommentOpt = agreement.getApprovalStatusDetails().getApprovalComment();
            approvalCommentOpt.ifPresent(agreementApprovalComments -> app.setComment(agreementApprovalComments.v()));

            // 申請者
            String enteredPersonSID = agreement.getEnteredPersonSID();
            if (enteredPersonInfoAll.containsKey(enteredPersonSID)) {
                app.setApplicant(enteredPersonInfoAll.get(enteredPersonSID).getBusinessName());
            }

            // 入力日付
            app.setInputDate(agreement.getInputDate());

            // 承認者
            ApprovalStatusDetails approval = agreement.getApprovalStatusDetails();
            if (approval != null && approval.getApproveSID().isPresent()) {
                String approveSID = approval.getApproveSID().get();
                if (approvalInfoAll.containsKey(approveSID)){
                    app.setApprover(approvalInfoAll.get(approveSID).getBusinessName());
                }
            }

            // 承認状況
            app.setApprovalStatus(agreement.getApprovalStatusDetails().getApprovalStatus().value);

            // 従業員代表 //TODO Q&A #33314
            app.setConfirmer(null);

            // 確認状況 //TODO Q&A #33314
            app.setConfirmStatus(0);

        }
        result.setApplications(applications);
        return result;
    }
}
