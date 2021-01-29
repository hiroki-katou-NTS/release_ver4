package nts.uk.ctx.at.function.dom.outputitemsofannualworkledger;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.uk.ctx.at.function.dom.adapter.actualmultiplemonth.MonthlyRecordValueImport;
import nts.uk.ctx.at.function.dom.adapter.outputitemsofworkstatustable.AttendanceItemDtoValue;
import nts.uk.ctx.at.function.dom.adapter.outputitemsofworkstatustable.AttendanceResultDto;
import nts.uk.ctx.at.function.dom.commonform.ClosureDateEmployment;
import nts.uk.ctx.at.function.dom.commonform.GetSuitableDateByClosureDateUtility;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.OutputItem;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.OutputItemDetailAttItem;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.dto.StatusOfEmployee;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.enums.CommonAttributesOfForms;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.enums.DailyMonthlyClassification;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.enums.OperatorsCommonToForms;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeBasicInfoImport;
import nts.uk.ctx.at.shared.dom.adapter.workplace.config.info.WorkplaceInfor;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;

import javax.ejb.Stateless;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Query: 年間勤務台帳の表示内容を作成する
 */
@Stateless
public class CreateAnnualWorkLedgerContentQuery {
    public static List<AnnualWorkLedgerContent> getData(Require require, DatePeriod datePeriod, Map<String, EmployeeBasicInfoImport> lstEmployee,
                                                        AnnualWorkLedgerOutputSetting outputSetting, Map<String, WorkplaceInfor> lstWorkplaceInfor,
                                                        Map<String, ClosureDateEmployment> lstClosureDateEmployment) {
        if (outputSetting == null) {
            throw new BusinessException("Msg_1860");
        }
        List<AnnualWorkLedgerContent> result = new ArrayList<>();

        List<String> listSid = new ArrayList<>(lstEmployee.keySet());

        // 1 - Call 社員の指定期間中の所属期間を取得する
        val listEmployeeStatus = require.getListAffComHistByListSidAndPeriod(listSid, datePeriod);

        // 日次の場合 - 印刷対象フラグ　==　true
        val dailyOutputItems = outputSetting.getDailyOutputItemList().stream()
                .filter(x -> x.getDailyMonthlyClassification() == DailyMonthlyClassification.DAILY && x.isPrintTargetFlag())
                .collect(Collectors.toList());
        // 月次の場合 - 印刷対象フラグ　==　true
        val monthlyOutputItems = outputSetting.getMonthlyOutputItemList().stream()
                .filter(x -> x.getDailyMonthlyClassification() == DailyMonthlyClassification.MONTHLY && x.isPrintTargetFlag())
                .collect(Collectors.toList());
        if (dailyOutputItems.isEmpty() && monthlyOutputItems.isEmpty()) {
            throw new BusinessException("Msg_1860");
        }
        // Loop 「社員の会社所属状況」の「対象社員」
        listEmployeeStatus.parallelStream().forEach(emp -> {

            val employee = lstEmployee.get(emp.getEmployeeId());
            val workplaceInfo = lstWorkplaceInfor.get(emp.getEmployeeId());

            if (employee == null || workplaceInfo == null) return;

            // 日次データ
            DailyData dailyData = null;
            if (!dailyOutputItems.isEmpty()) {
                dailyData = getDailyData(require, emp, dailyOutputItems);
            }
            // 月次データ
            List<MonthlyData> lstMonthlyData = new ArrayList<>();
            String closureDate = null;
            String employmentCode = null;
            String employmentName = null;

            if (lstClosureDateEmployment.size() == 0) return;
            val closureDateEmployment = lstClosureDateEmployment.getOrDefault(emp.getEmployeeId(), null);
            if (closureDateEmployment == null) return;
            val closureOptional = closureDateEmployment.getClosure();
            if (closureOptional.isPresent()) {
                val closure = closureOptional.get();
                if (closure.getClosureHistories() != null && closure.getClosureHistories().size() > 0) {
                    val closureHistory = closure.getClosureHistories().get(0);
                    val closureDay = closureHistory.getClosureDate().getClosureDay().v();
                    if (!monthlyOutputItems.isEmpty()) {
                        lstMonthlyData = getMonthlyData(require, emp, monthlyOutputItems, closureDay);
                    }
                    closureDate = closureHistory.getClosureName().v();
                    employmentCode = closureDateEmployment.getEmploymentCode();
                    employmentName = closureDateEmployment.getEmploymentName();
                }
            }
            AnnualWorkLedgerContent model = new AnnualWorkLedgerContent(
                    dailyData,
                    lstMonthlyData,
                    employee.getEmployeeCode(),
                    employee.getEmployeeName(),
                    closureDate,
                    workplaceInfo.getWorkplaceCode(),
                    workplaceInfo.getWorkplaceName(),
                    employmentCode,
                    employmentName
            );
            result.add(model);
        });
        if (result.size() == 0) {
            throw new BusinessException("Msg_1860");
        }
        return result;
    }

    /**
     * 日次データ
     */
    private static DailyData getDailyData(Require require, StatusOfEmployee emp, List<DailyOutputItemsAnnualWorkLedger> dailyOutputItemList) {
        List<DailyValue> lstRightValue = new ArrayList<>();
        List<DailyValue> lstLeftValue = new ArrayList<>();
        String rightColumnName = null;
        CommonAttributesOfForms rightAttribute = null;
        String leftColumnName = null;
        CommonAttributesOfForms leftAttribute = null;
        val listIds = dailyOutputItemList.stream()
                .flatMap(x -> x.getSelectedAttendanceItemList().stream()
                        .map(OutputItemDetailAttItem::getAttendanceItemId))
                .distinct().collect(Collectors.toCollection(ArrayList::new));
        List<AttendanceResultDto> listAttendance = new ArrayList<>();
        for (val date : emp.getListPeriod()) {
            List<AttendanceResultDto> listValue = new ArrayList<>();
            try {
                listValue = require.getValueOf(Collections.singletonList(emp.getEmployeeId()), date, listIds);
            } catch (Exception e) {
                continue;
            }
            if (listValue == null) continue;
            listAttendance.addAll(listValue);
        }
        Map<GeneralDate, Map<Integer, AttendanceItemDtoValue>> allValue = listAttendance.stream()
                .collect(Collectors.toMap(AttendanceResultDto::getWorkingDate,
                        k -> k.getAttendanceItems().stream().filter(distinctByKey(AttendanceItemDtoValue::getItemId))
                                .collect(Collectors.toMap(AttendanceItemDtoValue::getItemId, l -> l))));
        // Loop 出力項目 日次
        for (int index = 0; index < dailyOutputItemList.size(); index++) {
            DailyOutputItemsAnnualWorkLedger item = dailyOutputItemList.get(index);
            val rank = item.getRank();
            if (index > 1) {
                break;
            }
            if (allValue == null) continue;
            val itemValue = new ArrayList<DailyValue>();
            allValue.forEach((key, value1) -> {
                val listAtId = item.getSelectedAttendanceItemList();
                StringBuilder character = new StringBuilder();
                Double actualValue = 0D;
                boolean alwayNull = true;
                if (item.getItemDetailAttributes() == CommonAttributesOfForms.WORK_TYPE ||
                        item.getItemDetailAttributes() == CommonAttributesOfForms.WORKING_HOURS ||
                        item.getItemDetailAttributes() == CommonAttributesOfForms.OTHER_CHARACTERS ||
                        item.getItemDetailAttributes() == CommonAttributesOfForms.OTHER_CHARACTER_NUMBER) {
                    for (val d : listAtId) {
                        val sub = value1.getOrDefault(d.getAttendanceItemId(), null);
                        if (sub == null || sub.getValue() == null) continue;
                        character.append(sub.getValue());

                    }
                    itemValue.add(
                            new DailyValue(null, character.toString(), key));
                } else {
                    for (val d : listAtId) {
                        val sub = value1.getOrDefault(d.getAttendanceItemId(), null);
                        if (sub == null || sub.getValue() == null) continue;
                        alwayNull = false;
                        actualValue = actualValue + ((d.getOperator() == OperatorsCommonToForms.ADDITION ? 1 : -1) * Double.parseDouble(sub.getValue()));
                    }
                    itemValue.add(new DailyValue(alwayNull ? null : actualValue, character.toString(), key));
                }

            });
            if (rank == 1) {
                leftColumnName = item.getName().v();
                leftAttribute = item.getItemDetailAttributes();
                lstLeftValue.addAll(itemValue);
            } else {
                lstRightValue.addAll(itemValue);
                rightColumnName = item.getName().v();
                rightAttribute = item.getItemDetailAttributes();
            }


        }

        return new DailyData(
                lstRightValue,
                lstLeftValue,
                rightColumnName,
                rightAttribute,
                leftColumnName,
                leftAttribute
        );
    }

    /**
     * 月次データ
     */
    private static List<MonthlyData> getMonthlyData(Require require, StatusOfEmployee emp, List<OutputItem> monthlyOutputItems, int closureDay) {
        List<MonthlyData> lstMonthlyData = new ArrayList<>();
        // Loop 出力項目 月次
        val listIds = monthlyOutputItems.stream()
                .flatMap(x -> x.getSelectedAttendanceItemList().stream()
                        .map(OutputItemDetailAttItem::getAttendanceItemId))
                .distinct().collect(Collectors.toCollection(ArrayList::new));
        List<MonthlyRecordValueImport> listAttendants = new ArrayList<>();
        for (val period : emp.getListPeriod()) {
            val yearMonthPeriod = GetSuitableDateByClosureDateUtility.convertPeriod(period, closureDay);
            List<MonthlyRecordValueImport> monthlyRecordValues = null;
            try {
                monthlyRecordValues = (require.getActualMultipleMonth(
                        Collections.singletonList(emp.getEmployeeId())
                        , yearMonthPeriod, listIds)).get(emp.getEmployeeId());
            } catch (Exception e) {
                continue;
            }

            if (monthlyRecordValues == null) continue;
            listAttendants.addAll(monthlyRecordValues);
        }
        Map<YearMonth, Map<Integer, ItemValue>> allValue = listAttendants.stream()
                .collect(Collectors.toMap(MonthlyRecordValueImport::getYearMonth,
                        k -> k.getItemValues().stream().filter(distinctByKey(ItemValue::getItemId))
                                .collect(Collectors.toMap(ItemValue::getItemId, l -> l))));
        // Loop 出力項目 日次
        for (OutputItem monthlyItem : monthlyOutputItems) {
            List<MonthlyValue> lstMonthlyValue = new ArrayList<>();
            // 「期間」の中にループを行い
            allValue.forEach((key, value1) -> {
                val monthlyRecordValues = monthlyItem.getSelectedAttendanceItemList();
                if (monthlyRecordValues != null) {
                    lstMonthlyValue.add(fromMonthlyRecordValue(monthlyItem, monthlyRecordValues, value1, key));
                }
            });

            lstMonthlyData.add(new MonthlyData(lstMonthlyValue, monthlyItem.getName().v(), monthlyItem.getItemDetailAttributes()));
        }
        return lstMonthlyData;
    }

    private static MonthlyValue fromMonthlyRecordValue(OutputItem monthlyItem, List<OutputItemDetailAttItem> selectedAttendanceItemList, Map<Integer, ItemValue> itemValueMap, YearMonth ym) {
        StringBuilder character = new StringBuilder();
        Double actualValue = 0d;
        for (OutputItemDetailAttItem d : selectedAttendanceItemList) {
            val subItem = itemValueMap.getOrDefault(d.getAttendanceItemId(), null);
            if (subItem == null) continue;
            val rawValue = subItem.getValue();
            if (monthlyItem.getItemDetailAttributes() == CommonAttributesOfForms.WORK_TYPE ||
                    monthlyItem.getItemDetailAttributes() == CommonAttributesOfForms.WORKING_HOURS ||
                    monthlyItem.getItemDetailAttributes() == CommonAttributesOfForms.OTHER_CHARACTERS ||
                    monthlyItem.getItemDetailAttributes() == CommonAttributesOfForms.OTHER_CHARACTER_NUMBER) {
                character.append(rawValue);
            } else {
                Double value = rawValue == null ? 0 : Double.parseDouble(rawValue);
                actualValue += value * (d.getOperator() == OperatorsCommonToForms.ADDITION ? 1 : -1);
            }

        }

        return new MonthlyValue(actualValue, character.toString(), ym);
    }

    public static interface Require {
        //[RQ 588] 社員の指定期間中の所属期間を取得する
        List<StatusOfEmployee> getListAffComHistByListSidAndPeriod(List<String> sid, DatePeriod datePeriod);

        List<AttendanceResultDto> getValueOf(List<String> employeeIds, DatePeriod workingDatePeriod, Collection<Integer> itemIds);

        // [No.495]勤怠項目IDを指定して月別実績の値を取得（複数レコードは合算）
        Map<String, List<MonthlyRecordValueImport>> getActualMultipleMonth(
                List<String> employeeIds, YearMonthPeriod period, List<Integer> itemIds);

    }

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
