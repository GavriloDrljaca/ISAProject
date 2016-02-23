app.controller('friendDetails', function($scope,$state, $mdDialog, userService, userfriend, loginService, GENDERS){
	
	$scope.init = function(){
		$scope.user = userfriend;
		$scope.genders = GENDERS;
		userService.getFriends($scope.user.id, function(response){
			if(response.data){
				$scope.user.friends = response.data;
			}
		});	
		//GET LOGGED USER and active reservations
		loginService.getProfile(function(response){
			$scope.loggedUser = response.data;
			userService.getUserReservations($scope.loggedUser.id, function(response){
				if(response.data){
					var allRezervacije = response.data;
					var today = new Date();
					$scope.rezervacije = [];
					angular.forEach(allRezervacije, function(rezervacija){
						rezervacija.datetime = new Date(rezervacija.vreme);
						if(rezervacija.datetime>today){
							$scope.rezervacije.push(rezervacija);
						}
					})
				}
			})
			
			userService.isFriend($scope.loggedUser.id, $scope.user.id, function(response){
				if(response.data === "FRIEND"){
					$scope.isFriend = true;
				}else{
					$scope.isFriend = false;
				}
			})
		}, function(response){
			loginService.logout();
		})
		
		$scope.uploader = {};
		$scope.successSend = false;
		$scope.currRezervacija = [];
	};
	
	$scope.openDetails = function(friend){
		$mdDialog.show({
			  locals: {
				  userfriend: friend,
			  },
	          controller: 'friendDetails',
	          templateUrl: 'module/friends/friendDetails.html',
	          clickOutsideToClose: true
	       });
	}
	$scope.cancel = function(ev){
		$mdDialog.hide();
	}
	
	$scope.addRezervacija = function(rezervacija, ev){
		$scope.currRezervacija.push(rezervacija);
		ev.preventDefault();
	    ev.stopPropagation();
	}
	
	$scope.removeCurrRezervacija = function(rezervacija, ev){
		
		$scope.currRezervacija = [];
		ev.preventDefault();
	    ev.stopPropagation();
	}
	
	$scope.sendVisitation = function(){
		console.log($scope.currRezervacija[0]);
		console.log($scope.user);
		userService.createPoseta($scope.currRezervacija[0], $scope.user, function(response){
			$scope.successSend = true;
		})
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
