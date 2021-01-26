package nts.uk.file.at.app.export.annualworkledger;

import lombok.AllArgsConstructor;
import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.arc.time.calendar.period.DatePeriod;
import nts.arc.time.calendar.period.YearMonthPeriod;
import nts.uk.ctx.at.function.app.find.annualworkledger.AnnualWorkLedgerOutputSettingFinder;
import nts.uk.ctx.at.function.dom.adapter.actualmultiplemonth.ActualMultipleMonthAdapter;
import nts.uk.ctx.at.function.dom.adapter.actualmultiplemonth.MonthlyRecordValueImport;
import nts.uk.ctx.at.function.dom.adapter.outputitemsofworkstatustable.AffComHistAdapter;
import nts.uk.ctx.at.function.dom.adapter.outputitemsofworkstatustable.AttendanceItemServiceAdapter;
import nts.uk.ctx.at.function.dom.adapter.outputitemsofworkstatustable.AttendanceResultDto;
import nts.uk.ctx.at.function.dom.commonform.ClosureDateEmployment;
import nts.uk.ctx.at.function.dom.commonform.GetClosureDateEmploymentDomainService;
import nts.uk.ctx.at.function.dom.outputitemsofannualworkledger.AnnualWorkLedgerContent;
import nts.uk.ctx.at.function.dom.outputitemsofannualworkledger.AnnualWorkLedgerExportDataSource;
import nts.uk.ctx.at.function.dom.outputitemsofannualworkledger.AnnualWorkLedgerOutputSetting;
import nts.uk.ctx.at.function.dom.outputitemsofannualworkledger.CreateAnnualWorkLedgerContentQuery;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.dto.EmployeeInfor;
import nts.uk.ctx.at.function.dom.outputitemsofworkstatustable.dto.StatusOfEmployee;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffAtWorkplaceImport;
import nts.uk.ctx.at.record.dom.adapter.workplace.affiliate.AffWorkplaceAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeBasicInfoImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.ShareEmploymentAdapter;
import nts.uk.ctx.at.shared.dom.adapter.workplace.config.info.WorkplaceConfigInfoAdapter;
import nts.uk.ctx.at.shared.dom.adapter.workplace.config.info.WorkplaceInfor;
import nts.uk.ctx.at.shared.dom.workrule.closure.Closure;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureEmploymentRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureRepository;
import nts.uk.ctx.at.shared.dom.workrule.closure.service.ClosureService;
import nts.uk.ctx.sys.gateway.dom.adapter.company.CompanyBsAdapter;
import nts.uk.ctx.sys.gateway.dom.adapter.company.CompanyBsImport;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 年間勤務台帳のExcelファイルを出力する
 */
@Stateless
public class AnnualWorkLedgerExportService extends ExportService<AnnualWorkLedgerFileQuery> {

    @Inject
    private EmpEmployeeAdapter empEmployeeAdapter;

    @Inject
    private CompanyBsAdapter companyBsAdapter;

    @Inject
    private AffWorkplaceAdapter affWorkplaceAdapter;

    @Inject
    private WorkplaceConfigInfoAdapter workplaceConfigInfoAdapter;

    @Inject
    private ShareEmploymentAdapter shareEmploymentAdapter;

    @Inject
    private ClosureRepository closureRepository;

    @Inject
    private ClosureEmploymentRepository closureEmploymentRepository;

    @Inject
    private AnnualWorkLedgerOutputSettingFinder annualWorkLedgerOutputSettingFinder;

    @Inject
    private AffComHistAdapter affComHistAdapter;

    @Inject
    private AttendanceItemServiceAdapter itemServiceAdapter;

    @Inject
    private ActualMultipleMonthAdapter actualMultipleMonthAdapter;

    @Inject
    private DisplayAnnualWorkLedgerReportGenerator displayGenerator;

    /**
     * 勤務状況表の対象ファイルを出力する :: ファイルの出力
     *
     * @param context
     */
    @Override
    protected void handle(ExportServiceContext<AnnualWorkLedgerFileQuery> context) {
        AnnualWorkLedgerFileQuery query = context.getQuery();
        YearMonth yearMonthStart = new YearMonth(query.getStartMonth());
        YearMonth yearMonthEnd = new YearMonth(query.getEndMonth());
        YearMonthPeriod yearMonthPeriod = new YearMonthPeriod(yearMonthStart, yearMonthEnd);

        val cl = closureRepository.findById(AppContexts.user().companyId(), query.getClosureId());
        val basedateNow = GeneralDate.today();
        if (!cl.isPresent() || cl.get().getHistoryByBaseDate(basedateNow) == null) {
            throw new BusinessException("");
        }
        val closureDate = cl.get().getHistoryByBaseDate(basedateNow).getClosureDate();
        List<String> lstEmpIds = query.getLstEmpIds();
        DatePeriod datePeriod = this.getFromClosureDate(yearMonthStart, yearMonthEnd, closureDate.getClosureDay().v());
        GeneralDate baseDate = datePeriod.end();
        String companyId = AppContexts.user().companyId();

        // 1 Call [No.600]社員ID（List）から社員コードと表示名を取得（削除社員考慮）
        List<EmployeeBasicInfoImport> lstEmployeeInfo = empEmployeeAdapter.getEmpInfoLstBySids(lstEmpIds, datePeriod, true, false);
        Map<String, EmployeeBasicInfoImport> mapEmployeeInfo = lstEmployeeInfo.stream().filter(distinctByKey(EmployeeBasicInfoImport::getSid))
                .collect(Collectors.toMap(EmployeeBasicInfoImport::getSid, i -> i));

        // 2 Call 会社を取得する
        CompanyBsImport companyInfo = companyBsAdapter.getCompanyByCid(companyId);

        // 3 Call 社員ID（List）と基準日から所属職場IDを取得
        List<AffAtWorkplaceImport> lstAffAtWorkplaceImport = affWorkplaceAdapter.findBySIdAndBaseDate(lstEmpIds, baseDate);
        List<String> listWorkplaceId = lstAffAtWorkplaceImport.stream().map(AffAtWorkplaceImport::getWorkplaceId).distinct()
                .collect(Collectors.toList());
        // 3.1 Call [No.560]職場IDから職場の情報をすべて取得する
        List<WorkplaceInfor> lstWorkplaceInfo = workplaceConfigInfoAdapter.getWorkplaceInforByWkpIds(companyId, listWorkplaceId, baseDate);
        Map<String, WorkplaceInfor> mapWorkplaceInfo = lstWorkplaceInfo.stream().filter(distinctByKey(WorkplaceInfor::getWorkplaceId))
                .collect(Collectors.toMap(WorkplaceInfor::getWorkplaceId, i -> i));

        Map<String, WorkplaceInfor> mapEmployeeWorkplace = new HashMap<>();
        lstAffAtWorkplaceImport.forEach(x -> {
            WorkplaceInfor workplaceInfor = mapWorkplaceInfo.get(x.getWorkplaceId());
            mapEmployeeWorkplace.put(x.getEmployeeId(), workplaceInfor);
        });

        // 4 Call 基準日で社員の雇用と締め日を取得する
        RequireClosureDateEmploymentService require1 = new RequireClosureDateEmploymentService(
                shareEmploymentAdapter, closureRepository, closureEmploymentRepository);
        List<ClosureDateEmployment> lstClosureDateEmployment = GetClosureDateEmploymentDomainService.get(require1, baseDate, lstEmpIds);
        Map<String, ClosureDateEmployment> mapClosureDateEmployment = lstClosureDateEmployment.stream().filter(distinctByKey(ClosureDateEmployment::getEmployeeId))
                .collect(Collectors.toMap(ClosureDateEmployment::getEmployeeId, i -> i));

        // 5 Call 年間勤務台帳の出力設定の詳細を取得する
        Optional<AnnualWorkLedgerOutputSetting> outputSetting = annualWorkLedgerOutputSettingFinder.getById(query.getSettingId());
        if (!outputSetting.isPresent()) {
            throw new BusinessException("Msg_1898");
        }

        // 6 Call 年間勤務台帳の表示内容を作成する
        RequireCreateAnnualWorkLedgerContentService require2 = new RequireCreateAnnualWorkLedgerContentService(
                affComHistAdapter, itemServiceAdapter, actualMultipleMonthAdapter);
        List<AnnualWorkLedgerContent> lstContent = CreateAnnualWorkLedgerContentQuery.getData(require2,
                datePeriod, mapEmployeeInfo, outputSetting.get(), mapEmployeeWorkplace, mapClosureDateEmployment);
        Comparator<AnnualWorkLedgerContent> compare = Comparator
                .comparing(AnnualWorkLedgerContent::getWorkplaceCode)
                .thenComparing(AnnualWorkLedgerContent::getEmployeeCode);
        val lsSorted = lstContent.stream().sorted(compare).collect(Collectors.toList());
        // 7 年間勤務台帳を作成する
        AnnualWorkLedgerExportDataSource dataSource = new AnnualWorkLedgerExportDataSource(
                query.getMode(),
                companyInfo.getCompanyName(),
                outputSetting.get(),
                yearMonthPeriod,
                closureDate,
                query.isZeroDisplay(),
                lsSorted
        );
        displayGenerator.generate(context.getGeneratorContext(), dataSource);
    }

    private DatePeriod getFromClosureDate(YearMonth startMonth, YearMonth endMonth, int closureDay) {
        GeneralDate startDate = GeneralDate.ymd(startMonth.year(), startMonth.month(),
                Math.min(closureDay, startMonth.lastDateInMonth())).addDays(1);
        GeneralDate endDate = GeneralDate.ymd(endMonth.year(), endMonth.month(),
                Math.min(closureDay, endMonth.lastDateInMonth()));

        return new DatePeriod(startDate, endDate);
    }

    @AllArgsConstructor
    private class RequireClosureDateEmploymentService implements GetClosureDateEmploymentDomainService.Require {
        private ShareEmploymentAdapter shareEmploymentAdapter;
        private ClosureRepository closureRepository;
        private ClosureEmploymentRepository closureEmploymentRepository;

        @Override
        public Map<String, BsEmploymentHistoryImport> getEmploymentInfor(List<String> listSid, GeneralDate baseDate) {
            return shareEmploymentAdapter.findEmpHistoryVer2(AppContexts.user().companyId(), listSid, baseDate);
        }

        @Override
        public Optional<Closure> getClosureDataByEmployee(String employeeId, GeneralDate baseDate) {
            Closure closure = ClosureService.getClosureDataByEmployee(
                    ClosureService.createRequireM3(closureRepository, closureEmploymentRepository, shareEmploymentAdapter),
                    new CacheCarrier(), employeeId, baseDate);
            if (closure != null)
                return Optional.of(closure);
            return Optional.empty();
        }
    }

    @AllArgsConstructor
    private class RequireCreateAnnualWorkLedgerContentService implements CreateAnnualWorkLedgerContentQuery.Require {
        private AffComHistAdapter affComHistAdapter;
        private AttendanceItemServiceAdapter itemServiceAdapter;
        private ActualMultipleMonthAdapter actualMultipleMonthAdapter;

        @Override
        public List<StatusOfEmployee> getListAffComHistByListSidAndPeriod(List<String> sid, DatePeriod datePeriod) {
            return affComHistAdapter.getListAffComHist(sid, datePeriod);
        }

        @Override
        public List<AttendanceResultDto> getValueOf(List<String> employeeIds, DatePeriod workingDatePeriod, Collection<Integer> itemIds) {
            return itemServiceAdapter.getValueOf(employeeIds, workingDatePeriod, itemIds);
        }


        @Override
        public Map<String, List<MonthlyRecordValueImport>> getActualMultipleMonth(List<String> employeeIds, YearMonthPeriod period, List<Integer> itemIds) {
            return actualMultipleMonthAdapter.getActualMultipleMonth(employeeIds, period, itemIds);
        }
    }

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
