package nts.uk.ctx.pr.core.infra.repository.retirement.payitem;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.retirement.payitem.IndicatorCategory;
import nts.uk.ctx.pr.core.dom.retirement.payitem.RetirementPayItem;
import nts.uk.ctx.pr.core.dom.retirement.payitem.RetirementPayItemCode;
import nts.uk.ctx.pr.core.dom.retirement.payitem.RetirementPayItemEnglishName;
import nts.uk.ctx.pr.core.dom.retirement.payitem.RetirementPayItemFullName;
import nts.uk.ctx.pr.core.dom.retirement.payitem.RetirementPayItemName;
import nts.uk.ctx.pr.core.dom.retirement.payitem.RetirementPayItemPrintName;
import nts.uk.ctx.pr.core.dom.retirement.payitem.RetirementPayItemRepository;
import nts.uk.ctx.pr.core.infra.entity.retirement.payitem.QremtRetirePayItem;
import nts.uk.ctx.pr.core.infra.entity.retirement.payitem.QremtRetirePayItemPK;
import nts.uk.shr.com.primitive.Memo;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
@Transactional
public class JpaRetirementPayItemRepository extends JpaRepository implements RetirementPayItemRepository{
	private final String SEL_1 = "SELECT a FROM QremtRetirePayItem a WHERE a.qremtRetirePayItemPK.companyCode = :companyCode";
	
	@Override
	public Optional<RetirementPayItem> findByKey(String companyCode, IndicatorCategory category, RetirementPayItemCode itemCode) {
		return this.queryProxy().find(new QremtRetirePayItemPK(companyCode, category.value, itemCode.v()), QremtRetirePayItem.class)
				.map(x -> convertToDomain(x));
	}
	
	@Override
	public List<RetirementPayItem> findByCompanyCode(String companyCode) {
		return this.queryProxy().query(SEL_1, QremtRetirePayItem.class).setParameter("companyCode", companyCode)
				.getList(x -> convertToDomain(x));
	}
	
	@Override
	public void update(RetirementPayItem payItem) {
		this.commandProxy().update(convertToEntity(payItem));
	}
	
	/**
	 * convert domain item to entity item
	 * @param payItem domain item
	 * @return entity item
	 */
	private QremtRetirePayItem convertToEntity(RetirementPayItem payItem) {
		return new QremtRetirePayItem(
				new QremtRetirePayItemPK(payItem.getCompanyCode(), payItem.getCategory().value, payItem.getItemCode().v()), 
				payItem.getItemName().v(), 
				payItem.getPrintName().v(), 
				payItem.getEnglishName().v(), 
				payItem.getFullName().v(), 
				payItem.getMemo().v());
	}
	
	/**
	 * convert entity item to domain item
	 * @param qremtRetirePayItem entity item
	 * @return domain item
	 */
	private RetirementPayItem convertToDomain(QremtRetirePayItem qremtRetirePayItem) {
		return new RetirementPayItem(
				qremtRetirePayItem.qremtRetirePayItemPK.companyCode,
				EnumAdaptor.valueOf(qremtRetirePayItem.qremtRetirePayItemPK.category, IndicatorCategory.class),
				new RetirementPayItemCode(qremtRetirePayItem.qremtRetirePayItemPK.itemCode),
				new RetirementPayItemName(qremtRetirePayItem.itemName),
				new RetirementPayItemPrintName(qremtRetirePayItem.printName),
				new RetirementPayItemEnglishName(qremtRetirePayItem.englishName),
				new RetirementPayItemFullName(qremtRetirePayItem.fullName),
				new Memo(qremtRetirePayItem.memo));
	}
}
