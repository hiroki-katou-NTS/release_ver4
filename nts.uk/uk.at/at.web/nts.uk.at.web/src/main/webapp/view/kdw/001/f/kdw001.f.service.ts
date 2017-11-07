module nts.uk.at.view.kdw001.f {
    export module service {
        var paths = {
            getAllEmpCalAndSumExeLog : "at/record/log/getallbydate",
            getCaseSpecExeContentById : "at/record/case/getcasebyid",
            getAllCaseSpecExeContent :  "at/record/case/getallcase",
            getAllClosure : "ctx/at/shared/workrule/closure/findAll"
        }
        
        /**
         * get all EmpCalAndSumExeLog by startDate and endDate
         */
        export function getAllEmpCalAndSumExeLog(inputEmpCalAndSumByDate: any ) : JQueryPromise<Array<any>>{
            return nts.uk.request.ajax("at",paths.getAllEmpCalAndSumExeLog,inputEmpCalAndSumByDate);
        }
    
        /**
         * get CaseSpecExeContent By Id
         */
        export function getCaseSpecExeContentById(caseSpecExeContentID: string ) : JQueryPromise<any>{
            return nts.uk.request.ajax("at",paths.getCaseSpecExeContentById+"/"+caseSpecExeContentID);
        }
        
        /**
         * get all CaseSpecExeContent 
         */
        export function getAllCaseSpecExeContent() : JQueryPromise<Array<any>>{
            return nts.uk.request.ajax("at",paths.getAllCaseSpecExeContent);
        }
        
        /**
         * get all Closure 
         */
        export function getAllClosure() : JQueryPromise<Array<any>>{
            return nts.uk.request.ajax("at",paths.getAllClosure);
        }
    
    
    }//end module service
}//end module
