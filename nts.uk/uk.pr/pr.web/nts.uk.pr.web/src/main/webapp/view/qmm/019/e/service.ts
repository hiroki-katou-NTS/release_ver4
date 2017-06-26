module qmm019.e.service {
    var paths:any = {
        getLayoutInfor : "/pr/proto/layout/findlayout/{0}/{1}",
        pathDeleteLayout: "/pr/proto/layout/deletedata",
        pathUpdateLayout: "/pr/proto/layout/updatedata"
    }
    
    /**
     * Get list layout master new history
     */
    export function getLayout(stmtCode: string, historyId: string): JQueryPromise<model.LayoutMasterDto> {
        var dfd = $.Deferred<any>();
        var objectLayout = {stmtCode: stmtCode, historyId: historyId};
        var _path = nts.uk.text.format(paths.getLayoutInfor, stmtCode, historyId);
        nts.uk.request.ajax(_path)
            .done(function(res: any){
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise(); 
    }
    
    export function deleteLayout(layoutMaster : model.LayoutMasterDto){
        var dfd = $.Deferred<Array<any>>();  
//        var dto : model.LayoutMasterDto = {
//            companyCode: layoutMaster.companyCode,
//            stmtCode: layoutMaster.stmtCode,
//            startYm: layoutMaster.startYm,
//            stmtName: layoutMaster.stmtName,
//            endYM: layoutMaster.endYM,
//            layoutAtr: layoutMaster.layoutAtr
//        };
        var _path = nts.uk.text.format(paths.pathDeleteLayout, layoutMaster);
        nts.uk.request.ajax(paths.pathDeleteLayout, layoutMaster).done(function(res: Array<any>){
            dfd.resolve(res);    
        }).fail(function(res){
            dfd.reject(res);
        })
        
        return dfd.promise(); 
    }
    
    export function updateLayout(layoutMaster : model.LayoutMasterDto){
        var dfd = $.Deferred<Array<any>>();  
        nts.uk.request.ajax(paths.pathUpdateLayout, layoutMaster).done(function(res: Array<any>){
            dfd.resolve(res);    
        }).fail(function(res){
            dfd.reject(res);
        })
        
        return dfd.promise(); 
    }
    
   /**
           * Model namespace.
        */
        export module model {
            // layout
            export class LayoutMasterDto {
                companyCode: string;
                stmtCode: string;
                startYm: number;
                stmtName: string;
                endYm: number;
                layoutAtr: number;
                startYmOriginal: number;
                histortyId: string;
            }

        }
}