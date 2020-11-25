package nts.uk.ctx.at.record.infra.entity.employmentinfoterminal.infoterminal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCMT_TR_REQUEST")
public class KrcmtTrRequest extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KrcmtTrRequestPK pk;

	/**
	 * 会社ID
	 */
	@Column(name = "CID")
	public String cid;

	/**
	 * 会社コード
	 */
	@Column(name = "COMPANY_CD")
	public String companyCode;

//	/**
//	 * 社員ID
//	 */
//	@Column(name = "SID")
//	public String sid;

	/**
	 * 残業・休日出勤送信
	 */
	@Column(name = "SEND_OVERTIME_NAME")
	public Integer sendOverTime;

	/**
	 * 申請理由送信
	 */
	@Column(name = "SEND_REASON_APP")
	public Integer sendReasonApp;

	/**
	 * 時刻セット
	 */
	@Column(name = "SEND_SERVERTIME")
	public Integer sendServerTime;
	
	/**
	 * 社員ID送信
	 */
	@Column(name = "SEND_SID")
	public Integer sendSid;

	/**
	 * 弁当メニュー枠番送信
	 */
	@Column(name = "SEND_RESERVATION")
	public Integer sendReservation;

	/**
	 * 	勤務種類コード送信
	 */
	@Column(name = "SEND_WORKTYPE")
	public Integer sendWorkType;
	
	/**
	 * 就業時間帯コード送信
	 */
	@Column(name = "SEND_WORKTIME")
	public Integer sendWorkTime;

	/**
	 * 全ての打刻データ
	 */
	@Column(name = "RECV_ALL_STAMP")
	public Integer recvStamp;

	/**
	 * 全ての予約データ
	 */
	@Column(name = "RECV_ALL_RESERVATION")
	public Integer recvReservation;

	/**
	 * 全ての申請データ
	 */
	@Column(name = "RECV_ALL_APPLICATION")
	public Integer recbApplication;
	
	/**
	 * リモート設定
	 */
	@Column(name = "REMOTE_SETTING")
	public Integer remoteSetting;

	/**
	 * 再起動を行う
	 */
	@Column(name = "REBOOT")
	public Integer reboot;
	
	

	@Override
	protected Object getKey() {
		return pk;
	}

}