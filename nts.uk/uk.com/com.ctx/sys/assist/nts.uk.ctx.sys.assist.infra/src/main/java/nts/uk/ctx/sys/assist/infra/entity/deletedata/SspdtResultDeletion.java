package nts.uk.ctx.sys.assist.infra.entity.deletedata;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.assist.dom.deletedata.ResultDeletion;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "SSPDT_RESULT_DELETION")
@NoArgsConstructor
@AllArgsConstructor
public class SspdtResultDeletion extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
    public SspdtResultDeletionPK sspdtResultDeletionPK;
	
	/** The company Id. */
	/** 会社ID */
	@Basic(optional = false)
	@Column(name = "CID")
	public String companyID;
	
	/** The deletion name. */
	/** 削除名称 */
	@Basic(optional = false)
	@Column(name = "DEL_NAME")
	public String delName;
	
	/** The deletion type. */
	/** 削除形態 */
	@Basic(optional = false)
	@Column(name = "DEL_TYPE")
	public int delType;
	
	/** The deleted file flag. */
	/** 削除済みファイル */
	@Basic(optional = false)
	@Column(name = "IS_DELETED_FILES_FLG")
	public int isDeletedFilesFlg;
	
	/** The deletion code. */
	/** 削除セットコード  */
	@Basic(optional = true)
	@Column(name = "DEL_CODE")
	public String delCode;
	
	/** The number employees. */
	/** 対象人数 */
	@Basic(optional = false)
	@Column(name = "NUMBER_EMPLOYEES")
	public int numberEmployees;
	
	/** The system type. */
	/** システム種類  */
	@Basic(optional = false)
	@Column(name = "SYSTEM_TYPE")
	public int systemType;
	
	/** The employee Id. */
	/** 実行者 */
	@Basic(optional = false)
	@Column(name = "SID")
	public String sId;
	
	/** The status. */
	/** 結果状態 */
	@Basic(optional = false)
	@Column(name = "STATUS")
	public int status;
	
	/** The start date time deletion. */
	/** 削除開始日時 */
	@Basic(optional = false)
	@Column(name = "START_DATE_TIME_DELETE")
	public GeneralDateTime startDateTimeDel;
	
	/** The end date time deletion. */
	/** 削除終了日時 */
	@Basic(optional = true)
	@Column(name = "END_DATE_TIME_DELETE")
	public GeneralDateTime endDateTimeDel;
	
	/** The file id. */
	/**  ファイルID */
	@Basic(optional = true)
	@Column(name = "FILE_ID")
	public String fileId;
	
	/** The file name. */
	/** 保存ファイル名 */
	@Basic(optional = true)
	@Column(name = "FILE_NAME")
	public String fileName;
	
	/** The file size. */
	/** ファイル容量 */
	@Basic(optional = true)
	@Column(name = "FILE_SIZE")
	public Integer fileSize;
	
	/** The password encrypt for compress file. */
	/** 手動保存の圧縮パスワード */
	@Basic(optional = true)
	@Column(name = "PASSWORD_FOR_COMPRESS_FILE")
	public String passwordCompressFileEncrypt;
	
	@Override
	protected Object getKey() {
		return sspdtResultDeletionPK;
	}

	public ResultDeletion toDomain() {
		boolean isDeletedFilesFlg = this.isDeletedFilesFlg == 1;
		return ResultDeletion.createFromJavatype(this.sspdtResultDeletionPK.delId, this.companyID, this.delName, 
				this.delType, isDeletedFilesFlg,
				this.delCode, this.numberEmployees, this.systemType, this.sId, this.status,
				this.startDateTimeDel, this.endDateTimeDel, this.fileId, this.fileName, this.fileSize, this.passwordCompressFileEncrypt);
	}

	public static SspdtResultDeletion toEntity(ResultDeletion result) {
		int isDeletedFilesFlg = result.isDeletedFilesFlg() ? 1 : 0;
		
		return new SspdtResultDeletion(new SspdtResultDeletionPK(result.getDelId()),
				result.getCompanyId(), result.getDelName().v(), result.getDelType().value, isDeletedFilesFlg,
				result.getDelCode().v(), result.getNumberEmployees(), result.getSystemType().value, 
				result.getSId(), result.getStatus().value, result.getStartDateTimeDel(), 
				result.getEndDateTimeDel(), result.getFileId(), result.getFileName().v(), result.getFileSize(), 
				result.getPasswordCompressFileEncrypt().isPresent() ? result.getPasswordCompressFileEncrypt().get().toString() : null );
	}
}
