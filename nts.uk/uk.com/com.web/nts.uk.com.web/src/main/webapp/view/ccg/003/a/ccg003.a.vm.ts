/// <reference path='../../../../lib/nittsu/viewcontext.d.ts' />
module nts.uk.com.view.ccg003.a {

  const API = {
    // <<ScreenQuery>> 社員のお知らせの画面を表示する
    getEmployeeNotification: 'sys/portal/notice/getEmployeeNotification',
    // <<ScreenQuery>> 社員が宛先のお知らせの内容を取得する
    getContentOfDestinationNotification: 'sys/portal/notice/getContentOfDestinationNotification',
    // <<Command>> お知らせを閲覧する
    viewMessageNotice: 'sys/portal/notice/viewMessageNotice',
    // <<Command>> 個人の記念日を閲覧する
    updateAnnivesaryNotice: 'ctx/bs/person/personal/anniversary/updateAnnivesaryNotice'
  };

  const urlRegex = /(((https?:\/\/)|(www\.))[^\s]+)/g;

  @component({
    name: 'ccg003-component',
    template: '/nts.uk.com.web/view/ccg/003/a/index.html'
  })
  @bean()
  export class ViewModel extends ko.ViewModel {
    isStartScreen = true;
    formatType = 'YYYY/MM/DD';
    dateValue: KnockoutObservable<DatePeriod> = ko.observable(new DatePeriod({
      startDate: moment.utc().format(this.formatType),
      endDate: moment.utc().format(this.formatType)
    }));
    systemDate: KnockoutObservable<string> = ko.observable('');

    // Map<お知らせメッセージ、作成者> (List)
    msgNotices: KnockoutObservableArray<MsgNotices> = ko.observableArray([]);
    // Map<個人の記念日情報、新記念日Flag> (List)
    anniversaries: KnockoutObservableArray<AnniversaryNotices> = ko.observableArray([]);
    // ロール
    roleFlag: KnockoutObservable<boolean> = ko.observable(true);
    role: KnockoutObservable<Role> = ko.observable(new Role());

    created() {
      const vm = this;
      vm.$blockui('show');
      vm.$ajax('com', API.getEmployeeNotification)
        .then((response: EmployeeNotification) => {
          if (response) {
            vm.anniversaries(response.anniversaryNotices);
            const msgNotices = vm.listMsgNotice(response.msgNotices);
            vm.msgNotices(msgNotices);
            vm.role(response.role);
            vm.roleFlag(response.role.employeeReferenceRange !== 3);
            vm.systemDate(moment.utc(response.systemDate).locale('ja').format('YYYY/M/D(dd)'));
          }
        })
        .fail(error => vm.$dialog.error(error))
        .always(() => vm.$blockui('hide'));
    }

    mounted() {
      const vm = this;
      const elementId ='#notice-msg';
      $('#A0').ntsPopup({
        trigger: elementId,
        position: {
          my: 'right top',
          at: 'right bottom',
          of: $('#user')
        },
        showOnStart: false,
        dismissible: true
      });

      $(elementId).click(() => {
        $('#A0').ntsPopup('show');
      });
      $('#top-title').dblclick(e => e.preventDefault());
      $('#top-title').click(() => {
        const maxHeight = $('.auto-overflow').css('max-height');
        if (maxHeight === '320px') {
          $('.auto-overflow').css('max-height', '385px');
        } else {
          $('.auto-overflow').css('max-height', '320px');
        }

        if (!_.isEmpty(vm.anniversaries()) || !_.isEmpty(vm.msgNotices())) {
          $('#A4').css('border-bottom', 'unset');
        } else {
          $('#A4').css('border-bottom', '1px groove');
        }
      });
    }

    /**
     * A4_3:絞込をクリックする
     */
    onClickFilter(): void {
      const vm = this;
      vm.$blockui('show');
      const startDate = moment.utc(vm.dateValue().startDate, vm.formatType);
      const endDate = moment.utc(vm.dateValue().endDate, vm.formatType);
      const baseDate = moment.utc(new Date(), vm.formatType);
      if (startDate.isAfter(baseDate) || endDate.isAfter(baseDate)) {
        vm.$dialog.error({ messageId: 'Msg_1833' });
        vm.$blockui('hide');
        return;
      }
      vm.anniversaries([]);
      vm.msgNotices([]);

      const param: DatePeriod = new DatePeriod({
        startDate: startDate.toISOString(),
        endDate: endDate.toISOString()
      });
      vm.$ajax('com', API.getContentOfDestinationNotification, param)
        .then((response: DestinationNotification) => {
          if (response) {
            vm.anniversaries(response.anniversaryNotices);
            const msgNotices = vm.listMsgNotice(response.msgNotices);
            vm.msgNotices(msgNotices);

            if (!_.isEmpty(response.anniversaryNotices || !_.isEmpty(response.msgNotices))) {
              $('#A4').css('border-bottom', 'unset');
            }
          }
        })
        .fail(error => vm.$dialog.error(error))
        .always(() => vm.$blockui('hide'));
    }

    listMsgNotice(messages: any[]): MsgNotices[] {
      const vm = this;
      if (_.isEmpty(messages)) {
        return [];
      }
      return _.map(messages, (item: any) => {
        const msg = new MsgNotices();
        msg.creator = item.creator;
        msg.flag = item.flag;
        msg.message = item.message;
        msg.dateDisplay = item.message ? `${item.message.startDate} ${vm.$i18n('CCG003_15')} ${item.message.endDate}` : '';
        msg.messageDisplay = vm.replaceUrl(item.message.notificationMessage);
        return msg;
      });
    }

    replaceUrl(text: string): string {
      return text.replace(urlRegex, (url, b, c) => {
        const url2 = (c == 'www.') ? 'http://' + url : url;
        return '<a style="color: blue !important;" href="' + url2 + '" target="_blank">' + url + '</a>';
      });
    }

    /**
     * A5、アコーディオンを広げて内容を表示する
     */
    onClickAnniversary(index: any): void {
      const vm = this;
      if (_.isNil(vm.anniversaries()[index()])) {
        return;
      }
      if (!vm.anniversaries()[index()].flag) {
        return;
      }
      const command = {
        personalId: vm.anniversaries()[index()].anniversaryNotice.personalId,
        anniversary: vm.anniversaries()[index()].anniversaryNotice.anniversary,
        referDate: moment.utc(vm.dateValue().endDate).toISOString(),
      }
      vm.$blockui('show');
      vm.$ajax('com', API.updateAnnivesaryNotice, command)
        .then(() => {
          const anniversaries = vm.anniversaries();
          anniversaries[index()].flag = false;
          vm.anniversaries(anniversaries);
        })
        .fail(error => vm.$dialog.error(error))
        .always(() => vm.$blockui('hide'));
    }

    /**
     * A6、アコーディオンを広げて内容を表示する
     */
    onClickMessageNotice(creator: string, inputDate: string, index: any): void {
      const vm = this;
      if (!vm.msgNotices()[index()].flag) {
        return;
      }
      const command: any = {
        msgInfors: [{
          creatorId: creator,
          inputDate: inputDate
        }],
        sid: __viewContext.user.employeeId
      }
      vm.$blockui('show');
      vm.$ajax('com', API.viewMessageNotice, command)
        .then(() => {
          const msgNotices = vm.msgNotices();
          msgNotices[index()].flag = false;
          vm.msgNotices(msgNotices);
        })
        .fail(error => vm.$dialog.error(error))
        .always(() => vm.$blockui('hide'));
    }

    /**
     * A2:メッセージ入力のリンクをクリックする
     */
    openScreenB(): void {
      const vm = this;
      vm.$window.modal('/view/ccg/003/b/index.xhtml', vm.role())
        .then(() => vm.onClickFilter());
    }

    closeWindow(): void {
      $('#A0').ntsPopup('hide');
    }
  }

  class DestinationNotification {
    msgNotices: MsgNotices[];
    anniversaryNotices: AnniversaryNotices[];
  }

  class DatePeriod {
    startDate: string;
    endDate: string;

    constructor(init?: Partial<DatePeriod>) {
      $.extend(this, init);
    }
  }

  class EmployeeNotification {
    msgNotices: MsgNotices[];
    anniversaryNotices: AnniversaryNotices[];
    role: Role;
    systemDate: string;
  }

  class MsgNotices {
    message: MessageNotice;
    creator: string;
    dateDisplay: string;
    flag: boolean;
    messageDisplay: string;
  }

  class AnniversaryNotices {
    anniversaryNotice: AnniversaryNoticeImport;
    flag: boolean;
  }

  class AnniversaryNoticeImport {
    personalId: string;
    noticeDay: number;
    seenDate: string;
    anniversary: string;
    anniversaryTitle: string;
    notificationMessage: string;
    displayDate: string;
  }

  class Role {
    companyId: string;
    roleId: string;
    roleCode: string;
    roleName: string;
    assignAtr: number;
    employeeReferenceRange: number;
  }

  class MessageNotice {
    creatorID: string;
    inputDate: string;
    modifiedDate: string;
    targetInformation: any;
    startDate: any;
    endDate: any;
    employeeIdSeen: string[];
    notificationMessage: string;
  }

}
