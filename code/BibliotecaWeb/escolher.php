<!DOCTYPE HTML>
<html>
<head>
<center>
<title>Biblioteca EDT</title>
<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  
<link rel="stylesheet" href="css/styles.css" />

<script language="Javascript">
      
function showDiv(idSelect){

    if(idSelect == "plano1"){
         document.getElementById('plano2').style.display = "none";
         document.getElementById('plano3').style.display = "none";
    }else if(idSelect == 'plano2'){
            document.getElementById('plano2').style.display = "block";
            document.getElementById('plano3').style.display = "none";

     }else if(idSelect == 'plano3'){
            document.getElementById('plano3').style.display = "block";
            document.getElementById('plano2').style.display = "none";

    }
}
</script>
</head>
    
<body >
        
<div data-role="header" data-position="fixed">    
    <a href="#"  onClick="history.go(-1)">Sair</a>
		<h2>Biblioteca EDT</h2>
	</div>
        
        Qual legislação você procura?
        <br><br>
        
        <form id="formLei" name="formLei" method="POST" action="visualizar.php">
            
              <select id="opcaoLei" name="opcaoLei" onChange="showDiv(this.value);">
                <option id="federal" name="federal" value="plano1">Federal</option>
                <option id="estadual" name="estadual" value="plano2">Estadual</option>
                <option id="municipal" name="municipal" value="plano3">Municipal</option>
            </select>
        
           
<select size="1" id="plano2" name="plano2" class="form-control" style="display: none;">
<option selected value="Estadual">Escolha o Estado</option>
<option id="acre" name="acre" value="Acre">Acre</option>
<option id="alagoas" name="alagoas" value="Alagoas">Alagoas</option>
<option id="amapa" name="amapa" value="Amapa">Amapá</option>
<option id="amazonas" name="amazonas" value="Amazonas">Amazonas</option>
<option id="bahia" name="bahia" value="Bahia">Bahia</option>
<option id="ceara" name="ceara" value="Ceara">Ceará</option>
<option id="distrito" name="distrito" value="Distrito Federal">Distrito Federal</option>
<option id="espirito" name="espirito" value="Espírito Santo">Espírito Santo</option>
<option id="goias" name="goias" value="Goias">Goiás</option>
<option id="maranhao" name="maranhao" value="Maranhao">Maranhão</option>
<option id="matogrosso" name="matogrosso" value="Mato Grosso">Mato Grosso</option>
<option id="matogrossosul" name="matogrossosul" value="Mato Grosso do Sul">Mato Grosso do Sul</option>
<option id="minas" name="minas" value="Minas Gerais">Minas Gerais</option>
<option id="para" name="para" value="Para">Pará</option>
<option id="paraiba" name="paraiba" value="Paraiba">Paraíba</option>
<option id="parana" name="parana" value="Parana">Paraná</option>
<option id="pernambuco" name="pernambuco" value="Pernambuco">Pernambuco</option>
<option id="piaui" name="piaui" value="Piaui">Piauí</option>
<option id="rio" name="rio" value="Rio de Janeiro">Rio de Janeiro</option>
<option id="norte" name="norte" value="Rio Grande do Norte">Rio Grande do Norte</option>
<option id="sul" name="sul" value="Rio Grande do Sul">Rio Grande do Sul</option>
<option id="rondonia" name="rondonia" value="Rondoia">Rondônia</option>
<option id="roraima" name="roraima" value="Roraima">Roraima</option>
<option id="santa" name="santa" value="Santa Catarina">Santa Catarina</option>
<option id="saopaulo" name="saopaulo" value="Sao Paulo">São Paulo</option>
<option id="sergipe" name="sergipe" value="Sergipe">Sergipe</option>
<option id="tocantins" name="tocantins" value="Tocantins">Tocantins</option>
<br><br>
</select>

        
<select size="1" id="plano3" name="plano3" class="form-control" style="display: none;">
<option selected value="Municipal">Escolha o Município</option>
<option id="aracaju" name="aracaju" value="Aracaju">Aracaju</option>
<option id="belem" name="belem" value="Belem">Belém</option>
<option id="belo" name="belo" value="Belo Horizonte">Belo Horizonte</option>
<option id="boavista" name="boavista" value="Boa Vista">Boa Vista</option>
<option id="brasilia" name="brasilia" value="Brasilia">Brasília</option>
<option id="campogrande" name="campogrande" value="Campo Grande">Campo Grande</option>
<option id="cuiaba" name="cuiaba" value="Cuiaba">Cuiabá</option>
<option id="curitiba" name="curitiba" value="Curitiba">Curitiba</option>
<option id="florianopolis" name="florianopolis" value="Florianopolis">Florianópolis</option>
<option id="fortaleza" name="fortaleza" value="Fortaleza">Fortaleza</option>
<option id="goiania" name="goiania" value="Goiania">Goiânia</option>
<option id="pessoa" name="pessoa" value="Joao Pessoa">João Pessoa</option>
<option id="macapa" name="macapa" value="Macapa">Macapá</option>
<option id="maceio" name="maceio" value="Maceio">Maceió</option>
<option id="manaus" name="manaus" value="Manaus">Manaus</option>
<option id="natal" name="natal" value="Natal">Natal</option>
<option id="palmas" name="palmas" value="Palmas">Palmas</option>
<option id="portoalegre" name="portoalegre" value="Porto Alegre">Porto Alegre</option>
<option id="portovelho" name="portovelho" value="Porto Velho">Porto Velho</option>
<option id="recife" name="recife" value="Recife">Recife</option>
<option id="riobranco" name="riobranco" value="Rio Branco">Rio Branco</option>
<option id="riodejaneiro" name="riodejaneiro" value="Rio de Janeiro">Rio de Janeiro</option>
<option id="salvador" name="salvador" value="Salvador">Salvador</option>
<option id="saoluis" name="saoluis" value="Sao Luis">São Luís</option>
<option id="saopaulocap" name="saopaulocap" value="Sao Paulo">São Paulo</option>
<option id="tersina" name="tersina" value="Teresina">Teresina</option>
<option id="vitoria" name="vitoria" value="Vitoria">Vitória</option>

</select>
    <input type="submit" name="avancar" value="Avançar" class="btn btn-lg btn-primary btn-block"><br><br>

        </form>

<br><br>
</body>
</center>
</html>

