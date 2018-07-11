package nts.uk.ctx.exio.dom.exo.exoutsummaryservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.i18n.I18NText;
import nts.uk.ctx.exio.dom.exo.categoryitemdata.CtgItemData;
import nts.uk.ctx.exio.dom.exo.categoryitemdata.CtgItemDataRepository;
import nts.uk.ctx.exio.dom.exo.categoryitemdata.DataType;
import nts.uk.ctx.exio.dom.exo.commonalgorithm.AcquisitionExOutSetting;
import nts.uk.ctx.exio.dom.exo.outcnddetail.ConditionSymbol;
import nts.uk.ctx.exio.dom.exo.outcnddetail.OutCndDetailItem;
import nts.uk.ctx.exio.dom.exo.outcnddetail.OutCndDetailItemRepository;
import nts.uk.ctx.exio.dom.exo.outcnddetail.SearchCodeList;
import nts.uk.ctx.exio.dom.exo.outcnddetail.SearchCodeListRepository;
import nts.uk.ctx.exio.dom.exo.outputitem.StandardOutputItem;

@Stateless
public class ExOutSummarySettingService {

	@Inject
	private OutCndDetailItemRepository outCndDetailItemRepo;

	@Inject
	private SearchCodeListRepository searchCodeListRepo;

	@Inject
	private CtgItemDataRepository ctgItemDataRepo;
	
	@Inject
	private AcquisitionExOutSetting acquisitionExOutSetting;

	// アルゴリズム「外部出力サマリー設定」を実行する
	public ExOutSummarySetting getExOutSummarySetting(String conditionSetCd) {
		List<StandardOutputItem> stdOutItemList = acquisitionExOutSetting.getExOutItemList(conditionSetCd, null, "", true, false);
		List<CtgItemDataCustom> ctgItemDataCustomList = getExOutCond(conditionSetCd);
		
		return new ExOutSummarySetting(stdOutItemList, ctgItemDataCustomList);
	}

	private List<CtgItemDataCustom> getExOutCond(String code) {
		List<OutCndDetailItem> outCndDetailItemList = outCndDetailItemRepo.getOutCndDetailItemByCode(code);
		List<CtgItemDataCustom> ctgItemDataCustomList = new ArrayList<CtgItemDataCustom>();
		List<SearchCodeList> searchCodeList;
		Optional<CtgItemData> ctgItemData;
		StringBuilder cond = new StringBuilder();

		for (OutCndDetailItem outCndDetailItem : outCndDetailItemList) {
			searchCodeList = searchCodeListRepo.getSearchCodeByCateIdAndCateNo(
					outCndDetailItem.getCategoryId(), outCndDetailItem.getCategoryItemNo().v());
			ctgItemData = ctgItemDataRepo.getCtgItemDataById(outCndDetailItem.getCategoryId(),
					outCndDetailItem.getCategoryItemNo().v());
			cond.setLength(0);
			
			if(ctgItemData.isPresent()) {
				continue;
			}
			
			//TODO đổi thành swith case, với xem so sánh enum
			// 数値型 NumericType
			if(ctgItemData.get().getDataType() == DataType.NUMERIC) {
				if(outCndDetailItem.getConditionSymbol() == ConditionSymbol.BETWEEN) {
					cond.append(outCndDetailItem.getSearchNumStartVal().isPresent() ? outCndDetailItem.getSearchNumStartVal().get() : "");
					cond.append(I18NText.getText("#CMF002_235"));
					cond.append(outCndDetailItem.getSearchNumEndVal().isPresent() ? outCndDetailItem.getSearchNumEndVal().get() : "");
				} else {
					cond.append(outCndDetailItem.getSearchNum().isPresent() ? outCndDetailItem.getSearchNum().get().v() : "");
					cond.append(outCndDetailItem.getConditionSymbol());
				}
			}
			//文字型 CharacterType
			else if(ctgItemData.get().getDataType() == DataType.CHARACTER) {
				if(outCndDetailItem.getConditionSymbol() == ConditionSymbol.BETWEEN) {
					cond.append(outCndDetailItem.getSearchCharStartVal().isPresent() ? outCndDetailItem.getSearchCharStartVal().get() : "");
					cond.append(I18NText.getText("#CMF002_235"));
					cond.append(outCndDetailItem.getSearchCharEndVal().isPresent() ? outCndDetailItem.getSearchCharEndVal().get() : "");
				} else {
					cond.append(outCndDetailItem.getSearchChar().isPresent() ? outCndDetailItem.getSearchChar().get().v() : "");
					cond.append(outCndDetailItem.getConditionSymbol());
				}
			}
			//日付型 DateType
			else if(ctgItemData.get().getDataType() == DataType.DATE) {
				if(outCndDetailItem.getConditionSymbol() == ConditionSymbol.BETWEEN) {
					cond.append(outCndDetailItem.getSearchDateStart().isPresent() ? outCndDetailItem.getSearchDateStart().get() : "");
					cond.append(I18NText.getText("#CMF002_235"));
					cond.append(outCndDetailItem.getSearchDateEnd().isPresent() ? outCndDetailItem.getSearchDateEnd().get() : "");
				} else {
					cond.append(outCndDetailItem.getSearchDate().isPresent() ? outCndDetailItem.getSearchDate().get() : "");
					cond.append(outCndDetailItem.getConditionSymbol());
				}
			}
			//時間型 TimeType
			else if(ctgItemData.get().getDataType() == DataType.TIME) {
				if(outCndDetailItem.getConditionSymbol() == ConditionSymbol.BETWEEN) {
					cond.append(outCndDetailItem.getSearchTimeStartVal().isPresent() ? outCndDetailItem.getSearchTimeStartVal().get() : "");
					cond.append(I18NText.getText("#CMF002_235"));
					cond.append(outCndDetailItem.getSearchTimeEndVal().isPresent() ? outCndDetailItem.getSearchTimeEndVal().get() : "");
				} else {
					cond.append(outCndDetailItem.getSearchTime().isPresent() ? outCndDetailItem.getSearchTime().get() : "");
					cond.append(outCndDetailItem.getConditionSymbol());
				}
			}
			//時刻型 TimeClockType
			else if(ctgItemData.get().getDataType() == DataType.INS_TIME) {
				if(outCndDetailItem.getConditionSymbol() == ConditionSymbol.BETWEEN) {
					cond.append(outCndDetailItem.getSearchClockStartVal().isPresent() ? outCndDetailItem.getSearchClockStartVal().get() : "");
					cond.append(I18NText.getText("#CMF002_235"));
					cond.append(outCndDetailItem.getSearchClockEndVal().isPresent() ? outCndDetailItem.getSearchClockEndVal().get() : "");
				} else {
					cond.append(outCndDetailItem.getSearchClock().isPresent() ? outCndDetailItem.getSearchClock().get() : "");
					cond.append(outCndDetailItem.getConditionSymbol());
				}
			}
			
			if(ctgItemData.get().getSearchValueCd().isPresent() && "with".equals(ctgItemData.get().getSearchValueCd().get().toLowerCase())) {
				for (SearchCodeList searchCodeItem: searchCodeList) {
					cond.append(", ");
					cond.append(searchCodeItem.getSearchCode());
				}
			}
			
			ctgItemDataCustomList.add(new CtgItemDataCustom(ctgItemData.get().getItemName(), cond.toString()));
		}

		return ctgItemDataCustomList;
	}
}
