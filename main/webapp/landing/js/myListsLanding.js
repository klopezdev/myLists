var myListsLanding = angular.module('myListsLanding', ['ngRoute']);

myListsLanding.config(function ($routeProvider) {
  $routeProvider
    .when('/registration', {
      templateUrl: 'pages/registration.html'
    })
    .when('/exciteMe', {
      templateUrl: 'pages/getMeExcited.html'
    })
    .when('/whySMS', {
      templateUrl: 'pages/whySMS.html'
    })
    .when('/premium', {
      templateUrl: 'pages/premium.html'
    })
    .otherwise({
      redirectTo: 'registration'
    }); 
});
