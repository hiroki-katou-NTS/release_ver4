package nts.uk.ctx.sys.assist.app.find.saveProtection;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.ctx.sys.assist.dom.saveProtection.SaveProtection;
/**
* 保存保護
*/
@AllArgsConstructor
@Value
public class SaveProtectionDto
{
    
    /**
    * カテゴリID
    */
    private int categoryId;
    
    /**
    * 補正区分
    */
    private int correctClasscification;
    
    /**
    * 置き換え列
    */
    private String replaceColumn;
    
    /**
    * テーブルNo
    */
    private int tableNo;
    
    
    public static SaveProtectionDto fromDomain(SaveProtection domain)
    {
        return new SaveProtectionDto(domain.getCategoryId(), domain.getCorrectClasscification(), domain.getReplaceColumn(), domain.getTableNo());
    }
    
}
