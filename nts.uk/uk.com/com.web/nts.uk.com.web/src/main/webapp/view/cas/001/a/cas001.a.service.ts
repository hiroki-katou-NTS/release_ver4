module nts.uk.com.view.cas001.a.service {
    import ajax = nts.uk.request.ajax;
    var paths = {
        getPersonRoleList: "/ctx/bs/person/roles/findAll",
        getPersonRoleAuth: "/ctx/bs/person/roles/auth/find/{0}",
        getCategoryRoleList: "/ctx/bs/person/roles/auth/category/findAllCategory/{0}",
        getCategoryAuth: "/ctx/bs/person/roles/auth/category/find/{0}/{1}",
        getPersonRoleItemList: "/ctx/bs/person/roles/auth/item/findAllItem/{0}/{1}",
        savePersonRole: "/ctx/bs/person/roles/auth/save"
    }

    export function getPersonRoleList(): JQueryPromise<any> {
        return ajax(paths.getPersonRoleList);
    }

    export function getPersonRoleAuth(roleID): JQueryPromise<any> {
        return ajax(nts.uk.text.format(paths.getPersonRoleAuth, roleID));
    }

    export function getCategoryRoleList(roleID): JQueryPromise<any> {
        return ajax(nts.uk.text.format(paths.getCategoryRoleList, roleID));
    }

    export function getCategoryAuth(roleId, personInfoCategoryAuthId): JQueryPromise<any> {
        return ajax(nts.uk.text.format(paths.getCategoryAuth, roleId, personInfoCategoryAuthId));
    }

    export function getPersonRoleItemList(roleId, personInfoCategoryAuthId): JQueryPromise<any> {
        return ajax(nts.uk.text.format(paths.getPersonRoleItemList, roleId, personInfoCategoryAuthId));
    }

    export function savePersonRole(command): JQueryPromise<any> {
        return ajax(paths.savePersonRole, command);
    }

}
