package nts.uk.ctx.sys.assist.dom.saveprotetion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;

/**
* 保存保護
*/
@AllArgsConstructor
@Getter
public class SaveProtetion extends AggregateRoot
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
    
     /**
      * <<Optional>> 結合用PID項目名
      */
    private String couplePidItemName;
    
    public SaveProtetion(int categoryId, int correctClasscification, String replaceColumn, int tableNo) {
 		super();
 		this.categoryId = categoryId;
 		this.correctClasscification = correctClasscification;
 		this.replaceColumn = replaceColumn;
 		this.tableNo = tableNo;
 	}
    
    
}
