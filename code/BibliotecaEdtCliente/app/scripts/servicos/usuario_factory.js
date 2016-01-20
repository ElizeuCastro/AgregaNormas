'use strict';

app.factory('ServicoUsuario', function($http) {

  var URL_REGISTRAR = Host.PATH + '/usuario/registrar';
  var URL_AUTENTICAR= Host.PATH + '/usuario/autenticar';
  var config = {
    headers : {
      'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
    }
  };

  var factory = {};
   
  factory.registrar = function (data, callback) { 

    $http.post(URL_REGISTRAR, data, config)
      .success(function (data, status, headers, config) {
        callback.sucesso(data, status);
      }).error(function (data, status, header, config) {
     	  callback.falha(data, status);
      });
  };

  factory.autenticar = function (data, callback) { 

    $http.post(URL_AUTENTICAR, data, config)
      .success(function (data, status, headers, config) {
        callback.sucesso(data, status);
      }).error(function (data, status, header, config) {
        callback.falha(data, status);
      });
  };
  
  return factory;

}); 