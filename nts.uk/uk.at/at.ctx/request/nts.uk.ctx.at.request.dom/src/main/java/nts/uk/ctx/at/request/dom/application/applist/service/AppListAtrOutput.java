package nts.uk.ctx.at.request.dom.application.applist.service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
/**
 * output 5 - 申請一覧リスト取得実績
 * @author hoatt
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppListAtrOutput {

	private List<ApplicationFullOutput> lstAppFull;
	private ApplicationStatus appStatus;
	private List<CheckColorTime> lstAppColor;
	private List<AppPrePostGroup> lstAppGroup;
}
