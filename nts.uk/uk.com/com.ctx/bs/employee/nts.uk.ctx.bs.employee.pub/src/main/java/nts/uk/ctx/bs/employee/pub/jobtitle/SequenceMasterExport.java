package nts.uk.ctx.bs.employee.pub.jobtitle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
public class SequenceMasterExport {

	/** The company id. */
	//会社ID
	private String companyId;

	/** The order. */
	//並び順
	private int order;

	/** The sequence code. */
	//序列コード
	private String sequenceCode;

	/** The sequence name. */
	//序列名称
	private String sequenceName;
}
