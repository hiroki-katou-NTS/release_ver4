package nts.uk.ctx.at.request.dom.application.timeleaveapplication.service;

import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.request.dom.adapter.monthly.vacation.childcarenurse.ChildCareNursePeriodImport;
import nts.uk.ctx.at.request.dom.adapter.monthly.vacation.childcarenurse.care.GetRemainingNumberCareAdapter;
import nts.uk.ctx.at.request.dom.adapter.monthly.vacation.childcarenurse.childcare.GetRemainingNumberChildCareNurseAdapter;
import nts.uk.ctx.at.request.dom.adapter.record.remainingnumber.holidayover60h.AggrResultOfHolidayOver60hImport;
import nts.uk.ctx.at.request.dom.adapter.record.remainingnumber.holidayover60h.GetHolidayOver60hRemNumWithinPeriodAdapter;
import nts.uk.ctx.at.request.dom.adapter.record.remainingnumber.specialholiday.GetSpecialRemainingWithinPeriodAdapter;
import nts.uk.ctx.at.request.dom.adapter.record.remainingnumber.specialholiday.TotalResultOfSpecialLeaveImport;
import nts.uk.ctx.at.request.dom.application.Application;
import nts.uk.ctx.at.request.dom.application.ApplicationDate;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.annualleave.AnnLeaveRemainNumberAdapter;
import nts.uk.ctx.at.request.dom.application.common.adapter.record.remainingnumber.annualleave.ReNumAnnLeaReferenceDateImport;
import nts.uk.ctx.at.request.dom.application.common.service.other.OtherCommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.common.service.other.output.PeriodCurrentMonth;
import nts.uk.ctx.at.request.dom.application.common.service.setting.CommonAlgorithm;
import nts.uk.ctx.at.request.dom.application.timeleaveapplication.TimeDigestAppType;
import nts.uk.ctx.at.request.dom.application.timeleaveapplication.TimeLeaveApplication;
import nts.uk.ctx.at.request.dom.application.timeleaveapplication.TimeLeaveApplicationDetail;
import nts.uk.ctx.at.request.dom.application.timeleaveapplication.output.*;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.*;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.require.RemainNumberTempRequireService;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnLeaEmpBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.export.InterimRemainMngMode;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffMngInPeriodQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffRemainMngOfInPeriod;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.BreakDayOffRemainMngParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.InterimSpecialHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.basicinfo.SpecialLeaveBasicInfoRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.empinfo.grantremainingdata.SpecialLeaveGrantRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.ComplileInPeriodOfSpecialLeaveParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.InPeriodOfSpecialLeave;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialleave.service.SpecialLeaveManagementService;
import nts.uk.ctx.at.shared.dom.remainingnumber.work.VacationTimeInfor;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHoliday;
import nts.uk.ctx.at.shared.dom.specialholiday.SpecialHolidayRepository;
import nts.uk.ctx.at.shared.dom.specialholiday.grantinformation.GrantDateTblRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.SettingDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.acquisitionrule.AcquisitionRule;
import nts.uk.ctx.at.shared.dom.vacation.setting.acquisitionrule.AcquisitionRuleRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.processten.AbsenceTenProcess;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.processten.AnnualHolidaySetOutput;
import nts.uk.ctx.at.shared.dom.vacation.setting.annualpaidleave.processten.SubstitutionHolidayOutput;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.NursingCategory;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.NursingLeaveSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.NursingLeaveSettingRepository;
import nts.uk.ctx.at.shared.dom.vacation.setting.sixtyhours.Com60HourVacation;
import nts.uk.ctx.at.shared.dom.vacation.setting.sixtyhours.Com60HourVacationRepository;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.timeleaveapplication.TimeLeaveAppReflectRepository;
import nts.uk.ctx.at.shared.dom.workcheduleworkrecord.appreflectprocess.appreflectcondition.timeleaveapplication.TimeLeaveApplicationReflect;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.ctx.at.shared.dom.workrule.vacation.specialvacation.timespecialvacation.TimeSpecialLeaveManagementSetting;
import nts.uk.ctx.at.shared.dom.workrule.vacation.specialvacation.timespecialvacation.TimeSpecialLeaveMngSetRepository;
import nts.uk.ctx.at.shared.dom.worktype.DeprecateClassification;
import nts.uk.ctx.at.shared.dom.worktype.specialholidayframe.SpecialHolidayFrame;
import nts.uk.ctx.at.shared.dom.worktype.specialholidayframe.SpecialHolidayFrameRepository;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.ctx.at.shared.dom.vacation.setting.TimeDigestiveUnit;

import nts.uk.ctx.at.request.dom.application.appabsence.apptimedigest.TimeDigestApplication;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class TimeLeaveApplicationServiceImpl implements TimeLeaveApplicationService {
    @Inject
    private TimeLeaveAppReflectRepository timeLeaveAppReflectRepo;

    @Inject
    private RemainNumberTempRequireService requireService;

    @Inject
    private Com60HourVacationRepository com60HourVacationRepo;

    @Inject
    private NursingLeaveSettingRepository nursingLeaveSettingRepo;

    @Inject
    private TimeSpecialLeaveMngSetRepository timeSpecialLeaveMngSetRepo;

    @Inject
    private SpecialHolidayFrameRepository specialHolidayFrameRepo;

    @Inject
    private AnnLeaveRemainNumberAdapter leaveAdapter;

    @Inject
    private AcquisitionRuleRepository acquisitionRuleRepository;

    @Inject
    private OtherCommonAlgorithm otherCommonAlgorithm;

    @Inject
    private InterimRemainDataMngCheckRegister interimRemainDataMngCheckRegister;

    @Inject
    private SpecialHolidayRepository specialHolidayRepository;

    @Inject
    private CommonAlgorithm commonAlgorithm;

    @Inject
    private SpecialLeaveGrantRepository specialLeaveGrantRepo;

    @Inject
    private ShareEmploymentAdapter shareEmploymentAdapter;

    @Inject
    private EmpEmployeeAdapter empEmployeeAdapter;

    @Inject
    private GrantDateTblRepository grantDateTblRepo;

    @Inject
    private AnnLeaEmpBasicInfoRepository annLeaEmpBasicInfoRepo;

    @Inject
    private InterimSpecialHolidayMngRepository interimSpecialHolidayMngRepo;

    @Inject
    private InterimRemainRepository interimRemainRepo;

    @Inject
    private SpecialLeaveBasicInfoRepository specialLeaveBasicInfoRepo;

    @Inject
    private GetHolidayOver60hRemNumWithinPeriodAdapter getHolidayOver60hRemNumWithinPeriodAdapter;

    @Inject
    private GetRemainingNumberChildCareNurseAdapter getRemainingNumberChildCareNurseAdapter;

    @Inject
    private GetRemainingNumberCareAdapter getRemainingNumberCareAdapter;

    @Inject
    private WorkingConditionRepository workingConditionRepo;

    @Inject
    private GetSpecialRemainingWithinPeriodAdapter getSpecialRemainingWithinPeriodAdapter;

    @Override
    public TimeLeaveApplicationReflect getTimeLeaveAppReflectSetting(String companyId) {
        TimeLeaveApplicationReflect reflectSetting = timeLeaveAppReflectRepo.findByCompany(companyId).orElse(null);
        if (reflectSetting == null
                || (reflectSetting.getCondition().getSuperHoliday60H() == NotUseAtr.NOT_USE
                && reflectSetting.getCondition().getAnnualVacationTime() == NotUseAtr.NOT_USE
                && reflectSetting.getCondition().getChildNursing() == NotUseAtr.NOT_USE
                && reflectSetting.getCondition().getNursing() == NotUseAtr.NOT_USE
                && reflectSetting.getCondition().getSpecialVacationTime() == NotUseAtr.NOT_USE
                && reflectSetting.getCondition().getSubstituteLeaveTime() == NotUseAtr.NOT_USE)
                || (reflectSetting.getDestination().getFirstBeforeWork() == NotUseAtr.NOT_USE
                && reflectSetting.getDestination().getFirstAfterWork() == NotUseAtr.NOT_USE
                && reflectSetting.getDestination().getSecondBeforeWork() == NotUseAtr.NOT_USE
                && reflectSetting.getDestination().getSecondAfterWork() == NotUseAtr.NOT_USE
                && reflectSetting.getDestination().getPrivateGoingOut() == NotUseAtr.NOT_USE
                && reflectSetting.getDestination().getUnionGoingOut() == NotUseAtr.NOT_USE)
                ) {
            throw new BusinessException("Msg_474");
        }
        return reflectSetting;
    }

    /**
     * 時間休暇の管理区分を取得する
     * @param companyId
     * @param employeeId
     * @param baseDate
     * @return
     */
    @Override
    public TimeVacationManagementOutput getTimeLeaveManagement(String companyId, String employeeId, GeneralDate baseDate) {
        RemainNumberTempRequireService.Require require = requireService.createRequire();
        CacheCarrier cache = new CacheCarrier();

        // 10-1.年休の設定を取得する
        AnnualHolidaySetOutput annualHolidaySetOutput = AbsenceTenProcess.getSettingForAnnualHoliday(require, companyId);
        TimeAnnualLeaveManagement timeAnnualLeaveMng = new TimeAnnualLeaveManagement(
                EnumAdaptor.valueOf(annualHolidaySetOutput.getTimeYearRest(), TimeDigestiveUnit.class),
                annualHolidaySetOutput.isSuspensionTimeYearFlg()
        );

        // 10-2.代休の設定を取得する
        SubstitutionHolidayOutput substitutionHoliday =  AbsenceTenProcess.getSettingForSubstituteHoliday(require, cache, companyId, employeeId, baseDate);
        TimeAllowanceManagement timeSubstituteLeaveMng = new TimeAllowanceManagement(
                EnumAdaptor.valueOf(substitutionHoliday.getDigestiveUnit(), TimeDigestiveUnit.class),
                substitutionHoliday.isTimeOfPeriodFlg()
        );

        // ドメインモデル「60H超休管理設定」を取得する
        Com60HourVacation com60HourVacation = com60HourVacationRepo.findById(companyId).orElse(null);
        SupHolidayManagement super60HLeaveMng = new SupHolidayManagement(
                com60HourVacation == null ? null : com60HourVacation.getSetting().getDigestiveUnit(),
                com60HourVacation != null && com60HourVacation.isManaged()
        );

        // ドメインモデル「介護看護休暇設定」を取得する
        List<NursingLeaveSetting> nursingLeaveSettingList = nursingLeaveSettingRepo.findByCompanyId(companyId);
        NursingLeaveSetting careNursingLeaveSetting = nursingLeaveSettingList.stream().filter(i -> i.getNursingCategory() == NursingCategory.Nursing).findFirst().orElse(null);
        NursingLeaveSetting childCareNursingLeaveSetting = nursingLeaveSettingList.stream().filter(i -> i.getNursingCategory() == NursingCategory.ChildNursing).findFirst().orElse(null);
        ChildNursingManagement nursingLeaveMng = new ChildNursingManagement(
                careNursingLeaveSetting == null ? null : careNursingLeaveSetting.getTimeCareNursingSetting().getTimeDigestiveUnit(),
                careNursingLeaveSetting != null && careNursingLeaveSetting.isManaged() && careNursingLeaveSetting.getTimeCareNursingSetting().getManageDistinct() == ManageDistinct.YES,
                childCareNursingLeaveSetting == null ? null : childCareNursingLeaveSetting.getTimeCareNursingSetting().getTimeDigestiveUnit(),
                childCareNursingLeaveSetting != null && childCareNursingLeaveSetting.isManaged() && childCareNursingLeaveSetting.getTimeCareNursingSetting().getManageDistinct() == ManageDistinct.YES
        );

        // ドメインモデル「時間特別休暇の管理設定」を取得する
        TimeSpecialLeaveManagementSetting timeSpecialLeaveManagementSetting = timeSpecialLeaveMngSetRepo.findByCompany(companyId).orElse(null);
        TimeSpecialLeaveManagement timeSpecialLeaveMng = new TimeSpecialLeaveManagement(
                timeSpecialLeaveManagementSetting == null ? null : timeSpecialLeaveManagementSetting.getTimeDigestiveUnit(),
                timeSpecialLeaveManagementSetting != null && timeSpecialLeaveManagementSetting.getManageType() == ManageDistinct.YES,
                new ArrayList<>()
        );
        if (timeSpecialLeaveMng.isTimeSpecialLeaveManagement()) {
            // ドメインモデル「特別休暇枠」を取得する
            List<SpecialHolidayFrame> listSpecialFrame = specialHolidayFrameRepo.findAll(companyId)
                    .stream()
                    .filter(i -> i.getDeprecateSpecialHd() == DeprecateClassification.NotDeprecated && i.getTimeMngAtr() == NotUseAtr.USE)
                    .sorted(Comparator.comparing(SpecialHolidayFrame::getSpecialHdFrameNo))
                    .collect(Collectors.toList());
            timeSpecialLeaveMng.getListSpecialFrame().addAll(listSpecialFrame);
        }

        return  new TimeVacationManagementOutput(
                super60HLeaveMng,
                nursingLeaveMng,
                timeSubstituteLeaveMng,
                timeAnnualLeaveMng,
                timeSpecialLeaveMng
        );
    }

    /**
     * 各時間休暇の残数を取得する
     * @param companyId
     * @param employeeId
     * @param baseDate
     * @param timeLeaveManagement
     * @return
     */
    @Override
    public TimeVacationRemainingOutput getTimeLeaveRemaining(String companyId, String employeeId, GeneralDate baseDate, TimeVacationManagementOutput timeLeaveManagement) {
        RemainNumberTempRequireService.Require require = requireService.createRequire();
        CacheCarrier cache = new CacheCarrier();

        TimeVacationRemainingOutput timeLeaveRemaining = new TimeVacationRemainingOutput();
        timeLeaveRemaining.setRemainingPeriod(DatePeriod.oneDay(baseDate));

        // INPUT「時間休暇管理」をチェックする
        if (!timeLeaveManagement.getTimeAnnualLeaveManagement().isTimeAnnualManagement()
                && !timeLeaveManagement.getTimeAllowanceManagement().isTimeBaseManagementClass()
                && !timeLeaveManagement.getSupHolidayManagement().isOverrest60HManagement()
                && !timeLeaveManagement.getChildNursingManagement().isTimeChildManagementClass()
                && !timeLeaveManagement.getChildNursingManagement().isTimeManagementClass()) {
            return timeLeaveRemaining;
        }

        // 社員に対応する締め期間を取得する
        DatePeriod closingPeriod = ClosureService.findClosurePeriod(require, cache, employeeId, baseDate);

        if (timeLeaveManagement.getTimeAnnualLeaveManagement().isTimeAnnualManagement()) {
            // 基準日時点の年休残数を取得する
            ReNumAnnLeaReferenceDateImport reNumAnnLeave = leaveAdapter.getReferDateAnnualLeaveRemainNumber(employeeId, baseDate);
            if (reNumAnnLeave != null && reNumAnnLeave.getAnnualLeaveRemainNumberExport() != null) {
                if (reNumAnnLeave.getAnnualLeaveRemainNumberExport().getAnnualLeaveGrantDay() != null)
                    timeLeaveRemaining.setAnnualTimeLeaveRemainingDays(reNumAnnLeave.getAnnualLeaveRemainNumberExport().getAnnualLeaveGrantDay());
                if (reNumAnnLeave.getAnnualLeaveRemainNumberExport().getAnnualLeaveGrantTime() != null)
                    timeLeaveRemaining.setAnnualTimeLeaveRemainingTime(reNumAnnLeave.getAnnualLeaveRemainNumberExport().getAnnualLeaveGrantTime());
            }
        }

        if (timeLeaveManagement.getTimeAllowanceManagement().isTimeBaseManagementClass()) {
            // 期間内の休出代休残数を取得する
            BreakDayOffRemainMngParam remainParam = new BreakDayOffRemainMngParam(
                    companyId,
                    employeeId,
                    new DatePeriod(closingPeriod.start(), closingPeriod.start().addYears(1).addDays(-1)),
                    false,
                    baseDate,
                    false,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty());
            BreakDayOffRemainMngOfInPeriod dataCheck = BreakDayOffMngInPeriodQuery.getBreakDayOffMngInPeriod(require, cache, remainParam);
            timeLeaveRemaining.setSubTimeLeaveRemainingTime(dataCheck.getRemainTimes());
        }

        if (timeLeaveManagement.getSupHolidayManagement().isOverrest60HManagement()) {
            // [RQ677]期間中の60H超休残数を取得する
            AggrResultOfHolidayOver60hImport aggrResultOfHolidayOver60h = this.getHolidayOver60hRemNumWithinPeriodAdapter.algorithm(
                    companyId,
                    employeeId,
                    new DatePeriod(closingPeriod.start(), closingPeriod.start().addYears(1).addDays(-1)),
                    InterimRemainMngMode.OTHER,
                    baseDate,
                    Optional.of(false),
                    Optional.empty(),
                    Optional.empty()
            );
            if (aggrResultOfHolidayOver60h != null
                    && aggrResultOfHolidayOver60h.getAsOfPeriodEnd()!= null
                    && aggrResultOfHolidayOver60h.getAsOfPeriodEnd().getRemainingNumber()!= null
                    && aggrResultOfHolidayOver60h.getAsOfPeriodEnd().getRemainingNumber().getRemainingTimeWithMinus()!= null)
                timeLeaveRemaining.setSuper60HRemainingTime(aggrResultOfHolidayOver60h.getAsOfPeriodEnd().getRemainingNumber().getRemainingTimeWithMinus().v());
        }

        if (timeLeaveManagement.getChildNursingManagement().isTimeChildManagementClass()) {
            // [NO.206]期間中の子の看護休暇残数を取得
            ChildCareNursePeriodImport resultOfChildCareNurse = this.getRemainingNumberChildCareNurseAdapter.getChildCareNurseRemNumWithinPeriod(
                    employeeId,
                    new DatePeriod(closingPeriod.start(), closingPeriod.start().addYears(1).addDays(-1)),
                    InterimRemainMngMode.OTHER,
                    baseDate,
                    Optional.of(false),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
            );
            timeLeaveRemaining.setChildCareRemainingDays(resultOfChildCareNurse.getStartdateDays().getThisYear().getRemainingNumber().getUsedDays());
            resultOfChildCareNurse.getStartdateDays().getThisYear().getRemainingNumber().getUsedTime().ifPresent(i -> {
                timeLeaveRemaining.setChildCareRemainingTime(i);
            });
        }

        if (timeLeaveManagement.getChildNursingManagement().isTimeManagementClass()) {
            // [NO.207]期間中の介護休暇残数を取得
            ChildCareNursePeriodImport childCareNursePeriodImport = getRemainingNumberCareAdapter.getCareRemNumWithinPeriod(
                    employeeId,
                    new DatePeriod(closingPeriod.start(), closingPeriod.start().addYears(1).addDays(-1)),
                    InterimRemainMngMode.OTHER,
                    baseDate,
                    Optional.of(false),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
            );
            timeLeaveRemaining.setCareRemainingDays(childCareNursePeriodImport.getStartdateDays().getThisYear().getRemainingNumber().getUsedDays());
            childCareNursePeriodImport.getStartdateDays().getThisYear().getRemainingNumber().getUsedTime().ifPresent(i -> {
                timeLeaveRemaining.setCareRemainingTime(i);
            });
        }

        timeLeaveRemaining.setRemainingPeriod(new DatePeriod(closingPeriod.start(), closingPeriod.start().addYears(1).addDays(-1)));
        return timeLeaveRemaining;
    }

    /**
     * 特別休暇残数情報を取得する
     * @param companyId
     * @param specialFrameNo
     * @param timeLeaveAppOutput
     * @return
     */
    @Override
    public TimeLeaveApplicationOutput getSpecialLeaveRemainingInfo(String companyId, Optional<Integer> specialFrameNo, TimeLeaveApplicationOutput timeLeaveAppOutput) {
        timeLeaveAppOutput.getTimeVacationRemaining().getSpecialTimeFrames().clear();
        if (specialFrameNo.isPresent()) {
            // ドメインモデル「特別休暇」を取得する
            List<SpecialHoliday> specialHolidayList = specialHolidayRepository.findByCompanyIdWithTargetItem(companyId);
            Optional<SpecialHoliday> specialHoliday = specialHolidayList.stream().filter(i -> i.getTargetItem().getFrameNo().contains(specialFrameNo.get())).findFirst();
            if (!specialHoliday.isPresent()) {
                return timeLeaveAppOutput;
            }

            // [NO.273]期間中の特別休暇残数を取得
            TotalResultOfSpecialLeaveImport result = getSpecialRemainingWithinPeriodAdapter.algorithm(
                    companyId,
                    timeLeaveAppOutput.getAppDispInfoStartup().getAppDispInfoNoDateOutput().getEmployeeInfoLst().get(0).getSid(),
                    timeLeaveAppOutput.getTimeVacationRemaining().getRemainingPeriod(),
                    false,
                    timeLeaveAppOutput.getAppDispInfoStartup().getAppDispInfoWithDateOutput().getBaseDate(),
                    specialHoliday.get().getSpecialHolidayCode().v(),
                    Optional.of(false),
                    Optional.empty(),
                    Optional.empty()
            );

            if (result.getAtEndPeriodInfo() != null && result.getAtEndPeriodInfo().isPresent()) {
                timeLeaveAppOutput.getTimeVacationRemaining().getSpecialTimeFrames().add(
                        new TimeSpecialVacationRemaining(
                                result.getAtEndPeriodInfo().get().getRemainingInfo().getSpecialLeaveWithMinus().getRemaining().getTotal().getDays(),
                                result.getAtEndPeriodInfo().get().getRemainingInfo().getSpecialLeaveWithMinus().getRemaining().getTotal().getTime().orElse(0),
                                specialFrameNo.get()
                        )
                );
            }
        }
        return timeLeaveAppOutput;
    }

    /**
     * 時間休暇申請登録前チェック
     */
    @Override
    public void checkBeforeRegister(String companyId, TimeDigestAppType timeLeaveType, TimeLeaveApplication timeLeaveApplication, TimeLeaveApplicationOutput output) {
        // 申請時間をチェック
        if (timeLeaveApplication.getLeaveApplicationDetails().isEmpty())
            throw new BusinessException("Msg_1654");

        //特別休暇枠をチェックする
        if (timeLeaveApplication.getLeaveApplicationDetails()
                .stream()
                .anyMatch(detail -> detail.getTimeDigestApplication().getTimeSpecialVacation().v() > 0
                        && !detail.getTimeDigestApplication().getSpecialVacationFrameNO().isPresent())) {
            throw new BusinessException("Msg_1983");
        }

        //時間休暇優先順位チェック
        timeLeavePriorityCheck(companyId, timeLeaveType, timeLeaveApplication, output.getTimeVacationRemaining(), output.getTimeVacationManagement());

        //時間休暇残数チェック
        remainingTimeVacationCheck(companyId, timeLeaveType, timeLeaveApplication);

        //時間休暇の消化単位チェック
        Optional<TimeDigestiveUnit> super60HDigestion = Optional.ofNullable(output.getTimeVacationManagement().getSupHolidayManagement().getSuper60HDigestion());
        Optional<TimeDigestiveUnit> timeBaseRestingUnit = Optional.ofNullable(output.getTimeVacationManagement().getTimeAllowanceManagement().getTimeBaseRestingUnit());
        Optional<TimeDigestiveUnit> timeAnnualLeaveUnit = Optional.ofNullable(output.getTimeVacationManagement().getTimeAnnualLeaveManagement().getTimeAnnualLeaveUnit());
        Optional<TimeDigestiveUnit> timeChildNursing = Optional.ofNullable(output.getTimeVacationManagement().getChildNursingManagement().getTimeChildDigestiveUnit());
        Optional<TimeDigestiveUnit> timeNursing = Optional.ofNullable(output.getTimeVacationManagement().getChildNursingManagement().getTimeDigestiveUnit());
        Optional<TimeDigestiveUnit> pendingUnit = Optional.ofNullable(output.getTimeVacationManagement().getTimeSpecialLeaveMng().getTimeSpecialLeaveUnit());
        timeLeaveApplication.getLeaveApplicationDetails().forEach(x -> {
            commonAlgorithm.vacationDigestionUnitCheck(x.getTimeDigestApplication(),
                super60HDigestion,
                timeBaseRestingUnit,
                timeAnnualLeaveUnit,
                timeChildNursing,
                timeNursing,
                pendingUnit
            );
        });

        // 社員の労働条件を取得する
        Optional<WorkingCondition> workingCondition = workingConditionRepo.getBySidAndStandardDate(
                companyId,
                timeLeaveApplication.getEmployeeID(),
                timeLeaveApplication.getAppDate().getApplicationDate()
        );
        if (!workingCondition.isPresent())
            throw new BusinessException("Msg_430");
        Optional<WorkingConditionItem> workingConditionItem = workingConditionRepo.getWorkingConditionItem(workingCondition.get().getDateHistoryItem().get(0).identifier());
        if (!workingConditionItem.isPresent())
            throw new BusinessException("Msg_430");
        //契約時間をチェック
        checkContractTime(timeLeaveApplication, workingConditionItem.get());
    }

    /**
     * 時間休暇優先順位チェック
     */
    private void timeLeavePriorityCheck(String companyId, TimeDigestAppType timeDigestAppType, TimeLeaveApplication timeLeaveApplication,
                                        TimeVacationRemainingOutput timeVacationRemainingOutput,
                                        TimeVacationManagementOutput timeVacationManagementOutput) {
        if (timeDigestAppType == TimeDigestAppType.TIME_ANNUAL_LEAVE
                || (timeDigestAppType == TimeDigestAppType.USE_COMBINATION && timeLeaveApplication.getLeaveApplicationDetails().stream().anyMatch(detail -> detail.getTimeDigestApplication().getTimeAnnualLeave().v() > 0))) {

            //ドメインモデル「休暇の取得ルール」を取得する
            Optional<AcquisitionRule> acquisitionRule = acquisitionRuleRepository.findById(companyId);

            //時間休暇の優先順をチェックする

            //代休の時間管理区分
            boolean timeBaseManagementClass = timeVacationManagementOutput.getTimeAllowanceManagement().isTimeBaseManagementClass();
            //60H超休の時間管理区分
            boolean overrest60HManagement = timeVacationManagementOutput.getSupHolidayManagement().isOverrest60HManagement();
            //時間消化申請
            List<TimeDigestApplication> timeDigestApplications = timeLeaveApplication.getLeaveApplicationDetails().stream().map(TimeLeaveApplicationDetail::getTimeDigestApplication).collect(Collectors.toList());

            Integer totalTimeAnnualLeave = timeDigestApplications.stream().reduce(0, (a, b) -> a + b.getTimeAnnualLeave().v(), Integer::sum);
            Integer totalSixtyOvertime = timeDigestApplications.stream().reduce(0, (a, b) -> a + b.getOvertime60H().v(), Integer::sum);
            Integer totalTimeOff = timeDigestApplications.stream().reduce(0, (a, b) -> a + b.getTimeOff().v(), Integer::sum);

            //「@取得する順番をチェックする」を確認する
            if (acquisitionRule.isPresent() && acquisitionRule.get().getCategory() == SettingDistinct.YES) {
                //「@年休より優先する休暇．60H超休を優先」　と　INPUT「60H超休の時間管理区分」を確認する
                if (acquisitionRule.get().getAnnualHoliday().isSixtyHoursOverrideHoliday() && overrest60HManagement) {
                    //60H超休残時間
                    int super60HRemainingTime = timeVacationRemainingOutput.getSuper60HRemainingTime();

                    //INPUT「時間消化申請」とINPUT「60H超休残数」を確認する
                    if (totalTimeAnnualLeave > 0 && super60HRemainingTime > 0 && (super60HRemainingTime - totalSixtyOvertime) > 0) {
                        throw new BusinessException("Msg_1687", "Com_ExsessHoliday", "Com_PaidHoliday", "Com_ExsessHoliday");
                    }
                } else {
                    //「@年休より優先する休暇．代休を優先」　と　INPUT「代休の時間管理区分」を確認する
                    if (acquisitionRule.get().getAnnualHoliday().isPriorityPause() && timeBaseManagementClass) {
                        //時間代休残数
                        int timeOfTimeOff = timeVacationRemainingOutput.getSubTimeLeaveRemainingTime();

                        //INPUT「時間消化申請」とINPUT「時間代休残数」を確認する
                        if (totalTimeAnnualLeave > 0 && timeOfTimeOff > 0 && (timeOfTimeOff - totalTimeOff) > 0) {
                            throw new BusinessException("Msg_1687", "Com_CompensationHoliday", "Com_PaidHoliday", "Com_CompensationHoliday");
                        }
                    }
                }
            }
        }
    }

    /**
     * 時間休暇残数チェック
     */
    private void remainingTimeVacationCheck(String companyId, TimeDigestAppType timeDigestAppType, TimeLeaveApplication timeLeaveApplication) {
        //4.社員の当月の期間を算出する
        PeriodCurrentMonth periodCurrentMonth =
            otherCommonAlgorithm.employeePeriodCurrentMonthCalculate(companyId, timeLeaveApplication.getEmployeeID(), GeneralDate.today());

        //登録時の残数チェック
        boolean chkSixtyHOvertime = timeLeaveApplication.getLeaveApplicationDetails().stream().filter(x -> x.getTimeDigestApplication().getOvertime60H().v() > 0).collect(Collectors.toList()).size() > 0;
        boolean chkHoursOfHoliday = timeLeaveApplication.getLeaveApplicationDetails().stream().filter(x -> x.getTimeDigestApplication().getTimeAnnualLeave().v() > 0).collect(Collectors.toList()).size() > 0;
        boolean chkHoursOfSubHoliday = timeLeaveApplication.getLeaveApplicationDetails().stream().filter(x -> x.getTimeDigestApplication().getTimeOff().v() > 0).collect(Collectors.toList()).size() > 0;
        InterimRemainCheckInputParam inputParam = new InterimRemainCheckInputParam(
                companyId,
                timeLeaveApplication.getEmployeeID(),
                new DatePeriod(periodCurrentMonth.getStartDate(), periodCurrentMonth.getStartDate().addYears(1).addDays(-1)),
                false,
                timeLeaveApplication.getAppDate().getApplicationDate(),
                new DatePeriod(
                        timeLeaveApplication.getOpAppStartDate().map(ApplicationDate::getApplicationDate).orElse(timeLeaveApplication.getAppDate().getApplicationDate()),
                        timeLeaveApplication.getOpAppEndDate().map(ApplicationDate::getApplicationDate).orElse(timeLeaveApplication.getAppDate().getApplicationDate())
                ),
                true,
                Collections.emptyList(),
                Collections.emptyList(),
                getAppData(timeLeaveApplication),
                timeDigestAppType == TimeDigestAppType.USE_COMBINATION ? chkHoursOfSubHoliday : timeDigestAppType == TimeDigestAppType.TIME_OFF,
                false,
                timeDigestAppType == TimeDigestAppType.USE_COMBINATION ? chkHoursOfHoliday : timeDigestAppType == TimeDigestAppType.TIME_ANNUAL_LEAVE,
                false,
                false,
                false,
                timeDigestAppType == TimeDigestAppType.USE_COMBINATION ? chkSixtyHOvertime : timeDigestAppType == TimeDigestAppType.SIXTY_H_OVERTIME
        );
        EarchInterimRemainCheck remainCheck = interimRemainDataMngCheckRegister.checkRegister(inputParam);

        if (remainCheck.isChkSubHoliday()
                || remainCheck.isChkPause()
                || remainCheck.isChkAnnual()
                || remainCheck.isChkFundingAnnual()
                || remainCheck.isChkSpecial()
                || remainCheck.isChkPublicHoliday()
                || remainCheck.isChkSuperBreak()) {
            throw new BusinessException("Msg_1409", timeDigestAppType.name);
        }
    }

    /**
     * 契約時間をチェック
     */
    private void checkContractTime(TimeLeaveApplication timeLeaveApplication, WorkingConditionItem workingConditionItem) {
        //1日の契約時間をチェックする
        Integer totalAnnualHoliday = 0;
        for (TimeLeaveApplicationDetail detail : timeLeaveApplication.getLeaveApplicationDetails()) {
            totalAnnualHoliday += detail.getTimeDigestApplication().getTimeOff().v();
            totalAnnualHoliday += detail.getTimeDigestApplication().getTimeAnnualLeave().v();
            totalAnnualHoliday += detail.getTimeDigestApplication().getChildTime().v();
            totalAnnualHoliday += detail.getTimeDigestApplication().getNursingTime().v();
            totalAnnualHoliday += detail.getTimeDigestApplication().getOvertime60H().v();
            totalAnnualHoliday += detail.getTimeDigestApplication().getTimeSpecialVacation().v();
        }
        if (totalAnnualHoliday > workingConditionItem.getContractTime().v()) {
            throw new BusinessException("Msg_1706");
        }
    }

    private List<AppRemainCreateInfor> getAppData(TimeLeaveApplication application) {
        List<AppRemainCreateInfor> apps = application.getLeaveApplicationDetails().stream().map(detail -> {
            List<GeneralDate> listAppDates = new ArrayList<>();
//            if (application.getOpAppStartDate().isPresent() && application.getOpAppEndDate().isPresent()) {
//                listAppDates.addAll(new DatePeriod(application.getOpAppStartDate().get().getApplicationDate(), application.getOpAppEndDate().get().getApplicationDate()).datesBetween());
//            } else {
//                listAppDates.add(application.getAppDate().getApplicationDate());
//            }
            return new AppRemainCreateInfor(
                    application.getEmployeeID(),
                    application.getAppID(),
                    application.getInputDate(),
                    application.getAppDate().getApplicationDate(),
                    EnumAdaptor.valueOf(application.getPrePostAtr().value, PrePostAtr.class),
                    EnumAdaptor.valueOf(application.getAppType().value, ApplicationType.class),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.of(new VacationTimeInfor(
                            detail.getTimeDigestApplication().getOvertime60H().v(),
                            detail.getAppTimeType(),
                            detail.getTimeDigestApplication().getTimeOff().v(),
                            detail.getTimeDigestApplication().getTimeAnnualLeave().v()
                    )),
                    Optional.empty(),
                    Optional.empty(),
                    application.getOpAppStartDate().map(ApplicationDate::getApplicationDate),
                    application.getOpAppEndDate().map(ApplicationDate::getApplicationDate),
                    listAppDates
            );
        }).collect(Collectors.toList());

        return apps;
    }
}
