package nts.uk.ctx.at.shared.dom.specialholiday.yearserviceper;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
@Getter
@AllArgsConstructor
public class YearServicePer extends AggregateRoot{
	/**会社ID**/
	private String companyId;
	/**コード**/
	private String specialHolidayCode;
	private String yearServiceCode;
	/**名称**/
	private String yearServiceName;
	private Integer yearServiceCls;
	private List<YearServicePerSet> yearServicePerSets;
	
	public static YearServicePer createFromJavaType(String companyId, String specialHolidayCode, String yearServiceCode, String yearServiceName, Integer yearServiceCls, List<YearServicePerSet> yearServicePerSets){
		return new YearServicePer(companyId, specialHolidayCode, yearServiceCode, yearServiceName, yearServiceCls, yearServicePerSets);
	}
	
//	public static YearServicePer update(String companyId, String specialHolidayCode, String yearServiceCode, String yearServiceName, Integer yearServiceCls, List<YearServicePerSet> yearServicePerSets){
//		return new YearServicePer(companyId, specialHolidayCode, yearServiceCode, yearServiceName, yearServiceCls, yearServicePerSets);
//	}
	
//	/** check validate for list "year service person" input **/
//	public static void validateInput(List<YearServicePer> yearServicePerlst){
//		// check duplicate code per
//		for(int i = 0; i < (yearServicePerlst.size()-2); i++){
//			for(int k = 0; k < yearServicePerlst.size(); i++){
//				if(yearServicePerlst.get(i).equals(yearServicePerlst.get(k))){
//					throw new BusinessException("Msg_3");
//				}
//			}
//		}
//	}
}
