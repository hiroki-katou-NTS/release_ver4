package nts.uk.ctx.at.record.dom.daily.optionalitemtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDate;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.record.dom.dailyprocess.calc.converter.DailyRecordToAttendanceItemConverter;
import nts.uk.ctx.at.record.dom.optitem.OptionalItem;
import nts.uk.ctx.at.record.dom.optitem.OptionalItemRepository;
import nts.uk.ctx.at.record.dom.optitem.applicable.EmpCondition;
import nts.uk.ctx.at.record.dom.optitem.calculation.CalcResultOfAnyItem;
import nts.uk.ctx.at.record.dom.optitem.calculation.Formula;
import nts.uk.ctx.at.shared.dom.adapter.employment.BsEmploymentHistoryImport;
import nts.uk.shr.com.context.AppContexts;

/** 日別実績の任意項目*/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnyItemValueOfDaily {
	
	private String employeeId;
	
	private GeneralDate ymd;

	/** 任意項目値: 任意項目値 */
	private List<AnyItemValue> items;
	
	
	
    /**
     * 任意項目の計算
     * @return
     */
    public static AnyItemValueOfDaily caluculationAnyItem(String companyId,
    											   		  String employeeId,
    											   		  GeneralDate ymd,
    											   		  List<OptionalItem> optionalItemList,
    											   		  List<Formula> formulaList,
    											   		  List<EmpCondition> empConditionList,
    											   		  Optional<DailyRecordToAttendanceItemConverter> dailyRecordDto,
    											   		  Optional<BsEmploymentHistoryImport> bsEmploymentHistOpt
												   		  ) {
    	
               
        //任意項目分ループ
        List<CalcResultOfAnyItem> anyItemList = new ArrayList<>();
        
        for(OptionalItem optionalItem : optionalItemList) {
        	
        	Optional<EmpCondition> empCondition = Optional.empty();
        	List<EmpCondition> findResult = empConditionList.stream().filter(t -> t.getOptItemNo().equals(optionalItem.getOptionalItemNo())).collect(Collectors.toList());
        	if(!findResult.isEmpty()) {
        		empCondition = Optional.of(findResult.get(0));
        	}
        	
        	//利用条件の判定
        	if(optionalItem.checkTermsOfUse(empCondition,bsEmploymentHistOpt)) {
        		List<Formula> test = formulaList.stream().filter(t -> t.getOptionalItemNo().equals(optionalItem.getOptionalItemNo())).collect(Collectors.toList());
        		//計算処理
                anyItemList.add(optionalItem.caluculationFormula(companyId, optionalItem, test, dailyRecordDto/*,Optional.empty()　月次用なので日別から呼ぶ場合は何も無し*/));
        	}
        }
        
        /*パラメータ：任意項目の計算結果から任意項目値クラスへ変換*/
        List<AnyItemValue> transAnyItem = new ArrayList<>();
        for(CalcResultOfAnyItem calcResultOfAnyItem:anyItemList) {
        	transAnyItem.add(new AnyItemValue(new AnyItemNo(Integer.parseInt(calcResultOfAnyItem.getOptionalItemNo().v())),
    										  calcResultOfAnyItem.getCount().map(v -> new AnyItemTimes(v)),
    										  calcResultOfAnyItem.getMoney().map(v -> new AnyItemAmount(v)),
    										  calcResultOfAnyItem.getTime().map(v -> new AnyItemTime(v))));       	
        }
                
        return new AnyItemValueOfDaily(employeeId,ymd,transAnyItem);
    }
    
    public void correctAnyType(OptionalItemRepository optionalMasterRepo){
    	List<String> itemIds = items.stream().map(i -> i.getItemNo().v())
				.map(id -> StringUtil.padLeft(String.valueOf(id), 3, '0')).collect(Collectors.toList());
		Map<Integer, OptionalItem> optionalMaster = optionalMasterRepo
				.findByListNos(AppContexts.user().companyId(), itemIds).stream()
				.collect(Collectors.toMap(c -> Integer.parseInt(c.getOptionalItemNo().v()), c -> c));
		if(!optionalMaster.isEmpty()){
			items.stream().forEach(i -> {
				OptionalItem master = optionalMaster.get(i.getItemNo().v());
				if(master != null){
					switch (master.getOptionalItemAtr()) {
					case AMOUNT:
						i.markAsAmount();
						break;
					case NUMBER:
						i.markAsTimes();
						break;
					case TIME:
						i.markAsTime();
						break;
					default:
						break;
					}
				}
			});	
		}
    }
    
}
