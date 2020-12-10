package nts.uk.file.at.app.export.outputworkstatustable;

import lombok.AllArgsConstructor;
import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.function.app.query.outputworkstatustable.GetDetailOutputSettingWorkStatusQuery;
import nts.uk.ctx.at.function.dom.adapter.outputitemsofworkstatustable.AffComHistAdapter;
import nts.uk.ctx.at.function.dom.adapter.outputitemsofworkstatustable.AttendanceItemServiceAdapter;
import nts.uk.ctx.at.function.dom.adapter.outputitemsofworkstatustable.AttendanceResultDto;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.CreateDisplayContentWorkStatusDService;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.DisplayContentWorkStatus;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.WorkStatusOutputSettings;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.dto.*;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffAtWorkplaceImport;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeBasicInfoImport;
import nts.uk.ctx.at.shared.dom.adapter.workplace.config.info.WorkplaceConfigInfoAdapter;
import nts.uk.ctx.at.shared.dom.adapter.workplace.config.info.WorkplaceInfor;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.sys.gateway.dom.adapter.company.CompanyBsAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.company.CompanyBsImport;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.calendar.date.ClosureDate;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class OutputFileWorkStatusService extends ExportService<OutputFileWorkStatusFileQuery> {
    @Inject
    private EmpEmployeeAdapter empEmployeeAdapter;

    @Inject
    private CompanyBsAdapter companyBsAdapter;

    @Inject
    private AffWorkplaceAdapter affWorkplaceAdapter;

    @Inject
    private WorkplaceConfigInfoAdapter workplaceConfigInfoAdapter;

    @Inject
    private DisplayWorkStatusReportGenerator displayGenerator;

    @Inject
    private AffComHistAdapter affComHistAdapter;

    @Inject
    private AttendanceItemServiceAdapter itemServiceAdapter;

    @Inject
    private ClosureRepository closureRepository;

    @Inject
    private GetDetailOutputSettingWorkStatusQuery getDetailOutputSettingWorkStatusQuery;

    @Override
    protected void handle(ExportServiceContext<OutputFileWorkStatusFileQuery> context) {
        OutputFileWorkStatusFileQuery query = context.getQuery();
        YearMonth targetDate = new YearMonth(query.getTargetDate());
        List<String> lstEmpIds = query.getLstEmpIds();
        // TODO DANG QA
        val cl = closureRepository.findById(AppContexts.user().companyId(), query.getClosureId());
        val basedateNow = GeneralDate.today();
        if (!cl.isPresent() || cl.get().getHistoryByBaseDate(basedateNow) == null) {
            throw new BusinessException("Còn QA");
        }
        val closureDate = cl.get().getHistoryByBaseDate(basedateNow).getClosureDate();

        DatePeriod datePeriod = this.getFromClosureDate(targetDate, closureDate);
        // [No.600]社員ID（List）から社員コードと表示名を取得（削除社員考慮）
        List<EmployeeBasicInfoImport> lstEmployeeInfo = empEmployeeAdapter.getEmpInfoLstBySids(lstEmpIds, datePeriod, true, true);
        // 2 Call 会社を取得する
        String companyId = AppContexts.user().companyId();
        CompanyBsImport companyInfo = companyBsAdapter.getCompanyByCid(companyId);
        // 3 Call 社員ID（List）と基準日から所属職場IDを取得
        GeneralDate baseDate = datePeriod.end();
        List<AffAtWorkplaceImport> lstAffAtWorkplaceImport = affWorkplaceAdapter
                .findBySIdAndBaseDate(lstEmpIds, baseDate);
        List<EmployeeInfor> employeeInfoList = new ArrayList<EmployeeInfor>();
        lstEmployeeInfo.forEach(e -> {
            val wpl = lstAffAtWorkplaceImport.stream().filter(i -> i.getEmployeeId().equals(e.getSid())).findFirst();
            employeeInfoList.add(new EmployeeInfor(
                    e.getSid(),
                    e.getEmployeeCode(),
                    e.getEmployeeName(),
                    wpl.isPresent() ? wpl.get().getWorkplaceId() : null
            ));
        });
        List<String> listWorkplaceId = lstAffAtWorkplaceImport.stream()
                .map(AffAtWorkplaceImport::getWorkplaceId).collect(Collectors.toList());
        // 3.1 Call [No.560]職場IDから職場の情報をすべて取得する
        List<WorkplaceInfor> lstWorkplaceInfo = workplaceConfigInfoAdapter.getWorkplaceInforByWkpIds(companyId, listWorkplaceId, baseDate);

        List<WorkPlaceInfo> placeInfoList = lstWorkplaceInfo.stream()
                .map(e -> new WorkPlaceInfo(e.getWorkplaceId(), e.getWorkplaceCode(), e.getWorkplaceName()))
                .collect(Collectors.toList());

        RequireImpl require = new RequireImpl(itemServiceAdapter, affComHistAdapter);
        // 4. 勤務状況表の出力設定の詳細を取得する.
        WorkStatusOutputSettings workStatusOutputSetting = getDetailOutputSettingWorkStatusQuery.getDetail(query.getSettingId());
        // 5 Call 勤務状況表の表示内容を作成する:
        val listData = CreateDisplayContentWorkStatusDService.displayContentsOfWorkStatus(require, datePeriod,
                employeeInfoList, workStatusOutputSetting, placeInfoList);
        val wplaceSort = listData.stream().filter(Objects::nonNull).map(DisplayContentWorkStatus::getWorkPlaceCode).distinct().collect(Collectors.toCollection(TreeSet::new));
        val listRs = new ArrayList<ExportExcelDto>();

        wplaceSort.forEach(e -> {
            val item = listData.stream().filter(i -> i.getWorkPlaceCode().equals(e))
                    .sorted(Comparator.comparing(DisplayContentWorkStatus::getEmployeeCode))
                    .map(j -> new DisplayContentWorkStatus(
                            j.getEmployeeCode(),
                            j.getEmployeeName(),
                            j.getWorkPlaceCode(),
                            j.getWorkPlaceName(),
                            j.getOutputItemOneLines()
                    )).collect(Collectors.toList());

            listRs.add(new ExportExcelDto(
                    item.get(0).getWorkPlaceCode(),
                    item.get(0).getWorkPlaceName(),
                    item
            ));
        });
        val result = new OutPutWorkStatusContent(
                listRs,
                datePeriod,
                query.getMode(),
                workStatusOutputSetting.getSettingName().v(),
                companyInfo.getCompanyName(),
                query.isPageBreak(),
                query.isZeroDisplay()
        );
        this.displayGenerator.generate(context.getGeneratorContext(), result);
    }

    private DatePeriod getFromClosureDate(YearMonth yearMonth, ClosureDate closureDate) {
        Integer closureDay = closureDate.getClosureDay().v();
        val baseDate = GeneralDate.ymd(yearMonth.year(), yearMonth.month(), closureDay);
        val date = GeneralDate.ymd(yearMonth.year(), yearMonth.month(), baseDate.lastDateInMonth());
        if (closureDate.getLastDayOfMonth()) {
            return new DatePeriod(baseDate, date);
        }
        return new DatePeriod(baseDate.addMonths(-1).addDays(1), baseDate);
    }

    @AllArgsConstructor
    public class RequireImpl implements CreateDisplayContentWorkStatusDService.Require {
        private AttendanceItemServiceAdapter itemServiceAdapter;
        private AffComHistAdapter affComHistAdapter;

        @Override
        public List<StatusOfEmployee> getListAffComHistByListSidAndPeriod(List<String> sid, DatePeriod datePeriod) {
            return affComHistAdapter.getListAffComHist(sid, datePeriod);
        }

        @Override
        public List<AttendanceResultDto> getValueOf(List<String> employeeIds, DatePeriod workingDatePeriod, Collection<Integer> itemIds) {
            return itemServiceAdapter.getValueOf(employeeIds, workingDatePeriod, itemIds);
        }
    }
}
