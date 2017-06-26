package nts.uk.ctx.pr.core.dom.rule.employment.layout;

import java.util.List;
import java.util.Optional;

public interface LayoutHistRepository {

	/**
	 * QSTMT_STMT_LAYOUT_HIST - SEL-1
	 * @return
	 */
	List<LayoutHistory> getBy_SEL_1(String companyCode, int baseYM);
	List<LayoutHistory> getBy_SEL_2(String companyCode, String stmtCode);
	List<LayoutHistory> getBy_SEL_3(String companyCode, int baseYM);
	Optional<LayoutHistory> getBy_SEL_4(String companyCode,  String stmtCode, String historyId);
	Optional<LayoutHistory> getHistoryBefore(String companyCode, String stmtCode, int startYear);
	List<LayoutHistory> getBy_SEL_5(String companyCode, int baseYM);
	void add(LayoutHistory layoutHistory);
	void update(LayoutHistory layoutHistory);
	void remove(String companyCode, String stmtCode, String history);
	
	List<LayoutHistory> getAllLayoutHist(String companyCode);
	
}
