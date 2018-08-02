<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentHeader", "domap");%>
<%pageContext.setAttribute("currentMenu", "domap");%>
<%pageContext.setAttribute("currentMenuId", "718");%>
<!doctype html>
<html>
<!-- 用户页面 -->
  <head>
    <%@include file="/common/meta.jsp"%>
    <title>编辑</title>
    <%@include file="/common/s3.jsp"%>
    <%-- <%request.getAttribute(roomId); %> --%>
    
   
    
    <script type="text/javascript">
    
$(function() {
    $("#conference-managementForm").validate({
        submitHandler: function(form) {
			bootbox.animate(false);
			var box = bootbox.dialog('<div class="progress progress-striped active" style="margin:0px;"><div class="bar" style="width: 100%;"></div></div>');
            form.submit();
        },
        errorClass: 'validate-error'
    });
})

$(function(){
	
	if($("#groupWeekAtr")!=""&&$("#groupWeekAtr")!=null){
		var groupWeek = $("#groupWeekAtr").val();
		var groupWeekArr = groupWeek.split(",");
		for(var i=0;i<groupWeekArr.length;i++ ){
			switch(groupWeekArr[i]){
			case "星期一":
			 $("#Monday").attr("checked","checked");
			break;
			case "星期二":
				 $("#Tuesday").attr("checked","checked");
				break;
			case "星期三":
				 $("#Wednesday").attr("checked","checked");
				break;
			case "星期四":
				 $("#Thursday").attr("checked","checked");
				break;
			case "星期五":
				 $("#Friday").attr("checked","checked");
				break;
			case "星期六":
				 $("#Saturday").attr("checked","checked");
				break;
			case "星期日":
				 $("#Sunday").attr("checked","checked");
				break;
			}
		}
	}
	
})
$(function(){
			$("#submitButton").click(function(){
				var start =$("#startTime").val();
				var startSplit=start.replace(":","");
				var end =$("#endTime").val();
				var endSplit=end.replace(":","");
				var over =$("#overTime").val();
				var overSplit=over.replace(":","");
				var startSum = parseInt(startSplit);
				var endSum = parseInt(endSplit);
				var overSum = parseInt(overSplit);
				if(endSum<=startSum){
					$("#endTime").val("");
					alert("下班时间应大于上班时间");
					return;
				}
				if(overSum<endSum){
					$("#overTime").val("");
					alert("加班时间大于等于下班时间,请重新输入或跳过");
					return;
					
				}
				$(this).parents("form").submit();
			})
		})
    </script>
 <style type="text/css">
		#l-map{height:400px;width:100%;}
		#r-result{width:100%;}
		 {position:relative;}
		#positionAbsolute{position:absolute;top:50px;right:30px; z-index: 99}
		.tangram-suggestion-main{
		z-index: 99999;}
</style>
<link type="text/css" rel="stylesheet" href="${tenantPrefix}/widgets/userpickerSuper/userpicker.css?jsEdition=${jsEdition}">
<script type="text/javascript" src="${tenantPrefix}/widgets/userpickerSuper/userpicker.js?jsEdition=${jsEdition}"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=R49vO9Mwb8tWpEcjq5amrWsAfcAnZ3NM"></script>

<script type="text/javascript">
	var userconf = {
			url: '${tenantPrefix}',
			dom: '.userPicker',
	};
	userPicker(userconf);
	
	
</script>

  </head>

  <body>
     <%@include file="/header/mainHeader.jsp" %>
      <div class="panel panel-default">
        <div class="panel-heading">
		  <i class="glyphicon glyphicon-list"></i>
		  编辑
		</div>

		<div class="panel-body">
<c:if test="${empty groupId}">
<form id="conference-managementForm" method="post" action="map-mapGroup-save.do" class="form-horizontal">
</c:if>
<c:if test="${!empty groupId}">
<form id="conference-managementForm" method="post" action="map-mapGroup-update.do" class="form-horizontal">
</c:if>
   	<div class="form-group">
    <label class="control-label col-md-1" for="conference-info_address">考勤组名称</label>
	<div class="col-sm-5">
	<input type="text" name="groupName" class="form-control required" value="${groupName}" maxlength="30">
	<input type="hidden" name="groupId"  value="${groupId}" >
	<input type="hidden" name="groupConter" id="groupConter" value="${groupConter}" >
	<input type="hidden"  id="groupWeekAtr" value="${groupWeek}" >
	
    </div>
    </div>
    
    
    <div class="form-group">
    <label class="control-label col-md-1" for="conference-info_address">考勤日期</label>
	<div class="col-sm-5">
	<label><input type="checkbox" name="groupWeek" id="Monday" value="星期一" >星期一</label>
	<label><input type="checkbox" name="groupWeek" id="Tuesday" value="星期二" >星期二</label>
	<label><input type="checkbox" name="groupWeek" id="Wednesday" value="星期三" >星期三</label>
	<label><input type="checkbox" name="groupWeek" id="Thursday" value="星期四" >星期四</label>
	<label><input type="checkbox" name="groupWeek" id="Friday" value="星期五" >星期五</label>
	<label><input type="checkbox" name="groupWeek" id="Saturday" value="星期六" >星期六</label>
	<label><input type="checkbox" name="groupWeek" id="Sunday" value="星期日" >星期日</label>
    </div>
  	</div>

  	
   	<div class="form-group">
    <label class="control-label col-md-1" for="conference-info_address">上班时间</label>
	<div class="col-sm-5">
	<input id="startTime" type="text" name="startTime" value="${startTime }" readonly="readonly"  style="background-color:white" onclick="WdatePicker({dateFmt:'HH:mm'})" size="40" 
	class="form-control required" >
    </div>
    </div>
    
    <div class="form-group">
    <label class="control-label col-md-1" for="conference-info_address">下班时间</label>
	<div class="col-sm-5">
	<input id="endTime" type="text" name="endTime" value="${endTime }" readonly="readonly" style="background-color:white" onclick="WdatePicker({dateFmt:'HH:mm'})"  size="40" 
	class="form-control required" >
    </div>
  	</div>
  	
  	<div class="form-group">
    <label class="control-label col-md-1" for="conference-info_address">加班时间</label>
	<div class="col-sm-5">
	<input id="overTime" type="text" name="overTime"  value="${overTime }" readonly="readonly" style="background-color:white" onclick="WdatePicker({dateFmt:'HH:mm'})"  size="40" 
	class="form-control " >
    </div>
  	</div>
  	
  	<div class="form-group">
  	<label class="control-label col-md-1" for="conference-info_address">有效范围</label>
	<div class="col-sm-5">
	<c:if test="${radius==null||radius==100}">
	<label><input class="clickRadio" type="radio" name="radius"  value="100" checked="checked" > 100米</label>
	<label><input class="clickRadio" type="radio" name="radius"  value="300"> 300米</label>
	<label><input class="clickRadio" type="radio" name="radius"  value="500"> 500米</label>
	</c:if>
	<c:if test="${radius==300}">
	<label><input class="clickRadio" type="radio" name="radius"  value="100" > 100米</label>
	<label><input class="clickRadio" type="radio" name="radius"  value="300" checked="checked"> 300米</label>
	<label><input class="clickRadio" type="radio" name="radius"  value="500"> 500米</label>
	</c:if>
	<c:if test="${radius==500}">
	<label><input class="clickRadio" type="radio" name="radius"  value="100" > 100米</label>
	<label><input class="clickRadio" type="radio" name="radius"  value="300" > 300米</label>
	<label><input class="clickRadio" type="radio" name="radius"  value="500" checked="checked"> 500米</label>
	</c:if>
	</div>
  	</div>

	<div class="form-group">		    	
	<label class="control-label  col-md-1">考勤人员:</label>
	<div class="input-group userPicker col-md-5" selectedType="checkbox" selectedStyle="true" style="display:;padding-left: 15px;">
		<input id="" name="userId" type="hidden" class="input-medium" value="${userId}">
			<input type="text"
			 value='<tags:user userId="${userId}"></tags:user>' 
			 class="form-control " readOnly>
		<div class="input-group-addon addPerson"><i class="glyphicon glyphicon-user"></i></div>
		<div class="input-group-addon delPerson"><i class="fa fa-trash-o"></i></div>
	</div>
	</div> 

		<div class="form-group">
		<label class="control-label col-md-1" for="conference-info_address"></label>
    	<div class="col-sm-5" style="height:20px;" >
    	  温馨提示：每人只有一个考勤组哦!
    	</div>
    	</div>


 	<div class="form-group">
    <label class="control-label col-md-1" for="conference-info_address">打卡定位</label>
	<div class="col-sm-5" style="position: relative;">
		<div style="position: absolute;width: 20px;height: 36px;z-index: 999;left: 50%;top: 50%;margin-left: -10px;margin-top: -25px;">
			<i class="fa fa-map-marker" style="font-size: 35px;color: #ff6c00;"></i>
		</div>
		<div style="position: absolute;z-index: 999;left: 5%;top: 5%;">
			<input id="suggestId" type="text" >
			
		</div>
		<div id="searchResultPanel" style="z-index: 999; border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
		<div id="l-map" class="form-control"></div>
    </div>
  	</div>
  	
  		<div class="form-group">
		<label class="control-label col-md-1" for="conference-info_address"></label>
    	<div class="col-sm-5" style="height:20px;" >
    	  温馨提示：比例尺越小设置的打卡地点越精准哦!
    	</div>
    	</div>
   	
   	
   	<div class="form-group">
    <label class="control-label col-md-1" for="conference-info_address">详细地址</label>
	<div class="col-sm-5">
	<input id="centerSite" type="text" name="centerSite"  value="${centerSite }" class="form-control required" maxlength="50" >
    </div>
  	</div>
<!-- 	<div class="form-group">

    <label class="control-label col-md-1" for="conference-info_address">手动输入地点 </label>
	<div class="col-sm-5">
	<div id="r-result"><input type="text" id="suggestId" size="20" value="百度" class="form-control" /></div>
	<div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>
    </div>
    
  	</div>
	
	
    <div class="form-group">
    
    
    <label class="control-label col-md-1" for="conference-info_address">办公地点</label>
	<div class="col-sm-5">
	<div id="l-map" class="form-control"></div>
    </div>
  
    
  	</div>-->


  	<div class="form-group-separator"></div>
  	<div class="form-group">
	<div class="col-sm-1"></div>
	<div class="col-sm-1">
	<button id="submitButton" type="button" class="btn btn-secondary">
	<i class="fa fa-save"></i><span>&nbsp;&nbsp;保&nbsp;存</span>
	</button>
	</div>
	
	<div class="col-sm-1"><button type="button" class="btn btn-warning" onclick="location='${pageContext.request.contextPath}/domap/map-mapGroup-list.do'">
	<i class="fa-mail-reply"></i>&nbsp;&nbsp;返&nbsp;回</button>
	</div>
  	</div>
</form>

		</div>


	<!-- end of main -->
	</div>
<%@include file="/header/mainFooter.jsp" %>
  </body>

</html>

<script type="text/javascript">

		$(".clickRadio").click(function(){
			var groupConter=$("#groupConter").val();
			var groupConterArr = groupConter.split(","); 
			var groupConterLng = groupConterArr[0];
			var groupConterLat = groupConterArr[1];
			var conter = new BMap.Point(groupConterLng,groupConterLat);
			map.clearOverlays();  
			var circle = new BMap.Circle(conter,$("input[name='radius']:checked").val(),{strokeColor:"white",fillColor:"#0078D7",strokeWeight:2, strokeOpacity:0.2});
			map.addOverlay(circle);
		})


		var map = new BMap.Map('l-map',{enableMapClick:false});//构造底图时，关闭底图可点功能
		map.disableInertialDragging()						//禁用惯性拖拽
		map.addControl(new BMap.ScaleControl());             // 添加比例尺控件
		map.enableScrollWheelZoom();                         //启用滚轮放大缩小
		map.disable3DBuilding();
		
		
		var geoc = new BMap.Geocoder();    
		var groupConter="1"
			groupConter=$("#groupConter").val();
		//初始化地图操作  修改
		if(groupConter!=""){
			var groupConterArr =groupConter.split(","); 
			var groupConterLng = groupConterArr[0];
			var groupConterLat = groupConterArr[1];
			var conter = new BMap.Point(groupConterLng,groupConterLat);
			map.centerAndZoom(conter,16);
			//查看区域
			map.clearOverlays();  
			var circle = new BMap.Circle(conter,$("input[name='radius']:checked").val(),{strokeColor:"white",fillColor:"#0078D7",strokeWeight:2, strokeOpacity:0.2});
			map.addOverlay(circle);
		}else{
			//新建操作
			var r = new BMap.Point(116.404, 39.915);
			map.centerAndZoom(r,16);
			var geolocation = new BMap.Geolocation();
			geolocation.getCurrentPosition(function(conter){
				if(this.getStatus() == BMAP_STATUS_SUCCESS){
					var mk = new BMap.Marker(conter.point);
					map.panTo(conter.point);
					$("#groupConter").val(conter.point.lng+','+conter.point.lat);

					//当前位置详细信息
					geoc.getLocation(conter.point, function(rs){
		    			var addComp = rs.addressComponents;
		    			var test = rs.surroundingPois;
		    			if(test=="" || typeof(test) == "undefined"){
		    				$("#centerSite").val(addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber);
		    			}else{
		    				$("#centerSite").val(addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber+test[0].title);
		    			}
		    			
		    		}); 
					
					//查看区域
	    			map.clearOverlays();  
	    			var circle = new BMap.Circle(conter.point,$("input[name='radius']:checked").val(),{strokeColor:"blue",fillColor:"#0078D7",strokeWeight:2, strokeOpacity:0.2});
	    			map.addOverlay(circle);
				}       
			},{enableHighAccuracy: true})
		}

		
		//移动后中心点坐标
		map.addEventListener("dragend", function(){    
    		var center = map.getCenter();     
    		$("#groupConter").val(center.lng+','+center.lat);
				//获取地图街道信息
    			geoc.getLocation(center, function(rs){
	    			var addComp = rs.addressComponents;
	    			var test = rs.surroundingPois;
	    			if(test=="" || typeof(test) == "undefined"){
	    				$("#centerSite").val(addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber);
	    			}else{
	    				$("#centerSite").val(addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber+test[0].title);
	    			}
	    		}); 
    			//查看区域
    			map.clearOverlays();  
    			var circle = new BMap.Circle(center,$("input[name='radius']:checked").val(),{strokeColor:"blue",fillColor:"#0078D7",strokeWeight:2, strokeOpacity:0.2});
    			map.addOverlay(circle);
		})
		
		
		//个性化在线编辑器地址：http://lbsyun.baidu.com/custom/
		var styleJson = [
		
		]
		map.setMapStyle({styleJson:styleJson});

		 // 添加定位控件
//		 var geolocationControl = new BMap.GeolocationControl();
//		  geolocationControl.addEventListener("locationSuccess", function(e){
		    // 定位成功事件
//			  var center = map.getCenter();
//			  $("#groupConter").val(center.lng+','+center.lat);
			//当前位置详细信息
///			  geoc.getLocation(center, function(rs){
//	    			var addComp = rs.addressComponents;
//	    			var test = rs.surroundingPois;
//	    			if(test=="" || typeof(test) == "undefined"){
//    				$("#centerSite").val(addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber);
//	    			}else{
//	    				$("#centerSite").val(addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber+test[0].title);
//	    			}
//	    		}); 
			//查看区域
//  			map.clearOverlays();  
//  			var circle = new BMap.Circle(center,$("input[name='radius']:checked").val(),{strokeColor:"blue",fillColor:"#0078D7",strokeWeight:2, strokeOpacity:0.2});
//  			map.addOverlay(circle);
//		  });
//		  geolocationControl.addEventListener("locationError",function(e){
//		    // 定位失败事件
//		    alert("获取定位失败,请联系管理员");
//		  });
//		  map.addControl(geolocationControl);
			
		  
		  //移动分辨率事件
		   map.addEventListener("zoomend", function(){
			   	var center = map.getCenter();   
	    		$("#groupConter").val(center.lng+','+center.lat);
					//获取地图街道信息
	    			geoc.getLocation(center, function(rs){
		    			var addComp = rs.addressComponents;
		    			var test = rs.surroundingPois;
		    			if(test=="" || typeof(test) == "undefined"){
		    				$("#centerSite").val(addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber);
		    			}else{
		    				$("#centerSite").val(addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber+test[0].title);
		    			}
		    		}); 
	    			//查看区域
	    			map.clearOverlays();  
	    			var circle = new BMap.Circle(center,$("input[name='radius']:checked").val(),{strokeColor:"blue",fillColor:"#0078D7",strokeWeight:2, strokeOpacity:0.2});
	    			map.addOverlay(circle);
  			});
  
		  
		  //搜索框事件
			function G(id) {
				return document.getElementById(id);
			}


			var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
				{"input" : "suggestId"
				,"location" : map
			});

			ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
			var str = "";
				var _value = e.fromitem.value;
				var value = "";
				if (e.fromitem.index > -1) {
					value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
				}    
				str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;
				
				value = "";
				if (e.toitem.index > -1) {
					_value = e.toitem.value;
					value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
				}    
				str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
				G("searchResultPanel").innerHTML = str;
			});

			var myValue;
			ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
			var _value = e.item.value;
				myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
				G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
				
				setPlace();
			});

			function setPlace(){
				map.clearOverlays();    //清除地图上所有覆盖物
				function myFun(){
					var conter = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
					map.centerAndZoom(conter, 18);
					//当前位置详细信息
					$("#groupConter").val(conter.lng+','+conter.lat);
		    		geoc.getLocation(conter, function(rs){
		    			var addComp = rs.addressComponents;
		    			var test = rs.surroundingPois;
		    			if(test=="" || typeof(test) == "undefined"){
		    				$("#centerSite").val(addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber);
		    			}else{
		    				$("#centerSite").val(addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber+test[0].title);
		    			}
		    		}); 
		    		//查看区域
	    			map.clearOverlays();  
	    			var circle = new BMap.Circle(conter,$("input[name='radius']:checked").val(),{strokeColor:"blue",fillColor:"#0078D7",strokeWeight:2, strokeOpacity:0.2});
	    			map.addOverlay(circle);
				}
				var local = new BMap.LocalSearch(map, { //智能搜索
				  onSearchComplete: myFun
				});
				local.search(myValue);
			}






	
</script>
