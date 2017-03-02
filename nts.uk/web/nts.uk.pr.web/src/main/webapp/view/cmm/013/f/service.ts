 
  module cmm013.f.service {
    var paths = {
        findAllPosition: "basic/position/findallposition/",
        addPosition: "basic/position/addPosition",
        deletePosition: "basic/position/deletePosition",
        updatePosition: "basic/position/updatePosition",
        getAllHistory: "basic/position/getallhist"
        
  
    }
    export function findAllPosition(historyId: string): JQueryPromise<Array<any>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com",paths.findAllPosition + historyId)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    

    
    export function addPosition(position: viewmodel.model.DeletePositionCommand){
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com",paths.addPosition, position)
            .done(function(res: any){
                dfd.resolve(res);    
            })    
            .fail(function(res){
                dfd.reject(res);
            })
        return dfd.promise();
     }

 

    export function updatePosition(position: viewmodel.model.ListPositionDto) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.updatePosition, position).done(
            function(res: any) {
                dfd.resolve(res);
            }
        )
            .fail(function(res) {
                dfd.reject(res);
            })

        return dfd.promise();
    }

 

    export function deletePosition(position: viewmodel.model.DeletePositionCommand) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com", paths.deletePosition, position).done(
            function(res: any) {
                dfd.resolve(res);
            }
        )
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    

    export function getAllHistory():JQueryPromise<Array<any>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax("com",paths.getAllHistory)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }



}