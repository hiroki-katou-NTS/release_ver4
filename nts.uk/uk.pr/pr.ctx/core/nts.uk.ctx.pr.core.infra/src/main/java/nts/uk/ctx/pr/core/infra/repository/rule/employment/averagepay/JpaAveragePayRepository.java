package nts.uk.ctx.pr.core.infra.repository.rule.employment.averagepay;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.averagepay.AttendDayGettingSet;
import nts.uk.ctx.pr.core.dom.rule.employment.averagepay.AveragePay;
import nts.uk.ctx.pr.core.dom.rule.employment.averagepay.AveragePayRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.averagepay.ExceptionPayRate;
import nts.uk.ctx.pr.core.dom.rule.employment.averagepay.RoundDigitSet;
import nts.uk.ctx.pr.core.dom.rule.employment.averagepay.RoundTimingSet;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.averagepay.QapmtAvePay;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
@Transactional
public class JpaAveragePayRepository extends JpaRepository implements AveragePayRepository {
	
	private final String SEL_1 = "SELECT a FROM QapmtAvePay a WHERE a.companyCode = :companyCode";
	
	@Override
	public void register(AveragePay averagePay) {
		this.commandProxy().insert(convertToEntity(averagePay));
	}
	
	@Override
	public Optional<AveragePay> findByCompanyCode(String companyCode) {
		return this.queryProxy().query(SEL_1, QapmtAvePay.class).setParameter("companyCode", companyCode)
				.getSingle(x -> convertToDomain(x));
	}
	
	@Override
	public void update(AveragePay averagePay) {
		this.commandProxy().update(convertToEntity(averagePay));
	}
	/**
	 * convert domain item to entity item
	 * 
	 * @param averagePay 
	 * 				domain item
	 * @return entity item
	 */
	private QapmtAvePay convertToEntity(AveragePay averagePay) {
		return new QapmtAvePay(
				averagePay.getCompanyCode(), 
				averagePay.getRoundTimingSet().value,
				averagePay.getAttendDayGettingSet().value, 
				averagePay.getRoundDigitSet().value,  
				averagePay.getExceptionPayRate().v());
	}
	
	/**
	 * convert entity to domain item
	 * 
	 * @param qapmtAvePay 
	 * 				entity item
	 * @return domain item
	 */
	private AveragePay convertToDomain(QapmtAvePay qapmtAvePay) {
		return new AveragePay(
				qapmtAvePay.companyCode,
				EnumAdaptor.valueOf(qapmtAvePay.roundTimingSet, RoundTimingSet.class),
				EnumAdaptor.valueOf(qapmtAvePay.attendDayGettingSet, AttendDayGettingSet.class),
				EnumAdaptor.valueOf(qapmtAvePay.roundDigitSet, RoundDigitSet.class),
				new ExceptionPayRate(qapmtAvePay.exceptionPayRate));
	}
	
}
