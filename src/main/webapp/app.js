var app = angular.module('app', ['ui.router', 'ngSanitize', 'ngMessages', 'ngMaterial', 'translation', 'flow', 'scDateTime']);

app.constant('GENDERS', ['MALE', 'FEMALE']);
app.constant('POSETA_STATUS', ['PRIHVACENO', 'ODBIJENO', 'NEODLUCENO'])

app.config(function($stateProvider, $urlRouterProvider, $translateProvider, $httpProvider, $mdThemingProvider, $mdDateLocaleProvider){

    $urlRouterProvider.otherwise('/login');
    $stateProvider
        .state('login', {
            url: '/login',
            templateUrl: 'module/login.html',
            controller: 'login'
        }).state('main', {
            url: '/',
            abstract: true,
            templateUrl: 'module/main.html',
            controller: 'main'
        })

        .state('main.restaurants', {
            url: 'restaurants',
            templateUrl: 'module/restaurants/list.html',
            controller: 'restaurantsList'
        })
        .state('main.user', {
            url: 'user',
            templateUrl: 'module/user/userMain.html',
            controller: 'userMain'
        })
        .state('main.friends', {
            url: 'friends',
            templateUrl: 'module/friends/friendsMain.html',
            controller: 'friendsMain'
        })
        .state('main.createManager', {
            url: 'createManager',
            templateUrl: 'module/manager/managerMain.html',
            controller: 'managerMain'
        });

    //Ne radi UTF-8 sa ovim
    //$translateProvider.useSanitizeValueStrategy('sanitize');

    $mdThemingProvider.theme('default')
        .primaryPalette('light-green')
        .accentPalette('indigo');


    $mdDateLocaleProvider.formatDate = function(date) {
        return moment(date).format('DD-MM-YYYY');
    };


});