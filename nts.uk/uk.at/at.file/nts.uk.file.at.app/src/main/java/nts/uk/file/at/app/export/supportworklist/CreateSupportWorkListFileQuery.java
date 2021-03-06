package nts.uk.file.at.app.export.supportworklist;

import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.function.dom.adapter.DailyAttendanceItemAdapter;
import nts.uk.ctx.at.function.dom.adapter.supportworkdata.SupportWorkDataAdapter;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkPlaceInforExport;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.function.dom.supportworklist.aggregationsetting.SupportAggregationUnit;
import nts.uk.ctx.at.function.dom.supportworklist.aggregationsetting.SupportWorkAggregationSettingRepository;
import nts.uk.ctx.at.function.dom.supportworklist.aggregationsetting.SupportWorkDetails;
import nts.uk.ctx.at.function.dom.supportworklist.outputsetting.SupportWorkListOutputSettingRepository;
import nts.uk.ctx.at.function.dom.supportworklist.outputsetting.SupportWorkOutputCode;
import nts.uk.ctx.at.function.dom.supportworklist.outputsetting.SupportWorkOutputDataRequireImpl;
import nts.uk.ctx.at.function.dom.supportworklist.outputsetting.outputdata.SupportWorkOutputData;
import nts.uk.ctx.at.record.dom.stampmanagement.workplace.WorkLocation;
import nts.uk.ctx.at.record.dom.stampmanagement.workplace.WorkLocationRepository;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmployeeBasicInfoImport;
import nts.uk.ctx.at.shared.dom.scherec.dailyattdcal.dailyattendance.converter.util.item.ItemValue;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.adapter.attendanceitemname.AttItemName;
import nts.uk.ctx.at.shared.dom.scherec.dailyattendanceitem.service.CompanyDailyItemService;
import nts.uk.ctx.at.shared.dom.scherec.taskmanagement.repo.taskmaster.TaskingRepository;
import nts.uk.ctx.bs.company.dom.company.CompanyRepository;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ????????????????????????????????????
 * UKDesign.UniversalK.??????.KHA_????????????.KHA002_?????????????????????.???????????????????????????.???????????????OCD.A:????????????????????????????????????.A:????????????????????????????????????
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CreateSupportWorkListFileQuery {
    @Inject
    private SupportWorkListOutputSettingRepository supportWorkListOutSetRepo;

    @Inject
    private SupportWorkAggregationSettingRepository supportWorkAggregationSettingRepo;

    @Inject
    private DailyAttendanceItemAdapter dailyAttendanceItemAdapter;

    @Inject
    private WorkplaceAdapter workplaceAdapter;

    @Inject
    private SupportWorkDataAdapter supportWorkDataAdapter;

    @Inject
    private EmpEmployeeAdapter empEmployeeAdapter;

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private TaskingRepository taskRepository;

    @Inject
    private CompanyDailyItemService companyDailyItemService;

    @Inject
    private WorkLocationRepository workLocationRepository;

    public SupportWorkListDataSource create(SupportWorkListQuery query) {
        String cid = AppContexts.user().companyId();
        String contractCd = AppContexts.user().contractCode();
        val require = new SupportWorkOutputDataRequireImpl(supportWorkAggregationSettingRepo, dailyAttendanceItemAdapter,
                workplaceAdapter, supportWorkDataAdapter);

        /**get(??????????????????ID???????????????????????????)*/
        val supportWorkOutSettingOpt = supportWorkListOutSetRepo.get(cid, new SupportWorkOutputCode(query.getSupportWorkCode()));

        SupportWorkOutputData supportWorkOutputData = null;
        if (supportWorkOutSettingOpt.isPresent()) {
            /** ??????????????????????????????????????????????????????(@Require, ??????ID, ??????, ?????????, ??????ID) */
            if (query.getAggregationUnit() == SupportAggregationUnit.WORKPLACE.value)
                supportWorkOutputData = supportWorkOutSettingOpt.get().getOutputDataByWorkplace(require, cid, query.getPeriod(), query.baseDateOpt().get(), query.getWorkplaceIds());

            /** ??????????????????????????????????????????????????????(@Require, ??????ID, ??????, ?????????????????????)*/
            if (query.getAggregationUnit() == SupportAggregationUnit.WORK_LOCATION.value)
                supportWorkOutputData = supportWorkOutSettingOpt.get().getOutputDataByWorkLocation(require, cid, query.getPeriod(), query.getWorkLocationCodes());
        }

        /**??????????????????????????????0???*/
        if (supportWorkOutputData == null || supportWorkOutputData.getSupportWorkDataList().isEmpty())
            throw new BusinessException("Msg_2155");

        /**???????????????????????????*/
        val attendanceItems = this.getAttendanceItemName(supportWorkOutputData);

        /**??????/???????????????????????????*/
        val workplaceWorkLocation = this.getWkpWorkLocationInfo(query.getAggregationUnit(), supportWorkOutputData, query.baseDateOpt());

        /**[RQ600]??????ID???List????????????????????????????????????????????????????????????????????? */
        val supportWorkDetails = supportWorkOutputData.getSupportWorkDataList().stream().flatMap(x -> x.getSupportWorkDetails().stream())
                .collect(Collectors.toList());
        val supportDetails = supportWorkDetails.stream().flatMap(x -> x.getSupportWorkDetailsList().stream()).collect(Collectors.toList());
        val empIds = supportDetails.stream().map(SupportWorkDetails::getEmployeeId).distinct().collect(Collectors.toList());
        List<EmployeeBasicInfoImport> employeeInfoList = empEmployeeAdapter.getEmpInfoLstBySids(empIds, query.getPeriod(), true, true);

        /** ???????????? */
        val companyInfoOpt = companyRepository.findAllByListCid(contractCd, Collections.singletonList(cid)).stream().findFirst();

        /** ??????????????????????????? */
        val supportWorkDataList = supportWorkOutputData.getSupportWorkDataList().stream().flatMap(x -> x.getSupportWorkDetails().stream()).collect(Collectors.toList());
        val supportDetailList = supportWorkDataList.stream().flatMap(x -> x.getSupportWorkDetailsList().stream()).collect(Collectors.toList());
        List<ItemValue> itemValueList = supportDetailList.stream().flatMap(x -> x.getItemList().stream()).collect(Collectors.toList());

        val workList1 = taskRepository.getListTask(cid, 1, this.getWorkCodes(1, itemValueList));
        val workList2 = taskRepository.getListTask(cid, 2, this.getWorkCodes(2, itemValueList));
        val workList3 = taskRepository.getListTask(cid, 3, this.getWorkCodes(3, itemValueList));
        val workList4 = taskRepository.getListTask(cid, 4, this.getWorkCodes(4, itemValueList));
        val workList5 = taskRepository.getListTask(cid, 5, this.getWorkCodes(5, itemValueList));

        return new SupportWorkListDataSource(
                query.getAggregationUnit(),
                query.getSupportWorkCode(),
                query.getPeriod(),
                query.getHeaderInfos(),
                supportWorkOutSettingOpt.get(),
                supportWorkOutputData,
                attendanceItems,
                workplaceWorkLocation.getWorkplaceInfoList(),
                workplaceWorkLocation.getWorkLocations(),
                employeeInfoList,
                companyInfoOpt,
                workList1,
                workList2,
                workList3,
                workList4,
                workList5
        );
    }

    public List<AttItemName> getAttendanceItemName(SupportWorkOutputData supportWorkOutputData) {
        String cid = AppContexts.user().companyId();
        val supportWorkDataList = supportWorkOutputData.getSupportWorkDataList().stream().flatMap(x -> x.getSupportWorkDetails().stream()).collect(Collectors.toList());
        val supportDetailList = supportWorkDataList.stream().flatMap(x -> x.getSupportWorkDetailsList().stream()).collect(Collectors.toList());
        val itemValueList = supportDetailList.stream().flatMap(x -> x.getItemList().stream()).collect(Collectors.toList());
        val attendanceItemIds = itemValueList.stream().map(ItemValue::getItemId).distinct().collect(Collectors.toList());

        return companyDailyItemService.getDailyItems(cid, Optional.empty(),
                attendanceItemIds, null);
    }

    public WorkplaceWorkLocationData getWkpWorkLocationInfo(int aggregationUnit, SupportWorkOutputData supportWorkOutputData, Optional<GeneralDate> baseDate) {
        val loginInfo = AppContexts.user();

        /**1. ??????/??????????????????????????????*/
        val supportWorkDetails = supportWorkOutputData.getSupportWorkDataList().stream().flatMap(x -> x.getSupportWorkDetails().stream())
                .collect(Collectors.toList());
        val supportDetails = supportWorkDetails.stream().flatMap(x -> x.getSupportWorkDetailsList().stream()).collect(Collectors.toList());

        val affInfoList = supportDetails.stream().map(SupportWorkDetails::getAffiliationInfo).collect(Collectors.toList());
        val workInfoList = supportDetails.stream().map(SupportWorkDetails::getWorkInfo).collect(Collectors.toList());
        val workplaceLocationCodes = Stream.of(affInfoList, workInfoList)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        /** ????????????????????? */
        List<WorkPlaceInforExport> workplaceInfoList = new ArrayList<>();
        if (aggregationUnit == SupportAggregationUnit.WORKPLACE.value)
            workplaceInfoList = workplaceAdapter.getWorkplaceInforByWkpIds(loginInfo.companyId(), workplaceLocationCodes, baseDate.get());

        /** ??????????????????????????? */
        List<WorkLocation> workLocations = new ArrayList<>();
        if (aggregationUnit == SupportAggregationUnit.WORK_LOCATION.value)
            workLocations = workLocationRepository.findByCodes(loginInfo.contractCode(), workplaceLocationCodes);

        return new WorkplaceWorkLocationData(workplaceInfoList, workLocations);
    }

    private List<String> getWorkCodes(int taskFrameNo, List<ItemValue> itemValueList) {
        switch (taskFrameNo) {
            case 1:
                return itemValueList.stream().filter(c -> c.getItemId() == 924).map(ItemValue::getValue).distinct().collect(Collectors.toList());
            case 2:
                return itemValueList.stream().filter(c -> c.getItemId() == 925).map(ItemValue::getValue).distinct().collect(Collectors.toList());
            case 3:
                return itemValueList.stream().filter(c -> c.getItemId() == 926).map(ItemValue::getValue).distinct().collect(Collectors.toList());
            case 4:
                return itemValueList.stream().filter(c -> c.getItemId() == 927).map(ItemValue::getValue).distinct().collect(Collectors.toList());
            case 5:
                return itemValueList.stream().filter(c -> c.getItemId() == 928).map(ItemValue::getValue).distinct().collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }

    }

}
