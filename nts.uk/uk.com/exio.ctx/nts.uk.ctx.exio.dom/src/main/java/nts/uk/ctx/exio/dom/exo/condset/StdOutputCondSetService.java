package nts.uk.ctx.exio.dom.exo.condset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.uk.ctx.exio.dom.exo.commonalgorithm.AcquisitionExOutSetting;
import nts.uk.ctx.exio.dom.exo.outputitem.StandardOutputItem;
import nts.uk.ctx.exio.dom.exo.outputitemorder.StandardOutputItemOrder;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class StdOutputCondSetService {
	
	@Inject
	private StdOutputCondSetRepository stdOutputCondSetRepository;
	@Inject
	private AcquisitionExOutSetting mAcquisitionExOutSetting;
	
	
	//Screen T
	public Map<String, String> excuteCopy(String copyDestinationCode,String destinationName, String conditionSetCd, boolean overwite){
		Map<String, String> resultExvuteCopy = new HashMap<>();
		String cid = AppContexts.user().companyId();
		Optional<StdOutputCondSet> stdOutputCondSet = stdOutputCondSetRepository.getStdOutputCondSetById(cid,conditionSetCd);
		if(stdOutputCondSet.isPresent()){
			if(overwite){
				resultExvuteCopy.put("result", "OK");
				resultExvuteCopy.put("overwrite", "TO");
			}else{
				throw new BusinessException("Msg_3");
			}
		}else{
			resultExvuteCopy.put("result", "OK");
			resultExvuteCopy.put("overwrite", "DONOT");
		}
		resultExvuteCopy.put("copyDestinationCode", copyDestinationCode);
		resultExvuteCopy.put("destinatioName", destinationName);
	return resultExvuteCopy;
	}
	//******
	
	
	public void registerOutputSet(String screenMode , String standType, StdOutputCondSet stdOutputCondSet, boolean checkAutoExecution, List<StandardOutputItemOrder> stdOutItemOrder){
		if (outputSetRegisConfir(screenMode, standType, stdOutputCondSet.getCid(), checkAutoExecution)) {
			updateOutputCndSet(stdOutputCondSet,screenMode);
		}
	}
	
	//アルゴリズム「外部出力設定登録確認」を実行する
	private boolean outputSetRegisConfir(String screenMode , String standType, String cId, boolean checkAutoExecution){
		if (screenMode.equals("register")) {
			if (standType.equals("fixedForm")) {
  				if (checkExistCid(cId)){
 					return false;
  				} else if (checkAutoExecution) {
  					return true;
  					} else {
  						//throw new BusinessException("Msg_677");
  						return false;
  					}
			}
		}
		return true;
	}
	
	private boolean checkExistCid(String cid){
		Optional<StdOutputCondSet> stdOutputCondSet = stdOutputCondSetRepository.getStdOutputCondSetByCid(cid);
		if (stdOutputCondSet.isPresent()){
			return true;
		}
		return false;
	}
	
	//アルゴリズム「外部出力登録条件設定」を実行する
	private void updateOutputCndSet(StdOutputCondSet stdOutputCondSet, String screenMode){
		if (screenMode.equals("register")){
			stdOutputCondSetRepository.add(stdOutputCondSet);
		}
		if (screenMode.equals("update")) {
			stdOutputCondSetRepository.update(stdOutputCondSet);
		}
	}
	public List<StdOutputCondSet> getListStandardOutputItem(List<StdOutputCondSet> data){
		String userID = AppContexts.user().userId();
		
		for(StdOutputCondSet temp: data){
			if (mAcquisitionExOutSetting.getExOutItemList(temp.getConditionSetCode().toString(),userID,temp.getItemOutputName().toString(),true,true).isEmpty()){
				data.remove(temp);
			}
		}
		
		return data;
		
	}
	
	
}
