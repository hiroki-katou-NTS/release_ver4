module kmk011.a.service {
    var paths = {
        getAllDivTime: "at/record/divergencetime/getalldivtime",
        updateDivTime: "at/record/divergencetime/updatedivtime",
        updateTimeItemId: "at/record/divergencetime/updateTimeItemId",
        getAllAttItem: "at/share/attendanceType/getByType/",
        getItemSet: "at/record/divergencetime/getitemset/",
        getAllName: "at/share/attendanceitem/getPossibleAttendanceItem"
    }
    /**
    * get all item selected(item da duoc chon)
    */
    export function getItemSelected(divTimeId: number): JQueryPromise<Array<viewmodel.model.TimeItemSet>> {
        return nts.uk.request.ajax("at", paths.getItemSet + divTimeId);
    }
    /**
    * get name(item da duoc chon)
    */
    export function getNameItemSelected(lstItemId: Array<number>): JQueryPromise<Array<viewmodel.model.DivergenceItem>> {
        return nts.uk.request.ajax("at", paths.getAllName, lstItemId);
    }
    /**
    * get all attendance item id(id co the chon)
    */
    export function getAllAttItem(divType: number): JQueryPromise<Array<viewmodel.model.AttendanceType>> {
        return nts.uk.request.ajax("at", paths.getAllAttItem + divType);
    }
    /**
    * update time item id (da duoc chon lai)
    */
    export function updateTimeItemId(lstItemId: Array<viewmodel.model.DivergenceTimeItem>): JQueryPromise<Array<viewmodel.model.DivergenceTimeItem>> {
        return nts.uk.request.ajax("at", paths.updateTimeItemId, lstItemId);
    }
    /**
    * get all divergence time
    */
    export function getAllDivTime(): JQueryPromise<Array<viewmodel.model.DivergenceTime>> {
        return nts.uk.request.ajax("at", paths.getAllDivTime);
    }
    /**
     * update divergence time
     */
    export function updateDivTime(Object: viewmodel.model.ObjectDivergence): JQueryPromise<Array<viewmodel.model.DivergenceTimeItem>> {
        return nts.uk.request.ajax("at", paths.updateDivTime, Object);
    }
}