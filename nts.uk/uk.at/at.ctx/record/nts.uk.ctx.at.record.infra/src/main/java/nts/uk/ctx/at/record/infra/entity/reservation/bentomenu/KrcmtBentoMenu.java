package nts.uk.ctx.at.record.infra.entity.reservation.bentomenu;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import nts.uk.ctx.at.record.dom.reservation.bento.BentoReservationTime;
import nts.uk.ctx.at.record.dom.reservation.bento.rules.BentoReservationTimeName;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.BentoMenu;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.closingtime.BentoReservationClosingTime;
import nts.uk.ctx.at.record.dom.reservation.bentomenu.closingtime.ReservationClosingTime;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@Entity
@Table(name = "KRCMT_BENTO_MENU")
@AllArgsConstructor
public class KrcmtBentoMenu extends UkJpaEntity {
	
	@EmbeddedId
	public KrcmtBentoMenuPK pk;
	
	@Column(name = "CONTRACT_CD")
	public String contractCD;
	
	@Column(name = "RESERVATION_FRAME1_NAME")
	public String reservationFrameName1;
	
	@Column(name = "RESERVATION_FRAME1_START_TIME")
	public Integer reservationStartTime1;
	
	@Column(name = "RESERVATION_FRAME1_END_TIME")
	public Integer reservationEndTime1;
	
	@Column(name = "RESERVATION_FRAME2_NAME")
	public String reservationFrameName2;
	
	@Column(name = "RESERVATION_FRAME2_START_TIME")
	public Integer reservationStartTime2;
	
	@Column(name = "RESERVATION_FRAME2_END_TIME")
	public Integer reservationEndTime2;

	@OneToMany(targetEntity = KrcmtBento.class, mappedBy = "bentoMenu", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "KRCMT_BENTO")
	public List<KrcmtBento> bentos;
	
	@Override
	protected Object getKey() {
		return pk;
	}
	
	public BentoMenu toDomain() {
		Optional<ReservationClosingTime> closingTime2 = Optional.empty();
		if(reservationStartTime2!=null) {
			closingTime2 = Optional.of(new ReservationClosingTime(
					new BentoReservationTimeName(reservationFrameName2), 
					new BentoReservationTime(reservationEndTime2), 
					reservationStartTime1==null ? Optional.empty() : Optional.of(new BentoReservationTime(reservationStartTime2))));
		}
		return new BentoMenu(
				pk.histID, 
				bentos.stream().map(x -> x.toDomain()).collect(Collectors.toList()), 
				new BentoReservationClosingTime(
						new ReservationClosingTime(
								new BentoReservationTimeName(reservationFrameName1), 
								new BentoReservationTime(reservationEndTime1), 
								reservationStartTime1==null ? Optional.empty() : Optional.of(new BentoReservationTime(reservationStartTime1))), 
						closingTime2));
	}
	
}
