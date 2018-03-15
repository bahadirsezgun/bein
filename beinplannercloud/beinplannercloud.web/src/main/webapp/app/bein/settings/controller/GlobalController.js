ptBossApp.controller('GlobalController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {

	$scope.ptGlobalLocal=new Object();
	
	$scope.ptGlobalLocal.glbId=1;
	$scope.ptGlobalLocal.ptTz="-";
	$scope.ptGlobalLocal.ptCurrency="$";
	$scope.ptGlobalLocal.ptStaticIp="0.0.0.0";
	$scope.ptGlobalLocal.ptLang="-";
	$scope.ptGlobalLocal.ptDateFormat="-";
	
	
	
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
			$http({method:"POST", url:"/bein/global/getGlobals"}).then(function(response){
				$scope.ptGlobalLocal=response.data.resultObj;
				
			});
	}
	
	$scope.createPtGlobal =function(){
		
		   $http({
			  method:'POST',
			  url: "/bein/settings/global/create",
			  data: angular.toJson($scope.ptGlobalLocal),
			}).then(function successCallback(response) {
				
				var ptGlob=response.data;
				
				if(ptGlob!=null){
					if(ptGlob.resultStatu=="success"){
						toastr.success($translate.instant(ptGlob.resultMessage.trim()));
						$scope.ptGlobal=ptGlob.resultObj;
						
						if($scope.ptGlobal.ptLang!=""){
							var lang=$scope.ptGlobalLocal.ptLang.substring(0,2);
							  $translate.use(lang);
							  localStorage.setItem('lang', lang);
							  commonService.setPtGlobal($scope.ptGlobalLocal);
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