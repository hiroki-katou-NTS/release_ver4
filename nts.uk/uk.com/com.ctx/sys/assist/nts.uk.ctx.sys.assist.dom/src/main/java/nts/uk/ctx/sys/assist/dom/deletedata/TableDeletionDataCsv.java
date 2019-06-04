/**
 * 
 */
package nts.uk.ctx.sys.assist.dom.deletedata;

import java.util.Optional;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nam.lh
 *
 */
@NoArgsConstructor
@Data
public class TableDeletionDataCsv {

	public String delId;//データ保存処理ID
	private int delType;//保存形態
	private String delCode;//保存セットコード
	private String delName;//保存する名称
	private String supplementExplanation;

	private String categoryId;//カテゴリID
	private int hasParentTblFlg; //するしない区分
	private int historyCls;//履歴区分
	private String tableEnglishName;//テーブル物理名
	private String tableJapanName; //テーブル日本語名
	private int tableNo;//テーブルNo
	
	private Optional<String> defaultCondKeyQuery;//抽出キー条件固定
	private Optional<String> fieldAcqCid;//付加取得項目_会社ID
	private Optional<String> fieldAcqDateTime;//付加取得項目_日付
	private Optional<String> fieldAcqEmployeeId;//付加取得項目_社員ID
	private Optional<String> fieldAcqEndDate; //付加取得項目_終了日付
	private Optional<String> fieldAcqStartDate; //付加取得項目_開始日付

	
	private Optional<String> clsKeyQuery1;//抽出キー区分1
	private Optional<String> clsKeyQuery2;//抽出キー区分2
	private Optional<String> clsKeyQuery3;//抽出キー区分3
	private Optional<String> clsKeyQuery4;//抽出キー区分4
	private Optional<String> clsKeyQuery5;//抽出キー区分5
	private Optional<String> clsKeyQuery6;//抽出キー区分6
	private Optional<String> clsKeyQuery7;//抽出キー区分7
	private Optional<String> clsKeyQuery8;//抽出キー区分8
	private Optional<String> clsKeyQuery9;//抽出キー区分9
	private Optional<String> clsKeyQuery10;//抽出キー区分10
	
	private Optional<String> fieldKeyQuery1; //抽出キー項目1
	private Optional<String> fieldKeyQuery2;//抽出キー項目2
	private Optional<String> fieldKeyQuery3;//抽出キー項目3
	private Optional<String> fieldKeyQuery4;//抽出キー項目4
	private Optional<String> fieldKeyQuery5;//抽出キー項目5
	private Optional<String> fieldKeyQuery6;//抽出キー項目6
	private Optional<String> fieldKeyQuery7;//抽出キー項目7
	private Optional<String> fieldKeyQuery8;//抽出キー項目8
	private Optional<String> fieldKeyQuery9;//抽出キー項目9
	private Optional<String> fieldKeyQuery10;//抽出キー項目10
	
	private Optional<String> fieldParent1;//親側結合キー1
	private Optional<String> fieldParent2;//親側結合キー2
	private Optional<String> fieldParent3;//親側結合キー3
	private Optional<String> fieldParent4;//親側結合キー4
	private Optional<String> fieldParent5;//親側結合キー5
	private Optional<String> fieldParent6;//親側結合キー6
	private Optional<String> fieldParent7;//親側結合キー7
	private Optional<String> fieldParent8;//親側結合キー8
	private Optional<String> fieldParent9;//親側結合キー9
	private Optional<String> fieldParent10;//親側結合キー10
	
	private Optional<String> fieldChild1;//子側結合キー1
	private Optional<String> fieldChild2;//子側結合キー2
	private Optional<String> fieldChild3;//子側結合キー3
	private Optional<String> fieldChild4;//子側結合キー4
	private Optional<String> fieldChild5;//子側結合キー5
	private Optional<String> fieldChild6;//子側結合キー6
	private Optional<String> fieldChild7;//子側結合キー7
	private Optional<String> fieldChild8;//子側結合キー8
	private Optional<String> fieldChild9;//子側結合キー9
	private Optional<String> fieldChild10;//子側結合キー10
	
	
	private Optional<String> fieldDate1;//日付項目1
	private Optional<String> fieldDate2;//日付項目2
	private Optional<String> fieldDate3;//日付項目3
	private Optional<String> fieldDate4;//日付項目4
	private Optional<String> fieldDate5;//日付項目5
	private Optional<String> fieldDate6;//日付項目6
	private Optional<String> fieldDate7;//日付項目7
	private Optional<String> fieldDate8;//日付項目8
	private Optional<String> fieldDate9;//日付項目9
	private Optional<String> fieldDate10;//日付項目10
	private Optional<String> fieldDate11;//日付項目11
	private Optional<String> fieldDate12;//日付項目12
	private Optional<String> fieldDate13;//日付項目13
	private Optional<String> fieldDate14;//日付項目14
	private Optional<String> fieldDate15;//日付項目15
	private Optional<String> fieldDate16; //日付項目16
	private Optional<String> fieldDate17; //日付項目17
	private Optional<String> fieldDate18; //日付項目18
	private Optional<String> fieldDate19; //日付項目19
	private Optional<String> fieldDate20; //日付項目20
		
	private Optional<String> filedKeyUpdate1;//更新キー項目1
	private Optional<String> filedKeyUpdate2;//更新キー項目2
	private Optional<String> filedKeyUpdate3;//更新キー項目3
	private Optional<String> filedKeyUpdate4;//更新キー項目4
	private Optional<String> filedKeyUpdate5;//更新キー項目5
	private Optional<String> filedKeyUpdate6;//更新キー項目6
	private Optional<String> filedKeyUpdate7;//更新キー項目7
	private Optional<String> filedKeyUpdate8;//更新キー項目8
	private Optional<String> filedKeyUpdate9;//更新キー項目9
	private Optional<String> filedKeyUpdate10;//更新キー項目10
	private Optional<String> filedKeyUpdate11;//更新キー項目11
	private Optional<String> filedKeyUpdate12;//更新キー項目12
	private Optional<String> filedKeyUpdate13;//更新キー項目13
	private Optional<String> filedKeyUpdate14;//更新キー項目14
	private Optional<String> filedKeyUpdate15;//更新キー項目15
	private Optional<String> filedKeyUpdate16;//更新キー項目16
	private Optional<String> filedKeyUpdate17;//更新キー項目17
	private Optional<String> filedKeyUpdate18;//更新キー項目18
	private Optional<String> filedKeyUpdate19;//更新キー項目19
	private Optional<String> filedKeyUpdate20;//更新キー項目20
	
	
	private Optional<String> parentTblName; //テーブル物理名
	private Optional<String> parentTblJapanName;//テーブル日本語名

	private String categoryName;//カテゴリ名
	private int timeStore;//保存期間区分
	private int recoveryStorageRange;//保存範囲
	private String retentionPeriod;//画面保存期間
	private String referenceYear;//参照年
	private String referenceMonth;//参照月
	private int saveForInvest;//調査用保存
	private int otherCompanyCls;//別会社区分
	private int categoryConstitutionNo;//日次削除開始日
	private String startDateOfDaily;//カテゴリ構成No
	private String endDateOfDaily;//日次削除終了日
	private String startMonthOfMonthly;//月次削除開始月
	private String endMonthOfMonthly;//月次削除終了月
	private String startYearOfMonthly;//年次開始年
	private String endYearOfMonthly;//年次終了年
	private String companyId; //会社ID
	private String internalFileName;
	private String compressedFileName;
	
	/**
	 * @param delId
	 * @param delType
	 * @param delCode
	 * @param delName
	 * @param supplementExplanation
	 * @param categoryId
	 * @param categoryName
	 * @param timeStore
	 * @param recoveryStorageRange
	 * @param saveForInvest
	 * @param otherCompanyCls
	 * @param tableJapanName
	 * @param tableEnglishName
	 * @param historyCls
	 * @param defaultCondKeyQuery
	 * 
	 * @param fieldKeyQuery1
	 * @param fieldKeyQuery2
	 * @param fieldKeyQuery3
	 * @param fieldKeyQuery4
	 * @param fieldKeyQuery5
	 * @param fieldKeyQuery6
	 * @param fieldKeyQuery7
	 * @param fieldKeyQuery8
	 * @param fieldKeyQuery9
	 * @param fieldKeyQuery10
	 * 
	 * @param clsKeyQuery1
	 * @param clsKeyQuery2
	 * @param clsKeyQuery3
	 * @param clsKeyQuery4
	 * @param clsKeyQuery5
	 * @param clsKeyQuery6
	 * @param clsKeyQuery7
	 * @param clsKeyQuery8
	 * @param clsKeyQuery9
	 * @param clsKeyQuery10
	 * 
	 * @param filedKeyUpdate1
	 * @param filedKeyUpdate2
	 * @param filedKeyUpdate3
	 * @param filedKeyUpdate4
	 * @param filedKeyUpdate5
	 * @param filedKeyUpdate6
	 * @param filedKeyUpdate7
	 * @param filedKeyUpdate8
	 * @param filedKeyUpdate9
	 * @param filedKeyUpdate10
	 * @param filedKeyUpdate11
	 * @param filedKeyUpdate12
	 * @param filedKeyUpdate13
	 * @param filedKeyUpdate14
	 * @param filedKeyUpdate15
	 * @param filedKeyUpdate16
	 * @param filedKeyUpdate17
	 * @param filedKeyUpdate18
	 * @param filedKeyUpdate19
	 * @param filedKeyUpdate20
	 * 
	 * @param fieldDate1
	 * @param fieldDate2
	 * @param fieldDate3
	 * @param fieldDate4
	 * @param fieldDate5
	 * @param fieldDate6
	 * @param fieldDate7
	 * @param fieldDate8
	 * @param fieldDate9
	 * @param fieldDate10
	 * @param fieldDate11
	 * @param fieldDate12
	 * @param fieldDate13
	 * @param fieldDate14
	 * @param fieldDate15
	 * @param fieldDate16
	 * @param fieldDate17
	 * @param fieldDate18
	 * @param fieldDate19
	 * @param fieldDate20
	 * 
	 * @param hasParentTblFlg
	 * @param parentTblName
	 * @param parentTblJapanName
	 * @param fieldAcqDateTime
	 * @param fieldAcqEndDate
	 * @param fieldAcqEmployeeId
	 * @param fieldAcqStartDate
	 * @param fieldAcqCid
	 * @param fieldParent1
	 * @param fieldParent2
	 * @param fieldParent3
	 * @param fieldParent4
	 * @param fieldParent5
	 * @param fieldParent6
	 * @param fieldParent7
	 * @param fieldParent8
	 * @param fieldParent9
	 * @param fieldParent10
	 * @param fieldChild1
	 * @param fieldChild2
	 * @param fieldChild3
	 * @param fieldChild4
	 * @param fieldChild5
	 * @param fieldChild6
	 * @param fieldChild7
	 * @param fieldChild8
	 * @param fieldChild9
	 * @param fieldChild10
	 * @param endDateOfDaily
	 * @param startMonthOfMonthly
	 * @param endMonthOfMonthly
	 * @param startYearOfMonthly
	 * @param endYearOfMonthly
	 */
	public TableDeletionDataCsv(String delId, int delType, String delCode, String delName, String supplementExplanation,
			String categoryId, String categoryName, int timeStore, int recoveryStorageRange, 
			int otherCompanyCls, String tableJapanName, String tableEnglishName, int historyCls,
			String defaultCondKeyQuery, String fieldKeyQuery1, String fieldKeyQuery2, String fieldKeyQuery3,
			String fieldKeyQuery4, String fieldKeyQuery5, String fieldKeyQuery6, String fieldKeyQuery7,
			String fieldKeyQuery8, String fieldKeyQuery9, String fieldKeyQuery10, String clsKeyQuery1,
			String clsKeyQuery2, String clsKeyQuery3, String clsKeyQuery4, String clsKeyQuery5, String clsKeyQuery6,
			String clsKeyQuery7, String clsKeyQuery8, String clsKeyQuery9, String clsKeyQuery10, String filedKeyUpdate1,
			String filedKeyUpdate2, String filedKeyUpdate3, String filedKeyUpdate4, String filedKeyUpdate5,
			String filedKeyUpdate6, String filedKeyUpdate7, String filedKeyUpdate8, String filedKeyUpdate9,
			String filedKeyUpdate10, String filedKeyUpdate11, String filedKeyUpdate12, String filedKeyUpdate13,
			String filedKeyUpdate14, String filedKeyUpdate15, String filedKeyUpdate16, String filedKeyUpdate17,
			String filedKeyUpdate18, String filedKeyUpdate19, String filedKeyUpdate20, String fieldDate1,
			String fieldDate2, String fieldDate3, String fieldDate4, String fieldDate5, String fieldDate6,
			String fieldDate7, String fieldDate8, String fieldDate9, String fieldDate10, String fieldDate11,
			String fieldDate12, String fieldDate13, String fieldDate14, String fieldDate15, String fieldDate16,
			String fieldDate17, String fieldDate18, String fieldDate19, String fieldDate20, int hasParentTblFlg,
			String parentTblName, String parentTblJapanName, String fieldAcqDateTime, String fieldAcqEndDate,
			String fieldAcqEmployeeId, String fieldAcqStartDate, String fieldAcqCid, String fieldParent1,
			String fieldParent2, String fieldParent3, String fieldParent4, String fieldParent5, String fieldParent6,
			String fieldParent7, String fieldParent8, String fieldParent9, String fieldParent10, String fieldChild1,
			String fieldChild2, String fieldChild3, String fieldChild4, String fieldChild5, String fieldChild6,
			String fieldChild7, String fieldChild8, String fieldChild9, String fieldChild10, 
			String startDateOfDaily, String endDateOfDaily, String startMonthOfMonthly, 
			String endMonthOfMonthly, String startYearOfMonthly, String endYearOfMonthly, String companyId , int tableNo,
			String compressedFileName , String internalFileName ) {

		this.delId = delId;
		this.delType = delType;
		this.delCode = delCode;
		this.delName = delName;
		this.supplementExplanation = supplementExplanation;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.timeStore = timeStore;
		this.tableNo = tableNo;
		this.recoveryStorageRange = recoveryStorageRange;
//		this.saveForInvest = saveForInvest;
		this.otherCompanyCls = otherCompanyCls;
		this.tableJapanName = tableJapanName;
		this.tableEnglishName = tableEnglishName;
		this.historyCls = historyCls;
		this.defaultCondKeyQuery = Optional.of(defaultCondKeyQuery);
		this.fieldKeyQuery1 = Optional.of(fieldKeyQuery1);
		this.fieldKeyQuery2 = Optional.of(fieldKeyQuery2);
		this.fieldKeyQuery3 = Optional.of(fieldKeyQuery3);
		this.fieldKeyQuery4 = Optional.of(fieldKeyQuery4);
		this.fieldKeyQuery5 = Optional.of(fieldKeyQuery5);
		this.fieldKeyQuery6 = Optional.of(fieldKeyQuery6);
		this.fieldKeyQuery7 = Optional.of(fieldKeyQuery7);
		this.fieldKeyQuery8 = Optional.of(fieldKeyQuery8);
		this.fieldKeyQuery9 = Optional.of(fieldKeyQuery9);
		this.fieldKeyQuery10 = Optional.of(fieldKeyQuery10);
		this.clsKeyQuery1 = Optional.of(clsKeyQuery1);
		this.clsKeyQuery2 = Optional.of(clsKeyQuery2);
		this.clsKeyQuery3 = Optional.of(clsKeyQuery3);
		this.clsKeyQuery4 = Optional.of(clsKeyQuery4);
		this.clsKeyQuery5 = Optional.of(clsKeyQuery5);
		this.clsKeyQuery6 = Optional.of(clsKeyQuery6);
		this.clsKeyQuery7 = Optional.of(clsKeyQuery7);
		this.clsKeyQuery8 = Optional.of(clsKeyQuery8);
		this.clsKeyQuery9 = Optional.of(clsKeyQuery9);
		this.clsKeyQuery10 = Optional.of(clsKeyQuery10);
		this.filedKeyUpdate1 = Optional.of(filedKeyUpdate1);
		this.filedKeyUpdate2 = Optional.of(filedKeyUpdate2);
		this.filedKeyUpdate3 = Optional.of(filedKeyUpdate3);
		this.filedKeyUpdate4 = Optional.of(filedKeyUpdate4);
		this.filedKeyUpdate5 = Optional.of(filedKeyUpdate5);
		this.filedKeyUpdate6 = Optional.of(filedKeyUpdate6);
		this.filedKeyUpdate7 = Optional.of(filedKeyUpdate7);
		this.filedKeyUpdate8 = Optional.of(filedKeyUpdate8);
		this.filedKeyUpdate9 = Optional.of(filedKeyUpdate9);
		this.filedKeyUpdate10 = Optional.of(filedKeyUpdate10);
		this.filedKeyUpdate11 = Optional.of(filedKeyUpdate11);
		this.filedKeyUpdate12 = Optional.of(filedKeyUpdate12);
		this.filedKeyUpdate13 = Optional.of(filedKeyUpdate13);
		this.filedKeyUpdate14 = Optional.of(filedKeyUpdate14);
		this.filedKeyUpdate15 = Optional.of(filedKeyUpdate15);
		this.filedKeyUpdate16 = Optional.of(filedKeyUpdate16);
		this.filedKeyUpdate17 = Optional.of(filedKeyUpdate17);
		this.filedKeyUpdate18 = Optional.of(filedKeyUpdate18);
		this.filedKeyUpdate19 = Optional.of(filedKeyUpdate19);
		this.filedKeyUpdate20 = Optional.of(filedKeyUpdate20);
		this.fieldDate1 = Optional.of(fieldDate1);
		this.fieldDate2 = Optional.of(fieldDate2);
		this.fieldDate3 = Optional.of(fieldDate3);
		this.fieldDate4 = Optional.of(fieldDate4);
		this.fieldDate5 = Optional.of(fieldDate5);
		this.fieldDate6 = Optional.of(fieldDate6);
		this.fieldDate7 = Optional.of(fieldDate7);
		this.fieldDate8 = Optional.of(fieldDate8);
		this.fieldDate9 = Optional.of(fieldDate9);
		this.fieldDate10 = Optional.of(fieldDate10);
		this.fieldDate11 = Optional.of(fieldDate11);
		this.fieldDate12 = Optional.of(fieldDate12);
		this.fieldDate13 = Optional.of(fieldDate13);
		this.fieldDate14 = Optional.of(fieldDate14);
		this.fieldDate15 = Optional.of(fieldDate15);
		this.fieldDate16 = Optional.of(fieldDate16);
		this.fieldDate17 = Optional.of(fieldDate17);
		this.fieldDate18 = Optional.of(fieldDate18);
		this.fieldDate19 = Optional.of(fieldDate19);
		this.fieldDate20 = Optional.of(fieldDate20);
		this.hasParentTblFlg = hasParentTblFlg;
		this.parentTblName = Optional.of(parentTblName);
		this.parentTblJapanName = Optional.of(parentTblJapanName);
		this.fieldAcqDateTime = Optional.of(fieldAcqDateTime);
		this.fieldAcqEndDate = Optional.of(fieldAcqEndDate);
		this.fieldAcqEmployeeId = Optional.of(fieldAcqEmployeeId);
		this.fieldAcqStartDate = Optional.of(fieldAcqStartDate);
		this.fieldAcqCid = Optional.of(fieldAcqCid);
		this.fieldParent1 = Optional.of(fieldParent1);
		this.fieldParent2 = Optional.of(fieldParent2);
		this.fieldParent3 = Optional.of(fieldParent3);
		this.fieldParent4 = Optional.of(fieldParent4);
		this.fieldParent5 = Optional.of(fieldParent5);
		this.fieldParent6 = Optional.of(fieldParent6);
		this.fieldParent7 = Optional.of(fieldParent7);
		this.fieldParent8 = Optional.of(fieldParent8);
		this.fieldParent9 = Optional.of(fieldParent9);
		this.fieldParent10 = Optional.of(fieldParent10);
		this.fieldChild1 = Optional.of(fieldChild1);
		this.fieldChild2 = Optional.of(fieldChild2);
		this.fieldChild3 = Optional.of(fieldChild3);
		this.fieldChild4 = Optional.of(fieldChild4);
		this.fieldChild5 = Optional.of(fieldChild5);
		this.fieldChild6 = Optional.of(fieldChild6);
		this.fieldChild7 = Optional.of(fieldChild7);
		this.fieldChild8 = Optional.of(fieldChild8);
		this.fieldChild9 = Optional.of(fieldChild9);
		this.fieldChild10 = Optional.of(fieldChild10);
		this.startDateOfDaily = startDateOfDaily;
		this.endDateOfDaily = endDateOfDaily;
		this.startMonthOfMonthly = startMonthOfMonthly;
		this.endMonthOfMonthly = endMonthOfMonthly;
		this.startYearOfMonthly = startYearOfMonthly;
		this.endYearOfMonthly = endYearOfMonthly;
		this.companyId = companyId;
		this.compressedFileName = compressedFileName;
		this.internalFileName = internalFileName;
	}
	
	public boolean hasParentTblFlg() {
		return this.hasParentTblFlg == 1;
	}
}
