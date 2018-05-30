ptBossApp.controller('UserTestController', function($rootScope,$scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {
	
	
	
	$scope.initUTC = function(){
		
		$scope.findDefTest().then(function(response){
			$scope.defTests=response.data;
			$scope.findTest();
		})
		
	};
	
	
	$scope.userTests;
	$scope.userTest=new Object();
	$scope.userTest.userId=0;
	$scope.userTest.testId="0";
	$scope.userTest.testDate=new Date();
	
	$scope.defTests;
	
	$scope.findDefTest =function(){
		return $http({
					  method: 'POST',
					  url: "/bein/definition/defTest/findAll"
				}).then(function successCallback(response) {
					return response;
				}, function errorCallback(response) {
				    // called asynchronously if an error occurs
				    // or server returns response with an error status.
				});
	};
	
	$scope.findTest =function(){

		
		 $http({
			  method: 'POST',
			  url: "/bein/userTest/usertest/find/"+$scope.member.userId,
			}).then(function successCallback(response) {
				$scope.userTests=response.data.resultObj;
				
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
	
	$scope.createTest =function(){

		if($scope.userTest.testId=="0"){
			toastr.error($translate.instant("pleaseChooseTest"));
			return;
		}
		
		 $scope.userTest.userId=$scope.member.userId;
		 $http({
			  method: 'POST',
			  url: "/bein/userTest/usertest/create",
			  data:angular.toJson($scope.userTest)
			}).then(function successCallback(response) {
				if(response.data.resultStatu="success"){
					toastr.success($translate.instant(response.data.resultMessage));
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
				$scope.findTest();
				
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
	
	
	$scope.updateTest=function(userTest){
		
		$scope.userTest=userTest;
		$scope.userTest.testId=""+userTest.testId;
		$scope.userTest.testDate=new Date(userTest.testDate);
		
		
		$scope.showTest=false;
	}
	
	$scope.deleteTest =function(userTest){
		 $http({
			  method: 'POST',
			  url: "/bein/userTest/usertest/delete",
			  data:angular.toJson(userTest)
			}).then(function successCallback(response) {
				if(response.data.resultStatu="success"){
					toastr.success($translate.instant(response.data.resultMessage));
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
				$scope.findTest();
				
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	}
	
});