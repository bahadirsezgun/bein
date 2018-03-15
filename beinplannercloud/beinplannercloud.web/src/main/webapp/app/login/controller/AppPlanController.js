ptBossLoginApp.controller('AppPlanController', function($scope,$translate,$http,$location) {
	
	
	$scope.currency="TRY";
	$scope.planBasic="";
	$scope.planPremium="";
	$scope.planPremium="";
	$scope.planPrestige="";
	
	
	$scope.descriptionBasic="BASIC PLAN";
	$scope.descriptionStandart="STANDART PLAN";
	$scope.descriptionPremium="PREMIUM PLAN";
	$scope.descriptionPrestige="PRESTIGE PLAN";
	
	
	$scope.init=function(){
		
		 var userLang = navigator.language || navigator.userLanguage; 
	     $translate.use(userLang);
	     
	     if(userLang=="tr"){
	    	 $scope.currency="TRY";
	    	 $scope.planBasic="bbptryv1";
	    	 $scope.planStandard="bsptryv1";
	    	 $scope.planPremium="bpptryv1";
	    	 $scope.planPrestige="bprptryv1";
	     }else{
	    	 $scope.currency="USD";
	    	 $scope.planBasic="bbpusdv1";
	    	 $scope.planStandard="bspusdv1";
	    	 $scope.planPremium="bppusdv1";
	    	 $scope.planPrestige="bprpusdv1";
	     }
	     
	     
	};
	
});