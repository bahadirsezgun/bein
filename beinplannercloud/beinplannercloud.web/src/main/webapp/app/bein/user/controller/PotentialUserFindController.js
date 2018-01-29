ptBossApp.controller('PotentialUserFindController', function($scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

$scope.maskGsm="(999) 999-9999";
	
   $scope.filterName="";
   $scope.filterSurname="";
   $scope.showQuery=true;
   $scope.potentials;
   $scope.potential;
	
   $scope.dateFormat;
   
	$scope.initPUC=function(){
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
		});
	}
	
	$scope.query =function(){
		 $scope.showQuery=true;
	}
	
	$scope.newPotential =function(){
		$location.path("/member/potential/create");
	}
	
	$scope.edit =function(potential){
		$location.path("/member/potential/edit/"+potential.userId);
	}
	$scope.find =function(){
		   
		  var searchObject=new Object();
		  searchObject.userName=$scope.filterName;
		  searchObject.userSurname=$scope.filterSurname;
		
		  $http({
			  method:'POST',
			  url: "/bein/potential/findByName",
			  data: angular.toJson(searchObject)
			}).then(function successCallback(response) {
					$scope.potentials=response.data;
				    $scope.showQuery=false;
			}, function errorCallback(response) {
				$location.path("/login");
			});
	};
	
	
	
});