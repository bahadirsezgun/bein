ptBossApp.controller('DefinitionTestController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	$scope.defTest;
	$scope.defTests;
	
	
	toastr.options = {
	        "debug": false,
	        "newestOnTop": false,
	        "positionClass": "toast-top-center",
	        "closeButton": true,
	        "toastClass": "animated fadeInDown",
	    };
	
	$scope.init = function(){
     	$("[data-toggle=popover]").popover();
    		commonService.pageName=$translate.instant("testTitle");
		commonService.pageComment=$translate.instant("defTestDefinitionComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		findAllDefTest();
		
    };
    
    $scope.addNewDefTest =function(){
		$scope.defTest=new Object();
		$scope.defTest.testId="0";
		$scope.defTest.testName="";
		
		$scope.willDefTestCreate=true;
	};
    
	
	
	
	$scope.deleteDefTest =function(defTest){
		$http({
			  method: 'POST',
			  url: "/bein/definition/defTest/delete",
			  data:angular.toJson(defTest)
			}).then(function successCallback(response) {
				toastr.success($translate.instant(response.data.resultMessage))
				findAllDefTest();
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
	
	
	$scope.createDefTest =function(){
		$http({
			  method: 'POST',
			  url: "/bein/definition/defTest/create",
			  data:angular.toJson($scope.defTest)
			}).then(function successCallback(response) {
				$scope.defTest=response.data.resultObj;
				
				$scope.willDefTestCreate=true;
				findAllDefTest();
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
		
	
	
	$scope.showDefTest =function(testId){
		$http({
			  method: 'POST',
			  url: "/bein/definition/defTest/findById/"+testId
			}).then(function successCallback(response) {
				$scope.defTest=response.data.resultObj;
				$scope.willDefTestCreate=true;
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
		
	};
	
    function findAllDefTest(){
		$http({
		  method: 'POST',
		  url: "/bein/definition/defTest/findAll"
		}).then(function successCallback(response) {
			$scope.defTests=response.data;
			if($scope.defTests.length!=0){
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