var resetPasswordApp = angular.module("resetPasswordApp", []);

resetPasswordApp.controller("resetPasswordAppController", function($timeout, $scope, $http,$rootScope, $window) {

	
	 $scope.login = function() {
		var password = $scope.password ; 
		var email = $scope.email ;
		var person = {password,email};
		 $http({
		        method : "post",
		        url : "http://localhost:8090/test/login",
		        data: person,
		    }).then(function mySuccess(response) {
		    	if (response.data.entity.obj==true){
		    		$window.location.href = 'http://localhost:8090/index.html';
		    	}
		    }, function myError(response) {
		        $scope.myWelcome = response.statusText;
		    });
	}

 
	 $scope.signup = function() {
			var password = $scope.password ; 
			var email = $scope.email ;
			var person = { email ,password }
			
			$http({
			     method : "post",
			     url : "http://localhost:8090/test/signup",
			     data: person,
		    }).then(function mySuccess(response) {
		    	if (response.data.entity.obj==true){
		    		$window.location.href = 'http://localhost:8090/index.html';
		    	}
		    }, function myError(response) {
		        $scope.myWelcome = response.statusText;
		    });
	}

		 
		 

	 
 
	 $scope.forgotMyPassword = function() {
		 var emailToRecover = $scope.emailToRecover ; 
		$http({
		  method : "post",
		  url : "http://localhost:8090/test/forgotMyPassword",
		  data: emailToRecover,
		}).then(function mySuccess(response) {
		  $scope.forgotMyPasswordError = response.data.entity.message
		}, function myError(response) {
		  $scope.myWelcome = response.statusText;
		});
 
	}
 
	 $scope.updateMyPassword = function() {
		 var token = window.location.search;
		 token = token.replace("?", ''); 
		 var newPassword = $scope.newPassword ; 
		$http({
		  method : "post",
		  url : "http://localhost:8090/test/updateMyPassword/"+ token,
		  data: newPassword,
		}).then(function mySuccess(response) {
		  $scope.forgotMyPasswordError = response.data.message ; 
		}, function myError(response) {
		  $scope.myWelcome = response.statusText;
		});
 
	}

});
