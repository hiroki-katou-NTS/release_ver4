module nts.uk.at.view.kdl051.test.screenModel {
  
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;

    export class ViewModel {

        selectedCodeList = ko.observable("c7cb93ce-d23b-4283-875e-a0bbb21b9d36," +
            "fe3b1f44-dbc8-44c0-ab32-f617f01f00a5," +
            "96c1e494-5cde-402c-8629-81b0dec7ac92," +
            "da1886cf-b80f-425c-af09-44a94a7643f2," +
            "ae69eb3f-4198-47e3-9f98-967d23c00997");
        baseDate = ko.observable(new Date());
        constructor() {
            let self = this;


        }

        openKDL051Dialog() {
            let self = this,
                employeeList = _.split(self.selectedCodeList(), ',');
            setShared('KDL051A_PARAM', { baseDate: self.baseDate(), employeeIds: employeeList } );
            if(employeeList.length > 1 ) {
              modal("/view/kdl/051/multi.xhtml");
            } else {
              modal("/view/kdl/051/single.xhtml");
            }

        }
        start(): JQueryPromise<any> {

            var self = this,
                dfd = $.Deferred();


            dfd.resolve();

            return dfd.promise();
        }
    }
}