module nts.uk.pr.view.qmm017.k {
    export module service {
        var paths: any = {
            updateFormulaHistory: "pr/formula/formulaHistory/updateHistory",
            removeFormulaHistory: "pr/formula/formulaHistory/removeHistory",
        }
        
        export function updateFormulaHistory(command): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.updateFormulaHistory, command).done(function(){
                dfd.resolve();    
            }).fail(function(res){
                dfd.reject(res);
            });
            return dfd.promise();
        }
        
        export function removeFormulaHistory(command): JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            nts.uk.request.ajax(paths.removeFormulaHistory, command).done(function(){
                dfd.resolve();    
            }).fail(function(res){
                dfd.reject(res);
            });
            return dfd.promise();
        }
    }    
}