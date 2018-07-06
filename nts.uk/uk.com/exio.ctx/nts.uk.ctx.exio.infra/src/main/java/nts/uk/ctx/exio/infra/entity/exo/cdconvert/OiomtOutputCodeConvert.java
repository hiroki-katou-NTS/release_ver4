package nts.uk.ctx.exio.infra.entity.exo.cdconvert;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.exio.dom.exo.cdconvert.ConvertCode;
import nts.uk.ctx.exio.dom.exo.cdconvert.ConvertName;
import nts.uk.ctx.exio.dom.exo.cdconvert.OutputCodeConvert;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 出力コード変換
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "OIOMT_OUTPUT_CODE_CONVERT")
public class OiomtOutputCodeConvert extends UkJpaEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@EmbeddedId
	public OiomtOutputCodeConvertPk outputCodeConvertPk;

	/**
	 * コード変換コード
	 */
	@Basic(optional = false)
	@Column(name = "CONVERT_CD")
	public String convertCd;

	/**
	 * コード変換名称
	 */
	@Basic(optional = false)
	@Column(name = "CONVERT_NAME")
	public String convertName;

	/**
	 * 会社ID
	 */
	@Basic(optional = false)
	@Column(name = "CID")
	public String cid;

	/**
	 * 設定のないコードの出力
	 */
	@Basic(optional = false)
	@Column(name = "ACCEPT_WITHOUT_SETTING")
	public int acceptWithoutSetting;

	@Override
	protected Object getKey() {
		return outputCodeConvertPk;
	}

	public OutputCodeConvert toDomain() {
		return new OutputCodeConvert(new ConvertCode(this.convertCd), new ConvertName(this.convertName), this.cid,
				NotUseAtr.valueOf(this.acceptWithoutSetting));
	}

	public static OiomtOutputCodeConvert toEntity(OutputCodeConvert domain) {
		return new OiomtOutputCodeConvert(new OiomtOutputCodeConvertPk(domain.getCid(), domain.getConvertCode().v()),
				domain.getConvertName().v(), domain.getAcceptWithoutSetting().value);
	}

	public OiomtOutputCodeConvert(OiomtOutputCodeConvertPk outputCodeConvertPk, String convertName,
			int acceptWithoutSetting) {
		super();
		this.outputCodeConvertPk = outputCodeConvertPk;
		this.convertName = convertName;
		this.acceptWithoutSetting = acceptWithoutSetting;
	}

}
