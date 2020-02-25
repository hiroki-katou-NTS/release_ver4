let gridColums = [
       { headerText: nts.uk.resource.getText('KSM015_13'), key: 'shiftMaterCode', width: 55 },
       { headerText: nts.uk.resource.getText('KSM015_14'), key: 'shiftMasterName', width: 125, formatter: _.escape },
       { headerText: nts.uk.resource.getText('KSM015_15'), key: 'workTypeName', width: 125, formatter: _.escape },
       { headerText: nts.uk.resource.getText('KSM015_16'), key: 'workTimeName', width: 125, formatter: _.escape }
];

let searchValueOption = ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
       textmode: "text",
       placeholder: "Placeholder for text editor",
       width: "120px",
       textalign: "left"
}));

class ShiftGridOption {
       gridColums: Array<any>;
       searchValueOption: String;

       constructor() {
              let self = this;
              self.gridColums = gridColums;
              self.searchValueOption = searchValueOption;

       }
}

class RegistrationForm {
       selectedCode: KnockoutObservable<String>;
       shiftMasterName: KnockoutObservable<String>;
       color: KnockoutObservable<String>;
       note: KnockoutObservable<String>;
       newMode: KnockoutObservable<Boolean>;
       workTypeCd: KnockoutObservable<String>;
       workTypeName: KnockoutObservable<String>;
       workTypeDisplay: KnockoutObservable<String>;
       workTimeSetCd: KnockoutObservable<String>;
       workTimeSetName: KnockoutObservable<String>;
       workTimeSetDisplay: KnockoutObservable<String>;

       constructor() {
              let self = this;
              self.selectedCode = ko.observable("");
              self.shiftMasterName = ko.observable("");
              self.color = ko.observable("#FFFFFF");
              self.note = ko.observable("");
              self.newMode = ko.observable(false);
              self.workTypeCd = ko.observable('');
              self.workTypeName = ko.observable('');
              self.workTypeDisplay = ko.observable('');
              self.workTypeCd.subscribe((val) => {
                     console.log(val);
                     self.workTypeDisplay(val + ' ' + self.shiftMasterName());
              });
              self.workTimeSetCd = ko.observable('');
              self.workTimeSetName = ko.observable('');
              self.workTimeSetDisplay = ko.observable('');
              self.workTimeSetCd.subscribe((val) => {
                     console.log(val);
                     self.workTimeSetDisplay(val + ' ' + self.workTimeSetName());
              });
       }

       public bindData(shiftMaster: ShiftMaster) {
              let self = this;
              if (shiftMaster) {
                     self.selectedCode(shiftMaster.shiftMaterCode);
                     self.shiftMasterName(shiftMaster.shiftMasterName);
                     self.color("#" + shiftMaster.color);
                     self.note(shiftMaster.remark);
                     self.newMode(false);
                     self.workTypeName(shiftMaster.workTypeName);
                     self.workTypeCd(shiftMaster.workTypeCd);
                     self.workTimeSetName(shiftMaster.workTimeName);
                     self.workTimeSetCd(shiftMaster.workTimeCd);
              }

       }

       public clearData() {
              let self = this;
              self.newMode(true);
              self.selectedCode('');
              self.shiftMasterName('');
              self.color("#FFFFFF");
              self.note('');
              self.workTypeName('');
              self.workTypeCd('');
              self.workTimeSetName('');
              self.workTimeSetCd('');
       }



}

interface ShiftMaster {
       companyId: String;
       shiftMasterName: String;
       shiftMaterCode: String;
       color: String;
       remark: String;
       workTypeCd: String;
       workTypeName: String;
       workTimeCd: String;
       workTimeName: String;
}

class RegisterShiftMasterDto {
       shiftMasterName: String;
       shiftMaterCode: String;
       color: String;
       remark: String;
       workTypeCd: String;
       workTimeSetCd: String;
       constructor(form: RegistrationForm) {
              this.shiftMasterName = form.shiftMasterName();
              this.shiftMaterCode = form.selectedCode();
              this.color = form.color().replace('#', '');
              this.remark = form.note();
              this.workTypeCd = form.workTypeCd();
              this.workTimeSetCd = form.workTimeSetCd();
       }
}