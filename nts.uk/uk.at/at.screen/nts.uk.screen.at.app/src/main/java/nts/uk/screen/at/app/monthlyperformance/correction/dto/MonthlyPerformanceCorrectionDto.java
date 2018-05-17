package nts.uk.screen.at.app.monthlyperformance.correction.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import nts.uk.ctx.at.record.app.find.monthly.root.dto.ClosureDateDto;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.FormatPerformanceDto;
import nts.uk.ctx.at.record.app.find.workrecord.operationsetting.IdentityProcessDto;
import nts.uk.ctx.at.shared.dom.attendance.util.item.ItemValue;
import nts.uk.screen.at.app.monthlyperformance.correction.param.MonthlyPerformanceParam;

/**
 * TODO
 */
@Getter
@Setter
public class MonthlyPerformanceCorrectionDto {

	private Set<ItemValue> itemValues;
	private MPControlDisplayItem lstControlDisplayItem;
	private String employmentCode;
	private List<MonthlyPerformanceEmployeeDto> lstEmployee;
	private List<MPDataDto> lstData;
	private Map<String, String > data;
	private List<MPCellStateDto> lstCellState;
	/**
	 * 本人確認処理の利用設定
	 */
	private IdentityProcessDto identityProcess;
	/**
	 * 実績修正画面で利用するフォーマット
	 */
	private FormatPerformanceDto formatPerformance;
	/**
	 * ログイン社員の月別実績の権限を取得する
	 */
	private List<MonthlyPerformanceAuthorityDto> authorityDto;
	/**
	 * list fixed header
	 */
	private List<MPHeaderDto> lstFixedHeader;
	
	/**
	 * コメント
	 */
	private String comment;
	/**
	 * 処理年月
	 * YYYYMM
	 */
	private int processDate;
	
	/**
	 * 締め名称
	 * 画面項目「A4_2：対象締め日」
	 */
	private String closureName;
	/** Hidden closureId*/
	private Integer closureId;
	/**
	 * ・実績期間：List＜実績期間＞
	 * 画面項目「A4_5：実績期間選択肢」
	 */
	private List<ActualTime> lstActualTimes;
	/**
	 * 期間：取得した期間に一致する
	 * ※一致する期間がない場合は、先頭を選択状態にする
	 */
	private ActualTime selectedActualTime;
	/**
	 * 画面項目の非活制御をする
	 */
	private int actualTimeState;

	/**
	 * パラメータ
	 */
	private MonthlyPerformanceParam param;
	/**
	 * 締め日: 日付
	 */
	private ClosureDateDto closureDate;
	
	public MonthlyPerformanceCorrectionDto(){
		super();
		this.lstFixedHeader = MPHeaderDto.GenerateFixedHeader();
		this.lstData = new ArrayList<>();
		this.lstCellState = new ArrayList<>();
		this.lstControlDisplayItem = new MPControlDisplayItem();
		this.itemValues = new HashSet<>();
		this.data = new HashMap<>();
		
	}
}
