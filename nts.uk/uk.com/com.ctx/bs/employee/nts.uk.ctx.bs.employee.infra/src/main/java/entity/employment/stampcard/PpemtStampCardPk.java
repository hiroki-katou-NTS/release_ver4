package entity.employment.stampcard;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class PpemtStampCardPk implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 個人ID **/
	@Column(name = "PID")
	public String personId;
}
