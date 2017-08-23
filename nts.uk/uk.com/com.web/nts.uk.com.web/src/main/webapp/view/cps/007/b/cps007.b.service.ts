module cps007.b.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;

    let paths: any = {
        'getCat': 'ctx/bs/person/info/category/find/companyby/{0}',
        'getItemDs': 'ctx/bs/person/info/ctgItem/findby/categoryId/{0}'
    };

    export function getCategory(cid) {
        return ajax(format(paths.getCat, cid));
    }

    export function getItemDefinitions(cid) {
        return ajax(format(paths.getItemDs, cid));
    }
}