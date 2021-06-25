package nts.uk.ctx.at.function.app.query.holidayconfirmationtable;


import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.function.dom.adapter.AffComHistItemImport;
import nts.uk.ctx.at.function.dom.adapter.EmployeeHistWorkRecordAdapter;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.dto.EmployeeInfor;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.adapter.workplace.config.info.WorkplaceInfor;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.OccurrenceDigClass;
import nts.uk.ctx.at.shared.dom.remainingnumber.algorithm.require.RemainNumberTempRequireService;
import nts.uk.ctx.at.shared.dom.remainingnumber.base.CompensatoryDayoffDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.MngHistDataAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.NumberRemainVacationLeaveRangeQuery;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.AccumulationAbsenceDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.BreakDayOffRemainMngRefactParam;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.SeqVacationAssociationInfo;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.SubstituteHolidayAggrResult;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.MonthlyVacationDays;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.MonthlyVacationRemainingTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.MonthlyVacationUsageTime;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.empinfo.grantremainingdata.daynumber.LeaveRemainingDayNumber;
import nts.uk.ctx.at.shared.dom.remainingnumber.specialholidaymng.interim.ManagermentAtr;
import nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.breakinfo.FixedManagementDataMonth;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 代休確認表の表示内容を作成する
 * UKDesign.UniversalK.就業.KDR_休暇帳表.KDR003_代休確認表.A:代休確認表.A:メニュー別OCD.代休確認表を作成する.代休確認表の表示内容を作成する.代休確認表の表示内容を作成する
 *
 * @author chinhhm
 */
@Stateless
public class CreateDisplayContentOfTheSubstituteLeaveQuery {

    @Inject
    EmployeeHistWorkRecordAdapter employeeHistWorkRecordAdapter;
    @Inject
    private ClosureRepository closureRepo;
    @Inject
    private ClosureEmploymentRepository closureEmploymentRepo;
    @Inject
    private ShareEmploymentAdapter shareEmploymentAdapter;
    @Inject
    private RemainNumberTempRequireService requireService;

    public List<DisplayContentsOfSubLeaveConfirmationTable> getDisplayContent(GeneralDate referenceDate, List<EmployeeInfor> basicInfoImportList,
                                                                              ManagermentAtr mngAtr, boolean moreSubstituteHolidaysThanHolidays, boolean moreHolidaysThanSubstituteHolidays, List<WorkplaceInfor> lstWorkplaceInfo) {


        val mapEmployee = basicInfoImportList.stream()
                .filter(distinctByKey(EmployeeInfor::getEmployeeId)).collect(Collectors.toMap(EmployeeInfor::getEmployeeId, e -> e));
        val mapWplInfo = lstWorkplaceInfo.stream().filter(distinctByKey(WorkplaceInfor::getWorkplaceId))
                .collect(Collectors.toMap(WorkplaceInfor::getWorkplaceId, e -> e));
        // 1. ① Get(「２」List<社員情報>．社員ID):所属会社履歴（社員別）
        val listSid = new ArrayList<>(mapEmployee.keySet());
        val listCompanyHist = employeeHistWorkRecordAdapter.getWplByListSid(listSid);
        // 2. ② Call 社員に対応する締め期間を取得する
        // 社員に対応する締め期間を取得する
        val cid = AppContexts.user().companyId();
        List<DisplayContentsOfSubLeaveConfirmationTable> rs = new ArrayList<>();
        for (val item : listCompanyHist) {
            val sid = item.getEmployeeId();

            // Call 社員に対応する締め期間を取得する
            DatePeriod period = ClosureService.findClosurePeriod(
                    ClosureService.createRequireM3(closureRepo, closureEmploymentRepo, shareEmploymentAdapter),
                    new CacheCarrier(), sid, referenceDate);
            val rq = requireService.createRequire();
            // Call RQ 203
            BreakDayOffRemainMngRefactParam inputRefactor = new BreakDayOffRemainMngRefactParam(
                    cid,
                    sid,
                    period,
                    false,
                    GeneralDate.today(),
                    false,
                    new ArrayList<>(),
                    Optional.empty(),
                    Optional.empty(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    Optional.empty(),
                    new FixedManagementDataMonth());
            val substituteHolidayAggrResult = NumberRemainVacationLeaveRangeQuery
                    .getBreakDayOffMngInPeriod(rq, inputRefactor);
            if (!checkShow(mngAtr, moreSubstituteHolidaysThanHolidays, moreHolidaysThanSubstituteHolidays, substituteHolidayAggrResult))
                continue;
            val employeeInfo = mapEmployee.get(sid);
            val wplInfo = mapWplInfo.get(employeeInfo.getWorkPlaceId());

            val employeeCode = employeeInfo.getEmployeeCode();
            val employeeName = employeeInfo.getEmployeeName();
            val workplaceCode = wplInfo.getWorkplaceCode();
            val workplaceName = wplInfo.getWorkplaceName();
            val hierarchyCode = wplInfo.getHierarchyCode();
            //・繰越数　．日数＝代休の集計結果．繰越日数
            NumberOfSubstituteHolidayCarriedForward numberCarriedForward = null;
            TotalNumberOfSubstituteHolidays totalNumberOfSubstituteHolidays = null;
            TotalNumberOfSubstituteHolidaysUsed numOfUse = null;
            NumberOfSubstituteHoliday numberOfRemaining = null;
            NumberOfUndigestedSubstitutes undeterminedNumber = null;
            Boolean er = false;
            if (mngAtr.equals(ManagermentAtr.DAYS)) {
                numberCarriedForward = new NumberOfSubstituteHolidayCarriedForward(
                        new LeaveRemainingDayNumber(substituteHolidayAggrResult.getCarryoverDay().v()),
                        Optional.empty());
                //・発生数　．日数＝代休の集計結果．発生日数
                totalNumberOfSubstituteHolidays = new TotalNumberOfSubstituteHolidays(
                        new MonthlyVacationDays(substituteHolidayAggrResult.getOccurrenceDay().v()),
                        Optional.empty());
                //・使用数　．日数＝代休の集計結果．使用日数
                numOfUse = new TotalNumberOfSubstituteHolidaysUsed(
                        new MonthlyVacationDays(substituteHolidayAggrResult.getDayUse().v()),
                        Optional.empty());
                //・残数　　．日数＝代休の集計結果．残日数
                numberOfRemaining = new NumberOfSubstituteHoliday(
                        new LeaveRemainingDayNumber(substituteHolidayAggrResult.getRemainDay().v()),
                        Optional.empty());
                //・未消化数．日数＝代休の集計結果．未消化日数
                undeterminedNumber = new NumberOfUndigestedSubstitutes(
                        new LeaveRemainingDayNumber(substituteHolidayAggrResult.getUnusedDay().v()),
                        Optional.empty());
                //・ER　＝残数．日数が負の場合、ture
                er = substituteHolidayAggrResult.getRemainDay() == null || substituteHolidayAggrResult.getRemainDay().v() < 0;

            }
            if (mngAtr.equals(ManagermentAtr.TIMES)) {
                // ・繰越数　．時間＝代休の集計結果．繰越時間
                numberCarriedForward = new NumberOfSubstituteHolidayCarriedForward(
                        null,
                        Optional.of(new MonthlyVacationRemainingTime(substituteHolidayAggrResult.getCarryoverTime().v())));
                //　・発生数　．時間＝代休の集計結果．発生時間
                totalNumberOfSubstituteHolidays = new TotalNumberOfSubstituteHolidays(
                        null,
                        Optional.of(new MonthlyVacationRemainingTime(substituteHolidayAggrResult.getOccurrenceTime().v())));
                //・使用数　．時間＝代休の集計結果．使用時間
                numOfUse = new TotalNumberOfSubstituteHolidaysUsed(
                        null,
                        Optional.of(new MonthlyVacationUsageTime(substituteHolidayAggrResult.getTimeUse().v())));
                //・残数　　．時間＝代休の集計結果．残時間
                numberOfRemaining = new NumberOfSubstituteHoliday(
                        null,
                        Optional.of(new MonthlyVacationRemainingTime(substituteHolidayAggrResult.getRemainTime().v())));
                //・未消化数．時間＝代休の集計結果．未消化時間
                undeterminedNumber = new NumberOfUndigestedSubstitutes(
                        null,
                        Optional.of(new MonthlyVacationRemainingTime(substituteHolidayAggrResult.getUnusedTime().v())));
                //・ER　＝残数．日数が負の場合、ture
                er = substituteHolidayAggrResult.getRemainDay() == null || substituteHolidayAggrResult.getRemainTime().v() < 0;
            }
            List<AffComHistItemImport> lstAffComHistItem = item.getLstAffComHistItem();
            List<OccurrenceAcquisitionDetails> occurrenceAcquisitionDetailsList = new ArrayList<>();
            List<LinkingInformation> listTyingInformation = new ArrayList<>();
            for (int i = 0; i < lstAffComHistItem.size(); i++) {
                val affComHistItem = lstAffComHistItem.get(i);
                //逐次発生の休暇明細一覧
                val lstAcctAbsenDetail = substituteHolidayAggrResult
                        .getVacationDetails().getLstAcctAbsenDetail();
                for (int j = 0; j < lstAcctAbsenDetail.size(); j++) {
                    val acctAbsenDetail = lstAcctAbsenDetail.get(j);
                    val dateOptional = acctAbsenDetail.getDateOccur().getDayoffDate();
                    //※１
                    Boolean isExpired = (dateOptional.isPresent() &&
                            affComHistItem.getDatePeriod().start().beforeOrEquals(dateOptional.get())
                            && (dateOptional.get()).beforeOrEquals(affComHistItem.getDatePeriod().end()));
                    //　・発生取得明細(i)．発生消化区分　　　＝ 代休の集計結果．逐次発生の休暇明細一覧(i)．休暇リスト．発生消化区分
                    OccurrenceDigClass occurrenceDigClass = acctAbsenDetail.getOccurrentClass();
                    CompensatoryDayoffDate date = null;
                    AccumulationAbsenceDetail.NumberConsecuVacation numberConsecuVacation = null;
                    MngHistDataAtr status = null;
                    GeneralDate deadline = null;
                    Optional<Boolean> isExpiredInCurrentMonth = Optional.empty();
                    //　【発生消化区分:発生の場合】
                    if (occurrenceDigClass.equals(OccurrenceDigClass.OCCURRENCE)) {
                        //　・発生取得明細(i)．年月日　　　　　＝ 代休の集計結果．逐次発生の休暇明細一覧(i)．休暇リスト．年月日
                        date = acctAbsenDetail.getDateOccur();
                        //  ・発生取得明細(i)．発生使用数．日数＝代休の集計結果．逐次発生の休暇明細一覧(i)．休暇リスト．発生数．日数
                        numberConsecuVacation = acctAbsenDetail.getNumberOccurren();
                        //  ・発生取得明細(i)．状態　　　　　　＝代休の集計結果．逐次発生の休暇明細一覧(i)．休暇リスト．状態
                        status = EnumAdaptor.valueOf(acctAbsenDetail.getDataAtr().value, MngHistDataAtr.class);
                        // ・発生取得明細(i)．期限日　　　　　＝代休の集計結果．逐次発生の休暇明細一覧(i)．休暇リスト．休暇発生明細．期限日
                        deadline = null; // TODO PHẢI QA.
                        //　・発生取得明細(i)．当月で期限切れ　＝※１
                        isExpiredInCurrentMonth = Optional.of(isExpired);
                    } else if (occurrenceDigClass.equals(OccurrenceDigClass.DIGESTION)) {
                        // 　・発生取得明細(i)．年月日　　　　　＝代休の集計結果．逐次発生の休暇明細一覧(i)．休暇リスト．年月日
                        date = acctAbsenDetail.getDateOccur();
                        //   ・発生取得明細(i)．発生使用数．日数＝代休の集計結果．逐次発生の休暇明細一覧(i)．休暇リスト．発生数．日数
                        numberConsecuVacation = acctAbsenDetail.getNumberOccurren();
                        //　・発生取得明細(i)．状態　　　　　　＝代休の集計結果．逐次発生の休暇明細一覧(i)．休暇リスト．状態
                        status = EnumAdaptor.valueOf(acctAbsenDetail.getDataAtr().value, MngHistDataAtr.class);
                    }
                    occurrenceAcquisitionDetailsList.add(new OccurrenceAcquisitionDetails(
                            date,
                            status,
                            numberConsecuVacation,
                            occurrenceDigClass,
                            isExpiredInCurrentMonth,
                            deadline));
                }
                List<SeqVacationAssociationInfo> lstSeqVacation = substituteHolidayAggrResult
                        .getLstSeqVacation();
                for (int j = 0; j < lstSeqVacation.size(); j++) {
                    val lstSeqVacationItem = lstSeqVacation.get(i);
                    //・紐付け情報(j)．発生日　＝代休の集計結果．逐次休暇の紐付け情報(j)．発生日
                    GeneralDate ymd = lstSeqVacationItem.getOutbreakDay();
                    // ・紐付け情報(j)．使用日　＝代休の集計結果．逐次休暇の紐付け情報(j)．使用日
                    MonthlyVacationDays dateOfUse = new MonthlyVacationDays(
                            lstSeqVacationItem.getDayNumberUsed().v()
                    );
                    //・紐付け情報(j)．使用日数＝代休の集計結果．逐次休暇の紐付け情報(j)．使用日数
                    GeneralDate occurrenceDate = lstSeqVacationItem.getDateOfUse();
                    listTyingInformation.add(new LinkingInformation(
                            ymd,
                            dateOfUse,
                            occurrenceDate));
                }
            }
            SubstituteHolidayOccurrenceInfo observationOfExitLeave = new SubstituteHolidayOccurrenceInfo(
                    er,
                    numOfUse, undeterminedNumber,
                    numberOfRemaining,
                    occurrenceAcquisitionDetailsList,
                    totalNumberOfSubstituteHolidays,
                    listTyingInformation,
                    numberCarriedForward
            );
            val sub = new DisplayContentsOfSubLeaveConfirmationTable(
                    employeeCode,
                    employeeName,
                    workplaceCode,
                    workplaceName,
                    hierarchyCode,
                    Optional.of(observationOfExitLeave)

            );
            rs.add(sub);
        }
        if (rs.isEmpty()) {
            throw new BusinessException("Msg_1894");
        }
        return rs;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private boolean checkShow(ManagermentAtr mngAtr, boolean moreSubstituteHolidaysThanHolidays,
                              boolean moreHolidaysThanSubstituteHolidays, SubstituteHolidayAggrResult substituteHolidayAggrResult) {
        if (mngAtr.equals(ManagermentAtr.DAYS)) {

            //　・「代休が休出より多い人」のチェックボックスが☑されている場合は、代休の集計結果．繰越日数＋代休の集計結果．発生日数＜代休の集計結果．使用日数
            //　・「休出が代休より多い人」のチェックボックスが☑されている場合は、代休の集計結果．繰越日数＋代休の集計結果．発生日数＞代休の集計結果．使用日数
            //　・両方☑されている場合は、　　　　　　　　　　　　　　　　　　　　代休の集計結果．繰越日数＋代休の集計結果．発生日数≠代休の集計結果．使用日数
            if (moreSubstituteHolidaysThanHolidays && substituteHolidayAggrResult != null) {
                val checkShow = (substituteHolidayAggrResult.getCarryoverDay() != null ? substituteHolidayAggrResult.getCarryoverDay().v() : 0)
                        + (substituteHolidayAggrResult.getOccurrenceDay() != null ? substituteHolidayAggrResult.getOccurrenceDay().v() : 0)
                        - (substituteHolidayAggrResult.getDayUse() != null ? substituteHolidayAggrResult.getDayUse().v() : 0);
                if (checkShow > 0) return false;
            }
            if (moreHolidaysThanSubstituteHolidays && substituteHolidayAggrResult != null) {
                val checkShow = (substituteHolidayAggrResult.getCarryoverDay() != null ? substituteHolidayAggrResult.getCarryoverDay().v() : 0)
                        + (substituteHolidayAggrResult.getOccurrenceDay() != null ? substituteHolidayAggrResult.getOccurrenceDay().v() : 0)
                        - (substituteHolidayAggrResult.getDayUse() != null ? substituteHolidayAggrResult.getDayUse().v() : 0);

                if (checkShow < 0) return false;
            }
            if (moreSubstituteHolidaysThanHolidays && moreHolidaysThanSubstituteHolidays && substituteHolidayAggrResult != null) {
                val checkShow = (substituteHolidayAggrResult.getCarryoverDay() != null ? substituteHolidayAggrResult.getCarryoverDay().v() : 0)
                        + (substituteHolidayAggrResult.getOccurrenceDay() != null ? substituteHolidayAggrResult.getOccurrenceDay().v() : 0)
                        - (substituteHolidayAggrResult.getDayUse() != null ? substituteHolidayAggrResult.getDayUse().v() : 0);
                if (checkShow == 0) return false;
            }
        } else if (mngAtr.equals(ManagermentAtr.TIMES)) {
            //　・「代休が休出より多い人」のチェックボックスが☑されている場合は、代休の集計結果．繰越時間＋代休の集計結果．発生時間＜代休の集計結果．使用時間
            //　・「休出が代休より多い人」のチェックボックスが☑されている場合は、代休の集計結果．繰越時間＋代休の集計結果．発生時間＞代休の集計結果．使用時間
            //　・両方☑されている場合は、　　　　　　　　　　　　　　　　　　　　代休の集計結果．繰越時間＋代休の集計結果．発生時間≠代休の集計結果．使用時間
            if (moreSubstituteHolidaysThanHolidays && substituteHolidayAggrResult != null) {
                val checkShow = (substituteHolidayAggrResult.getCarryoverTime() != null ? substituteHolidayAggrResult.getCarryoverTime().v() : 0)
                        + (substituteHolidayAggrResult.getOccurrenceTime() != null ? substituteHolidayAggrResult.getOccurrenceTime().v() : 0)
                        - (substituteHolidayAggrResult.getTimeUse() != null ? substituteHolidayAggrResult.getTimeUse().v() : 0);
                if (checkShow > 0) return false;
            }
            if (moreHolidaysThanSubstituteHolidays && substituteHolidayAggrResult != null) {
                val checkShow = (substituteHolidayAggrResult.getCarryoverTime() != null ? substituteHolidayAggrResult.getCarryoverTime().v() : 0)
                        + (substituteHolidayAggrResult.getOccurrenceTime() != null ? substituteHolidayAggrResult.getOccurrenceTime().v() : 0)
                        - (substituteHolidayAggrResult.getTimeUse() != null ? substituteHolidayAggrResult.getTimeUse().v() : 0);

                if (checkShow < 0) return false;
            }
            if (moreSubstituteHolidaysThanHolidays && moreHolidaysThanSubstituteHolidays && substituteHolidayAggrResult != null) {
                val checkShow = (substituteHolidayAggrResult.getCarryoverTime() != null ? substituteHolidayAggrResult.getCarryoverTime().v() : 0)
                        + (substituteHolidayAggrResult.getOccurrenceTime() != null ? substituteHolidayAggrResult.getOccurrenceTime().v() : 0)
                        - (substituteHolidayAggrResult.getTimeUse() != null ? substituteHolidayAggrResult.getTimeUse().v() : 0);
                if (checkShow == 0) return false;
            }
        }
        return false;
    }
}
