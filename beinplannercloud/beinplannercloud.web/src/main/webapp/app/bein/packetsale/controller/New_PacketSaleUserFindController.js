ptBossApp.controller('New_PacketSaleUserFindController', function($rootScope,$scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {
	
	$scope.filterName="";
	$scope.filterSurname="";
	
	$scope.noMember=false;
	$scope.searchSection=true;
	$scope.members;
	$scope.search;
	
	$scope.dateFormat;
	
	$scope.init = function(){
		$('.animate-panel').animatePanel();
		commonService.pageName=$translate.instant("packetProcess");
		commonService.pageComment=$translate.instant("packetProcessComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		commonService.getPtGlobal().then(function(result) {
			$scope.dateFormat=commonService.ptGlobal.ptDateFormat;
			$scope.dateTimeFormat=commonService.ptGlobal.ptDateTimeFormat;
			
		});
	};
	
	$scope.$on("search",function(){
		$scope.search=commonService.search;
	});
	
	$scope.findKey=function(keyEvent){
		if (keyEvent.which === 13){
			$scope.find();
		}
	}
	
	$scope.saleToUser =function(userId){
		$location.path('/packetsale/saletouser/'+userId);
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
				
				if($scope.members.length==0){
					$scope.noMember=true;
					$scope.searchSection=false;
				}else{
					$scope.noMember=false;
					$scope.searchSection=false;
				}
				
			}, function errorCallback(response) {
				$location.path("/login");
			});
	};
	
	
	$rootScope.$on("$routeChangeStart", function (event, next, current) {
		commonService.searchBoxPH="";
		commonService.searchBoxPHItem();
	});
	
	
});