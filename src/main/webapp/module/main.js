app.controller('main', function($scope, $state, $mdUtil, $mdSidenav, userService){

    $scope.init = function(){
        userService.getProfile(function(response){
            $scope.user = response.data;
        }, function(){
            $state.transitionTo('login')
        });
    };

    $scope.toggleNavigation = $mdUtil.debounce(function(){
        $mdSidenav('left').toggle()
    }, 200);

    $scope.logout = function(){
        userService.logout()
        gapi.auth.signOut();
        $state.transitionTo('login')
    };

});