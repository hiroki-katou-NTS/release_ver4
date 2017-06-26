package nts.uk.ctx.at.record.infra.entity.divergencetime;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;
@Entity
@Table(name = "KMKMT_DIVERGENCE_TIME")
@AllArgsConstructor
@NoArgsConstructor
public class KmkmtDivergenceTime extends UkJpaEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	/*主キー*/
	@EmbeddedId
	public KmkmtDivergenceTimePK kmkmtDivergenceTimePK;
	/*乖離時間名称*/
	@Column(name = "DIVERGENCETIME_NAME")
	public String divTimeName;
	/*乖離時間使用設定*/
	@Column(name = "DIVERGENCETIME_USE_SET")
	public int divTimeUseSet;
	/*アラーム時間*/
	@Column(name = "ALARM_TIME")
	public int alarmTime;
	/*エラー時間*/
	@Column(name = "ERR_TIME")
	public int errTime;
	/*選択使用設定*/
	@Column(name = "SELECT_USE_SET")
	public int selectUseSet;
	/*乖離理由選択エラー解除*/
	@Column(name = "CANCEL_ERR_SEL_REASON")
	public int cancelErrSelReason;
	/*入力使用設定*/
	@Column(name = "INPUT_USE_SET")
	public int inputUseSet;
	/*乖離理由入力エラー解除*/
	@Column(name = "CANCEL_EER_INPUT_REASON")
	public int cancelErrInputReason;
	
	@Override
	protected Object getKey() {
		return null;
	}
	
	
}
