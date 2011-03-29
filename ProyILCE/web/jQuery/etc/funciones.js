//Libreria de funciones comunes y validaciones

function busca(s, nSitio)
{ if (s=='') 
	{alert('Escriba en el cuadro de texto una palabra relacionada al tema que busca');
	 return false; }
  else
	 window.location='busca.asp?kw=' + s + '&s=' + nSitio
}

function buscaX(s, nSitio)
{ if (s=='') 
	{alert('Escriba en el cuadro de texto una palabra relacionada al tema que busca');
	 return false; }
  else
	 window.location='buscax.asp?kw=' + s + '&s=' + nSitio
}	

<!--
function getkey(e)
{
if (window.event)
   return window.event.keyCode;
else if (e)
   return e.which;
else
   return null;
}
//-->

function openpop(sPagina, nWidth,nLenght) {
	window.open(sPagina,'saraPop',"location=0,status=0,scrollbars=yes,width=" + nWidth + ",height=" + nLenght);
}

function check_date(field){
var checkstr = "0123456789";
var DateField = field;
var Datevalue = "";
var DateTemp = "";
var seperator = "/";
var day;
var month;
var year;
var leap = 0;
var err = 0;
var i;
   err = 0;
   DateValue = DateField.value;
   /* Delete all chars except 0..9 */
   for (i = 0; i < DateValue.length; i++) {
	  if (checkstr.indexOf(DateValue.substr(i,1)) >= 0) {
	     DateTemp = DateTemp + DateValue.substr(i,1);
	  }
   }
   DateValue = DateTemp;
   /* Always change date to 8 digits - string*/
   /* if year is entered as 2-digit / always assume 20xx */
   if (DateValue.length == 6) {
      DateValue = DateValue.substr(0,4) + '20' + DateValue.substr(4,2); }
   if (DateValue.length != 8) {
      err = 19;}
   /* year is wrong if year = 0000 */
   year = DateValue.substr(4,4);
   if (year == 0) {
      err = 20;
   }
   /* Validation of month*/
   month = DateValue.substr(2,2);
   if ((month < 1) || (month > 12)) {
      err = 21;
   }
   /* Validation of day*/
   day = DateValue.substr(0,2);
   if (day < 1) {
     err = 22;
   }
   /* Validation leap-year / february / day */
   if ((year % 4 == 0) || (year % 100 == 0) || (year % 400 == 0)) {
      leap = 1;
   }
   if ((month == 2) && (leap == 1) && (day > 29)) {
      err = 23;
   }
   if ((month == 2) && (leap != 1) && (day > 28)) {
      err = 24;
   }
   /* Validation of other months */
   if ((day > 31) && ((month == "01") || (month == "03") || (month == "05") || (month == "07") || (month == "08") || (month == "10") || (month == "12"))) {
      err = 25;
   }
   if ((day > 30) && ((month == "04") || (month == "06") || (month == "09") || (month == "11"))) {
      err = 26;
   }
   /* if 00 ist entered, no error, deleting the entry */
   if ((day == 0) && (month == 0) && (year == 00)) {
      err = 0; day = ""; month = ""; year = ""; seperator = "";
   }
   /* if no error, write the completed date to Input-Field (e.g. 13.12.2001) */
   if (err == 0) {
      DateField.value = day + seperator + month + seperator + year;
  	  return true;
   }
   /* Error-message if err != 0 */
   else {
      alert("Fecha inválida, verifique");
      DateField.select();
	  DateField.focus();
	  return false;
   }
}

/*-------*/

function check_number(field,nRangoMenor,nRangoMayor) {
var numberField = parseInt(field.value);

  if (isNaN(numberField)&&field.value!=='') {
    alert('Valor inválido, se debe indicar un número, verifique.');
	field.value='';	
    field.select();
	field.focus();
	return false;
	}
  else
  { if (nRangoMayor==0&&nRangoMenor==0)
  		return true;
  	else 
		{ if (numberField>nRangoMayor) {
			alert('El número indicado es mayor a ' + nRangoMayor +',verifique'); 
		    field.select();
			field.focus();	
			return false;}
		  else { if (numberField<nRangoMenor) {
 				alert('El número indicado es menor a ' + nRangoMenor +',verifique'); 
			   field.select();
				field.focus();
				return false	}
				else return true;
				}	
  		}
	}	
  return true;	
}

function expand(s)
{
  var td = s;
  td.className = "menuHover";
  
  var d = td.getElementsByTagName("div").item(0);	
  if (d!=null) {d.className = "menuHover"}
  
  var a = td.getElementsByTagName("a").item(0);
  if (a!=null) {a.className = "menuHover";}
}

function collapse(s)
{
  var td = s;
  var d = td.getElementsByTagName("div").item(0);
  td.className = "menuNormal";
  var a = td.getElementsByTagName("a").item(0);	
  if (a!=null) {
    a.className = "menu_superior2";  }
  if (d!=null) {d.className = "menuNormal"}
}

function expandX(s,menuPos)
{
  var td = s;
  td.className = "menuHover"+ menuPos;
  
  var d = td.getElementsByTagName("div").item(0);	
  if (d!=null) {d.className = "menuHover" + menuPos}
  
  var a = td.getElementsByTagName("a").item(0);
  if (a!=null) {a.className = "menuHover" + menuPos;}
}

function collapseX(s, menuPos)
{
  var td = s;
  var d = td.getElementsByTagName("div").item(0);
  td.className = "menuNormal"+ menuPos;
  var a = td.getElementsByTagName("a").item(0);	
  if (a!=null) {
    a.className = "menu_superior" + menuPos;  }
  if (d!=null) {d.className = "menuNormal" + menuPos}
}

function setCookie(name, value, expires, path, domain, secure) {
  var curCookie = name + "=" + escape(value) +
      ((expires) ? "; expires=" + expires.toGMTString() : "") +
      ((path) ? "; path=" + path : "") +
      ((domain) ? "; domain=" + domain : "") +
      ((secure) ? "; secure" : "");
  document.cookie = curCookie;
}


/*
  name - name of the desired cookie
  return string containing value of specified cookie or null
  if cookie does not exist
*/

function getCookie(name) {
  var dc = document.cookie;
  var prefix = name + "=";
  var begin = dc.indexOf("; " + prefix);
  if (begin == -1) {
    begin = dc.indexOf(prefix);
    if (begin != 0) return null;
  } else
    begin += 2;
  var end = document.cookie.indexOf(";", begin);
  if (end == -1)
    end = dc.length;
  return unescape(dc.substring(begin + prefix.length, end));
}

function right(e) {
if (navigator.appName == 'Netscape' && (e.which == 3 || e.which == 2))
return false;
else if (navigator.appName == 'Microsoft Internet Explorer' && (event.button == 2 || event.button == 3)) {
	alert("Dirección de investigación y Contenidos Educativos (c)");
	return false;
	}
return true;
}

function setPref(parametro, valor) { 
	if (window.ActiveXObject) // for IE 
	{ 
		var httpRequest = new ActiveXObject("Microsoft.XMLHTTP"); 
		var objDOM=new ActiveXObject("Microsoft.XMLDOM");
		//objDOM.async=false;
	} 
	else if (window.XMLHttpRequest) // for other browsers 
	{ 
		var httpRequest = new XMLHttpRequest(); 
		var objDOM=document.implementation.createDocument("","",null) 
	} 
	
	httpRequest.open('GET','setpref.asp?parametro=' + parametro + '&valor=' + valor, true); 
	httpRequest.onreadystatechange = function() {
		if (httpRequest.readyState == 4) {
		 if(httpRequest.status == 200) { 
			 if (httpRequest.responseText!='ok') {
				 alert("Error al establecer parámetro");
				 return;
				}
			} 
		else
			{ 
				alert("Error cargando pagina setpref.asp\n"+ httpRequest.status +":"+ httpRequest.statusText); 
				return
			} 
		}
	} ; 
	httpRequest.send(null); 	
}

function getPref(parametro) {
	if (window.ActiveXObject) // for IE 
	{ 
		var httpRequest = new ActiveXObject("Microsoft.XMLHTTP"); 
		var objDOM=new ActiveXObject("Microsoft.XMLDOM");
		//objDOM.async=false;
	} 
	else if (window.XMLHttpRequest) // for other browsers 
	{ 
		var httpRequest = new XMLHttpRequest(); 
		var objDOM=document.implementation.createDocument("","",null) 
	} 

	httpRequest.open('GET','getpref.asp?parametro=' + parametro, false); 
	httpRequest.send(null); 
	var Doc = httpRequest.responseText;
	return Doc;			
}

function getRS(s) {
//Recupera un archivo XML a partir de una consulta a la base de datos
if (window.ActiveXObject) // for IE 
	{ 
		var httpRequest = new ActiveXObject("Microsoft.XMLHTTP"); 
		var objDOM=new ActiveXObject("Microsoft.XMLDOM");
		objDOM.async=false;
	} 
	else if (window.XMLHttpRequest) // for other browsers 
	{ 
		var httpRequest = new XMLHttpRequest(); 
		var objDOM=document.implementation.createDocument("","",null) 
	} 	

	httpRequest.open('GET','http://investigacion.ilce.edu.mx/panel_control/getXML.asp?q=' + s, false); 
	httpRequest.send(null); 
	return httpRequest.responseText;
	
}

/*function getRS2(s) {
var xmldoc=new ActiveXObject("MSXML2.DOMDocument.3.0");
xmldoc.load("getXML.asp?q=" + s);
return  xmldoc.documentElement.xlm
}*/

function setXMLInSelect2(sSelect, sTabla, sCampo,sWhere) {
if (window.ActiveXObject) // for IE 
	{ 
		var objDOM=new ActiveXObject("Microsoft.XMLDOM");
	} 
	else if (window.XMLHttpRequest) // for other browsers 
	{ 
		var objDOM=document.implementation.createDocument("","",null) 
	} 	

	objDOM.async=false;
	var s='http://investigacion.ilce.edu.mx/panel_control/getXML2.asp?t=' + sTabla + '&c=' + sCampo + '&w=' + sWhere
	objDOM.load(s);
	var x = objDOM.getElementsByTagName('row');

	//Llena una lista a partir de un archivo XML
	//Obtiene el nombre del select y lo transforma en objeto
	var aSelect=sSelect.split(",");
	for (a=0;a<aSelect.length;a++) {
		
		var oSelect=document.getElementById(aSelect[a]);
		//var oSelect=eval('window.document.form1.' + aSelect[a]); 
		
		if (oSelect!=null) {

		//Elimina los elementos de la lista
		oSelect.options.length=0
		
		/*for (m=oSelect.options.length-1;m>0;m--) {
			oSelect.options[m]=null
		}*/
		
		//Carga en un objeto el XML
		//Siempre deja la primera opción vacía
		var addOption = new Option('',''); 
		oSelect.options[oSelect.length] = addOption;
		for (i=0;i<x.length;i++) {
			 addOption = new Option(x[i].childNodes[1].childNodes[0].nodeValue,x[i].childNodes[0].childNodes[0].nodeValue); 
			 oSelect.options[oSelect.length] = addOption;
			}
	  	} 
	}
}

function setXMLInSelect(sSelect, q) {
if (window.ActiveXObject) // for IE 
	{ 
		var objDOM=new ActiveXObject("Microsoft.XMLDOM");
	} 
	else if (window.XMLHttpRequest) // for other browsers 
	{ 
		var objDOM=document.implementation.createDocument("","",null) 
	} 	

	objDOM.async=false;
	var s='panel_control/getXML.asp?q=' + q
	objDOM.load(s);
	var x = objDOM.getElementsByTagName('row');

	//Llena una lista a partir de un archivo XML
	//Obtiene el nombre del select y lo transforma en objeto
	var aSelect=sSelect.split(",");
	for (a=0;a<aSelect.length;a++) {
		
		var oSelect=document.getElementById(aSelect[a]);
		//var oSelect=eval('window.document.form1.' + aSelect[a]); 
		
		if (oSelect!=null) {

		//Elimina los elementos de la lista
		oSelect.options.length=0
		
		/*for (m=oSelect.options.length-1;m>0;m--) {
			oSelect.options[m]=null
		}*/
		
		//Carga en un objeto el XML
		//Siempre deja la primera opción vacía
		var addOption = new Option('',''); 
		oSelect.options[oSelect.length] = addOption;
		for (i=0;i<x.length;i++) {
			 addOption = new Option(x[i].childNodes[1].childNodes[0].nodeValue,x[i].childNodes[0].childNodes[0].nodeValue); 
			 oSelect.options[oSelect.length] = addOption;
			}
	  	} 
	}
}


function recupera_empleado() 
{
	var nEmpleado=document.getElementById('pregunta1346').value;
	if (nEmpleado=='' || nEmpleado==null) {
		alert('Escriba el número de empleado');
		return
	}

	if (window.ActiveXObject) // for IE 
		{ 
			var objDOM=new ActiveXObject("Microsoft.XMLDOM");
		} 
		else if (window.XMLHttpRequest) // for other browsers 
		{ 
			var objDOM=document.implementation.createDocument("","",null) 
		} 	

		objDOM.async=false;
		/*
		httpRequest.open('GET','getXML.asp?q=select * from jornada_nomina where clave_empleado=' + nEmpleado, false); 
		httpRequest.send(null); */
		
				//Verifica si el empleado fue anteriormente capturado
		s = "select count(clave_respuesta) as num_respuestas from respuesta_alumno where clave_cuestionario=175 AND email='" + nEmpleado + "'"
		objDOM.load('http://investigacion.ilce.edu.mx/panel_control/getXML.asp?q=' + s)
		x = objDOM.getElementsByTagName('row');

		if (parseInt(x[0].childNodes[0].childNodes[0].nodeValue) >0) {
			//setCookie('id',nEmpleado);
			//window.location.reload();
			window.location='cc.asp?id='+nEmpleado;
			return;
		}	
			
		var s='select jn.*, jp.puesto, r.clave_respuesta from jornada_nomina jn, jornada_puesto jp, respuesta r where jn.clave_departamento=int(r.codigo_respuesta) AND jn.clave_puesto=jp.clave_puesto AND r.clave_pregunta=1348 AND jn.clave_empleado=' + nEmpleado
		//alert(getRS(s));
		objDOM.load('http://investigacion.ilce.edu.mx/panel_control/getXML.asp?q=' + s)
	
		//var x = httpRequest.responseXML.getElementsByTagName('row');
		var x = objDOM.getElementsByTagName('row');
		if (x.length==0) {
			alert('No se encontró el empleado señalado');
			return
		}
		
		//Nombre del empleado
		document.getElementById('pregunta1347').value=x[0].childNodes[1].childNodes[0].nodeValue;
		
		//clave de departamento
		var dpt=document.getElementById('pregunta1348')
		for(i=0;i<dpt.options.length;i++) {
			if (dpt.options[i].value==x[0].childNodes[9].childNodes[0].nodeValue) {
				dpt.selectedIndex=i;
				break; }
		}

		//clave puesto
		document.getElementById('pregunta1353').value=x[0].childNodes[8].childNodes[0].nodeValue

		//fecha ingreso
		document.getElementById('pregunta1351').value=x[0].childNodes[4].childNodes[0].nodeValue;

		//antigüedad
		var fecha_ingreso = new Date(x[0].childNodes[4].childNodes[0].nodeValue)
		var hoy = new Date();

	    var diferencia = hoy.getTime() - fecha_ingreso.getTime();
   		var dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));   
		var years = Math.floor(dias/365)
   		var segundos = Math.floor(diferencia / 1000) ;
   
		document.getElementById('pregunta1352').value=years;
		
		//Salario integral
		document.getElementById('pregunta1355').value=x[0].childNodes[7].childNodes[0].nodeValue;
		
		//Llena puesto inmediato a quien reporta (puestos del mismo depto ( pregunta 1356)
		//s="SELECT clave_respuesta, respuesta FROM respuesta WHERE clave_pregunta=1356 AND INT(codigo_respuesta) IN " + 
		//  "(SELECT jp.clave_puesto FROM respuesta r, jornada_puesto jp WHERE int(r.codigo_respuesta)=jp.clave_departamento " +
		//  "AND r.clave_respuesta="+ x[0].attributes[6].nodeValue + ")" */

		//setXMLInSelect('pregunta1356', s);
		
		//establece cookie para llenar otros  campos
		setCookie('nDepto',x[0].childNodes[2].childNodes[0].nodeValue);
		//establece ID como el numero de empleado
		setCookie('id',nEmpleado);
		
}

function fillSelects() {
	
	nPuesto=getCookie('nDepto');
	alert(nPuesto);
	//Puesto que le reporta 1
	var s="SELECT clave_respuesta, respuesta FROM respuesta WHERE clave_pregunta=1358 AND INT(codigo_respuesta) IN " + 
		  "(SELECT jp.clave_puesto FROM respuesta r, jornada_puesto jp WHERE int(r.codigo_respuesta)=jp.clave_departamento " +
		  "AND r.clave_respuesta="+ nPuesto + ")"
	setXMLInSelect('pregunta1358',s)
	
	//Puesto que le reporta 2
	s="SELECT clave_respuesta, respuesta FROM respuesta WHERE clave_pregunta=1360 AND INT(codigo_respuesta) IN " + 
		  "(SELECT jp.clave_puesto FROM respuesta r, jornada_puesto jp WHERE int(r.codigo_respuesta)=jp.clave_departamento " +
		  "AND r.clave_respuesta="+ nPuesto + ")"
	setXMLInSelect('pregunta1360',s)

	//Puesto que le reporta 3	
	s="SELECT clave_respuesta, respuesta FROM respuesta WHERE clave_pregunta=1362 AND INT(codigo_respuesta) IN " + 
	  "(SELECT jp.clave_puesto FROM respuesta r, jornada_puesto jp WHERE int(r.codigo_respuesta)=jp.clave_departamento " +
	  "AND r.clave_respuesta="+ nPuesto+ ")"
	setXMLInSelect('pregunta1362',s)
	
	//Puesto que le reporta 4	
	s="SELECT clave_respuesta, respuesta FROM respuesta WHERE clave_pregunta=1364 AND INT(codigo_respuesta) IN " + 
	  "(SELECT jp.clave_puesto FROM respuesta r, jornada_puesto jp WHERE int(r.codigo_respuesta)=jp.clave_departamento " +
	  "AND r.clave_respuesta="+ nPuesto + ")"
	setXMLInSelect('pregunta1364',s)
	
	//Puesto que le reporta 5
	s="SELECT clave_respuesta, respuesta FROM respuesta WHERE clave_pregunta=1374 AND INT(codigo_respuesta) IN " + 
	  "(SELECT jp.clave_puesto FROM respuesta r, jornada_puesto jp WHERE int(r.codigo_respuesta)=jp.clave_departamento " +
	  "AND r.clave_respuesta="+ nPuesto + ")"
	setXMLInSelect('pregunta1374',s)
}

function seldepto(inputNumber)  {
  var myValues = new Object();
  var sResultado=showModalDialog("selecciona_departamento.asp",myValues, "resizable: yes; help: no; status: no; scroll: yes; ");
  var aResultado=sResultado.split(',');
  document.getElementById('hidden' + inputNumber).value=aResultado[0];
  document.getElementById('pregunta' + inputNumber).value=aResultado[1];
}


function selpuesto(hiddenInputNumber, inputNumber)  {
  var myValues = new Object();  	
  if (document.getElementById('hidden' + hiddenInputNumber)!=null) {
  var sResultado=showModalDialog("selecciona_puesto.asp?depto=" + document.getElementById('hidden' + hiddenInputNumber).value,myValues, "resizable: yes; help: no; status: no; scroll: yes; ") }
  else {
  var sResultado=showModalDialog("selecciona_puesto.asp",myValues, "resizable: yes; help: no; status: no; scroll: yes; ") }	
   
  document.getElementById('pregunta' + inputNumber).value=sResultado;
}


function emailCheck (emailStr) {
/* Verificar si el email tiene el formato user@dominio. */
var emailPat=/^(.+)@(.+)$/ 

/* Verificar la existencia de caracteres. ( ) < > @ , ; : \ " . [ ] */
var specialChars="\\(\\)<>@,;:\\\\\\\"\\.\\[\\]" 

/* Verifica los caracteres que son válidos en una dirección de email */
var validChars="\[^\\s" + specialChars + "\]" 

var quotedUser="(\"[^\"]*\")" 

/* Verifica si la dirección de email está representada con una dirección IP Válida */ 
var ipDomainPat=/^\[(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})\]$/


/* Verificar caracteres inválidos */ 
var atom=validChars + '+'
var word="(" + atom + "|" + quotedUser + ")"
var userPat=new RegExp("^" + word + "(\\." + word + ")*$")
/*domain, as opposed to ipDomainPat, shown above. */
var domainPat=new RegExp("^" + atom + "(\\." + atom +")*$")


var matchArray=emailStr.match(emailPat)
if (matchArray==null) {
alert("El correo parece ser incorrecto (Verifique el @ y los .)")
return false
}
var user=matchArray[1]
var domain=matchArray[2]

// Si el user "user" es valido 
if (user.match(userPat)==null) {
// Si no
alert("El nombre de usuario no es válido.")
return false
}

/* Si la dirección IP es válida */
var IPArray=domain.match(ipDomainPat)
if (IPArray!=null) {
for (var i=1;i<=4;i++) {
if (IPArray[i]>255) {
alert("IP de destino inválida")
return false
}
}
return true
}

var domainArray=domain.match(domainPat)
if (domainArray==null) {
alert("El dominio parece no ser válido.")
return false
}

var atomPat=new RegExp(atom,"g")
var domArr=domain.match(atomPat)
var len=domArr.length
if (domArr[domArr.length-1].length<2 || 
domArr[domArr.length-1].length>3) { 

alert("La dirección debe tener 3 letras si es .'com' o 2 si en de algún pais.")
return false
}

if (len<2) {
var errStr="La dirección es erronea"
alert(errStr)
return false
}

// La dirección de email ingresada es Válida
return true;
}
// End -->

//funcion utilizada en la pagina principal del CEDAL
function suscribe() {
	var sEmail=document.getElementById('emailsuscriptor').value;
	var sNombre=document.getElementById('nombresuscriptor').value;
	var sInstitucion=document.getElementById('institucionsuscriptor').value;
	
	if (!emailCheck(sEmail)) {
		return false; }
	else {
		document.getElementById('emailsuscriptor').innerHTML=setSuscripcion(sEmail,sNombre,sInstitucion);
	}
}


function LimpiaSuscripcion() {
	document.getElementById('emailsuscriptor').value='';
	document.getElementById('nombresuscriptor').value='';
	document.getElementById('institucionsuscriptor').value='';
}

function setSuscripcion(sEmail, sNombre, sInstitucion) {
//Recupera un archivo XML a partir de una consulta a la base de datos
if (window.ActiveXObject) // for IE 
	{ 
		var httpRequest = new ActiveXObject("Microsoft.XMLHTTP"); 
		var objDOM=new ActiveXObject("Microsoft.XMLDOM");
		objDOM.async=false;
	} 
	else if (window.XMLHttpRequest) // for other browsers 
	{ 
		var httpRequest = new XMLHttpRequest(); 
		var objDOM=document.implementation.createDocument("","",null) 
	} 	

	httpRequest.open('GET','http://cedal.ilce.edu.mx/panel_control/setSuscripcion.asp?email=' + sEmail + '&nombre=' + sNombre + '&Institucion=' + sInstitucion, false);
	
	httpRequest.send(null); 
	if (httpRequest.responseText=='ok') {
		alert('Se recibieron correctamente sus datos');
		LimpiaSuscripcion(); }
	else {
		alert('Error al recibir información, favor de intentar de nuevo'); }
	
	
}


function bsqdEdusat1 (sKW) {
var KeyWord =sKW

if (KeyWord=='') {
	alert('No se definió palabra de búsqueda, verifique')
	return}

document.getElementById('resultados').innerHTML = "<div style='position:relative;width:200px; height:90px; z-index:1;left:30%; top: 35%;border:none'><p align='center' style='font:Verdana, Arial, Helvetica, sans-serif; font-size:12px; width:100%;'><br />Cargando informaci&oacute;n... <br /> <br /><img src='http://investigacion.ilce.edu.mx/panel_control/wait30.gif' /></p></div>";

if (window.ActiveXObject) // for IE 
	{ 
		var httpRequest = new ActiveXObject("Microsoft.XMLHTTP"); 
		var objDOM=new ActiveXObject("Microsoft.XMLDOM");
		objDOM.async=false;
	} 
	else if (window.XMLHttpRequest) // for other browsers 
	{ 
		var httpRequest = new XMLHttpRequest(); 
		var objDOM=document.implementation.createDocument("","",null) 
	} 	
	httpRequest.open('GET','/panel_control/busqueda_sencilla_edusat.asp?kw=' + KeyWord);
	httpRequest.onreadystatechange = function() {
		if (httpRequest.readyState == 4) {
		 if(httpRequest.status == 200) { 
				document.getElementById('resultados').innerHTML=httpRequest.responseText;
			} 
		else
			{ 
				alert("Error cargando pagina /busqueda_sencilla_edusat.asp\n"+ httpRequest.status +":"+ httpRequest.statusText); 
				return
			} 
		}
	} ; 
	httpRequest.send(null); 	
}

function bsqdEdusat2 (sKW) {
var KeyWord =sKW

if (KeyWord=='') {
	alert('No se definieron criterios de búsqueda, verifique');
 	return;
}

document.getElementById('resultados').innerHTML = "<div style='position:relative;width:200px; height:90px; z-index:1;left:30%; top: 35%;border:none'><p align='center' style='font:Verdana, Arial, Helvetica, sans-serif; font-size:12px; width:100%;'><br />Cargando informaci&oacute;n... <br /> <br /><img src='http://investigacion.ilce.edu.mx/panel_control/wait30.gif' /></p></div>";

if (window.ActiveXObject) // for IE 
	{ 
		var httpRequest = new ActiveXObject("Microsoft.XMLHTTP"); 
		var objDOM=new ActiveXObject("Microsoft.XMLDOM");
		objDOM.async=false;
	} 
	else if (window.XMLHttpRequest) // for other browsers 
	{ 
		var httpRequest = new XMLHttpRequest(); 
		var objDOM=document.implementation.createDocument("","",null) 
	} 	
	//window.open ('http://investigacion.ilce.edu.mx/panel_control/busqueda_avanzada_investigacion.asp?' + KeyWord, '_blank');
	httpRequest.open('GET','/panel_control/busqueda_avanzada_edusat.asp?' + KeyWord);
	httpRequest.onreadystatechange = function() {
		if (httpRequest.readyState == 4) {
		 if(httpRequest.status == 200) { 
				document.getElementById('resultados').innerHTML=httpRequest.responseText;
			} 
		else
			{ 
				alert("Error cargando pagina /busqueda_avanzada_edusat.asp\n"+ httpRequest.status +":"+ httpRequest.statusText); 
				return
			} 
		}
	} ; 
	httpRequest.send(null); 
	
}

function detalle_programa(sPrograma, sSinopsis,sCanal,sFecha,sHora) {
document.getElementById('divDetalle').className='div_detalle';
document.getElementById('tituloprograma').innerHTML=sPrograma;
document.getElementById('sinopsis').innerHTML=sSinopsis;
document.getElementById('canal').innerHTML=sCanal;
document.getElementById('fecha').innerHTML=sFecha;
document.getElementById('hora').innerHTML=sHora;
}

function lo_mas_visitado(nSitio,sDiv) {
	if (window.ActiveXObject) // for IE 
	{ 
		var httpRequest = new ActiveXObject("Microsoft.XMLHTTP"); 
		var objDOM=new ActiveXObject("Microsoft.XMLDOM");
		objDOM.async=false;
	} 
	else if (window.XMLHttpRequest) // for other browsers 
	{ 
		var httpRequest = new XMLHttpRequest(); 
		var objDOM=document.implementation.createDocument("","",null) 
	} 	

	httpRequest.open('GET','/panel_control/lo_mas_visitado.asp?s=' + nSitio);
	httpRequest.onreadystatechange = function() {
		if (httpRequest.readyState == 4) {
		 if(httpRequest.status == 200) { 
				document.getElementById(sDiv).innerHTML=httpRequest.responseText;
				return;
			} 
		else
			{ 
				document.getElementById(sDiv).innerHTML= "Error al cargar Lo más visitado: " + httpRequest.status +":"+ httpRequest.statusText; 
				return;
			} 
		}
	} ; 
	httpRequest.send(null);
}

function setScrollingCookie(nItem) {
	setCookie('scrollingCookie',nItem);
	window.open('investigacion.asp?id=2534','_self')
}

function sDate(dDate) {
	var nDay=dDate.getDate();
	var nMonth=dDate.getMonth()+1;
	var nYear=dDate.getFullYear();
	var sDate="";

	sDate= nYear;
	if (nMonth<10) {
		sDate= sDate + '/0' + nMonth; }
	else {
		sDate= sDate + '/' + nMonth;  }

	if (nDay<10) {
		sDate=sDate + '/0' + nDay; }
	else {
		sDate=sDate + '/' + nDay; }

	return sDate
}

function removeHTMLTags(s){
 		var strInputCode = s;
 		/* 
  			This line is optional, it replaces escaped brackets with real ones, 
  			i.e. < is replaced with < and > is replaced with >
 		*/	
 	 	strInputCode = strInputCode.replace(/&(lt|gt);/g, function (strMatch, p1){
 		 	return (p1 == "lt")? "<" : ">";
 		});
 		var strTagStrippedText = strInputCode.replace(/<\/?[^>]+(>|$)/g, "");
 		return strTagStrippedText;
   // Use the alert below if you want to show the input and the output text
   //		alert("Input code:\n" + strInputCode + "\n\nOutput text:\n" + strTagStrippedText);	

}

function editField(nKernelId, sFieldName, nPKValue) {
if (window.ActiveXObject) // for IE 
{ 
	var oXML=new ActiveXObject("Microsoft.XMLDOM");
	//objDOM.async=false;
} 
else if (window.XMLHttpRequest) // for other browsers 
{ 
	var oXML=document.implementation.createDocument("","",null) 
} 

oXML.async=false;
var s='getEditField.asp?k=' + nKernelId + '&fn=' + sFieldName + '&pkv=' + nPKValue
oXML.load(s);

var oDiv=document.getElementById('categoria_'+ nPKValue);
//Verifica el tipo de control

var sControl= oXML.getElementsByTagName("f")[0].childNodes[0].nodeName;

// Si el DIV no contiene un control del mismo tipo
if (oDiv.getElementsByTagName(sControl).length==0) {
	var oControl=oDiv.appendChild(document.createElement(sControl));
	var i=0;
	for (i=0; i<oXML.getElementsByTagName(sControl)[0].attributes.length; i++) {
		oControl.setAttribute(oXML.getElementsByTagName(sControl)[0].attributes[i].nodeName,oXML.getElementsByTagName(sControl)[0].attributes[i].nodeValue ); 
	}	
	
	var oWaitDiv=oDiv.appendChild(document.createElement("<div>"));
	oWaitDiv.setAttribute("id","waitDiv");
	oDiv.appendChild(oWaitDiv);
											
	oControl.onblur = function (evt) { 
		document.getElementById("waitDiv").innerHTML="<img src='http://investigacion.ilce.edu.mx/panel_control/wait16trans.gif' />";
		saveField(nKernelId, sFieldName, this.value, nPKValue); 
		document.getElementById("waitDiv").innerHTML="";
		var oDiv=document.getElementById('categoria_'+ nPKValue);
		var oText=oDiv.appendChild(document.createTextNode(this.value));	
		oDiv.replaceChild(oText, oDiv.firstChild);
		};
	oDiv.replaceChild(oControl, oDiv.firstChild);
	oControl.focus();
	}
}


function saveField (nKernelId, sFieldName, sFieldValue, nPKValue) {
		if (window.ActiveXObject) // for IE 
	{ 
		var httpRequest = new ActiveXObject("Microsoft.XMLHTTP"); 
		//objDOM.async=false;
	} 
	else if (window.XMLHttpRequest) // for other browsers 
	{ 
		var httpRequest = new XMLHttpRequest(); 
	} 

	httpRequest.open('GET','setEditField.asp?k=' + nKernelId + '&fn=' + sFieldName + '&fv=' + sFieldValue + '&pkv=' + nPKValue, false); 
	httpRequest.send(null);
}