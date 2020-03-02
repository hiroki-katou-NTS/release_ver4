package nts.uk.ctx.pr.core.infra.repository.socialinsurance.welfarepensioninsurance;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.YearMonth;
import nts.uk.ctx.pr.core.dom.socialinsurance.welfarepensioninsurance.WelfarePensionInsuranceClassification;
import nts.uk.ctx.pr.core.dom.socialinsurance.welfarepensioninsurance.WelfarePensionInsuranceClassificationRepository;
import nts.uk.ctx.pr.core.dom.socialinsurance.welfarepensioninsurance.WelfarePensionInsuranceRateHistory;
import nts.uk.ctx.pr.core.infra.entity.socialinsurance.welfarepensioninsurance.QpbmtWelfarePensionInsuranceClassification;
import nts.uk.ctx.pr.core.infra.entity.socialinsurance.welfarepensioninsurance.QpbmtWelfarePensionInsuranceClassificationPk;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.history.YearMonthHistoryItem;
import nts.arc.time.calendar.period.YearMonthPeriod;

@Stateless
public class JpaWelfarePensionInsuranceClassificationRepository extends JpaRepository
		implements WelfarePensionInsuranceClassificationRepository {
	private static final String FIND_WELFARE_PENSION_CLS_BY_HISTORY_ID = "SELECT a FROM QpbmtWelfarePensionInsuranceClassification a WHERE a.welfarePenClsPk.historyId  =:historyId";
	private static final String FIND_HISTORY_BY_OFFICE_CODE = "SELECT a FROM QpbmtWelfarePensionInsuranceClassification a WHERE a.welfarePenClsPk.cid =:cid AND a.welfarePenClsPk.socialInsuranceOfficeCd =:officeCode ORDER BY a.startYearMonth DESC";
    private static final String DELETE_BY_HISTORY_IDS = "DELETE FROM QpbmtWelfarePensionInsuranceClassification a WHERE a.welfarePenClsPk.historyId IN :historyId";

	@Override
	public Optional<WelfarePensionInsuranceRateHistory> getWelfarePensionHistoryByOfficeCode(String officeCode) {
		return this.getWelfarePensionHistory(this.findWelfarePensionClassficationByOfficeCode(officeCode));
	}

	@Override
	public Optional<WelfarePensionInsuranceClassification> getWelfarePensionInsuranceClassificationById( String historyId) {
		return this.queryProxy().query(FIND_WELFARE_PENSION_CLS_BY_HISTORY_ID, QpbmtWelfarePensionInsuranceClassification.class).setParameter("historyId", historyId).getSingle().map(QpbmtWelfarePensionInsuranceClassification::toDomain);
	}

	@Override
	public void deleteByHistoryIds(List<String> historyIds) {
        if (!historyIds.isEmpty()) this.getEntityManager().createQuery(DELETE_BY_HISTORY_IDS, QpbmtWelfarePensionInsuranceClassification.class).setParameter("historyId", historyIds).executeUpdate();
	}
	
	@Override
	public void add(WelfarePensionInsuranceClassification domain, String officeCode, YearMonthHistoryItem yearMonth) {
		this.commandProxy().insert(QpbmtWelfarePensionInsuranceClassification.toEntity(domain, officeCode, yearMonth));
	}
	
	@Override
	public void update(WelfarePensionInsuranceClassification domain, String officeCode, YearMonthHistoryItem yearMonth) {
		this.commandProxy().update(QpbmtWelfarePensionInsuranceClassification.toEntity(domain, officeCode, yearMonth));
	}
	
	@Override
	public void remove(WelfarePensionInsuranceClassification domain, String officeCode, YearMonthHistoryItem yearMonth) {
		this.commandProxy().remove(QpbmtWelfarePensionInsuranceClassification.toEntity(domain, officeCode, yearMonth));
	}

	@Override
	public void updateHistory(String officeCode, YearMonthHistoryItem yearMonth) {
		Optional<QpbmtWelfarePensionInsuranceClassification> opt_entity = this.queryProxy().find(new QpbmtWelfarePensionInsuranceClassificationPk(AppContexts.user().companyId(), officeCode, yearMonth.identifier()), QpbmtWelfarePensionInsuranceClassification.class);
		if (!opt_entity.isPresent()) return;
		QpbmtWelfarePensionInsuranceClassification entity = opt_entity.get();
		entity.startYearMonth = yearMonth.start().v();
		entity.endYearMonth = yearMonth.end().v();
		this.commandProxy().update(entity);
	}

	private List<QpbmtWelfarePensionInsuranceClassification> findWelfarePensionClassficationByOfficeCode(String officeCode){
        return this.queryProxy().query(FIND_HISTORY_BY_OFFICE_CODE, QpbmtWelfarePensionInsuranceClassification.class).setParameter("cid", AppContexts.user().companyId()).setParameter("officeCode", officeCode).getList();
    }

    private Optional<WelfarePensionInsuranceRateHistory> getWelfarePensionHistory (List<QpbmtWelfarePensionInsuranceClassification> entities) {
	    if (entities.isEmpty()) return Optional.empty();
	    return Optional.of(new WelfarePensionInsuranceRateHistory(entities.get(0).welfarePenClsPk.cid, entities.get(0).welfarePenClsPk.socialInsuranceOfficeCd, entities.stream().map(item -> new YearMonthHistoryItem(item.welfarePenClsPk.historyId, new YearMonthPeriod(new YearMonth(item.startYearMonth), new YearMonth(item.endYearMonth)))).collect(Collectors.toList())));
    }


}
