package nts.uk.ctx.at.record.app.command.monthly.agreement.monthlyresult.specialprovision;

import lombok.AllArgsConstructor;
import nts.arc.layer.app.command.CommandHandler;
import nts.arc.layer.app.command.CommandHandlerContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.adapter.classification.affiliate.AffClassificationAdapter;
import nts.uk.ctx.at.record.dom.adapter.classification.affiliate.AffClassificationSidImport;
import nts.uk.ctx.at.record.dom.adapter.employment.SyEmploymentAdapter;
import nts.uk.ctx.at.record.dom.adapter.employment.SyEmploymentImport;
import nts.uk.ctx.at.record.dom.adapter.workplace.SWkpHistRcImported;
import nts.uk.ctx.at.record.dom.adapter.workplace.SyWorkplaceAdapter;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceAdapter;
import nts.uk.ctx.at.record.dom.monthly.agreement.approver.Approver36AgrByWorkplace;
import nts.uk.ctx.at.record.dom.monthly.agreement.approver.ApproverItem;
import nts.uk.ctx.at.record.dom.monthly.agreement.approver.OneMonthAppCreate;
import nts.uk.ctx.at.record.dom.monthly.agreement.export.AgreementExcessInfo;
import nts.uk.ctx.at.record.dom.monthly.agreement.monthlyresult.approveregister.UnitOfApprover;
import nts.uk.ctx.at.record.dom.monthly.agreement.monthlyresult.specialprovision.SpecialProvisionsOfAgreement;
import nts.uk.ctx.at.record.dom.monthly.agreement.monthlyresult.specialprovision.SpecialProvisionsOfAgreementRepo;
import nts.uk.ctx.at.record.dom.standardtime.repository.*;
import nts.uk.ctx.at.shared.dom.common.Year;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.AgreMaxAverageTimeMulti;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.AgreementTimeOfManagePeriod;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.AgreementTimeYear;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.AgreementTimeOfClassification;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.AgreementTimeOfCompany;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.AgreementTimeOfEmployment;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.AgreementTimeOfWorkPlace;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.enums.LaborSystemtAtr;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.exceptsetting.AgreementYearSetting;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.onemonth.AgreementOneMonthTime;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.setting.AgreementOperationSetting;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.setting.AgreementUnitSetting;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 36協定特別条項の適用申請の登録を行う（1ヶ月）
 *
 * @author Le Huu Dat
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class RegisterAppSpecialProvisionMonthCommandHandler extends CommandHandler<List<RegisterAppSpecialProvisionMonthCommand>> {

    @Inject
    private SpecialProvisionsOfAgreementRepo specialProvisionsOfAgreementRepo;
    @Inject
    private SyWorkplaceAdapter syWorkplaceAdapter;
    @Inject
    private AgreementUnitSettingRepository agreementUnitSettingRepository;
    @Inject
    private AffClassificationAdapter affClassificationAdapter;
    @Inject
    private AgreementTimeOfClassificationRepository agreementTimeOfClassificationRepo;
    @Inject
    private BasicAgreementSettingRepository basicAgreementSettingRepo;
    @Inject
    private AffWorkplaceAdapter affWorkplaceAdapter;
    @Inject
    private AgreementTimeOfWorkPlaceRepository agreementTimeWorkPlaceRepo;
    @Inject
    private SyEmploymentAdapter syEmploymentAdapter;
    @Inject
    private AgreementTimeOfEmploymentRepostitory agreementTimeOfEmploymentRepo;
    @Inject
    private AgreementTimeCompanyRepository agreementTimeCompanyRepo;

    @Override
    protected void handle(CommandHandlerContext<List<RegisterAppSpecialProvisionMonthCommand>> context) {
        String cid = AppContexts.user().companyId();
        String sid = AppContexts.user().employeeId();
        RequireImpl require = new RequireImpl(cid, specialProvisionsOfAgreementRepo, syWorkplaceAdapter,
                agreementUnitSettingRepository,
                affClassificationAdapter, agreementTimeOfClassificationRepo,
                affWorkplaceAdapter, agreementTimeWorkPlaceRepo, syEmploymentAdapter,
                agreementTimeOfEmploymentRepo, agreementTimeCompanyRepo);
        List<RegisterAppSpecialProvisionMonthCommand> commands = context.getCommand();
        for (RegisterAppSpecialProvisionMonthCommand command : commands) {
            OneMonthAppCreate.create(require, cid, sid, command.getContent().toMonthlyAppContent(),
                    command.getScreenInfo().toScreenDisplayInfo());
        }
    }


    @AllArgsConstructor
    private class RequireImpl implements OneMonthAppCreate.Require {

        private String companyId;
        private SpecialProvisionsOfAgreementRepo specialProvisionsOfAgreementRepo;
        private SyWorkplaceAdapter syWorkplaceAdapter;
        private AgreementUnitSettingRepository agreementUnitSettingRepository;
        private AffClassificationAdapter affClassificationAdapter;
        private AgreementTimeOfClassificationRepository agreementTimeOfClassificationRepo;
        private AffWorkplaceAdapter affWorkplaceAdapter;
        private AgreementTimeOfWorkPlaceRepository agreementTimeWorkPlaceRepo;
        private SyEmploymentAdapter syEmploymentAdapter;
        private AgreementTimeOfEmploymentRepostitory agreementTimeOfEmploymentRepo;
        private AgreementTimeCompanyRepository agreementTimeCompanyRepo;

        @Override
        public void addApp(SpecialProvisionsOfAgreement app) {
            specialProvisionsOfAgreementRepo.insert(app);
        }

        @Override
        public Optional<ApproverItem> getApproverHistoryItem(GeneralDate baseDate) {
            return Optional.empty();
        }

        @Override
        public UnitOfApprover getUsageSetting() {
            return null;
        }

        @Override
        public Optional<SWkpHistRcImported> getYourWorkplace(String employeeId, GeneralDate baseDate) {
            return syWorkplaceAdapter.findBySid(employeeId, baseDate);
        }

        @Override
        public Optional<Approver36AgrByWorkplace> getApproveHistoryItem(String workplaceId, GeneralDate baseDate) {
            return Optional.empty();
        }

        @Override
        public List<String> getUpperWorkplace(String workplaceID, GeneralDate date) {
            return affWorkplaceAdapter.getUpperWorkplace(companyId, workplaceID, date);
        }

        @Override
        public Optional<AgreementUnitSetting> agreementUnitSetting(String companyId) {
            return agreementUnitSettingRepository.find(companyId);
        }

        @Override
        public Optional<AffClassificationSidImport> affEmployeeClassification(String companyId, String employeeId, GeneralDate baseDate) {
            return affClassificationAdapter.findByEmployeeId(companyId, employeeId, baseDate);
        }

        @Override
        public Optional<AgreementTimeOfClassification> agreementTimeOfClassification(String companyId, LaborSystemtAtr laborSystemAtr, String classificationCode) {
            return agreementTimeOfClassificationRepo.find(companyId, laborSystemAtr, classificationCode);
        }

        @Override
        public List<String> getCanUseWorkplaceForEmp(String companyId, String employeeId, GeneralDate baseDate) {
            return affWorkplaceAdapter.findAffiliatedWorkPlaceIdsToRoot(companyId, employeeId, baseDate);
        }

        @Override
        public Optional<AgreementTimeOfWorkPlace> agreementTimeOfWorkPlace(String workplaceId, LaborSystemtAtr laborSystemAtr) {
            return agreementTimeWorkPlaceRepo.findAgreementTimeOfWorkPlace(workplaceId, laborSystemAtr);
        }

        @Override
        public Optional<SyEmploymentImport> employment(String companyId, String employeeId, GeneralDate baseDate) {
            return syEmploymentAdapter.findByEmployeeId(companyId, employeeId, baseDate);
        }

        @Override
        public Optional<AgreementTimeOfEmployment> agreementTimeOfEmployment(String companyId, String employmentCategoryCode, LaborSystemtAtr laborSystemAtr) {
            return agreementTimeOfEmploymentRepo.find(companyId, employmentCategoryCode, laborSystemAtr);
        }

        @Override
        public Optional<AgreementTimeOfCompany> agreementTimeOfCompany(String companyId, LaborSystemtAtr laborSystemAtr) {
            return agreementTimeCompanyRepo.find(companyId, laborSystemAtr);
        }


        @Override
        public AgreMaxAverageTimeMulti getMaxAverageMulti(String sid, GeneralDate baseDate, YearMonth ym, Map<YearMonth, AgreementOneMonthTime> agreementTimes) {
            return null;
        }

        @Override
        public AgreementTimeYear timeYear(String sid, GeneralDate baseDate, nts.arc.time.calendar.Year year, Map<YearMonth, AgreementOneMonthTime> agreementTimes) {
            return null;
        }

        @Override
        public AgreementExcessInfo algorithm(String employeeId, nts.arc.time.calendar.Year year) {
            return null;
        }

        @Override
        public Optional<AgreementOperationSetting> agreementOperationSetting(String cid) {
            return Optional.empty();
        }

        @Override
        public List<AgreementTimeOfManagePeriod> agreementTimeOfManagePeriod(List<String> sids, List<YearMonth> yearMonths) {
            return null;
        }

        @Override
        public Optional<WorkingConditionItem> workingConditionItem(String employeeId, GeneralDate baseDate) {
            return Optional.empty();
        }
    }
}
