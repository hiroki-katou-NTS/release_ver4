/// <reference path="../reference.ts"/>

module nts.uk.ui {
    
    export module toBeResource {
        export let yes = "はい";
        export let no = "いいえ";
        export let cancel = "キャンセル";
        export let close = "閉じる";
        export let info = "情報";
        export let warn = "警告";
        export let error = "エラー";
        export let confirm = "確認";
        export let unset = "未設定";
        export let errorContent = "エラー内容";
        export let errorCode = "エラーコード";
        export let errorList = "エラー一覧";
        export let plzWait = "お待ちください";
    }


    export function localize(textId: string): string {
        return textId;
    }


    export var confirmSave: (dirtyChecker: DirtyChecker) => any;
    confirmSave = function(dirtyChecker: DirtyChecker) {
        var frame = windows.getSelf();
        if (frame.$dialog === undefined || frame.$dialog === null) {
            confirmSaveWindow(dirtyChecker);
        } else {
            confirmSaveDialog(dirtyChecker, frame.$dialog);
        }
    }
    function confirmSaveWindow(dirtyChecker: DirtyChecker) {

        var beforeunloadHandler = function(e) {
            if (dirtyChecker.isDirty()) {
                return "ban co muon save hok?";
                //return nts.ui.message('Com_0000105');
            }
        };

        confirmSaveEnable(beforeunloadHandler);
    }

    function confirmSaveDialog(dirtyChecker: DirtyChecker, dialog: JQuery) {
        //dialog* any;
        var beforeunloadHandler = function(e) {
            if (dirtyChecker.isDirty()) {
                e.preventDefault();
                nts.uk.ui.dialog.confirm("Are you sure you want to leave the page?")
                    .ifYes(function() {
                        dirtyChecker.reset();
                        dialog.dialog("close");
                    }).ifNo(function() {
                    })
                //return nts.ui.message('Com_0000105');
            }
        };

        confirmSaveEnableDialog(beforeunloadHandler, dialog);
    }

    export function confirmSaveEnableDialog(beforeunloadHandler, dialog) {
        dialog.on("dialogbeforeclose", beforeunloadHandler);
    };

    export function confirmSaveDisableDialog(dialog) {
        dialog.on("dialogbeforeclose", function() { });
    };

    export function confirmSaveEnable(beforeunloadHandler) {
        $(window).bind('beforeunload', beforeunloadHandler);
    };

    export function confirmSaveDisable() {
        $(window).unbind('beforeunload');
    };

    /**
     * Block UI Module
     * Using for blocking UI when action in progress
     */
    export module block {

        export function invisible() {
            let rect = calcRect();

            (<any>$).blockUI({
                message: null,
                overlayCSS: { opacity: 0 },
                css: {
                    width: rect.width,
                    left: rect.left
                }
            });
        }

        export function grayout() {
            let rect = calcRect();

            (<any>$).blockUI({
                message: '<div class="block-ui-message">' + toBeResource.plzWait + '</div>',
                fadeIn: 200,
                css: {
                    width: rect.width,
                    left: rect.left
                }
            });
        }

        export function clear() {
            (<any>$).unblockUI({
                fadeOut: 200
            });
        }

        function calcRect() {
            let width = 220;
            let left = ($(window).width() - width) / 2;
            return {
                width: width,
                left: left
            };
        }
    }

    export class DirtyChecker {

        targetViewModel: KnockoutObservable<any>;
        initialState: string;

        constructor(targetViewModelObservable: KnockoutObservable<any>) {
            this.targetViewModel = targetViewModelObservable;
            this.initialState = this.getCurrentState();
        }

        getCurrentState() {
            return ko.toJSON(this.targetViewModel());
        }

        reset() {
            this.initialState = this.getCurrentState();
        }

        isDirty() {
            return this.initialState !== this.getCurrentState();
        }
    }


    module smallExtensions {

        $(() => {
            $(document).on('mouseenter', '.limited-label', e => {
                let $label = $(e.target);

                // Check if contents is overflow
                if ($label.outerWidth() < $label[0].scrollWidth) {
                    let $view = $('<div />').addClass('limited-label-view')
                        .text($label.text())
                        .appendTo('body')
                        .position({
                            my: 'left top',
                            at: 'left bottom',
                            of: $label,
                            collision: 'flip'
                        });

                    $label.bind('mouseleave.limitedlabel', () => {
                        $label.unbind('mouseleave.limitedlabel');
                        $view.remove();
                    });
                }
            });
        });
    }
    
    export module keyboardStream {
        
        let _lastKey: { code: number, time: Date } = {
            code: undefined,
            time: undefined
        };
        
        export function lastKey(): { code: number, time: Date } {
            return {
                code: _lastKey.code,
                time: _lastKey.time
            };
        }
        
        export function wasKeyDown(keyCode: number, millisToExpire: number): boolean {
            
            return _lastKey.code === keyCode
                && (+new Date() - +_lastKey.time <= millisToExpire);
        }
        
        $(() => {
            
            $(window).on("keydown", e => {
                _lastKey.code = e.keyCode;
                _lastKey.time = new Date();
            });
            
        });
    }
}