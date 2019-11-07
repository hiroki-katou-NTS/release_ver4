package nts.uk.ctx.bs.employee.infra.repository.groupcommonmaster;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.bs.employee.dom.employee.employeelicense.ContractCode;
import nts.uk.ctx.bs.employee.dom.groupcommonmaster.CommonMasterCode;
import nts.uk.ctx.bs.employee.dom.groupcommonmaster.CommonMasterItem;
import nts.uk.ctx.bs.employee.dom.groupcommonmaster.CommonMasterItemCode;
import nts.uk.ctx.bs.employee.dom.groupcommonmaster.CommonMasterItemName;
import nts.uk.ctx.bs.employee.dom.groupcommonmaster.CommonMasterName;
import nts.uk.ctx.bs.employee.dom.groupcommonmaster.GroupCommonMaster;
import nts.uk.ctx.bs.employee.dom.groupcommonmaster.GroupCommonMasterRepository;
import nts.uk.ctx.bs.employee.dom.groupcommonmaster.NotUseCompany;
import nts.uk.ctx.bs.employee.infra.entity.groupcommonmaster.BsymtGpMasterCategory;
import nts.uk.ctx.bs.employee.infra.entity.groupcommonmaster.BsymtGpMasterItem;
import nts.uk.ctx.bs.employee.infra.entity.groupcommonmaster.BsymtGpMasterNotUse;
import nts.uk.ctx.bs.employee.infra.entity.groupcommonmaster.BsymtGpMasterNotUsePK;

@Stateless
public class JpaGroupCommonMasterRepository extends JpaRepository implements GroupCommonMasterRepository {

	private static final String GET_BY_CONTRACT_CODE = "SELECT mc FROM BsymtGpMasterCategory mc WHERE mc.contractCode = :contractCode";

	private static final String GET_ITEM_BY_CONTRACT_CODE_AND_LIST_MASTER_ID = "SELECT mi BsymtGpMasterItem mi WHERE mi.contractCode = :contractCode AND mi.commonMasterId IN :commonMasterIds";

	private static final String GET_NOT_USE_BY_LIST_ITEM_ID = "SELECT nu BsymtGpMasterNotUse nu WHERE nu.pk.commonMasterItemId IN :commonMasterItemIds";

	private static final String GET_BY_CONTRACT_CODE_AND_ID = "SELECT mc FROM BsymtGpMasterCategory mc WHERE mc.contractCode = :contractCode AND mc.commonMasterId = :commonMasterId";

	@Override
	public void addListGroupCommonMaster(List<GroupCommonMaster> domains) {

		// add categories
		List<BsymtGpMasterCategory> Categories = domains.stream().map(x -> mapCategory(x)).collect(Collectors.toList());

		this.commandProxy().insertAll(Categories);

		// add items
		List<BsymtGpMasterItem> items = domains.stream().filter(x -> !CollectionUtil.isEmpty(x.getCommonMasterItems()))
				.map(x -> mapItemList(x)).collect(Collectors.toList()).stream().flatMap(List::stream)
				.collect(Collectors.toList());

		if (!CollectionUtil.isEmpty(items)) {
			return;
		}

		this.commandProxy().insertAll(items);

		// add not use
		List<BsymtGpMasterNotUse> notUses = domains.stream()
				.filter(domain -> !CollectionUtil.isEmpty(domain.getCommonMasterItems()))
				.map(domain -> mapNotUseList(domain.getCommonMasterItems())).collect(Collectors.toList()).stream()
				.flatMap(List::stream).collect(Collectors.toList());

		if (!CollectionUtil.isEmpty(notUses)) {
			return;
		}

		this.commandProxy().insertAll(notUses);

	}

	private BsymtGpMasterCategory mapCategory(GroupCommonMaster domain) {
		return new BsymtGpMasterCategory(domain.getContractCode().v(), domain.getCommonMasterId(),
				domain.getCommonMasterCode().v(), domain.getCommonMasterName().v(), domain.getCommonMasterMemo());
	}

	private List<BsymtGpMasterItem> mapItemList(GroupCommonMaster domain) {

		return domain.getCommonMasterItems().stream()
				.map(x -> mapItem(domain.getContractCode().v(), domain.getCommonMasterId(), x))
				.collect(Collectors.toList());

	}

	private BsymtGpMasterItem mapItem(String contractCode, String commonMasterId, CommonMasterItem item) {

		return new BsymtGpMasterItem(contractCode, commonMasterId, item.getCommonMasterItemId(),
				item.getCommonMasterItemCode().v(), item.getCommonMasterItemName().v(), item.getDisplayNumber(),
				item.getUsageStartDate(), item.getUsageEndDate());

	}

	private List<BsymtGpMasterNotUse> mapNotUseList(List<CommonMasterItem> items) {

		return items.stream()
				.map(item -> item.getNotUseCompanyList().stream()
						.map(notuse -> mapNotUse(item.getCommonMasterItemId(), notuse)).collect(Collectors.toList()))
				.flatMap(List::stream).collect(Collectors.toList());

	}

	private BsymtGpMasterNotUse mapNotUse(String commonMasterItemId, NotUseCompany notuse) {
		return new BsymtGpMasterNotUse(new BsymtGpMasterNotUsePK(commonMasterItemId, notuse.getCompanyId()));
	}

	@Override
	public List<GroupCommonMaster> getByContractCode(String contractCode) {

		List<GroupCommonMaster> commonMasters = this.queryProxy()
				.query(GET_BY_CONTRACT_CODE, BsymtGpMasterCategory.class).setParameter("contractCode", contractCode)
				.getList().stream().map(x -> toCategoryDomain(x)).collect(Collectors.toList());

		setMasterItems(contractCode, commonMasters);

		setNotUses(commonMasters);

		return commonMasters;

	}

	private void setMasterItems(String contractCode, List<GroupCommonMaster> commonMasters) {

		List<String> commonMasterIds = commonMasters.stream().map(x -> x.getCommonMasterId())
				.collect(Collectors.toList());

		List<BsymtGpMasterItem> commonMasterItems = this.queryProxy()
				.query(GET_ITEM_BY_CONTRACT_CODE_AND_LIST_MASTER_ID, BsymtGpMasterItem.class)
				.setParameter("contractCode", contractCode).setParameter("commonMasterIds", commonMasterIds).getList();

		commonMasters.forEach(commonMaster -> {
			List<CommonMasterItem> items = commonMasterItems.stream()
					.filter(item -> item.getCommonMasterId().equals(commonMaster.getCommonMasterId()))
					.map(item -> toItemDomain(item)).collect(Collectors.toList());
			commonMaster.setCommonMasterItems(items);
		});

	}

	private void setNotUses(List<GroupCommonMaster> commonMasters) {
		List<String> commonMasterItemIds = commonMasters.stream()
				.filter(domain -> !CollectionUtil.isEmpty(domain.getCommonMasterItems()))
				.map(x -> x.getCommonMasterItems().stream().map(item -> item.getCommonMasterItemId())
						.collect(Collectors.toList()))
				.flatMap(List::stream).collect(Collectors.toList());

		List<BsymtGpMasterNotUse> notUseCompanyList = this.queryProxy()
				.query(GET_NOT_USE_BY_LIST_ITEM_ID, BsymtGpMasterNotUse.class)
				.setParameter("commonMasterItemIds", commonMasterItemIds).getList();

		List<CommonMasterItem> masterItems = commonMasters.stream().map(master -> master.getCommonMasterItems())
				.flatMap(List::stream).collect(Collectors.toList());

		masterItems.forEach(item -> {

			List<NotUseCompany> notUses = notUseCompanyList.stream()
					.filter(notUse -> notUse.getPk().getCommonMasterItemId().equals(item.getCommonMasterItemId()))
					.map(notUse -> new NotUseCompany(notUse.getPk().getCompanyId())).collect(Collectors.toList());

			item.setNotUseCompanyList(notUses);

		});
	}

	private GroupCommonMaster toCategoryDomain(BsymtGpMasterCategory entity) {

		GroupCommonMaster domain = new GroupCommonMaster(new ContractCode(entity.getContractCode()),
				entity.getCommonMasterId(), new CommonMasterCode(entity.getCommonMasterCode()),
				new CommonMasterName(entity.getCommonMasterName()), entity.getCommonMasterMemo());

		return domain;
	}

	private CommonMasterItem toItemDomain(BsymtGpMasterItem entity) {

		CommonMasterItem domain = new CommonMasterItem(entity.getCommonMasterItemId(),
				new CommonMasterItemCode(entity.getCommonMasterItemCode()),
				new CommonMasterItemName(entity.getCommonMasterItemName()), entity.getDisplayNumber(),
				entity.getUsageStartDate(), entity.getUsageEndDate());

		return domain;
	}

	@Override
	public Optional<GroupCommonMaster> getByContractCodeAndId(String contractCode, String commonMasterId) {

		Optional<GroupCommonMaster> commonMasterOpt = this.queryProxy()
				.query(GET_BY_CONTRACT_CODE_AND_ID, BsymtGpMasterCategory.class)
				.setParameter("contractCode", contractCode).setParameter("commonMasterId", commonMasterId).getSingle()
				.map(x -> toCategoryDomain(x));

		GroupCommonMaster commonMaster = null;

		if (!commonMasterOpt.isPresent()) {

			commonMaster = commonMasterOpt.get();

			setMasterItems(contractCode, Arrays.asList(commonMaster));

			setNotUses(Arrays.asList(commonMaster));
		}

		return Optional.of(commonMaster);
	}

	@Override
	public void removeGroupCommonMasterUsage(String contractCode, String commonMasterId, String companyId,
			List<String> masterItemIds) {

		this.commandProxy().removeAll(genNotuseEntity(masterItemIds, companyId));

	}

	@Override
	public void addGroupCommonMasterUsage(String contractCode, String commonMasterId, String companyId,
			List<String> masterItemIds) {

		this.commandProxy().insertAll(genNotuseEntity(masterItemIds, companyId));

	}

	private List<BsymtGpMasterNotUse> genNotuseEntity(List<String> masterItemIds, String companyId) {
		return masterItemIds.stream()
				.map(masterItemId -> new BsymtGpMasterNotUse(new BsymtGpMasterNotUsePK(masterItemId, companyId)))
				.collect(Collectors.toList());
	}

}
