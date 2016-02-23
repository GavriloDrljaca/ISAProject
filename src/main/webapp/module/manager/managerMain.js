app.controller('managerMain', function($scope, $mdDialog, userService, restaurantService, GENDERS){
	
	$scope.init = function(){
		
		$scope.successCreated = false;
		$scope.newManager = {};
		$scope.genders = GENDERS;
		restaurantService.getRestaurants(function(response){
			$scope.chooseRestaurants = response.data;
			
		});
	}
	
	$scope.save = function(){
		
		var i=0;
		for(i; i<$scope.chooseRestaurants.length; i++){
			if($scope.chooseRestaurants[i].id == $scope.newManager.restoran){
				$scope.newManager.restoran =$scope.chooseRestaurants[i];
			}
		}
		
		userService.createManager($scope.newManager, function(response){
			if(response.data === "MANAGER_CREATED"){
				$scope.successCreated = true;
			}
		}, function(response){
			if(response.data === "NO_RESTORAN"){
				alert("no restoran");
			}else if(response.data === "USER_EXISTS"){
				$scope.userExists = true;
			}
			console.log(response);
		})
	}
	
	$scope.createNewManger = function(){
		$scope.successCreated = false;
		$scope.newManager = {};
	}
})