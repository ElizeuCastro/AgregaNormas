app.directive('validarEmail', function($timeout) {

  return {
    restrict: 'A',
    link: function(scope, element, attrs){

      element.on('blur', function(){
        if (!eEmailValido(element[0].value)){
          $timeout(function() {
            element[0].setCustomValidity('email inválido');
            element[0].focus(); 
          });
        }
      });  

      function eEmailValido(email) {
        var regex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return regex.test(email);
      };

    }
  }
});