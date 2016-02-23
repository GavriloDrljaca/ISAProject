app.controller('restaurantsList', function($scope, $state, $mdDialog, $translate, restaurantService){
	
	$scope.init = function(){
		restaurantService.getRestaurants(function(response){
			$scope.restaurants = response.data;
			
			if(navigator.geolocation){
				navigator.geolocation.getCurrentPosition(setPosition);
			}
			
			$scope.sort = "-ocenaUkupno"
			angular.forEach($scope.restaurants, function(restaurant){
				console.log(restaurant);
				restaurantService.getOcenaRestorana(restaurant.id, $scope.user.id, function(response){
					console.log("ojsaa");
					restaurant.ocenaUkupno = response.data[0];
					restaurant.ocenaUkupnoPrijatelji = response.data[1];
				}, function(response){
					alert("neki fakup");
				});
				
				
			});
		})
		

	};
	function setPosition(position){
		$scope.position = {};
		$scope.position.latitude = position.coords.latitude;
		$scope.position.longitude = position.coords.longitude;
	}
	$scope.openDetails = function(restaurant){
		 $mdDialog.show({
	          controller: 'restaurantDetails',
	          templateUrl: 'module/restaurants/restDetails.html',
	          clickOutsideToClose: true,
	          restaurant: restaurant
	       });
	}
	
	$scope.addNewRestaurant = function(){
		var restaurant = {};
		$mdDialog.show({
	          controller: 'restaurantDetails',
	          templateUrl: 'module/restaurants/restDetails.html',
	          clickOutsideToClose: true,
	          restaurant: restaurant
	       }).then(function(){
	    	   $scope.init();
	       });
	}
	
	$scope.sortOcenaAsc = function(){
		$scope.sort = "ocenaUkupno";
	}
	$scope.sortOcenaDsc = function(){
		$scope.sort = "-ocenaUkupno";
	}
	$scope.sortPrijateljiOcenaAsc = function(){
		$scope.sort = "ocenaUkupnoPrijatelji";
	}
	$scope.sortPrijateljiOcenaDsc = function(){
		$scope.sort = "-ocenaUkupnoPrijatelji";
	}
	$scope.sortDistance = function(){
		var i =0;
		var lat = 0;
		var long = 0;
		for(i; i<$scope.restaurants.length; i++){
			$scope.restaurants[i].distance = 100.0000;
			lat = $scope.restaurants[i].latituda;
			long = $scope.restaurants[i].longituda;
			lat2 = $scope.position.latitude.toFixed(6);
			long2 = $scope.position.longitude.toFixed(6);
			$scope.restaurants[i].distance = getDistanceFromLatLonInKm(lat, long, lat2, long2);
		}
		console.log($scope.position);
		$scope.sort = "distance";
	}
	
	function getDistanceFromLatLonInKm(lat1,lon1,lat2,lon2) {
		  var R = 6371; // Radius of the earth in km
		  var dLat = deg2rad(lat2-lat1);  // deg2rad below
		  var dLon = deg2rad(lon2-lon1); 
		  var a = 
		    Math.sin(dLat/2) * Math.sin(dLat/2) +
		    Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * 
		    Math.sin(dLon/2) * Math.sin(dLon/2); 
		  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		  var d = R * c; // Distance in km
		  return d;
		}

	function deg2rad(deg) {
		return deg * (Math.PI/180)
	}
	

	
});