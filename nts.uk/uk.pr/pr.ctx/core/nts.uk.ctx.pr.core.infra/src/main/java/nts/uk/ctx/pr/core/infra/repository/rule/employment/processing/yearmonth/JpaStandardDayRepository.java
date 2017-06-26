package nts.uk.ctx.pr.core.infra.repository.rule.employment.processing.yearmonth;

import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.StandardDayRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.standardday.StandardDay;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.processing.yearmonth.QpdmtStandardDay;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.processing.yearmonth.QpdmtStandardDayPK;

@Stateless
public class JpaStandardDayRepository extends JpaRepository implements StandardDayRepository {
	private final String SELECT_ALL = "SELECT c FROM QpdmtStandardDay c";
	private final String SELECT_ALL_BY_CCD = SELECT_ALL + " WHERE c.qpdmtStandardDayPk.ccd = :companyCode";
	private final String SELECT_ALL_BY_CCD_AND_PROCESSING_NO = SELECT_ALL_BY_CCD
			+ " AND c.qpdmtStandardDayPk.processingNo = :processingNo";

	@Override
	public StandardDay select1(String companyCode, int processingNo) {
		List<StandardDay> standardDays = this.queryProxy()
				.query(SELECT_ALL_BY_CCD_AND_PROCESSING_NO, QpdmtStandardDay.class)
				.setParameter("companyCode", companyCode).setParameter("processingNo", processingNo)
				.getList(c -> toDomain(c));
		
		if (standardDays.isEmpty()) {
			return null;
		} else {
			return standardDays.get(0);
		}
	}

	@Override
	public void insert1(StandardDay domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	@Override
	public void update1(StandardDay domain) {
		this.commandProxy().update(toEntity(domain));
	}

	private StandardDay toDomain(QpdmtStandardDay entity) {
		return StandardDay.createSimpleFromJavaType(entity.qpdmtStandardDayPk.ccd,
				entity.qpdmtStandardDayPk.processingNo, entity.socialInsStdYearAtr, entity.socialInsStdMon,
				entity.socialInsStdDay, entity.incometaxStdYearAtr, entity.incometaxStdMon, entity.incometaxStdDay,
				entity.empInsStdMon, entity.empInsStdDay);
	}

	private QpdmtStandardDay toEntity(StandardDay domain) {
		QpdmtStandardDayPK qpdmtStandardDayPk = new QpdmtStandardDayPK(domain.getCompanyCode().v(),
				domain.getProcessingNo().v());

		return new QpdmtStandardDay(qpdmtStandardDayPk, domain.getSocialInsStdYearAtr().value,
				domain.getSocialInsStdMon().v(), domain.getSocialInsStdDay().v(), domain.getEmpInsStdMon().v(),
				domain.getEmpInsStdDay().v(), domain.getIncometaxStdYearAtr().value, domain.getIncometaxStdMon().v(),
				domain.getIncometaxStdDay().v());
	}
}
