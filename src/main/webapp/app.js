var app = angular.module('app', ['ui.router', 'ngSanitize', 'ngMessages', 'ngMaterial', 'translation', 'flow']);

app.constant('GENDERS', ['MALE', 'FEMALE']);

app.config(function($stateProvider, $urlRouterProvider, $translateProvider, $httpProvider, $mdThemingProvider, $mdDateLocaleProvider){

    $urlRouterProvider.otherwise('/login');
    $stateProvider
        .state('login', {
            url: '/login',
            templateUrl: 'module/login.html',
            controller: 'login'
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