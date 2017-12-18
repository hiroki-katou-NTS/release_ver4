module nts.uk.com.view.cmm051.a {
    export module service {
        var paths: any = {
            findAllWkpManager: "at/auth/workplace/manager/findAll/",
            saveWkpManager: "at/auth/workplace/manager/save/",
            deleteWkpManager: "at/auth/workplace/manager/remove/",
            getEmpInfo: "ctx/sys/auth/grant/rolesetperson/getempinfo/",
        }
    
        export function findAllWkpManagerByWkpId(wkpId : string): JQueryPromise<Array<base.IWorkplaceManager>> {
            return nts.uk.request.ajax("at", paths.findAllWkpManager + wkpId);
        }
    
        export function saveWkpManager(command: any) {
            return nts.uk.request.ajax("at", paths.saveWkpManager, command);
        }
    
        export function deleteWkpManager(command: any) {
            return nts.uk.request.ajax("at", paths.deleteWkpManager, command);
        }
        
        export function getEmployeeInfo(empId: string): JQueryPromise<any> {
            return nts.uk.request.ajax("com", paths.getEmpInfo + empId);
        };
    }
}