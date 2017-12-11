module nts.uk.com.view.cas013.a {
	import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;
    export module service {
        export class Service {
            paths = {
                getRoleType: "ctx/sys/auth/grant/roleindividual/getRoleType",
                getRole: "ctx/sys/auth/grant/roleindividual/getRoles",
                getRoleGrants: "ctx/sys/auth/grant/roleindividual/getRoleGrants",
                getRoleGrant: "ctx/sys/auth/grant/roleindividual/getRoleGrant",
                insertRoleGrant: "ctx/sys/auth/grant/roleindividual/insertRoleGrant",
                upDateRoleGrant:"ctx/sys/auth/grant/roleindividual/upDateRoleGrant"
            }
            constructor() {

            }
            getRoleTypes(): JQueryPromise<any> {
                return ajax("com", this.paths.getRoleType);
            };
            getRole(roleType: string): JQueryPromise<any> {
                return ajax("com", this.paths.getRole +'/'+ roleType);
            };
            getRoleGrants(role: string): JQueryPromise<any> {
                return ajax("com", this.paths.getRoleGrants, role);
            };
            getRoleGrant(roleId: string, userId: string): JQueryPromise<any> {
                var data = {
                    roleID: roleId,
                    userID: userId
                };
                return ajax("com", this.paths.getRoleGrant, data);
            };
            insertRoleGrant(roleType: string, roleId: string, userId: string, start: string, end: string): JQueryPromise<any> {
                var roleGrant = {
                    userID: userId,
                    roleID: roleId, 
                    roleType: roleType,
                    startValidPeriod: start,
                    endValidPeriod: end
                };
                return ajax("com", this.paths.insertRoleGrant, roleGrant);
            }
            upDateRoleGrant(roleType: string, roleId: string, userId: string, start: string, end: string): JQueryPromise<any> {
                var roleGrant = {
                    userID: userId,
                    roleID: roleId, 
                    roleType: roleType,
                    startValidPeriod: start,
                    endValidPeriod: end
                };
                return ajax("com", this.paths.upDateRoleGrant, roleGrant);
            }

            
            

        }
    }
}