module nts.uk {

    export module format {

        export interface IFormatter {
            format(source: any): string;
        }

        export interface IFormatterOption {
            constraintName: string
        }

        export class NoFormatter implements format.IFormatter {
            format(source: any): string {
                return source;
            }
        }
    }

    export module text {

        var regexp = {
            allHalfNumeric: /^\d*$/,
            allHalfAlphabet: /^[a-zA-Z]*$/,
            allHalfAlphanumeric: /^[a-zA-Z0-9]*$/,
            allHalfKatakanaReg: /^[ｱ-ﾝｧ-ｫｬ-ｮｯｦ ﾞﾟ｡.ｰ､･'-]*$/,
            allFullKatakanaReg: /^[ァ-ー　。．ー、・’－ヴヽヾ]*$/,
            allHiragana: /^[ぁ-ん　ー ]*$/,
        };

        /**
         * 文字列の半角文字数を数える（Unicode用）
         * @param text 解析対象の文字列
         */
        export function countHalf(text: string) {
            var count = 0;
            for (var i = 0; i < text.length; i++) {
                var c = text.charCodeAt(i);

                // 0x20 ～ 0x80: 半角記号と半角英数字
                // 0xff61 ～ 0xff9f: 半角カタカナ
                if ((0x20 <= c && c <= 0x7e) || (0xff61 <= c && c <= 0xff9f)) {
                    count += 1;
                } else {
                    count += 2;
                }
            }
            return count;
        }
        
        export function toOneByteAlphaNumberic(text: string){
            return text.replace(/[！-～　]/g, function(s) {
                if(s === "　" ){
                    return String.fromCharCode(s.charCodeAt(0) - 12256)
                }
                return String.fromCharCode(s.charCodeAt(0) - 0xFEE0);
            });        
        }
        
        export function toTwoByteAlphaNumberic(text: string){
            return text.replace(/[\!-\~ ]/g, function(s) {
                if(s === " " ){
                    return String.fromCharCode(s.charCodeAt(0) + 12256)
                }
                return String.fromCharCode(s.charCodeAt(0) + 0xFEE0);
            });        
        }
        
        export function katakanaToHiragana (text : string) {
            text = text.replace(/[ァ-ヴ]/g, function (s) {
                        return String.fromCharCode(s.charCodeAt(0) - 0x60);
                    }).replace(/ﾞ/g, '゛').replace(/ﾟ/g, '゜')
                    .replace(/(う゛)/g, 'ゔ').replace(/ヷ/g, 'わ゛')
                    .replace(/ヸ/g, 'ゐ゛').replace(/ヹ/g, 'ゑ゛')
                    .replace(/ヺ/g, 'を゛').replace(/(ヽ゛)/g, 'ゞ')
                    .replace(/ヽ/g, 'ゝ').replace(/ヾ/g, 'ゞ');
            return text;
        }
        
        export function hiraganaToKatakana (text : string, opt? : boolean) {
            text = text.replace(/[ぁ-ゔ]/g, function (s) {
                        return String.fromCharCode(s.charCodeAt(0) + 0x60);
                    }).replace(/ﾞ/g, '゛').replace(/ﾟ/g, '゜')
                    .replace(/(ウ゛)/g, 'ヴ').replace(/(ワ゛)/g, 'ヷ')
                    .replace(/(ヰ゛)/g, 'ヸ').replace(/(ヱ゛)/g, 'ヹ')
                    .replace(/(ヲ゛)/g, 'ヺ').replace(/(ゝ゛)/g, 'ヾ')
                    .replace(/ゝ/g, 'ヽ').replace(/ゞ/g, 'ヾ');
            if (opt !== false) {
                text = text.replace(/ゕ/g, 'ヵ').replace(/ゖ/g, 'ヶ');
            }
            return text;
        }
        
        /**
         * 半角カタカナを全角カタカナに変換
         * 
         * @param {String} str 変換したい文字列
         */
        export function oneByteKatakanaToTwoByte (text: string) {
            var katakanaMap = {
                'ｶﾞ': 'ガ', 'ｷﾞ': 'ギ', 'ｸﾞ': 'グ', 'ｹﾞ': 'ゲ', 'ｺﾞ': 'ゴ',
                'ｻﾞ': 'ザ', 'ｼﾞ': 'ジ', 'ｽﾞ': 'ズ', 'ｾﾞ': 'ゼ', 'ｿﾞ': 'ゾ',
                'ﾀﾞ': 'ダ', 'ﾁﾞ': 'ヂ', 'ﾂﾞ': 'ヅ', 'ﾃﾞ': 'デ', 'ﾄﾞ': 'ド',
                'ﾊﾞ': 'バ', 'ﾋﾞ': 'ビ', 'ﾌﾞ': 'ブ', 'ﾍﾞ': 'ベ', 'ﾎﾞ': 'ボ',
                'ﾊﾟ': 'パ', 'ﾋﾟ': 'ピ', 'ﾌﾟ': 'プ', 'ﾍﾟ': 'ペ', 'ﾎﾟ': 'ポ',
                'ｳﾞ': 'ヴ', 'ﾜﾞ': 'ヷ', 'ｦﾞ': 'ヺ',
                'ｱ': 'ア', 'ｲ': 'イ', 'ｳ': 'ウ', 'ｴ': 'エ', 'ｵ': 'オ',
                'ｶ': 'カ', 'ｷ': 'キ', 'ｸ': 'ク', 'ｹ': 'ケ', 'ｺ': 'コ',
                'ｻ': 'サ', 'ｼ': 'シ', 'ｽ': 'ス', 'ｾ': 'セ', 'ｿ': 'ソ',
                'ﾀ': 'タ', 'ﾁ': 'チ', 'ﾂ': 'ツ', 'ﾃ': 'テ', 'ﾄ': 'ト',
                'ﾅ': 'ナ', 'ﾆ': 'ニ', 'ﾇ': 'ヌ', 'ﾈ': 'ネ', 'ﾉ': 'ノ',
                'ﾊ': 'ハ', 'ﾋ': 'ヒ', 'ﾌ': 'フ', 'ﾍ': 'ヘ', 'ﾎ': 'ホ',
                'ﾏ': 'マ', 'ﾐ': 'ミ', 'ﾑ': 'ム', 'ﾒ': 'メ', 'ﾓ': 'モ',
                'ﾔ': 'ヤ', 'ﾕ': 'ユ', 'ﾖ': 'ヨ',
                'ﾗ': 'ラ', 'ﾘ': 'リ', 'ﾙ': 'ル', 'ﾚ': 'レ', 'ﾛ': 'ロ',
                'ﾜ': 'ワ', 'ｦ': 'ヲ', 'ﾝ': 'ン',
                'ｧ': 'ァ', 'ｨ': 'ィ', 'ｩ': 'ゥ', 'ｪ': 'ェ', 'ｫ': 'ォ',
                'ｯ': 'ッ', 'ｬ': 'ャ', 'ｭ': 'ュ', 'ｮ': 'ョ',
                '｡': '。', '､': '、', 'ｰ': 'ー', '｢': '「', '｣': '」', '･': '・'
            };
        
            var expression = new RegExp('(' + Object.keys(katakanaMap).join('|') + ')', 'g');
            return text.replace(expression, function (match) {
                        return katakanaMap[match];
                    }).replace(/ﾞ/g, '゛').replace(/ﾟ/g, '゜');
        }
        
        export function anyChar(text: string) {
            return {
                        probe: true,
                        messageId: 'FND_E_ANY'
                   }
        }

        /**
         * 文字列が半角数字のみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        export function allHalfNumeric(text: string) {
            return  { 
                        probe: regexp.allHalfNumeric.test(text),
                        messageId: 'FND_E_NUMERIC'
                    };
        }

        /**
         * 文字列が半角英字のみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        export function allHalfAlphabet(text: string) {
            return {
                        probe: regexp.allHalfAlphabet.test(text),
                        messageId: 'NO_MESSAGE'
                    };
        }
 
        /**
         * 文字列が半角英数字のみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        export function allHalfAlphanumeric(text: string) {
            return {
                        probe: regexp.allHalfAlphanumeric.test(text),
                        messageId: 'FND_E_ALPHANUMERIC'
                    };
        }

        /**
         * 文字列が半角カナのみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        export function allHalfKatakana(text: string) {
            return {
                        probe: regexp.allHalfKatakanaReg.test(text),
                        messageId: 'NO_MESSAGE' 
                    };
        }

        /**
         * 文字列が全角カナのみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        export function allFullKatakana(text: string) {
            return {
                        probe: regexp.allFullKatakanaReg.test(text),
                        messageId: 'FND_E_KANA'
                    };
        }

        /**
         * 文字列が半角文字のみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        export function allHalf(text: string) {
            return {
                        probe: text.length === countHalf(text),
                        messageId: 'FND_E_ANYHALFWIDTH'
                    };
        }

        /**
         * 文字列が平仮名のみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        export function allHiragana(text: string) {
            return { 
                        probe: regexp.allHiragana.test(text),
                        messageId: 'NO_MESSAGE'
                    };
        }

        /**
         * 文字列がカタカナのみで構成された1文字以上の文字列かどうか判断する
         * @param text 解析対象の文字列
         */
        export function allKatakana(text: string) {
            return {
                        probe: regexp.allFullKatakanaReg.test(text),
                        messageId: 'NO_MESSAGE'
                    };
        }
        
        /**
         * Determines if text is half integer
         * @param text text to check
         */
        export function halfInt(text: string) {
            var val = parseFloat(text);
            var probe = false;
            if (val !== NaN && (val * 2) % 1 === 0) probe = true;
            return {
                        probe: probe,
                        messageId: 'FND_E_HALFINT'
                    };
        }

        /**
         * 文字列中のHTML記号をサニタイズする
         * @param text 変換対象の文字列
         */
        export function htmlEncode(text: string) {
            var element = document.createElement('pre');
            if (typeof element.textContent !== 'undefined') {
                element.textContent = text;
            } else {
                element.innerText = text;
            }
            return element.innerHTML;
        }

        /**
         * 1文字目のみ小文字に変換する
         * @param text 変換対象の文字列
         */
        export function toLowerCaseFirst(text: string) {
            return text.charAt(0).toLowerCase() + text.slice(1);
        };

        /**
         * 1文字目のみ大文字に変換する
         * @param text 変換対象の文字列
         */
        export function toUpperCaseFirst(text: string) {
            return text.charAt(0).toUpperCase() + text.slice(1);
        }
        
        /**
         * Convert lower case text to upper case one
         * @param text text to convert
         */
        export function toUpperCase(text: string) {
            return text.replace(/[a-z]/g, function(c) {
                return String.fromCharCode(c.charCodeAt(0) - 0x20);
            });
        }

        /**
        * 指定された文字列が、null、undefined、Emptyか判定する
        * @param text 判定対象の文字列
        */
        export function isNullOrEmpty(text: any) {
            var result = true;
            if (text !== null && text !== undefined) {
                var convertValue = String(text);
                result = convertValue.length === 0;
            }
            return result;
        } 

        /**
        * 指定した文字列の各書式項目を、対応するオブジェクトの値と等価のテキストに置換する
        * @param text 書式文字列
        * @param args 置換の文字列（配列可）
        */
        export function format(format: string, ...args: any[]) {
            var replaceFunction: any = undefined;

            if (typeof args === 'object') {
                replaceFunction = function(m: any, k: any) { return args[k]; };
            }
            else {
                var workArgs = arguments;
                replaceFunction = function(m: any, k: any) { return workArgs[Number(k) + 1]; };
            }
            return format.replace(/\{(\w+)\}/g, replaceFunction);
        }

        /**
        * 変換文字列の先頭に、文字数分の指定文字列を追加する
        * @param text 変換対象の文字列
        * @param paddingChar 指定文字列
        * @param length 文字数
        */
        export function padLeft(text: string, paddingChar: string, length: number) {
            return charPadding(text, paddingChar, true, length);
        }

        /**
        * 変換文字列の末尾に、文字数分の指定文字列を追加する
        * @param text 変換対象の文字列
        * @param paddingChar 指定文字列
        * @param length 文字数
        */
        export function padRight(text: string, paddingChar: string, length: number) {
            return charPadding(text, paddingChar, false, length);
        }

        /**
        * 指定した文字列に、指定した文字列数分、指定文字列を追加する
        * @param text 変換対象の文字列
        * @param paddingChar 埋める文字列
        * @param isPadLeft 左埋めフラグ（false：右埋め）
        * @param length 文字数
        */
        export function charPadding(text: string, paddingChar: string, isPadLeft: boolean, length: number) {
            var result: string;

            if (countHalf(paddingChar) !== 1) {
                throw new Error('paddingChar "' + paddingChar + '" is not single character');
            }

            var lengthOfSource = countHalf(text);
            var shortage = length - lengthOfSource;
            if (shortage <= 0) {
                return text;
            }

            var pad = new Array(shortage + 1).join(paddingChar);

            if (isPadLeft) {
                return pad + text;
            } else {
                return text + pad;
            }
        }

        export function replaceAll(originalString: string, find: string, replace: string) {
            return originalString.split(find).join(replace);
        }

        export function removeFromStart(originalString: string, charSet: string) {
            if (originalString.length === charSet.length) {
                return (originalString === charSet) ? "" : originalString;
            }
            var i = findLastContinousIndex(originalString, charSet, 0);
            return originalString.substr(i, originalString.length - i);
        }

        function findLastContinousIndex(originalString: string, charSet: string, startIndex: number) {
            if (startIndex >= originalString.length - 1) {
                return startIndex;
            }
            if (originalString.substr(startIndex, charSet.length) !== charSet) {
                return startIndex;
            } else {
                return findLastContinousIndex(originalString, charSet, startIndex + charSet.length);
            }
        }

        /**
         * Type of characters
         */
        export class CharType {
            viewName: string;
            width: number;
            validator: (text: string) => any;

            constructor(
                viewName: string,
                width: number,
                validator: (text: string) => any) {

                this.viewName = viewName;
                this.width = width;
                this.validator = validator;
            }

            validate(text: string) {
                var result = new ui.validation.ValidationResult();
                let validateResult = this.validator(text);
                if (validateResult === true || validateResult.probe) {
                    result.isValid = true;
                    result.errorMessage = validateResult.messageId;
                } else {
                    result.fail(validateResult.messageId);
                }
                return result;
            }

            buildConstraintText(maxLength: number) {
                return this.viewName + this.getViewLength(maxLength) + '文字';
            }

            getViewLength(length) {
                return Math.floor(length / (this.width * 2));
            }
        }

        var charTypes: { [key: string]: CharType } = {
            AnyHalfWidth: new CharType(
                '半角',
                0.5,
                nts.uk.text.allHalf),
            AlphaNumeric: new CharType(
                '半角英数字',
                0.5,
                nts.uk.text.allHalfAlphanumeric),
            Alphabet: new CharType(
                '半角英字',
                0.5,
                nts.uk.text.allHalfAlphabet),
            Numeric: new CharType(
                '半角数字',
                0.5,
                nts.uk.text.allHalfNumeric),
            Any: new CharType(
                '全角',
                1,
                nts.uk.text.anyChar),
            Kana: new CharType(
                'カナ',
                1,
                nts.uk.text.allFullKatakana),
            HalfInt: new CharType(
                '半整数',
                0.5,
                nts.uk.text.halfInt)
        };

        export function getCharType(primitiveValueName: string): CharType {
            var constraint = __viewContext.primitiveValueConstraints[primitiveValueName];
            if (constraint === undefined)
                return null;
            if (constraint.charType === undefined)
                constraint.charType = "Any";
            var charType = charTypes[constraint.charType];
            if (charType === undefined) {
                throw new Error('invalid charTypeName: ' + constraint.charType);
            }

            return charType;
        }

        /**
         * Format for EmployeeCode
         * @return {String}  EmployeeCode
         */
        export function formatCode(code: string, filldirection: string, fillcharacter: string, length: number): string {
            if (filldirection === "left")
                return padLeft(code, fillcharacter, length);
            else
                return padRight(code, fillcharacter, length);
        }

        export function splitOrPadRight(originalString: string, length: number, char?: string) {
            if (originalString === undefined || length > originalString.length) {
                originalString = text.padRight(originalString ? originalString : "", char ? char : " ", length);
            } else {
                originalString = originalString.substr(0, length);
            }
            return originalString;
        }

        function addSeperation(amount: string) {
            var leng = amount.indexOf(".") > -1 ? amount.indexOf(".") : amount.length;
            if (leng < 4) return amount;
            var result = amount.substring(leng);
            var num = parseInt(amount.substring(0, leng));
            var times = Math.floor(leng / 3);
            for (var i = 0; i < times; i++) {
                var block = num % 1000;
                if (i > 0)
                    result = padLeft("" + block, "0", 3) + "," + result;
                else result = padLeft("" + block, "0", 3) + result;
                num = Math.floor(num / 1000);
            }
            result = num % 1000 + "," + result;
            return result;
        }

        export function formatCurrency(amount: number, locale: string) {
            var result = addSeperation("" + amount);
            if (locale == 'en' || locale == 'EN') return "￥" + result;
            return result + "円";
        }

        export function reverseDirection(direction: string): string {
            if (direction === "left")
                return "right";
            else if (direction === "right")
                return "left";
            else if (direction === "top")
                return "bottom";
            else if (direction === "bottom")
                return "top";
        }
        
        export function getISOFormat(format: string): string {
            format = util.orDefault(format, "ISO");
            if (format.toLowerCase() === "iso")
                return "YYYY-MM-DD[T]HH:mm:ss.SSS[Z]";
            if (format.toLowerCase() === "date")
                return "YYYY/MM/DD";
            if (format.toLowerCase() === "yearmonth")
                return "YYYY/MM";
            if (format.toLowerCase() === "time")
                return "HH:mm";
            if (format.toLowerCase() === "datetime")
                return "YYYY/MM/DD HH:mm";
            format = format.replace(/y/g, "Y");
            return format;
        }

        export class StringFormatter implements format.IFormatter {
            args: any;

            constructor(args: any) {
                this.args = args;
            }

            format(source: any): string {
                var constraintName = this.args.constraintName;
                var autofill: boolean = this.args.editorOption.autofill;
                if (!util.isNullOrEmpty(source)) {
                    if (autofill === true || constraintName === "EmployeeCode") {
                        var constraint = this.args.constraint;
                        var filldirection: string = this.args.editorOption.filldirection;
                        var fillcharacter: string = this.args.editorOption.fillcharacter;
                        var length: number = (constraint && constraint.maxLength) ? constraint.maxLength : 10;
                        return formatCode(source, filldirection, fillcharacter, length);
                    }
                }
                return source;
            }
        }

        export class NumberFormatter implements format.IFormatter {
            option: any;

            constructor(option: any) {
                this.option = option;
            }

            format(source: any): string {
                return nts.uk.util.isNullOrEmpty(source) ? (!nts.uk.util.isNullOrEmpty(this.option.option.defaultValue) 
                    ? this.option.option.defaultValue : source) 
                        : ntsNumber.formatNumber(source, this.option.option);
            }
        }

        export class TimeFormatter implements format.IFormatter {
            option: any;

            constructor(option: any) {
                this.option = option;
            }

            format(source: any): string {
                var result;
                if (this.option.inputFormat === "yearmonth") {
                    result = time.parseYearMonth(source);
                }
                else if (this.option.inputFormat === "time") {
                    result = time.parseTime(source, true);
                }
                else {
                    result = moment(source, "YYYYMMDD");
                    if (result.isValid()) {
                        var format = getISOFormat(this.option.inputFormat);
                        return result.format(format);
                    }
                    return source;
                }
                
                if (result.success) 
                    return result.format();
                
                return source;
            }
        }
    }
}