package nts.uk.ctx.pr.core.infra.repository.rule.employment.processing.yearmonth;

import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.PaydayProcessingRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.paydayprocessing.PaydayProcessing;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.processing.yearmonth.QpdmtPaydayProcessing;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.processing.yearmonth.QpdmtPaydayProcessingPK;

@Stateless
public class JpaPaydayProcessingRepository extends JpaRepository implements PaydayProcessingRepository {

	private final String SELECT_ALL = "SELECT c FROM QpdmtPaydayProcessing c";
	private final String SELECT_ALL_BY_CCD = SELECT_ALL + " WHERE c.qpdmtPaydayProcessingPK.ccd = :companyCode";

	private final String SELECT_ONE_BY_KEY = SELECT_ALL_BY_CCD
			+ " AND c.qpdmtPaydayProcessingPK.processingNo = :processingNo";

	private final String SELECT_ALL_BY_CCD_AND_DISP_ATR_1 = SELECT_ALL
			+ " WHERE c.qpdmtPaydayProcessingPK.ccd = :companyCode AND c.dispSet = 1";

	private final String SELECT_ALL_BY_DISP_SET = SELECT_ALL_BY_CCD + " AND c.dispSet = 1";

	@Override
	public PaydayProcessing selectOne(String companyCode, int processingNo) {
		return this.queryProxy().query(SELECT_ONE_BY_KEY, QpdmtPaydayProcessing.class)
				.setParameter("companyCode", companyCode).setParameter("processingNo", processingNo)
				.getList(c -> toDomain(c)).get(0);
	}

	@Override
	public List<PaydayProcessing> select1(String companyCode) {
		return this.queryProxy().query(SELECT_ALL_BY_DISP_SET, QpdmtPaydayProcessing.class)
				.setParameter("companyCode", companyCode).getList(c -> toDomain(c));
	}

	@Override
	public List<PaydayProcessing> select3(String companyCode) {
		return this.queryProxy().query(SELECT_ALL_BY_CCD, QpdmtPaydayProcessing.class)
				.setParameter("companyCode", companyCode).getList(c -> toDomain(c));
	}

	@Override
	public List<PaydayProcessing> select4(String companyCode) {
		return this.queryProxy().query(SELECT_ALL_BY_CCD_AND_DISP_ATR_1, QpdmtPaydayProcessing.class)
				.setParameter("companyCode", companyCode).getList(c -> toDomain(c));
	}

	@Override
	public void insert1(PaydayProcessing domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	@Override
	public void update1(PaydayProcessing domain) {
		this.commandProxy().update(toEntity(domain));
	}

	@Override
	public void update2(PaydayProcessing domain) {
		this.commandProxy().update(toEntity(domain));
	}

	private PaydayProcessing toDomain(QpdmtPaydayProcessing entity) {
		return PaydayProcessing.createSimpleFromJavaType(entity.qpdmtPaydayProcessingPK.ccd,
				entity.qpdmtPaydayProcessingPK.processingNo, entity.processingName, entity.dispSet,
				entity.currentProcessingYm, entity.bonusAtr, entity.bCurrentProcessingYm);
	}

	private QpdmtPaydayProcessing toEntity(PaydayProcessing domain) {
		QpdmtPaydayProcessingPK qpdmtPaydayProcessingPK = new QpdmtPaydayProcessingPK(domain.getCompanyCode().v(),
				domain.getProcessingNo().v());

		return new QpdmtPaydayProcessing(qpdmtPaydayProcessingPK, domain.getProcessingName().v(),
				domain.getDispSet().value, domain.getCurrentProcessingYm().v(), domain.getBonusAtr().value,
				domain.getBCurrentProcessingYm().v());
	}
}
