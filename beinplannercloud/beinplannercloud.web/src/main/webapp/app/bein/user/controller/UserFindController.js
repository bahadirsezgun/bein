ptBossApp.controller('UserFindController', function($rootScope,$scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {
	
	$scope.filterName="";
	$scope.filterSurname="";
	$scope.noMember=false;
	$scope.search;
	$scope.isSearch=true;
	$scope.members;
	$scope.dateFormat;
	$scope.dateTimeFormat;
	
	$scope.init = function(){
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
		});
	};
	
	
	$scope.$on("search",function(){
		$scope.search=commonService.search;
	});
	
	$scope.hidePackets=function(){
		$scope.packetPaymentPage=""
		$scope.makePayment=false;
	}
	
	$scope.showPackets=function(member){
		$scope.selectedMember=member;
		$scope.packetPaymentPage="./packetpayment/payment.html"
		$scope.makePayment=true;
	}
	
	
	$scope.findKey=function(keyEvent){
		if (keyEvent.which === 13){
			$scope.find();
		}
	}
	
	
	
	
	$scope.getMember=function(member){
		$location.path("/member/profile/"+member.userId);
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
					  url: "/bein/member/delete/"+user.userId,
					
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
		 var user=new Object();
		 user.userName=$scope.filterName;
		 user.userSurname=$scope.filterSurname;
		
		 $http({
			  method: 'POST',
			  url: "/bein/member/findByUsernameAndUsersurname",
			  data:angular.toJson(user)
			}).then(function successCallback(response) {
				$scope.members=response.data.resultObj;
				$scope.isSearch=false;
				if($scope.members.length==0){
					$scope.noMember=true;
					$scope.searchSection=false;
				}else{
					$scope.noMember=false;
					$scope.searchSection=false;
				}
				
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	};
	
	
	
	
});
