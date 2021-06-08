package nts.uk.at.app.schedule.filemanagement;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.AllArgsConstructor;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.function.dom.adapter.RegulationInfoEmployeeAdapter;
import nts.uk.ctx.at.schedule.app.query.schedule.shift.management.shifttable.GetHolidaysByPeriod;
import nts.uk.ctx.at.schedule.dom.displaysetting.authcontrol.ScheAuthModifyDeadline;
import nts.uk.ctx.at.schedule.dom.displaysetting.authcontrol.ScheAuthModifyDeadlineRepository;
import nts.uk.ctx.at.schedule.dom.importschedule.ImportResult;
import nts.uk.ctx.at.schedule.dom.importschedule.ImportResultDetail;
import nts.uk.ctx.at.schedule.dom.importschedule.ImportStatus;
import nts.uk.ctx.at.schedule.dom.importschedule.WorkScheduleImportService;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkSchedule;
import nts.uk.ctx.at.schedule.dom.schedule.workschedule.WorkScheduleRepository;
import nts.uk.ctx.at.schedule.dom.shift.businesscalendar.holiday.PublicHoliday;
import nts.uk.ctx.at.shared.dom.adapter.employee.EmpEmployeeAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employee.PersonEmpBasicInfoImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.employwork.leaveinfo.EmpLeaveHistoryAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.employwork.leaveinfo.EmpLeaveWorkHistoryAdapter;
import nts.uk.ctx.at.shared.dom.adapter.employment.employwork.leaveinfo.EmpLeaveWorkPeriodImport;
import nts.uk.ctx.at.shared.dom.adapter.employment.employwork.leaveinfo.EmployeeLeaveJobPeriodImport;
import nts.uk.ctx.at.shared.dom.common.EmployeeId;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.BasicScheduleService;
import nts.uk.ctx.at.shared.dom.schedule.basicschedule.SetupType;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingCondition;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionItem;
import nts.uk.ctx.at.shared.dom.workingcondition.WorkingConditionRepository;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.employeeinfor.employmenthistory.imported.EmpComHisAdapter;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.employeeinfor.employmenthistory.imported.EmpEnrollPeriodImport;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.employeeinfor.employmenthistory.imported.EmploymentHisScheduleAdapter;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.employeeinfor.employmenthistory.imported.EmploymentPeriodImported;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.EmployeeSearchCallSystemType;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.RegulationInfoEmpQuery;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.adapter.WorkplaceGroupAdapter;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMaster;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterImportCode;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.ShiftMasterRepository;
import nts.uk.ctx.at.shared.dom.worktime.common.WorkTimeCode;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.fixedset.FixedWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flexset.FlexWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSetting;
import nts.uk.ctx.at.shared.dom.worktime.flowset.FlowWorkSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.predset.PredetemineTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSetting;
import nts.uk.ctx.at.shared.dom.worktime.worktimeset.WorkTimeSettingRepository;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;
import nts.uk.ctx.bs.employee.pub.employee.employeeInfo.setting.code.EmployeeCodeEditSettingExport;
import nts.uk.ctx.bs.employee.pub.employee.employeeInfo.setting.code.IEmployeeCESettingPub;
import nts.uk.query.pub.employee.EmployeeSearchQueryDto;
import nts.uk.query.pub.employee.RegulationInfoEmployeeExport;
import nts.uk.query.pub.employee.RegulationInfoEmployeePub;
import nts.uk.shr.com.context.AppContexts;


/**
 * @author anhnm
 *
 */
@Stateless
public class WorkPlaceCheckFileService {
    
    @Inject
    private IEmployeeCESettingPub employeeSettingPub;
    
    @Inject
    private CheckFileService checkfileService;
    
    @Inject
    private ScheAuthModifyDeadlineRepository scheAuthModifyDeadlineRepository;
    
    @Inject
    private WorkplaceGroupAdapter workplaceGroupAdapter;
    
    @Inject
    private RegulationInfoEmployeeAdapter regulInfoEmpAdap;
    
    @Inject
    private RegulationInfoEmployeePub regulInfoEmpPub;
    
    @Inject
    private WorkingConditionRepository workingConditionRepo;
    
    @Inject
    private WorkTypeRepository workTypeRepo;
    
    @Inject
    private WorkTimeSettingRepository workTimeSettingRepository;
    
    @Inject
    private BasicScheduleService basicScheduleService;
    
    @Inject
    private FixedWorkSettingRepository fixedWorkSettingRepository;
    
    @Inject
    private FlowWorkSettingRepository flowWorkSettingRepository;
    
    @Inject
    private FlexWorkSettingRepository flexWorkSettingRepository;
    
    @Inject
    private PredetemineTimeSettingRepository predetemineTimeSettingRepository;
    
    @Inject
    private ShiftMasterRepository shiftMasterRepository;
    
    @Inject
    private WorkScheduleRepository workScheduleRepository;
    
    @Inject
    private EmpEmployeeAdapter empEmployeeAdapter;
    
    @Inject
    private EmpComHisAdapter comHisAdapter;
    
    @Inject
    private EmpLeaveHistoryAdapter empHisAdapter;
    
    @Inject
    private EmpLeaveWorkHistoryAdapter leaHisAdapter;
    
    @Inject
    private EmploymentHisScheduleAdapter scheAdapter;
    
    @Inject
    private GetHolidaysByPeriod getHolidaysByPeriod;

    public CapturedRawDataDto processingFile(WorkPlaceScheCheckFileParam param) throws Exception {
        try {
            Optional<EmployeeCodeEditSettingExport> employeeCESettingOpt = employeeSettingPub.getByComId(AppContexts.user().companyId());
            EmployeeCodeEditSettingExport employeeCodeEditSettingExport = null;
            if (employeeCESettingOpt.isPresent()) {
                employeeCodeEditSettingExport = employeeCESettingOpt.get();
            }
            return CapturedRawDataDto.fromDomain(checkfileService.processingFile(param, employeeCodeEditSettingExport));
            
        } catch (Exception e) {
            throw e;
        }
    }
    
    public CaptureDataOutput getCaptureData(CapturedRawDataDto data, boolean overwrite) {
        // 1: 取り込む(Require, 取り込み内容)
        ImportResult importResult = WorkScheduleImportService.importFrom(
                new RequireImp(scheAuthModifyDeadlineRepository, workplaceGroupAdapter, regulInfoEmpAdap, regulInfoEmpPub, workingConditionRepo, workTypeRepo, workTimeSettingRepository, basicScheduleService, fixedWorkSettingRepository, flowWorkSettingRepository, flexWorkSettingRepository, predetemineTimeSettingRepository, shiftMasterRepository, workScheduleRepository, empEmployeeAdapter, comHisAdapter, empHisAdapter, leaHisAdapter, scheAdapter), 
                data.toDomain());
        
        // 2: 取り込み可能な社員IDリストを取得する()
        List<EmployeeId> employeeIds = importResult.getImportableEmployees();
        
        // 3: 取り込み可能な年月日リストを取得する()
        List<GeneralDate> importableDates = importResult.getImportableDates();
        
        // 4: 処理2の結果
        List<PersonEmpBasicInfoImport> listPersonEmp = this.empEmployeeAdapter.getPerEmpBasicInfo(employeeIds.stream().map(x -> x.v()).collect(Collectors.toList()));
        
        // 5: create 取り込みエラーDto
        List<MappingErrorDto> mappingErrorList = new ArrayList<MappingErrorDto>();
        // <<create>>
        //            取り込み結果.取り込み不可日 : 
        //　　map 取り込みエラーDto (empty, empty, $, #Msg_2121)
        importResult.getUnimportableDates().forEach(x -> {
            mappingErrorList.add(new MappingErrorDto(Optional.empty(), Optional.empty(), Optional.of(x), "Msg_2121"));
        });
        // <<create>>
        //            取り込み結果.存在しない社員 :
        //　　map 取り込みエラーDto ($, empty, empty, #Msg_2175)
        importResult.getUnexistsEmployees().forEach(x -> {
            mappingErrorList.add(new MappingErrorDto(Optional.of(x), Optional.empty(), Optional.empty(), "Msg_2175"));
        });
        // <<create>>
        //            取り込み結果.1件分の取り込み結果 :
        //　　find $.状態.取り込み不可か()
        //　　map { $対象社員 = $社員 in 処理4の結果 : find $社員.社員ID == $.社員ID;
        //　　　　　return 取り込みエラーDto ($対象社員.社員コード, $対象社員.ビジネスネーム, $.年月日, $.状態.エラーメッセージ) }
        List<ImportResultDetail> resultDetails1 = importResult.getResults().stream().filter(x -> x.getStatus().isUnimportable()).collect(Collectors.toList());
        
        resultDetails1.forEach(x -> {
            Optional<PersonEmpBasicInfoImport> personEmpOptional = listPersonEmp.stream()
                    .filter(y -> y.getEmployeeId().equals(x.getEmployeeId().v())).findFirst();
            if (personEmpOptional.isPresent()) {
                mappingErrorList.add(new MappingErrorDto(
                        Optional.of(personEmpOptional.get().getEmployeeId()), 
                        Optional.of(personEmpOptional.get().getBusinessName()), 
                        Optional.of(x.getYmd()), 
                        x.getStatus().getMessageId().isPresent() ? x.getStatus().getMessageId().get().replace("#", "") : ""));
            }
        });
        // <<create>>
        //            取り込み結果.1件分の取り込み結果 :
        //　　find $.状態 == すでに勤務予定が存在する && Input.上書きするか == false
        //　　map { $対象社員 = $社員 in 処理4の結果 : find $社員.社員ID == $.社員ID;
        //　　　　　return 取り込みエラーDto ($対象社員.社員コード, $対象社員.ビジネスネーム, $.年月日, $.状態.エラーメッセージ) }
        List<ImportResultDetail> resultDetails2 = importResult.getResults().stream().filter(x -> {
            return x.getStatus().equals(ImportStatus.SCHEDULE_IS_EXISTS) && overwrite;
        }).collect(Collectors.toList());
        resultDetails2.forEach(x -> {
            Optional<PersonEmpBasicInfoImport> personEmpOptional = listPersonEmp.stream()
                    .filter(y -> y.getEmployeeId().equals(x.getEmployeeId().v())).findFirst();
            if (personEmpOptional.isPresent()) {
                mappingErrorList.add(new MappingErrorDto(
                        Optional.of(personEmpOptional.get().getEmployeeId()), 
                        Optional.of(personEmpOptional.get().getBusinessName()), 
                        Optional.of(x.getYmd()), 
                        x.getStatus().getMessageId().isPresent() ? x.getStatus().getMessageId().get().replace("#", "") : ""));
            }
        });
        
        // 6: 曜日()
        // TODO: Use List<GeneralDate> importableDates step 3
        
        //7: 取得する(期間)
        List<PublicHoliday> holidays = getHolidaysByPeriod.get(new DatePeriod(importableDates.get(0), importableDates.get(importableDates.size() - 1)));
        
        return new CaptureDataOutput(listPersonEmp, importableDates, holidays, importResult, mappingErrorList);
    }
    
    @AllArgsConstructor
    private static class RequireImp implements WorkScheduleImportService.Require {
        
        @Inject
        private ScheAuthModifyDeadlineRepository scheAuthModifyDeadlineRepository;
        
        @Inject
        private WorkplaceGroupAdapter workplaceGroupAdapter;
        
        @Inject
        private RegulationInfoEmployeeAdapter regulInfoEmpAdap;
        
        @Inject
        private RegulationInfoEmployeePub regulInfoEmpPub;
        
        @Inject
        private WorkingConditionRepository workingConditionRepo;
        
        @Inject
        private WorkTypeRepository workTypeRepo;
        
        @Inject
        private WorkTimeSettingRepository workTimeSettingRepository;
        
        @Inject
        private BasicScheduleService basicScheduleService;
        
        @Inject
        private FixedWorkSettingRepository fixedWorkSettingRepository;
        
        @Inject
        private FlowWorkSettingRepository flowWorkSettingRepository;
        
        @Inject
        private FlexWorkSettingRepository flexWorkSettingRepository;
        
        @Inject
        private PredetemineTimeSettingRepository predetemineTimeSettingRepository;
        
        @Inject
        private ShiftMasterRepository shiftMasterRepository;
        
        @Inject
        private WorkScheduleRepository workScheduleRepository;
        
        @Inject
        private EmpEmployeeAdapter empEmployeeAdapter;
        
        @Inject
        private EmpComHisAdapter comHisAdapter;
        
        @Inject
        private EmpLeaveHistoryAdapter empHisAdapter;
        
        @Inject
        private EmpLeaveWorkHistoryAdapter leaHisAdapter;
        
        @Inject
        private EmploymentHisScheduleAdapter scheAdapter;

        @Override
        public Optional<ScheAuthModifyDeadline> getScheAuthModifyDeadline(String roleID) {
            return scheAuthModifyDeadlineRepository.get(AppContexts.user().companyId(), roleID);
        }

        @Override
        public List<String> getEmpCanReferByWorkplaceGroup(GeneralDate date, String empId, String workplaceGroupID) {
            return workplaceGroupAdapter.getReferableEmp(date, empId, workplaceGroupID);
        }

        @Override
        public List<String> getAllEmpCanReferByWorkplaceGroup(GeneralDate date, String empId) {
            return workplaceGroupAdapter.getAllReferableEmp(date, empId);
        }

        @Override
        public List<String> sortEmployee(List<String> employeeIdList, EmployeeSearchCallSystemType systemType,
                Integer sortOrderNo, GeneralDate date, Integer nameType) {
            return regulInfoEmpAdap.sortEmployee(
                    AppContexts.user().companyId(), 
                    employeeIdList, 
                    systemType.value, 
                    sortOrderNo, 
                    nameType, 
                    GeneralDateTime.fromString(date.toString() + " " + "00:00", "yyyy/MM/dd HH:mm"));
        }

        @Override
        public String getRoleID() {
            return AppContexts.user().roles().forAttendance();
        }

        @Override
        public List<String> searchEmployee(RegulationInfoEmpQuery regulationInfoEmpQuery, String roleId) {
            EmployeeSearchQueryDto query = EmployeeSearchQueryDto.builder()
                    .baseDate(GeneralDateTime.fromString(regulationInfoEmpQuery.getBaseDate().toString() + " " + "00:00", "yyyy/MM/dd HH:mm"))
                    .referenceRange(regulationInfoEmpQuery.getReferenceRange().value)
                    .systemType(regulationInfoEmpQuery.getSystemType().value)
                    .filterByWorkplace(regulationInfoEmpQuery.getFilterByWorkplace())
                    .workplaceCodes(regulationInfoEmpQuery.getWorkplaceIds())
                    .filterByEmployment(false)
                    .employmentCodes(new ArrayList<String>())
                    .filterByDepartment(false)
                    .departmentCodes(new ArrayList<String>())
                    .filterByClassification(false)
                    .classificationCodes(new ArrayList<String>())
                    .filterByJobTitle(false)
                    .jobTitleCodes(new ArrayList<String>())
                    .filterByWorktype(false)
                    .worktypeCodes(new ArrayList<String>())
                    .filterByClosure(false)
                    .closureIds(new ArrayList<Integer>())
                    .periodStart(GeneralDateTime.now())
                    .periodEnd(GeneralDateTime.now())
                    .includeIncumbents(true)
                    .includeWorkersOnLeave(true)
                    .includeOccupancy(true)
                    .includeRetirees(false)
                    .includeAreOnLoan(false)
                    .includeGoingOnLoan(false)
                    .retireStart(GeneralDateTime.now())
                    .retireEnd(GeneralDateTime.now())
                    .sortOrderNo(null)
                    .nameType(null)

                    .build();
            List<RegulationInfoEmployeeExport> data = regulInfoEmpPub.find(query);
            List<String> resultList = data.stream().map(item -> item.getEmployeeId())
                    .collect(Collectors.toList());
            return resultList;
        }

        @Override
        public Optional<EmpEnrollPeriodImport> getAffCompanyHistByEmployee(String employeeId, GeneralDate date) {
            val result = comHisAdapter.getEnrollmentPeriod(Arrays.asList(employeeId), new DatePeriod(date, date));
            if (result.isEmpty())
                return Optional.empty();
            return Optional.of(result.get(0));
        }

        @Override
        public Optional<WorkingConditionItem> getBySidAndStandardDate(String employeeId, GeneralDate date) {
            Optional<WorkingCondition> workingCondition = workingConditionRepo.getBySidAndStandardDate(
                    AppContexts.user().companyId(),
                    employeeId,
                    date);
            
            return workingConditionRepo.getWorkingConditionItem(workingCondition.get().getDateHistoryItem().get(0).identifier());
        }

        @Override
        public Optional<EmployeeLeaveJobPeriodImport> getByDatePeriod(String employeeId, GeneralDate date) {
            val result = empHisAdapter.getLeaveBySpecifyingPeriod(Arrays.asList(employeeId),
                    new DatePeriod(date, date));
            if (result.isEmpty())
                return Optional.empty();
            return Optional.of(result.get(0));
        }

        @Override
        public Optional<EmpLeaveWorkPeriodImport> specAndGetHolidayPeriod(String employeeId, GeneralDate date) {
            val result = leaHisAdapter.getHolidayPeriod(Arrays.asList(employeeId), new DatePeriod(date, date));
            if (result.isEmpty())
                return Optional.empty();
            return Optional.of(result.get(0));
        }

        @Override
        public Optional<EmploymentPeriodImported> getEmploymentHistory(String employeeId, GeneralDate date) {
            val result = scheAdapter.getEmploymentPeriod(Arrays.asList(employeeId), new DatePeriod(date, date));
            if (result.isEmpty())
                return Optional.empty();
            return Optional.of(result.get(0));
        }

        @Override
        public Optional<WorkType> getWorkType(String workTypeCd) {
            return workTypeRepo.findByPK(AppContexts.user().companyId(), workTypeCd);
        }

        @Override
        public Optional<WorkTimeSetting> getWorkTime(String workTimeCode) {
            return workTimeSettingRepository.findByCode(AppContexts.user().companyId(), workTimeCode);
        }

        @Override
        public SetupType checkNeededOfWorkTimeSetting(String workTypeCode) {
            return basicScheduleService.checkNeededOfWorkTimeSetting(workTypeCode);
        }

        @Override
        public FixedWorkSetting getWorkSettingForFixedWork(WorkTimeCode code) {
            return fixedWorkSettingRepository.findByKey(AppContexts.user().companyId(), code.v()).get();
        }

        @Override
        public FlowWorkSetting getWorkSettingForFlowWork(WorkTimeCode code) {
            return flowWorkSettingRepository.find(AppContexts.user().companyId(), code.v()).get();
        }

        @Override
        public FlexWorkSetting getWorkSettingForFlexWork(WorkTimeCode code) {
            return flexWorkSettingRepository.find(AppContexts.user().companyId(), code.v()).get();
        }

        @Override
        public PredetemineTimeSetting getPredetermineTimeSetting(WorkTimeCode wktmCd) {
            return predetemineTimeSettingRepository.findByWorkTimeCode(AppContexts.user().companyId(), wktmCd.v()).get();
        }

        @Override
        public String getOwnAttendanceRoleId() {
            return AppContexts.user().roles().forAttendance();
        }

        @Override
        public EmployeeId getOwnEmployeeId() {
            return new EmployeeId(AppContexts.user().employeeId());
        }

        @Override
        public Map<String, String> getEmployeeIds(List<String> employeeCodes) {
            return empEmployeeAdapter.getEmployeeIDListByCode(AppContexts.user().companyId(), employeeCodes);
        }

        @Override
        public List<ShiftMaster> getShiftMasters(List<ShiftMasterImportCode> importCodes) {
            return shiftMasterRepository.getByListShiftMaterCd2(AppContexts.user().companyId(), 
                    importCodes.stream().map(x -> x.v()).collect(Collectors.toList()));
        }

        @Override
        public Optional<WorkSchedule> getWorkSchedule(EmployeeId employeeId, GeneralDate ymd) {
            return workScheduleRepository.get(employeeId.v(), ymd);
        }
        
    }
}
