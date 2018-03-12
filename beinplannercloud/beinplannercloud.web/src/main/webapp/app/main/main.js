require.config({
   //urlArgs: "version="+(new Date()).getTime(),
   waitSeconds: 200,
   paths: {
		    'angular': '../../jslib/lib/angular/angular',
			'anSanitize': '../../jslib/lib/angular/angular-sanitize',
			'uiMask': '../../jslib/lib/mask/mask.min',
	        'ptbossapp':'../../app/main/ptbossapp',
	        'anroute': '../../jslib/lib/angular/angular-route',
	        'modulloader':'../../app/main/modulloader',
			'antranslate': '../../jslib/lib/angular/angular-translate',
			'ngDraggable': '../../homerlib/vendor/draggable/ngDraggable',
			'ngPrint': '../../jslib/lib//print/ngPrint.min'
   },
   shim: {
	   'angular': {
           exports: 'angular'
       },
      'ptbossapp': {
           deps: ['angular']
       },
      'modulloader':{
    	   deps: ['angular','main']
       },
       'anSanitize': {
           deps: ['angular']
       },
       'antranslate': {
            deps: ['angular']
       },
       'uiMask': {
           deps: ['angular']
       },
      	'anroute': {
           deps: ['angular']
       },
     	'ngDraggable': {
            deps: ['angular']
        },
     	'ngPrint': {
            deps: ['angular']
        }
      
   },
    deps :[]
});

requirejs(['angular','anSanitize','ptbossapp','modulloader','antranslate','anroute','uiMask','ngDraggable','ngPrint'],
  function   (angular ,anSanitize,ptbossapp,modulloader,antranslate,anroute,uiMask,ngDraggable,ngPrint ) {
	 
	  angular.element(document).ready(function() {
	      angular.bootstrap(document, ['PTBossApp']);
	  });
	  
  });

