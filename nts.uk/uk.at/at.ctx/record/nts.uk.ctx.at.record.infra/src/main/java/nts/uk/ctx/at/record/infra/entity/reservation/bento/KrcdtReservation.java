package nts.uk.ctx.at.record.infra.entity.reservation.bento;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.arc.time.GeneralDate;
import nts.gul.text.IdentifierUtil;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReservation;
import nts.uk.ctx.at.record.dom.reservation.bento.ReservationDate;
import nts.uk.ctx.at.record.dom.reservation.bento.ReservationRegisterInfo;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.closingtime.ReservationClosingTimeFrame;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCDT_RESERVATION")
@AllArgsConstructor
@NoArgsConstructor
public class KrcdtReservation extends UkJpaEntity {
	
	@EmbeddedId
	public KrcdtReservationPK pk;
	
	@Column(name = "CONTRACT_CD")
	public String contractCD;

	@Column(name = "RESERVATION_YMD")
	public GeneralDate date;
	
	@Column(name = "RESERVATION_FRAME")
	public Integer frameAtr;
	
	@Column(name = "CARD_NO")
	public String cardNo;
	
	@Column(name = "ORDERED")
	public boolean ordered;
	
	@OneToMany(targetEntity = KrcdtReservationDetail.class, mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "KRCDT_RESERVATION_DETAIL")
	public List<KrcdtReservationDetail> reservationDetails;
	
	@Override
	protected Object getKey() {
		return pk;
	}
	
	public BentoReservation toDomain() {
		return new BentoReservation(
				new ReservationRegisterInfo(cardNo), 
				new ReservationDate(date, EnumAdaptor.valueOf(frameAtr, ReservationClosingTimeFrame.class)), 
				ordered, 
				reservationDetails.stream().map(x -> x.toDomain()).collect(Collectors.toList()));
	}
	
	public static KrcdtReservation fromDomain(BentoReservation bentoReservation) {
		return new KrcdtReservation(
				new KrcdtReservationPK(
						AppContexts.user().companyId(), 
						IdentifierUtil.randomUniqueId()), 
				AppContexts.user().contractCode(), 
				bentoReservation.getReservationDate().getDate(), 
				bentoReservation.getReservationDate().getClosingTimeFrame().value, 
				bentoReservation.getRegisterInfor().getReservationCardNo(), 
				bentoReservation.isOrdered(), 
				bentoReservation.getBentoReservationDetails().stream().map(x -> KrcdtReservationDetail.fromDomain(x)).collect(Collectors.toList()));
	}
}
