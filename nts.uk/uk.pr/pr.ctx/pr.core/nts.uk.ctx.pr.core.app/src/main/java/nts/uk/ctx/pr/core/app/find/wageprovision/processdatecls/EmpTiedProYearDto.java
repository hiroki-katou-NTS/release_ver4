package nts.uk.ctx.pr.core.app.find.wageprovision.processdatecls;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.pr.core.dom.wageprovision.processdatecls.EmpTiedProYear;

/**
* 処理年月に紐づく雇用
*/
@AllArgsConstructor
@Value
public class EmpTiedProYearDto
{
    
    /**
    * CID
    */
    private String cid;
    
    /**
    * PROCESS_CATE_NO
    */
    private int processCateNo;
    
    /**
    * EMPLOYMENT_CODE
    */
    private String employmentCode;
    
    
    public static EmpTiedProYearDto fromDomain(EmpTiedProYear domain)
    {
        return new EmpTiedProYearDto(domain.getCid(), domain.getProcessCateNo(), domain.getEmploymentCode().v());
    }



    
}
