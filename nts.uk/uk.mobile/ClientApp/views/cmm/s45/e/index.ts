import { Vue, _ } from '@app/provider';
import { component, Prop } from '@app/core/component';
import { CmmS45FComponent } from '../f/index';

@component({
    name: 'cmms45e',
    style: require('./style.scss'),
    template: require('./index.vue'),
    resource: require('./resources.json'),
    validations: {
        reasonRemand: {
            required: true
        }
    },
    constraints: [],
    components: {
        'cmms45f': CmmS45FComponent
    }
})
export class CmmS45EComponent extends Vue {
    public title: string = 'CmmS45E';

    @Prop({ default: () => ({ listAppMeta: [], currentApp: '', version: 0}) })
    public readonly params: { listAppMeta: Array<string>, currentApp: string, version: number };
    public reasonRemand: string = null;
    public selectedValue: string = '1';
    public apprList: Array<{id: string, content: string}> = [{id: '1', content: ''}];
    public appInfo: any = null;
    
    public created() {
        let self = this;
        self.$http.post('at', servicePath.getAppInfoByAppID, [self.params.currentApp]).then((result: any) => {
            self.appInfo = result.data;
            if (!self.$data) {
                return;
            }
            let lstAppr = [];
            lstAppr.push({
                sId: self.appInfo.applicantId,
                atr: 0,
                name: self.appInfo.applicantName,
                phaseOrder: null
            });
            self.appInfo.lstApprover.forEach(function(approver: any) {
                lstAppr.push({
                    sId: approver.approverID,
                    atr: approver.agentFlag ? 2 : 1,
                    name: approver.approverName,
                    phaseOrder: approver.phaseOrder
                });
            });
            self.apprList = self.creatContent(lstAppr);
            self.selectedValue = self.appInfo.applicantId;
        });
    }
    //戻る
    private callBack() {
        //「D：申請内容確認（承認）」に戻る
        this.$close();
    }
    //差し戻す
    private remand() {
        let self = this;
        self.$validate();
        if (!self.$valid) {
            return;
        }
        // 確認メッセージ（Msg_384）を表示する
        self.$modal.confirm('Msg_384').then((res) => {
            if (res == 'no') {
                return;
            }
            self.$mask('show');
            let phaseOrder = _.find(self.appInfo.lstApprover, (c) => c.approverID == self.selectedValue).phaseOrder;
            let remandParam = {
                appID: [self.params.currentApp],
                applicaintName: self.appInfo.applicantName,//申請本人の名前
                order: phaseOrder,//差し戻し先
                remandReason: self.reasonRemand,//差し戻しコメント
                version: self.params.version
            };
            // アルゴリズム「差戻処理」を実行する
            self.$http.post('at', servicePath.remand, remandParam).then((res) => {
                self.$mask('hide');
                console.log('remand');
                // 「F：処理完了」画面に遷移する
                this.$modal('cmms45f', { action: 3 }).then((result: any) => {
                    self.$close(result.backToMenu);
                });
            }).catch((res) => {
                self.$modal.error(res.messageId).then(() => {
                    self.$mask('hide');
                });
            });
        });
    }

    private creatContent(lstAppr: Array<IApproverInfo>) {
        let lstResult = [];
        lstAppr.forEach((appr) => {
            let contentApp = '';
            if (appr.atr == 0) {//申請者
                contentApp = this.$i18n('CMMS45_71') + appr.name;
            }
            if (appr.atr == 1) {//承認者
                contentApp = this.$i18n('CMMS45_72', appr.phaseOrder.toString()) + appr.name;
            }
            if (appr.atr == 2) {//代行者
                contentApp = this.$i18n('CMMS45_72', appr.phaseOrder.toString()) + appr.name + this.$i18n('CMMS45_73');
            }
            lstResult.push({ id: appr.sId, content: contentApp});
        });

        return lstResult;
    }
}

const servicePath = {
    getAppInfoByAppID: 'at/request/application/getAppInfoForRemandByAppId',
    remand: 'at/request/application/remandapp'
};

interface IApproverInfo {
    sId: string;//社員Id
    atr: number;//
    name: string;//
    phaseOrder: number;//
}