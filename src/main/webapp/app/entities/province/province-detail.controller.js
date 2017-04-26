(function() {
    'use strict';

    angular
        .module('thepellestoreApp')
        .controller('ProvinceDetailController', ProvinceDetailController);

    ProvinceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Province', 'Country'];

    function ProvinceDetailController($scope, $rootScope, $stateParams, previousState, entity, Province, Country) {
        var vm = this;

        vm.province = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('thepellestoreApp:provinceUpdate', function(event, result) {
            vm.province = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
