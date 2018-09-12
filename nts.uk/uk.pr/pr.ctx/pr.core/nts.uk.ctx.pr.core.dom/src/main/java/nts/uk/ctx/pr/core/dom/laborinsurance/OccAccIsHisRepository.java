package nts.uk.ctx.pr.core.dom.laborinsurance;


import nts.uk.shr.com.history.YearMonthHistoryItem;

import javax.ejb.Stateless;
import java.util.Optional;
import java.util.List;

/**
* 労災保険履歴
*/
@Stateless
public interface OccAccIsHisRepository
{

    Optional<OccAccIsHis> getAllOccAccIsHisByCid(String cid);


    void add(YearMonthHistoryItem domain, String cId);

    void update(YearMonthHistoryItem domain,String cId);

    void remove(String cid, String hisId);

}
