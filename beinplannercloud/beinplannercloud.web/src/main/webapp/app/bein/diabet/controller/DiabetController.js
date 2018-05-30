ptBossApp.controller('DiabetController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	$scope.showDiabet=true;
	
	$scope.dateFormat;
	
	$scope.diabetCalori=new Object();
	$scope.diabet=new Object();
	$scope.diabet.diaDate=new Date();
	
	$scope.diabet.diaMorningCal=0;
	$scope.diabet.diaMorningProtein=0;
	$scope.diabet.diaMorningComment="";
	
	$scope.diabet.diaMorningGapCal=0;
	$scope.diabet.diaMorningGapProtein=0;
	$scope.diabet.diaMorningGapComment="";
	
	$scope.diabet.diaAfternoonCal=0;
	$scope.diabet.diaAfternoonProtein=0;
	$scope.diabet.diaAfternoonComment="";
	
	$scope.diabet.diaAfternoonGapCal=0;
	$scope.diabet.diaAfternoonGapProtein=0;
	$scope.diabet.diaAfternoonGapComment="";
	
	$scope.diabet.diaNightCal=0;
	$scope.diabet.diaNightProtein=0;
	$scope.diabet.diaNightComment="";
	
	$scope.diabet.diaNightGapCal=0;
	$scope.diabet.diaNightGapProtein=0;
	$scope.diabet.diaNightGapComment="";
	
	
	
	$scope.diabets=new Array();
	
	
	$scope.initDIA=function(){
		$scope.diabetCalori.calAmount=0;
		findAllDiabetCalori().then(function(diabetCalori){
			if(diabetCalori!=""){
				findAllDiabet(diabetCalori.udcId);
			}
			
		});
	}
	
	
	$scope.createDiabetCalori=function(){
		$(".splash").css("display",'');
		
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptScrDateFormat+" HH:mm";
			
		
		
		return $http({
			  method:'POST',
			  url: "/bein/diabet/calori/create",
			  data:angular.toJson($scope.diabetCalori)
			}).then(function successCallback(response) {
				if(response.data.resultStatu=="success"){
					toastr.succes($translate.instant(response.data.resultMessage));
					findAllDiabetCalori().then(function(diabetCalori){
						if(diabetCalori!=""){
							findAllDiabet(diabetCalori.udcId);
						}
					});
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
				$(".splash").css("display",'none');
				
			}, function errorCallback(response) {
				$(".splash").css("display",'none');
			});
		});
	}
	
	
	
	$scope.updateDiabet=function(diabet){
		$scope.diabet=diabet;
		$scope.showDiabet=false;
	}
	
	$scope.deleteDiabetCalori=function(diabetCalori){
		$(".splash").css("display",'');
		$http({
			  method:'POST',
			  url: "/bein/diabet/calori/delete",
			  data:angular.toJson(diabetCalori)
			}).then(function successCallback(response) {
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					findAllDiabetCalori().then(function(diabetCalori){
						if(diabetCalori!=""){
							findAllDiabet(diabetCalori.udcId);
						}
					});
					return response.data.resultObj;
					
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
					return null;
				}
				
				$(".splash").css("display",'none');
				
			}, function errorCallback(response) {
				$(".splash").css("display",'none');
				return null;
			});
	}
	
	
	$scope.createDiabet=function(){
		$(".splash").css("display",'');
		
		
		$scope.diabet.udcId=$scope.diabetCalori.udcId;
		$scope.diabet.calAmount=$scope.diabetCalori.calAmount;
		$scope.diabet.userId=$scope.userId;
		$scope.diabet.saleId=$scope.packetSale.saleId;
		$scope.diabet.saleType=$scope.packetSale.progType;
		
		$http({
			  method:'POST',
			  url: "/bein/diabet/create",
			  data:angular.toJson($scope.diabet)
			}).then(function successCallback(response) {
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					findAllDiabetCalori().then(function(diabetCalori){
						if(diabetCalori!=""){
							findAllDiabet(diabetCalori.udcId);
						}
					});
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
				}
				
				$(".splash").css("display",'none');
				
			}, function errorCallback(response) {
				$(".splash").css("display",'none');
			});
	}
	
	$scope.deleteDiabet=function(diabet){
		$(".splash").css("display",'');
		$http({
			  method:'POST',
			  url: "/bein/diabet/delete",
			  data:angular.toJson(diabet)
			}).then(function successCallback(response) {
				if(response.data.resultStatu=="success"){
					toastr.success($translate.instant(response.data.resultMessage));
					findAllDiabetCalori().then(function(diabetCalori){
						if(diabetCalori!=""){
							findAllDiabet(diabetCalori.udcId);
						}
					});
					
				}else{
					toastr.error($translate.instant(response.data.resultMessage));
					
				}
				$(".splash").css("display",'none');
			}, function errorCallback(response) {
				$(".splash").css("display",'none');
				
			});
	}
	
	
	function findAllDiabetCalori(){
		return $http({
			  method:'POST',
			  url: "/bein/diabet/calori/find/"+$scope.userId+"/"+$scope.packetSale.saleId+"/"+$scope.packetSale.progType
			}).then(function successCallback(response) {
				$scope.diabetCalori=response.data;
				return response.data;
			}, function errorCallback(response) {
				$(".splash").css("display",'none');
			});
	}
	
	function findAllDiabet(udcId){
		return $http({
			  method:'POST',
			  url: "/bein/diabet/find/"+udcId
			}).then(function successCallback(response) {
				$(".splash").css("display",'none');
				$scope.diabets=response.data;
				return response.data;
			}, function errorCallback(response) {
				$(".splash").css("display",'none');
			});
	}
	
	
});