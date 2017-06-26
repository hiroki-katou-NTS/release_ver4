package nts.uk.ctx.pr.core.ac.basic.classification;

import lombok.AllArgsConstructor;
import lombok.Value;
import nts.uk.shr.com.primitive.Memo;

@Value
@AllArgsConstructor
public class ClassificationDto {

	String companyCode;
	String classificationCode;
	String classificationName;
	String classificationOutCode;
	String classificationMemo;

}
