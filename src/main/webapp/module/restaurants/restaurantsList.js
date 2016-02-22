app.controller('restaurantsList', function($scope, $state, $mdDialog, $translate, restaurantService){
	
	$scope.init = function(){
		restaurantService.getRestaurants(function(response){
			$scope.restaurants = response.data;
		})
	};
	$scope.openDetails = function(restaurant){
		 $mdDialog.show({
	          controller: 'restaurantDetails',
	          templateUrl: 'module/restaurants/restDetails.html',
	          restaurant: restaurant
	       });
	}
});