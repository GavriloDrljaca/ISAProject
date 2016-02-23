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
		$scope.uploader = {};
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
			var date = new Date();
			$scope.pastVisits = [];
			$scope.futureVisits = [];
			angular.forEach($scope.user.posete, function(poseta){
				poseta.rezervacija.datetime = new Date(poseta.rezervacija.vreme);
			
				if(poseta.rezervacija.datetime<date){
					if(poseta.status.toLowerCase() == "prihvaceno"){
						$scope.pastVisits.push(poseta);
					}
					
				}else{
					if(poseta.rezervacija.user.id != $scope.user.id){
						$scope.futureVisits.push(poseta);
					}
					
				}
			})
		})
	}
	$scope.save = function(){
		$scope.uploader.flow.opts.target = "/users/" + $scope.user.id + "/image";
        $scope.uploader.flow.upload();
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
	$scope.openDetailsP = function(poseta, ev){
		$mdDialog.show({
	          controller: 'posetaDetails',
	          templateUrl: 'module/user/posetaDetails.html',
	          poseta: poseta,
	          clickOutsideToClose: true
	       });
		
	}
	
	$scope.openDetailsRez = function(rezervacija){
		$mdDialog.show({
	          controller: 'rezervacijaDetails',
	          templateUrl: 'module/user/rezervacijaDetails.html',
	          rezervacija: rezervacija,
	          clickOutsideToClose: true
	       }).then(function(){
	    	   loadReservations();
	       });
	}
	
	$scope.getImageUrl = function(){
        if ($scope.user.photoUrl){
            return $scope.user.photoUrl
        } else if ($scope.user.gender === 'MALE'){
            return '/images/male-placeholder.jpg'
        } else {
            return '/images/female-placeholder.jpg'
        }
    };
});