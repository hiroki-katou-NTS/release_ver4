package nts.uk.ctx.pr.core.dom.rule.employment.unitprice.personal;

import java.util.List;
import java.util.Optional;


public interface PersonalUnitPriceRepository {
	
	 Optional<PersonalUnitPrice> find (String companyCode, String personalUnitPriceCode);
     
     List<PersonalUnitPrice> findAll (String companyCode);
     
     /**
      * Find all personal unit price
      * @param companyCode
      * @param unitPriceCodeList
      * @return
      */
	 List<PersonalUnitPrice> findAll(String companyCode, List<String> unitPriceCodeList);
	 
     void add(PersonalUnitPrice personalUnitPrice);
     
     void update(PersonalUnitPrice personalUnitPrice);
     
     void remove(String companyCode, String code);
    
}
