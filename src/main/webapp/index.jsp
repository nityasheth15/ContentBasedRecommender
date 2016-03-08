<html>
<head>
	<jsp:directive.include file="header.jsp" />
    <style>
		p.dotted {border-style: dotted;}
		p.dashed {border-style: dashed;}
		p.solid {border-style: solid;}
		p.double {border-style: double;}
		p.groove {border-style: groove;}
		p.ridge {border-style: ridge;}
		p.inset {border-style: inset;}
		p.outset {border-style: outset; border-width: 2px}
		p.none {border-style: none;}
		p.hidden {border-style: hidden;}
		p.mix {border-style: dotted dashed solid double;}
		input.text_1{
    		height:60px;
   			width:600px;
    		word-break: break-word;
		}
		tr > td
		{
  			padding-bottom: 1em;
		}
</style>
</head>
<body onload="showTextParagraphs()">

<div>
<p>Please select your option:</p>
<form>
  <input type="radio" name="selectOption" onclick="javascript:selectedOptionCheck();" id="searchPost" value="searchPost"> Search Post<br>
  <input type="radio" name="selectOption" onclick="javascript:selectedOptionCheck();" id="searchYourQuery"  value="searchYourQuery"> Search Your Query<br>
</form>
 
</div>
<!-- <textarea id="txtArea_queryContent" form="frm_userQuery" rows="4" cols="80"></textarea><br> -->

<form action="ControlData" id="frm_userQuery">
<div id="txtArea_userQuery" style="visibility:hidden;">
<input class="text_1" type="text" name="text1"></input><br>
<input type="submit" name="search" value="Search" id="btn_search"/>
</div>
</form>

<form action="ControlData">
<div id="buttonDiv" style="visibility: hidden;">
<input type="submit" name="post" id="Post1" value="Post 1">
<input type="submit" name="post" id="Post2" value="Post 2">
<input type="submit" name="post" id="Post3" value="Post 3">
<input type="submit" name="post" id="Post4" value="Post 4">
<input type="submit" name="post" id="Post5" value="Post 5">
<input type="submit" name="post" id="Post6" value="Post 6">
<input type="submit" name="post" id="Post7" value="Post 7">
<input type="submit" name="post" id="Post8" value="Post 8">
<input type="submit" name="post" id="Post9" value="Post 9">
<input type="submit" name="post" id="Post10" value="Post 10">
</div>
<div id="tableDiv" style="visibility: hidden;">
<table style="width:100%">
<tr><td><p class="outset" id="selectedPost" ><%=request.getAttribute("recommendedPost0")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost1")%></p><td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost2")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost3")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost4")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost5")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost6")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost7")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost8")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost9")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost10")%></p></td></tr>
</table>
</div>
<div id=table2Div style="visibility: hidden">
<%-- <p class="outset" id="selectedPost" ><%=request.getAttribute("recommendedPost2_0")%></p> --%>
<table style="width:100%">
<tr><td><p class="outset" id="selectedPost" ><%=request.getAttribute("recommendedPost2_0")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost2_1")%></p><td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost2_2")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost2_3")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost2_4")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost2_5")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost2_6")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost2_7")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost2_8")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost2_9")%></p></td></tr>
<tr><td><p class="outset"><%=request.getAttribute("recommendedPost2_10")%></p></td></tr>
</table>
</div>
</form>

<script type="text/javascript">
function selectedOptionCheck() {
    if (document.getElementById('searchPost').checked) 
    {
    	
    	//document.getElementById('txtArea_userQuery').remove();
        document.getElementById('buttonDiv').style.visibility = 'visible';
        document.getElementById('txtArea_userQuery').style.visibility = 'hidden';
		document.getElementById('table2Div').style.visibility = 'hidden';
    } 
    else
	{
    	document.getElementById('buttonDiv').style.visibility = 'hidden';
    	document.getElementById('txtArea_userQuery').style.visibility = 'visible';
    	document.getElementById('tableDiv').style.visibility = 'hidden';
    }
}
 
function showTextParagraphs()
{	
		if(document.getElementsByTagName('p')[1].innerHTML == 'null')
			document.getElementById('tableDiv').style.visibility = 'hidden';
		else{
			//document.getElementById('txtArea_userQuery').remove();
			document.getElementById('searchPost').checked = true;
			document.getElementById('tableDiv').style.visibility = 'visible';
			document.getElementById('buttonDiv').style.visibility = 'visible';
		}
		
		if(document.getElementsByTagName('p')[12].innerHTML == 'null')
		{
			document.getElementById('table2Div').style.visibility = 'hidden';
		}
		else
		{
			document.getElementById('txtArea_userQuery').style.visibility = 'visible';	
			document.getElementById("tableDiv").remove();
			document.getElementById('searchYourQuery').checked = true;
			document.getElementById('table2Div').style.visibility = 'visible';
		}
		
			
}
</script>
</body>
</html>
