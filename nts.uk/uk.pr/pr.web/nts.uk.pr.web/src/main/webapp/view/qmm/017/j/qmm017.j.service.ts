module nts.uk.pr.view.qmm017.j {
    export module service {
        var paths: any = {
            registerFormulaHistory: "pr/formula/formulaHistory/addHistory",
        }
        
        export function registerFormulaHistory(command): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.registerFormulaHistory, command).done(function(){
                dfd.resolve();    
            }).fail(function(res){
                dfd.reject(res);
            });
            return dfd.promise();
        }
    }    
}