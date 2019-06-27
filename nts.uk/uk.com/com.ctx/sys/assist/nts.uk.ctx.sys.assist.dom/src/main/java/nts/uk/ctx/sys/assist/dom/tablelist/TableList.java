package nts.uk.ctx.sys.assist.dom.tablelist;

import java.util.List;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.sys.assist.dom.category.RecoverFormCompanyOther;
import nts.uk.ctx.sys.assist.dom.category.StorageRangeSaved;
import nts.uk.ctx.sys.assist.dom.category.TimeStore;
import nts.uk.ctx.sys.assist.dom.categoryfieldmt.HistoryDiviSion;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * テーブル一覧
 */
@Getter
public class TableList extends DomainObject {
	/**
	 * カテゴリID
	 */
	private String categoryId;

	/**
	 * カテゴリ名
	 */
	private String categoryName;

	/**
	 * データ保存処理ID
	 */
	private String dataStorageProcessingId;

	/**
	 * データ復旧処理ID
	 */
	@Setter
	private Optional<String> dataRecoveryProcessId;

	/**
	 * テーブルNo
	 */
	private int tableNo;

	/**
	 * テーブル日本語名
	 */
	private String tableJapaneseName;

	/**
	 * テーブル物理名
	 */
	private String tableEnglishName;

	/**
	 * 付加取得項目_会社ID
	 */
	private Optional<String> fieldAcqCid;

	/**
	 * 付加取得項目_日付
	 */
	private Optional<String> fieldAcqDateTime;

	/**
	 * 付加取得項目_社員ID
	 */
	private Optional<String> fieldAcqEmployeeId;

	/**
	 * 付加取得項目_終了日付
	 */
	private Optional<String> fieldAcqEndDate;

	/**
	 * 付加取得項目_開始日付
	 */
	private Optional<String> fieldAcqStartDate;

	/**
	 * 保存セットコード
	 */
	private Optional<String> saveSetCode;

	/**
	 * 保存セット名称
	 */
	private String saveSetName;

	// /**
	// * 保存ファイル名
	// */
	// private String saveFileName;

	/**
	 * 保存形態
	 */
	private String saveForm;

	/**
	 * 保存日付From
	 */
	@Setter
	private Optional<String> saveDateFrom;

	/**
	 * 保存日付To
	 */
	@Setter
	private Optional<String> saveDateTo;

	/**
	 * 保存時保存範囲
	 */
	private StorageRangeSaved storageRangeSaved;

	/**
	 * 保存期間区分
	 */
	private TimeStore retentionPeriodCls;

	/**
	 * 内部ファイル名
	 */
	private String internalFileName;

	/**
	 * 別会社区分
	 */
	@Setter
	private RecoverFormCompanyOther anotherComCls;

	/**
	 * 参照年
	 */
	private Optional<String> referenceYear;

	/**
	 * 参照月
	 */
	private Optional<String> referenceMonth;

	/**
	 * 圧縮ファイル名
	 */
	private String compressedFileName;

	/**
	 * 子側結合キー1
	 */
	private Optional<String> fieldChild1;

	/**
	 * 子側結合キー2
	 */
	private Optional<String> fieldChild2;

	/**
	 * 子側結合キー3
	 */
	private Optional<String> fieldChild3;

	/**
	 * 子側結合キー4
	 */
	private Optional<String> fieldChild4;

	/**
	 * 子側結合キー5
	 */
	private Optional<String> fieldChild5;

	/**
	 * 子側結合キー6
	 */
	private Optional<String> fieldChild6;

	/**
	 * 子側結合キー7
	 */
	private Optional<String> fieldChild7;

	/**
	 * 子側結合キー8
	 */
	private Optional<String> fieldChild8;

	/**
	 * 子側結合キー9
	 */
	private Optional<String> fieldChild9;

	/**
	 * 子側結合キー10
	 */
	private Optional<String> fieldChild10;

	/**
	 * 履歴区分
	 */
	private HistoryDiviSion historyCls;

	/**
	 * 復旧対象可不可
	 */
	@Setter
	private Optional<Integer> canNotBeOld;

	/**
	 * 復旧対象選択
	 */
	@Setter
	private Optional<Integer> selectionTargetForRes;

	/**
	 * 抽出キー区分1
	 */
	private Optional<String> clsKeyQuery1;

	/**
	 * 抽出キー区分2
	 */
	private Optional<String> clsKeyQuery2;

	/**
	 * 抽出キー区分3
	 */
	private Optional<String> clsKeyQuery3;

	/**
	 * 抽出キー区分4
	 */
	private Optional<String> clsKeyQuery4;

	/**
	 * 抽出キー区分5
	 */
	private Optional<String> clsKeyQuery5;

	/**
	 * 抽出キー区分6
	 */
	private Optional<String> clsKeyQuery6;

	/**
	 * 抽出キー区分7
	 */
	private Optional<String> clsKeyQuery7;

	/**
	 * 抽出キー区分8
	 */
	private Optional<String> clsKeyQuery8;

	/**
	 * 抽出キー区分9
	 */
	private Optional<String> clsKeyQuery9;

	/**
	 * 抽出キー区分10
	 */
	private Optional<String> clsKeyQuery10;

	/**
	 * 抽出キー項目1
	 */
	private Optional<String> fieldKeyQuery1;

	/**
	 * 抽出キー項目2
	 */
	private Optional<String> fieldKeyQuery2;

	/**
	 * 抽出キー項目3
	 */
	private Optional<String> fieldKeyQuery3;

	/**
	 * 抽出キー項目4
	 */
	private Optional<String> fieldKeyQuery4;

	/**
	 * 抽出キー項目5
	 */
	private Optional<String> fieldKeyQuery5;

	/**
	 * 抽出キー項目6
	 */
	private Optional<String> fieldKeyQuery6;

	/**
	 * 抽出キー項目7
	 */
	private Optional<String> fieldKeyQuery7;

	/**
	 * 抽出キー項目8
	 */
	private Optional<String> fieldKeyQuery8;

	/**
	 * 抽出キー項目9
	 */
	private Optional<String> fieldKeyQuery9;

	/**
	 * 抽出キー項目10
	 */
	private Optional<String> fieldKeyQuery10;

	/**
	 * 抽出条件キー固定
	 */
	private Optional<String> defaultCondKeyQuery;

	/**
	 * 日付項目1
	 */
	private Optional<String> fieldDate1;

	/**
	 * 日付項目2
	 */
	private Optional<String> fieldDate2;

	/**
	 * 日付項目3
	 */
	private Optional<String> fieldDate3;

	/**
	 * 日付項目4
	 */
	private Optional<String> fieldDate4;

	/**
	 * 日付項目5
	 */
	private Optional<String> fieldDate5;

	/**
	 * 日付項目6
	 */
	private Optional<String> fieldDate6;

	/**
	 * 日付項目7
	 */
	private Optional<String> fieldDate7;

	/**
	 * 日付項目8
	 */
	private Optional<String> fieldDate8;

	/**
	 * 日付項目9
	 */
	private Optional<String> fieldDate9;

	/**
	 * 日付項目10
	 */
	private Optional<String> fieldDate10;

	/**
	 * 日付項目11
	 */
	private Optional<String> fieldDate11;

	/**
	 * 日付項目12
	 */
	private Optional<String> fieldDate12;

	/**
	 * 日付項目13
	 */
	private Optional<String> fieldDate13;

	/**
	 * 日付項目14
	 */
	private Optional<String> fieldDate14;

	/**
	 * 日付項目15
	 */
	private Optional<String> fieldDate15;

	/**
	 * 日付項目16
	 */
	private Optional<String> fieldDate16;

	/**
	 * 日付項目17
	 */
	private Optional<String> fieldDate17;

	/**
	 * 日付項目18
	 */
	private Optional<String> fieldDate18;
	/**
	 * 日付項目19
	 */
	private Optional<String> fieldDate19;

	/**
	 * 日付項目20
	 */
	private Optional<String> fieldDate20;

	/**
	 * 更新キー項目1
	 */
	private Optional<String> filedKeyUpdate1;

	/**
	 * 更新キー項目2
	 */
	private Optional<String> filedKeyUpdate2;

	/**
	 * 更新キー項目3
	 */
	private Optional<String> filedKeyUpdate3;

	/**
	 * 更新キー項目4
	 */
	private Optional<String> filedKeyUpdate4;

	/**
	 * 更新キー項目5
	 */
	private Optional<String> filedKeyUpdate5;

	/**
	 * 更新キー項目6
	 */
	private Optional<String> filedKeyUpdate6;

	/**
	 * 更新キー項目7
	 */
	private Optional<String> filedKeyUpdate7;

	/**
	 * 更新キー項目8
	 */
	private Optional<String> filedKeyUpdate8;

	/**
	 * 更新キー項目9
	 */
	private Optional<String> filedKeyUpdate9;

	/**
	 * 更新キー項目10
	 */
	private Optional<String> filedKeyUpdate10;

	/**
	 * 更新キー項目11
	 */
	private Optional<String> filedKeyUpdate11;

	/**
	 * 更新キー項目12
	 */
	private Optional<String> filedKeyUpdate12;

	/**
	 * 更新キー項目13
	 */
	private Optional<String> filedKeyUpdate13;

	/**
	 * 更新キー項目14
	 */
	private Optional<String> filedKeyUpdate14;

	/**
	 * 更新キー項目15
	 */
	private Optional<String> filedKeyUpdate15;

	/**
	 * 更新キー項目16
	 */
	private Optional<String> filedKeyUpdate16;

	/**
	 * 更新キー項目17
	 */
	private Optional<String> filedKeyUpdate17;

	/**
	 * 更新キー項目18
	 */
	private Optional<String> filedKeyUpdate18;

	/**
	 * 更新キー項目19
	 */
	private Optional<String> filedKeyUpdate19;

	/**
	 * 更新キー項目20
	 */
	private Optional<String> filedKeyUpdate20;

	/**
	 * 画面保存期間
	 */
	private Optional<String> screenRetentionPeriod;

	/**
	 * 補足説明
	 */
	private Optional<String> supplementaryExplanation;

	/**
	 * 親テーブル日本語名
	 */
	private Optional<String> parentTblJpName;

	/**
	 * 親テーブル有無
	 */
	private NotUseAtr hasParentTblFlg;

	/**
	 * 親テーブル物理名
	 */
	private Optional<String> parentTblName;

	/**
	 * 親側結合キー1
	 */
	private Optional<String> fieldParent1;

	/**
	 * 親側結合キー2
	 */
	private Optional<String> fieldParent2;

	/**
	 * 親側結合キー3
	 */
	private Optional<String> fieldParent3;

	/**
	 * 親側結合キー4
	 */
	private Optional<String> fieldParent4;

	/**
	 * 親側結合キー5
	 */
	private Optional<String> fieldParent5;

	/**
	 * 親側結合キー6
	 */
	private Optional<String> fieldParent6;

	/**
	 * 親側結合キー7
	 */
	private Optional<String> fieldParent7;

	/**
	 * 親側結合キー8
	 */
	private Optional<String> fieldParent8;

	/**
	 * 親側結合キー9
	 */
	private Optional<String> fieldParent9;

	/**
	 * 親側結合キー10
	 */
	private Optional<String> fieldParent10;

	/**
	 * 調査用保存
	 */
	private NotUseAtr surveyPreservation;

	public TableList(String categoryId, String categoryName, String dataStorageProcessingId,
			String dataRecoveryProcessId, int tableNo, String tableJapaneseName, String tableEnglishName,
			String fieldAcqCid, String fieldAcqDateTime, String fieldAcqEmployeeId, String fieldAcqEndDate,
			String fieldAcqStartDate, String saveSetCode, String saveSetName, String saveForm, String saveDateFrom,
			String saveDateTo, int storageRangeSaved, int retentionPeriodCls, String internalFileName,
			int anotherComCls, String referenceYear, String referenceMonth, String compressedFileName,
			String fieldChild1, String fieldChild2, String fieldChild3, String fieldChild4, String fieldChild5,
			String fieldChild6, String fieldChild7, String fieldChild8, String fieldChild9, String fieldChild10,
			int historyCls, int canNotBeOld, int selectionTargetForRes, String clsKeyQuery1, String clsKeyQuery2,
			String clsKeyQuery3, String clsKeyQuery4, String clsKeyQuery5, String clsKeyQuery6, String clsKeyQuery7,
			String clsKeyQuery8, String clsKeyQuery9, String clsKeyQuery10, String fieldKeyQuery1,
			String fieldKeyQuery2, String fieldKeyQuery3, String fieldKeyQuery4, String fieldKeyQuery5,
			String fieldKeyQuery6, String fieldKeyQuery7, String fieldKeyQuery8, String fieldKeyQuery9,
			String fieldKeyQuery10, String defaultCondKeyQuery, String fieldDate1, String fieldDate2, String fieldDate3,
			String fieldDate4, String fieldDate5, String fieldDate6, String fieldDate7, String fieldDate8,
			String fieldDate9, String fieldDate10, String fieldDate11, String fieldDate12, String fieldDate13,
			String fieldDate14, String fieldDate15, String fieldDate16, String fieldDate17, String fieldDate18,
			String fieldDate19, String fieldDate20, String filedKeyUpdate1, String filedKeyUpdate2,
			String filedKeyUpdate3, String filedKeyUpdate4, String filedKeyUpdate5, String filedKeyUpdate6,
			String filedKeyUpdate7, String filedKeyUpdate8, String filedKeyUpdate9, String filedKeyUpdate10,
			String filedKeyUpdate11, String filedKeyUpdate12, String filedKeyUpdate13, String filedKeyUpdate14,
			String filedKeyUpdate15, String filedKeyUpdate16, String filedKeyUpdate17, String filedKeyUpdate18,
			String filedKeyUpdate19, String filedKeyUpdate20, String screenRetentionPeriod,
			String supplementaryExplanation, String parentTblJpName, int hasParentTblFlg, String parentTblName,
			String fieldParent1, String fieldParent2, String fieldParent3, String fieldParent4, String fieldParent5,
			String fieldParent6, String fieldParent7, String fieldParent8, String fieldParent9, String fieldParent10,
			int surveyPreservation) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.dataStorageProcessingId = dataStorageProcessingId;
		this.dataRecoveryProcessId = Optional.ofNullable(dataRecoveryProcessId);
		this.tableNo = tableNo;
		this.tableJapaneseName = tableJapaneseName;
		this.tableEnglishName = tableEnglishName;
		this.fieldAcqCid = Optional.ofNullable(fieldAcqCid);
		this.fieldAcqDateTime = Optional.ofNullable(fieldAcqDateTime);
		this.fieldAcqEmployeeId = Optional.ofNullable(fieldAcqEmployeeId);
		this.fieldAcqEndDate = Optional.ofNullable(fieldAcqEndDate);
		this.fieldAcqStartDate = Optional.ofNullable(fieldAcqStartDate);
		this.saveSetCode = Optional.ofNullable(saveSetCode);
		this.saveSetName = saveSetName;
		this.saveForm = saveForm;
		this.saveDateFrom = Optional.ofNullable(saveDateFrom);
		this.saveDateTo = Optional.ofNullable(saveDateTo);
		this.storageRangeSaved = EnumAdaptor.valueOf(storageRangeSaved, StorageRangeSaved.class);
		this.retentionPeriodCls = EnumAdaptor.valueOf(retentionPeriodCls, TimeStore.class);
		this.internalFileName = internalFileName;
		this.anotherComCls = EnumAdaptor.valueOf(anotherComCls, RecoverFormCompanyOther.class);
		this.referenceYear = Optional.ofNullable(referenceYear);
		this.referenceMonth = Optional.ofNullable(referenceMonth);
		this.compressedFileName = compressedFileName;
		this.fieldChild1 = Optional.ofNullable(fieldChild1);
		this.fieldChild2 = Optional.ofNullable(fieldChild2);
		this.fieldChild3 = Optional.ofNullable(fieldChild3);
		this.fieldChild4 = Optional.ofNullable(fieldChild4);
		this.fieldChild5 = Optional.ofNullable(fieldChild5);
		this.fieldChild6 = Optional.ofNullable(fieldChild6);
		this.fieldChild7 = Optional.ofNullable(fieldChild7);
		this.fieldChild8 = Optional.ofNullable(fieldChild8);
		this.fieldChild9 = Optional.ofNullable(fieldChild9);
		this.fieldChild10 = Optional.ofNullable(fieldChild10);
		this.historyCls = EnumAdaptor.valueOf(historyCls, HistoryDiviSion.class);
		this.canNotBeOld = Optional.ofNullable(canNotBeOld);
		this.selectionTargetForRes = Optional.ofNullable(selectionTargetForRes);
		this.clsKeyQuery1 = Optional.ofNullable(clsKeyQuery1);
		this.clsKeyQuery2 = Optional.ofNullable(clsKeyQuery2);
		this.clsKeyQuery3 = Optional.ofNullable(clsKeyQuery3);
		this.clsKeyQuery4 = Optional.ofNullable(clsKeyQuery4);
		this.clsKeyQuery5 = Optional.ofNullable(clsKeyQuery5);
		this.clsKeyQuery6 = Optional.ofNullable(clsKeyQuery6);
		this.clsKeyQuery7 = Optional.ofNullable(clsKeyQuery7);
		this.clsKeyQuery8 = Optional.ofNullable(clsKeyQuery8);
		this.clsKeyQuery9 = Optional.ofNullable(clsKeyQuery9);
		this.clsKeyQuery10 = Optional.ofNullable(clsKeyQuery10);
		this.fieldKeyQuery1 = Optional.ofNullable(fieldKeyQuery1);
		this.fieldKeyQuery2 = Optional.ofNullable(fieldKeyQuery2);
		this.fieldKeyQuery3 = Optional.ofNullable(fieldKeyQuery3);
		this.fieldKeyQuery4 = Optional.ofNullable(fieldKeyQuery4);
		this.fieldKeyQuery5 = Optional.ofNullable(fieldKeyQuery5);
		this.fieldKeyQuery6 = Optional.ofNullable(fieldKeyQuery6);
		this.fieldKeyQuery7 = Optional.ofNullable(fieldKeyQuery7);
		this.fieldKeyQuery8 = Optional.ofNullable(fieldKeyQuery8);
		this.fieldKeyQuery9 = Optional.ofNullable(fieldKeyQuery9);
		this.fieldKeyQuery10 = Optional.ofNullable(fieldKeyQuery10);
		this.defaultCondKeyQuery = Optional.ofNullable(defaultCondKeyQuery);
		this.fieldDate1 = Optional.ofNullable(fieldDate1);
		this.fieldDate2 = Optional.ofNullable(fieldDate2);
		this.fieldDate3 = Optional.ofNullable(fieldDate3);
		this.fieldDate4 = Optional.ofNullable(fieldDate4);
		this.fieldDate5 = Optional.ofNullable(fieldDate5);
		this.fieldDate6 = Optional.ofNullable(fieldDate6);
		this.fieldDate7 = Optional.ofNullable(fieldDate7);
		this.fieldDate8 = Optional.ofNullable(fieldDate8);
		this.fieldDate9 = Optional.ofNullable(fieldDate9);
		this.fieldDate10 = Optional.ofNullable(fieldDate10);
		this.fieldDate11 = Optional.ofNullable(fieldDate11);
		this.fieldDate12 = Optional.ofNullable(fieldDate12);
		this.fieldDate13 = Optional.ofNullable(fieldDate13);
		this.fieldDate14 = Optional.ofNullable(fieldDate14);
		this.fieldDate15 = Optional.ofNullable(fieldDate15);
		this.fieldDate16 = Optional.ofNullable(fieldDate16);
		this.fieldDate17 = Optional.ofNullable(fieldDate17);
		this.fieldDate18 = Optional.ofNullable(fieldDate18);
		this.fieldDate19 = Optional.ofNullable(fieldDate19);
		this.fieldDate20 = Optional.ofNullable(fieldDate20);
		this.filedKeyUpdate1 = Optional.ofNullable(filedKeyUpdate1);
		this.filedKeyUpdate2 = Optional.ofNullable(filedKeyUpdate2);
		this.filedKeyUpdate3 = Optional.ofNullable(filedKeyUpdate3);
		this.filedKeyUpdate4 = Optional.ofNullable(filedKeyUpdate4);
		this.filedKeyUpdate5 = Optional.ofNullable(filedKeyUpdate5);
		this.filedKeyUpdate6 = Optional.ofNullable(filedKeyUpdate6);
		this.filedKeyUpdate7 = Optional.ofNullable(filedKeyUpdate7);
		this.filedKeyUpdate8 = Optional.ofNullable(filedKeyUpdate8);
		this.filedKeyUpdate9 = Optional.ofNullable(filedKeyUpdate9);
		this.filedKeyUpdate10 = Optional.ofNullable(filedKeyUpdate10);
		this.filedKeyUpdate11 = Optional.ofNullable(filedKeyUpdate11);
		this.filedKeyUpdate12 = Optional.ofNullable(filedKeyUpdate12);
		this.filedKeyUpdate13 = Optional.ofNullable(filedKeyUpdate13);
		this.filedKeyUpdate14 = Optional.ofNullable(filedKeyUpdate14);
		this.filedKeyUpdate15 = Optional.ofNullable(filedKeyUpdate15);
		this.filedKeyUpdate16 = Optional.ofNullable(filedKeyUpdate16);
		this.filedKeyUpdate17 = Optional.ofNullable(filedKeyUpdate17);
		this.filedKeyUpdate18 = Optional.ofNullable(filedKeyUpdate18);
		this.filedKeyUpdate19 = Optional.ofNullable(filedKeyUpdate19);
		this.filedKeyUpdate20 = Optional.ofNullable(filedKeyUpdate20);
		this.screenRetentionPeriod = Optional.ofNullable(screenRetentionPeriod);
		this.supplementaryExplanation = Optional.ofNullable(supplementaryExplanation);
		this.parentTblJpName = Optional.ofNullable(parentTblJpName);
		this.hasParentTblFlg = EnumAdaptor.valueOf(hasParentTblFlg, NotUseAtr.class);
		this.parentTblName = Optional.ofNullable(parentTblName);
		this.fieldParent1 = Optional.ofNullable(fieldParent1);
		this.fieldParent2 = Optional.ofNullable(fieldParent2);
		this.fieldParent3 = Optional.ofNullable(fieldParent3);
		this.fieldParent4 = Optional.ofNullable(fieldParent4);
		this.fieldParent5 = Optional.ofNullable(fieldParent5);
		this.fieldParent6 = Optional.ofNullable(fieldParent6);
		this.fieldParent7 = Optional.ofNullable(fieldParent7);
		this.fieldParent8 = Optional.ofNullable(fieldParent8);
		this.fieldParent9 = Optional.ofNullable(fieldParent9);
		this.fieldParent10 = Optional.ofNullable(fieldParent10);
		this.surveyPreservation = EnumAdaptor.valueOf(surveyPreservation, NotUseAtr.class);
	}

	public TableList(String dataStorageProcessingId, String saveForm, String saveSetCode, String saveSetName,
			String supplementaryExplanation, String categoryId, String categoryName, TimeStore retentionPeriodCls,
			StorageRangeSaved storageRangeSaved, String screenRetentionPeriod, String referenceYear,
			String referenceMonth, NotUseAtr surveyPreservation, RecoverFormCompanyOther anotherComCls, int tableNo,
			String tableJapaneseName, String tableEnglishName, HistoryDiviSion historyCls, NotUseAtr hasParentTblFlg,
			String parentTblJpName, String parentTblName, String fieldParent1, String fieldParent2, String fieldParent3,
			String fieldParent4, String fieldParent5, String fieldParent6, String fieldParent7, String fieldParent8,
			String fieldParent9, String fieldParent10, String fieldChild1, String fieldChild2, String fieldChild3,
			String fieldChild4, String fieldChild5, String fieldChild6, String fieldChild7, String fieldChild8,
			String fieldChild9, String fieldChild10, String fieldAcqCid, String fieldAcqEmployeeId,
			String fieldAcqDateTime, String fieldAcqStartDate, String fieldAcqEndDate, String defaultCondKeyQuery,
			String fieldKeyQuery1, String fieldKeyQuery2, String fieldKeyQuery3, String fieldKeyQuery4,
			String fieldKeyQuery5, String fieldKeyQuery6, String fieldKeyQuery7, String fieldKeyQuery8,
			String fieldKeyQuery9, String fieldKeyQuery10, String clsKeyQuery1, String clsKeyQuery2,
			String clsKeyQuery3, String clsKeyQuery4, String clsKeyQuery5, String clsKeyQuery6, String clsKeyQuery7,
			String clsKeyQuery8, String clsKeyQuery9, String clsKeyQuery10, String filedKeyUpdate1,
			String filedKeyUpdate2, String filedKeyUpdate3, String filedKeyUpdate4, String filedKeyUpdate5,
			String filedKeyUpdate6, String filedKeyUpdate7, String filedKeyUpdate8, String filedKeyUpdate9,
			String filedKeyUpdate10, String filedKeyUpdate11, String filedKeyUpdate12, String filedKeyUpdate13,
			String filedKeyUpdate14, String filedKeyUpdate15, String filedKeyUpdate16, String filedKeyUpdate17,
			String filedKeyUpdate18, String filedKeyUpdate19, String filedKeyUpdate20, String fieldDate1,
			String fieldDate2, String fieldDate3, String fieldDate4, String fieldDate5, String fieldDate6,
			String fieldDate7, String fieldDate8, String fieldDate9, String fieldDate10, String fieldDate11,
			String fieldDate12, String fieldDate13, String fieldDate14, String fieldDate15, String fieldDate16,
			String fieldDate17, String fieldDate18, String fieldDate19, String fieldDate20, String saveDateFrom,
			String saveDateTo, String compressedFileName, String internalFileName, String dataRecoveryProcessId,
			int canNotBeOld, int selectionTargetForRes) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.dataStorageProcessingId = dataStorageProcessingId;
		this.dataRecoveryProcessId = Optional.ofNullable(dataRecoveryProcessId);
		this.tableNo = tableNo;
		this.tableJapaneseName = tableJapaneseName;
		this.tableEnglishName = tableEnglishName;
		this.fieldAcqCid = Optional.ofNullable(fieldAcqCid);
		this.fieldAcqDateTime = Optional.ofNullable(fieldAcqDateTime);
		this.fieldAcqEmployeeId = Optional.ofNullable(fieldAcqEmployeeId);
		this.fieldAcqEndDate = Optional.ofNullable(fieldAcqEndDate);
		this.fieldAcqStartDate = Optional.ofNullable(fieldAcqStartDate);
		this.saveSetCode = Optional.ofNullable(saveSetCode);
		this.saveSetName = saveSetName;
		this.saveForm = saveForm;
		this.saveDateFrom = Optional.ofNullable(saveDateFrom);
		this.saveDateTo = Optional.ofNullable(saveDateTo);
		this.storageRangeSaved = storageRangeSaved;
		this.retentionPeriodCls = retentionPeriodCls;
		this.internalFileName = internalFileName;
		this.anotherComCls = anotherComCls;
		this.referenceYear = Optional.ofNullable(referenceYear);
		this.referenceMonth = Optional.ofNullable(referenceMonth);
		this.compressedFileName = compressedFileName;
		this.fieldChild1 = Optional.ofNullable(fieldChild1);
		this.fieldChild2 = Optional.ofNullable(fieldChild2);
		this.fieldChild3 = Optional.ofNullable(fieldChild3);
		this.fieldChild4 = Optional.ofNullable(fieldChild4);
		this.fieldChild5 = Optional.ofNullable(fieldChild5);
		this.fieldChild6 = Optional.ofNullable(fieldChild6);
		this.fieldChild7 = Optional.ofNullable(fieldChild7);
		this.fieldChild8 = Optional.ofNullable(fieldChild8);
		this.fieldChild9 = Optional.ofNullable(fieldChild9);
		this.fieldChild10 = Optional.ofNullable(fieldChild10);
		this.historyCls = historyCls;
		this.canNotBeOld = Optional.ofNullable(canNotBeOld);
		this.selectionTargetForRes = Optional.ofNullable(selectionTargetForRes);
		this.clsKeyQuery1 = Optional.ofNullable(clsKeyQuery1);
		this.clsKeyQuery2 = Optional.ofNullable(clsKeyQuery2);
		this.clsKeyQuery3 = Optional.ofNullable(clsKeyQuery3);
		this.clsKeyQuery4 = Optional.ofNullable(clsKeyQuery4);
		this.clsKeyQuery5 = Optional.ofNullable(clsKeyQuery5);
		this.clsKeyQuery6 = Optional.ofNullable(clsKeyQuery6);
		this.clsKeyQuery7 = Optional.ofNullable(clsKeyQuery7);
		this.clsKeyQuery8 = Optional.ofNullable(clsKeyQuery8);
		this.clsKeyQuery9 = Optional.ofNullable(clsKeyQuery9);
		this.clsKeyQuery10 = Optional.ofNullable(clsKeyQuery10);
		this.fieldKeyQuery1 = Optional.ofNullable(fieldKeyQuery1);
		this.fieldKeyQuery2 = Optional.ofNullable(fieldKeyQuery2);
		this.fieldKeyQuery3 = Optional.ofNullable(fieldKeyQuery3);
		this.fieldKeyQuery4 = Optional.ofNullable(fieldKeyQuery4);
		this.fieldKeyQuery5 = Optional.ofNullable(fieldKeyQuery5);
		this.fieldKeyQuery6 = Optional.ofNullable(fieldKeyQuery6);
		this.fieldKeyQuery7 = Optional.ofNullable(fieldKeyQuery7);
		this.fieldKeyQuery8 = Optional.ofNullable(fieldKeyQuery8);
		this.fieldKeyQuery9 = Optional.ofNullable(fieldKeyQuery9);
		this.fieldKeyQuery10 = Optional.ofNullable(fieldKeyQuery10);
		this.defaultCondKeyQuery = Optional.ofNullable(defaultCondKeyQuery);
		this.fieldDate1 = Optional.ofNullable(fieldDate1);
		this.fieldDate2 = Optional.ofNullable(fieldDate2);
		this.fieldDate3 = Optional.ofNullable(fieldDate3);
		this.fieldDate4 = Optional.ofNullable(fieldDate4);
		this.fieldDate5 = Optional.ofNullable(fieldDate5);
		this.fieldDate6 = Optional.ofNullable(fieldDate6);
		this.fieldDate7 = Optional.ofNullable(fieldDate7);
		this.fieldDate8 = Optional.ofNullable(fieldDate8);
		this.fieldDate9 = Optional.ofNullable(fieldDate9);
		this.fieldDate10 = Optional.ofNullable(fieldDate10);
		this.fieldDate11 = Optional.ofNullable(fieldDate11);
		this.fieldDate12 = Optional.ofNullable(fieldDate12);
		this.fieldDate13 = Optional.ofNullable(fieldDate13);
		this.fieldDate14 = Optional.ofNullable(fieldDate14);
		this.fieldDate15 = Optional.ofNullable(fieldDate15);
		this.fieldDate16 = Optional.ofNullable(fieldDate16);
		this.fieldDate17 = Optional.ofNullable(fieldDate17);
		this.fieldDate18 = Optional.ofNullable(fieldDate18);
		this.fieldDate19 = Optional.ofNullable(fieldDate19);
		this.fieldDate20 = Optional.ofNullable(fieldDate20);
		this.filedKeyUpdate1 = Optional.ofNullable(filedKeyUpdate1);
		this.filedKeyUpdate2 = Optional.ofNullable(filedKeyUpdate2);
		this.filedKeyUpdate3 = Optional.ofNullable(filedKeyUpdate3);
		this.filedKeyUpdate4 = Optional.ofNullable(filedKeyUpdate4);
		this.filedKeyUpdate5 = Optional.ofNullable(filedKeyUpdate5);
		this.filedKeyUpdate6 = Optional.ofNullable(filedKeyUpdate6);
		this.filedKeyUpdate7 = Optional.ofNullable(filedKeyUpdate7);
		this.filedKeyUpdate8 = Optional.ofNullable(filedKeyUpdate8);
		this.filedKeyUpdate9 = Optional.ofNullable(filedKeyUpdate9);
		this.filedKeyUpdate10 = Optional.ofNullable(filedKeyUpdate10);
		this.filedKeyUpdate11 = Optional.ofNullable(filedKeyUpdate11);
		this.filedKeyUpdate12 = Optional.ofNullable(filedKeyUpdate12);
		this.filedKeyUpdate13 = Optional.ofNullable(filedKeyUpdate13);
		this.filedKeyUpdate14 = Optional.ofNullable(filedKeyUpdate14);
		this.filedKeyUpdate15 = Optional.ofNullable(filedKeyUpdate15);
		this.filedKeyUpdate16 = Optional.ofNullable(filedKeyUpdate16);
		this.filedKeyUpdate17 = Optional.ofNullable(filedKeyUpdate17);
		this.filedKeyUpdate18 = Optional.ofNullable(filedKeyUpdate18);
		this.filedKeyUpdate19 = Optional.ofNullable(filedKeyUpdate19);
		this.filedKeyUpdate20 = Optional.ofNullable(filedKeyUpdate20);
		this.screenRetentionPeriod = Optional.ofNullable(screenRetentionPeriod);
		this.supplementaryExplanation = Optional.ofNullable(supplementaryExplanation);
		this.parentTblJpName = Optional.ofNullable(parentTblJpName);
		this.hasParentTblFlg = hasParentTblFlg;
		this.parentTblName = Optional.ofNullable(parentTblName);
		this.fieldParent1 = Optional.ofNullable(fieldParent1);
		this.fieldParent2 = Optional.ofNullable(fieldParent2);
		this.fieldParent3 = Optional.ofNullable(fieldParent3);
		this.fieldParent4 = Optional.ofNullable(fieldParent4);
		this.fieldParent5 = Optional.ofNullable(fieldParent5);
		this.fieldParent6 = Optional.ofNullable(fieldParent6);
		this.fieldParent7 = Optional.ofNullable(fieldParent7);
		this.fieldParent8 = Optional.ofNullable(fieldParent8);
		this.fieldParent9 = Optional.ofNullable(fieldParent9);
		this.fieldParent10 = Optional.ofNullable(fieldParent10);
		this.surveyPreservation = surveyPreservation;
	}

	public static TableList createFromCsvData(List<String> tableListSetting) {
		TimeStore retentionPeriodCls = EnumAdaptor.valueOf(Integer.parseInt(tableListSetting.get(7)), TimeStore.class);
		StorageRangeSaved storageRangeSaved = EnumAdaptor.valueOf(Integer.parseInt(tableListSetting.get(8)),
				StorageRangeSaved.class);
		NotUseAtr surveyPreservation = EnumAdaptor.valueOf(Integer.parseInt(tableListSetting.get(12)), NotUseAtr.class);
		RecoverFormCompanyOther anotherComCls = EnumAdaptor.valueOf(Integer.parseInt(tableListSetting.get(13)),
				RecoverFormCompanyOther.class);
		HistoryDiviSion historyCls = EnumAdaptor.valueOf(Integer.parseInt(tableListSetting.get(17)),
				HistoryDiviSion.class);
		NotUseAtr hasParentTblFlg = EnumAdaptor.valueOf(Integer.parseInt(tableListSetting.get(18)), NotUseAtr.class);
		TableList tableListData = new TableList(
				tableListSetting.get(0), 
				tableListSetting.get(1),
				tableListSetting.get(2),
				tableListSetting.get(3),
				tableListSetting.get(4),
				tableListSetting.get(5),
				tableListSetting.get(6),
				retentionPeriodCls, 
				storageRangeSaved, 
				tableListSetting.get(9),
				tableListSetting.get(10), 
				tableListSetting.get(11), 
				surveyPreservation, anotherComCls,
				Integer.parseInt(tableListSetting.get(14)), 
				tableListSetting.get(15), tableListSetting.get(16),
				historyCls, hasParentTblFlg, tableListSetting.get(19), 
				tableListSetting.get(20),
				tableListSetting.get(21), 
				tableListSetting.get(22), tableListSetting.get(23), tableListSetting.get(24),
				tableListSetting.get(25), tableListSetting.get(26), tableListSetting.get(27), tableListSetting.get(28),
				tableListSetting.get(29), tableListSetting.get(30), tableListSetting.get(31), tableListSetting.get(32),
				tableListSetting.get(33), tableListSetting.get(34), tableListSetting.get(35), tableListSetting.get(36),
				tableListSetting.get(37), tableListSetting.get(38), tableListSetting.get(39), tableListSetting.get(40),
				tableListSetting.get(41), tableListSetting.get(42), tableListSetting.get(43), tableListSetting.get(44),
				tableListSetting.get(45), tableListSetting.get(46), tableListSetting.get(47), tableListSetting.get(48),
				tableListSetting.get(49), tableListSetting.get(50), tableListSetting.get(51), tableListSetting.get(52),
				tableListSetting.get(53), tableListSetting.get(54), tableListSetting.get(55), tableListSetting.get(56),
				tableListSetting.get(57), tableListSetting.get(58), tableListSetting.get(59), tableListSetting.get(60),
				tableListSetting.get(61), tableListSetting.get(62), tableListSetting.get(63), tableListSetting.get(64),
				tableListSetting.get(65), tableListSetting.get(66), tableListSetting.get(67), tableListSetting.get(68),
				tableListSetting.get(69), tableListSetting.get(70), tableListSetting.get(71), tableListSetting.get(72),
				tableListSetting.get(73), tableListSetting.get(74), tableListSetting.get(75), tableListSetting.get(76),
				tableListSetting.get(77), tableListSetting.get(78), tableListSetting.get(79), tableListSetting.get(80),
				tableListSetting.get(81), tableListSetting.get(82), tableListSetting.get(83), tableListSetting.get(84),
				tableListSetting.get(85), tableListSetting.get(86), tableListSetting.get(87), tableListSetting.get(88),
				tableListSetting.get(89), tableListSetting.get(90), tableListSetting.get(91), tableListSetting.get(92),
				tableListSetting.get(93), tableListSetting.get(94), tableListSetting.get(95), tableListSetting.get(96),
				tableListSetting.get(97), tableListSetting.get(98), tableListSetting.get(99), tableListSetting.get(100),
				tableListSetting.get(101), tableListSetting.get(102), tableListSetting.get(103),
				tableListSetting.get(104), tableListSetting.get(105), tableListSetting.get(106),
				tableListSetting.get(107), tableListSetting.get(108), tableListSetting.get(109),
				tableListSetting.get(110), tableListSetting.get(111), Integer.parseInt(tableListSetting.get(112)),
				Integer.parseInt(tableListSetting.get(113)));
		tableListData.setCanNotBeOld(Optional.of(1));
		tableListData.setSelectionTargetForRes(Optional.of(1));
		return tableListData;
	}

	public TableList(String categoryId, String categoryName, String saveSetCode, String saveSetName,
			String saveDateFrom, String saveDateTo, int storageRangeSaved, int retentionPeriodCls, int anotherComCls,
			String compressedFileName, int canNotBeOld, String supplementaryExplanation, String saveForm) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.saveSetCode = Optional.ofNullable(saveSetCode);
		this.saveSetName = saveSetName;
		this.saveDateFrom = Optional.ofNullable(saveDateFrom);
		this.saveDateTo = Optional.ofNullable(saveDateTo);
		this.storageRangeSaved = EnumAdaptor.valueOf(storageRangeSaved, StorageRangeSaved.class);
		this.retentionPeriodCls = EnumAdaptor.valueOf(retentionPeriodCls, TimeStore.class);
		this.anotherComCls = EnumAdaptor.valueOf(anotherComCls, RecoverFormCompanyOther.class);
		this.compressedFileName = compressedFileName;
		this.canNotBeOld = Optional.ofNullable(canNotBeOld);
		this.supplementaryExplanation = Optional.ofNullable(supplementaryExplanation);
		this.saveForm = saveForm;
	}
}
