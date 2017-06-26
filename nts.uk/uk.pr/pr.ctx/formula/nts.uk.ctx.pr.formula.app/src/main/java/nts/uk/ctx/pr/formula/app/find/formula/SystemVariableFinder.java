package nts.uk.ctx.pr.formula.app.find.formula;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.pr.formula.dom.adapter.SystemVariableAdapter;
import nts.uk.ctx.pr.formula.dom.repository.BasicPayrollRepository;
import nts.uk.ctx.pr.formula.dom.repository.SystemVariableRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.context.LoginUserContext;

/**
 * @author nampt
 *
 */
@Stateless
public class SystemVariableFinder {

	@Inject
	private SystemVariableRepository systemVariableRepository;
	
	@Inject
	private BasicPayrollRepository basicPayrollRepository;
	
	@Inject
	private SystemVariableAdapter systemVariableAdapter;

	public List<SystemVariableDto> init() {
		
		LoginUserContext login = AppContexts.user();
		String companyCode = login.companyCode();

		List<SystemVariableDto> systemVariables = new ArrayList<>();

		systemVariables.add(new SystemVariableDto("システム日付（年月日）", "1"));
		systemVariables.add(new SystemVariableDto("システム日付（年月）", "2"));
		systemVariables.add(new SystemVariableDto("システム日付（年）", "3"));
		systemVariables.addAll(systemVariableRepository.findAll().stream().map(f -> SystemVariableDto.fromDomain(f))
				.collect(Collectors.toList()));

		systemVariables.forEach(item -> {
			if (item.getSystemVariableCode().equals("1")) {
				item.setResult(GeneralDate.today().toString());
			} else if (item.getSystemVariableCode().equals("2")) {
				item.setResult(GeneralDate.today().yearMonth().toString());
			} else if (item.getSystemVariableCode().equals("3")) {				
				item.setResult(String.valueOf(GeneralDate.today().year()));
			} else if(item.getSystemVariableCode().equals("4")){
				BigDecimal numbersOfWorkingDay = systemVariableAdapter.getNumbersOfWorkingDay(companyCode, 1, 0, GeneralDate.today().yearMonth().v(), 0);
				item.setResult(numbersOfWorkingDay.toString());
			} else if (item.getSystemVariableCode().equals("5")){
				Optional<BasicPayrollDto> basicPayroll = basicPayrollRepository.findAll(companyCode).map(f -> BasicPayrollDto.fromDomain(f));				
				item.setResult(basicPayroll.get().getBaseDay().toString());
			} else if (item.getSystemVariableCode().equals("6")) {
				Optional<BasicPayrollDto> basicPayroll = basicPayrollRepository.findAll(companyCode).map(f -> BasicPayrollDto.fromDomain(f));
				BigDecimal timeNotationSet = basicPayroll.get().getTimeNotationSetting();
				BigDecimal baseHours = basicPayroll.get().getBaseDay();
				if (timeNotationSet == BigDecimal.ONE) {
					item.setResult(baseHours.divide(new BigDecimal(60), 2, RoundingMode.HALF_UP).toString());
				} else {
					int hour = baseHours.intValue() / 60;
					int minute = baseHours.intValue() % 60;
					item.setResult(hour + ":" + minute);
				}
			}
		});

		return systemVariables;
	}

}
