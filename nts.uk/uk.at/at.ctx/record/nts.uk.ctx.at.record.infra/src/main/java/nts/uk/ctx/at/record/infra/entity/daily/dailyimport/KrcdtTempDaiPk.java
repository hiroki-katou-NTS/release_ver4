package nts.uk.ctx.at.record.infra.entity.daily.dailyimport;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class KrcdtTempDaiPk implements Serializable {

	/***/
	private static final long serialVersionUID = 1L;

	@Column(name =  "CCD")
	public int ccd;
	
	@Column(name = "SCD")
	public String scd;

    @Column(name = "YMD")
    @Convert(converter = GeneralDateToDBConverter.class)
	public GeneralDate ymd;
}
