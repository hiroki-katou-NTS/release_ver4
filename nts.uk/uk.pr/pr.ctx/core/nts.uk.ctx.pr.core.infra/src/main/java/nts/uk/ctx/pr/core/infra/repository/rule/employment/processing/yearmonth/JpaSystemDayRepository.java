package nts.uk.ctx.pr.core.infra.repository.rule.employment.processing.yearmonth;

import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.SystemDayRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.processing.yearmonth.systemday.SystemDay;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.processing.yearmonth.QpdmtSystemDay;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.processing.yearmonth.QpdmtSystemDayPK;

@Stateless
public class JpaSystemDayRepository extends JpaRepository implements SystemDayRepository {

	private final String SELECT_ALL = "SELECT c FROM QpdmtSystemDay c";
	private final String SELECT_ALL_BY_CCD = SELECT_ALL + " WHERE c.qpdmtSystemDayPk.ccd = :companyCode";
	private final String SELECT_ALL_BY_CCD_AND_PROCESSING_NO = SELECT_ALL_BY_CCD
			+ " AND c.qpdmtSystemDayPk.processingNo = :processingNo";

	@Override
	public SystemDay select1(String companyCode, int processingNo) {
		List<SystemDay> systemDays = this.queryProxy().query(SELECT_ALL_BY_CCD_AND_PROCESSING_NO, QpdmtSystemDay.class)
				.setParameter("companyCode", companyCode).setParameter("processingNo", processingNo)
				.getList(c -> toDomain(c));
		
		if (systemDays.isEmpty()) {
			return null;
		} else {
			return systemDays.get(0);
		}
	}

	@Override
	public void insert(SystemDay domain) {
		this.commandProxy().insert(toEntity(domain));
	}

	@Override
	public void update1(SystemDay domain) {
		this.commandProxy().update(toEntity(domain));

	}

	@Override
	public void delete(SystemDay domain) {
		this.commandProxy().remove(toEntity(domain));
	}

	private SystemDay toDomain(QpdmtSystemDay entity) {
		return SystemDay.createSimpleFromJavaType(entity.qpdmtSystemDayPk.ccd, entity.qpdmtSystemDayPk.processingNo,
				entity.socialInsuLevyMonAtr, entity.pickupStdMonAtr, entity.pickupStdDay, entity.payStdDay,
				entity.accountDueMonAtr, entity.accountDueDay, entity.payslipPrintMonthAtr);
	}

	private QpdmtSystemDay toEntity(SystemDay domain) {
		QpdmtSystemDayPK qpdmtSystemDayPk = new QpdmtSystemDayPK(domain.getCompanyCode().v(),
				domain.getProcessingNo().v());

		return new QpdmtSystemDay(qpdmtSystemDayPk, domain.getPayStdDay().v(), domain.getPickupStdMonAtr().value,
				domain.getPickupStdDay().v(), domain.getSocialInsLevyMonAtr().value, domain.getAccountDueMonAtr().value,
				domain.getAccountDueDay().v(), domain.getPayslipPrintMonthAtr().value);
	}
}
