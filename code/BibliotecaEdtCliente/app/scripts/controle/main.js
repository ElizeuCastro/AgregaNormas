'use strict';

app.controller('MainCtrl', function ($scope, ServicoUsuario) {

    $scope.btnRegistrar = "Registrar";
    $scope.confirmaSenha = "";
  

   $scope.registrar = function () {           

        $scope.campoVazio = "";
        $scope.emailInvalido = "";
        $scope.loginInvalido = "";
        $scope.senhaInvalida = "";
        $scope.confirmarSenhaInvalida = "";
        $scope.mostrarErro = false;

        var temErro = false;

        var data = $.param({
            nome:  $scope.nome,
            email: $scope.email,
            login: $scope.login,
            senha: $scope.senha
        });

        var callback= {
            sucesso : function(data, status){
                console.log(status);        
                console.log(data.identificador);        
                console.log(data.nome);        
                console.log(data.email);        
                console.log(data.login);        
                console.log(data.senha);        
            },
            falha : function(data, status){
                $scope.btnRegistrar = "Registrar"; 
                if (status == HttpStatus.CONFLICT){
                    if (data.codigo == ErroDeUsuario.LOGIN_DUPLICADO){
                        $scope.loginInvalido = data.mensagem;
                    } else if (data.codigo == ErroDeUsuario.EMAIL_DUPLICADO){
                        $scope.emailInvalido = data.mensagem;
                    } 
                    $scope.mostrarErro = true;
                }      
            }
        };
             
        $scope.btnRegistrar = "Registrando ... "; 

        if (temCampoVazio([$scope.nome,$scope.email,$scope.login,$scope.senha])){
            temErro = true;
            $scope.campoVazio = "Exitem campos vazios";       
        };
       
        if (!eEmailValido($scope.email)){
            temErro = true;
            $scope.emailInvalido = "Email inválido";     
        };

        if (!eApelidoValido($scope.login)){
            temErro = true;
            $scope.loginInvalido = "Apelido deve conter entre  6 - 20 caracteres";     
        };

        if (!eSenhaValida($scope.senha)){
            temErro = true;
            $scope.senhaInvalida = "Senha deve conter entre  6 - 20 caracteres";     
        };

        if (!confirmarSenha($scope.senha, $scope.confirmaSenha)){
            temErro = true;
            $scope.confirmarSenhaInvalida = "As senhas não são iguais";     
        };

        $scope.mostrarErro = temErro;

        if (!$scope.mostrarErro){
            ServicoUsuario.registrar(data, callback);
        } else{
            $scope.btnRegistrar = "Registrar"; 
        }
    };

    function eEmailValido(email) {
        var regex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return regex.test(email);
    };

    function temCampoVazio(valores) {
        for( var i = 0; i < valores.length; i++ ) {
            if (valores[i] == undefined || valores[i] == ''){
                return true;
            }
        }
        return false;
    };

    function eSenhaValida(senha) {
        return senha != undefined && senha.length >= 6;
    };

    function eApelidoValido(login) {
        return login != undefined && login.length >= 6;
    };

    function confirmarSenha(login, confirmaSenha) {
        console.log(login);
        console.log(confirmaSenha);
        return login != undefined && confirmaSenha != undefined && login === confirmaSenha;
    };

});
