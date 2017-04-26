(function() {
    'use strict';

    angular
        .module('thepellestoreApp')
        .controller('FooterController', FooterController);

    FooterController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService'];

    function FooterController ($state, Auth, Principal, ProfileService, LoginService) 
    {
    	var vm = this;
    }
})();
