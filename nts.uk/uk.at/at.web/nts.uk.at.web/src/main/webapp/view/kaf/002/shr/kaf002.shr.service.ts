module nts.uk.at.view.kaf002.shr {
    import ajax = nts.uk.request.ajax;
    export module service {
        var paths: any = {
            getAllWorkLocation: "at/record/worklocation/findall",
            findByAppID: "at/request/application/stamp/findByID",
            newScreenFind: "at/request/application/stamp/newAppStampInitiative",
            insert: "at/request/application/stamp/insert",
            update: "at/request/application/stamp/update",
            getStampCombinationAtr: "at/request/application/stamp/enum/stampCombination"
        }
        
        export function findAllWorkLocation(): JQueryPromise<any> {
            return ajax("at", paths.getAllWorkLocation);
        }
        
        export function findByAppID(appID: string): JQueryPromise<any> {
            return ajax("at", paths.findByAppID, appID);
        }
        
        export function newScreenFind(): JQueryPromise<any> {
            return ajax(paths.newScreenFind);
        }
        
        export function insert(command): JQueryPromise<any> {
            return ajax(paths.insert, command);
        }
        
        export function update(command): JQueryPromise<any> {
            return ajax(paths.update, command);
        }
        
        export function getStampCombinationAtr(): JQueryPromise<any> {
            return ajax(paths.getStampCombinationAtr);    
        }
    }
}