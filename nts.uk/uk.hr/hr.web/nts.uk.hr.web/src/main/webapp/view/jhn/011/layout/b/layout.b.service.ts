module layout.b.service {
    import ajax = nts.uk.request.ajax;
    import format = nts.uk.text.format;

    let paths: any = {
        'getCat': 'ctx/pereg/person/info/category/find/companyby/{0}',
        'getItemDs': 'ctx/pereg/person/info/ctgItem/layout/findby/categoryId/{0}'
    };

    export function getCategory(cid) {
        return ajax("com", format(paths.getCat, cid));
    }

    export function getItemDefinitions(cid) {
        return ajax("com",format(paths.getItemDs, cid));
    }
}