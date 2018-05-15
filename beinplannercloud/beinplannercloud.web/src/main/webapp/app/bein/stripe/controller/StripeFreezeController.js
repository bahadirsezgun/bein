ptBossApp.controller('StripeFreezeController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	$scope.init = function(){
		
	};
	
	
	$scope.freezeMyAccount=function(){
		 $http({
			  method:'POST',
			  url: "/bein/stripeService/freeze",
			  data: angular.toJson($scope.ptGlobalLocal),
			}).then(function successCallback(response) {
				$location.path("/login");
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
});