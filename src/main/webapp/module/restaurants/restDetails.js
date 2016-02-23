app.controller('restaurantDetails', function($scope, $mdDialog, restaurantService, restaurant, loginService){
	
	$scope.init = function(){
		$scope.restaurant = restaurant;
		loginService.getProfile(function(response){
			$scope.user = response.data;
			if($scope.user.role === 'MENADZER_RESTORANA'){
				if($scope.user.restoran.id == $scope.restaurant.id){
					$scope.isManager = true;
				}else{
					$scope.isManager = false;
				}
			}else{
				$scope.isManager = false;
			}
		}, function(response){
			loginService.logout();
		});
	}
	
	$scope.cancel = function(){
		$mdDialog.hide();
	}
	$scope.save = function(){
		if($scope.restaurant.id){
			console.log("SAVING");
			console.log($scope.restaurant)
			if($scope.restaurant.id){
				restaurantService.updateRestaurant($scope.restaurant, function(response){
					$mdDialog.hide();
				})
			}
		}else{
			restaurantService.createRestaurant($scope.restaurant, function(response){
				$mdDialog.hide();
			});
		}
	}
});