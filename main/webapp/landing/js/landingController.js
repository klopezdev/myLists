// Here we get the module
angular.module('myListsLanding')

// Add landing page controller
.controller('LandingController', LandingController);

// Inject my dependencies (I know I need a service here.....)
LandingController.$inject = ['$scope', '$http', '$location'];

// Now create our controller function with all necessary logic
function LandingController($scope, $http, $location) {

  $scope.user = {
    firstName: null,
    lastName: null,
    email: null,
    phone: null,
    password: null
  }

  $scope.isFormComplete = function() {
    return $scope.user.email != null && $scope.user.phone != null && $scope.user.password != null;
  }

  $scope.submitForm = function() {
	  if($scope.isFormComplete()) {
		  var data = {
		  			firstName: $scope.user.firstName,
		  			lastName: $scope.user.lastName,
		  			emailAddress: $scope.user.email,
		  			smsNumber: $scope.user.phone,
		  			password: $scope.user.password
		  		};
		  
		  $http({
	    		url: 'accountActivationCode', 
	    		method: "POST",
	    		data: data
	    		})
	    		.success( function(response) {
	    			$scope.user.activationCode = response;
				}).error( function(response, status) {
					if(status == 406) {
						window.alert("A user account already exists for the email address: " + $scope.user.email);
					} else {
						window.alert("Unable to request an account activation code! Please contact technical support for assistance with this issue!");
					}
				});
	  }
  }
  
  $scope.isLogInFormComplete = function() {
	  return $scope.user.email != null && $scope.user.password != null;
  }
  
  $scope.submitLogInForm = function() {
	  if($scope.isLogInFormComplete()) {
		  var data = {
		  			emailAddress: $scope.user.email,
		  			password: $scope.user.password,
		  			rememberMePlease: $scope.user.rememberMePlease
		  		};
		  
		  $http({
	    		url: 'login', 
	    		method: "POST",
	    		params: data,
	    		headers: { 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
	    		})
	    		.success( function(response) {
	    			window.location.assign("/myLists/dashboard/dashboard.html");
				}).error( function(response, status) {
					if(status == 406) {
						window.alert("A user account already exists for the email address: " + $scope.user.email);
					} else if (status == 401) {
						window.alert(response.message);
					} else {
						window.alert("Unable to request an account activation code! Please contact technical support for assistance with this issue!");
					}
				});
	  }
  }
  
  var queryString = $location.search();
  if(queryString.showLoginForm) {
	  $("#logInForm").modal()
  }
}
