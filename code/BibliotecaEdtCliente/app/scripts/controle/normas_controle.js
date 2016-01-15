'use strict';

app.controller('NormasCtrl', function ($scope, $location, ServicoNormas) {

    $scope.tipo = -1;
    $scope.numero = "";

    $scope.buscar = function(){
        $scope.anos = [];
        if ($scope.tipo === -1 && $scope.numero === ""){
            ServicoNormas.getAnos(function(data, status){
                if (status == HttpStatus.OK){
                    preparaTabelasDeAnos(data);
                }
            }, function(data, status){
                console.log(data);
            });
        } else {

        }
    };

    $scope.buscarNormasPorAno = function(ano){
        $scope.normas = null;
        var data = {
            ano: ano
        };
        ServicoNormas.getNormasPorAno(data, function(data, status){
            if (status == HttpStatus.OK){
               $scope.normas = data;
            }
        }, function(data, status){
            console.log(data);
        });
    };
   
    function preparaTabelasDeAnos (data){
        var colunas = [];
        for (var i = 0; i < data.length; i++) {
           
            if (colunas.length < 5){
                colunas.push(data[i].ano); 
            }

            if (colunas.length === 5 || i === data.length -1){
                $scope.anos.push(
                    {
                        colunas: colunas  
                    }
                );  
                colunas = [];
            } 
        }
        console.log($scope.anos);
    };
});
