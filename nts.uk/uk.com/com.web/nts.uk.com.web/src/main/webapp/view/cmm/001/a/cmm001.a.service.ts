module cmm001.a.service {
    var paths = {
        getAll: "bs/company/findCom",
        findDiv: "bs/employee/workplacedifferinfor/findDiv",
        findSys: "sys/env/usatr/findSys",
        findPost: "contact/postalcode/findAll",
        findPostId: "contact/postalcode/find",
        findPostCd: "contact/postalcode/findByCode",
        update: "screen/com/cmm001/update",
        add: "screen/com/cmm001/add",
        
    }
       
     export function getAll(){
        return nts.uk.request.ajax(paths.getAll);    
    }
      
    export function getDiv(param: any): JQueryPromise<void>{
        return nts.uk.request.ajax(paths.findDiv, param);     
    }
     
    export function getSys(vari: any): JQueryPromise<void>{
        return nts.uk.request.ajax(paths.findSys, vari);     
    }
    
    export function findPost(){
        return nts.uk.request.ajax(paths.findPost);    
    }
    
    export function findPostId(vari: String): JQueryPromise<void>{
        return nts.uk.request.ajax("com", paths.findPostId + "/" + vari);     
    }
    
    export function findPostCd(vari: String): JQueryPromise<void>{
        return nts.uk.request.ajax("com", paths.findPostCd + "/" + vari);     
    }
    
    export function update(command: any): JQueryPromise<void>{
        return nts.uk.request.ajax(paths.update, command);    
    }
    
    export function add(command: any): JQueryPromise<void>{
        return nts.uk.request.ajax(paths.add, command);    
    }
}