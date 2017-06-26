module qmm020.k.viewmodel {
    import option = nts.uk.ui.option;
    export class ScreenModel {
        currentItem: KnockoutObservable<any>;
        scrType: KnockoutObservable<string>;
        startYM: KnockoutObservable<string>;
        endYM: KnockoutObservable<string>;
        selectStartYm: KnockoutObservable<string>;
        startYmHis: KnockoutObservable<number>;
        timeEditorOption: KnockoutObservable<any>;
        //---radio        
        isRadioCheck: KnockoutObservable<number>;
        itemsRadio: KnockoutObservableArray<any>;
        enableYm: KnockoutObservable<boolean>;
        /**
         * Init screen model.
         */
        constructor() {
            var self = this;
            self.startYmHis = ko.observable(null);
            self.enableYm = ko.observable(false);
            self.scrType = ko.observable(nts.uk.ui.windows.getShared('scrType'));
            self.startYM = ko.observable(nts.uk.ui.windows.getShared('startYM'));
            self.endYM = ko.observable(nts.uk.ui.windows.getShared('endYM'));
            self.currentItem = ko.observable(nts.uk.ui.windows.getShared('currentItem'));
            self.selectStartYm = ko.observable(nts.uk.ui.windows.getShared('startYM'));

            if (self.scrType() === '1') {
                $('#K_LBL_005').parent().hide();
                $('#K_LBL_006').hide();
            }
            //console.log(option);
            self.timeEditorOption = ko.mapping.fromJS(new option.TimeEditorOption({ inputFormat: "yearmonth" }));
            //---radio
            self.itemsRadio = ko.observableArray([
                { value: 1, text: '履歴を削除する' },
                { value: 2, text: '履歴を修正する' }
            ]);
            self.isRadioCheck = ko.observable(1);
        }

        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();
            
            //checkbox change
            self.isRadioCheck.subscribe(function(newValue) {
                if (newValue === 2) {
                    self.enableYm(true);
                } else {
                    self.enableYm(false);
                }
            })
            
            dfd.resolve();
            // Return.
            return dfd.promise();
        }

        //Setting History 
        historyProcess(): any {
            var self = this;
            debugger;
            if(parseInt(self.selectStartYm())> parseInt($('#K_INP_001').val())){
                //$('#K_INP_001').ntsError('set',Error.ER023);
                nts.uk.ui.dialog.confirm("Do you want to say \"Hello World!\"?");
            }
            //履歴の編集-削除処理
            if (self.isRadioCheck() == 1) {
                service.delComAllot(self.currentItem()).done(function() {
                }).fail(function(res) {
                    alert(res);
                });
            //履歴を修正する
            } else {
                self.currentItem();
                var previousItem = nts.uk.ui.windows.getShared('previousItem');
                //Update current Item 
                var startValue = $('#K_INP_001').val().replace('/', '');
                self.currentItem().startDate = startValue;

                //previousItem.startDate = 
                service.updateComAllot(self.currentItem()).done(function() {
                }).fail(function(res) {
                    alert(res);
                });
                //Update previous iTem
                if (previousItem != undefined) {
                    previousItem.endDate = previousYM(startValue);
                    service.updateComAllot(previousItem).done(function() {
                    }).fail(function(res) {
                        alert(res);
                    });
                }
            }
            nts.uk.ui.windows.close();
        }

        //close function
        closeDialog(): any {
            nts.uk.ui.windows.close();
        }
    }
    
    
    enum Error {
        ER023 = <any>"履歴の期間が重複しています。",   
    }
    //Previous Month 
    function previousYM(sYm: string):number{
        var preYm: number = 0;
        if (sYm.length == 6) {
            let sYear: string = sYm.substr(0, 4);
            let sMonth: string = sYm.substr(4, 2);
            //Trong truong hop thang 1 thi thang truoc la thang 12
            if (sMonth == "01") {
                preYm = parseInt((parseInt(sYear) - 1).toString() + "12");
                //Truong hop con lai thi tru di 1      
            } else {
                preYm = parseInt(sYm) - 1;
            }
        }
        return preYm;
    }

}