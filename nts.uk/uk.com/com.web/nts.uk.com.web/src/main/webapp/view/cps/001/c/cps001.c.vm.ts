module cps001.c.vm {
    import info = nts.uk.ui.dialog.info;
    import alert = nts.uk.ui.dialog.alert;
    import text = nts.uk.resource.getText;
    import clearError = nts.uk.ui.errors.clearAll;
    import showDialog = nts.uk.ui.dialog;
    import confirm = nts.uk.ui.dialog.confirm;
    import close = nts.uk.ui.windows.close;
    import setShared = nts.uk.ui.windows.setShared;
    import alertError = nts.uk.ui.dialog.alertError;

    let __viewContext: any = window['__viewContext'] || {},
        block = window["nts"]["uk"]["ui"]["block"]["grayout"],
        unblock = window["nts"]["uk"]["ui"]["block"]["clear"],
        invisible = window["nts"]["uk"]["ui"]["block"]["invisible"];

    export class ViewModel {
        listEmployee: KnockoutObservableArray<IEmployee> = ko.observableArray([]);
        currentEmployee: KnockoutObservable<Employee> = ko.observable(new Employee());

        enaBtnRes: KnockoutObservable<boolean> = ko.observable(true);
        enaBtnDel: KnockoutObservable<boolean> = ko.observable(true);
        
        listEmpRestored: KnockoutObservableArray<string> = ko.observableArray([]);

        constructor() {
            let self = this,
                emps = self.listEmployee(),
                emp = self.currentEmployee();

            emp.id.subscribe(x => {
                
                let listEmp = $("#list_employees").igGrid("option",'dataSource');
                console.log("LIST DATASOURCE " + listEmp.length );
                if(listEmp.length  == 0 ){
                    self.newMode();
                    nts.uk.ui.errors.clearAll();
                    return; 
                }
                
                let obj = _.filter(listEmp, function(o) { return o.id == x; });
                
                if (obj.length > 0) {
                    self.enableControl();
                    nts.uk.ui.errors.clearAll();
                    nts.uk.ui.errors.clearAll();

                    let iem: IEmployee = _.find(self.listEmployee(), e => e.id == x);

                    block();
                    service.getDetail(x).done((data: IEmployee) => {
                        if (data) {
                            if(iem){
                                 emp.id(iem.id);
                            }
                            emp.code(data.code);
                            emp.name(data.name);
                            if(data.reason){
                                emp.reason(data.reason || '');}
                            if(data.dateDelete){
                                emp.dateDelete(data.dateDelete || undefined);
                            }
                            
                            $('#code').focus();
                            unblock();
                            nts.uk.ui.errors.clearAll();
                        }else{
                            unblock();
                            nts.uk.ui.errors.clearAll();}
                    });
                } else {
                    self.newMode();
                    nts.uk.ui.errors.clearAll();
                }
            });

            self.start();
        }

        start(sid?: string): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                emps = self.listEmployee,
                emp = self.currentEmployee();


            block();
            service.getData().done((data: Array<IEmployee>) => {

                unblock();

                if (data && data.length) {
                    emps(data);
                    $('#code').focus();
                    if (!sid) {
                        emp.id(data[0].id);
                    } else {

                        let _item: IEmployee = _.find(ko.toJS(self.listEmployee), (item: IEmployee) => item.id == sid);

                        if (_item) {
                            emp.id(_item.id);
                        } else {
                            emp.id(data[0].id);
                        }
                    }
                } else {
                    // list null
                    self.newMode();
                }
                dfd.resolve();
            }).fail(() => {
                //emps.removeAll();
                unblock();

            });
            return dfd.promise();
        }

        reStoreData() {
            let self = this,
                emp: IEmployee = ko.toJS(self.currentEmployee),
                listItem: Array<IEmployee> = ko.toJS(self.listEmployee);

            $("#A_INP_CODE").trigger("validate");
            $("#A_INP_NAME").trigger("validate");

            if (nts.uk.ui.errors.hasError()) {
                return;
            }

            confirm({messageId: "Msg_528"}, {buttonStyles: { yes: "proceed" } }).ifYes(() => {
                let itemListLength = self.listEmployee().length,
                    indexItemDelete = _.findIndex(ko.toJS(self.listEmployee), function(item: any) { return item.id == emp.id; }),
                    objToRestore = {
                        id: emp.id,
                        code: emp.code,
                        name: emp.name
                    };
                block();
                service.restoreData(objToRestore).done(() => {

                    setShared('CPS001A_PARAMS', {
                        showAll: false,
                        employeeId: emp.id
                    });

                    if (itemListLength === 1) {
                        self.start();
                    } else if (itemListLength - 1 === indexItemDelete) {
                        self.start(listItem[indexItemDelete - 1].id);
                    } else if (itemListLength - 1 > indexItemDelete) {
                        self.start(listItem[indexItemDelete + 1].id);
                    }
                    
                    self.listEmpRestored.push(emp.id);

                    unblock();

                }).fail(error => {

                    if (error.messageId == 'Msg_345') {
                        alertError({ messageId: "Msg_345" })
                            .then(() => {
                                $('#code').focus();
                            });
                    } else {
                        alertError({ messageId: error.messageId }).then(() => {
                        });
                    }
                    unblock();

                });

            }).ifNo(() => {
                unblock();
            });
        }

        deleteData() {
            let self = this,
                emp: IEmployee = ko.toJS(self.currentEmployee()),
                listItem: Array<IEmployee> = ko.toJS(self.listEmployee());
            
            $("#A_INP_CODE").trigger("validate");
            $("#A_INP_NAME").trigger("validate");

            if (nts.uk.ui.errors.hasError()) {
                return;
            }


            confirm({ messageId: "Msg_1449" }).ifYes(() => {
                let sid = emp.id;
                block();
                service.removedata(sid).done(() => {
                    showDialog.info({ messageId: "Msg_464" }).then(function() {
                        let itemListLength = self.listEmployee().length,
                            indexItemDelete = _.findIndex(ko.toJS(self.listEmployee), function(item: any) { return item.id == emp.id; });
                        if (itemListLength === 1) {
                            self.start();
                        } else if (itemListLength - 1 === indexItemDelete) {
                            self.start(listItem[indexItemDelete - 1].id);
                        } else if (itemListLength - 1 > indexItemDelete) {
                            self.start(listItem[indexItemDelete + 1].id);
                        }
                        nts.uk.ui.block.clear();
                        unblock();
                    });


                }).fail((mes) => {
                    unblock();
                });
            }).ifNo(() => {
                unblock();
            });
        }

        closeUp() {
             let self = this;
            setShared('CPS001C_RESTORE', self.listEmpRestored());
            close();
        }

        newMode() {
            let self = this,
                emps = self.listEmployee(),
                emp = self.currentEmployee();

            emp.enableCode(false);
            emp.enableName(false);
            emp.code('');
            emp.name('');
            emp.reason('');
            emp.dateDelete('');
            self.enaBtnRes(false);
            self.enaBtnDel(false);
        }

        enableControl() {
            let self = this,
                emps = self.listEmployee(),
                emp = self.currentEmployee();

            emp.enableCode(true);
            emp.enableName(true);
            self.enaBtnRes(true);
            self.enaBtnDel(true);
            nts.uk.ui.errors.clearAll();
        }


    }

    interface IEmployee {
        id: string;
        code: string;
        name: string;
        reason?: string;
        dateDelete?: string;
    }

    class Employee {
        id: KnockoutObservable<string> = ko.observable('');
        code: KnockoutObservable<string> = ko.observable('');
        name: KnockoutObservable<string> = ko.observable('');
        enableCode: KnockoutObservable<boolean> = ko.observable(true);
        enableName: KnockoutObservable<boolean> = ko.observable(true);

        reason: KnockoutObservable<string> = ko.observable('');
        dateDelete: KnockoutObservable<string> = ko.observable('');

        constructor(param?: IEmployee) {
            let self = this;
            if (param) {
                self.id(param.id || '');

                self.code(param.code || '');
                self.name(param.name || '');
            }
        }
    }
}