package nts.uk.ctx.sys.assist.app.find.salary;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.sys.assist.dom.salary.SpecPrintYmSet;

/**
* 明細書印字年月設定
*/
@AllArgsConstructor
@Value
public class SpecPrintYmSetDto
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
    * PROCESS_DATE
    */
    private int processDate;
    
    /**
    * PRINT_DATE
    */
    private int printDate;
    
    
    public static SpecPrintYmSetDto fromDomain(SpecPrintYmSet domain)
    {
        return new SpecPrintYmSetDto(domain.getCid(), domain.getProcessCateNo(), domain.getProcessDate(), domain.getPrintDate());
    }
    
}
