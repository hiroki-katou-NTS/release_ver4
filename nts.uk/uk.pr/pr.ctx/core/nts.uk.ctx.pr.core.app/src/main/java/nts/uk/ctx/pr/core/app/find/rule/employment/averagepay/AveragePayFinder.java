package nts.uk.ctx.pr.core.app.find.rule.employment.averagepay;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.pr.core.app.find.itemmaster.dto.ItemMasterDto;
import nts.uk.ctx.pr.core.app.find.rule.employment.averagepay.dto.AveragePayDto;
import nts.uk.ctx.pr.core.dom.itemmaster.AvePayAtr;
import nts.uk.ctx.pr.core.dom.itemmaster.ItemMasterRepository;
import nts.uk.ctx.pr.core.dom.itemmaster.itemattend.ItemAttend;
import nts.uk.ctx.pr.core.dom.itemmaster.itemattend.ItemAttendRespository;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ItemSalary;
import nts.uk.ctx.pr.core.dom.itemmaster.itemsalary.ItemSalaryRespository;
import nts.uk.ctx.pr.core.dom.rule.employment.averagepay.AveragePay;
import nts.uk.ctx.pr.core.dom.rule.employment.averagepay.AveragePayRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * 
 * @author Doan Duy Hung
 *
 */
@Stateless
public class AveragePayFinder {

	@Inject
	private AveragePayRepository averagePayRepository;
	
	@Inject
	private ItemMasterRepository itemMasterRepository;
	
	@Inject
	private ItemSalaryRespository itemSalaryRespository;
	
	@Inject
	private ItemAttendRespository itemAttendRespository;
	
	/**
	 * find average pay by company code, item salary by item code, item attend by item code
	 * @return average pay dto
	 */
	public AveragePayDto findByCompanyCode() {
		String companyCode = AppContexts.user().companyCode();
		
		// find average pay
		Optional<AveragePay> data = this.averagePayRepository.findByCompanyCode(companyCode);
		if (!data.isPresent()) {
			return null;
		}
		AveragePay avepay = data.get();
		
		List<ItemMasterDto> itemsSalaryDto = null;
		List<ItemMasterDto> itemsAttendDto = null;
		
		// find item master by salary item code and category = 0
		List<ItemSalary> itemSalarys = this.itemSalaryRespository.findAll(companyCode, AvePayAtr.Object);
		if (itemSalarys.isEmpty()){
			return null;
		}
		List<String> itemSalaryCodeList = itemSalarys.stream().map(x -> x.getItemCode().v()).collect(Collectors.toList());
		itemsSalaryDto = this.itemMasterRepository.findAll(companyCode, 0, itemSalaryCodeList)
				.stream().map(x -> ItemMasterDto.fromDomain(x)).collect(Collectors.toList());
		
		
		if (avepay.isAttenDayStatementItem()) {
				// find item master by attend item code and category = 2
				List<ItemAttend> itemAttends = this.itemAttendRespository.findAll(companyCode, AvePayAtr.Object);
				if (itemAttends.isEmpty()){
					return null;
				}
				List<String> itemAttendCodeList = itemAttends.stream().map(y -> y.getItemCode().v()).collect(Collectors.toList());
				itemsAttendDto = this.itemMasterRepository.findAll(companyCode, 2, itemAttendCodeList)
						.stream().map(x -> ItemMasterDto.fromDomain(x)).collect(Collectors.toList());
		}
		return new AveragePayDto(
				avepay.getCompanyCode(),
				avepay.getRoundTimingSet().value,
				avepay.getAttendDayGettingSet().value, 
				avepay.getRoundDigitSet().value,
				avepay.getExceptionPayRate().v(), 
				itemsSalaryDto,
				itemsAttendDto);
	}
}
