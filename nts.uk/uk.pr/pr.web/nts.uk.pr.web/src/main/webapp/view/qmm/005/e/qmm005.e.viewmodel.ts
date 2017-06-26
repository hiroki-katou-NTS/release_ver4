/// <reference path="../qmm005.ts"/>
module qmm005.e {
    export class ViewModel {
        index: KnockoutObservable<number> = ko.observable(0);
        lbl003: KnockoutObservable<string> = ko.observable('');
        lbl005: KnockoutObservable<number> = ko.observable(0);
        lbl006: KnockoutObservable<string> = ko.observable('');

        sel001: KnockoutObservable<number> = ko.observable(1);
        sel001Data: KnockoutObservableArray<common.SelectItem> = ko.observableArray([]);

        sel002: common.CheckBoxItem = new common.CheckBoxItem({ enable: true, checked: true, text: '支払日', helper: { id: 'E_LBL_012', text: ko.observable('処理月の末日') } }); // Ngày trả lương (ngày cuối cùng của tháng xử lý)
        sel003: common.CheckBoxItem = new common.CheckBoxItem({ enable: true, checked: true, text: '対象者抽出基準日', helper: { id: 'E_LBL_013', text: ko.observable('処理月の当月15日') } }); // Ngày (cơ bản) xuất ra đối tượng (điều chuyển)
        sel004: common.CheckBoxItem = new common.CheckBoxItem({ enable: true, checked: true, text: '社会保険徴収月', helper: { id: 'E_LBL_014', text: ko.observable('処理月当月') } }); //Ngày thu bảo hiểm xã hội (tháng hiện tại)
        sel005: common.CheckBoxItem = new common.CheckBoxItem({ enable: true, checked: true, text: '明細書印字年月', helper: { id: 'E_LBL_015', text: ko.observable('常に活性') } }); //Tháng, năm in phiếu (thông tin) (luôn luôn enable).
        sel006: common.CheckBoxItem = new common.CheckBoxItem({ enable: true, checked: true, text: '社会保険基準日', helper: { id: 'E_LBL_016', text: ko.observable('処理月の前月末日') } }); // Base date của bảo hiểm xã hội (ngày cuối cùng của tháng trước)
        sel007: common.CheckBoxItem = new common.CheckBoxItem({ enable: true, checked: true, text: '雇用保険基準日', helper: { id: 'E_LBL_017', text: ko.observable('処理月の直前の４月１日') } }); // Base date của bảo hiểm thất nghiệp (ngày trước đấy của ngày 1/4)
        sel008: common.CheckBoxItem = new common.CheckBoxItem({ enable: false, checked: true, text: '所得税基準日', helper: { id: 'E_LBL_018', text: ko.observable('処理月の習前の１月１日') } }); // Base date của thuế thu nhập (ngày 1/1 tiếp theo)
        sel009: common.CheckBoxItem = new common.CheckBoxItem({ enable: true, checked: true, text: '経理締め日', helper: { id: 'E_LBL_019', text: ko.observable('処理月の前月15日') } }); // Ngày chốt (tháng trước của tháng xử lý)
        dataSources: Array<any> = [];
        dirty = {
            
            isDirty: function() {
            },
            reset: function() {
            }
        }
        constructor() {
            let self = this;

            self.lbl005.subscribe(function(v) {
                if (v) {
                    self.lbl006("(" + v["yearInJapanEmpire"]() + ")");
                } else {
                    self.lbl006("");
                }
            });

            self.start();
        }

        start() {
            let self = this;

            let dataRow = nts.uk.ui.windows.getShared('dataRow');
            let viewModelB = nts.uk.ui.windows.getShared('viewModelB');

            // Default value
            self.sel001(1);

            let sel001Data: Array<common.SelectItem> = [];
            for (let i = 1; i <= 12; i++) {
                sel001Data.push(new common.SelectItem({ index: i, label: i.toString() }));
            }
            self.index(dataRow.index());
            self.sel001Data(sel001Data);
            self.lbl003(dataRow.index() + "." + dataRow.label());

            // Year
            self.lbl005(viewModelB.inp001());

            services.getData(self.index()).done(function(resp: Array<any>) {
                if (resp && resp[0] && resp[1]) {
                    self.dataSources = resp;
                    self.sel003.helper.text("処理月の当月" + resp[0].payStdDay + "日");
                    self.sel007.helper.text("処理月の直前の" + resp[1].empInsStdMon + "月" + resp[1].empInsStdDay + "日");
                    self.sel008.helper.text("処理月の習前の" + resp[1].incometaxStdMon + "月" + resp[1].incometaxStdDay + "日");
                    self.sel009.helper.text("処理月の前月" + resp[0].accountDueDay + "日");
                }
            });
        }

        saveData() {
            let self = this,
                model = {
                    sel003: self.sel003.checked(),
                    sel004: self.sel004.checked(),
                    sel005: self.sel005.checked(),
                    sel006: self.sel006.checked(),
                    sel007: self.sel007.checked(),
                    sel008: self.sel008.checked(),
                    sel009: self.sel009.checked(),
                    flectionStartMonth: self.sel001(),
                    payStdDay: self.dataSources[0] && self.dataSources[0].payStdDay,
                    empInsStdMon: self.dataSources[1] && self.dataSources[1].empInsStdMon,
                    empInsStdDay: self.dataSources[1] && self.dataSources[1].empInsStdDay,
                    incometaxStdMon: self.dataSources[1] && self.dataSources[1].incometaxStdMon,
                    incometaxStdDay: self.dataSources[1] && self.dataSources[1].incometaxStdDay,
                    accountDueDay: self.dataSources[0] && self.dataSources[0].accountDueDay
                };

            // Share data to global and close dialog
            nts.uk.ui.windows.setShared('viewModelE', model);
            self.closeDialog();
        }

        closeDialog() { nts.uk.ui.windows.close(); }
    }
}