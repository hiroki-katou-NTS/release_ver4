package nts.uk.ctx.exio.app.find.exo.condset;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.exio.app.find.exo.category.ExOutCtgDto;
import nts.uk.ctx.exio.app.find.exo.item.StdOutItemDto;
import nts.uk.ctx.exio.app.find.exo.menu.RoleAuthorityDto;
import nts.uk.ctx.exio.dom.exo.category.ExOutCtg;
import nts.uk.ctx.exio.dom.exo.categoryitemdata.CtgItemDataRepository;
import nts.uk.ctx.exio.dom.exo.commonalgorithm.AcquisitionExternalOutputCategory;
import nts.uk.ctx.exio.dom.exo.commonalgorithm.AcquisitionSettingList;
import nts.uk.ctx.exio.dom.exo.condset.StandardAtr;
import nts.uk.ctx.exio.dom.exo.condset.StdOutputCondSetRepository;
import nts.uk.ctx.exio.dom.exo.condset.StdOutputCondSetService;
import nts.uk.ctx.exio.dom.exo.outputitem.StandardOutputItem;
import nts.uk.ctx.exio.dom.exo.outputitem.StandardOutputItemRepository;
import nts.uk.ctx.exio.dom.exo.outputitemorder.StandardOutputItemOrder;
import nts.uk.ctx.exio.dom.exo.outputitemorder.StandardOutputItemOrderRepository;
import nts.uk.shr.com.context.AppContexts;

@Stateless
/**
 * 出力条件設定（定型）
 */
public class StdOutputCondSetFinder {
	
	@Inject
	private AcquisitionExternalOutputCategory acquisitionExternalOutputCategory;

	@Inject
	private AcquisitionSettingList acquisitionSettingList;

	@Inject
	private StdOutputCondSetRepository finder;
	
	@Inject
	private StdOutputCondSetService stdOutputCondSetService;

	@Inject
	private StandardOutputItemRepository standardOutputItemRepository;
	
	@Inject
    private StandardOutputItemOrderRepository standardOutputItemOrderRepository;

	@Inject
	private CtgItemDataRepository ctgItemDataRepository;

	@Inject
	private StdOutputCondSetService mStdOutputCondSetService;

	public List<StdOutputCondSetDto> getAllStdOutputCondSet() {
		return finder.getAllStdOutputCondSet().stream().map(item -> StdOutputCondSetDto.fromDomain(item))
				.collect(Collectors.toList());
	}

	public List<CondSetDto> getCndSet(RoleAuthorityDto param) {
		String cId = AppContexts.user().companyId();
		String userId = AppContexts.user().userId();
		// アルゴリズム「外部出力取得設定一覧」を実行する
		List<CondSetDto> listCondSetDto = acquisitionSettingList.getAcquisitionSettingList(cId, userId, StandardAtr.STANDARD, Optional.empty())
				.stream().map(item -> CondSetDto.fromDomain(item)).collect(Collectors.toList());
		if(listCondSetDto.size() == 0) return Collections.emptyList();
		// アルゴリズム「外部出力設定一覧カテゴリ確認」を実行する
		List<Integer> listCategoryId = acquisitionExternalOutputCategory.getExternalOutputCategoryList(param.getEmpRole()).stream().map(item -> item.getCategoryId().v()).collect(Collectors.toList());
		List<CondSetDto> result = listCondSetDto.stream().filter(item -> listCategoryId.contains(Integer.valueOf(item.getCategoryId()))).collect(Collectors.toList());
		return result;
	}

	public StdOutItemDto getByKey(String cndSetCd, String outItemCode) {
		String cId = AppContexts.user().companyId();
		Optional<StandardOutputItem> stdOutItemOpt = standardOutputItemRepository.getStdOutItemById(cId, outItemCode,
				cndSetCd);
		if (!stdOutItemOpt.isPresent()) {
			return null;
		}
		return StdOutItemDto.fromDomain(stdOutItemOpt.get(), ctgItemDataRepository.getAllCtgItemData());
	}

	public List<StdOutputCondSetDto> getConditionSetting(String modeScreen, String cndSetCd,String roleId) {
		return mStdOutputCondSetService.getListStandardOutputItem(cndSetCd,roleId).stream()
				.map(item -> StdOutputCondSetDto.fromDomain(item)).collect(Collectors.toList());
	}

	public ExOutCtgDto getExOutCtgDto(int cateloryId) {
		Optional<ExOutCtg> exOutCtg = mStdOutputCondSetService.getExOutCtg(cateloryId);
		if (!exOutCtg.isPresent()) {
			return null;
		}
		return ExOutCtgDto.fromDomain(exOutCtg.get());
	}
	
	public List<StdOutItemDto> getOutItem(String condSetCd ,int standType) {
        String userId = AppContexts.user().userId();
        String cid = AppContexts.user().companyId();
        List<StandardOutputItemOrder> standardOutputItemOrder = standardOutputItemOrderRepository.getStandardOutputItemOrderByCidAndSetCd(cid, condSetCd);
        String outItemCd = new String ();
        StandardAtr standardAtr =  EnumAdaptor.valueOf(standType, StandardAtr.class);
        return stdOutputCondSetService.outputAcquisitionItemList(condSetCd, userId, outItemCd, standardAtr, false).stream()
                .map(item -> StdOutItemDto.fromDomain(standardOutputItemOrder, item))
                .collect(Collectors.toList());
    }

}
