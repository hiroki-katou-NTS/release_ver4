package nts.uk.ctx.sys.assist.app.command.salary;

import lombok.Value;

@Value
public class DetailPrintingMonCommand
{
    
    /**
    * 処理区分NO
    */
    private int processCateNo;
    
    /**
    * 会社ID
    */
    private String cid;
    
    /**
    * 印字月
    */
    private int printingMonth;
    
    private Long version;

}
