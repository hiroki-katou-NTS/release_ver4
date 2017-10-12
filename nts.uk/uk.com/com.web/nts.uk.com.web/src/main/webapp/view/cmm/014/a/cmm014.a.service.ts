module nts.uk.com.view.cmm014.a.service {
        
    /**
     *  Service paths
     */
    var servicePath: any = {
        findClassification: "basic/employee/classification/find",
        saveClassification: "basic/employee/classification/save",
        removeClassification: "basic/employee/classification/remove"
    }

    /**
     * Get list classification
     */
    export function findClassification(classificationCode: any): JQueryPromise<Array<model.ClassificationFindDto>> {
        return nts.uk.request.ajax(servicePath.findClassification + "/" + classificationCode);
    }

    /**
     * save Classification
     */
    export function saveClassification(classification: any): JQueryPromise<any> {
        return nts.uk.request.ajax(servicePath.saveClassification, classification);
    }

    /**
    * remove Classification
    */
    export function removeClassification(params: any): JQueryPromise<any> {    
        return nts.uk.request.ajax(servicePath.removeClassification, params);
    }  
    
    export module model {
        
        export class ClassificationFindDto {
            code: string;
            name: string;
            memo: string;
            
            constructor(code?: string, name?: string, memo?: string) {
                this.code = code;
                this.name = name;
                this.memo = memo;
            }
        }
    }    
}
