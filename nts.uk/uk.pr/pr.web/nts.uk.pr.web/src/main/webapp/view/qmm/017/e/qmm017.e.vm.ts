module nts.qmm017 {
    export class EScreen {
        listBoxItemType: KnockoutObservable<ListBox>;
        listBoxItems: KnockoutObservable<ListBox>;
        description: KnockoutObservableArray<string>;

        constructor() {
            var self = this;
            self.description = ko.observableArray([]);
            var eList001 = [
                { code: '1', name: '全て' }
            ];
            let eList002 = [
                { code: '1', name: '条件式' },
                { code: '2', name: 'かつ' },
                { code: '3', name: 'または' },
                { code: '4', name: '四捨五入' },
                { code: '5', name: '切り捨て' },
                { code: '6', name: '切り上げ' },
                { code: '7', name: '最大値' },
                { code: '8', name: '最小値' },
                { code: '9', name: '家族人数' },
                { code: '10', name: '年月加算' },
                { code: '11', name: '年抽出' },
                { code: '12', name: '月抽出' }
            ];
            self.listBoxItemType = ko.observable(new ListBox(eList001));
            self.listBoxItems = ko.observable(new ListBox([]));
            self.listBoxItemType().selectedCode.subscribe(function(codeChange) {
                if (codeChange === '1') {
                    self.listBoxItems().itemList(eList002);
                }
            });
            
            self.listBoxItems().selectedCode.subscribe(function(codeChange) {
                if (codeChange === '1') {
                    self.description([]);
                    self.description.push('関数＠条件式（＜式＞ , ＜結果①＞ , ＜結果②＞ ）');
                    self.description.push('＜式＞の内容が正しい場合、＜結果①＞とする');
                    self.description.push('＜式＞の内容が間違っている場合、＜結果②＞とする');
                    self.description.push('※＜結果①＞、＜結果②＞には値のほか、式を指定することも可能です。');
                    self.description.push('例）皆勤手当て計算式');
                    self.description.push('条件式（　勤怠＠欠勤日数　≦　0 , 5000 , 0 )');
                    self.description.push('　欠勤日数が０日以下の場合、5000円');
                    self.description.push('　欠勤日数が０日を超える場合、0円');
                } else if (codeChange === '2') {
                    self.description([]);
                    self.description.push('関数＠かつ（＜式1＞,＜式2>, ・・・　）');
                    self.description.push('※＜式＞は３つ以上記載してもよい。');
                    self.description.push('複数の式をまとめて評価する場合に利用します。');
                    self.description.push('論理和（AND条件）です。');
                    self.description.push('括弧内の式が全て正しい場合、正しいものとして評価する');
                    self.description.push('括弧内の式に1つでも誤りがあれば、誤りとして評価する');
                } else if (codeChange === '3') {
                    self.description([]);
                    self.description.push('関数＠または（＜式1＞,　＜式2＞ , ・・・　）');
                    self.description.push('※＜式＞は３つ以上記載してもよい。');
                    self.description.push('複数の式をまとめて評価する場合に利用します。');
                    self.description.push('論理積（OR条件）です。');
                    self.description.push('括弧内の式に1つでも正しいものがあれば、正しいものとして評価する');
                    self.description.push('カッコ内の式が全て誤っている場合、誤りとして評価する');
                } else if (codeChange === '4') {
                    self.description([]);
                    self.description.push('関数＠四捨五入（　＜値＞　）');
                    self.description.push('＜値＞に小数点以下の桁が含まれている場合、小数第1位を四捨五入します');
                    self.description.push('例）　四捨五入（ 1.5 ) の場合、結果は２です。');
                    self.description.push('　　　　四捨五入（ 1.4 ) の場合、結果は１です。');
                } else if (codeChange === '5') {
                    self.description([]);
                    self.description.push('関数＠切捨て（ ＜値＞ ）');
                    self.description.push('＜値＞に小数点以下の桁が含まれている場合、小数点以下を切捨てます');
                    self.description.push('例）　切捨て（1.5 ）の場合、結果は１です。');
                    self.description.push('　　　　切捨て（1.4 ）の場合、結果は１です。');
                } else if (codeChange === '6') {
                    self.description([]);
                    self.description.push('関数＠切上げ（ ＜値＞ ）');
                    self.description.push('＜値＞に小数点以下の桁が含まれている場合、小数第1位を切上げます');
                    self.description.push('例）　切上げ（1.5）の場合、結果は2です。');
                    self.description.push('　　　　切上げ（1.4）の場合、結果は2です。');
                } else if (codeChange === '7') {
                    self.description([]);
                    self.description.push('関数＠最大値（＜値1＞ , ＜値2＞ , ・・・　） ');
                    self.description.push('※＜値＞は3つ以上記載してもよい');
                    self.description.push('括弧内の＜値＞の中で最も大きい＜値＞を返します。');
                } else if (codeChange === '8') {
                    self.description([]);
                    self.description.push('"関数＠最小値（＜値1＞ , ＜値2＞ , ・・・　）');
                    self.description.push('※＜値＞は3つ以上記載してもよい');
                    self.description.push('括弧内の＜値＞の中で最も小さい＜値＞を返します。');
                } else if (codeChange === '9') {
                    self.description([]);
                    self.description.push('関数＠家族人数（ ＜年齢下限＞、＜年齢上限＞）');
                    self.description.push('登録されている家族情報のうち、指定した年齢の範囲内の');
                    self.description.push('家族の人数を返します。');
                } else if (codeChange === '10') {
                    self.description([]);
                    self.description.push('関数＠月加算（　＜年月＞ ,　＜加算月数＞ ）');
                    self.description.push('＜年月＞（西暦年/月）に＜加算月数＞（整数）を加算した年月を返します。');
                } else if (codeChange === '11') {
                    self.description([]);
                    self.description.push('関数＠年抽出（ ＜年月＞ ）');
                    self.description.push('＜年月＞（西暦年/月）から年（西暦年）を返します。');
                } else if (codeChange === '12') {
                    self.description([]);
                    self.description.push('"関数＠月抽出（ ＜年月＞ ）');
                    self.description.push('＜年月＞（西暦年/月）から月を返します。');
                }
            });
        }
    }
}