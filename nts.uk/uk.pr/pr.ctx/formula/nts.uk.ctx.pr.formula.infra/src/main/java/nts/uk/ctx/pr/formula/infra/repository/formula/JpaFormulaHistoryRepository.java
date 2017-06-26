package nts.uk.ctx.pr.formula.infra.repository.formula;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.formula.dom.formula.FormulaHistory;
import nts.uk.ctx.pr.formula.dom.primitive.FormulaCode;
import nts.uk.ctx.pr.formula.dom.repository.FormulaHistoryRepository;
import nts.uk.ctx.pr.formula.infra.entity.formula.QcfmtFormulaHistory;
import nts.uk.ctx.pr.formula.infra.entity.formula.QcfmtFormulaHistoryPK;

@Stateless
public class JpaFormulaHistoryRepository extends JpaRepository implements FormulaHistoryRepository {

	private static final String FIND_ALL_BY_COMPANYCODE;

	private static final String FIND_ALL_DIFFERENT_FORMULACODE;

	private static final String IS_EXISTED_HISTORY;

	private static final String LAST_HISTORY;

	private static final String DEL_HISTORY_BY_KEY;

	private static final String FIND_NEWEST_HISTORY;

	private static final String FIND_PREVIOUS_HISTORY;

	private static final String UPDATE_HISTORY_BY_KEY;

	private static final String IS_EXISTED_HISTORY_FOR_UPDATE;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) ");
		builderString.append("FROM QcfmtFormulaHistory a ");
		builderString.append("WHERE a.qcfmtFormulaHistoryPK.companyCode = :companyCode ");
		IS_EXISTED_HISTORY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) ");
		builderString.append("FROM QcfmtFormulaHistory a ");
		builderString.append("WHERE a.qcfmtFormulaHistoryPK.companyCode = :companyCode ");
		builderString.append("AND a.qcfmtFormulaHistoryPK.formulaCode = :formulaCode ");
		builderString.append("AND a.qcfmtFormulaHistoryPK.historyId = :historyId ");
		IS_EXISTED_HISTORY_FOR_UPDATE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM QcfmtFormulaHistory a ");
		builderString.append("WHERE a.qcfmtFormulaHistoryPK.companyCode = :companyCode ");
		builderString.append(
				"ORDER BY a.qcfmtFormulaHistoryPK.companyCode, a.qcfmtFormulaHistoryPK.formulaCode, a.qcfmtFormulaHistoryPK.historyId ");
		FIND_ALL_BY_COMPANYCODE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM QcfmtFormulaHistory a ");
		builderString.append("WHERE a.qcfmtFormulaHistoryPK.companyCode = :companyCode ");
		builderString.append("AND a.startDate <= :baseDate ");
		builderString.append("AND a.endDate >= :baseDate ");
		builderString.append("AND a.qcfmtFormulaHistoryPK.formulaCode <> :formulaCode ");
		FIND_ALL_DIFFERENT_FORMULACODE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM QcfmtFormulaHistory a ");
		builderString.append("WHERE a.qcfmtFormulaHistoryPK.companyCode = :companyCode ");
		builderString.append("AND a.qcfmtFormulaHistoryPK.formulaCode = :formulaCode ");
		builderString.append("ORDER BY a.startDate DESC ");
		LAST_HISTORY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM QcfmtFormulaHistory a ");
		builderString.append("WHERE a.qcfmtFormulaHistoryPK.companyCode = :companyCode ");
		builderString.append("AND a.qcfmtFormulaHistoryPK.formulaCode = :formulaCode ");
		builderString.append("AND a.qcfmtFormulaHistoryPK.historyId = :historyId ");
		DEL_HISTORY_BY_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) ");
		builderString.append("FROM QcfmtFormulaHistory a ");
		builderString.append("WHERE a.qcfmtFormulaHistoryPK.companyCode = :companyCode ");
		builderString.append("AND a.qcfmtFormulaHistoryPK.formulaCode = :formulaCode ");
		builderString.append("AND a.startDate < :startDate ");
		FIND_NEWEST_HISTORY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM QcfmtFormulaHistory a ");
		builderString.append("WHERE a.qcfmtFormulaHistoryPK.companyCode = :companyCode ");
		builderString.append("AND a.qcfmtFormulaHistoryPK.formulaCode = :formulaCode ");
		builderString.append("AND a.startDate < :startDate ");
		builderString.append("ORDER BY a.startDate DESC");
		FIND_PREVIOUS_HISTORY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("UPDATE QcfmtFormulaHistory a ");
		builderString.append("SET a.startDate = :startDate ");
		builderString.append("WHERE a.qcfmtFormulaHistoryPK.companyCode = :companyCode ");
		builderString.append("AND a.qcfmtFormulaHistoryPK.formulaCode = :formulaCode ");
		builderString.append("AND a.qcfmtFormulaHistoryPK.historyId = :historyId ");
		UPDATE_HISTORY_BY_KEY = builderString.toString();
	}

	@Override
	public List<FormulaHistory> findAll(String companyCode) {

		return this.queryProxy().query(FIND_ALL_BY_COMPANYCODE, QcfmtFormulaHistory.class)
				.setParameter("companyCode", companyCode).getList(f -> toDomain(f));
	}

	@Override
	public Optional<FormulaHistory> findByPriKey(String companyCode, FormulaCode formulaCode, String historyId) {
		return this.queryProxy()
				.find(new QcfmtFormulaHistoryPK(companyCode, formulaCode.v(), historyId), QcfmtFormulaHistory.class)
				.map(f -> toDomain(f));
	}

	@Override
	public List<FormulaHistory> findDataDifFormulaCode(String companyCode, FormulaCode formulaCode,
			YearMonth baseDate) {
		return this.queryProxy().query(FIND_ALL_DIFFERENT_FORMULACODE, QcfmtFormulaHistory.class)
				.setParameter("companyCode", companyCode).setParameter("formulaCode", formulaCode.v())
				.setParameter("baseDate", baseDate.v()).getList(f -> toDomain(f));
	}

	@Override
	public void add(FormulaHistory formulaHistory) {
		this.commandProxy().insert(toEntity(formulaHistory));
	}

	@Override
	public void remove(String companyCode, FormulaCode formulaCode, String historyId) {
		this.getEntityManager().createQuery(DEL_HISTORY_BY_KEY).setParameter("companyCode", companyCode)
				.setParameter("formulaCode", formulaCode.v()).setParameter("historyId", historyId).executeUpdate();
	}

	@Override
	public void update(FormulaHistory formulaHistory) {
		this.commandProxy().update(toEntity(formulaHistory));
	}

	@Override
	public void updateByKey(String companyCode, FormulaCode formulaCode, String historyId, YearMonth startDate) {
		this.getEntityManager().createQuery(UPDATE_HISTORY_BY_KEY).setParameter("companyCode", companyCode)
				.setParameter("formulaCode", formulaCode.v()).setParameter("historyId", historyId)
				.setParameter("startDate", startDate.v()).executeUpdate();
	}

	private static FormulaHistory toDomain(QcfmtFormulaHistory qcfmtFormulaHistory) {

		FormulaHistory formulaHistory = FormulaHistory.createFromJavaType(
				qcfmtFormulaHistory.qcfmtFormulaHistoryPK.companyCode,
				qcfmtFormulaHistory.qcfmtFormulaHistoryPK.formulaCode,
				qcfmtFormulaHistory.qcfmtFormulaHistoryPK.historyId, qcfmtFormulaHistory.startDate.intValue(),
				qcfmtFormulaHistory.endDate.intValue());

		return formulaHistory;
	}

	private QcfmtFormulaHistory toEntity(FormulaHistory formulaHistory) {
		val entity = new QcfmtFormulaHistory();

		entity.qcfmtFormulaHistoryPK = new QcfmtFormulaHistoryPK();
		entity.qcfmtFormulaHistoryPK.companyCode = formulaHistory.getCompanyCode();
		entity.qcfmtFormulaHistoryPK.formulaCode = formulaHistory.getFormulaCode().v();
		entity.qcfmtFormulaHistoryPK.historyId = formulaHistory.getHistoryId();
		entity.startDate = new BigDecimal(formulaHistory.getStartDate().v());
		entity.endDate = new BigDecimal(formulaHistory.getEndDate().v());

		return entity;
	}

	@Override
	public boolean isExistedHistory(String companyCode) {
		return this.queryProxy().query(IS_EXISTED_HISTORY, long.class).setParameter("companyCode", companyCode)
				.getSingle().get() > 0;
	}

	@Override
	public boolean isExistedHistoryForUpdate(String companyCode, FormulaCode formulaCode, String historyId) {
		return this.queryProxy().query(IS_EXISTED_HISTORY_FOR_UPDATE, long.class)
				.setParameter("companyCode", companyCode).setParameter("formulaCode", formulaCode)
				.setParameter("historyId", historyId).getSingle().get() > 0;
	}

	@Override
	public Optional<FormulaHistory> findLastHistory(String companyCode, FormulaCode formulaCode) {
		List<QcfmtFormulaHistory> result = this.getEntityManager().createQuery(LAST_HISTORY, QcfmtFormulaHistory.class)
				.setParameter("companyCode", companyCode).setParameter("formulaCode", formulaCode.v()).setMaxResults(1)
				.getResultList();
		return !result.isEmpty() ? Optional.of(toDomain(result.get(0))) : Optional.empty();
	}

	@Override
	public boolean isNewestHistory(String companyCode, FormulaCode formulaCode, YearMonth startDate) {
		return this.queryProxy().query(FIND_NEWEST_HISTORY, long.class).setParameter("companyCode", companyCode)
				.setParameter("formulaCode", formulaCode.v()).setParameter("startDate", startDate.v()).getSingle()
				.get() > 0;
	}

	@Override
	public Optional<FormulaHistory> findPreviousHistory(String companyCode, FormulaCode formulaCode,
			YearMonth startDate) {
		List<QcfmtFormulaHistory> result = this.getEntityManager()
				.createQuery(FIND_PREVIOUS_HISTORY, QcfmtFormulaHistory.class).setParameter("companyCode", companyCode)
				.setParameter("formulaCode", formulaCode.v()).setParameter("startDate", startDate.v()).setMaxResults(1)
				.getResultList();
		return !result.isEmpty() ? Optional.of(toDomain(result.get(0))) : Optional.empty();

	}

}
