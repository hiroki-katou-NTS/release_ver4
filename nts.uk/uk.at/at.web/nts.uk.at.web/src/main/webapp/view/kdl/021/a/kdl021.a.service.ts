module kdl021.a.service {
    var paths = {
        getPossibleAttendanceItem: "at/share/attendanceitem/getPossibleAttendanceItem"
    }
    /**
    * get all divergence item id(id co the chon)
    */
    export function getPossibleItem(arrPossible: Array<number>): JQueryPromise<Array<model.AttendanceItem>> {
        return nts.uk.request.ajax("at", paths.getPossibleAttendanceItem , arrPossible);
    }
    export module model {
        export class AttendanceItem {
            id: number;
            name: string;
            displayNumber: number;
            useAtr: number;
            attendanceAtr: number;
        }
    }

}