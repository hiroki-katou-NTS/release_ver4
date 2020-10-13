package nts.uk.screen.at.app.kaf021.query.a;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.Year;
import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.function.dom.adapter.monthly.agreement.GetExcessTimesYearAdapter;
import nts.uk.ctx.at.record.dom.monthly.agreement.export.GetAgreementTime;
import nts.uk.ctx.at.record.dom.monthly.agreement.export.GetAgreementTimeOfMngPeriod;
import nts.uk.ctx.at.record.dom.monthly.agreement.monthlyresult.specialprovision.SpecialProvisionsOfAgreementRepo;
import nts.uk.ctx.at.record.dom.require.RecordDomRequireService;
import nts.uk.ctx.at.record.dom.standardtime.repository.AgreementOperationSettingRepository;

import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.*;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.agreement.management.setting.AgreementOperationSetting;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.screen.at.app.workrule.closure.ClosurePeriodForAllQuery;
import nts.uk.screen.at.app.workrule.closure.CurrentClosurePeriod;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class SpecialProvisionOfAgreementSelectionQuery {

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

    /**
     * 初期起動を行う
     */
    public StartupInfo initStarup() {
        String cid = AppContexts.user().companyId();

        // get(会社ID):36協定運用設定
        AgreementOperationSetting setting = getSetting(cid);

        // <call>(会社ID):List＜締めID, 現在の締め期間＞
        CurrentClosurePeriod closurePeriod = getClosure(cid);

        return new StartupInfo(setting.getStartingMonth().value, closurePeriod.getProcessingYm().v());
    }

    /**
     * 抽出した社員情報を一覧表示する（当月の申請種類）
     */
    public List<EmployeeAgreementTimeDto> getEmloyeeInfoForCurrentMonth(List<EmployeeBasicInfoDto> employees) {
        return getAgreementTime(employees, 0);
        // TODO: get(社員ID): 36協定特別条項の適用申請
    }

    /**
     * 抽出した社員情報を一覧表示する（翌月の申請種類）
     */
    public List<EmployeeAgreementTimeDto> getEmloyeeInfoForNextMonth(List<EmployeeBasicInfoDto> employees) {
        return getAgreementTime(employees, 1);
        // TODO: get(社員ID): 36協定特別条項の適用申請
    }

    /**
     * 抽出した社員情報を一覧表示する（年間の申請種類）
     */
    public List<EmployeeAgreementTimeDto> getEmloyeeInfoForYear(List<EmployeeBasicInfoDto> employees) {
        return getAgreementTime(employees, 0);
        // TODO: get(社員ID): 36協定特別条項の適用申請
    }

    private List<EmployeeAgreementTimeDto> getAgreementTime(List<EmployeeBasicInfoDto> employees, int monthAdd) {
        if (CollectionUtil.isEmpty(employees)) return new ArrayList<>();
        String cid = AppContexts.user().companyId();
        String sid = employees.get(0).getEmployeeId();
        GeneralDate baseDate = GeneralDate.today();
        val require = requireService.createRequire();
        val cacheCarrier = new CacheCarrier();
        List<String> employeeIds = employees.stream().map(EmployeeBasicInfoDto::getEmployeeId).distinct()
                .collect(Collectors.toList());

        // get(会社ID):36協定運用設定
        AgreementOperationSetting setting = getSetting(cid);

        // 社員に対応する処理締めを取得する
        Closure closureInfo = ClosureService.getClosureDataByEmployee(require, cacheCarrier, sid, baseDate);

        // 年月を指定して、36協定期間の年月を取得する
        YearMonth startYm = setting.getYearMonthOfAgreementPeriod(closureInfo.getClosureMonth().getProcessingYm().addMonths(monthAdd));
        YearMonth endYm = startYm.addMonths(11);
        YearMonth currentYm = YearMonth.of(baseDate.year(), baseDate.month());
        YearMonthPeriod yearMonthPeriod = new YearMonthPeriod(currentYm, endYm);
        Map<String, Map<Integer, AgreementTimeOfManagePeriod>> agreementTimeAll = new HashMap<>();
        for (String employeeId : employeeIds) {
            Map<Integer, AgreementTimeOfManagePeriod> timeAll = new HashMap<>();
            for (YearMonth ymIndex : yearMonthPeriod.yearMonthsBetween()) {
                timeAll.put(ymIndex.v(), null);
            }
            agreementTimeAll.put(employeeId, timeAll);
        }

        // [NO.612]年月期間を指定して管理期間の36協定時間を取得する
        Map<String, List<AgreementTimeOfManagePeriod>> agreementTimeOfMngPeriodAll = GetAgreementTimeOfMngPeriod
                .get(this.createRequire(), employeeIds, new YearMonthPeriod(startYm, endYm.addMonths(-1)))
                .stream().collect(Collectors.groupingBy(AgreementTimeOfManagePeriod::getSid));

        for (String employeeId : employeeIds) {
            Map<Integer, AgreementTimeOfManagePeriod> timeAll = agreementTimeAll.get(employeeId);
            for (YearMonth ymIndex : yearMonthPeriod.yearMonthsBetween()) {
                // 【NO.333】36協定時間の取得
                AgreementTimeOfManagePeriod time = GetAgreementTime.get(require, employeeId, ymIndex, new ArrayList<>(), baseDate, ScheRecAtr.RECORD);
                if (time != null) {
                    timeAll.put(ymIndex.v(), time);
                }
            }
        }

        Year fiscalYear = new Year(startYm.year());
        // [No.458]年間超過回数の取得
        Map<String, Integer> monthsExceededAll = getExcessTimesYearAdapter.algorithm(employeeIds, fiscalYear);

        Map<String, AgreementTimeYear> agreementTimeYearAll = new HashMap<>();
        Map<String, AgreMaxAverageTimeMulti> agreMaxAverageTimeMultiAll = new HashMap<>();
        for (EmployeeBasicInfoDto employee : employees) {
            // 36協定上限複数月平均時間と年間時間の取得(年度指定)
            Optional<AgreementTimeYear> agreementTimeYearOpt = GetAgreementTime.getYear(require,
                    employee.getEmployeeId(), yearMonthPeriod, baseDate, ScheRecAtr.RECORD);
            agreementTimeYearOpt.ifPresent(agreementTimeYear -> agreementTimeYearAll.put(employee.getEmployeeId(), agreementTimeYear));

            Optional<AgreMaxAverageTimeMulti> agreMaxAverageTimeMultiOpt = GetAgreementTime.getMaxAverageMulti(require,
                    new ArrayList<>(), employee.getEmployeeId(), startYm, baseDate, ScheRecAtr.RECORD);
            agreMaxAverageTimeMultiOpt.ifPresent(agreMaxAverageTimeMulti -> agreMaxAverageTimeMultiAll.put(employee.getEmployeeId(), agreMaxAverageTimeMulti));
        }

        return mappingEmployee(employees, startYm, endYm, agreementTimeOfMngPeriodAll, agreementTimeAll,
                agreementTimeYearAll, agreMaxAverageTimeMultiAll, monthsExceededAll);
    }

    private List<EmployeeAgreementTimeDto> mappingEmployee(List<EmployeeBasicInfoDto> employees,
                                                           YearMonth startYm, YearMonth endYm,
                                                           Map<String, List<AgreementTimeOfManagePeriod>> agreementTimeOfMngPeriodAll,
                                                           Map<String, Map<Integer, AgreementTimeOfManagePeriod>> agreementTimeAll,
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

            // fill data to each month: AgreementTimeOfManagePeriod, AgreementTimeDetail
            YearMonthPeriod yearMonthPeriod = new YearMonthPeriod(startYm, endYm);
            List<AgreementTimeOfManagePeriod> agrTimePeriods = new ArrayList<>();
            if (agreementTimeOfMngPeriodAll.containsKey(result.getEmployeeId())) {
                agrTimePeriods = agreementTimeOfMngPeriodAll.get(result.getEmployeeId());
            }
            /*Map<Integer, AgreementTimeDetail> agreementTimeDetailByMonth = new HashMap<>();
            if (agreementTimeDetailAll.containsKey(result.getEmployeeId())) {
                agreementTimeDetailByMonth = agreementTimeDetailAll.get(result.getEmployeeId());
            }*/
            mappingPeriodMonth(result, yearMonthPeriod, agrTimePeriods);

            // fill data AgreementTimeOutput
            mappingYearAndMonthAverage(result, agreementTimeYearAll, agreMaxAverageTimeMultiAll);

            // fill data monthsExceeded
            if (monthsExceededAll.containsKey(result.getEmployeeId())) {
                result.setExceededNumber(monthsExceededAll.get(result.getEmployeeId()));
            }

            results.add(result);
        }

        return results.stream().sorted(Comparator.comparing(EmployeeAgreementTimeDto::getEmployeeCode))
                .collect(Collectors.toList());
    }

    private void mappingPeriodMonth(EmployeeAgreementTimeDto result,
                                    YearMonthPeriod yearMonthPeriod,
                                    List<AgreementTimeOfManagePeriod> agrTimePeriods) {
        for (YearMonth ymIndex : yearMonthPeriod.yearMonthsBetween()) {
            AgreementTimeDto agreementTime = null;
            AgreementTimeDto agreementMaxTime = null;

            Optional<AgreementTimeOfManagePeriod> agrTimePeriodOpt = agrTimePeriods.stream()
                    .filter(x -> x.getYm() == ymIndex).findFirst();
            if (agrTimePeriodOpt.isPresent()) {
                AgreementTimeOfManagePeriod agrTimePeriod = agrTimePeriodOpt.get();
                agreementTime = new AgreementTimeDto(agrTimePeriod);
                agreementMaxTime = new AgreementTimeDto(agrTimePeriod); //TODO
            }

            /*if (agreementTimeDetailByMonth.containsKey(ymIndex.v())) {
                AgreementTimeDetail agreementTimeDetail = agreementTimeDetailByMonth.get(ymIndex.v());
                Optional<AgreMaxTimeOfMonthly> confirmedMaxOpt = agreementTimeDetail.getConfirmedMax();
                if (!confirmedMaxOpt.isPresent()) continue;
                agreementMaxTime = new AgreementTimeDto(confirmedMaxOpt.get());
            }*/

            AgreementTimeMonthDto agreementTimeMonth = new AgreementTimeMonthDto(ymIndex.v(), agreementTime, agreementMaxTime);
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
                                            Map<String, AgreementTimeYear> agreementTimeYearAll,
                                            Map<String, AgreMaxAverageTimeMulti> agreMaxAverageTimeMultiAll) {
        if (agreementTimeYearAll.containsKey(result.getEmployeeId())) {
            // fill year

            if (agreementTimeYearAll.containsKey(result.getEmployeeId())) {
                AgreementTimeYear agreementTimeYear = agreementTimeYearAll.get(result.getEmployeeId());
                result.setYear(new AgreementTimeYearDto(agreementTimeYear));
            }
            // fill average range month
            if (agreMaxAverageTimeMultiAll.containsKey(result.getEmployeeId())) {
                AgreMaxAverageTimeMulti agreMaxAverageTimeMulti = agreMaxAverageTimeMultiAll.get(result.getEmployeeId());
                for (AgreMaxAverageTime averageTime : agreMaxAverageTimeMulti.getAverageTimes()) {
                    YearMonthPeriod period = averageTime.getPeriod();
                    int rangeMonth = period.end().compareTo(period.start());
                    AgreementMaxAverageTimeDto average = new AgreementMaxAverageTimeDto(averageTime);
                    switch (rangeMonth) {
                        case 1:
                            result.setMonthAverage2(average);
                            break;
                        case 2:
                            result.setMonthAverage3(average);
                            break;
                        case 3:
                            result.setMonthAverage4(average);
                            break;
                        case 4:
                            result.setMonthAverage5(average);
                            break;
                        case 5:
                            result.setMonthAverage6(average);
                            break;
                    }
                }
            }
        }
    }

    private AgreementOperationSetting getSetting(String cid) {
        Optional<AgreementOperationSetting> settingOpt = agreementOperationSettingRepo.find(cid);
        if (!settingOpt.isPresent()) throw new BusinessException("Msg_1843");
        return settingOpt.get();
    }

    private CurrentClosurePeriod getClosure(String cid) {
        List<CurrentClosurePeriod> closurePeriods = closurePeriodForAllQuery.get(cid);
        if (CollectionUtil.isEmpty(closurePeriods)) throw new BusinessException("Msg_1843");
        return closurePeriods.get(0);
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
