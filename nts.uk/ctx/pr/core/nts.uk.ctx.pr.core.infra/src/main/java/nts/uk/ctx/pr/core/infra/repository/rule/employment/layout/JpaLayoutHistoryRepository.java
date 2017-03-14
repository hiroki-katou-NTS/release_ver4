/**
 * 
 */
package nts.uk.ctx.pr.core.infra.repository.rule.employment.layout;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutHistRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.LayoutHistory;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.layout.QstmtStmtLayoutHistory;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.layout.QstmtStmtLayoutHistoryPK;

/**
 * @author lanlt
 *
 */
public class JpaLayoutHistoryRepository extends JpaRepository implements LayoutHistRepository {
	private final String SELECT_NO_WHERE = "SELECT c FROM QstmtStmtLayoutHistory c";
	private final String SELECT_ALL = SELECT_NO_WHERE + " WHERE c.QstmtStmtLayoutHistoryPK.companyCd = :companyCd";
	private final String SEL_1 = SELECT_NO_WHERE + " WHERE c.qstmtStmtLayoutHistPK.companyCd = :companyCode "
			+ " AND c.startYear <= :baseYearMonth " + " AND c.endYear >= :baseYearMonth ";
	private final String SEL_2 = SELECT_NO_WHERE + " WHERE c.qstmtStmtLayoutHistPK.companyCd = :companyCode "
			+ " AND c.qstmtStmtLayoutHeadPK.stmtCd = :stmtCd";
	private final String SEL_3 = SELECT_NO_WHERE + " WHERE c.qstmtStmtLayoutHistPK.companyCd = :companyCode "
			+ " AND c.qstmtStmtLayoutHeadPK.stmtCd = :stmtCd" + " AND c.endYear = 999912";
	private final String SEL_4 = SEL_2 + " AND c.qstmtStmtLayoutHistPK.historyId = :historyId ";
	private final String SEL_HISTORY_BEFORE = SEL_2 + " AND c.startYear = :startYear ";
	private final String SEL_5 = SELECT_NO_WHERE + " WHERE c.qstmtStmtLayoutHistPK.companyCd = :companyCode "
			+ " AND c.qstmtStmtLayoutHistPK.stmtCd = :stmtCode" + " AND c.strYm <= :baseYearMonth "
			+ " AND c.endYm >= :baseYearMonth ";

	private final LayoutHistory toDomain(QstmtStmtLayoutHistory entity) {
		val domain = LayoutHistory.createFromJavaType(entity.qstmtStmtLayoutHistPK.companyCd,
				entity.qstmtStmtLayoutHistPK.stmtCd, entity.qstmtStmtLayoutHistPK.historyId, entity.startYear,
				entity.endYear, entity.layoutAttr);

		return domain;
	}

	private static QstmtStmtLayoutHistory toEntity(LayoutHistory domain) {
		QstmtStmtLayoutHistory entity = new QstmtStmtLayoutHistory();
		entity.qstmtStmtLayoutHistPK = new QstmtStmtLayoutHistoryPK(domain.getCompanyCode().v(),
				domain.getStmtCode().v(), domain.getHistoryId());
		entity.startYear = domain.getStartYm().v();
		entity.endYear = domain.getEndYm().v();
		entity.layoutAttr = domain.getLayoutAtr().value;

		return entity;
	}

	@Override
	public List<LayoutHistory> getBy_SEL_1(String companyCd, YearMonth baseYM) {

		return this.queryProxy().query(SEL_1, QstmtStmtLayoutHistory.class).setParameter("companyCd", companyCd)
				.setParameter("baseYearMonth", baseYM).getList(x -> toDomain(x));
	}

	@Override
	public List<LayoutHistory> getBy_SEL_2(String companyCd, String stmtCd) {
		return this.queryProxy().query(SEL_2, QstmtStmtLayoutHistory.class).setParameter("companyCd", companyCd)
				.setParameter("stmtCd", stmtCd).getList(x -> toDomain(x));
	}

	@Override
	public List<LayoutHistory> getBy_SEL_3(String companyCd, YearMonth baseYM) {
		return this.queryProxy().query(SEL_3, QstmtStmtLayoutHistory.class).setParameter("companyCd", companyCd)
				.setParameter("baseYearMonth", baseYM).getList(x -> toDomain(x));
	}

	@Override
	public Optional<LayoutHistory> getBy_SEL_4(String companyCd, String stmtCd, String historyId) {
		return this.queryProxy().query(SEL_4, QstmtStmtLayoutHistory.class).setParameter("companyCd", companyCd)
				.setParameter("stmtCd", stmtCd).setParameter("historyId", historyId).getSingle(x -> toDomain(x));
	}

	@Override
	public List<LayoutHistory> getBy_SEL_5(String companyCd, YearMonth baseYM) {
		return this.queryProxy().query(SEL_5, QstmtStmtLayoutHistory.class).setParameter("companyCd", companyCd)
				.setParameter("baseYearMonth", baseYM).getList(x -> toDomain(x));
	}
	@Override
	public void add(LayoutHistory layoutHistory) {
		this.commandProxy().insert(toEntity(layoutHistory));
		
	}

	@Override
	public void update(LayoutHistory layoutHistory) {
		try {
			this.commandProxy().update(toEntity(layoutHistory));
		} catch (Exception ex) {
			throw ex;
		}
		
	}

	@Override
	public void remove(String companyCode, String stmtCode, String history) {
		val objectKey = new QstmtStmtLayoutHistoryPK();
		objectKey.companyCd = companyCode;
		objectKey.stmtCd = stmtCode;
		objectKey.historyId = history;
		this.commandProxy().remove(QstmtStmtLayoutHistory.class, objectKey);
	}

	@Override
	public List<LayoutHistory> getAllLayoutHist(String companyCode) {
		return this.queryProxy().query(SELECT_ALL, QstmtStmtLayoutHistory.class)
				.setParameter("companyCode", companyCode)
				.getList(entity -> toDomain(entity));
	}

	@Override
	public Optional<LayoutHistory> getHistoryBefore(String companyCd, String stmtCd, int startYear) {
		return this.queryProxy().query(SEL_HISTORY_BEFORE, QstmtStmtLayoutHistory.class).setParameter("companyCd", companyCd)
				.setParameter("stmtCd", stmtCd).setParameter("startYear", startYear).getSingle(x -> toDomain(x));
	}
}
