require.config({
	 paths: {
		    'angular': '../../jslib/lib/angular/angular',
			'anSanitize': '../../jslib/lib/angular/angular-sanitize',
	        'main':'./ptbossloginapp',
			'modulloader':'./modulloader',
			'uiMask': '../../jslib/lib/mask/mask.min',
			'antranslate': '../../jslib/lib/angular/angular-translate',
   },
   shim: {
	   'angular': {
           exports: 'angular'
       },
      'main': {
           deps: ['angular']
       },
      'modulloader':{
    	   deps: ['angular','main']
       },
       'anSanitize': {
           deps: ['angular']
       },
       'uiMask': {
           deps: ['angular']
       },
       'antranslate': {
            deps: ['angular']
       }
   },
    deps :[]
});

requirejs(['angular','anSanitize','main','modulloader','uiMask','antranslate'],
  function   (angular ,anSanitize,main,modulloader,uiMask,antranslate ) {
	 
	  angular.element(document).ready(function() {
	      angular.bootstrap(document, ['PTBossLoginApp']);
	  });
	  
  });
