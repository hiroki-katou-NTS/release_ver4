package nts.uk.screen.at.app.kaf021.query;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.task.parallel.ManagedParallelWithContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.Year;
import nts.arc.time.calendar.period.DatePeriod;
import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.adapter.monthly.agreement.GetExcessTimesYearAdapter;
import nts.uk.ctx.at.record.dom.monthly.agreement.export.AggregateAgreementTimeByYM;
import nts.uk.ctx.at.record.dom.monthly.agreement.export.GetAgreementTime;
import nts.uk.ctx.at.record.dom.monthly.agreement.export.GetAgreementTimeOfMngPeriod;
import nts.uk.ctx.at.record.dom.monthly.agreement.monthlyresult.specialprovision.*;
import nts.uk.ctx.at.record.dom.require.RecordDomRequireService;
import nts.uk.ctx.at.record.dom.standardtime.AgreementDomainService;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementOperationSettingRepository;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeMonth;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.*;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.setting.AgreementOperationSetting;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.timesetting.AgreementOneYear;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.timesetting.BasicAgreementSetting;
import nts.uk.ctx.bs.employee.pub.person.IPersonInfoPub;
import nts.uk.ctx.bs.employee.pub.person.PersonInfoExport;
import nts.uk.query.model.employee.EmployeeInformation;
import nts.uk.query.model.employee.EmployeeInformationQuery;
import nts.uk.query.model.employee.EmployeeInformationRepository;
import nts.uk.query.model.workplace.WorkplaceModel;
import nts.uk.screen.at.app.kaf021.query.a.*;
import nts.uk.screen.at.app.kaf021.query.c_d.*;
import nts.uk.screen.at.app.workrule.closure.ClosurePeriodForAllQuery;
import nts.uk.screen.at.app.workrule.closure.CurrentClosurePeriod;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * KAF021 ???????????????OCD
 *
 * @author Le Huu Dat
 */
@Stateless
public class SpecialProvisionOfAgreementQuery {
    @Inject
    private ClosurePeriodForAllQuery closurePeriodForAllQuery;
    @Inject
    private AgreementOperationSettingRepository agreementOperationSettingRepo;
    @Inject
    private RecordDomRequireService requireService;
    @Inject
    private GetExcessTimesYearAdapter getExcessTimesYearAdapter;
    @Inject
    private AgreementTimeOfManagePeriodRepository agreementTimeOfManagePeriodRepo;
    @Inject
    private SpecialProvisionsOfAgreementRepo specialProvisionsOfAgreementRepo;
    @Inject
    private ManagedParallelWithContext parallel;
    @Inject
    private IPersonInfoPub personInfoPub;
    @Inject
    private EmployeeInformationRepository employeeInformationRepo;

    /**
     * UKDesign.UniversalK.??????.KAF_??????.KAF021_36?????????????????????????????????.A???36??????????????????????????????????????????????????????.???????????????OCD
     * ?????????????????????
     */
    public StartupInfo initStarup() {
        String cid = AppContexts.user().companyId();

        // get(??????ID):36??????????????????
        AgreementOperationSetting setting = getSetting(cid);

        // <call>(??????ID):List?????????ID, ????????????????????????
        List<CurrentClosurePeriod> closurePeriods = closurePeriodForAllQuery.get(cid);

        return new StartupInfo(new AgreementOperationSettingDto(setting), closurePeriods.stream().map(x ->
                new CurrentClosurePeriodDto(x.getClosureId(), x.getProcessingYm().v())).collect(Collectors.toList()));
    }

    /**
     * UKDesign.UniversalK.??????.KAF_??????.KAF021_36?????????????????????????????????.A???36??????????????????????????????????????????????????????.???????????????OCD
     * ????????????????????????????????????????????????????????????????????????
     */
    public List<EmployeeAgreementTimeDto> getEmloyeeInfoForCurrentMonth(List<EmployeeBasicInfoDto> employees, int currentYm) {
        return getAgreementTime(employees, 0, false, YearMonth.of(currentYm));
    }

    /**
     * UKDesign.UniversalK.??????.KAF_??????.KAF021_36?????????????????????????????????.A???36??????????????????????????????????????????????????????.???????????????OCD
     * ????????????????????????????????????????????????????????????????????????
     */
    public List<EmployeeAgreementTimeDto> getEmloyeeInfoForNextMonth(List<EmployeeBasicInfoDto> employees, int currentYm) {
        return getAgreementTime(employees, 1, false, YearMonth.of(currentYm));
    }

    /**
     * UKDesign.UniversalK.??????.KAF_??????.KAF021_36?????????????????????????????????.A???36??????????????????????????????????????????????????????.???????????????OCD
     * ????????????????????????????????????????????????????????????????????????
     */
    public List<EmployeeAgreementTimeDto> getEmloyeeInfoForYear(List<EmployeeBasicInfoDto> employees, int currentYm) {
        return getAgreementTime(employees, 0, true, YearMonth.of(currentYm));
    }

    /**
     * UKDesign.UniversalK.??????.KAF_??????.KAF021_36?????????????????????????????????.C???36???????????????????????????????????????????????????.???????????????OCD
     * ?????????????????????
     */
    public SpecialProvisionOfAgreementAppListDto initDisplay(List<Integer> status) {
        String cid = AppContexts.user().companyId();
        String sid = AppContexts.user().employeeId();
        DatePeriod range = this.getRangeDate(cid);
        GeneralDate startDate = range.start();
        GeneralDate endDate = range.end();
        List<SpecialProvisionsOfAgreement> agreements = specialProvisionsOfAgreementRepo.getByPersonSID(sid,
                GeneralDateTime.ymdhms(startDate.year(), startDate.month(), startDate.day(), 0, 0, 0),
                GeneralDateTime.ymdhms(endDate.year(), endDate.month(), endDate.day(), 23, 59, 59),
                this.getApproveStatus(status));
        return mapData(agreements, startDate, endDate);
    }

    /**
     * UKDesign.UniversalK.??????.KAF_??????.KAF021_36?????????????????????????????????.C???36???????????????????????????????????????????????????.???????????????OCD
     * 36????????????????????????????????????????????????
     */
    public SpecialProvisionOfAgreementAppListDto search(GeneralDate startDate, GeneralDate endDate, List<Integer> status) {
        String sid = AppContexts.user().employeeId();
        List<SpecialProvisionsOfAgreement> agreements = specialProvisionsOfAgreementRepo.getByPersonSID(sid,
                GeneralDateTime.ymdhms(startDate.year(), startDate.month(), startDate.day(), 0, 0, 0),
                GeneralDateTime.ymdhms(endDate.year(), endDate.month(), endDate.day(), 23, 59, 59),
                this.getApproveStatus(status));
        return mapData(agreements, startDate, endDate);
    }

    /**
     * UKDesign.UniversalK.??????.KAF_??????.KAF021_36?????????????????????????????????.D???36?????????????????????????????????????????????.???????????????OCD
     * ?????????????????????
     */
    public SpecialProvisionOfAgreementAppListDto initDisplayApprove(List<Integer> status) {
        String cid = AppContexts.user().companyId();
        String sid = AppContexts.user().employeeId();
        DatePeriod range = this.getRangeDate(cid);
        GeneralDate startDate = range.start();
        GeneralDate endDate = range.end();
        List<SpecialProvisionsOfAgreement> agreements = specialProvisionsOfAgreementRepo.getBySID(sid,
                GeneralDateTime.ymdhms(startDate.year(), startDate.month(), startDate.day(), 0, 0, 0),
                GeneralDateTime.ymdhms(endDate.year(), endDate.month(), endDate.day(), 23, 59, 59),
                this.getApproveStatus(status));
        return mapData(agreements, startDate, endDate);
    }

    /**
     * UKDesign.UniversalK.??????.KAF_??????.KAF021_36?????????????????????????????????.D???36?????????????????????????????????????????????.???????????????OCD
     * 36????????????????????????????????????????????????
     */
    public SpecialProvisionOfAgreementAppListDto searchApprove(GeneralDate startDate, GeneralDate endDate, List<Integer> status) {
        String sid = AppContexts.user().employeeId();
        List<SpecialProvisionsOfAgreement> agreements = specialProvisionsOfAgreementRepo.getBySID(sid,
                GeneralDateTime.ymdhms(startDate.year(), startDate.month(), startDate.day(), 0, 0, 0),
                GeneralDateTime.ymdhms(endDate.year(), endDate.month(), endDate.day(), 23, 59, 59),
                this.getApproveStatus(status));
        return mapData(agreements, startDate, endDate);
    }

    private List<ApprovalStatus> getApproveStatus(List<Integer> status) {
        return status.stream().map(x -> EnumAdaptor.valueOf(x, ApprovalStatus.class)).collect(Collectors.toList());
    }

    private SpecialProvisionOfAgreementAppListDto mapData(List<SpecialProvisionsOfAgreement> agreements,
                                                          GeneralDate startDate, GeneralDate endDate) {
        String cid = AppContexts.user().companyId();
        String sid = AppContexts.user().employeeId();
        AgreementOperationSettingDto setting = new AgreementOperationSettingDto(this.getSetting(cid));

        // ??????ID???????????????????????????????????????
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

        // ????????????????????????
        Map<String, PersonInfoExport> enteredPersonInfoAll = personInfoPub.listPersonInfor(enteredPersonSIDs)
                .stream().collect(Collectors.toMap(PersonInfoExport::getEmployeeId, x -> x));
        // ????????????????????????
        Map<String, PersonInfoExport> approvalInfoAll = personInfoPub.listPersonInfor(approverSIDs)
                .stream().collect(Collectors.toMap(PersonInfoExport::getEmployeeId, x -> x));
        // ????????????????????????
        Map<String, PersonInfoExport> confirmerInfoAll = personInfoPub.listPersonInfor(confirmSIDs)
                .stream().collect(Collectors.toMap(PersonInfoExport::getEmployeeId, x -> x));

        // <<Public>> ??????????????????????????????
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

            if (empInfoAll.containsKey(employeeId)) {
                EmployeeInformation empInfo = empInfoAll.get(employeeId);
                Optional<WorkplaceModel> workplaceOpt = empInfo.getWorkplace();
                if (workplaceOpt.isPresent()) {
                    WorkplaceModel workplace = workplaceOpt.get();
                    app.setWorkplaceName(workplace.getWorkplaceName());
                }
                app.setEmployeeCode(empInfo.getEmployeeCode());
                app.setEmployeeName(empInfo.getBusinessName());
            }

            // ????????????
            app.setApplicationTime(new ApplicationTimeDto(agreement.getApplicationTime()));

            // ??????????????????
            app.setScreenDisplayInfo(new ScreenDisplayInfoDto(agreement.getScreenDisplayInfo()));

            // ????????????
            app.setReason(agreement.getReasonsForAgreement().v());

            // ????????????
            Optional<AgreementApprovalComments> approvalCommentOpt = agreement.getApprovalStatusDetails().getApprovalComment();
            approvalCommentOpt.ifPresent(agreementApprovalComments -> app.setComment(agreementApprovalComments.v()));

            // ?????????
            String enteredPersonSID = agreement.getEnteredPersonSID();
            if (enteredPersonInfoAll.containsKey(enteredPersonSID)) {
                app.setApplicant(enteredPersonInfoAll.get(enteredPersonSID).getBusinessName());
            }

            // ????????????
            app.setInputDate(agreement.getInputDate().toDate());

            // ?????????
            ApprovalStatusDetails approval = agreement.getApprovalStatusDetails();
            if (approval != null && approval.getApproveSID().isPresent()) {
                String approveSID = approval.getApproveSID().get();
                if (approvalInfoAll.containsKey(approveSID)) {
                    app.setApprover(approvalInfoAll.get(approveSID).getBusinessName());
                }
            }

            // ????????????
            app.setApprovalStatus(agreement.getApprovalStatusDetails().getApprovalStatus().value);

            // ???????????????
            List<ConfirmationStatusDetails> confirmDetails = agreement.getConfirmationStatusDetails();
            ConfirmationStatusDetails confirmed = confirmDetails.stream()
                    .filter(x -> x.getConfirmationStatus().equals(ConfirmationStatus.CONFIRMED))
                    .findFirst().orElse(null);
            String confirmerSID = null;
            ConfirmationStatus confirmationStatus;
            if (confirmed != null) {
                confirmerSID = confirmed.getConfirmerSID();
                confirmationStatus = ConfirmationStatus.CONFIRMED;
            } else {
                ConfirmationStatusDetails deny = confirmDetails.stream()
                        .filter(x -> x.getConfirmationStatus().equals(ConfirmationStatus.DENY))
                        .findFirst().orElse(null);
                if (deny != null) {
                    confirmerSID = deny.getConfirmerSID();
                    confirmationStatus = ConfirmationStatus.DENY;
                } else {
                    ConfirmationStatusDetails unconfirmed = confirmDetails.stream()
                            .filter(x -> x.getConfirmationStatus().equals(ConfirmationStatus.UNCONFIRMED))
                            .findFirst().orElse(null);
                    if (unconfirmed != null) {
                        confirmerSID = unconfirmed.getConfirmerSID();
                    }
                    confirmationStatus = ConfirmationStatus.UNCONFIRMED;
                }
            }
            if (confirmerSID != null) {
                if (confirmerInfoAll.containsKey(confirmerSID)) {
                    app.setConfirmer(confirmerInfoAll.get(confirmerSID).getBusinessName());
                }
            }

            // ????????????
            app.setConfirmStatus(confirmationStatus.value);

            // check can approve, confirm
            Optional<String> checkApprove = agreement.getListApproverSID().stream().filter(x -> x.equals(sid)).findAny();
            app.setCanApprove(checkApprove.isPresent());
            Optional<ConfirmationStatusDetails> checkConfirm = agreement.getConfirmationStatusDetails().stream().filter(x -> x.getConfirmerSID().contains(sid)).findAny();
            app.setCanConfirm(checkConfirm.isPresent());

            applications.add(app);
        }

        result.setApplications(applications.stream().sorted(Comparator.comparing(ApplicationListDto::getEmployeeCode)).collect(Collectors.toList()));
        result.setSetting(setting);
        return result;
    }

    private List<EmployeeAgreementTimeDto> getAgreementTime(List<EmployeeBasicInfoDto> employees, int monthAdd, boolean isYearMode, YearMonth currentYm) {
        if (CollectionUtil.isEmpty(employees)) return new ArrayList<>();
        String cid = AppContexts.user().companyId();
        GeneralDate baseDate = GeneralDate.today();
        val require = requireService.createRequire();
        List<String> employeeIds = employees.stream().map(EmployeeBasicInfoDto::getEmployeeId).distinct()
                .collect(Collectors.toList());

        // get(??????ID):36??????????????????
        AgreementOperationSetting setting = getSetting(cid);

        YearMonth ym = currentYm.addMonths(monthAdd);
        YearMonth startY = setting.getYearMonthOfAgreementPeriod(ym);
        YearMonth startYm = YearMonth.of(startY.year(), setting.getStartingMonth().getMonth());
        YearMonth endYm = startYm.addMonths(11);
        YearMonthPeriod yearMonthPeriodBefore = new YearMonthPeriod(startYm, ym.addMonths(-1));
        YearMonthPeriod yearMonthPeriodAfter = new YearMonthPeriod(ym, endYm);
        Map<String, Map<YearMonth, AgreementTimeOfManagePeriod>> agreementTimeAll = Collections.synchronizedMap(new HashMap<>());
        for (String employeeId : employeeIds) {
            agreementTimeAll.put(employeeId, new HashMap<>());
        }

        // [NO.612]??????????????????????????????????????????36???????????????????????????
        Map<String, List<AgreementTimeOfManagePeriod>> agreementTimeData = GetAgreementTimeOfMngPeriod
                .get(this.createRequire(), employeeIds, yearMonthPeriodBefore)
                .stream().collect(Collectors.groupingBy(AgreementTimeOfManagePeriod::getSid));

        for (Map.Entry<String, List<AgreementTimeOfManagePeriod>> data : agreementTimeData.entrySet()) {
            if (!agreementTimeAll.containsKey(data.getKey())) continue;

            List<AgreementTimeOfManagePeriod> agreements = data.getValue();
            for (AgreementTimeOfManagePeriod agreement : agreements) {
                agreementTimeAll.get(data.getKey()).put(agreement.getYm(), agreement);
            }
        }

        this.parallel.forEach(employeeIds, employeeId -> {
            Map<YearMonth, AgreementTimeOfManagePeriod> timeAll = agreementTimeAll.get(employeeId);
            yearMonthPeriodAfter.yearMonthsBetween().forEach(ymIndex -> {
                // ???NO.333???36?????????????????????
                AgreementTimeOfManagePeriod time = GetAgreementTime.get(require, employeeId, ymIndex, new ArrayList<>(), baseDate, ScheRecAtr.RECORD);
                if (time != null) {
                    timeAll.put(ymIndex, time);
                }
            });
        });

        Year fiscalYear = new Year(startYm.year());
        // [No.458]???????????????????????????
        Map<String, Integer> monthsExceededAll = getExcessTimesYearAdapter.algorithm(employeeIds, fiscalYear);

        Map<String, BasicAgreementSetting> basicSettingAll = new HashMap<>();
        Map<String, AgreementTimeYear> agreementTimeYearAll = new HashMap<>();
        Map<String, AgreMaxAverageTimeMulti> agreMaxAverageTimeMultiAll = new HashMap<>();
        this.parallel.forEach(employees, employee -> {
            // ??????????????????36?????????????????????????????????
            BasicAgreementSetting basicSetting = AgreementDomainService.getBasicSet(requireService.createRequire(), cid,
                    employee.getEmployeeId(), GeneralDate.today(), fiscalYear).getBasicSetting();
            if (basicSetting != null) {
                basicSettingAll.put(employee.getEmployeeId(), basicSetting);
            }

            Map<YearMonth, AgreementTimeOfManagePeriod> timeAll = agreementTimeAll.get(employee.getEmployeeId());

            // 36?????????????????????????????????????????????????????????(????????????)
            Optional<AgreementTimeYear> agreementTimeYearOpt = GetAgreementTime.getYear(require,
                    employee.getEmployeeId(), fiscalYear, baseDate, timeAll);
            agreementTimeYearOpt.ifPresent(agreementTimeYear -> agreementTimeYearAll.put(employee.getEmployeeId(),
                    agreementTimeYear));

            if (!isYearMode) {
                Map<YearMonth, AttendanceTimeMonth> times = timeAll.entrySet().stream()
                        .filter(x -> x.getValue().getYm().greaterThanOrEqualTo(ym.addMonths(-5))
                                && x.getValue().getYm().lessThanOrEqualTo(ym))
                        .collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().getAgreementTime().getAgreementTime()));
                AgreMaxAverageTimeMulti agreMaxAverageTimeMulti = AggregateAgreementTimeByYM.aggregate(require,
                        employee.getEmployeeId(), baseDate, ym, times);
                if (agreMaxAverageTimeMulti != null) {
                    agreMaxAverageTimeMultiAll.put(employee.getEmployeeId(), agreMaxAverageTimeMulti);
                }
            }
        });

        List<EmployeeAgreementTimeDto> empAgreementTimes = mappingEmployee(employees, startYm, endYm, agreementTimeAll,
                basicSettingAll, agreementTimeYearAll, agreMaxAverageTimeMultiAll, monthsExceededAll);

        this.parallel.forEach(empAgreementTimes, emp -> {
            Optional<SpecialProvisionsOfAgreement> specialAgreementOpt;
            if (isYearMode) {
                specialAgreementOpt = specialProvisionsOfAgreementRepo.getByYear(emp.getEmployeeId(), fiscalYear);
            } else {
                specialAgreementOpt = specialProvisionsOfAgreementRepo.getByYearMonth(emp.getEmployeeId(), ym);
            }

            if (specialAgreementOpt.isPresent()) {
                SpecialProvisionsOfAgreement specialAgreement = specialAgreementOpt.get();
                ApprovalStatus status = specialAgreement.getApprovalStatusDetails().getApprovalStatus();
                emp.setStatus(status.value);
            }
        });

        return empAgreementTimes;
    }

    private List<EmployeeAgreementTimeDto> mappingEmployee(List<EmployeeBasicInfoDto> employees,
                                                           YearMonth startYm, YearMonth endYm,
                                                           Map<String, Map<YearMonth, AgreementTimeOfManagePeriod>> agreementTimeAll,
                                                           Map<String, BasicAgreementSetting> basicSettingAll,
                                                           Map<String, AgreementTimeYear> agreementTimeYearAll,
                                                           Map<String, AgreMaxAverageTimeMulti> agreMaxAverageTimeMultiAll,
                                                           Map<String, Integer> monthsExceededAll) {
        List<EmployeeAgreementTimeDto> results = new ArrayList<>();
        for (EmployeeBasicInfoDto employee : employees) {
            // mapping data
            EmployeeAgreementTimeDto result = new EmployeeAgreementTimeDto();
            result.setEmployeeId(employee.getEmployeeId());
            result.setEmployeeName(employee.getEmployeeName());
            result.setEmployeeCode(employee.getEmployeeCode());
            result.setAffiliationCode(employee.getAffiliationCode());
            result.setAffiliationId(employee.getAffiliationId());
            result.setAffiliationName(employee.getAffiliationName());

            // fill data to each month: AgreementTimeOfManagePeriod
            YearMonthPeriod yearMonthPeriod = new YearMonthPeriod(startYm, endYm);
            Map<YearMonth, AgreementTimeOfManagePeriod> agrTimePeriods = new HashMap<>();
            if (agreementTimeAll.containsKey(result.getEmployeeId())) {
                agrTimePeriods = agreementTimeAll.get(result.getEmployeeId());
            }
            mappingPeriodMonth(result, yearMonthPeriod, agrTimePeriods);

            // fill data AgreementTimeOutput
            mappingYearAndMonthAverage(result, startYm.year(), basicSettingAll, agreementTimeYearAll, agreMaxAverageTimeMultiAll);

            // fill data monthsExceeded
            result.setExceededNumber(monthsExceededAll.getOrDefault(result.getEmployeeId(), 0));

            results.add(result);
        }

        return results.stream().sorted(Comparator.comparing(EmployeeAgreementTimeDto::getEmployeeCode))
                .collect(Collectors.toList());
    }

    private void mappingPeriodMonth(EmployeeAgreementTimeDto result,
                                    YearMonthPeriod yearMonthPeriod,
                                    Map<YearMonth, AgreementTimeOfManagePeriod> agrTimePeriods) {
        for (YearMonth ymIndex : yearMonthPeriod.yearMonthsBetween()) {
            AgreementTimeOfManagePeriod agrTimePeriod = agrTimePeriods.getOrDefault(ymIndex, null);
            AgreementTimeMonthDto agreementTimeMonth = new AgreementTimeMonthDto(ymIndex.v());
            if (agrTimePeriod != null) {
                agreementTimeMonth = new AgreementTimeMonthDto(agrTimePeriod);
            }

            switch (ymIndex.month()) {
                case 1:
                    result.setMonth1(agreementTimeMonth);
                    break;
                case 2:
                    result.setMonth2(agreementTimeMonth);
                    break;
                case 3:
                    result.setMonth3(agreementTimeMonth);
                    break;
                case 4:
                    result.setMonth4(agreementTimeMonth);
                    break;
                case 5:
                    result.setMonth5(agreementTimeMonth);
                    break;
                case 6:
                    result.setMonth6(agreementTimeMonth);
                    break;
                case 7:
                    result.setMonth7(agreementTimeMonth);
                    break;
                case 8:
                    result.setMonth8(agreementTimeMonth);
                    break;
                case 9:
                    result.setMonth9(agreementTimeMonth);
                    break;
                case 10:
                    result.setMonth10(agreementTimeMonth);
                    break;
                case 11:
                    result.setMonth11(agreementTimeMonth);
                    break;
                case 12:
                    result.setMonth12(agreementTimeMonth);
                    break;
            }
        }
    }

    private void mappingYearAndMonthAverage(EmployeeAgreementTimeDto result,
                                            int year,
                                            Map<String, BasicAgreementSetting> basicSettingAll,
                                            Map<String, AgreementTimeYear> agreementTimeYearAll,
                                            Map<String, AgreMaxAverageTimeMulti> agreMaxAverageTimeMultiAll) {
        // init
        result.setYear(new AgreementTimeYearDto(year));
        result.setMonthAverage2(new AgreementMaxAverageTimeDto());
        result.setMonthAverage3(new AgreementMaxAverageTimeDto());
        result.setMonthAverage4(new AgreementMaxAverageTimeDto());
        result.setMonthAverage5(new AgreementMaxAverageTimeDto());
        result.setMonthAverage6(new AgreementMaxAverageTimeDto());

        if (agreementTimeYearAll.containsKey(result.getEmployeeId())) {
            // fill year
            if (agreementTimeYearAll.containsKey(result.getEmployeeId())) {
                AgreementTimeYear agreementTimeYear = agreementTimeYearAll.get(result.getEmployeeId());
                BasicAgreementSetting basicSetting = basicSettingAll.get(result.getEmployeeId());
                result.setYear(new AgreementTimeYearDto(year, agreementTimeYear, basicSetting == null ? new AgreementOneYear() : basicSetting.getOneYear()));
            }
            // fill average range month
            if (agreMaxAverageTimeMultiAll.containsKey(result.getEmployeeId())) {
                AgreMaxAverageTimeMulti agreMaxAverageTimeMulti = agreMaxAverageTimeMultiAll.get(result.getEmployeeId());
                for (AgreMaxAverageTime averageTime : agreMaxAverageTimeMulti.getAverageTimes()) {
                    YearMonthPeriod period = averageTime.getPeriod();
                    int rangeMonth = period.yearMonthsBetween().size();
                    AgreementMaxAverageTimeDto average = new AgreementMaxAverageTimeDto(averageTime);
                    switch (rangeMonth) {
                        case 2:
                            result.setMonthAverage2(average);
                            break;
                        case 3:
                            result.setMonthAverage3(average);
                            break;
                        case 4:
                            result.setMonthAverage4(average);
                            break;
                        case 5:
                            result.setMonthAverage5(average);
                            break;
                        case 6:
                            result.setMonthAverage6(average);
                            break;
                    }
                }
            }
        }
    }

    private AgreementOperationSetting getSetting(String cid) {
        Optional<AgreementOperationSetting> settingOpt = agreementOperationSettingRepo.find(cid);
        if (!settingOpt.isPresent()) throw new RuntimeException("AgreementOperationSetting is null!");
        return settingOpt.get();
    }

    private DatePeriod getRangeDate(String cid) {
        List<CurrentClosurePeriod> closurePeriods = closurePeriodForAllQuery.get(cid);
        if (CollectionUtil.isEmpty(closurePeriods)) throw new RuntimeException("CurrentClosurePeriod is null!");

        YearMonth start = closurePeriods.stream().min(Comparator.comparing(CurrentClosurePeriod::getProcessingYm, Comparator.naturalOrder())).get().getProcessingYm();
        YearMonth end = closurePeriods.stream().max(Comparator.comparing(CurrentClosurePeriod::getProcessingYm, Comparator.naturalOrder())).get().getProcessingYm();

        return new DatePeriod(GeneralDate.ymd(start.year(), start.month(), 1), GeneralDate.ymd(end.year(), end.month(), 1).addMonths(2).addDays(-1));
    }

    private GetAgreementTimeOfMngPeriod.RequireM1 createRequire() {

        return new GetAgreementTimeOfMngPeriod.RequireM1() {

            @Override
            public List<AgreementTimeOfManagePeriod> agreementTimeOfManagePeriod(List<String> sids,
                                                                                 List<YearMonth> yearMonths) {

                return agreementTimeOfManagePeriodRepo.findBySidsAndYearMonths(sids, yearMonths);
            }

            @Override
            public Optional<AgreementOperationSetting> agreementOperationSetting(String cid) {

                return agreementOperationSettingRepo.find(cid);
            }
        };
    }
}
