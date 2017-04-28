module nts.uk.com.view.ccg015.a {

    export module service {

        // Service paths.
        var servicePath = {
            findAllTopPageItem: "/toppage/findAll",
            getTopPageItemDetail: "/toppage/topPageDetail",
            registerTopPage: "/toppage/create",
            updateTopPage: "/toppage/update",
            removeTopPage: "/toppage/remove"
        }
        export function loadTopPage(): JQueryPromise<Array<model.TopPageItemDto>> {
            var self = this;
            var dfd = $.Deferred<Array<model.TopPageItemDto>>();
            var path = servicePath.findAllTopPageItem;
            nts.uk.request.ajax(path).done(function(data: Array<model.TopPageItemDto>) {
                dfd.resolve(data);
            });
            return dfd.promise();
        }

        export function loadDetailTopPage(topPageCode: string): JQueryPromise<model.TopPageDto> {
            var self = this;
            var dfd = $.Deferred<model.TopPageDto>();
            var path = servicePath.getTopPageItemDetail + '/' + topPageCode;
            nts.uk.request.ajax(path).done(function(data: model.TopPageDto) {
                dfd.resolve(data);
            });
            return dfd.promise();
        }

        /**
          * Function is used to save new Top Page.
          */
        export function registerTopPage(data: model.TopPageDto): JQueryPromise<model.TopPageDto> {
            return nts.uk.request.ajax(servicePath.registerTopPage, data);
        }

        /**
        * Function is used to update Top Page.
        */
        export function updateTopPage(data: model.TopPageDto): JQueryPromise<model.TopPageDto> {
            return nts.uk.request.ajax(servicePath.updateTopPage, data);
        }

        /**
        * Function is used to delete Top Page.
        */
        export function deleteTopPage(topPageItemCode: string): JQueryPromise<model.TopPageDto> {
            return nts.uk.request.ajax(servicePath.removeTopPage, topPageItemCode);
        }

        export module model {
            export interface TopPageItemDto {
                topPageCode: string;
                topPageName: string;
            }
            export interface TopPageDto {
                topPageCode: string;
                topPageName: string;
                languageNumber: number;
                placementDto: Array<PlacementDto>;
            }
            export interface PlacementDto {
                row: number;
                column: number;
                topPagePartDto: TopPagePartDto;
            }
            export interface TopPagePartDto {
                topPagePartType: number;
                topPagePartCode: string;
                topPagePartName: string;
                width: number;
                height: number;
            }
        }
    }
}