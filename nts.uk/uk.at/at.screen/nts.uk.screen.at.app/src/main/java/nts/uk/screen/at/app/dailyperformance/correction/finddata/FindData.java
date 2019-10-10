package nts.uk.screen.at.app.dailyperformance.correction.finddata;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.workrecord.operationsetting.DaiPerformanceFun;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.DaiPerformanceFunRepository;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class FindData implements IFindData{

	private static Map<String, Optional<DaiPerformanceFun>> daiPerfomanceFunMap = new HashMap<>();
	
	@Inject
	private DaiPerformanceFunRepository daiPerformanceFunRepository;
	
	@Override
	public  Optional<DaiPerformanceFun> getDailyPerformFun(String company) {
		if(daiPerfomanceFunMap.containsKey(company)){
			return daiPerfomanceFunMap.get(company);
		}else{
			Optional<DaiPerformanceFun> funOpt = daiPerformanceFunRepository.getDaiPerformanceFunById(company);
			daiPerfomanceFunMap.put(company, funOpt);
			return funOpt;
		}
	
	}

	@Override
	public void destroyFindData() {
		daiPerfomanceFunMap.clear();
	}

}
