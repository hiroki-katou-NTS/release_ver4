module cmm014.a.viewmodel {

    export class ScreenModel {

        dataSource: KnockoutObservableArray<viewmodel.model.ClassificationDto>;
        columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        currentCode: KnockoutObservable<any>;
        currentCodeList: KnockoutObservableArray<any>;
        currentItem: KnockoutObservable<any>;

        INP_002_code: any;
        INP_002_enable: any;
        INP_003_name: any;
        INP_004_notes: any;
        classificationCode: KnockoutObservable<string>;
        classificationName: KnockoutObservable<string>;
        classificationMemo: KnockoutObservable<string>;
        classificationList: KnockoutObservableArray<Classification>;
        selectedClassificationCode: any;
        texteditorcode: any;
        texteditorname: any;
        multilineeditor: any;

        constructor() {
            var self = this;
            self.dataSource = ko.observableArray([
                new viewmodel.model.ClassificationDto('A000001', '1��{��'),
                new viewmodel.model.ClassificationDto('A000002', '2��{��'),
                new viewmodel.model.ClassificationDto('A000003', '3��12�{'),
                new viewmodel.model.ClassificationDto('A000004', '4��12�{'),
                new viewmodel.model.ClassificationDto('A000005', '5��12�{'),
                new viewmodel.model.ClassificationDto('A000006', '6��12�{'),
                new viewmodel.model.ClassificationDto('A000007', '7��12�{'),
                new viewmodel.model.ClassificationDto('A000008', '8��12�{'),
                new viewmodel.model.ClassificationDto('A000009', '9��12�{'),
                new viewmodel.model.ClassificationDto('A000010', '10��12�{')

            ]);
            self.columns = ko.observableArray([
                { headerText: '�R�[�h', prop: 'classificationCode', width: 100 },
                { headerText: '����', prop: 'classificationName', width: 80 }

            ]);
            self.currentCode = ko.observable(self.dataSource()[0].classificationCode);
            self.currentCodeList = ko.observableArray([]);
            self.currentItem = ko.observable("");

            self.INP_002_code = ko.observable(self.dataSource()[0].classificationCode);
            self.INP_002_enable = ko.observable(false);
            self.INP_003_name = ko.observable(self.dataSource()[0].classificationName);
            self.INP_004_notes = ko.observable('');


            self.currentCode.subscribe((function(codeChanged) {
                self.currentItem(self.findObj(codeChanged));
                if (self.currentItem() != null) {
                    self.INP_002_code(self.currentItem().classificationCode);
                    self.INP_003_name(self.currentItem().classificationName);
                }
            }));




        }

        findObj(value: string): any {
            let self = this;
            var itemModel = null;
            _.find(self.dataSource(), function(obj: viewmodel.model.ClassificationDto) {
                if (obj.classificationCode == value) {
                    itemModel = obj;
                }
            })
            return itemModel;
        }

        initRegisterClassification() {
            var self = this;
            self.INP_002_enable(true);
            self.INP_002_code("");
            self.INP_003_name("");
            self.currentCode(null);
        }

        checkInput(): boolean {
            var self = this;
            if (self.INP_002_code() == '' || self.INP_003_name() == '') {
                console.log("input is null");
                return false;
            } else {
                return true;
            }
        }

        RegisterClassification() {
            var self = this;
            var dfd = $.Deferred<any>();
            if (self.checkInput()) {
                console.log("Insert(??ng ky m?i) or Update Classification");
                for (let i = 0; i < self.dataSource().length; i++) {
                    if (self.INP_002_code() == self.dataSource()[i].classificationCode) {
                        var classification_old = self.dataSource()[i];
                        var classification_update = new viewmodel.model.ClassificationDto(self.INP_002_code(), self.INP_003_name());
                        classification_update.classificationMemo = self.INP_004_notes();
                        service.updateClassification(classification_update).done(function() {
                            service.getAllClassification().done(function(classification_arr: Array<model.ClassificationDto>) {
                                self.dataSource(classification_arr);
                                dfd.resolve();
                            }).fail(function(res) {
                                dfd.reject(res);
                            })
                            dfd.resolve();
                        }).fail(function(res) {
                            dfd.reject(res);
                        })

                        //                        self.dataSource.replace(classification_old, classification_update);
                        //                        console.log("update");
                        //                        console.log(classification_update);
                        break;
                    } else if (self.INP_002_code() != self.dataSource()[i].classificationCode && i == self.dataSource().length - 1) {
                        var classification_new = new viewmodel.model.ClassificationDto(self.INP_002_code(), self.INP_003_name());
                        classification_new.classificationMemo = self.INP_004_notes();

                        service.addClassification(classification_new).done(function() {
                            service.getAllClassification().done(function(classification_arr: Array<model.ClassificationDto>) {
                                self.dataSource(classification_arr);
                                dfd.resolve();
                            }).fail(function(res) {
                                dfd.reject(res);
                            })
                            dfd.resolve();
                        }).fail(function(res) {
                            dfd.reject(res);
                        })
                        //                        self.dataSource.push(classification_new);
                        //                        console.log("dang ky moi");
                        //                        console.log(classification_new);
                        break;
                    }

                }
            }
        }

        DeleteClassification() {

            var self = this;
            var dfd = $.Deferred<any>();
            var item = new model.RemoveClassificationCommand(self.currentItem().classificationCode);
            service.removeClassification(item).done(function() {

                service.getAllClassification().done(function(classification_arr: Array<model.ClassificationDto>) {
                    self.dataSource(classification_arr);
                    dfd.resolve();
                }).fail(function(res) {
                    dfd.reject(res);
                })

                //   self.dataSource(classification_arr);
                //  dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);
            })

            //   self.dataSource.remove(self.currentItem());

            self.classificationCode = ko.observable("");
            self.classificationName = ko.observable("");
            self.classificationMemo = ko.observable("");
            self.classificationList = ko.observableArray([]);
            self.selectedClassificationCode = ko.observable(null);
            self.texteditorcode = {
                value: ko.observable(''),
                constraint: 'ResidenceCode',
                option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    textmode: "text",
                    placeholder: "",
                    width: "100px",
                    textalign: "left"
                })),
                required: ko.observable(true),
                enable: ko.observable(true)
            };
            self.texteditorname = {
                value: ko.observable(''),
                constraint: 'ResidenceCode',
                option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                    textmode: "text",
                    placeholder: "",
                    width: "100px",
                    textalign: "left"
                })),
                required: ko.observable(true),
                enable: ko.observable(true)
            };
            self.multilineeditor = {
                value: ko.observable(''),
                constraint: 'ResidenceCode',
                option: ko.mapping.fromJS(new nts.uk.ui.option.MultilineEditorOption({
                    resizeable: true,
                    placeholder: "",
                    width: "",
                    textalign: "left"
                })),
                required: ko.observable(true),
                enable: ko.observable(true),
                readonly: ko.observable(false)
            };
        }

        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();
            service.getAllClassification().done(function(classification_arr: Array<model.ClassificationDto>) {
                self.dataSource(classification_arr);
                dfd.resolve();
            }).fail(function(res) {
                dfd.reject(res);
            })
            dfd.resolve();
            return dfd.promise();
        }
    }


    export module model {
        export class ClassificationDto {
            classificationCode: string;
            classificationName: string;
            classificationMemo: string;
            constructor(classificationCode: string, classificationName: string) {
                this.classificationCode = classificationCode;
                this.classificationName = classificationName;
            }
        }

        export class RemoveClassificationCommand {
            classificationCode: string;
            constructor(classificationCode: string) {
                this.classificationCode = classificationCode;
            }
    
    }
            }
      class Classification {
            classificationCode: string;
            classificationName: string;
            classificationMemo: string;
            constructor(classificationCode: string, classificationName: string) {
                this.classificationCode = classificationCode;
                this.classificationName = classificationName;
            }
        }
    }