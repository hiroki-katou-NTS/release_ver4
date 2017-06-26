module cmm008.a.service{
    var path = {
        getAllEmployment: "basic/organization/employment/findallemployments",
        createEmployment: "basic/organization/employment/createemployment",
        updateEmployment: "basic/organization/employment/updateemployment",
        deleteEmployment: "basic/organization/employment/deleteemployment/" ,
        getEmploymentByCode: "basic/organization/employment/findemploymentbycode/",
        getAllProcessingNo: "pr/core/paydayrocessing/getbyccd",
        getCompanyInfor: "ctx/proto/company/findCompany"
    }
    //find all employment data
    export function getAllEmployments(): JQueryPromise<Array<model.employmentDto>>{
        return  nts.uk.request.ajax("com",path.getAllEmployment);
    }
    
    export function getEmploymentByCode(employmentCode: string): JQueryPromise<model.employmentDto>{
        return  nts.uk.request.ajax("com",path.getEmploymentByCode + employmentCode);
    }
    //create new employment data
    export function createEmployment(employment: model.employmentDto){
        return nts.uk.request.ajax("com", path.createEmployment, employment);
    }
    //update employment data
    export function updateEmployment(employment: model.employmentDto){
        return nts.uk.request.ajax("com",path.updateEmployment,employment);
    }
    //delete employment data
    export function deleteEmployment(employment: model.employmentDto){
        return nts.uk.request.ajax("com",path.deleteEmployment, employment);
    }
    //get all 処理日区分
     export function getProcessingNo(){
         return nts.uk.request.ajax(path.getAllProcessingNo);
    }
    //get 就業権限 by company
    export function getCompanyInfor(){
        return nts.uk.request.ajax('com',path.getCompanyInfor);
    }
    
    
    export module model{
        export class employmentDto{
            employmentCode: string;
            employmentName: string;
            memo: string;
            closeDateNo: number;
            processingNo: number;
            statutoryHolidayAtr: number;
            employementOutCd: string;
            displayFlg: number;   
            displayStr: string; 
            closeDateNoStr: string;
            processingStr: string
        }    
    }
}