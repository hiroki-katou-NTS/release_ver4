package nts.uk.ctx.pr.file.app.core.empinsqualifiinfo.empinsofficeinfo;

import nts.arc.error.BusinessException;
import nts.arc.layer.app.file.export.ExportService;
import nts.arc.layer.app.file.export.ExportServiceContext;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.core.dom.adapter.company.CompanyInforAdapter;
import nts.uk.ctx.pr.core.dom.adapter.employee.employee.EmployeeInfoAdapter;
import nts.uk.ctx.pr.core.dom.adapter.employee.employee.EmployeeInfoEx;
import nts.uk.ctx.pr.core.dom.adapter.person.PersonExport;
import nts.uk.ctx.pr.core.dom.adapter.person.PersonExportAdapter;
import nts.uk.ctx.pr.core.dom.laborinsurance.laborinsuranceoffice.LaborInsuranceOffice;
import nts.uk.ctx.pr.report.dom.printconfig.empinsreportsetting.EmpInsReportSetting;
import nts.uk.ctx.pr.report.dom.printconfig.empinsreportsetting.EmpInsReportSettingRepository;
import nts.uk.ctx.pr.shared.dom.empinsqualifiinfo.empinsofficeinfo.EmpEstabInsHistRepository;
import nts.uk.ctx.pr.shared.dom.empinsqualifiinfo.employmentinsqualifiinfo.EmpInsHist;
import nts.uk.ctx.pr.shared.dom.empinsqualifiinfo.employmentinsqualifiinfo.EmpInsHistRepository;
import nts.uk.ctx.pr.shared.dom.empinsqualifiinfo.employmentinsqualifiinfo.EmpInsNumInfo;
import nts.uk.ctx.pr.shared.dom.empinsqualifiinfo.employmentinsqualifiinfo.EmpInsNumInfoRepository;
import nts.uk.shr.com.context.AppContexts;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static nts.uk.ctx.pr.report.dom.printconfig.empinsreportsetting.EmpSubNameClass.PERSONAL_NAME;

@Stateless
public class NotifiOfChangInNameInsPerPDFService extends ExportService<NotifiOfChangInNameInsPerExportQuery> {

    @Inject
    private EmpInsReportSettingRepository mEmpInsReportSettingRepository;

    @Inject
    private EmpInsHistRepository mEmpInsHistRepository;

    @Inject
    private CompanyInforAdapter mCompanyInforAdapter;

    @Inject
    private NotifiOfChangInNameInsPerExRepository empInsReportSettingExRepository;

    @Inject
    private EmployeeInfoAdapter employeeInfoAdapter;

    @Inject
    private PersonExportAdapter mPersonExportAdapter;

    @Inject
    private EmpInsNumInfoRepository mEmpInsNumInfoRepository;

    @Inject
    private NotifiOfChangInNameInsPerExFileGenerator generator;



    @Override
    protected void handle(ExportServiceContext<NotifiOfChangInNameInsPerExportQuery> exportServiceContext) {
        String cid = AppContexts.user().companyId();
        String userId = AppContexts.user().userId();
        NotifiOfChangInNameInsPerExport mEmpInsReportSettingExport =  exportServiceContext.getQuery().getEmpInsReportSettingExport();
        GeneralDate fillingDate = exportServiceContext.getQuery().getFillingDate();
        List<EmployeeChangeDate> empIds = exportServiceContext.getQuery().getEmpIdChangDate();
        EmpInsReportSetting empInsReportSetting = new EmpInsReportSetting(
                cid,
                userId,
                mEmpInsReportSettingExport.getSubmitNameAtr(),
                mEmpInsReportSettingExport.getOutputOrderAtr(),
                mEmpInsReportSettingExport.getOfficeClsAtr(),
                mEmpInsReportSettingExport.getMyNumberClsAtr(),
                mEmpInsReportSettingExport.getNameChangeClsAtr()
        );
        mEmpInsReportSettingRepository.update(empInsReportSetting);
        // data export
        List<NotifiOfChangInNameInsPerExportData> listDataExport = new ArrayList<>();
        List<EmployeeInfoEx> employee = employeeInfoAdapter.findBySIds(empIds.stream().map(EmployeeChangeDate::getEmployeeId).collect(Collectors.toList()));
        List<PersonExport> personExports = mPersonExportAdapter.findByPids(employee.stream().map(EmployeeInfoEx::getPId).collect(Collectors.toList()));

        employee.forEach((EmployeeInfoEx e) ->{

            NotifiOfChangInNameInsPerExportData temp = new NotifiOfChangInNameInsPerExportData();
            Optional<EmpInsHist> mEmpInsHist = mEmpInsHistRepository.getEmpInsHistById(cid,e.getEmployeeId());
            if(!mEmpInsHist.isPresent()){
                return;
            }
            temp.setEmpInsHist(mEmpInsHist.get());
            temp.setChangeDate(empIds.stream().filter(eChangDate->eChangDate.getEmployeeId().equals(e.getEmployeeId())).findFirst().get().getChangeDate());
            temp.setFillingDate(fillingDate.toString());
            temp.setEmpInsReportSetting(empInsReportSetting);
            switch (empInsReportSetting.getOfficeClsAtr()){
                case OUTPUT_COMPANY:{
                    temp.setCompanyInfor(mCompanyInforAdapter.getCompanyNotAbolitionByCid(cid));
                    break;
                }
                case OUPUT_LABOR_OFFICE:{
                    List<LaborInsuranceOffice> lstLaborInsuranceOffice =  empInsReportSettingExRepository.getListEmpInsHistByDate(cid,e.getEmployeeId(),fillingDate);
                    temp.setLaborInsuranceOffice(lstLaborInsuranceOffice.isEmpty() ? null : lstLaborInsuranceOffice.get(0));
                    break;
                }
                case DO_NOT_OUTPUT:{
                    break;
                }
            };
            String hisId = mEmpInsHist.get().getHistoryItem().get(0).identifier();
            Optional<EmpInsNumInfo> empInsNumInfo = mEmpInsNumInfoRepository.getEmpInsNumInfoById(cid,e.getEmployeeId(),hisId);
            temp.setEmpInsNumInfo(empInsNumInfo.orElseGet(() -> new EmpInsNumInfo(hisId, "")));
            // dummy data thuật toán ドメインモデル「外国人在留履歴情報」を取得する
            Optional<PersonExport> person = personExports.stream().filter(dataPerson -> dataPerson.getPersonId().equals(e.getPId())).findFirst();
            if(person.isPresent()){
                temp.setBrithDay(person.get().getBirthDate().toString());
                temp.setName(person.get().getPersonNameGroup().getPersonName().getFullName());
                temp.setNameKana(person.get().getPersonNameGroup().getPersonName().getFullNameKana());
                temp.setFullName(person.get().getPersonNameGroup().getPersonRomanji().getFullName());
                temp.setFullNameKana(person.get().getPersonNameGroup().getPersonRomanji().getFullNameKana());
                temp.setReportFullName(person.get().getPersonNameGroup().getTodokedeFullName().getFullName());
                temp.setReportFullNameKana(person.get().getPersonNameGroup().getTodokedeFullName().getFullNameKana());
                temp.setGender(person.get().getGender());
                temp.setOldName(person.get().getPersonNameGroup().getOldName().getFullName());
                temp.setOldNameKana(person.get().getPersonNameGroup().getOldName().getFullNameKana());
            }
            temp.setEmployeeCode(e.getEmployeeCode());
            listDataExport.add(temp);


        });
        if(listDataExport.isEmpty()){
            throw new BusinessException("MsgQ_51");
        }
        switch (empInsReportSetting.getOutputOrderAtr()){
            case INSURANCE_NUMBER:{
                listDataExport.sort(Comparator.comparing(o -> o.getEmpInsNumInfo().getEmpInsNumber().v()));
                break;
            }
            case DEPARTMENT_EMPLOYEE:{
                listDataExport.sort(Comparator.comparing(NotifiOfChangInNameInsPerExportData::getEmployeeCode));
                break;
            }
            case EMPLOYEE_CODE:{
                listDataExport.sort(Comparator.comparing(NotifiOfChangInNameInsPerExportData::getEmployeeCode));
                break;
            }
            case EMPLOYEE:{
                if(empInsReportSetting.getSubmitNameAtr() == PERSONAL_NAME ){
                    listDataExport.sort((o1, o2) -> {
                        if (o1.getNameKana().compareTo(o2.getNameKana()) == 0) {
                            return o1.getEmployeeCode().compareTo(o2.getEmployeeCode());
                        } else {
                            return o1.getNameKana().compareTo(o2.getNameKana());
                        }
                    });
                }
                else{
                    listDataExport.sort((o1, o2) -> {
                        if (o1.getReportFullNameKana().compareTo(o2.getReportFullNameKana()) == 0) {
                            return o1.getEmployeeCode().compareTo(o2.getEmployeeCode());
                        } else {
                            return o1.getReportFullNameKana().compareTo(o2.getReportFullNameKana());
                        }
                    });
                }
                break;
            }

        }
        generator.generate(exportServiceContext.getGeneratorContext(),listDataExport);
    }
}
