package nts.uk.ctx.sys.portal.infra.entity.standardwidget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget.ApplicationStatusDetailedSetting;
import nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget.ApprovedAppStatusDetailedSetting;
import nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget.ApprovedApplicationStatusItem;
import nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget.StandardWidget;
import nts.uk.shr.com.enumcommon.NotUseAtr;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;

/**
 * 
 * @author tutt
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SPTMT_WIDGET_APPROVE")
public class SptmtApproveWidget extends ContractUkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 会社ID
	 */
	@Id
	@Column(name = "CID")
	public String companyId;

	/**
	 * 名称
	 */
	@Column(name = "TOPPAGE_PART_NAME")
	public String topPagePartName;

	/**
	 * 承認すべき申請データの表示区分
	 */
	@Column(name = "APP_DISPLAY_ATR")
	public Integer appDisplayAtr;

	/**
	 * 日別実績承認すべきデータの表示区分
	 */
	@Column(name = "DAY_DISPLAY_ATR")
	public Integer dayDisplayAtr;

	/**
	 * 月別実績承認すべきデータの表示区分
	 */
	@Column(name = "MON_DISPLAY_ATR")
	public Integer monDisplayAtr;

	/**
	 * ３６協定承認すべき申請データの表示区分
	 */
	@Column(name = "AGR_DISPLAY_ATR")
	public Integer agrDisplayAtr;

	@Override
	protected Object getKey() {
		return this.companyId;
	}

	public StandardWidget toDomain() {

		StandardWidget standardWidget = new StandardWidget(companyId, "", null, null, null, null);
		
		List<ApprovedAppStatusDetailedSetting> approvedAppStatusDetailedSettings = new ArrayList<>();

		ApprovedAppStatusDetailedSetting appDisplaySetting = new ApprovedAppStatusDetailedSetting(
				NotUseAtr.valueOf(this.appDisplayAtr),
				ApprovedApplicationStatusItem.APPLICATION_DATA);
		ApprovedAppStatusDetailedSetting dayDisplaySetting = new ApprovedAppStatusDetailedSetting(
				NotUseAtr.valueOf(this.dayDisplayAtr),
				ApprovedApplicationStatusItem.DAILY_PERFORMANCE_DATA);
		ApprovedAppStatusDetailedSetting monDisplaySetting = new ApprovedAppStatusDetailedSetting(
				NotUseAtr.valueOf(this.monDisplayAtr),
				ApprovedApplicationStatusItem.MONTHLY_RESULT_DATA);
		ApprovedAppStatusDetailedSetting agrDisplaySetting = new ApprovedAppStatusDetailedSetting(
				NotUseAtr.valueOf(this.agrDisplayAtr),
				ApprovedApplicationStatusItem.AGREEMENT_APPLICATION_DATA);

		approvedAppStatusDetailedSettings.add(appDisplaySetting);
		approvedAppStatusDetailedSettings.add(dayDisplaySetting);
		approvedAppStatusDetailedSettings.add(monDisplaySetting);
		approvedAppStatusDetailedSettings.add(agrDisplaySetting);

		standardWidget.setName(this.getTopPagePartName());
		standardWidget.setApprovedAppStatusDetailedSettingList(approvedAppStatusDetailedSettings);
		return standardWidget;
	}

	public static void toEntity(SptmtApproveWidget approveWidgeEntity, StandardWidget standardWidget) {

		List<ApplicationStatusDetailedSetting> appStatusDetailedSettings = standardWidget
				.getAppStatusDetailedSettingList();

		approveWidgeEntity.setTopPagePartName(standardWidget.getName().v());

		appStatusDetailedSettings.forEach(setting -> {

			if (setting.getItem().value == ApprovedApplicationStatusItem.APPLICATION_DATA.value) {
				approveWidgeEntity.setAppDisplayAtr(setting.getDisplayType().value);
			}

			if (setting.getItem().value == ApprovedApplicationStatusItem.DAILY_PERFORMANCE_DATA.value) {
				approveWidgeEntity.setDayDisplayAtr(setting.getDisplayType().value);
			}

			if (setting.getItem().value == ApprovedApplicationStatusItem.MONTHLY_RESULT_DATA.value) {
				approveWidgeEntity.setMonDisplayAtr(setting.getDisplayType().value);
			}

			if (setting.getItem().value == ApprovedApplicationStatusItem.AGREEMENT_APPLICATION_DATA.value) {
				approveWidgeEntity.setAgrDisplayAtr(setting.getDisplayType().value);
			}

		});

	}
}
