app.controller('friendsMain', function($scope, $state, $mdDialog, $translate, userService, loginService){
	
	$scope.init = function(){
		
		if($scope.user){
			userService.getNonFriends($scope.user.id, function(response){
				if(response.data){
					$scope.nonFriends = response.data;
				}else{
					$scope.nonFriends = [];
				}
			})
			
			userService.getFriends($scope.user.id, function(response){
								if(response.data){
									$scope.user.friends = response.data
								}
							})
		}else{
			loginService.getProfile(function(response){
				if(response.data){
					userService.getUser(response.data.id, function(response){
						if(response.data){
							$scope.user = response.data;
							//GET NON FRIENDS
							userService.getNonFriends($scope.user.id, function(response){
								if(response.data){
									$scope.nonFriends = response.data;
								}else{
									$scope.nonFriends = [];
								}
							})
							//GET FRIENDS
							userService.getFriends($scope.user.id, function(response){
								if(response.data){
									$scope.user.friends = response.data
								}
							})
						}
					})
				}
			})
		}
		
		$scope.newFriendsList = false;

	}
	
	$scope.openAddNewFriend = function(){
		$scope.newFriendsList = true;
		
	};
	$scope.addNewFriend = function(friend, ev){
		userService.addFriend($scope.user.id, friend.id, function(response){
			$scope.user.friends.push(friend);
			var index = $scope.nonFriends.indexOf(friend)
			if (index >-1){
				$scope.nonFriends.splice(index,1);
			}
		})
		
		ev.preventDefault();
	    ev.stopPropagation();
	}
	$scope.deleteFriend = function(friend, ev){
		userService.deleteFriend($scope.user.id, friend.id, function(response){
			var index = $scope.user.friends.indexOf(friend)
			if (index >-1){
				$scope.user.friends.splice(index,1);
			}
			$scope.nonFriends.push(friend);
		})
		ev.preventDefault();
	    ev.stopPropagation();
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
	};
});
