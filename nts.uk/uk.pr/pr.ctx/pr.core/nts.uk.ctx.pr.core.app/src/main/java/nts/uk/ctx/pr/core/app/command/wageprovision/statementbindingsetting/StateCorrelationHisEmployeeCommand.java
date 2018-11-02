package nts.uk.ctx.pr.core.app.command.wageprovision.statementbindingsetting;

import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;

@Value
public class StateCorrelationHisEmployeeCommand {
    
    /**
    * 会社ID
    */
    private String cid;
    
    /**
    * 履歴ID
    */
    private String hisId;
    
    /**
    * 開始年月
    */
    private int startYearMonth;
    
    /**
    * 終了年月
    */
    private int endYearMonth;
    

}
