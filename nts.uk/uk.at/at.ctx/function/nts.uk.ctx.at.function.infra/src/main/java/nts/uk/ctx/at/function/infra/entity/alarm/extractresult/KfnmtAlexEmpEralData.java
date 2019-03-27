package nts.uk.ctx.at.function.infra.entity.alarm.extractresult;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "KFNMT_ALEX_EMP_ERAL_DATA")
public class KfnmtAlexEmpEralData extends UkJpaEntity {

	@EmbeddedId
	public KfnmtAlexEmpEralDataPK pk;

	/** アラーム値日付 */
	@Column(name = "ALARM_TARGET_TIME")
	public String alarmTime;		
	
	/** カテゴリーコード */
	@Column(name = "CATEGORY_CODE")
	public int categoryCode;	
	
	/** カテゴリー名 */
	@Column(name = "CATEGORY_NAME")
	public String categoryName;
	
	/** アラーム項目 */
	@Column(name = "ALARM_ITEM")
	public String alarmItem;	
	
	/** アラーム値メッセージ */
	@Column(name = "ALARM_MESSAGE")
	public String alarmMes;	
	
	/** コメント */
	@Column(name = "COMMENT")
	public String comment;	
	
	@Override
	protected Object getKey() {
		return pk;
	}
}
