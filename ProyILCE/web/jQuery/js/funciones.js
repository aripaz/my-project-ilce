//Libreria de funciones comunes y validaciones
function getkey(e)
{
if (window.event)
   return window.event.keyCode;
else if (e)
   return e.which;
else
   return null;
}

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
      alert("Fecha no válida, verifique");
      DateField.value="";
      DateField.select();
      DateField.focus();
      return false;
   }
}

/*-------*/

function check_number(field,nRangoMenor,nRangoMayor) {
var numberField = field.value;// parseInt(field.value);

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
			alert('El número indicado es mayor a ' + nRangoMayor +', verifique');
		    field.select();
			field.focus();	
			return false;}
		  else { if (numberField<nRangoMenor) {
 				alert('El número indicado es menor a ' + nRangoMenor +', verifique');
			   field.select();
				field.focus();
				return false	}
				else return true;
				}	
  		}
	}	
  return true;	
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
	alert("Direcci�n de investigaci�n y Contenidos Educativos (c)");
	return false;
	}
return true;
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
		//Siempre deja la primera opci�n vac�a
		var addOption = new Option('',''); 
		oSelect.options[oSelect.length] = addOption;
		for (i=0;i<x.length;i++) {
			 addOption = new Option(x[i].childNodes[1].childNodes[0].nodeValue,x[i].childNodes[0].childNodes[0].nodeValue); 
			 oSelect.options[oSelect.length] = addOption;
			}
	  	} 
	}
}

function setXMLInSelect3(sSelect,cf,ta,pk,w) {
var httpRequest;
if (window.ActiveXObject) // for IE 
    httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
else if (window.XMLHttpRequest) // for other browsers 
    httpRequest = new XMLHttpRequest();

var s="";
if (w==undefined)
    w="";

if (pk!=null)
    s="srvFormaSearch?$cf="+cf+"&$ta="+ta+"&$pk="+pk+"&$w="+w
else
    s="srvFormaSearch?$cf="+cf+"&$ta="+ta+"&$w="+w

httpRequest.open('GET',s, false);
httpRequest.send(null);

var x = httpRequest.responseXML.getElementsByTagName('registro');
var error=httpRequest.responseXML.getElementsByTagName('error');

if (error.length>0) {
    alert("Ha perdido la conexión, intente otra vez");
    return
}

//Llena una lista a partir de un archivo XML
//Obtiene el nombre del select y lo transforma en objeto
		
var oSelect=document.getElementById(sSelect);
//var oSelect=eval('window.document.form1.' + aSelect[a]);

if (oSelect!=null) {

//Elimina los elementos de la lista
oSelect.options.length=0

//Carga en un objeto el XML
//Siempre deja la primera opcion vacia
var addOption = new Option('','');
oSelect.options[oSelect.length] = addOption;
for (i=0;i<x.length;i++) {
        /*if (pk!=null)
            addOption = new Option(x[i].childNodes[1].childNodes[0].nodeValue,x[i].childNodes[1].childNodes[0].nodeValue);
        else*/
        var display=x[i].childNodes[3].childNodes[0].nodeValue.replace(/&aacute;/g,"á").replace(/&eacute;/g,"é").replace(/&iacute;/g,"í").replace(/&oacute;/g,"ó").replace(/&uacute;/g,"ú").replace(/&ntilde;/g,"ñ");
        addOption = new Option(display,x[i].childNodes[1].childNodes[0].nodeValue);
        oSelect.options[oSelect.length] = addOption;
        }
}
}

function emailCheck (emailStr) {
/* Verificar si el email tiene el formato user@dominio. */
var emailPat=/^(.+)@(.+)$/ 

/* Verificar la existencia de caracteres. ( ) < > @ , ; : \ " . [ ] */
var specialChars="\\(\\)<>@,;:\\\\\\\"\\.\\[\\]" 

/* Verifica los caracteres que son v�lidos en una direcci�n de email */
var validChars="\[^\\s" + specialChars + "\]" 

var quotedUser="(\"[^\"]*\")" 

/* Verifica si la direcci�n de email est� representada con una direcci�n IP V�lida */ 
var ipDomainPat=/^\[(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})\]$/


/* Verificar caracteres inv�lidos */ 
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

/* Si la direcci�n IP es v�lida */
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
if (domArr[domArr.length-1].length<2 || domArr[domArr.length-1].length>3) { 
    alert("La dirección debe tener 3 letras si es .'com' o 2 si en de algún país.")
    return false
}

if (len<2) {
var errStr="La dirección es erronea"
alert(errStr)
return false
}

// La direcci�n de email ingresada es V�lida
return true;
}
// End -->

function sDateToString(dDate) {
	var nDay=dDate.getDate();
	var nMonth=dDate.getMonth()+1;
	var nYear=dDate.getFullYear();
	var sDate="";


	if (nDay<10) {
		sDate=sDate + '0' + nDay; }
	else {
		sDate=sDate + nDay; }
        
        sDate+="/";
        
        if (nMonth<10) {
		sDate= sDate + '0' + nMonth; }
	else {
		sDate= sDate + nMonth;  }

        sDate+="/"+nYear;
        
	return sDate;
}

function sDate(dDate) {
	var nDay=dDate.getDate();
	var nMonth=dDate.getMonth()+1;
	var nYear=dDate.getFullYear();
	var sDate="";

	sDate= nYear;
	if (nMonth<10) {
		sDate= sDate + '0' + nMonth; }
	else {
		sDate= sDate + nMonth;  }

	if (nDay<10) {
		sDate=sDate + '0' + nDay; }
	else {
		sDate=sDate + nDay; }

	return sDate
}

function sDateTime(dDate) {
        var nSeconds=dDate.getSeconds();
        var nMinutes=dDate.getMinutes();
        var nHours=dDate.getHours();
	var nDay=dDate.getDate();
	var nMonth=dDate.getMonth()+1;
	var nYear=dDate.getFullYear();
	var sDate="";

	sDate= nYear;
	if (nMonth<10) {
		sDate= sDate + '0' + nMonth; }
	else {
		sDate= sDate + nMonth;  }

	if (nDay<10) {
		sDate=sDate + '0' + nDay; }
	else {
		sDate=sDate + nDay; }

        var timeValue = "" + ((nHours >12) ? nHours -12 :nHours)
        if (timeValue == "0") timeValue = 12;
        timeValue += ((nMinutes < 10) ? "0" : "") + nMinutes
        timeValue += ((nSeconds < 10) ? "0" : "") + nSeconds

	return sDate + timeValue;
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
		//Siempre deja la primera opci�n vac�a
		var addOption = new Option('','');
		oSelect.options[oSelect.length] = addOption;
		for (i=0;i<x.length;i++) {
			 addOption = new Option(x[i].childNodes[1].childNodes[0].nodeValue,x[i].childNodes[0].childNodes[0].nodeValue);
			 oSelect.options[oSelect.length] = addOption;
			}
	  	}
	}
}

function validaAnteproyectoXAutorizar (sControl,mustBe,sObligatorios) {
   /*
    *"proyecto,clave_area,clave_cliente,clave_categoria_proyecto,clave_factibilidad,"+
     "fecha_inicio_planeada,fecha_final_planeada,duracion_planeada,fecha_alta,"+
     "clave_estatus_proyecto,egreso_planeado,ingreso_planeados,clave_moneda,tipo_cambio"
    */

   oControl=$("#"+sControl);
   if (oControl.length==0) return false;
   aControl=sControl.split("_")
   Suffix=aControl[aControl.length-3]+"_"+aControl[aControl.length-2]+"_"+aControl[aControl.length-1];

   if (oControl.val()!=mustBe ) return true;

   bCompleto=true;
   aObligatorios=sObligatorios.split(",");
   oForm=$($("#"+sControl)[0].form);
   aToValidate=oForm.serialize().split("&");
   for (k=0; k<aObligatorios.length;k++) {
        for (i=0; i<aToValidate.length; i++) {
             if (aToValidate[i].indexOf(aObligatorios[k])==0) {
               sField=aToValidate[i].split("=")[0]
               sVal=aToValidate[i].split("=")[1]
               oField=$("#"+sField);
               if (sVal=="") {
                  $("#td_" + sField).addClass("errorencampo")
                  $("#"+sField).addClass("errorencampo");
                  bCompleto=false;
                  break;
               }
           }
       }  
   }

   if (!bCompleto) {
       $("#tdEstatus_" +Suffix).html("Falta(n) dato(s) para poder cambiar " + $("#td_"+sControl).html() + ", verifique");
       
       oControl.val("0");
       return false;
   }
   else {
        if (confirm("Después de guardar el anteproyecto con estatus por autorizar no será posible modificarlo, desea continuar?"))
            return true;
        else {
            oControl.val("0");
            return false;
        }
    }
}

function calcula_iva(sFormSuffix){ 
nSubtotal=parseFloat($("#importe_"+sFormSuffix).val());

if ($("#iva_"+sFormSuffix).val()=="")
    nIVA=0;
else
    nIVA=parseFloat($("#iva_"+sFormSuffix+" option:selected").html().split("-")[1])/100 ;

$("#total_"+sFormSuffix).val(nSubtotal+(nSubtotal*nIVA));
}

$("#cantidad,#precio_unitario,#iva").change( function() { 
var IVA = parseFloat($("#iva option:selected").html().split("-")[1])/100;
var total=parseFloat($("#cantidad").val())*parseFloat($("#precio_unitario").val())*(1+IVA/100);
$("#total").val(total);
});
function calcula_total(sFormSuffix){ 
nCantidad=parseFloat($("#cantidad_"+sFormSuffix).val());
nPrecio=parseFloat($("#precio_unitario_"+sFormSuffix).val());
$("#total_"+sFormSuffix).val(nCantidad*nPrecio);
}