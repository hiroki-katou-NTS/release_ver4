package nts.uk.ctx.exio.app.find.exo.condset;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.exio.dom.exo.condset.StdOutputCondSet;

/**
* 出力条件設定（定型）
*/
@AllArgsConstructor
@Value
public class StdOutputCondSetDto
{
    
    /**
    * 会社ID
    */
    private String cid;
    
    /**
    * 外部出力条件コード
    */
    private String conditionSetCd;
    
    /**
    * カテゴリID
    */
    private String categoryId;
    
    /**
    * 区切り文字
    */
    private int delimiter;
    
    /**
    * するしない区分
    */
    private int itemOutputName;
    
    /**
    * するしない区分
    */
    private int autoExecution;
    
    /**
    * 外部出力条件名称
    */
    private String conditionSetName;
    
    /**
    * するしない区分
    */
    private int conditionOutputName;
    
    /**
    * 文字列形式
    */
    private int stringFormat;
    
    
    public static StdOutputCondSetDto fromDomain(StdOutputCondSet domain)
    {
        return new StdOutputCondSetDto(domain.getCid(), domain.getConditionSetCd(), domain.getCategoryId(), domain.getDelimiter(), domain.getItemOutputName(), domain.getAutoExecution(), domain.getConditionSetName(), domain.getConditionOutputName(), domain.getStringFormat());
    }
    
}
