package nts.uk.ctx.bs.employee.dom.employee.mgndata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.employeeinfo.EmployeeCode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/** 社員データ管理情報 */
public class EmployeeDataMngInfo extends AggregateRoot {

	/** 会社ID */
	private String companyId;

	/** 個人ID */
	private String personId;

	/** 社員ID */
	private String employeeId;

	/** 社員コード */
	private EmployeeCode employeeCode;

	/** 削除状況 */
	private EmployeeDeletionAttr deletedStatus;

	/** 一時削除日時 */
	private GeneralDate deleteDateTemporary;

	/** 削除理由 */
	private RemoveReason removeReason;

	/** 外部コード */
	private String externalCode;

	public static EmployeeDataMngInfo createFromJavaType(String cId, String pId, String sId, String sCd, int delStatus,
			GeneralDate delTemp, String removeReason, String extCode) {
		return new EmployeeDataMngInfo(cId, pId, sId, new EmployeeCode(sCd),
				EnumAdaptor.valueOf(delStatus, EmployeeDeletionAttr.class), delTemp, new RemoveReason(removeReason),
				extCode);
	}
}
