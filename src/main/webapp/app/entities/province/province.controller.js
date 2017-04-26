(function() {
    'use strict';

    angular
        .module('thepellestoreApp')
        .controller('ProvinceController', ProvinceController);

    ProvinceController.$inject = ['Province', 'ProvinceSearch'];

    function ProvinceController(Province, ProvinceSearch) {

        var vm = this;

        vm.provinces = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Province.query(function(result) {
                vm.provinces = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ProvinceSearch.query({query: vm.searchQuery}, function(result) {
                vm.provinces = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
