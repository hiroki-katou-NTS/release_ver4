module cas009.a {
    import ajax = nts.uk.request.ajax;

    export const fetch = {
        opt: () => ajax('com', 'ctx/sys/auth/role/get/enum/reference/range'),
        role: {
            'has': () => ajax('com', 'ctx/sys/auth/role/user/has/role/8'),
            'get': (rids: any) => ajax('com', 'ctx/sys/auth/role/find/person/role', rids)
        },
        permision: {
            save: (command: any) => ajax('com', 'ctx/pereg/functions/auth/register', command),
            remove: (command: any) => ajax('com', 'ctx/pereg/functions/auth/delete', command),
            person_info: (roleId: string) => ajax('com', 'ctx/pereg/functions/auth/find-with-role', roleId)
        },
    };
}