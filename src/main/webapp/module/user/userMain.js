app.controller('userMain', function($scope, $state, $mdDialog, $translate, userService, loginService, GENDERS, POSETA_STATUS){
	
	$scope.init = function(){
		if($scope.user){
			userService.getUser($scope.user.id, function(response){
				if(response.data){
					$scope.user = response.data
					$scope.user.asfasdf ="afsdf";
					loadReservations();
					loadVisits();
				}
			});
		}else{
			loginService.getProfile(function(response){
				if(response.data){
					userService.getUser(response.data.id, function(response){
						if(response.data){
							$scope.user = response.data;
							loadReservations();
							loadVisits();
						}
					})
				}
			})
		}
		
		$scope.genders = GENDERS;
		$scope.posetaStatus = POSETA_STATUS;
		
	}
	
	function loadReservations(){
		userService.getUserReservations($scope.user.id, function(response){
			console.log("RESERVATIONS");
			console.log(response);
			if(response.data){
				$scope.user.rezervacije = response.data;
			}
			angular.forEach($scope.user.rezervacije, function(rezervacija){
				rezervacija.datetime = new Date(rezervacija.vreme);
			})
		})
	}
	function loadVisits(){
		userService.getUserVisits($scope.user.id, function(response){
			console.log("VISITS");
			console.log(response);
			if(response.data){
				$scope.user.posete = response.data;
			}
			angular.forEach($scope.user.posete, function(poseta){
				poseta.rezervacija.datetime = new Date(poseta.rezervacija.vreme);
			})
		})
	}
	$scope.save = function(){
		userService.updateUser($scope.user, function(response){
			console.log($scope.user);
		})
		
	}
	$scope.cancel = function(){
		loginService.getProfile(function(response){
			$scope.user = response.data;
		}, function(response){
			loginService.logout();
		})
	}
	
	$scope.deleteRezervacija = function(rezervacija, ev){
		var today = new Date();
		
		if(rezervacija.datetime<today){
			alert("Ne mozete izbrisati rezervacije koje su se odigrale u proslosti!");
		}else{
			alert("Uspeh");
		}
		
		ev.preventDefault();
	    ev.stopPropagation();
	}
});