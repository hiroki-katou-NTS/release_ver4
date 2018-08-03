package nts.uk.ctx.exio.dom.exo.condset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.uk.ctx.exio.dom.exo.category.ExOutCtg;
import nts.uk.ctx.exio.dom.exo.category.ExOutCtgRepository;
import nts.uk.ctx.exio.dom.exo.commonalgorithm.AcquisitionExOutSetting;
import nts.uk.ctx.exio.dom.exo.commonalgorithm.RegisterMode;
import nts.uk.ctx.exio.dom.exo.commonalgorithm.RegistrationCondDetails;
import nts.uk.ctx.exio.dom.exo.dataformat.dataformatsetting.AwDataFormatSetting;
import nts.uk.ctx.exio.dom.exo.dataformat.dataformatsetting.CharacterDataFmSetting;
import nts.uk.ctx.exio.dom.exo.dataformat.dataformatsetting.DateFormatSetting;
import nts.uk.ctx.exio.dom.exo.dataformat.dataformatsetting.InstantTimeDataFmSetting;
import nts.uk.ctx.exio.dom.exo.dataformat.dataformatsetting.NumberDataFmSetting;
import nts.uk.ctx.exio.dom.exo.dataformat.dataformatsetting.TimeDataFmSetting;
import nts.uk.ctx.exio.dom.exo.outcnddetail.ConditionSettingCd;
import nts.uk.ctx.exio.dom.exo.outcnddetail.OutCndDetail;
import nts.uk.ctx.exio.dom.exo.outcnddetail.OutCndDetailItem;
import nts.uk.ctx.exio.dom.exo.outcnddetail.OutCndDetailRepository;
import nts.uk.ctx.exio.dom.exo.outputitem.CategoryItem;
import nts.uk.ctx.exio.dom.exo.outputitem.ConditionSettingCode;
import nts.uk.ctx.exio.dom.exo.outputitem.StandardOutputItem;
import nts.uk.ctx.exio.dom.exo.outputitem.StandardOutputItemRepository;
import nts.uk.ctx.exio.dom.exo.outputitemorder.StandardOutputItemOrder;
import nts.uk.ctx.exio.dom.exo.outputitemorder.StandardOutputItemOrderRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.enumcommon.NotUseAtr;

@Stateless
public class StdOutputCondSetService {

	@Inject
	private StdOutputCondSetRepository stdOutputCondSetRepository;

	@Inject
	private StandardOutputItemRepository stdOutputItemRepository;
	
	@Inject
	private StandardOutputItemOrderRepository standardOutputItemOrderRepository;

	@Inject
	private OutCndDetailRepository outCndDetailRepository;

	@Inject
	private AcquisitionExOutSetting mAcquisitionExOutSetting;
	
	@Inject
	private RegistrationCondDetails registrationCondDetails;
	

	@Inject
	private ExOutCtgRepository mExOutCtgRepository;

	// Screen T
	public Map<String, String> excuteCopy(String copyDestinationCode, String destinationName, String conditionSetCd,
			boolean overwite) {
		Map<String, String> resultExvuteCopy = new HashMap<>();
		String cid = AppContexts.user().companyId();
		Optional<StdOutputCondSet> stdOutputCondSet = stdOutputCondSetRepository.getStdOutputCondSetById(cid,
				copyDestinationCode);
		if (stdOutputCondSet.isPresent()) {
			if (overwite) {
				resultExvuteCopy.put("result", "OK");
				resultExvuteCopy.put("overwrite", "TO");
			} else {
				throw new BusinessException("Msg_3");
			}
		} else {
			resultExvuteCopy.put("result", "OK");
			resultExvuteCopy.put("overwrite", "DONOT");
		}
		resultExvuteCopy.put("copyDestinationCode", copyDestinationCode);
		resultExvuteCopy.put("destinatioName", destinationName);
		return resultExvuteCopy;
	}
	// ******

	/**
	 * 外部出力出力項目並び順登録
	 * @param
	 */
	private void registerOutputItemSortOrder(List<StandardOutputItem> listStandardOutputItem, String cndSetCd, RegisterMode mode, int standType){
	    StandardOutputItemOrder standardOutputItemOrder;
	    for (int i = 0; i<listStandardOutputItem.size(); i++) {
            if (standType == StandardAtr.STANDARD.value) {
                standardOutputItemOrder = new StandardOutputItemOrder(listStandardOutputItem.get(i).getCid(), listStandardOutputItem.get(i).getOutputItemCode().v(), cndSetCd, i);
                standardOutputItemOrderRepository.add(standardOutputItemOrder);
            }
        }
	}
	
	public void registerOutputSet(RegisterMode mode, int standType, StdOutputCondSet stdOutputCondSet,
	        List<StandardOutputItem> listStandardOutputItem) {
		if (outputSetRegisConfir(mode, standType, stdOutputCondSet.getCid(), stdOutputCondSet.getAutoExecution().value, stdOutputCondSet.getConditionSetCode().v())) {
			updateOutputCndSet(stdOutputCondSet, standType, mode);
		}
		if (listStandardOutputItem != null && !listStandardOutputItem.isEmpty()) {
		    registerOutputItemSortOrder(listStandardOutputItem, stdOutputCondSet.getConditionSetCode().v(), mode, standType);
		}
	}
	
	
	/**
	 * 外部出力設定削除実行
	 * @param cid
	 * @param condSetCd
	 */
	public void remove(String cid, String condSetCd){
		List<StandardOutputItem> listStandardOutputItem = stdOutputItemRepository.getStdOutItemByCidAndSetCd(cid, condSetCd);
		List<StandardOutputItemOrder> listStandardOutputItemOrder = standardOutputItemOrderRepository.getStandardOutputItemOrderByCidAndSetCd(cid, condSetCd);
		Optional<OutCndDetail> outCndDetail = outCndDetailRepository.getOutCndDetailById(cid, condSetCd);
		if(listStandardOutputItem != null && !listStandardOutputItem.isEmpty()) {
			stdOutputItemRepository.remove(listStandardOutputItem);
		}
		
		if(listStandardOutputItemOrder != null && !listStandardOutputItemOrder.isEmpty()){
			standardOutputItemOrderRepository.remove(listStandardOutputItemOrder);
		}
		if(outCndDetail.isPresent()) {
			outCndDetailRepository.remove(cid, condSetCd);
		}
		stdOutputCondSetRepository.remove(cid, condSetCd);
	}

	// 外部出力設定登録確認
	private boolean outputSetRegisConfir(RegisterMode mode, int standType, String cId, int autoExecution, String cndSetCd) {
		if(mode == RegisterMode.NEW) {
			if(standType == StandardAtr.STANDARD.value) {
  				if(checkExist(cId, cndSetCd)){
  					throw new BusinessException("Msg_3");
  				}
			}
		}
		if (autoExecution == NotUseAtr.NOT_USE.value) {
			throw new BusinessException("Msg_677");
		}
		return true;
	}

	private boolean checkExist(String cid, String cndSetCd) {
		Optional<StdOutputCondSet> stdOutputCondSet = stdOutputCondSetRepository.getStdOutputCondSetById(cid, cndSetCd);
		if (stdOutputCondSet.isPresent()) {
			return true;
		}
		return false;
	}
	
	//外部出力登録条件設定
	private void updateOutputCndSet(StdOutputCondSet stdOutputCondSet, int standType, RegisterMode mode){
		if (standType == StandardAtr.STANDARD.value) {
		    stdOutputCondSetRepository.add(stdOutputCondSet);
		}
	}

	// 外部出力設定複写実行
	private void outputSettingCopy(String cndSetCode, int standType, StdOutputCondSet copyParams) {

		String cId = copyParams.getCid();
		Optional<OutCndDetail> outCndDetail = Optional.empty();

		// 外部出力取得項目一覧_定型
		List<StandardOutputItem> listStdOutputItem = outputAcquisitionItemList(cId, cndSetCode);
		
		// 外部出力取得項目並順一覧_定型
		List<StandardOutputItemOrder> listStdOutputItemOrder = outputAcquisitionItemOrderList(cId, cndSetCode);
		
		// 外部出力取得条件一覧
		outCndDetail = outCndDetailRepository.getOutCndDetailByCode(cndSetCode);
		if(outCndDetail.isPresent()) {
			outCndDetail.get().setListOutCndDetailItem(outputAcquisitionConditionList(cndSetCode));
		}

		// 取得内容の項目を複写先用の情報に変更する
		changeContent(listStdOutputItem, copyParams.getConditionSetCode().v(), outCndDetail, listStdOutputItemOrder);

		// 外部出力設定複写実行登録
		copyExecutionRegistration(copyParams, standType, listStdOutputItem, listStdOutputItemOrder, outCndDetail, cndSetCode);

	}

	// 外部出力取得項目並順一覧_定型
	private List<StandardOutputItemOrder> outputAcquisitionItemOrderList(String cId, String cndSetCode) {
		return standardOutputItemOrderRepository.getStandardOutputItemOrderByCidAndSetCd(cId, cndSetCode);
	}
	
	//外部出力設定複製
	public void copy( int standType, String cndSetCode, StdOutputCondSet copyParams ){
		if (standType == StandardAtr.STANDARD.value) {
			outputSettingCopy(cndSetCode, standType, copyParams);
		}
	}

	// 外部出力取得項目一覧_定型
	private List<StandardOutputItem> outputAcquisitionItemList(String cId, String cndSetCode) {
		return stdOutputItemRepository.getStdOutItemByCidAndSetCd(cId, cndSetCode);
	}

	// 外部出力取得条件一覧
	private List<OutCndDetailItem> outputAcquisitionConditionList(String conditionSettingCd) {
		Optional<OutCndDetail> cndDetailOtp = mAcquisitionExOutSetting.getExOutCond(conditionSettingCd, null, StandardAtr.STANDARD, false, null);
			if(cndDetailOtp.isPresent()){
				return cndDetailOtp.get().getListOutCndDetailItem();
			}
		return Collections.emptyList();
	}

	// 外部出力設定複写実行登録
	private void copyExecutionRegistration(StdOutputCondSet copyParams, int standType,
			List<StandardOutputItem> listStdOutputItem, List<StandardOutputItemOrder> listStdOutputItemOrder,
			Optional<OutCndDetail> outCndDetail, String cndSetCode) {
		RegisterMode mode = RegisterMode.NEW;
		String oldCndSetCode = copyParams.getConditionSetCode().v();
		if (checkExist(copyParams.getCid(), oldCndSetCode)){
			mode = RegisterMode.UPDATE;
			this.remove(copyParams.getCid(), oldCndSetCode);
		}
		// 外部出力登録条件設定
		updateOutputCndSet(copyParams, standType, mode);

		// 外部出力登録出力項目
		registrationOutputItem(listStdOutputItem, listStdOutputItemOrder, mode, cndSetCode, oldCndSetCode);

		// 外部出力登録条件詳細
		registrationCondDetails.algorithm(outCndDetail, StandardAtr.STANDARD, mode);
	}
	
	// 取得内容の項目を複写先用の情報に変更する
	private void changeContent(List<StandardOutputItem> listStdOutputItem, String cndSetCode, Optional<OutCndDetail> outCndDetail, List<StandardOutputItemOrder> listStdOutputItemOrder) {
		if (outCndDetail.isPresent()) {
			outCndDetail.get().setConditionSettingCd(new ConditionSettingCd(cndSetCode));
			for (OutCndDetailItem outCndDetailItem : outCndDetail.get().getListOutCndDetailItem()) {
				outCndDetailItem.setConditionSettingCd(new ConditionSettingCd(cndSetCode));
			}
		}
		for (StandardOutputItem standardOutputItem : listStdOutputItem) {
			standardOutputItem.setConditionSettingCode(new ConditionSettingCode(cndSetCode));
			for (CategoryItem categoryItem : standardOutputItem.getCategoryItems()) {
				categoryItem.setConditionSettingCode(new ConditionSettingCode(cndSetCode));
			}
		}
		for (StandardOutputItemOrder stdOutputItemOrder : listStdOutputItemOrder) {
			stdOutputItemOrder.setConditionSettingCode(new ConditionSettingCode(cndSetCode));
		}
		
	}

	// 外部出力登録出力項目_定型
	private void registrationOutputItem(List<StandardOutputItem> listStdOutputItem,
			List<StandardOutputItemOrder> listStdOutputItemOrder, RegisterMode mode, String cndSetCode,
			String oldCndSetCode) {
		String cid = AppContexts.user().companyId();
		if (listStdOutputItem != null && !listStdOutputItem.isEmpty()) {
			stdOutputItemRepository.add(listStdOutputItem);
		}
		if (listStdOutputItemOrder != null && !listStdOutputItemOrder.isEmpty())
			standardOutputItemOrderRepository.add(listStdOutputItemOrder);
		
		List<AwDataFormatSetting> listAwData = stdOutputItemRepository.getAwDataFormatSetting(cid, oldCndSetCode);
		listAwData.forEach(item -> {
			item.setConditionSettingCode(new ConditionSettingCode(cndSetCode));
			stdOutputItemRepository.register(item);
		});
		List<DateFormatSetting> listDate = stdOutputItemRepository.getDateFormatSetting(cid, oldCndSetCode);
		listDate.forEach(item -> {
			item.setConditionSettingCode(new ConditionSettingCode(cndSetCode));
			stdOutputItemRepository.register(item);
		});
		List<InstantTimeDataFmSetting> listInstantTime = stdOutputItemRepository.getInstantTimeDataFmSetting(cid, oldCndSetCode);
		listInstantTime.forEach(item -> {
			item.setConditionSettingCode(new ConditionSettingCode(cndSetCode));
			stdOutputItemRepository.register(item);
		});
		List<NumberDataFmSetting> listNumber = stdOutputItemRepository.getNumberDataFmSetting(cid, oldCndSetCode);
		listNumber.forEach(item -> {
			item.setConditionSettingCode(new ConditionSettingCode(cndSetCode));
			stdOutputItemRepository.register(item);
		});
		List<CharacterDataFmSetting> listCharacter = stdOutputItemRepository.getCharacterDataFmSetting(cid, oldCndSetCode);
		listCharacter.forEach(item -> {
			item.setConditionSettingCode(new ConditionSettingCode(cndSetCode));
			stdOutputItemRepository.register(item);
		});
		List<TimeDataFmSetting> listTime = stdOutputItemRepository.getTimeDataFmSetting(cid, oldCndSetCode);
		listTime.forEach(item -> {
			item.setConditionSettingCode(new ConditionSettingCode(cndSetCode));
			stdOutputItemRepository.register(item);
		});
		
	}

	// 起動する
	public List<StdOutputCondSet> getListStandardOutputItem(String cId, String cndSetCd) {
		List<StdOutputCondSet> data = mAcquisitionExOutSetting.getExOutSetting("", StandardAtr.STANDARD, "");
		List<StdOutputCondSet> arrTemp = new ArrayList<StdOutputCondSet>();
		String userID = AppContexts.user().userId();
		if (data == null || data.isEmpty()) {
			throw new BusinessException("Msg_754");
		} else {
			for (StdOutputCondSet temp : data) {
				if (mAcquisitionExOutSetting
						.getExOutItemList(temp.getConditionSetCode().toString(), userID, "", StandardAtr.STANDARD, true)
						.isEmpty() == false) {
					arrTemp.add(temp);
				}
			}
			if (arrTemp == null || arrTemp.isEmpty()) {
				throw new BusinessException("Msg_754");
			}
		}

		return arrTemp;
	}


	// 外部出力取得項目一覧
	public List<StandardOutputItem> outputAcquisitionItemList(String condSetCd, String userId, String outItemCd,
			StandardAtr standardType, boolean isAcquisitionMode) {
		return mAcquisitionExOutSetting.getExOutItemList(condSetCd, userId, outItemCd, standardType,
				isAcquisitionMode);
	}

	// 外部出力カテゴリ
	public Optional<ExOutCtg> getExOutCtg(int catelogyId) {
		return mExOutCtgRepository.getExOutCtgByIdAndCtgSetting(catelogyId);
	}

}
