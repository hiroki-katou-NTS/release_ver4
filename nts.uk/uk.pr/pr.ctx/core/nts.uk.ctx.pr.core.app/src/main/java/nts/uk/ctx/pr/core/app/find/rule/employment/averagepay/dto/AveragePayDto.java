package nts.uk.ctx.pr.core.app.find.rule.employment.averagepay.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.pr.core.app.find.itemmaster.dto.ItemMasterDto;
/**
 * 
 * @author Doan Duy Hung
 *
 */
@Data
@AllArgsConstructor
public class AveragePayDto {
	private String companyCode;
	private int roundTimingSet;
	private int attendDayGettingSet;
	private int roundDigitSet;
	private BigDecimal exceptionPayRate;
	private List<ItemMasterDto> itemsSalary;
	private List<ItemMasterDto> itemsAttend;
}
