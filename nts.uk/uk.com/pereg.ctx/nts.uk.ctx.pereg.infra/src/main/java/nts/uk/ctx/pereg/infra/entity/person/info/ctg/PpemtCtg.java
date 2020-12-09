package nts.uk.ctx.pereg.infra.entity.person.info.ctg;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.shr.infra.data.entity.ContractUkJpaEntity;
import lombok.val;
import nts.arc.layer.infra.data.jdbc.map.JpaEntityMapper;
import nts.uk.ctx.pereg.infra.repository.mastercopy.helper.IdContainer;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PPEMT_CTG")

public class PpemtCtg extends ContractUkJpaEntity implements Serializable {

	public static final long serialVersionUID = 1L;
	
	public static final JpaEntityMapper<PpemtCtg> MAPPER = new JpaEntityMapper<>(PpemtCtg.class);

	@EmbeddedId
	public PpemtPerInfoCtgPK ppemtPerInfoCtgPK;

	@Basic(optional = false)
	@Column(name = "CID")
	public String cid;

	@Basic(optional = true)
	@Column(name = "CATEGORY_CD")
	public String categoryCd;

	@Basic(optional = false)
	@Column(name = "CATEGORY_NAME")
	public String categoryName;

	@Basic(optional = false)
	@Column(name = "ABOLITION_ATR")
	public int abolitionAtr;

	@Override
	protected Object getKey() {
		return ppemtPerInfoCtgPK;
	}
	
	/**
	 * 初期値コピー用
	 * @param targetCompanyId
	 * @return
	 */
	public PpemtCtg copy(String targetCompanyId, IdContainer.IdGenerator ids) {
		
		val pk = new PpemtPerInfoCtgPK(ids.generateFor(ppemtPerInfoCtgPK.perInfoCtgId));
		
		return new PpemtCtg(
				pk,
				targetCompanyId,
				categoryCd,
				categoryName,
				abolitionAtr);
	}
}
