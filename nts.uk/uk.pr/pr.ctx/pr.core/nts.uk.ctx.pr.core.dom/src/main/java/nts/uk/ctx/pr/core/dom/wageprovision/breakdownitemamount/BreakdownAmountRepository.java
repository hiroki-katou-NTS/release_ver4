package nts.uk.ctx.pr.core.dom.wageprovision.breakdownitemamount;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.List;

/**
* 給与内訳個人金額
*/
public interface BreakdownAmountRepository
{

    List<BreakdownAmount> getAllBreakdownAmount();

    Optional<BreakdownAmount> getAllBreakdownAmountCode(String historyId);

    void add(BreakdownAmount domain);

    void update(BreakdownAmount domain);

    void remove(String historyId, List<String> lstBreakdownItemCode);

    void removeByHistoryId(String historyId);


}
