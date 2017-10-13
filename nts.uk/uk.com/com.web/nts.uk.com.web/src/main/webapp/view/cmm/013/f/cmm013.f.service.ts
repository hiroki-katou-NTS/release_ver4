module nts.uk.com.view.cmm013.f {
       
    import SequenceMasterSaveCommand = base.SequenceMasterSaveCommand;  
    import SequenceMasterRemoveCommand = base.SequenceMasterRemoveCommand;
    
    export module service {
        
        /**
         *  Service paths
         */
        var servicePath: any = {
            findMaxOrder: "bs/employee/jobtitle/sequence/findMaxOrder",
            findAllSequenceMaster: "bs/employee/jobtitle/sequence/findAll",
            findBySequenceCode: "bs/employee/jobtitle/sequence/find",
            saveSequenceMaster: "bs/employee/jobtitle/sequence/save",
            removeSequenceMaster: "bs/employee/jobtitle/sequence/remove",
        };
        
        /**
         * findMaxOrder
         */
        export function findMaxOrder(): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.findMaxOrder);
        }
        
        /**
         * findAllSequenceMaster
         */
        export function findAllSequenceMaster(): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.findAllSequenceMaster);
        }
        
        /**
         * findBySequenceCode
         */
        export function findBySequenceCode(sequenceCode: string): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.findBySequenceCode, {sequenceCode: sequenceCode});
        }
        
        /**
         * saveSequenceMaster
         */
        export function saveSequenceMaster(command: SequenceMasterSaveCommand): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.saveSequenceMaster, command);
        }
        
        /**
         * removeSequenceMaster
         */
        export function removeSequenceMaster(command: SequenceMasterRemoveCommand): JQueryPromise<any> {
            return nts.uk.request.ajax(servicePath.removeSequenceMaster, command);
        }
                
        /**
        * Model namespace.
        */
        export module model {
            
        }
    }
}
