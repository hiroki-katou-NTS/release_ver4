package nts.uk.ctx.pr.file.app.core.socialinsurnoticreset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.core.dom.socialinsurance.socialinsuranceoffice.SocialInsuranceOffice;
import nts.uk.ctx.pr.report.dom.printconfig.socinsurnoticreset.EmpAddChangeInfo;
import nts.uk.ctx.pr.report.dom.printconfig.socinsurnoticreset.SocialInsurNotiCreateSet;
import nts.uk.ctx.pr.shared.dom.familyinfo.empfamilysocialins.EmpFamilySocialIns;
import nts.uk.ctx.pr.shared.dom.socialinsurance.employeesociainsur.empcomofficehis.SocialInsuranceOfficeCode;
import nts.uk.ctx.pr.shared.dom.socialinsurance.employeesociainsur.emphealinsurbeneinfo.EmpBasicPenNumInfor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpAddChangeInforData {
    /*SocialInsurNotiCreateSet socialInsurNotiCreateSet;
    CompanyInformation companyInformation;
    SocialInsuranceOffice socialInsuranceOffice;*/
    /*List<PersonInformation> personInformationList;
    List<PersonCurrentAddress> personCurrentAddressList;
    List<EmpFamilySocialIns> empFamilySocialInsList;
    List<EmpBasicPenNumInfor> empBasicPenNumInforList;
    List<FamilyInformation> familyInformationList;
    List<FamilyBeforeAddress> familyBeforeAddressList;
    List<FamilyCurrentAddress> familyCurrentAddressList;
    List<FamilyResidence> familyResidenceList;
    List<EmpAddChangeInfo> empAddChangeInfoList;*/
    List<EmpAddChangeInfoExport> empAddChangeInfoExportList;
    GeneralDate baseDate;
}
