package nts.uk.screen.at.app.query.kdl.kdl016;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.arc.time.calendar.period.DatePeriod;
import nts.uk.ctx.at.function.dom.adapter.RegulationInfoEmployeeAdapter;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.EmployeeInformationAdapter;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.EmployeeInformationImport;
import nts.uk.ctx.at.function.dom.adapter.annualworkschedule.EmployeeInformationQueryDtoImport;
import nts.uk.ctx.at.function.dom.adapter.workplace.EmployeeInfoImported;
import nts.uk.ctx.at.function.dom.adapter.workplace.WorkplaceAdapter;
import nts.uk.ctx.at.record.app.find.dailyperform.dto.TimeSpanForCalcDto;
import nts.uk.ctx.at.schedule.dom.schedule.support.SupportFunctionControl;
import nts.uk.ctx.at.schedule.dom.schedule.support.SupportFunctionControlRepository;
import nts.uk.ctx.at.shared.app.find.supportmanagement.supportalloworg.SupportAllowOrganizationFinder;
import nts.uk.ctx.at.shared.dom.common.EmployeeId;
import nts.uk.ctx.at.shared.dom.supportmanagement.supportableemployee.SupportableEmployee;
import nts.uk.ctx.at.shared.dom.supportmanagement.supportableemployee.SupportableEmployeeRepository;
import nts.uk.ctx.at.shared.dom.supportmanagement.supportalloworg.SupportAllowOrganization;
import nts.uk.ctx.at.shared.dom.supportmanagement.supportoperationsetting.SupportOperationSetting;
import nts.uk.ctx.at.shared.dom.supportmanagement.supportoperationsetting.SupportOperationSettingRepository;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.*;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.adapter.EmpAffiliationInforAdapter;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.adapter.EmpOrganizationImport;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.adapter.WorkplaceGroupAdapter;
import nts.uk.ctx.at.shared.dom.workrule.organizationmanagement.workplace.adapter.WorkplaceGroupImport;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.AffWorkplaceAdapter;
import nts.uk.ctx.at.shared.dom.workrule.shiftmaster.WorkplaceExportServiceAdapter;
import nts.uk.ctx.bs.employee.dom.workplace.group.AffWorkplaceGroupRespository;
import nts.uk.ctx.bs.employee.dom.workplace.group.domainservice.EmployeeInfoData;
import nts.uk.ctx.bs.employee.dom.workplace.group.domainservice.GetAllEmpWhoBelongWorkplaceGroupService;
import nts.uk.ctx.bs.employee.pub.workplace.ResultRequest597Export;
import nts.uk.ctx.bs.employee.pub.workplace.master.WorkplacePub;
import nts.uk.query.pub.employee.EmployeeSearchQueryDto;
import nts.uk.query.pub.employee.RegulationInfoEmployeeExport;
import nts.uk.query.pub.employee.RegulationInfoEmployeePub;
import nts.uk.screen.at.app.query.kdl.kdl016.dto.*;
import nts.uk.shr.com.context.AppContexts;
import org.apache.logging.log4j.util.Strings;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class SupportInformationFinder {
    @Inject
    private SupportOperationSettingRepository supportOperationSettingRepo;

    @Inject
    private SupportFunctionControlRepository supportFuncCtrlRepo;

    @Inject
    private SupportableEmployeeRepository supportableEmployeeRepo;

    @Inject
    private WorkplaceGroupAdapter groupAdapter;

    @Inject
    private WorkplaceExportServiceAdapter serviceAdapter;

    @Inject
    private AffWorkplaceAdapter wplAdapter;

    @Inject
    private EmployeeInformationAdapter employeeInfoAdapter;

    @Inject
    private EmpAffiliationInforAdapter empAffiliationInforAdapter;

    @Inject
    private WorkplaceGroupAdapter workplaceGroupAdapter;

    @Inject
    private RegulationInfoEmployeeAdapter regulInfoEmployeeAdap;

    @Inject
    private RegulationInfoEmployeePub regulInfoEmpPub;

    @Inject
    private SupportAllowOrganizationFinder supportAllowOrganizationFinder;

    @Inject
    private WorkplaceAdapter workplaceAdapter;

    @Inject
    private AffWorkplaceGroupRespository affWorkplaceGroupRepo;

    @Inject
    private WorkplacePub workplacePub;

    final static String SPACE = " ";
    final static String ZEZO_TIME = "00:00";
    final static String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm";

    /**
     * ????????????????????????????????????
     * UKDesign.UniversalK.??????.KDL_???????????????.KDL016_???????????????????????????.A:??????????????????.???????????????OCD.????????????
     *
     * @param employeeIds
     * @param period
     * @return List<????????????DTO
                    */
    public InitialDataScreenA getDataInitScreenA(List<String> employeeIds, DatePeriod period) {
        String cid = AppContexts.user().companyId();
        // ?????????????????????
        SupportOperationSetting supportOperationSetting = supportOperationSettingRepo.get(cid);

        // ???????????????????????????
        SupportFunctionControl supportFuncCtrl = null;
        // ?????????????????????.???????????????==TRUE
        if (supportOperationSetting.isUsed()) {
            supportFuncCtrl = supportFuncCtrlRepo.get(cid);
        }

        //?????????????????????.??????????????? or ???????????????????????????.??????????????????????????????==FALSE
        if (!supportOperationSetting.isUsed() || (supportFuncCtrl != null && !supportFuncCtrl.isUse())) {
            throw new BusinessException("Msg_3240");
        }

        // ?????????????????????.??????????????? and ???????????????????????????.??????????????????????????????==TRUE
        List<SupportInfoDto> supportInfos = new ArrayList<>();
        if (supportOperationSetting.isUsed() && supportFuncCtrl != null && supportFuncCtrl.isUse()) {
            supportInfos = this.getInfoGoToSupport(employeeIds, period);
        }

        return new InitialDataScreenA(supportInfos, supportFuncCtrl != null && supportFuncCtrl.isUseSupportInTimezone());
    }

    /**
     * ?????????????????????????????????
     * UKDesign.UniversalK.??????.KDL_???????????????.KDL016_???????????????????????????.A:??????????????????.???????????????OCD.?????????????????????????????????
     */
    public List<SupportInfoDto> getInfoByDisplayMode(SupportInfoInput input) {

        // ??????????????????????????????????????????
        if (input.getDisplayMode() == DisplayMode.GO_TO_SUPPORT.value) { //?????????????????????
            return this.getInfoGoToSupport(input.getEmployeeIds(), input.getPeriod());
        } else { // ??????????????????????????????????????????: DisplayMode.COME_TO_SUPPORT
            return this.getInfoComeToSupport(input.getTargetOrg().getOrgId(), input.getTargetOrg().getOrgUnit(), input.getPeriod());
        }
    }

    /**
     * ????????????????????????????????????
     * UKDesign.UniversalK.??????.KDL_???????????????.KDL016_???????????????????????????.A:??????????????????.???????????????OCD.????????????????????????????????????.????????????????????????????????????
     *
     * @param employeeIds
     * @param period
     * @return
     */
    public List<SupportInfoDto> getInfoGoToSupport(List<String> employeeIds, DatePeriod period) {
        List<EmployeeId> sIds = employeeIds.stream().map(EmployeeId::new).collect(Collectors.toList());

        //1. List<?????????????????????> : ??????????????????????????????????????????()
        List<SupportableEmployee> supportableEmployees = supportableEmployeeRepo.findByEmployeeIdWithPeriod(sIds, period);

        //4. ??????????????????????????????
        val supportEmpIds = supportableEmployees.stream().map(x -> x.getEmployeeId().v()).distinct().collect(Collectors.toList());
        List<EmployeeInformationImport> supportEmployeeInfos = this.employeeInfoAdapter
                .getEmployeeInfo(new EmployeeInformationQueryDtoImport(supportEmpIds, period.end(), false, false, false,
                        false, false, false));

        RequireOrgInfoImpl requireOrgInfo = new RequireOrgInfoImpl(groupAdapter, serviceAdapter, wplAdapter);
        List<SupportInfoDto> supportInfoResults = new ArrayList<>();
        for (int i = 0; i < supportableEmployees.size(); i++) {
            SupportableEmployee supportableEmployee = supportableEmployees.get(i);
            // ????????????????????????????????????(Require, ?????????): output ?????????????????????
            DisplayInfoOrganization orgInfo = supportableEmployee.getRecipient().getDisplayInfor(requireOrgInfo, period.end());


            val employeeInfoOpt = supportEmployeeInfos.stream().filter(x -> x.getEmployeeId().equals(supportableEmployee.getEmployeeId().v())).findFirst();
            supportInfoResults.add(new SupportInfoDto(
                    i + 1,
                    supportableEmployee.getId(),
                    supportableEmployee.getPeriod().start().toString("yyyy/MM/dd"),
                    supportableEmployee.getPeriod().end().toString("yyyy/MM/dd"),
                    employeeInfoOpt.isPresent() ? employeeInfoOpt.get().getEmployeeCode() : Strings.EMPTY,
                    employeeInfoOpt.isPresent() ? employeeInfoOpt.get().getBusinessName() : Strings.EMPTY,
                    orgInfo.getName(),
                    supportableEmployee.getRecipient().getUnit() == TargetOrganizationUnit.WORKPLACE
                            ? supportableEmployee.getRecipient().getWorkplaceId().orElse(null)
                            : supportableEmployee.getRecipient().getWorkplaceGroupId().orElse(null),
                    orgInfo.getCode(),
                    supportableEmployee.getRecipient().getUnit().value,
                    supportableEmployee.getSupportType().getValue(),
                    supportableEmployee.getTimespan().isPresent() ? new TimeSpanForCalcDto(supportableEmployee.getTimespan().get().start(), supportableEmployee.getTimespan().get().end()) : new TimeSpanForCalcDto(null, null)
            ));
        }

        return supportInfoResults.stream().sorted(Comparator.comparing(SupportInfoDto::getPeriodStart)
                .thenComparing(Comparator.comparing(SupportInfoDto::getSupportOrgCode))
                .thenComparing(Comparator.comparing(SupportInfoDto::getEmployeeCode))
                .thenComparing(Comparator.comparing(SupportInfoDto::getSupportType))
                .thenComparing(Comparator.comparing(x -> x.getTimeSpan().getStart())))
                .collect(Collectors.toList());
    }

    /**
     * ????????????????????????????????????
     * UKDesign.UniversalK.??????.KDL_???????????????.KDL016_???????????????????????????.A:??????????????????.???????????????OCD.????????????????????????????????????.???????????????????????????
     *
     * @param orgId
     * @param orgUnit
     * @param period
     * @return
     */
    public List<SupportInfoDto> getInfoComeToSupport(String orgId, int orgUnit, DatePeriod period) {
        List<SupportInfoDto> supportInfoResults = new ArrayList<>();

        // 1. ????????????????????????
        TargetOrgIdenInfor targetOrgIdenInfor = orgUnit == TargetOrganizationUnit.WORKPLACE.value
                ? TargetOrgIdenInfor.creatIdentifiWorkplace(orgId)
                : TargetOrgIdenInfor.creatIdentifiWorkplaceGroup(orgId);

        // 2. List<?????????????????????>
        List<SupportableEmployee> supportableEmployees = supportableEmployeeRepo.findByRecipientWithPeriod(targetOrgIdenInfor, period);

        // 3.
        // 4.
        val supportEmpIds = supportableEmployees.stream().map(x -> x.getEmployeeId().v()).collect(Collectors.toList());
        List<EmployeeInformationImport> supportEmployeeInfos = this.employeeInfoAdapter
                .getEmployeeInfo(new EmployeeInformationQueryDtoImport(supportEmpIds, period.end(), false, false, false,
                        false, false, false));

        RequireOrgImpl requireOrg = new RequireOrgImpl(empAffiliationInforAdapter);
        RequireOrgInfoImpl requireOrgInfo = new RequireOrgInfoImpl(groupAdapter, serviceAdapter, wplAdapter);
        for (int i = 0; i < supportableEmployees.size(); i++) {
            SupportableEmployee supportableEmployee = supportableEmployees.get(i);
            // 5.1. ????????????(Require, ?????????, ??????ID)
            TargetOrgIdenInfor orgInfor = GetTargetIdentifiInforService.get(
                    requireOrg,
                    supportableEmployee.getPeriod().end(),
                    supportableEmployee.getEmployeeId().v()
            );
            // 5.2 ????????????????????? : ????????????????????????????????????(Require, ?????????)
            DisplayInfoOrganization orgInfo = orgInfor.getDisplayInfor(requireOrgInfo, supportableEmployee.getPeriod().end());

            val employeeInfoOpt = supportEmployeeInfos.stream().filter(x -> x.getEmployeeId().equals(supportableEmployee.getEmployeeId().v())).findFirst();
            supportInfoResults.add(new SupportInfoDto(
                    i + 1,
                    supportableEmployee.getId(),
                    supportableEmployee.getPeriod().start().toString("yyyy/MM/dd"),
                    supportableEmployee.getPeriod().end().toString("yyyy/MM/dd"),
                    employeeInfoOpt.isPresent() ? employeeInfoOpt.get().getEmployeeCode() : Strings.EMPTY,
                    employeeInfoOpt.isPresent() ? employeeInfoOpt.get().getBusinessName() : Strings.EMPTY,
                    orgInfo.getName(),
                    supportableEmployee.getRecipient().getUnit() == TargetOrganizationUnit.WORKPLACE
                            ? supportableEmployee.getRecipient().getWorkplaceId().orElse(null)
                            : supportableEmployee.getRecipient().getWorkplaceGroupId().orElse(null),
                    orgInfo.getCode(),
                    supportableEmployee.getRecipient().getUnit().value,
                    supportableEmployee.getSupportType().getValue(),
                    supportableEmployee.getTimespan().isPresent() ? new TimeSpanForCalcDto(supportableEmployee.getTimespan().get().start(), supportableEmployee.getTimespan().get().end()) : new TimeSpanForCalcDto(null, null)
            ));

        }

        return supportInfoResults.stream().sorted(Comparator.comparing(SupportInfoDto::getPeriodStart)
                .thenComparing(Comparator.comparing(SupportInfoDto::getSupportOrgCode))
                .thenComparing(Comparator.comparing(SupportInfoDto::getEmployeeCode))
                .thenComparing(Comparator.comparing(SupportInfoDto::getSupportType))
                .thenComparing(Comparator.comparing(x -> x.getTimeSpan().getStart())))
                .collect(Collectors.toList());
    }

    /**
     * ????????????????????????????????????
     * UKDesign.UniversalK.??????.KDL_???????????????.KDL016_???????????????????????????.B:????????????????????????????????????.???????????????OCD.????????????????????????????????????.???????????????????????????
     *
     * @param orgId
     * @param orgUnit
     * @param employeeIds
     * @return
     */
    public Kdl016ScreenBOutput getDataInitScreenB(List<String> employeeIds, String orgId, int orgUnit) {
        GeneralDate baseDate = GeneralDate.today();
        // 1. ??????????????????: ????????????????????????
        // 2. ??????????????????????????????: ????????????????????????
        TargetOrgIdenInfor targetOrgIdenInfor = orgUnit == TargetOrganizationUnit.WORKPLACE.value
                ? TargetOrgIdenInfor.creatIdentifiWorkplace(orgId)
                : TargetOrgIdenInfor.creatIdentifiWorkplaceGroup(orgId);

        // 3. List<????????????????????????>: ????????????(????????????????????????)
        List<SupportAllowOrganization> supportAllowOrgs = supportAllowOrganizationFinder.getListByTargetOrg(AppContexts.user().companyId(), targetOrgIdenInfor);

        // 4. List<????????????????????????> empty
        if (supportAllowOrgs.isEmpty()) {
            throw new BusinessException("Msg_3279");
        }

        RequireOrgInfoImpl requireOrgInfo = new RequireOrgInfoImpl(groupAdapter, serviceAdapter, wplAdapter);
        List<OrganizationDisplayInfoDto> orgDisplayInfoList = new ArrayList<>();
        for (SupportAllowOrganization supportAllowOrg : supportAllowOrgs) {
            DisplayInfoOrganization displayInfoOrg = supportAllowOrg.getSupportableOrganization().getDisplayInfor(requireOrgInfo, baseDate);
            orgDisplayInfoList.add(new OrganizationDisplayInfoDto(
                    supportAllowOrg.getSupportableOrganization().getUnit() == TargetOrganizationUnit.WORKPLACE
                            ? supportAllowOrg.getSupportableOrganization().getWorkplaceId().orElse(null)
                            : supportAllowOrg.getSupportableOrganization().getWorkplaceGroupId().orElse(null),
                    supportAllowOrg.getSupportableOrganization().getUnit().value,
                    displayInfoOrg.getCode(),
                    displayInfoOrg.getName()
            ));
        }

        List<EmployeeInformationImport> employeeInfos = this.employeeInfoAdapter
                .getEmployeeInfo(new EmployeeInformationQueryDtoImport(employeeIds, baseDate, false, false, false,
                        false, false, false));

        return new Kdl016ScreenBOutput(
                employeeInfos.stream().map(e -> new EmployeeInformationDto(e.getEmployeeId(), e.getEmployeeCode(), e.getBusinessName())).sorted(Comparator.comparing(x -> x.getEmployeeCode())).collect(Collectors.toList()),
                orgDisplayInfoList.stream().sorted(Comparator.comparing(OrganizationDisplayInfoDto::getOrgCode)).collect(Collectors.toList())
        );
    }

    /**
     * UKDesign.UniversalK.??????.KDL_???????????????.KDL016_???????????????????????????.C:????????????????????????????????????.???????????????OCD.????????????
     *
     * @param orgId
     * @param orgUnit
     * @param period
     * @return
     */
    public Kdl016ScreenBOutput getDataInitScreenC(String orgId, int orgUnit, DatePeriod period) {
        // 1.????????????(??????ID, ??????, ??????)
        List<OrganizationDisplayInfoDto> organizationDisplayInfos = this.getOrgInfoOfSupportSource(orgId, orgUnit, period);

        // 2. ????????????(??????ID, ??????, ??????)
        List<EmployeeInformationDto> employeeInfos = this.getEmployeeInfo(orgId, orgUnit, period);

        return new Kdl016ScreenBOutput(employeeInfos, organizationDisplayInfos);
    }

    /**
     * ???????????????????????????????????????
     * UKDesign.UniversalK.??????.KDL_???????????????.KDL016_???????????????????????????.C:????????????????????????????????????.???????????????OCD.???????????????????????????????????????.???????????????????????????????????????
     *
     * @param orgId
     * @param orgUnit
     * @return
     */
    public List<OrganizationDisplayInfoDto> getOrgInfoOfSupportSource(String orgId, int orgUnit, DatePeriod period) {
        GeneralDate baseDate = GeneralDate.today();
        // 1. ??????????????????: ????????????????????????
        // 2. ??????????????????????????????: ????????????????????????
        TargetOrgIdenInfor targetOrgIdenInfor = orgUnit == TargetOrganizationUnit.WORKPLACE.value
                ? TargetOrgIdenInfor.creatIdentifiWorkplace(orgId)
                : TargetOrgIdenInfor.creatIdentifiWorkplaceGroup(orgId);

        // 3. List<????????????????????????>: ????????????(????????????????????????)
        List<SupportAllowOrganization> supportAllowOrgs = supportAllowOrganizationFinder.getListBySupportableOrg(AppContexts.user().companyId(), targetOrgIdenInfor);

        // 4. List<????????????????????????> empty
        if (supportAllowOrgs.isEmpty()) {
            throw new BusinessException("Msg_3279");
        }

        RequireOrgInfoImpl requireOrgInfo = new RequireOrgInfoImpl(groupAdapter, serviceAdapter, wplAdapter);
        List<OrganizationDisplayInfoDto> orgDisplayInfoList = new ArrayList<>();
        for (SupportAllowOrganization supportAllowOrg : supportAllowOrgs) {
            DisplayInfoOrganization displayInfoOrg = supportAllowOrg.getTargetOrg().getDisplayInfor(requireOrgInfo, baseDate);
            orgDisplayInfoList.add(new OrganizationDisplayInfoDto(
                    supportAllowOrg.getTargetOrg().getUnit() == TargetOrganizationUnit.WORKPLACE
                            ? supportAllowOrg.getTargetOrg().getWorkplaceId().orElse(null)
                            : supportAllowOrg.getTargetOrg().getWorkplaceGroupId().orElse(null),
                    supportAllowOrg.getTargetOrg().getUnit().value,
                    displayInfoOrg.getCode(),
                    displayInfoOrg.getName()
            ));
        }

        return orgDisplayInfoList.stream().sorted(Comparator.comparing(OrganizationDisplayInfoDto::getOrgCode)).collect(Collectors.toList());
    }

    public Kdl016ScreenBOutput.Kdl016ScreenBOutputV2 getDataInitScreenCV2(String orgId, int orgUnit, DatePeriod period) {
        // 1.????????????(??????ID, ??????, ??????)
        val organizationDisplayInfos = this.getOrgInfoOfSupportSourceV2(orgId, orgUnit, period);

        // 2. ????????????(??????ID, ??????, ??????)
        List<EmployeeInformationDto> employeeInfos = this.getEmployeeInfo(orgId, orgUnit, period);

        return new Kdl016ScreenBOutput.Kdl016ScreenBOutputV2(employeeInfos, organizationDisplayInfos);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param orgId
     * @param orgUnit
     * @param period
     * @return Map<??????ID   ???   ?????????????????????>
     */
    public Map<String, DisplayInfoOrganization> getOrgInfoOfSupportSourceV2(String orgId, int orgUnit, DatePeriod period) {
        GeneralDate baseDate = GeneralDate.today();
        Map<String, DisplayInfoOrganization> resultMap = new HashMap<>();

        // 1. ??????????????????: ????????????????????????
        // 2. ??????????????????????????????: ????????????????????????
        TargetOrgIdenInfor targetOrgIdenInfor = orgUnit == TargetOrganizationUnit.WORKPLACE.value
                ? TargetOrgIdenInfor.creatIdentifiWorkplace(orgId)
                : TargetOrgIdenInfor.creatIdentifiWorkplaceGroup(orgId);

        // 3. List<????????????????????????>: ????????????(????????????????????????)
        List<SupportAllowOrganization> supportAllowOrgs = supportAllowOrganizationFinder.getListBySupportableOrg(AppContexts.user().companyId(), targetOrgIdenInfor);

        // 4. List<????????????????????????> empty
        if (supportAllowOrgs.isEmpty()) {
            throw new BusinessException("Msg_3279");
        }

        RequireOrgInfoImpl requireOrgInfo = new RequireOrgInfoImpl(groupAdapter, serviceAdapter, wplAdapter);
        for (SupportAllowOrganization supportAllowOrg : supportAllowOrgs) {
            String id = supportAllowOrg.getSupportableOrganization().getUnit() == TargetOrganizationUnit.WORKPLACE
                    ? supportAllowOrg.getSupportableOrganization().getWorkplaceId().orElse(null)
                    : supportAllowOrg.getSupportableOrganization().getWorkplaceGroupId().orElse(null);
            DisplayInfoOrganization displayInfoOrg = supportAllowOrg.getSupportableOrganization().getDisplayInfor(requireOrgInfo, baseDate);
            resultMap.put(id, displayInfoOrg);
        }

        return resultMap;
    }

    /**
     * ??????????????????????????????????????????
     * UKDesign.UniversalK.??????.KDL_???????????????.KDL016_???????????????????????????.C:????????????????????????????????????.???????????????OCD.??????????????????????????????????????????.??????????????????????????????????????????
     *
     * @param orgId
     * @param orgUnit
     * @param period
     * @return
     */
    public List<EmployeeInformationDto> getEmployeeInfo(String orgId, int orgUnit, DatePeriod period) {
        // ?????????????????? : ??????ID,?????? : List?????????ID?????????CD???????????????
        if (orgUnit == TargetOrganizationUnit.WORKPLACE.value) {
            // [No.597]????????????????????????????????????
            List<EmployeeInfoImported> employeeInfos = workplaceAdapter.getLstEmpByWorkplaceIdsAndPeriod(Collections.singletonList(orgId), period);
            return employeeInfos.stream().map(e -> new EmployeeInformationDto(e.getSid(), e.getEmployeeCode(), e.getEmployeeName()))
                    .sorted(Comparator.comparing(EmployeeInformationDto::getEmployeeCode)).collect(Collectors.toList());
        } else { // ?????????????????????????????? : require, ??????, ??????????????????ID : List<?????????????????????>
            val requireGetAllEmp = new RequireAllEmpWhoBelongWklGroupSv(affWorkplaceGroupRepo, workplacePub);
            val employeeInfos = GetAllEmpWhoBelongWorkplaceGroupService.getAllEmp(requireGetAllEmp, period, orgId);
            return employeeInfos.stream().map(e -> new EmployeeInformationDto(
                    e.getEmployeeID(),
                    e.getEmployeeCode().isPresent() ? e.getEmployeeCode().get().v() : "",
                    e.getBusinessName().isPresent() ? e.getBusinessName().get() : ""))
                    .sorted(Comparator.comparing(EmployeeInformationDto::getEmployeeCode)).collect(Collectors.toList());
        }
    }

    @AllArgsConstructor
    private class RequireOrgInfoImpl implements TargetOrgIdenInfor.Require {
        private WorkplaceGroupAdapter groupAdapter;
        private WorkplaceExportServiceAdapter serviceAdapter;
        private AffWorkplaceAdapter wplAdapter;

        @Override
        public List<WorkplaceGroupImport> getSpecifyingWorkplaceGroupId(List<String> workplacegroupId) {
            return groupAdapter.getbySpecWorkplaceGroupID(workplacegroupId);
        }

        @Override
        public List<WorkplaceInfo> getWorkplaceInforFromWkpIds(List<String> listWorkplaceId, GeneralDate baseDate) {
            return serviceAdapter.getWorkplaceInforByWkpIds(AppContexts.user().companyId(), listWorkplaceId, baseDate).stream()
                    .map(mapper -> new WorkplaceInfo(mapper.getWorkplaceId(), Optional.ofNullable(mapper.getWorkplaceCode()), Optional.ofNullable(mapper.getWorkplaceName()), Optional.ofNullable(mapper.getWorkplaceExternalCode()),
                            Optional.ofNullable(mapper.getWorkplaceGenericName()), Optional.ofNullable(mapper.getWorkplaceDisplayName()), Optional.ofNullable(mapper.getHierarchyCode()))).collect(Collectors.toList());
        }

        @Override
        public List<String> getWKPID(String WKPGRPID) {
            return wplAdapter.getWKPID(AppContexts.user().companyId(), WKPGRPID);
        }
    }

    @AllArgsConstructor
    private class RequireOrgImpl implements GetTargetIdentifiInforService.Require {
        private EmpAffiliationInforAdapter empAffiliationInforAdapter;

        @Override
        public List<EmpOrganizationImport> getEmpOrganization(GeneralDate referenceDate, List<String> listEmpId) {
            return empAffiliationInforAdapter.getEmpOrganization(referenceDate, listEmpId);
        }
    }

    @AllArgsConstructor
    private class RequireEmpImpl implements GetEmpCanReferService.Require {
        private WorkplaceGroupAdapter workplaceGroupAdapter;
        private RegulationInfoEmployeeAdapter regulInfoEmpAdap;
        private RegulationInfoEmployeePub regulInfoEmpPub;

        @Override
        public List<String> getEmpCanReferByWorkplaceGroup(String empId, GeneralDate date, DatePeriod period, String workplaceGroupID) {
            List<String> data = workplaceGroupAdapter.getReferableEmp(empId, date, period, workplaceGroupID);
            return data;
        }

        @Override
        public List<String> sortEmployee(List<String> lstmployeeId, EmployeeSearchCallSystemType sysAtr, Integer sortOrderNo,
                                         GeneralDate referenceDate, Integer nameType) {

            List<String> data = regulInfoEmpAdap.sortEmployee(
                    AppContexts.user().companyId(),
                    lstmployeeId,
                    sysAtr.value,
                    sortOrderNo,
                    nameType,
                    GeneralDateTime.fromString(referenceDate.toString() + SPACE + ZEZO_TIME, DATE_TIME_FORMAT));
            return data;
        }

        @Override
        public String getRoleID() {

            return AppContexts.user().roles().forAttendance();
        }

        @Override
        public List<String> searchEmployee(RegulationInfoEmpQuery q, String roleId) {
            EmployeeSearchQueryDto query = EmployeeSearchQueryDto.builder()
                    .baseDate(GeneralDateTime.fromString(q.getBaseDate().toString() + SPACE + ZEZO_TIME, DATE_TIME_FORMAT))
                    .referenceRange(q.getReferenceRange().value)
                    .systemType(q.getSystemType().value)
                    .filterByWorkplace(q.getFilterByWorkplace())
                    .workplaceCodes(q.getWorkplaceIds())
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
                    .periodStart(GeneralDateTime.fromString(q.getPeriodStart() + " 00:00", "yyyy/MM/dd HH:mm"))
                    .periodEnd(GeneralDateTime.fromString(q.getPeriodEnd() + " 00:00", "yyyy/MM/dd HH:mm"))
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
        public List<String> getAllEmpCanReferByWorkplaceGroup(String empId, GeneralDate date, DatePeriod period) {
            // don't have to implement it
            return null;
        }
    }

    @AllArgsConstructor
    private class RequireAllEmpWhoBelongWklGroupSv implements GetAllEmpWhoBelongWorkplaceGroupService.Require {
        private AffWorkplaceGroupRespository repo;

        private WorkplacePub pub;

        @Override
        public List<String> getWorkplaceBelongsWorkplaceGroup(String workplaceGroupId) {
            return repo.getWKPID(AppContexts.user().companyId(), workplaceGroupId);
        }

        @Override
        public List<EmployeeInfoData> getEmployeesWhoBelongWorkplace(String workplaceId, DatePeriod datePeriod) {
            // [No.597]????????????????????????????????????
            List<ResultRequest597Export> data = pub.getLstEmpByWorkplaceIdsAndPeriod(Arrays.asList(workplaceId),
                    datePeriod);
            List<EmployeeInfoData> result = data.stream()
                    .map(c -> new EmployeeInfoData(c.getSid(), c.getEmployeeCode(), c.getEmployeeName()))
                    .collect(Collectors.toList());
            return result;
        }

    }

    @Getter
    @AllArgsConstructor
    public enum DisplayMode {

        /**
         * ?????????????????????
         **/
        GO_TO_SUPPORT(1),
        /**  **/
        COME_TO_SUPPORT(2);

        public final int value;

    }
}
