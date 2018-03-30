ptBossLoginApp.controller('AppPlanController', function($scope,$translate,$http,$location) {
	
	
	$scope.currency="TRY";
	$scope.planBasic="";
	$scope.planPremium="";
	$scope.planPremium="";
	$scope.planPrestige="";
	
	$scope.m100000="100000";
	$scope.m50="50";
	$scope.m150="150";
	$scope.m450="450";
	
	
	$scope.descriptionBasic="BASIC PLAN";
	$scope.descriptionStandard="STANDART PLAN";
	$scope.descriptionPremium="PREMIUM PLAN";
	$scope.descriptionPrestige="PRESTIGE PLAN";
	$scope.lang="";
	
	$scope.init=function(){
		
		 var userLang = navigator.language || navigator.userLanguage; 
	     
	   
	     if((userLang).substring(0,2)=="tr"){
	    	 $scope.currency="USD";
	    	 $scope.planBasic="bbptryv1";
	    	 $scope.planStandard="bsptryv1";
	    	 $scope.planPremium="bpptryv1";
	    	 $scope.planPrestige="bprptryv1";
	    	 $scope.lang="tr_TR";
	    	 $translate.use("tr");
	     }else{
	    	 $scope.currency="USD";
	    	 $scope.planBasic="bbpusdv1";
	    	 $scope.planStandard="bspusdv1";
	    	 $scope.planPremium="bppusdv1";
	    	 $scope.planPrestige="bprpusdv1";
	    	 $scope.lang="en_EN";
	    	 $translate.use("en");
	     }
	     
	     
	};
	
});