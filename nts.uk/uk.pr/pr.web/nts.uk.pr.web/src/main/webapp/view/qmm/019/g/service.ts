module qmm019.g.service {
    var paths = {
        getLayoutHeadInfor: "pr/proto/layout/findlayoutwithmaxstartym",
        getAllLayoutHist: "pr/proto/layout/findalllayoutHist",
        copylayoutPath: "pr/proto/layout/createlayout"
    }

    /**
     * Get list layout master new history
     */
    export function getLayoutHeadInfor(): JQueryPromise<Array<model.LayoutHeadDto>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.getLayoutHeadInfor)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }
    export function getAllLayoutHist(): JQueryPromise<Array<model.LayoutHistory>> {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.getAllLayoutHist)
            .done(function(res: Array<any>) {
                dfd.resolve(res);
            })
            .fail(function(res) {
                dfd.reject(res);
            })
        return dfd.promise();
    }

    export function createLayout(layoutMaster: model.LayoutMasterDto) {
        var dfd = $.Deferred<Array<any>>();
        nts.uk.request.ajax(paths.copylayoutPath, layoutMaster).done(function(res: Array<any>) {
            dfd.resolve(res);
        }).fail(function(res) {
            dfd.reject(res);
        })
        return dfd.promise();
    }
    /**
           * Model namespace.
        */
    export module model {
        export class LayoutHeadDto {
            checkCopy: Boolean;
            /** StmtCode của Layout được copy */
            stmtCodeCopied: string;
            /** startYm của Layout được copy */
            stmtCode: string;
            stmtName: string;
        }
        // layout
        export class LayoutMasterDto {
            checkCopy: Boolean;
            /** StmtCode của Layout được copy */
            stmtCodeCopied: string;
            /** startYm của Layout được copy */
            startYmCopied: number;
            stmtCode: string;
            startYm: number;
            layoutAtr: number;
            stmtName: string;
            endYm: number;
        }
        export class LayoutHistory {
            companyCode: string;
            stmtCode: string;
            startYm: number;
            stmtName: string;
            endYm: number;
            layoutAtr: number;
            historyId: string;
            constructor() {
            }
            }
        // layoutHistory
        export class LayoutHistoryDto {
            checkCopy: Boolean;
            /** StmtCode của Layout được copy */
            stmtCodeCopied: string;
            /** startYm của Layout được copy */
            startYmCopied: number;
            stmtCode: string;
            startYm: number;
            layoutAtr: number;
            endYm: number;
        }

    }
}