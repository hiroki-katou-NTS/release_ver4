package nts.uk.screen.at.app.dailyperformance.correction.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import nts.arc.time.GeneralDate;
@Data
@AllArgsConstructor
public class ObjectShare {
	private Boolean changePeriodAtr;// 期間を変更する có cho thay đổi khoảng thời gian hay không
	private Boolean errorRefStartAtr; // エラー参照を起動する có hiện mode lỗi hay ko
	private String initClock; // 打刻初期値-社員ID Optional giờ check tay SPR
	private List<String> lstEmployee; // 社員一覧 社員ID danh sách nhân viên được chọn
	private Integer screenMode; // 画面モード-日別実績の修正の画面モード mode approval hay ko
	private String targetClosure; // 処理締め-締めID targetClosure lấy closureId
	private String transitionDesScreen; // 遷移先の画面 - Optional //truyền từ màn

	private GeneralDate dateTarget; // 日付別で起動- Optional ngày extract mode 2
	private Integer displayFormat; // 表示形式 mode hiển thị
	private GeneralDate individualStartUp; // 個人別で起動 ngày bắt đầu
	private List<String> lstExtratedEmployee;// 抽出した社員一覧
	private GeneralDate startDate;// 期間 khoảng thời gian
	private GeneralDate endDate;// 期間 khoảng thời gian
}
