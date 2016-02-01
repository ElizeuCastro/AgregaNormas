'use strict';

app.controller('NormasCtrl', 
    function ($scope, $rootScope, $location, $timeout, $routeParams, ServicoNormas, LocalStorage) {

    var NORMA_EMPTY = " Não foram encontradas normas para os filtros indicados ",
        CHOOSE_STATE = "Escolha um estado";
    var tipo = -1;

    restore();    

    /**
    * Recuperar os dados da última pesquisa realizada    
    */
    function restore(){
        var result = LocalStorage.restore($scope);
        if (result === false){
            $scope.mostrarBack = false;
            $scope.mostrarAnos = false;
            $scope.mostrarNormas = false;
            $scope.mostrarPaginacao = false;
            $scope.mostrarEstados = $routeParams.tipoEsfera === EsferaTipo.ESTADUAL;

            $scope.showErro = false;
            $scope.messagemDeErro = NORMA_EMPTY;

            $scope.tipo = -1;
            $scope.numero = "";
            $scope.index = 0;

            $scope.loading = false;

            var tipo = -1;

            buscarEstados();
        }
    };


    /**
    * Busca normas de acordo com o tipo de norma (federal, estadual, municipal)   
    */
    $scope.buscar = function(){
       
        $scope.messagemDeErro = NORMA_EMPTY;
        $scope.showErro = false;

        tipo = $scope.tipo;
        $scope.index = 0;
        $scope.paginaCorrente = 0;

        if ($routeParams.tipoEsfera === EsferaTipo.FEDERAL){
            buscarNormasFederais();        
        } else if ($routeParams.tipoEsfera === EsferaTipo.ESTADUAL){
            buscarNormasEstaduais();        
        } else if ($routeParams.tipoEsfera === EsferaTipo.MUNICIPAL){

        } else {
            console.log("Norma inválida");
        }
    };

    /**
    * Define o comportemento de busca de normas federais
    */
    function buscarNormasFederais (){
        $scope.anos = [];
        $scope.mostrarBack = false;
        $scope.loading = true;

        /*  Se não houver filtros indicados, busca todos os anos que possuem normas
            federais cadastradas */
        if ($scope.tipo === -1 && $scope.numero === ""){
          
            $scope.mostrarAnos = true;
            $scope.mostrarNormas = false;
            $scope.mostrarPaginacao = false;

            ServicoNormas.getNormasFederaisAnos(function(data, status){
                if (status === HttpStatus.OK){
                    if (data !== undefined && data.length > 0){
                        preparaTabelasDeAnos(data);
                        esconderProgress();
                    } else{
                        $scope.loading = false;
                        $scope.mostrarAnos = false;
                        $scope.showErro = true;
                    }
                } else {
                    $scope.loading = false;
                    $scope.mostrarAnos = false;
                    $scope.showErro = true;
                }
            }, function(data, status){
                $scope.loading = false;
                $scope.mostrarAnos = false;
                $scope.showErro = true;
            });

        } else {

            $scope.normas = [];
            var data = {
                tipo: $scope.tipo,     //tipo de norma (lei ou decreto)
                numero: $scope.numero, //número da norma
                limite: 50,            //quantidade de normas a serem retornas  
                inicio: 0              //index de ínicio
            };

            $scope.mostrarAnos = false;
            $scope.mostrarNormas = true;
            $scope.mostrarPaginacao = false;

            ServicoNormas.getNormasFederais(data, function(data, status){
                if (status === HttpStatus.OK){
                    if (data !== undefined && 
                        data.quantidade !== undefined && 
                        data.normas !== undefined && 
                        data.normas.length > 0){

                        preparaNormas(data);

                        esconderProgress();
                    
                    } else {
                        $scope.loading = false;
                        $scope.mostrarNormas = false;
                        $scope.showErro = true;
                    }
                } else {
                    $scope.loading = false;
                    $scope.mostrarNormas = false;
                    $scope.showErro = true;    
                }
            }, function(data, status){
                $scope.loading = false;
                $scope.mostrarNormas = false;
                $scope.showErro = true;
            });

        }
    };

    function esconderProgress(){
         $timeout(function(){

            $scope.loading = false;
            $scope.$digest();

            LocalStorage.save($scope);

        }, 500);
    }

    /**
    * Define o comportemento de busca de normas estaduais
    */
    function buscarNormasEstaduais (){

        //Valida se há um estado selecionado
        if ($scope.estado === undefined || $scope.estado.identificador < 0){

            $scope.messagemDeErro = CHOOSE_STATE;
            $scope.showErro = true;

            return;
        }    

        $scope.loading = true;
        $scope.anos = [];

        /*  Se não houver filtros indicados, busca todos os anos que possuem normas
            estaduais cadastradas */
        if ($scope.tipo === -1 && $scope.numero === ""){

            $scope.mostrarBack = false;
            $scope.mostrarAnos = true;
            $scope.mostrarNormas = false;
            $scope.mostrarPaginacao = false;

            var data = {
                estado: $scope.estado.identificador
            };

            ServicoNormas.getNormasEstaduaisAnos(data, function(data, status){
                if (status === HttpStatus.OK){
                    if (data !== undefined && data.length > 0){
                        preparaTabelasDeAnos(data);
                        esconderProgress();
                    } else{
                        $scope.loading = false;
                        $scope.mostrarAnos = false;
                        $scope.showErro = true;
                    }
                } else {
                    $scope.loading = false;
                    $scope.mostrarAnos = false;
                    $scope.showErro = true;
                }
            }, function(data, status){
                $scope.loading = false;
                $scope.mostrarAnos = false;
                $scope.showErro = true;
            });

        } else {
            
            $scope.normas = [];
            var data = {
                estado: $scope.estado.identificador, //identificador do estado (id do banco)
                tipo: $scope.tipo,
                numero: $scope.numero,
                limite: 50,
                inicio: 0
            };

            $scope.mostrarBack = false;
            $scope.mostrarAnos = false;
            $scope.mostrarNormas = true;
            $scope.mostrarPaginacao = false;

            ServicoNormas.getNormasEstaduais(data, function(data, status){
                if (status === HttpStatus.OK){
                    if (data !== undefined && 
                        data.quantidade !== undefined && 
                        data.normas !== undefined && 
                        data.normas.length > 0){

                        preparaNormas(data);

                        esconderProgress();
                    
                    } else {
                        $scope.loading = false;
                        $scope.mostrarNormas = false;
                        $scope.showErro = true;
                    }
                } else {
                    $scope.loading = false;
                    $scope.mostrarNormas = false;
                    $scope.showErro = true;    
                }
            }, function(data, status){
                $scope.loading = false;
                $scope.mostrarNormas = false;
                $scope.showErro = true;
            });

        }
    };

    /**
    * Busca normas por paginação, cada pagina representa um intervalo de retorna de normas,
    * este intervalo é montado no callback da busca de normas. 
    * ver (método preparaNormas)
    */
    $scope.buscarPorPaginacao = function(subPagina){
        
        $scope.loading = true;
        $scope.showErro = false;
        $scope.normas = [];

        $scope.paginaCorrente = $scope.paginas[$scope.index].subPaginas.indexOf(subPagina);

        if ($routeParams.tipoEsfera === EsferaTipo.FEDERAL){
           
            var data = {
                tipo: tipo,
                numero: undefined,
                limite: 50,
                inicio: subPagina.inicio
            };

            ServicoNormas.getNormasFederais(data, function(data, status){
                if (status === HttpStatus.OK){
                    if (data !== undefined && 
                        data.normas !== undefined &&
                        data.normas.length > 0){
                        $scope.normas = data.normas;
                        esconderProgress();
                    } else {
                        $scope.loading = false;
                        $scope.showErro = true;        
                    }
                } else {
                    $scope.loading = false;
                    $scope.showErro = true;        
                }
            }, function(data, status){
                $scope.loading = false;
                $scope.showErro = true;
            });

        } else if ($routeParams.tipoEsfera === EsferaTipo.ESTADUAL){

            var data = {
                estado: $scope.estado.identificador,
                tipo: tipo,
                numero: undefined,
                limite: 50,
                inicio: subPagina.inicio
            };

            ServicoNormas.getNormasEstaduais(data, function(data, status){
                if (status === HttpStatus.OK){
                    if (data !== undefined && 
                        data.normas !== undefined &&
                        data.normas.length > 0){
                        $scope.normas = data.normas;
                        esconderProgress();
                    } else {
                        $scope.loading = false;
                        $scope.showErro = true;        
                    }
                } else {
                    $scope.loading = false;
                    $scope.showErro = true;        
                }
            }, function(data, status){
                $scope.loading = false;
                $scope.showErro = true;
            });


        } else if ($routeParams.tipoEsfera === EsferaTipo.MUNICIPAL){
            //TODO implementação futura
        }
    };

    /**
    * Define o comportamento de paginação das normas.
    */
    function preparaNormas(data){
        if (data.quantidade > 50){
            
            $scope.paginas = [];
            var subPaginas = [];
            var paginas = Math.ceil(data.quantidade / 50);
            var inicio = 50;

            for (var i = 0; i < paginas; i++) {   
                
                inicio= i > 0 ?  inicio += 50 : 0;

                subPaginas.push(
                    {
                        numero: i + 1,
                        inicio: inicio
                    }
                );  

                if (subPaginas.length === 10){
                    $scope.paginas.push({subPaginas: subPaginas});
                    subPaginas = [];
                }

                if (i === paginas - 1){
                    $scope.paginas.push({subPaginas: subPaginas});
                    break;
                }   
            }  
            $scope.mostrarPaginacao = true;
        }
        $scope.normas = data.normas;
    };

    /**
    *  Busca normas por ano específico
    */
    $scope.buscarNormasPorAno = function(ano){

        $scope.loading = true;
        $scope.showErro = false;
        $scope.normas = [];
        $scope.mostrarPaginacao = false;
        $scope.mostrarBack = false;
        $scope.mostrarAnos = false;
        $scope.mostrarNormas = true;

        if ($routeParams.tipoEsfera === EsferaTipo.FEDERAL){
            
            var data = {
                ano: ano
            };

            ServicoNormas.getNormasFederaisPorAno(data, function(data, status){
                if (status == HttpStatus.OK){
                    if (data !==  undefined && data.length > 0){
                        $scope.normas = data;
                        $scope.mostrarBack = true;
                        esconderProgress();
                    } else{
                        $scope.mostrarBack = false;
                        $scope.showErro = true;
                        $scope.loading = false;
                    }
                } else {
                    $scope.mostrarBack = false;
                    $scope.showErro = true;
                    $scope.loading = false;
                }
            }, function(data, status){
                $scope.mostrarBack = false;
                $scope.showErro = true;
            });

        } else if ($routeParams.tipoEsfera === EsferaTipo.ESTADUAL){
            
            var data = {
                estado: $scope.estado.identificador,
                ano: ano
            };
            
            ServicoNormas.getNormasEstaduaisPorAno(data, function(data, status){
                if (status == HttpStatus.OK){
                    if (data !== undefined && data.length > 0){
                        $scope.normas = data;
                        $scope.mostrarBack = true;
                        esconderProgress();
                    } else{
                        $scope.mostrarBack = false;
                        $scope.showErro = true;
                        $scope.loading = false;
                    }
                } else {
                    $scope.loading = false;
                    $scope.mostrarBack = false;
                    $scope.showErro = true;
                }
            }, function(data, status){
                $scope.loading = false;
                $scope.mostrarBack = false;
                $scope.showErro = true;
            });

        } else if ($routeParams.tipoEsfera === EsferaTipo.MUNICIPAL){

        } else{
            console.log("Norma inválida");
        }
        
    };
   
    /**
    * Define um lista de anos para a renderização da tabela html.
    */
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
    };

    /** Move para a próxima página da tabela de normas*/
    $scope.paginaAnterior = function(){
        if ($scope.index > 0){
            $scope.index -= 1;
            $scope.paginaCorrente = -1;
        }    
    };

    /** Move para a próxima anterior da tabela de normas*/
    $scope.proximaPagina = function(){
        if ($scope.index < $scope.paginas.length - 1){
            $scope.index += 1;
            $scope.paginaCorrente = -1;
        }    
    };

    /** Abre a tela de detalhe da norma */
    $scope.abrirDetalhe = function(norma){
        $rootScope.url = norma.descricao;
        $location.path('/esfera/' + $routeParams.tipoEsfera + '/norma/' + norma.numero + '/detalhe/');

        LocalStorage.save($scope);
    };

    /**Busca os estados que possuem normas estaduais */
    function buscarEstados (){
        
        if ($routeParams.tipoEsfera === EsferaTipo.ESTADUAL){

            $scope.loading = true;
            $scope.showErro = false;
            $scope.estados = [];
            
            ServicoNormas.getEstados(function(data, status){
                if (status == HttpStatus.OK){
                    if (data.length > 0){
                        $scope.estados = data;
                        esconderProgress();
                    } else{
                        $scope.loading = false;
                        console.log("Erro");
                    }
                } else {
                    $scope.loading = false;
                }
            }, function(data, status){
                $scope.loading = false;
                console.log("Erro");
            });
        }
    };


});

