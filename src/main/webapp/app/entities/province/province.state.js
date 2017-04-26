(function() {
    'use strict';

    angular
        .module('thepellestoreApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('province', {
            parent: 'entity',
            url: '/province',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'thepellestoreApp.province.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/province/provinces.html',
                    controller: 'ProvinceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('province');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('province-detail', {
            parent: 'province',
            url: '/province/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'thepellestoreApp.province.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/province/province-detail.html',
                    controller: 'ProvinceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('province');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Province', function($stateParams, Province) {
                    return Province.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'province',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('province-detail.edit', {
            parent: 'province-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/province/province-dialog.html',
                    controller: 'ProvinceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Province', function(Province) {
                            return Province.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('province.new', {
            parent: 'province',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/province/province-dialog.html',
                    controller: 'ProvinceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                province_name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('province', null, { reload: 'province' });
                }, function() {
                    $state.go('province');
                });
            }]
        })
        .state('province.edit', {
            parent: 'province',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/province/province-dialog.html',
                    controller: 'ProvinceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Province', function(Province) {
                            return Province.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('province', null, { reload: 'province' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('province.delete', {
            parent: 'province',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/province/province-delete-dialog.html',
                    controller: 'ProvinceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Province', function(Province) {
                            return Province.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('province', null, { reload: 'province' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
