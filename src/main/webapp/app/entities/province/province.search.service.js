(function() {
    'use strict';

    angular
        .module('thepellestoreApp')
        .factory('ProvinceSearch', ProvinceSearch);

    ProvinceSearch.$inject = ['$resource'];

    function ProvinceSearch($resource) {
        var resourceUrl =  'api/_search/provinces/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
