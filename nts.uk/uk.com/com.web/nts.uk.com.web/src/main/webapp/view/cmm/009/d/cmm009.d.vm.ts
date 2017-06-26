module cmm009.d.viewmodel {
    import option = nts.uk.ui.option;
    export class ScreenModel {

        startDate: KnockoutObservable<string>;
        startDateFromA: KnockoutObservable<string>;
        enableInput: KnockoutObservable<boolean>;
        endDate: KnockoutObservable<string>;
        valueSel001: KnockoutObservable<string>;
        startYmHis: KnockoutObservable<number>;
        timeEditorOption: KnockoutObservable<any>;
        //---radio        
        isRadioCheck: KnockoutObservable<number>;
        itemsRadio: KnockoutObservableArray<any>;
        isEnable: KnockoutObservable<boolean>;
        // time editor
        yearmonthdayeditor: any;
        /**
         * Init screen model.
         */
        constructor() {
            var self = this;
            self.isEnable = ko.observable(true);
            self.startDate = ko.observable("");
            self.enableInput = ko.observable(true);
            self.yearmonthdayeditor = {
                option: ko.mapping.fromJS(new nts.uk.ui.option.TimeEditorOption({
                    inputFormat: 'date'
                })),
            };
            self.startDateFromA = ko.observable(null);
            self.valueSel001 = ko.observable("");
            self.startYmHis = ko.observable(null);
            self.endDate = ko.observable(null);
            var data = nts.uk.ui.windows.getShared('itemHist');
            self.startDate(data.startDate);
            self.startDateFromA(data.startDate);
            self.endDate(data.endDate);
            //---radio
            self.itemsRadio = ko.observableArray([
                { value: 1, text: ko.observable('履歴を削除する') },
                { value: 2, text: ko.observable('履歴を修正する') }
            ]);
            self.isRadioCheck = ko.observable(2);
            let index = data.index;
            if (index != "0") {
                self.isEnable(false);
            }

            self.isRadioCheck.subscribe(function(codeChangeds) {
                if (codeChangeds == 1) {
                    self.enableInput(false);
                } else if (codeChangeds == 2) {
                    self.enableInput(true);
                }
            });
        }

        createHistory(): any {
            var self = this;
            if (self.isRadioCheck() == 2) {
                self.confirmDelete();
            } else if (self.isRadioCheck() == 1) {
                nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function() {
                    self.confirmDelete();
                }).ifNo(function() { });
            }
        }

        confirmDelete() {
            var self = this;
            var inputYm = $('#INP_STARTYMD').val();
            //check YM
            if (self.isRadioCheck() == 2) {
                if (inputYm == "") {
                    alert("開始年月日 が入力されていません。");
                    return false;
                }

                if (!nts.uk.time.parseYearMonthDate(inputYm).success) {
                    alert(nts.uk.time.parseYearMonthDate(inputYm).msg);
                    return false;
                }
                var startYMD = self.startDateFromA();
                var endYMD = self.endDate();
                var inputYm2 = inputYm.replace('/', '');
                inputYm2 = inputYm2.replace('/', '');
                startYMD = startYMD.replace('/', '');
                startYMD = startYMD.replace('/', '');
                endYMD = endYMD.replace('/', '');
                endYMD = endYMD.replace('/', '');
                if (+inputYm2 < +startYMD
                    || +inputYm2 == +startYMD
                    || +inputYm2 > +endYMD
                    || +inputYm2 == +endYMD) {
                    alert('履歴の期間が正しくありません。');
                    return false;
                }
                else {
                    var self = this;
                    var isRadio = self.isRadioCheck() + "";
                    nts.uk.ui.windows.setShared('newstartDate', self.startDate());
                    nts.uk.ui.windows.setShared('isradio', isRadio);
                    nts.uk.ui.windows.close();
                }
            } else {
                var self = this;
                var isRadio = self.isRadioCheck() + "";
                nts.uk.ui.windows.setShared('newstartDate', self.startDate());
                nts.uk.ui.windows.setShared('isradio', isRadio);
                nts.uk.ui.windows.close();
            }
        }

        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();
            dfd.resolve();
            // Return.
            return dfd.promise();
        }

        closeDialog(): any {
            nts.uk.ui.windows.close();
        }

    }
}