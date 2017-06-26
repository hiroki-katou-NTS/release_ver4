package nts.uk.ctx.basic.app.find.organization.classification;

import lombok.Value;
import nts.uk.ctx.basic.dom.organization.classification.Classification;

@Value
public class ClassificationDto {

	private String classificationCode;

	private String classificationName;

	private String memo;

	
	public static ClassificationDto convertToDto(Classification classification) {
		ClassificationDto classificationDto = new ClassificationDto(classification.getClassificationCode().toString(),
				classification.getClassificationName().toString(), classification.getClassificationMemo().toString());
		return classificationDto;
	}

}
