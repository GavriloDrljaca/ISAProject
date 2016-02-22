app.controller('main', function($scope, $state, $mdUtil, $mdSidenav, userService, loginService){

    $scope.init = function(){
        loginService.getProfile(function(response){
            $scope.user = response.data;
        }, function(){
            $state.transitionTo('login')
        });
    };

    $scope.toggleNavigation = $mdUtil.debounce(function(){
        $mdSidenav('left').toggle()
    }, 200);

    $scope.logout = function(){
        loginService.logout()
        $state.transitionTo('login')
    };

});