ptBossApp.controller('ClassStaticBonusController', function($scope,$translate,parameterService,$location,homerService,commonService,globals) {

	
	$scope.defBonus=new Object();
	$scope.defBonus.bonusValue;
	$scope.defBonus.bonusCount;
	$scope.defBonus.bonusId=0;
	$scope.defBonus.bonusType=2;
	$scope.defBonus.bonusIsType=2;
	$scope.defBonus.bonusProgId="0";
	$scope.defBonus.userId=$scope.userId;
	
	
	$scope.defBonuses;
	$scope.programClasses;
	$scope.noProgram;
	
	$scope.initCStaticBonus=function(){
		$("[data-toggle=popover]").popover();
		findClassPrograms();
		findUserClassStaticBonus();
	};
	
	
	function findUserClassStaticBonus(){
			  
			    $http({
					  method:'POST',
					  url: "/bein/staff/bonus/findClassStaticBonus/"+$scope.userId,
					}).then(function successCallback(response) {
						$scope.defBonuses=response.data;
					}, function errorCallback(response) {
						$location.path("/login");
					});
			   
	}
	
	
	$scope.deleteClassStaticBonus=function(bonusId){
		$http({
			  method:'POST',
			  url: "/bein/staff/bonus/delete/"+bonusId,
			}).then(function successCallback(response) {
				var res=response.data;
				
				if(res.resultStatu=="1"){
					findUserClassRateBonus();
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
			}, function errorCallback(response) {
				$location.path("/login");
			});
		
	}
	
	$scope.addClassStaticBonus=function(bonusId){
		 $http({
			  method:'POST',
			  url: "/bein/staff/bonus/create",
			  data: angular.toJson($scope.defBonus),
		   }).then(function successCallback(response) {
				
				var res=response.data;
				
				if(res.resultStatu=="1"){
					findUserClassStaticBonus();
					toastr.success($translate.instant("success"));
				}else{
					toastr.error($translate.instant(res.resultMessage));
				}
			}, function errorCallback(response) {
				$location.path("/login");
			});
	}
	
	$scope.newClassStaticBonus=function(bonusId){
		$scope.defBonus=new Object();
		$scope.defBonus.bonusValue;
		$scope.defBonus.bonusCount;
		$scope.defBonus.bonusId=0;
		$scope.defBonus.bonusType=2;
		$scope.defBonus.bonusIsType=2;
		$scope.defBonus.bonusProgId="0";
		$scope.defBonus.userId=$scope.userId;
	}
	
	
	
	function findClassPrograms(){
		$http({
		  method: 'POST',
		  url: "/bein/program/findClassPrograms"
		}).then(function successCallback(response) {
			$scope.programClasses=response.data.resultObj;
			if($scope.programClasses.length!=0){
				$scope.defBonus.bonusProgId="0";
				$scope.noProgram=false;
			}else{
				$scope.noProgram=true;
			}
			
		}, function errorCallback(response) {
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.
		});
	}
	
	
	
});