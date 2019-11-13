package nts.uk.ctx.hr.notice.infra.repository.report;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.hr.notice.dom.report.RegisterPersonalReportItem;
import nts.uk.ctx.hr.notice.dom.report.RegisterPersonalReportItemRepository;
import nts.uk.ctx.hr.notice.infra.entity.report.JhnRptLtItem;
import nts.uk.ctx.hr.notice.infra.entity.report.JhnRptLtItemPk;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class JpaRegisterPersonalReportItemRepository extends JpaRepository implements RegisterPersonalReportItemRepository{
	
	private static final String SEL_ALL_ITEM_BY_CID_RPTLAYOUT_ID = "SELECT c FROM JhnRptLtItem c WHERE c.jhnRptLtItemPk.cid =:cid AND c.jhnRptLtItemPk.rptLayouId =:rptLayouId";
	
	@Override
	public List<RegisterPersonalReportItem> getAllItemBy(String cid, int rptLayoutId) {
		return this.queryProxy().query(SEL_ALL_ITEM_BY_CID_RPTLAYOUT_ID, JhnRptLtItem.class)
				.setParameter("cid", cid)
				.setParameter("rptLayouId", rptLayoutId)
				.getList(c -> {
					return createDomainFromEntity(c);
				});
	}

	@Override
	public void insertAll(List<RegisterPersonalReportItem> domain) {
		this.commandProxy().insertAll(domain.stream().map(c -> {
			return toEntity(c);
		}).collect(Collectors.toList()));
	}
	
	private RegisterPersonalReportItem createDomainFromEntity(JhnRptLtItem entity) {
		return  new RegisterPersonalReportItem(
				entity.jhnRptLtItemPk.cid, entity.jhnRptLtItemPk.rptLayouId,
				entity.rptLayouCd, entity.rptLayouName, 
				entity.layoutItemType, 
				entity.jhnRptLtItemPk.categoryCd, entity.categoryName, 
				entity.contractCd, entity.fixedAtr,
				entity.jhnRptLtItemPk.itemCd, entity.itemName,
				entity.displayOrder, Optional.ofNullable(entity.abolitionAtr == 1? true: false),
				"", entity.reflectId);
	}
	
	private JhnRptLtItem toEntity(RegisterPersonalReportItem domain) {
		
		JhnRptLtItemPk pk = new JhnRptLtItemPk (domain.getCompanyId(), domain.getPReportClsId(),
				domain.getCategoryCd(), domain.getItemCd());
		
		return new JhnRptLtItem(pk, domain.getPReportCode(), domain.getPReportName(), domain.getDisplayOrder(),
				domain.getItemType(), AppContexts.user().contractCode(), domain.getCategoryName(), domain.getItemName(),
				domain.isFixedAtr(),
				domain.getIsAbolition().isPresent() == true ? (domain.getIsAbolition().get() == true ? 1 : 0) : 0,
				domain.getReflectionId());
	}

	@Override
	public void updateAll(List<RegisterPersonalReportItem> domain) {
		this.commandProxy().updateAll(domain.stream().map(c -> {
			return toEntity(c);
		}).collect(Collectors.toList()));
		
	}

}
