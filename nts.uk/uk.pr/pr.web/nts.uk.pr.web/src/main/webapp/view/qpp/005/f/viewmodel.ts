module nts.uk.pr.view.qpp005.f {

    export module viewmodel {
        export class ScreenModel {
            totalCommuteCheck: CheckBox;
            totalCommuteEditor: NumberEditor;
            taxCommuteCheck: CheckBox;
            taxCommuteEditor: NumberEditor;
            oneMonthCheck: CheckBox;
            oneMonthCommuteEditor: NumberEditor;
            oneMonthRemainderEditor: NumberEditor;
            commuteDividedByMonth: CommuteDividedByMonth;
            commuteNotaxLimitItem: KnockoutObservable<any>;
            itemCode: KnockoutObservable<string>;
            itemName: KnockoutObservable<string>;

            constructor() {
                var self = this;

                /**
                 * 通勤費　合計
                 */
                self.totalCommuteCheck = new CheckBox();

                /**
                 * １ヵ月分通勤費
                 */
                self.oneMonthCheck = new CheckBox();
                self.oneMonthCheck.isChecked = ko.observable(false);

                /**
                 * 課税通勤費
                 */
                self.taxCommuteCheck = new CheckBox();
                self.taxCommuteCheck.isChecked = ko.observable(false);

                /**
                 * 通勤費合計 テキストボックス（金額）            
                 */
                self.totalCommuteEditor = new NumberEditor();
                self.totalCommuteEditor.isEnable = self.totalCommuteCheck.isChecked;

                /**
                 * 課税通勤費 テキストボックス（金額）
                 */
                self.taxCommuteEditor = new NumberEditor();
                self.taxCommuteEditor.isEnable = ko.computed(function() {
                    return self.totalCommuteCheck.isChecked() && self.taxCommuteCheck.isChecked();
                });

                /**
                 * 1か月分通勤費 テキストボックス（金額）
                 */
                self.oneMonthCommuteEditor = new NumberEditor();
                self.oneMonthCommuteEditor.isEnable = self.oneMonthCheck.isChecked;

                /**
                 * 余り（端数）
                 */
                self.oneMonthRemainderEditor = new NumberEditor();
                self.oneMonthRemainderEditor.isEnable = self.oneMonthCheck.isChecked;

                /**
                 * Commute items
                 */
                self.commuteDividedByMonth = new CommuteDividedByMonth();
                self.commuteDividedByMonth.isEnable = self.oneMonthCheck.isChecked;

                self.commuteNotaxLimitItem = ko.observable();

                /**
                 * item code, item name
                 */
                self.itemCode = ko.observable("");
                self.itemName = ko.observable("");


            }

            start(): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                var detailItemFromParentScreen = nts.uk.ui.windows.getShared('value');
                var employee = nts.uk.ui.windows.getShared('employee');
                var baseYearmonth = nts.uk.ui.windows.getShared('processingYM');

                // Set item code and item name value
                self.itemCode(detailItemFromParentScreen.itemCode);
                self.itemName(detailItemFromParentScreen.itemName);

                // Set value for 通勤費合計 textbox 
                self.totalCommuteEditor.value = ko.observable(detailItemFromParentScreen.value);

                // Set value for 課税通勤費 textbox 
                self.taxCommuteEditor.value = ko.observable(detailItemFromParentScreen.commuteAllowTaxImpose);

                // Set value for  1か月分通勤費 textbox 
                self.oneMonthCommuteEditor.value = ko.observable(detailItemFromParentScreen.commuteAllowMonth);
                if (self.oneMonthCommuteEditor.value()) {
                    self.oneMonthCheck.isChecked(true);
                    self.oneMonthCommuteEditor.isEnable(true);
                    self.oneMonthRemainderEditor.isEnable(true);
                    self.commuteDividedByMonth.isEnable(true);
                }

                // Set value for 余り textbox 
                self.oneMonthRemainderEditor.value = ko.observable(detailItemFromParentScreen.commuteAllowFraction);

                qpp005.f.service.getCommuteNotaxLimit(employee.personId, baseYearmonth).done(function(res: any) {
                    if (res != null) {
                        self.commuteNotaxLimitItem(new CommuteNotaxLimitItem(res.commuNotaxLimitCode,
                            res.commuNotaxLimitName,
                            res.commuNotaxLimitValue));
                    } else {
                        self.commuteNotaxLimitItem(new CommuteNotaxLimitItem("00", "未設定", ""));
                    }
                })

                return dfd.promise();
            }

            settingButtonClick() {
                var self = this;
                var detailItemFromParentScreen = nts.uk.ui.windows.getShared('value');

                // Check checked item
                if (!self.totalCommuteCheck.isChecked() && !self.oneMonthCheck.isChecked()) {
                    alert("対象データがありません。");
                    return;
                }

                // Check item is exist
                if (detailItemFromParentScreen.itemCode == "") {
                    alert("更新対象のデータが存在しません。");
                    return;
                }

                nts.uk.ui.windows.setShared('totalCommuteEditor', ko.toJS(self.totalCommuteEditor.value));
                nts.uk.ui.windows.setShared('taxCommuteEditor', ko.toJS(self.taxCommuteEditor.value));
                nts.uk.ui.windows.setShared('oneMonthCommuteEditor', ko.toJS(self.oneMonthCommuteEditor.value));
                nts.uk.ui.windows.setShared('oneMonthRemainderEditor', ko.toJS(self.oneMonthRemainderEditor.value));
                nts.uk.ui.windows.close();
            }

            cancelButtonClick() {
                nts.uk.ui.windows.close();
            }
        }

        export class CheckBox {
            isChecked: KnockoutObservable<boolean>;

            constructor() {
                var self = this;
                self.isChecked = ko.observable(true);
            }
        }

        export class NumberEditor {
            isEnable: KnockoutObservable<boolean>;
            value: KnockoutObservable<string>;
            width: string;
            option: ui.option.CurrencyEditorOption;

            constructor() {
                var self = this;
                self.option = ko.mapping.fromJS(new ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    decimallength: 0,
                    currencyformat: 'JPY',
                    width: "80"
                }));
            }
        }

        class CommuteDividedByMonth {
            commuteItems: KnockoutObservable<any>;
            selectedCode: KnockoutObservable<string>;
            isEnable: KnockoutObservable<boolean>;

            constructor() {
                var self = this;
                self.commuteItems = ko.observableArray([
                    new CommuteDividedItemsByMonth("1", "１ヶ月"),
                    new CommuteDividedItemsByMonth("2", "２ヶ月"),
                    new CommuteDividedItemsByMonth("3", "３ヶ月"),
                    new CommuteDividedItemsByMonth("4", "４ヶ月"),
                    new CommuteDividedItemsByMonth("5", "５ヶ月"),
                    new CommuteDividedItemsByMonth("6", "６ヶ月"),
                ])
                self.selectedCode = ko.observable("1");
            }
        }

        function CommuteNotaxLimitItem(code, name, value) {
            this.code = code;
            this.name = name;
            this.value = nts.uk.ntsNumber.formatNumber(value, { grouplength: 3 });
        }

        function CommuteDividedItemsByMonth(code, name) {
            this.code = code;
            this.name = name;
        }
    }
}