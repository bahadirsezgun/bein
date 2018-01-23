ptBossApp.controller('GlobalController', function($scope,$translate,parameterService,$location,homerService,commonService) {

	$scope.ptGlobal=new Objet();
	
	$scope.ptGlobal.glbId=1;
	$scope.ptGlobal.ptTz="Europe/Istanbul";
	$scope.ptGlobal.ptCurrency="$";
	$scope.ptGlobal.ptStaticIp="";
	$scope.ptGlobal.ptLang="tr_TR";
	$scope.ptGlobal.ptDateFormat="%d/%m/%y";
	
	
	
	toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	$scope.init = function(){
		
		   
			
			commonService.pageName=$translate.instant("settings_globalTitle");
			commonService.pageComment=$translate.instant("settings_globalComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
			
			
			commonService.getPtGlobal().then(function(global){
				$scope.ptGlobal=global;
			});
			
			
	}
	
	
	
	
	
	
	$scope.createPtGlobal =function(){
		
		   $http({
			  type:'POST',
			  url: "/bein/setting/global/create",
			  data: angular.toJson($scope.ptGlobal),
			}).then(function successCallback(response) {
				
				var ptGlob=response.data;
				
				if(ptGlob!=null){
					if(ptGlob.resultStatu=="success"){
						toastr.success($translate.instant(ptGlob.resultMessage.trim()));
						$scope.ptGlobal=ptGlob.resultObj;
						
						if($scope.ptGlobal.ptLang!=""){
							var lang=$scope.ptGlobal.ptLang.substring(0,2);
							  $translate.use(lang);
							  localStorage.setItem('lang', lang);
							  commonService.setPtGlobal($scope.ptGlobal);
							  commonService.changeLang(lang);	
						}
						
					}else{
						toastr.error($translate.instant(res.resultMessage.trim()));
					}
				}
				
			}, function errorCallback(response) {
				$location.path("/login");
			});
		
		
	};
	
	
	
	
});