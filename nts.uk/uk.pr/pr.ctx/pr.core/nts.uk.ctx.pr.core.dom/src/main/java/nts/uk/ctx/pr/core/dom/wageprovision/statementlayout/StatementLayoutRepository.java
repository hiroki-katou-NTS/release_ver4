package nts.uk.ctx.pr.core.dom.wageprovision.statementlayout;

import java.util.Optional;
import java.util.List;

/**
* 明細書レイアウト
*/
public interface StatementLayoutRepository {

    List<StatementLayout> getStatement(String cid, int startYearMonth);

    List<StatementLayout> getAllStatementLayoutByCid(String cid);

    Optional<StatementLayout> getStatementLayoutById(String cid, String statementCd);

    List<StatementLayout> getAllStatementLayoutByCidAndCodes(String cid, List<String> statementCds);

    List<StatementLayout> getStatementLayoutByCId(String cid);

    void add(StatementLayout domain);

    void update(StatementLayout domain);

    void remove(String cid, String statementCd);

}
