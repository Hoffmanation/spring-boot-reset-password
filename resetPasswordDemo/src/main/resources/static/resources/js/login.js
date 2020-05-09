//Main login angularJs module
var loginApp = angular.module("loginApp", [ 'ngCookies' ])


//Main login angularJs module-controller
loginApp.controller("loginAppController",function($scope, $http, $rootScope, $window,$location) {

		/**
		 * Global variables
		 */
		$scope.restUrl = $location.protocol() + '://'+ $location.host() +':'+  $location.port()+"/reset-pass-demo" ;
		$scope.url = $location.protocol() + '://'+ $location.host() +':'+  $location.port() ;
		$scope.dupPass="";
		$scope.emptyField="";
		$scope.nameNotEnChars="";
		$scope.passDontMatch="";
		$scope.passNotEnChars="";
		$scope.resetPasswrodError="";
		$scope.resetPasswrodResponse="";
		$scope.loggedUser ;
		$scope.loginMessage ;
		$scope.username = sessionStorage.username ;
					
					
		/**
		 * Functions 
		 */	
	    $scope.cleanErrors = function() {
	    	$scope.email = "" ;
	    	$scope.forgotMyPasswordError="";
			$scope.resetPasswrodError="";
			$scope.resetPasswrodResponse="";
			
			$scope.dupPass="";
			$scope.emptyField="";
			$scope.nameNotEnChars="";
			$scope.passDontMatch="";
			$scope.passNotEnChars="";
			$scope.resetPasswrodError="";
			$scope.resetPasswrodResponse="";
		}
	    
	    $scope.loaderTrue = function(){
	    	jQuery('#cover-spin').show();
	    }
	    $scope.loaderFalse = function(){
	    	jQuery('#cover-spin').hide();
	    }
		
	    //Login 
		$scope.login = function() {
			 $scope.cleanErrors();
			$scope.loaderTrue(); 
			$scope.userDetails = {
					email : $scope.loginEmail,
					password : $scope.loginPassword,
				}
			
			$http({
				method : 'POST',
				data: $scope.userDetails,
				url : $scope.restUrl + "/login",
				headers : {
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).success(function(response, data, status, headers,config) {
						if (response.status ==200) {
							$scope.loggedUser = response.entity ;
							$scope.username = $scope.loggedUser.email ;
							sessionStorage.username = $scope.username ;
							$window.location.href = '/index.html';
						}
						else if (response.status ==401 || response.status ==400){
							$scope.loginMessage = response.entity.message ;							
						}
						$scope.loaderFalse();
			}).error(function(response, data, status, headers,config) {
						if(response.status == 403){
							$window.location.href = '/login.html';
						}
						$scope.loginMessage = response.entity.message ;
						$scope.loaderFalse();
					});
		 };
									

		 //Registration
		$scope.register = function() {
			$scope.cleanErrors();
			$scope.loaderTrue(); 
			if ($scope.passwordConfirm!=$scope.signupPassword) {
				$scope.loginMessage == "Their password don't match" .
				return ;
			}
			
			$scope.userDetails = {
				email : $scope.signupEmail,
				password : $scope.signupPassword,
			}
			
			$http({
				method : 'POST',
				data : $scope.userDetails,
				url : $scope.restUrl + "/registration",
				headers : {
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).success(function(response, data, status, headers,config) {
						if (response.status ==201) {
							$scope.loggedUser = response.entity ;
							$scope.username = $scope.loggedUser.email ;
							sessionStorage.username = $scope.username ;
							$window.location.href = '/index.html';
						}
						else {
							$scope.loginMessage = response.entity.message ;
						}
						$scope.loaderFalse();
		   }).error(function(response, data, status, headers,config) {
						if(response.status == 403){
							$window.location.href = '/registration.html';
						}
						$scope.loginMessage = response.entity.message ;
						$scope.loaderFalse();
					});
		};
		
		
		 //Logout
		$scope.logout = function() {
			$scope.loaderTrue(); 
			$http({
				method : 'POST',
				url : $scope.restUrl + "/logout",
				headers : {
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).success(function(response, data, status, headers,config) {
							$window.location.href = '/login.html';
							$scope.loaderFalse();
		   }).error(function(response, data, status, headers,config) {
							$window.location.href = '/login.html';
							$scope.loaderFalse();
					});
		};


		
		//Forgot My Password - will send user 'Reset password' email 
		 $scope.forgotMyPassword = function(email) {
			 $scope.loaderTrue(); 
			$http({
			  method : "post",
			  url : $scope.restUrl +"/forgotMyPassword",
			  data: email,
				headers : {
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).then(function mySuccess(response) {
			  $scope.forgotMyPasswordError = response.data.entity.message
			  $scope.loaderFalse();
			}, function myError(response) {
			  $scope.myWelcome = response.statusText;
			  $scope.loaderFalse();
			});
		}
		
		 
		 //Update New password from 'Reset.html' page
		 $scope.updateMyPassword = function(passwordToUpdate) {
			 $scope.cleanErrors()
			 $scope.loaderTrue(); 
			 $scope.url =  window.location.search;
			 const urlParams = new URLSearchParams($scope.url);
			 $scope.resetPasswordRequest = {
						newPassword: passwordToUpdate,	 
						token: urlParams.get('token'),
						email: urlParams.get('email')
					 }
			$http({
			  method : "POST",
			  url : $scope.restUrl +"/updatePassword",
			  data: $scope.resetPasswordRequest,
				headers : {
					'Content-Type' : 'application/json',
					'Accept' : 'application/json'
				}
			}).then(function mySuccess(response) {
				$scope.loaderFalse();
				$scope.loginMessage = response.data.entity.message
			}, function myError(response) {
				$scope.loaderFalse();
				$scope.loginMessage = response.data.entity.message
			});
		}
});


