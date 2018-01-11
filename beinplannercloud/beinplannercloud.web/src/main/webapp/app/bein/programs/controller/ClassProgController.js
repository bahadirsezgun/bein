ptBossApp.controller('ClassProgController', function($rootScope,$scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	
	
	$scope.programClass=new Object();
	$scope.programClass.progStatus="1";
	$scope.programClass.progUserId="0";
	$scope.programClass.type="pc";
	$scope.programClass.restType="0";
	$scope.programClass.minMemberCount=1;
	$scope.programClass.maxMemberCount=10;
	$scope.programClass.progPrice=0;
	$scope.programClass.restDuration=1;
	
    $scope.programClasses;
	$scope.noProgram=true;
	$scope.willProgramCreate=false;
	$scope.programInstructors;
	
	toastr.options = {
        "debug": false,
        "newestOnTop": false,
        "positionClass": "toast-top-center",
        "closeButton": true,
        "toastClass": "animated fadeInDown",
    };

    $scope.init = function(){
     	$("[data-toggle=popover]").popover();
    		commonService.pageName=$translate.instant("definition_personelprog");
		commonService.pageComment=$translate.instant("personelProgDefinitionComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		findInstructors();
		findClassPrograms();
		
    };
   
    function findInstructors(){
    	     $http({
			  method: 'POST',
			  url: "/bein/staff/findAllSchedulerStaff"
			}).then(function successCallback(response) {
				$scope.programInstructors=response.data.resultObj;
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
    }
    
    function findClassPrograms(){
    		$http({
			  method: 'POST',
			  url: "/bein/program/findClassPrograms"
			}).then(function successCallback(response) {
				$scope.programClasses=response.data.resultObj;
				if($scope.programClasses.length!=0){
					$scope.noProgram=false;
				}else{
					$scope.noProgram=true;
				}
				
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	}
   
	
	
	$scope.addNewClassProgram =function(){
		$scope.programClass=new Object();
		$scope.programClass.progStatus="1";
		$scope.programClass.progUserId="0";
		$scope.programClass.restFlag="0";
		$scope.programClass.type="pc";
		$scope.programClass.progDescription="";
		$scope.programClass.progComment="";
		$scope.programClass.restType="0";
		$scope.programClass.progPrice=0;
		$scope.programClass.restDuration=1;
		$scope.willProgramCreate=true;
	};
	
	$scope.showProgram =function(progId){
		$http({
			  method: 'POST',
			  url: "/bein/program/findClassProgramById/"+progId
			}).then(function successCallback(response) {
				$scope.programClass=response.data.resultObj;
				$scope.programClass.progStatus=""+$scope.programClass.progStatus;
				$scope.programClass.progUserId=""+$scope.programClass.progUserId;
				$scope.programClass.restFlag=""+$scope.programClass.restFlag;
				$scope.programClass.restType=""+$scope.programClass.restType;
				$scope.programClass.type="pc";
				$scope.willProgramCreate=true;
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
		
	};
	
	
	
	$scope.createClassProgram =function(){
		$http({
			  method: 'POST',
			  url: "/bein/program/createClassProgram",
			  data:angular.toJson($scope.programClass)
			}).then(function successCallback(response) {
				$scope.programClass=response.data.resultObj;
				$scope.programClass.progStatus=""+$scope.programClass.progStatus;
				$scope.programClass.progUserId=""+$scope.programClass.progUserId;
				$scope.programClass.restFlag=""+$scope.programClass.restFlag;
				$scope.programClass.restDuration=$scope.programClass.restDuration;
				$scope.programClass.restType=""+$scope.programClass.restType;
				$scope.programClass.type="pc";
				$scope.willProgramCreate=true;
				findClassPrograms();
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	}
	
	$scope.deleteClassProgram =function(progId){
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteProgramComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: false,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
              	$http({
	   				  method: 'POST',
	   				  url: "/bein/program/deleteClassProgram/"+progId,
	   				}).then(function successCallback(response) {
	   					if(response.data.resultStatu=="1"){
	     					swal($translate.instant("deleted"), $translate.instant("deletedSuccessMessage"), "success");
	     					$scope.willProgramCreate=false;
	     					findClassPrograms();
	     				}else{
	     					swal($translate.instant("nodeleted"), $translate.instant("programUsedInSales"), "error");
	     				}
	   				}, function errorCallback(response) {
	   				    // called asynchronously if an error occurs
	   				    // or server returns response with an error status.
	   				});
            } else {
                swal($translate.instant("deleteCanceled"), "");
            }
        });
	};
});