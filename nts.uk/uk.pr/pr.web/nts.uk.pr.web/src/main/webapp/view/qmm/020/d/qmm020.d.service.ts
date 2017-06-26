module qmm020.d.service {
    let path = {
    };

    export function getData(): JQueryPromise<Array<any>> {        
        let dfd = $.Deferred<Array<any>>();
        
        return dfd.promise();
    }
}