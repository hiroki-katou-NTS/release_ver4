package nts.uk.ctx.exio.dom.exo.categoryitemdata;

import java.util.Optional;
import java.util.List;

/**
* 外部出力カテゴリ項目データ
*/
public interface CtgItemDataRepository
{

    List<CtgItemData> getAllCtgItemData();
    
    List<CtgItemData> getAllByCategoryId(Integer categoryId);
    
    List<CtgItemData> getAllByKey(Integer categoryId, int itemNo);

    Optional<CtgItemData> getCtgItemDataById(Integer categoryId, Integer itemNo);
    
    Optional<CtgItemData> getCtgItemDataByIdAndDisplayClass(Integer categoryId, Integer itemNo, int displayClassfication);

    List<CtgItemData> getByIdAndDisplayClass(Integer categoryId, Optional<Integer> itemNo, int displayClassfication);
    
    void add(CtgItemData domain);

    void update(CtgItemData domain);

    void remove();

}
