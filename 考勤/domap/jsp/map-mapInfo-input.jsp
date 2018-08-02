<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentHeader", "domap");%>
<%pageContext.setAttribute("currentMenu", "domap");%>
<%pageContext.setAttribute("currentMenuId", "717");%>
<!doctype html>
<html>
<!-- 用户页面 -->
  <head>
    <%@include file="/common/meta.jsp"%>
    <title>编辑</title>
    <%@include file="/common/s3.jsp"%>
    <%-- <%request.getAttribute(roomId); %> --%>
    <script type="text/javascript">
    
$(function(){
		var choose = $("#choose").val();
		if(choose == 0 ){
			var startTimeInfo = $("#startTimeInfo").val();
			$("#startTime").val(startTimeInfo);
			$("#startTimeWrite").val(startTimeInfo);
			var endTimeInfo = $("#endTimeInfo").val();
			$("#endTime").val(endTimeInfo);
			$("#endTimeWrite").val(endTimeInfo);
			
			$("#startTime,#endTime").show();
			$("#startTimeWrite,#endTimeWrite").hide();
		}else if(choose == 1){
			var startTimeInfo = $("#startTimeInfo").val();
			$("#startTimeWrite").val(startTimeInfo);
			var endTimeInfo = $("#endTimeInfo").val();
			$("#endTimeWrite").val(endTimeInfo);
			
			$("#startTime,#endTime").hide();
			$("#startTimeWrite,#endTimeWrite").show();
		}else if(choose == 5){
			var startTimeInfo = $("#startTimeInfo").val();
			$("#startTimeWrite").val(startTimeInfo);
			var endTimeInfo = $("#endTimeInfo").val();
			$("#endTimeWrite").val(endTimeInfo);
						
			$("#startTime,#endTime").show();
			$("#startTimeWrite,#endTimeWrite").hide();
			
			
		}else {
			var startTimeInfo = $("#startTimeInfo").val();
			$("#startTime").val(startTimeInfo);
			$("#startTimeWrite").val(startTimeInfo);
			var endTimeInfo = $("#endTimeInfo").val();
			$("#endTime").val(endTimeInfo);
			$("#endTimeWrite").val(endTimeInfo);
			
			$("#showAndHide").hide();
		}
		
		if($("#startStatusInfo").val()==0){
			$("#startWorkTimeRead").show();
			$("#startWorkTime").hide();
			$("#startSite").val("");
			$("#startSite").attr({"readonly":"readonly"});
		}else{
			$("#startWorkTimeRead").hide();
			$("#startWorkTime").show();
		}
		if($("#endStatusInfo").val()==0){
			$("#endWorkTimeRead").show();
			$("#endWorkTime").hide();
			$("#endSite").val("");
			$("#endSite").attr({"readonly":"readonly"});
		}else{
			$("#endWorkTimeRead").hide();
			$("#endWorkTime").show();
		}
		
		
		if($("#startOutInfo").val()==0){
			$("#startOutShowHide").hide();
		}else{
			$("#startOutShowHide").show();
		}
		
		if($("#endOutInfo").val()==0){
			$("#endOutShowHide").hide();
		}else{
			$("#endOutShowHide").show();
		}
		
		
		switch(parseInt(choose)){
		case 0:
			 $("#zero").attr("checked","checked");
			break;
		case 1:
		 	 $("#one").attr("checked","checked");
			break;
		case 2:
			 $("#two").attr("checked","checked");
			break;
		case 3:
			 $("#three").attr("checked","checked");
			break;
		case 4:
			 $("#four").attr("checked","checked");
			break;
		case 5:
			 $("#five").attr("checked","checked");
			break;
		case 6:
			 $("#six").attr("checked","checked");
			break;
		}
})

$(function(){
		$("#zero").click(function(){
			$("#showAndHide").show();
			$("#startTime,#endTime").show();
			$("#startTimeWrite,#endTimeWrite").hide();
			
			var startTimeInfo = $("#startTimeInfo").val();
			$("#startTime").val(startTimeInfo);
			$("#startTimeWrite").val(startTimeInfo);
			var endTimeInfo = $("#endTimeInfo").val();
			$("#endTime").val(endTimeInfo);
			$("#endTimeWrite").val(endTimeInfo);
			$("#startWorkTime,#endWorkTime").val("");
		})
		$("#one").click(function(){
			$("#showAndHide").show();
			$("#startTime,#endTime").hide();
			$("#startTimeWrite,#endTimeWrite").show();
			
			var startTimeInfo = $("#startTimeInfo").val();
			$("#startTimeWrite").val(startTimeInfo);
			var endTimeInfo = $("#endTimeInfo").val();
			$("#endTimeWrite").val(endTimeInfo);
			$("#startTime,#endTime").val("");
			$("#startWorkTime,#endWorkTime").val("");
		})
		$("#five").click(function(){
			$("#showAndHide").show();
			$("#startTime,#endTime").show();
			$("#startTimeWrite,#endTimeWrite").hide();
			
			var startTimeInfo = $("#startTimeInfo").val();
			$("#startTimeWrite").val(startTimeInfo);
			var endTimeInfo = $("#endTimeInfo").val();;
			$("#endTimeWrite").val(endTimeInfo);
			$("#startTime,#endTime").val("");
			$("#startWorkTime,#endWorkTime").val("");  	
		})
		$("#two,#three,#four,#six").click(function(){
			$("#showAndHide").hide();
			
			var startTimeInfo = $("#startTimeInfo").val();
			$("#startTimeWrite").val(startTimeInfo);
			var endTimeInfo = $("#endTimeInfo").val();;
			$("#endTimeWrite").val(endTimeInfo);
			$("#startTime,#endTime").val("");
			$("#startWorkTime,#endWorkTime").val(""); 
		})
		
		
		$("#startStatusV0").click(function(){
			$("#startWorkTimeRead").show();
			$("#startWorkTime").hide();
			$("#startWorkTime").val("");
			$("#startSite").val("");
			$("#startSite").attr({"readonly":"readonly"});
		})
		$("#startStatusV1,#startStatusV2").click(function(){
			$("#startWorkTimeRead").hide();
			$("#startWorkTime").show();
			$("#startWorkTime").val($("#startWorkTimeInfo").val());
			$("#startSite").val($("#startSiteInfo").val());
			$("#startSite").removeAttr("readonly");
		})
		$("#endStatusV0").click(function(){
			$("#endWorkTimeRead").show();
			$("#endWorkTime").hide();
			$("#endWorkTime").val("");
			$("#endSite").val("");
			$("#endSite").attr({"readonly":"readonly"});
			
		})
		$("#endStatusV1,#endStatusV2").click(function(){
			$("#endWorkTimeRead").hide();
			$("#endWorkTime").show();
			$("#endWorkTime").val($("#endWorkTimeInfo").val());
			$("#endSite").val($("#endSiteInfo").val());
			$("#endSite").removeAttr("readonly");
		})
		
		
		$("#startOutV0").click(function(){
			$("#startOutShowHide").hide();
			$("#startOutReason").val("");
		})
		$("#startOutV1").click(function(){
			$("#startOutShowHide").show();
			$("#startOutReason").val("");
		})
		$("#endOutV0").click(function(){
			$("#endOutShowHide").hide();
			$("#endOutReason").val("");
		})
		$("#endOutV1").click(function(){
			$("#endOutShowHide").show();
			$("#endOutReason").val("");
		})
		
		
})

$(function(){
			$("#submitButton").click(function(){
				var choose = $("input[name='choose']:checked").val();
					if(choose==0||choose==1){
						var startWorkTime =$("#startWorkTime").val();
						var startStatus = $("input[name='startStatus']:checked").val();
						if(startWorkTime==""){
							if(startStatus!=0){
								alert("签到时间不能为空")
								return;
							}
						} 
						if(startStatus==1){
							var startWorkTimeSplit=startWorkTime.replace(":","");
							var startTime = $("#startTimeWrite").val();
							var startTimeSplit=startTime.replace(":","");
							if(parseInt(startTimeSplit)<parseInt(startWorkTimeSplit)){
								$("#startWorkTime").val("");
								alert("当签到状态为正常时:签到时间应小于等于上班时间");
								return;
							}
						}else if(startStatus==2){
							var startWorkTimeSplit=startWorkTime.replace(":","");
							var startTime = $("#startTimeWrite").val();
							var startTimeSplit=startTime.replace(":","");
							if(parseInt(startTimeSplit)>parseInt(startWorkTimeSplit)){
								$("#startWorkTime").val("");
								alert("当签到状态为迟到时:签到时间应小于上班时间");
								return;
							}
						}
						
						var endWorkTime =$("#endWorkTime").val();
						var endStatus = $("input[name='endStatus']:checked").val();
						
						if(endWorkTime==""){
							if(endStatus!=0){
								alert("签退时间不能为空")
								return;
							}
						} 
						if(endStatus==1){
							var endWorkTimeSplit=endWorkTime.replace(":","");
							var endTime = $("#endTimeWrite").val();
							var endTimeSplit=endTime.replace(":","");
							if(parseInt(endTimeSplit)>parseInt(endWorkTimeSplit)){
								$("#endWorkTime").val("");
								alert("当签退状态为正常时:签退时间应大于等于下班时间");
								return;
							}
						}else if(endStatus==2){
							var endWorkTimeSplit=endWorkTime.replace(":","");
							var endTime = $("#endTimeWrite").val();
							var endTimeSplit=endTime.replace(":","");
							if(parseInt(endTimeSplit)<parseInt(endWorkTimeSplit)){
								$("#endWorkTime").val("");
								alert("当签退状态为早退时:签退时间应小于下班时间");
								return;
							}
						}
						if(startStatus!=0&&endStatus!=0){
							var startWorkTimeSplit=startWorkTime.replace(":","");
							var endWorkTimeSplit=endWorkTime.replace(":","");
							if(parseInt(endWorkTimeSplit)<parseInt(startWorkTimeSplit)){
								$("#endWorkTime").val("");
								alert("签退时间应大于签到时间");
								return;
							}
						}
					}else if(choose==5){
						var startStatus = $("input[name='startStatus']:checked").val();
						var endStatus = $("input[name='endStatus']:checked").val();
						if(startStatus==1&&endStatus==1){
							var startWorkTime =$("#startWorkTime").val();
							var endWorkTime =$("#endWorkTime").val();
							
							var startWorkTimeSplit=startWorkTime.replace(":","");
							var endWorkTimeSplit=endWorkTime.replace(":","");
							if(parseInt(endWorkTimeSplit)<parseInt(startWorkTimeSplit)){
								$("#endWorkTime").val("");
								alert("签退时间应大于签到时间");
								return;
							}
						}else{
							if(startStatus!=1){
								alert("休息日签到状态只能为正常")
								return;
							}else if(endStatus!=1){
								alert("休息日签退状态只能为正常")
								return;
							}
						}
					}
					$(this).parents("form").submit();
				})
		})			
    </script>
<link type="text/css" rel="stylesheet" href="${tenantPrefix}/widgets/userpickerSuper/userpicker.css?jsEdition=${jsEdition}">
<script type="text/javascript" src="${tenantPrefix}/widgets/userpickerSuper/userpicker.js?jsEdition=${jsEdition}"></script>
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

<form id="conference-managementForm" method="post" action="map-mapInfo-update.do" class="form-horizontal">

   	<div class="form-group">
    <label class="control-label col-md-1" for="conference-info_address">姓名</label>
	<div class="col-sm-5">
	<input type="hidden" name="id" value="${id}" >
	<input type="hidden" name="userId" value="${userId}" >
	<input type="hidden"  id="choose" value="${choose}" >
	<!-- 上班时间-->
	<input type="hidden"  id="startTimeInfo" value="${startTime}" >
	<!-- 下班时间-->
	<input type="hidden"  id="endTimeInfo" value="${endTime}" >
	<!-- 签到时间-->
	<input type="hidden"  id="startWorkTimeInfo" value="${startWorkTime}" >
	<!-- 签退时间-->
	<input type="hidden"  id="endWorkTimeInfo" value="${endWorkTime}" >
	<!-- 签到地点-->
	<input type="hidden"  id="startSiteInfo" value="${startSite}" >
	<!-- 签退地点-->
	<input type="hidden"  id="endSiteInfo" value="${endSite}" >
	<!-- 加班时间-->
	<input type="hidden"  id="overTimeInfo" name="overTime" value="${overTime}" >
	<!-- 签到状态-->
	<input type="hidden"  id="startStatusInfo" value="${startStatus}" >
	<!-- 签退状态-->
	<input type="hidden"  id="endStatusInfo" value="${endStatus}" >
	<!-- 上班外勤状态-->
	<input type="hidden"  id="startOutInfo" value="${startOut}" >
	<!-- 下班外勤状态-->
	<input type="hidden"  id="endOutInfo" value="${endOut}" >
	
	<input type="text" name="userName" value="${userName }" readonly="readonly" class="form-control required">
    </div>
  	</div>

  	<div class="form-group">
  	<label class="control-label col-md-1" for="conference-info_address">考勤组</label>
	<div class="col-sm-5">
	<input type="text" name="groupName" value="${groupName }" readonly="readonly" class="form-control required">
    </div>
  	</div>
  	
  	<div class="form-group">
    <label class="control-label col-md-1" for="conference-info_address">当前时间</label>
	<div class="col-sm-5">
	<input type="text" name="currentTime" value="<fmt:formatDate value='${currentTime}' pattern='yyyy-MM-dd' type="date"/>" readonly="readonly" class="form-control required">
    </div>
  	</div>
  	
  	<div class="form-group">
    <label class="control-label col-md-1" for="conference-info_address">状态</label>
	<div class="col-sm-5">
	<label><input type="radio" name="choose" id="zero" value="0" > 上班</label>
	<label><input type="radio" name="choose" id="one" value="1" > 请假(非全天)</label>
	<label><input type="radio" name="choose" id="five" value="5" > 加班</label>
	<label><input type="radio" name="choose" id="two" value="2" > 旷工</label>
	<label><input type="radio" name="choose" id="three" value="3" > 请假</label>
	<label><input type="radio" name="choose" id="four" value="4" > 出差</label>
	<label><input type="radio" name="choose" id="six" value="6" > 休息</label>
    </div>
  	</div>

  	<div id="showAndHide">
	  	<div class="form-group">
	  	<label class="control-label col-md-1" for="conference-info_address">&nbsp;</label>
		<div class="col-sm-5" style="text-align:center">
		上班操作
	    </div>
	  	</div>
	  	
	   	<div class="form-group">
	    <label class="control-label col-md-1" for="conference-info_address">上班时间</label>
		<div class="col-sm-5">
		<input id="startTime" type="text"  readonly="readonly" value="" size="40" 
		class="form-control " >
		<input id="startTimeWrite" type="text" name="startTime" readonly="readonly" value="" style="background-color:white;"   onclick="WdatePicker({dateFmt:'HH:mm'})" size="40" 
		class="form-control " >
	    </div>
	  	</div>
	
	  	<div class="form-group">
	  	<label class="control-label col-md-1" for="conference-info_address">签到状态</label>
		<div class="col-sm-5">
		<c:if test="${startStatus == 0}">
		<label><input type="radio" name="startStatus"  id="startStatusV0" value="0" checked="checked" > 缺卡</label>
		<label><input type="radio" name="startStatus"  id="startStatusV1" value="1" > 正常</label>
		<label><input type="radio" name="startStatus"  id="startStatusV2" value="2" > 迟到</label>
		</c:if>
		<c:if test="${startStatus == 1}">
		<label><input type="radio" name="startStatus"  id="startStatusV0" value="0" > 缺卡</label>
		<label><input type="radio" name="startStatus"  id="startStatusV1" value="1" checked="checked"> 正常</label>
		<label><input type="radio" name="startStatus"  id="startStatusV2" value="2" > 迟到</label>
		</c:if>
		<c:if test="${startStatus == 2}">
		<label><input type="radio" name="startStatus"  id="startStatusV0" value="0" > 缺卡</label>
		<label><input type="radio" name="startStatus"  id="startStatusV1" value="1" > 正常</label>
		<label><input type="radio" name="startStatus"  id="startStatusV2" value="2" checked="checked"> 迟到</label>
		</c:if>
	    </div>
	    </div>

	  	<div class="form-group">
	    <label class="control-label col-md-1" for="conference-info_address">签到时间</label>
		<div class="col-sm-5">
		<input id="startWorkTime" type="text" name="startWorkTime" readonly="readonly"  style="background-color:white;"  value="${startWorkTime }" onclick="WdatePicker({dateFmt:'HH:mm'})" size="40" 
		class="form-control " >
		<input id="startWorkTimeRead" type="text"  readonly="readonly"   size="40" class="form-control " >
	    </div>
	  	</div>
	  	
	  	<div class="form-group">
	    <label class="control-label col-md-1" for="conference-info_address">签到地点</label>
		<div class="col-sm-5">
		<input id="startSite" type="text" name="startSite"  value="${startSite}"  maxlength="50" class="form-control " >
	    </div>
	  	</div>

	    <div class="form-group">
	  	<label class="control-label col-md-1" for="conference-info_address">签到外勤</label>
		<div class="col-sm-5">
		<c:if test="${startOut == 0}">
		<label><input type="radio" name="startOut" id="startOutV0" value="0" checked="checked"> 本地</label>
		<label><input type="radio" name="startOut" id="startOutV1" value="1"  > 外勤</label>
		</c:if>
		<c:if test="${startOut == 1}">
		<label><input type="radio" name="startOut" id="startOutV0" value="0"  > 本地</label>
		<label><input type="radio" name="startOut" id="startOutV1" value="1" checked="checked" > 外勤</label>
		</c:if>
	    </div>	    
	    </div>

	    <div class="form-group"> 
	    <div id="startOutShowHide">
	    <label class="control-label col-md-1" for="conference-info_address">外勤理由</label>
		<div class="col-sm-5">
		<textarea name="startOutReason"  id="startOutReason"
			style="height: 100px;"  maxlength="400" class="form-control " value = "${startOutReason }">${startOutReason }</textarea>
	    </div>
	    </div>
  		</div>

  		<div class="form-group">
	    <label class="control-label col-md-1" for="conference-info_address">&nbsp;</label>
	    <div class="col-sm-5" style="text-align:center">
		下班操作
	    </div>
	  	</div>

	   	<div class="form-group">
	    <label class="control-label col-md-1" for="conference-info_address">下班时间</label>
		<div class="col-sm-5">
		<input id="endTime" type="text"  readonly="readonly" value="" size="40" 
		class="form-control " >
		<input id="endTimeWrite" type="text" name="endTime" readonly="readonly"  value="" style="background-color:white;"   onclick="WdatePicker({dateFmt:'HH:mm'})" size="40" 
		class="form-control " >
	    </div>
	  	</div>

	  	<div class="form-group">   
	    <label class="control-label col-md-1" for="conference-info_address">签退状态</label>
		<div class="col-sm-5">
		<c:if test="${endStatus == 0}">
		<label><input type="radio" name="endStatus"  id="endStatusV0" value="0" checked="checked" > 缺卡</label>
		<label><input type="radio" name="endStatus"  id="endStatusV1" value="1" > 正常</label>
		<label><input type="radio" name="endStatus"  id="endStatusV2" value="2" > 早退</label>
		</c:if>
		<c:if test="${endStatus == 1}">
		<label><input type="radio" name="endStatus"  id="endStatusV0" value="0" > 缺卡</label>
		<label><input type="radio" name="endStatus"  id="endStatusV1" value="1" checked="checked" > 正常</label>
		<label><input type="radio" name="endStatus"  id="endStatusV2" value="2" > 早退</label>
		</c:if>
		<c:if test="${endStatus == 2}">
		<label><input type="radio" name="endStatus"  id="endStatusV0" value="0" > 缺卡</label>
		<label><input type="radio" name="endStatus"  id="endStatusV1" value="1" > 正常</label>
		<label><input type="radio" name="endStatus"  id="endStatusV2" value="2" checked="checked" > 早退</label>
		</c:if>
		</div>
	    </div>
	    
	  	<div class="form-group">	    
	    <label class="control-label col-md-1" for="conference-info_address">签退时间</label>
		<div class="col-sm-5">
		<input id="endWorkTime" type="text" name="endWorkTime" readonly="readonly"  style="background-color:white;" value="${endWorkTime }" onclick="WdatePicker({dateFmt:'HH:mm'})" size="40" 
		class="form-control " >
		<input id="endWorkTimeRead" type="text"  readonly="readonly"   size="40" class="form-control " >
	  	</div>
	  	</div>

		<div class="form-group">
	    <label class="control-label col-md-1" for="conference-info_address">签退地点</label>
		<div class="col-sm-5">
		<input id="endSite" type="text" name="endSite"  value="${endSite}"  maxlength="50" class="form-control " >
	    </div>
	  	</div>
	  	
	    <div class="form-group"> 
	    <label class="control-label col-md-1" for="conference-info_address">签退外勤</label>
		<div class="col-sm-5">
		<c:if test="${endOut == 0}">
		<label><input type="radio" name="endOut" id="endOutV0" value="0" checked="checked"> 本地</label>
		<label><input type="radio" name="endOut" id="endOutV1" value="1"  > 外勤</label>
		</c:if>
		<c:if test="${endOut == 1}">
		<label><input type="radio" name="endOut" id="endOutV0" value="0"  > 本地</label>
		<label><input type="radio" name="endOut" id="endOutV1" value="1" checked="checked" > 外勤</label>
		</c:if>
		</div>
	    </div>

	    <div class="form-group">    
	    <div id="endOutShowHide">
	    <label class="control-label col-md-1" for="conference-info_address">外勤理由</label>
		<div class="col-sm-5">
		<textarea name="endOutReason" id="endOutReason" 
			style="height: 100px;"  maxlength="400" class="form-control"  value = "${endOutReason }">${endOutReason }</textarea>
	  	</div>
	  	</div>
  		</div>

  	</div>
  	<div class="form-group-separator"></div>
  	<div class="form-group">
	<div class="col-sm-1"></div>
	<div class="col-sm-1">
	<button id="submitButton" type="button" class="btn btn-secondary">
	<i class="fa fa-save"></i><span>&nbsp;&nbsp;保&nbsp;存</span>
	</button>
	</div>
	
	<div class="col-sm-1"><button type="button" class="btn btn-warning" onclick="location='map-mapInfo-list.do'">
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
