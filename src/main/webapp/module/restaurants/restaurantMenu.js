app.controller('restaurantMenu', function($scope, $mdDialog, restaurantService, loginService){

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
		$scope.newJelo = {};
		$scope.addJeloForm = false;
	}

	$scope.openAddJelo = function(ev){
		$scope.addJeloForm = true;
	};
	$scope.cancelAddJelo = function(){
		$scope.newJelo = {};
		$scope.addJeloForm = false;
	}
	$scope.addJelo = function(ev){
		if(!$scope.restaurant.jelovnik){
			$scope.restaurant.jelovnik = [];
		}
		if(!$scope.newJelo.id){
			$scope.restaurant.jelovnik.push({
				naziv: $scope.newJelo.naziv,
				opis: $scope.newJelo.opis,
				cena: $scope.newJelo.cena
			})
		}
		$scope.cancelAddJelo();
	};
	$scope.deleteJelo = function(jelo, ev){
		confirmAction(jelo);
		ev.preventDefault();
	    ev.stopPropagation();
	    
	}
	$scope.editJelo = function(jelo, ev){
		$scope.newJelo = jelo;
		$scope.addJeloForm = true;
	}
	
	function confirmAction(jelo){
		restaurantService.deleteJelo(jelo.id, function(response){
			var index = $scope.restaurant.jelovnik.indexOf(jelo)
			if (index >-1){
				$scope.restaurant.jelovnik.splice(index,1);
			}
		}, function(response){
			
		})
	}
	
});
