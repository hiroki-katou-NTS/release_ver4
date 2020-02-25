module nts.uk.at.view.kdl044.a {

    import getShared = nts.uk.ui.windows.getShared;
    import setShared = nts.uk.ui.windows.setShared;
    import block = nts.uk.ui.block;
    import getText = nts.uk.resource.getText;
    import alertError = nts.uk.ui.dialog.alertError;

    export module viewmodel {
        export class ScreenModel {
            columns: KnockoutObservableArray<any>;
            listShifuto: KnockoutObservableArray<Shifuto> = ko.observableArray( [] );
            //listShifuto: Array<IShifuto> = [];
            selectedCodes: KnockoutObservableArray<String> = ko.observableArray( [] );
            isMultiSelect: KnockoutObservable<boolean> = ko.observable( true );
            placeHolders: string = "";
            dataTransfer: KnockoutObservable<any> = ko.observable();
            constructor() {
                let self = this;

                let holders = getText( 'KDL044_2' ) + '・'
                    + getText( 'KDL044_3' ) + '・'
                    + getText( 'KDL044_4' ) + '・'
                    + getText( 'KDL044_5' ) + '・'
                    + getText( 'KDL044_6' ) + '・'
                    + getText( 'KDL044_8' ) + 'で検索…';
                self.placeHolders = holders.replace( "１", "" );
            }

            startPage(): JQueryPromise<any> {
                let self = this;

                block.invisible();
                let dfd = $.Deferred();
                let data: IDataTransfer = getShared( 'kdl044Data' );
                if ( data == null )
                    return;
                self.dataTransfer( data );
                self.isMultiSelect( data.isMultiSelect );
                let shifutos: Array<Shifuto> = [];
                //UI処理[1]
                if ( data.permission ) {
                    shifutos.push( new Shifuto(
                        "",
                        "",
                        getText( 'KDL044_13' ),
                        "",
                        "",
                        "",
                        ""
                    ) );
                }

                let paras: any;
                switch ( data.filter ) {
                    case 0: {
                        paras = {targetUnit: null, workplaceIds: null, workplaceGroupID: null}; 
                        break;
                    }
                    case 1: {
                        paras = {targetUnit: 0, workplaceIds: data.filterIDs, workplaceGroupID: null}; 
                        break;
                    }
                    case 2: {
                        paras = {targetUnit: 1, workplaceIds: null, workplaceGroupID: data.filterIDs}; 
                        break;
                    }
                }
                service.getShiftMaster( paras ).done( function( result ) {

                    //TO DO(chua viet) : get isUse
                    /**ドメインモデル「複数回勤務管理」により制御
                     * 使用する:○
                     * 使用しない:×
                     */
                    let isUse = true;
                    self.createHeader( isUse );
                    self.listShifuto(result);
                    self.listShifuto( _.sortBy(result, [function(o) { return new Number(o.code); }]) );
                    if ( data.shifutoCodes != null ) {
                        self.selectedCodes( data.shifutoCodes );
                    }
                    
                    
                    dfd.resolve();
                } ).fail( function( res: any ) {
                    alertError( { messageId: "" } );
                    block.clear();
                } );
                block.clear();
                return dfd.promise();
            }

            createHeader( isUse: boolean ) {
                if ( !isUse ) {
                    self.columns = ko.observableArray( [
                        { headerText: getText( 'KDL044_2' ), key: "shiftMasterCode", dataType: "string", width: 50 },
                        { headerText: getText( 'KDL044_3' ), key: "shiftMasterName", dataType: "string", width: 100 },
                        { headerText: getText( 'KDL044_4' ), key: "workTypeName", dataType: "string", width: 100 },
                        { headerText: getText( 'KDL044_5' ), key: "workTimeName", dataType: "string", width: 100 },
                        { headerText: getText( 'KDL044_6' ), key: "workTime1", dataType: "string", width: 200 },
                        { headerText: getText( 'KDL044_8' ), key: "remark", dataType: "string", width: 300 }
                    ] );
                }
                else {
                    self.columns = ko.observableArray( [
                        { headerText: getText( 'KDL044_2' ), key: "shiftMasterCode", dataType: "string", width: 50 },
                        { headerText: getText( 'KDL044_3' ), key: "shiftMasterName", dataType: "string", width: 100 },
                        { headerText: getText( 'KDL044_4' ), key: "workTypeName", dataType: "string", width: 100 },
                        { headerText: getText( 'KDL044_5' ), key: "workTimeName", dataType: "string", width: 100 },
                        { headerText: getText( 'KDL044_6' ), key: "workTime1", dataType: "string", width: 150 },
                        { headerText: getText( 'KDL044_7' ), key: "workTime2", dataType: "string", width: 150 },
                        { headerText: getText( 'KDL044_8' ), key: "remark", dataType: "string", width: 200 }
                    ] );
                }
            }

            decide() {
                let self = this;
                block.invisible();
                /**
                 * UI処理[2]
                 * 選択状態チェック
                 * 画面パラメータ[未選択許可区分]＝False and A2_1[シフト選択]の選択状態:  未選択    →   エラー
                 */
                if ( !self.dataTransfer().permission && self.selectedCodes().length == 0 ) {
                    alertError( { messageId: "Msg_1629" } );
                    block.clear();
                    return;
                }
                let selectedCodes = self.selectedCodes();
                setShared( 'kdl044ShifutoCodes', self.selectedCodes());
                let kdl044ShifutoData = _.filter(self.listShifuto(), (val) => {return self.selectedCodes().indexOf(val.shiftMasterCode ) !== -1});
                setShared( 'kdl044ShifutoData', kdl044ShifutoData);
                setShared( 'kdl044_IsCancel', false );
                block.clear();
                nts.uk.ui.windows.close();
            }

            closeDialog() {
                setShared( 'kdl044_IsCancel', true );
                nts.uk.ui.windows.close();
            }
        }

        class Shifuto {
            shiftMasterCode: string;
            shiftMasterName: string;
            workTypeName: string;
            workTimeName: string;
            workTime1: string;
            workTime2: string;
            remark: string;
            constructor( code: string,
                name: string,
                workType: string,
                workTime: string,
                time1: string,
                time2: string,
                remark: string ) {
                let self = this;
                self.shiftMasterCode = code;
                self.shiftMasterName = name;
                self.workTypeName = workType;
                self.workTimeName = workTime;
                self.workTime1 = time1;
                self.workTime2 = time2;
                self.remark = remark;
            }
        }

        //Data from other screens
        export interface IDataTransfer {

            /**
             * 選択モード                               
             *  複数選択: true, 単一選択 : false   
             */
            isMultiSelect: boolean,

            /**
             * 未選択許可区分
             * True：選択肢[なし]を表示する
             * False：選択肢[なし]を表示しない"
             */
            permission: boolean,

            /**
             * 絞り込みしない:0/職場:1/職場グループ:2
             * ※[絞り込みしない] →　全件表示
             */
            filter: number,

            /**
             * List<絞り込み対象ID>                               
             * 上記絞り込みを行う対象の職場/職場グループ    
            */
            filterIDs?: Array<string>,

            /**
             * List<選択済みシフトマスタコード>
             * 画面起動時に選択状態とするシフトマスタ      
             */
            shifutoCodes?: Array<string>       
        }
    }
}