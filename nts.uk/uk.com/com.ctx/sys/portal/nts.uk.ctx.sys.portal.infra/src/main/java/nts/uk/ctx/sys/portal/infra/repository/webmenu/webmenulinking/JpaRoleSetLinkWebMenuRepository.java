/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.sys.portal.infra.repository.webmenu.webmenulinking;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleSetLinkWebMenu;
import nts.uk.ctx.sys.portal.dom.webmenu.webmenulinking.RoleSetLinkWebMenuRepository;
import nts.uk.ctx.sys.portal.infra.entity.webmenu.webmenulinking.SptmtRoleSetWebMenu;
import nts.uk.ctx.sys.portal.infra.entity.webmenu.webmenulinking.SptmtRoleSetWebMenuPK;

/**
 * Class JpaRoleSetAndWebMenuRepository implement of RoleSetAndWebMenuRepository
 * @author HieuNV
 *
 */
@Stateless
public class JpaRoleSetLinkWebMenuRepository extends JpaRepository implements RoleSetLinkWebMenuRepository {

    private static final String SELECT_All_ROLE_SET_AND_WEB_MENU_BY_COMPANY_ID = "SELECT rw FROM SptmtRoleSetWebMenu rw"
            + " WHERE rw.roleSetWebMenuPK.companyId = :companyId ";

    private static final String SELECT_All_ROLE_SET_AND_WEB_MENU_BY_COMPANY_ID_AND_ROLE_SET_CD = "SELECT rw FROM SptmtRoleSetWebMenu rw"
            + " WHERE rw.roleSetWebMenuPK.companyId = :companyId"
            + "     AND rw.roleSetWebMenuPK.roleSetCd = :roleSetCd ";
    
    private static final String SELECT_All_ROLE_SET_AND_WEB_MENU_BY_COMPANY_ID_AND_ROLE_SET_CDS = "SELECT rw FROM SptmtRoleSetWebMenu rw"
            + " WHERE rw.roleSetWebMenuPK.companyId = :companyId"
            + "     AND rw.roleSetWebMenuPK.roleSetCd IN :roleSetCds ";
    
    private static final String DELETE_All_ROLE_SET_AND_WEB_MENU_BY_COMPANY_ID_ROLE_SET_CD = "DELETE FROM SptmtRoleSetWebMenu rw"
            + " WHERE rw.roleSetWebMenuPK.companyId = :companyId "
            + " AND rw.roleSetWebMenuPK.roleSetCd = :roleSetCd ";

    /**
     * Build Domain from Entity
     * @param entity
     * @return
     */
    private RoleSetLinkWebMenu toDomain(SptmtRoleSetWebMenu entity) {
        return new RoleSetLinkWebMenu(entity.roleSetWebMenuPK.companyId
                , entity.roleSetWebMenuPK.roleSetCd
                , entity.roleSetWebMenuPK.webMenuCd
                );
    }

    /**
     * Build Entity from Domain
     * @param domain
     * @return
     */
    private SptmtRoleSetWebMenu toEntity(RoleSetLinkWebMenu domain) {
        SptmtRoleSetWebMenuPK key = new SptmtRoleSetWebMenuPK(domain.getCompanyId()
                , domain.getWebMenuCd().v()
                , domain.getRoleSetCd().v());
        return new SptmtRoleSetWebMenu(key);
    }

    /**
     * Build Entity from Domain for updating
     * @param domain
     * @param upEntity
     * @return
     */
    private SptmtRoleSetWebMenu toEntityForUpdate(RoleSetLinkWebMenu domain, SptmtRoleSetWebMenu upEntity) {
        upEntity.buildEntity(new SptmtRoleSetWebMenuPK(domain.getCompanyId()
                , domain.getWebMenuCd().v()
                , domain.getRoleSetCd().v()));
        return upEntity;
    }

    @Override
    public List<RoleSetLinkWebMenu> findByCompanyId(String companyId) {
        return this.queryProxy().query(SELECT_All_ROLE_SET_AND_WEB_MENU_BY_COMPANY_ID
                , SptmtRoleSetWebMenu.class)
                .setParameter("companyId", companyId)
                .getList(c -> toDomain(c));
    }

    @Override
    public List<RoleSetLinkWebMenu> findByRoleSetCd(String companyId, String roleSetCd) {
    	String sql = " SELECT * FROM SPTMT_ROLE_SET_WEB_MENU WITH(INDEX(SPTMT_ROLE_SET_WEB_MENU_P)) WHERE CID = @companyId AND ROLE_SET_CD = @roleSetCd ";
    	List<RoleSetLinkWebMenu> resultList = new NtsStatement(sql, this.jdbcProxy())
    			.paramString("companyId", companyId)
    			.paramString("roleSetCd", roleSetCd)
    			.getList(rec -> {
    				return new RoleSetLinkWebMenu(
    						rec.getString("CID"),
    				    	rec.getString("ROLE_SET_CD"),
    						rec.getString("WEB_MENU_CD"));
    			});
        return resultList;
    }

    public List<RoleSetLinkWebMenu> findByListRoleSetCd(String companyId, List<String> roleSetCds) {
    	List<RoleSetLinkWebMenu> results = new ArrayList<>();
    	CollectionUtil.split(roleSetCds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
    		results.addAll(this.queryProxy().query(SELECT_All_ROLE_SET_AND_WEB_MENU_BY_COMPANY_ID_AND_ROLE_SET_CDS
                , SptmtRoleSetWebMenu.class)
                .setParameter("companyId", companyId)
                .setParameter("roleSetCds", subList)
                .getList(c -> toDomain(c)));
    	});
        return results;
    }
    
    @Override
    public Optional<RoleSetLinkWebMenu> findByKey(String companyId, String webMenuCd, String roleSetCd) {
        SptmtRoleSetWebMenuPK pk = new SptmtRoleSetWebMenuPK(companyId, webMenuCd, roleSetCd);
        return this.queryProxy().find(pk, SptmtRoleSetWebMenu.class).map(c -> toDomain(c));
    }

    @Override
    public void insert(RoleSetLinkWebMenu domain) {
        this.commandProxy().insert(toEntity(domain));
    }

    @Override
    public void update(RoleSetLinkWebMenu domain) {
         Optional<SptmtRoleSetWebMenu> upEntity = this.queryProxy().find(
                 new SptmtRoleSetWebMenuPK(domain.getCompanyId()
                    , domain.getWebMenuCd().v()
                    , domain.getRoleSetCd().v()),
                 SptmtRoleSetWebMenu.class);
        if (upEntity.isPresent()) {
            this.commandProxy().update(toEntityForUpdate(domain, upEntity.get()));
        }
    }

    @Override
    public void deleteAllByRoleCd(String companyId, String roleSetCd) {
        this.getEntityManager().createQuery(DELETE_All_ROLE_SET_AND_WEB_MENU_BY_COMPANY_ID_ROLE_SET_CD)
        .setParameter("companyId", companyId)
        .setParameter("roleSetCd", roleSetCd)
        .executeUpdate();
    }

    @Override
    public void deleteAllByRoleCd(String companyId, String roleSetCd, String webMenuCode) {
        SptmtRoleSetWebMenuPK pk = new SptmtRoleSetWebMenuPK(companyId, roleSetCd, webMenuCode);
        this.commandProxy().remove(SptmtRoleSetWebMenu.class, pk);
        
    }

    @Override
    public void insert(List<RoleSetLinkWebMenu> listDomain) {
        List<SptmtRoleSetWebMenu> lstSptmtRoleSetWebMenu = listDomain.stream()
                .map(domain -> toEntity(domain)).collect(Collectors.toList());
        this.commandProxy().insertAll(lstSptmtRoleSetWebMenu);
    }
}
