package nts.uk.ctx.at.record.dom.reservation.bento;

import org.eclipse.persistence.internal.xr.ValueObject;

import lombok.Getter;

/**
 * 予約登録情報
 * @author Doan Duy Hung
 *
 */
public class ReservationRegisterInfo extends ValueObject {
	
	/**
	 * 予約者のカード番号
	 */
	@Getter
	private	final String reservationCardNo;
	
	public ReservationRegisterInfo(String reservationCardNo) {
		this.reservationCardNo = reservationCardNo;
	}
}
