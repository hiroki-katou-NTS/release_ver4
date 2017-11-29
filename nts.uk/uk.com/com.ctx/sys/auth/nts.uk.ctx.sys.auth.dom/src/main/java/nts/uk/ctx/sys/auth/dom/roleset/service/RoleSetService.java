package nts.uk.ctx.sys.auth.dom.roleset.service;

import java.util.List;

import nts.uk.ctx.sys.auth.dom.roleset.RoleSet;

public interface RoleSetService {

	/**
	 * Get all Role Set - ロールセットをすべて取得する
	 * @return
	 */
	List<RoleSet> getAllRoleSet();
	
	/**
	 * アルゴリズム「新規登録」を実行する - Execute the algorithm "new registration"
	 * @param roleSet
	 */
	void registerRoleSet(RoleSet roleSet);
	
	
	/**
	 * アルゴリズム「更新登録」を実行する - Execute algorithm "update registration"
	 * @param roleSet
	 */
	void updateRoleSet(RoleSet roleSet);
	
	/**
	 * アルゴリズム「削除」を実行する - Execute algorithm "delete"
	 * @param roleSetCd
	 */
	void deleteRoleSet(String roleSetCd);
}
