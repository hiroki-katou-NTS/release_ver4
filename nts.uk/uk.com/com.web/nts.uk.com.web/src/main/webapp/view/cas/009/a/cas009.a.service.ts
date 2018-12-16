module cas009.a {
    import ajax = nts.uk.request.ajax;

    export const fetch = {
        opt: () => ajax('com', 'ctx/sys/auth/role/get/enum/reference/range'),
        role: {
            'has': () => ajax('com', 'ctx/sys/auth/role/user/has/role/8'),
            'get': (rids: any) => ajax('com', 'ctx/sys/auth/role/find/person/role', rids)
        },
        permision: {
            save: (command: any) => ajax('com', 'ctx/com/screen/person/role/register', command),
            remove: (command: any) => ajax('com', 'ctx/com/screen/person/role/delete', command),
            person_info: (roleId: string) => ajax('com', 'ctx/pereg/functions/auth/find-with-role', roleId),
            check: (roleId: string) => ajax('com', 'ctx/com/screen/person/role/check', roleId)
        },
    };
    export function exportExcel(): JQueryPromise<any> {
        let _params = { domainId: "RolePersonalInfor", 
                        domainType: "CAS009ロールの登録（個人情報）", 
                        languageId: "ja", 
                        reportType: 0, option : date };
        return nts.uk.request.exportFile('/masterlist/report/print', _params);
    }
}