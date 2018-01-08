ptBossLoginApp.config(['$routeProvider',
	 function($routeProvider, $httpProvider) {
    $routeProvider.
	     when('/login', {
	         templateUrl: '/index.html'
	     }).
         otherwise({
             redirectTo: '/index.html'
         });
}]);	