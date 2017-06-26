package nts.uk.ctx.pr.core.app.find.rule.law.tax.residential.input;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Value;
import nts.uk.ctx.pr.core.app.command.rule.law.tax.residential.input.ResidenceTaxCommand;
import nts.uk.ctx.pr.core.dom.rule.law.tax.residential.input.PersonResiTax;

@Value
public class PersonResiTaxDto {
	String personId;
	int yeakKey;
	List<ResidenceTaxCommand> residenceTax;
	
	public static PersonResiTaxDto fromDomain(PersonResiTax domain){
		return new PersonResiTaxDto(domain.getPersonId().v(),
				domain.getYearKey().v(),
				domain.getResidenceTax().stream().map(x -> new ResidenceTaxCommand(x.getMonth(), x.getValue().v())).collect(Collectors.toList()));
	}
}
