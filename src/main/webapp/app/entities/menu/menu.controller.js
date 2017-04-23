(function() {
    'use strict';

    angular
        .module('thepellestoreApp')
        .controller('MenuController', MenuController);

    MenuController.$inject = ['Menu', 'MenuSearch'];

    function MenuController(Menu, MenuSearch) {

        var vm = this;

        vm.menus = [];
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Menu.query(function(result) {
                vm.menus = result;
                vm.searchQuery = null;
            });
        }

        function search() {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MenuSearch.query({query: vm.searchQuery}, function(result) {
                vm.menus = result;
                vm.currentSearch = vm.searchQuery;
            });
        }

        function clear() {
            vm.searchQuery = null;
            loadAll();
        }    }
})();
