package nts.uk.ctx.basic.dom.organization.classification;

import lombok.Getter;
import nts.uk.shr.com.primitive.Memo;

@Getter
public class Classification {
	/* 会社コード  */
	private final String companyCode;
	/*分類コード	 */
	private final ClassificationCode classificationCode;
	/*分類名称	 */
	private ClassificationName classificationName;
	/*外部コード	 */
	private ClassificationCode classificationOutCode;
	/*メモ	 */
	private Memo classificationMemo;

	/**
	 * Constructor
	 * @param companyCode
	 * @param classificationCode
	 * @param classificationName
	 * @param classificationOutCode
	 * @param classificationMemo
	 */
	public Classification(String companyCode, ClassificationCode classificationCode, ClassificationName classificationName,
			ClassificationCode classificationOutCode, Memo classificationMemo) {
		this.companyCode = companyCode;
		this.classificationCode = classificationCode;
		this.classificationName = classificationName;
		this.classificationOutCode = classificationOutCode;
		this.classificationMemo = classificationMemo;
	}

}
