package nts.uk.ctx.basic.app.find.system.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.basic.app.find.system.bank.dto.BankDto;
import nts.uk.ctx.basic.app.find.system.bank.dto.BranchDto;
import nts.uk.ctx.basic.dom.system.bank.Bank;
import nts.uk.ctx.basic.dom.system.bank.BankRepository;
import nts.uk.ctx.basic.dom.system.bank.branch.BankBranch;
import nts.uk.ctx.basic.dom.system.bank.branch.BankBranchRepository;
import nts.uk.shr.com.context.AppContexts;
/**
 * BankFinder
 * @author sonnh
 *
 */
@Stateless
public class BankFinder {
	@Inject
	private BankRepository bankRepository;
	
	@Inject
	private BankBranchRepository bankBranchRepository;
	
	public List<BankDto> findAll() {
		List<BankDto> result = new ArrayList<>();
		
		String companyCode = AppContexts.user().companyCode();
		// get bank branch list
		List<BankBranch> branchList = this.bankBranchRepository.findAll(companyCode);	
		// get bank list
		List<Bank> bankList = this.bankRepository.findAll(companyCode);
		
		Map<String, List<BankBranch>> branchMap = branchList.stream().collect(Collectors.groupingBy(BankBranch::getBankCode));
		
		// get bank children node 
		bankList.forEach(bank -> {
			BankDto bankDto = new BankDto(bank.getBankCode().v(), bank.getBankName().v(), bank.getBankNameKana().v(), bank.getMemo().v(), null);
			
			List<BankBranch> branchs = branchMap.get(bank.getBankCode().v());
			if (!CollectionUtil.isEmpty(branchs)) {
				List<BranchDto> branhchDtos = branchs.stream()
						.map(x -> new BranchDto(x.getBankBranchCode().v(),x.getBranchId().toString(), x.getBankBranchName().v(), x.getBankBranchNameKana().v(), x.getMemo().v()))
						.collect(Collectors.toList());
				
				if (!CollectionUtil.isEmpty(branhchDtos)) {
					bankDto.setBankBranch(branhchDtos);
				}
			}
			
			// add children to father
			result.add(bankDto);
		});		
		
		return result;
	}
	
	/**
	 * check exists bank
	 */
	public void checkExistsBank() {
		String companyCode = AppContexts.user().companyCode();
		List<BankBranch> branchList = this.bankBranchRepository.findAll(companyCode);
		List<Bank> bankList = this.bankRepository.findAll(companyCode);
		if (branchList.isEmpty() || bankList.isEmpty()) {
			throw new BusinessException("ER010");
		}
	}
}
