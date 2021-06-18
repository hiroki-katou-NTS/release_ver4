package nts.uk.ctx.at.shared.infra.entity.holidaysetting.interimdata;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.shared.dom.holidaymanagement.publicholiday.interimdata.TempPublicHolidayManagement;
import nts.uk.ctx.at.shared.dom.remainingnumber.common.DayOfVacationUse;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;


/**
 * 暫定公休管理データ
 * @author
 *
 */

@Entity
@Table(name ="KSHDT_INTERIM_HDPUB")
public class KshdtInterimHdpub extends ContractUkJpaEntity {

	/* 主キー */
	@EmbeddedId
    public KshdtInterimHdpubPK pk;
	
	/** 残数管理データID */
	@Column(name = "REMAIN_MNG_ID")
	public String remainMngId;
	
	/** 作成元区分	 */
	@Column(name ="CREATOR_ATR")
	public int createAtr;
	
	/** 使用日数 */
	@Column(name ="USE_DAYS")
	public Double usedDays;
	
	@Override
	protected Object getKey() {
		return pk;
	}
	
	public TempPublicHolidayManagement toDomain(){
		return new TempPublicHolidayManagement(
				remainMngId, 
				pk.sid,  
				pk.ymd,
				EnumAdaptor.valueOf(createAtr, CreateAtr.class),
				RemainType.PUBLICHOLIDAY,
				new DayOfVacationUse(usedDays));
	}
}
