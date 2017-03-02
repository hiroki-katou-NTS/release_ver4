module cmm013.c.service {
    var paths = {
        getLayoutInfor : "pr/proto/layout/findlayoutwithmaxstartym",
        createlayouthistory: "pr/proto/layout/createlayouthistory"
    }
    
    /**
     * Get list layout master new history
     */
    export function getLayoutWithMaxStartYm(): JQueryPromise<Array<model.LayoutMasterDto>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.getLayoutInfor)
            .done(function(res: Array<any>){
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise(); 
    }
    
    export function createLayoutHistory(layoutMaster: model.LayoutMasterDto){
        var dfd = $.Deferred<Array<any>>();  
        nts.uk.request.ajax(paths.createlayouthistory, layoutMaster).done(function(res: Array<any>){
            dfd.resolve(res);    
        }).fail(function(res){
            dfd.reject(res);
        })
        return dfd.promise(); 
    }
    
    
    export module model {
            // layout
            export class LayoutMasterDto {
                checkContinue: Boolean;
                stmtCode: string;
                startYm: number;
                endYm: number;
                startPrevious: number;
                layoutAtr: number;
                stmtName: string;
            }

        }
}