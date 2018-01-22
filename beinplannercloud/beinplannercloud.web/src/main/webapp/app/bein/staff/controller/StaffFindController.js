ptBossApp.controller('StaffFindController', function($rootScope,$http,$routeParams,$scope,$translate,parameterService,$location,homerService,commonService,globals) {
	
	$scope.filterName="";
	$scope.filterSurname="";
	$scope.noMember=false;
	$scope.search;
	
	$scope.staffs;
	$scope.dateFormat;
	$scope.dateTimeFormat;
	
	$scope.init = function(){
		$('.animate-panel').animatePanel();
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
			$scope.find();
		});
		
	};
	
	$scope.search;
	
	$scope.$on("search",function(){
		$scope.search=commonService.search;
	});
	
	
	
	
	
	
	$scope.getStaff=function(staff){
		$location.path("/staff/profile/"+staff.userId);
	}
	
	$rootScope.$on("$routeChangeStart", function (event, next, current) {
		commonService.searchBoxPH="";
		commonService.searchBoxPHItem();
	});
	
	
	$scope.deleteUser =function(user){
		
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteMemberComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: true,
            closeOnCancel: true },
        function (isConfirm) {
		      if (isConfirm) {
				$http({
					  method:'POST',
					  url: "/bein/staff/delete/"+user.userId,
					
				}).then(function successCallback(response) {
						
						if(response.data.resultStatu=="success"){
							toastr.success($translate.instant(response.data.resultMessage));
							$scope.find();
						}else{
							toastr.error($translate.instant(response.data.resultMessage));
						}
					}, function errorCallback(response) {
						$location.path("/login");
					});
			};
	
        });
	};
	
	$scope.research=function(){
		$scope.isSearch=true;
	}
	
	$scope.find =function(){
		
		 $http({
			  method: 'POST',
			  url: "/bein/staff/findAllStaff",
			}).then(function successCallback(response) {
				$scope.staffs=response.data.resultObj;
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
	
	
	
	
});
