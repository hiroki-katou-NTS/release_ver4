package repository.workplacedifferinfor;

import java.util.Optional;

import javax.ejb.Stateless;

import entity.workplacedifferinfor.BcmmtDivWorkDifferInfor;
import entity.workplacedifferinfor.BcmmtDivWorkDifferInforPK;
import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.bs.employee.dom.workplace.differinfor.DivWorkDifferInfor;
import nts.uk.ctx.bs.employee.dom.workplace.differinfor.DivWorkDifferInforRepository;
@Stateless
public class JpaWorkplaceDifferInforRepository extends JpaRepository implements DivWorkDifferInforRepository{
	// division workplace difference information
		private final String SELECT_NO_WHERE = "SELECT c FROM BcmmtDivWorkDifferInfor c ";
		private final String SELECT_ITEM = SELECT_NO_WHERE + "WHERE c.bcmmtDivWorkDifferInforPK.companyId = :companyId AND c.bcmmtDivWorkDifferInforPK.companyCode = :companyCode AND c.bcmmtDivWorkDifferInforPK.contractCd = :contractCd";
	
		/**
		 * convert from DivWorkDifferInfor entity to DivWorkDifferInfor domain
		 * @param entity
		 * @return
		 * author: Hoang Yen
		 */
		private static DivWorkDifferInfor toDomainDiv(BcmmtDivWorkDifferInfor entity){
			DivWorkDifferInfor domain = DivWorkDifferInfor.createFromJavaType(entity.bcmmtDivWorkDifferInforPK.companyId,
																				entity.bcmmtDivWorkDifferInforPK.companyCode,
																				entity.bcmmtDivWorkDifferInforPK.contractCd,
																				entity.regWorkDiv);
			return domain;
		}
		
		/**
		 * convert from DivWorkDifferInfor domain to DivWorkDifferInfor entity 
		 * @param domain
		 * @return  
		 * author: Hoang Yen
		 */
		private BcmmtDivWorkDifferInfor toEntityDiv(DivWorkDifferInfor domain){
			val entity = new BcmmtDivWorkDifferInfor();
			entity.bcmmtDivWorkDifferInforPK = new BcmmtDivWorkDifferInforPK(domain.getCompanyId(),
																				domain.getCompanyCode().v(),
																				domain.getContractCd().v());
			entity.regWorkDiv = domain.getRegWorkDiv().value;
			return entity;
		}

		/**
		 * find division workplace difference information
		 * author: Hoang Yen
		 */
		@Override
		public Optional<DivWorkDifferInfor> findDivWork(String companyId, String companyCode, String contractCd) {
			return this.queryProxy().find(new BcmmtDivWorkDifferInforPK(companyId, companyCode, contractCd), BcmmtDivWorkDifferInfor.class)
					.map(c -> toDomainDiv(c));
		}

		/**
		 * update division workplace difference information
		 * author: Hoang Yen
		 */
		@Override  
		public void updateDivWork(DivWorkDifferInfor domain) {
			BcmmtDivWorkDifferInfor entity = toEntityDiv(domain);
			BcmmtDivWorkDifferInfor oldEntity = this.queryProxy()
											.find(entity.bcmmtDivWorkDifferInforPK, BcmmtDivWorkDifferInfor.class).get();
			oldEntity.regWorkDiv = entity.regWorkDiv;
		}
	
		/**
		 * update division workplace difference information
		 * author: Hoang Yen
		 */
		@Override
		public void insertDivWork(DivWorkDifferInfor div) {
			BcmmtDivWorkDifferInfor entity = toEntityDiv(div);
			this.commandProxy().insert(entity);
		}
		
		/**
		 * delete a item
		 * author: Hoang Yen
		 */
		@Override
		public void deleteDivWork(String companyId, String companyCode, String contractCd) {
			BcmmtDivWorkDifferInforPK pk = new BcmmtDivWorkDifferInforPK(companyId, companyCode, contractCd);
			this.commandProxy().remove(BcmmtDivWorkDifferInforPK.class, pk);
		}

}
