module nts.uk.com.view.cmm018.m {
    export module service {
         // Service paths.
        var servicePath = {
            searchModeEmployee: "workflow/approvermanagement/workroot/testMasterDat",
            saveAsExcel : "company/approver/report/unRegister"
                        
        } 
        export function searchModeEmployee(data: MasterApproverRootQuery) {
            return request.ajax('com', servicePath.searchModeEmployee, data);
        }
        
        export function saveAsExcel(data: MasterApproverRootQuery){
            return request.exportFile(servicePath.saveAsExcel, data);
        }
        
        export class MasterApproverRootQuery{
            baseDate: Date;
            chkCompany: boolean;
            chkWorkplace: boolean;
            chkPerson: boolean;
            constructor(baseDate: Date, chkCompany: boolean, chkWorkplace: boolean, chkPerson: boolean){
                this.baseDate = baseDate;
                this.chkCompany = chkCompany;
                this.chkWorkplace = chkWorkplace;
                this.chkPerson = chkPerson;    
            }
        }
    }
    
    
}
