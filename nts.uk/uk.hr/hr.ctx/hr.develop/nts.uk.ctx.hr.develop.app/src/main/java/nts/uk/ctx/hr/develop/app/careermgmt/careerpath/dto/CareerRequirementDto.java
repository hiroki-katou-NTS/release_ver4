package nts.uk.ctx.hr.develop.app.careermgmt.careerpath.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.uk.ctx.hr.develop.dom.careermgmt.careerpath.CareerRequirement;

@Data
@AllArgsConstructor
public class CareerRequirementDto {

	public Integer displayNumber;
	
	public Integer requirementType;
	
	public YearRequirementDto yearRequirement;
	
	public MasterRequirementDto masterRequirement;
	
	public String inputRequirement;

	public CareerRequirementDto(CareerRequirement careerRequirement) {
		super();
		this.displayNumber = careerRequirement.getDisplayNumber().v();
		this.requirementType = careerRequirement.getRequirementType().value;
		this.yearRequirement = YearRequirementDto.fromDomain(careerRequirement.getYearRequirement());
		this.masterRequirement = MasterRequirementDto.fromDomain(careerRequirement.getMasterRequirement());
		this.inputRequirement = careerRequirement.getInputRequirement().isPresent()?careerRequirement.getInputRequirement().get().v():null;
	}
	
}
