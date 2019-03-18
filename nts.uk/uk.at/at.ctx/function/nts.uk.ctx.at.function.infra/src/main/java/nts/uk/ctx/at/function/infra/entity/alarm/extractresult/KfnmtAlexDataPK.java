package nts.uk.ctx.at.function.infra.entity.alarm.extractresult;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class KfnmtAlexDataPK implements Serializable{

	/***/
	private static final long serialVersionUID = 1L;

	/** 実行者ID: 手動の場合は社員ID、更新処理自動実行の場合は更新処理自動実行のタスクのコード */
	@Column(name = "EXECUTE_EMP_ID")
	public String executeEmpId;	
	
	/** 会社ID */
	@Column(name = "CID")
	public String cid;	
	
	/** "実行種類
		1:手動
		2:更新処理自動実行"
	*/
	@Column(name = "EXECUTE_TYPE")
	public int executeType;
	
	/** 実行ID */
	@Column(name = "EXECUTE_ID")
	public String executeId;

}
