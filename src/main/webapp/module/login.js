app.controller('login', function($scope, $state, $mdDialog, $translate, loginService, GENDERS){
	$scope.username = "";
	$scope.password = "";
	$scope.nePostojiKorisnik = false;
	$scope.neispravnaLoznika = false;
	$scope.user  = {};
	$scope.activation = {};
	$scope.repeat_password ="";
	$scope.genders = GENDERS;
	$scope.showRegisterForm = false;
	$scope.showLoginForm = true;
	$scope.passNotEqual = true;
	$scope.activationForm = false;
    $scope.loginUser = function(ev){
        loginService.login ($scope.username, $scope.password, function(response){
        	$scope.user = response.data;
            $state.transitionTo('main.user');
        }, function(response){
        	if(response.data === "BAD_PASSWORD"){
        		$scope.neispravnaLoznika = true;
        	}else if(response.data === "NO_USER"){
        		$scope.neispravnaLoznika = true;
        		$scope.nePostojiKorisnik = true;
        	}else if(response.data === "USER_NOT_ACTIVATED"){
        		 $mdDialog.show(
   				      $mdDialog.alert()
   				        .parent(angular.element(document.querySelector('#popupContainer')))
   				        .clickOutsideToClose(true)
   				        .title($translate.instant('USER_NOT_ACTIVATED'))
   				        .textContent($translate.instant('USER_NOT_ACTIVATED_MESSAGE'))
   				        .ariaLabel($translate.instant('USER_NOT_ACTIVATED'))
   				        .ok($translate.instant('OK'))
   				        .targetEvent(ev)
   				    ).then(function(){
   				     $scope.openActivationForm();
   				    });
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
	   });
   };
   
   $scope.activateUser = function(ev){
	   loginService.activateUser($scope.activation.username, $scope.activation.token, function(response){
		   $mdDialog.show(
				      $mdDialog.alert()
				        .parent(angular.element(document.querySelector('#popupContainer')))
				        .clickOutsideToClose(true)
				        .title($translate.instant('USER_ACTIVATED'))
				        .textContent($translate.instant('USER_ACTIVATED_MESSAGE'))
				        .ariaLabel($translate.instant('USER_ACTIVATED'))
				        .ok($translate.instant('OK'))
				        .targetEvent(ev)
				    ).then(function(){
				 	   $scope.activationForm = false;
					   $scope.showLoginForm = true;
					   $scope.activation = {};
				    });
		   }, function(response){
		   if(response.data==="INVALID_TOKEN"){
			   $mdDialog.show(
				   $mdDialog.alert()
			        .parent(angular.element(document.querySelector('#popupContainer')))
			        .clickOutsideToClose(true)
			        .title($translate.instant('TOKEN_NOT_VALID'))
			        .textContent($translate.instant('TOKEN_NOT_VALID_MESSAGE'))
			        .ariaLabel($translate.instant('TOKEN_NOT_VALID'))
			        .ok($translate.instant('OK'))
			        .targetEvent(ev)
			    );
		   }else if(response.data==="NO_USERNAME"){
			   $mdDialog.show( 
				   $mdDialog.alert()
			        .parent(angular.element(document.querySelector('#popupContainer')))
			        .clickOutsideToClose(true)
			        .title($translate.instant('NO_USERNAME'))
			        .textContent($translate.instant('NO_USERNAME_MESSAGE'))
			        .ariaLabel($translate.instant('NO_USERNAME'))
			        .ok($translate.instant('OK'))
			        .targetEvent(ev)
			    );
		   }
	   });
   };
   
   $scope.openActivationForm = function(){
	   $scope.activationForm = true;
	   $scope.showRegisterForm = false;
	   $scope.showLoginForm = false;
   }
   $scope.cancelActivationForm = function(){
	   $scope.activationForm = false;
	   $scope.showLoginForm = true;
	   $scope.activation = {};
   }
   

});