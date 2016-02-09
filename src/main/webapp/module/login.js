app.controller('login', function($scope, $state, $mdDialog, $translate, loginService, GENDERS){
	$scope.username = "";
	$scope.password = "";
	$scope.nePostojiKorisnik = false;
	$scope.neispravnaLoznika = false;
	$scope.user  = {};
	$scope.repeat_password ="";
	$scope.genders = GENDERS;
	$scope.showRegisterForm = false;
	$scope.showLoginForm = true;
	$scope.passNotEqual = true;
    $scope.loginUser = function(){
    	alert("OLALAL!");
        loginService.login($scope.username, $scope.password, function(response){
        	alert("BRAVO!");
        }, function(response){
        	if(response.data === "BAD_PASSWORD"){
        		$scope.neispravnaLoznika = true;
        	}else if(response.data === "NO_USER"){
        		$scope.neispravnaLoznika = true;
        		$scope.nePostojiKorisnik = true;
        	}
        	
        });
   };
   
   $scope.registerForm = function(){
	   $scope.showRegisterForm = true;
	   $scope.showLoginForm = false;
   };
   $scope.cancelRegister = function (){
	   $scope.showRegisterForm = false;
	   $scope.showLoginForm = true;
	   $scope.user = {};
	   $scope.repeat_password = "";
   };
   
   $scope.registerUser = function(ev){
	   if($scope.user.password != $scope.repeat_password){
		   $mdDialog.show(
				      $mdDialog.alert()
				        .parent(angular.element(document.querySelector('#popupContainer')))
				        .clickOutsideToClose(true)
				        .title($translate.instant('PASSWORD_NOT_EQUAL'))
				        .textContent($translate.instant('PASSWORD_NOT_EQUAL_MESSAGE'))
				        .ariaLabel($translate.instant('PASSWORD_NOT_EQUAL'))
				        .ok($translate.instant('OK'))
				        .targetEvent(ev)
				    );
		   $scope.repeat_password = "";
		   return;
	   }
	   
	   loginService.registerUser($scope.user, function(){
		   alert("BRAVOOO");
	   },function(response){
		   alert(response.data)
	   })
   };
   

});