package nts.uk.ctx.at.request.app.find.application.holidayshipment.dto;
/**
 * 振出の未使用
 * @author do_dt
 *
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.UnUseOfRec;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnUseOfRecDto {
	/**
	 * 使用期限日
	 */
	private String expirationDate;
	/**
	 * 振出データID
	 */
	private String recMngId;
	/**
	 * 発生日数
	 */
	private double occurrenceDays;
	/**
	 * 法定内外区分
	 */
	private Integer statutoryAtr;
	/**
	 * 未使用日数
	 */
	private double unUseDays;
	/**	代休消化区分 */
	private Integer digestionAtr;
	/**	消滅日 */
	private String disappearanceDate;
	/**
	 * 使用開始日
	 */
	private String startDate;
	
	public UnUseOfRecDto(UnUseOfRec unUseOfRec) {
		super();
		this.expirationDate = unUseOfRec.getExpirationDate().toString("yyyy/MM/dd");
		this.recMngId = unUseOfRec.getRecMngId();
		this.occurrenceDays = unUseOfRec.getOccurrenceDays();
		this.statutoryAtr = unUseOfRec.getStatutoryAtr().value;
		this.unUseDays = unUseOfRec.getUnUseDays();
		this.digestionAtr = unUseOfRec.getDigestionAtr().value;
		this.disappearanceDate = unUseOfRec.getDisappearanceDate().map(x -> x.toString("yyyy/MM/dd")).orElse(null);
		this.startDate = unUseOfRec.getStartDate().map(x -> x.toString("yyyy/MM/dd")).orElse(null);
	}
	
	
}