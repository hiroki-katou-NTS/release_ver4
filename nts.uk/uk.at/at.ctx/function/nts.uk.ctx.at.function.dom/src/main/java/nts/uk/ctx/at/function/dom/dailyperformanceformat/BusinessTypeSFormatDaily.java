package nts.uk.ctx.at.function.dom.dailyperformanceformat;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.function.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;


/**
 * 
 * @author anhdt 勤務種別日別実績の修正のフォーマット（スマホ版）
 *
 */
@Getter
public class BusinessTypeSFormatDaily extends AggregateRoot {
	
	/**
	 * 勤怠項目ID
	 */
	private String companyId;

	/**
	 * 勤務種別コード
	 */
	private BusinessTypeCode businessTypeCode;

	/**
	 * 日次表示項目一覧
	 */
	private int attendanceItemId;

	/**
	 * 並び順
	 */
	private int order;


	public BusinessTypeSFormatDaily(String companyId, BusinessTypeCode businessTypeCode, int attendanceItemId,
			int order) {
		super();
		this.companyId = companyId;
		this.businessTypeCode = businessTypeCode;
		this.attendanceItemId = attendanceItemId;
		this.order = order;
	}

	public static BusinessTypeSFormatDaily createFromJavaType(String companyId, String businessTypeCode,
			int attendanceItemId, int order) {
		return new BusinessTypeSFormatDaily(companyId, new BusinessTypeCode(businessTypeCode), attendanceItemId,
				order);
	}

}
