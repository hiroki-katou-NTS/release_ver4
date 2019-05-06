package nts.uk.ctx.at.function.infra.entity.processexecution;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class KfnmtExecutionTaskLogPK implements Serializable{
	private static final long serialVersionUID = 1L;
	/* 会社ID */
	@Column(name = "CID")
	public String companyId;
	
	/* コード */
	@Column(name = "EXEC_ITEM_CD")
	public String execItemCd;
	
	/* 実行ID */
	@Column(name = "EXEC_ID")
	public String execId;
	
	/* 更新処理 */
	@Column(name = "TASK_ID")
	public int taskId;
}
