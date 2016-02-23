app.controller('restaurantInfo', function ($scope, restaurantService, loginService){
	
	$scope.init = function(){
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
		})
		
		initMap();
	};
	
	function initMap() {
		var myLatLng = {lat: $scope.restaurant.latituda, lng: $scope.restaurant.longituda};

		  var map = new google.maps.Map(document.getElementById('map'), {
		    zoom: 15,
		    center: myLatLng
		  });

		  var marker = new google.maps.Marker({
		    position: myLatLng,
		    map: map,
		    title: 'Hello World!'
		  });
	  }
});

