module cmm013.d.service {
      var paths = {
          //xem lai duong dan
          updateHist: "basic/organization/position/updateHist",
          deleteHist: "basic/organization/position/deleteHist"  
      }
    

    //xoa lich su position
    export function deleteHistory(history: viewmodel.model.ListHistoryDto){
        history.oldStartDate = moment(history.oldStartDate).format("YYYY-MM-DD");
        history.newStartDate = moment(history.newStartDate).format("YYYY-MM-DD");
        return nts.uk.request.ajax("com", paths.deleteHist, history);    
    }
    //update lich su position
    export function updateHistory(history: viewmodel.model.ListHistoryDto){
        history.oldStartDate = moment(history.oldStartDate).format("YYYY-MM-DD");
        history.newStartDate = moment(history.newStartDate).format("YYYY-MM-DD");
        return nts.uk.request.ajax("com", paths.updateHist, history);    
    }

}