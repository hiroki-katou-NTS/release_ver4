package nts.uk.ctx.sys.assist.dom.deletedata;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.assist.dom.system.SystemTypeEnum;

@Getter
@Setter
@AllArgsConstructor
/**
 * データ削除の保存結果
 */
public class ResultDeletion extends AggregateRoot {

	// データ削除処理ID
	/** The deletion Id. */
	private String delId;

	// 会社ID
	/** The company Id. */
	private String companyId;

	// 削除名称
	/** The deletion name. */
	private DelName delName;

	// 削除形態
	/** The deletion type. */
	private DelType delType;

	// 削除済みファイル
	/** The deletion files flag. */
	private boolean isDeletedFilesFlg;

	// 削除セットコード
	/** The deletion code. */
	private DelCode delCode;

	// 対象人数
	/** The number employees. */
	private int numberEmployees;

	// システム種類
	/** The system type. */
	private SystemTypeEnum systemType;

	// 実行者
	/** The practitioner id. */
	private String sId;

	// 結果状態
	/** The status. */
	private SaveStatus status;

	// 削除開始日時
	/** The start date time deletion. */
	private GeneralDateTime startDateTimeDel;

	// 削除終了日時
	/** The end date time deletion. */
	private GeneralDateTime endDateTimeDel;

	// ファイルID
	/** The file id. */
	private String fileId;

	// 保存ファイル名
	/** The file name. */
	private FileName fileName;

	// ファイル容量
	/** The file size. */
	private Integer fileSize;

	// 手動保存の圧縮パスワード
	/** The password encrypt for compress file. */
	private Optional<PasswordCompressFileEncrypt> passwordCompressFileEncrypt;

	public static ResultDeletion createFromJavatype(String delId, String companyId, String delName, int delType,
			boolean isDeletedFilesFlg, String delCode, int numberEmployees, int systemType, String sId, int status,
			GeneralDateTime startDateTimeDel, GeneralDateTime endDateTimeDel, String fileId, String fileName,
			Integer fileSize, String passwordCompressFileEncrypt) {
		return new ResultDeletion(delId, companyId, new DelName(delName), DelType.valueOf(delType), isDeletedFilesFlg,
				new DelCode(delCode), numberEmployees, SystemTypeEnum.valueOf(systemType), sId,
				SaveStatus.valueOf(status), startDateTimeDel, endDateTimeDel, fileId, new FileName(fileName), fileSize,
				Optional.ofNullable(new PasswordCompressFileEncrypt(passwordCompressFileEncrypt)));
	}
}
