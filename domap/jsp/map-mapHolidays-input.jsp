<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%pageContext.setAttribute("currentHeader", "domap");%>
<%pageContext.setAttribute("currentMenu", "domap");%>
<%pageContext.setAttribute("currentMenuId", "719");%>
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
					$("#submitButton").click(function(){
				var start =$("#startWorkTime").val();
				var startSplit=start.replace(":","");
				var end =$("#endWorkTime").val();
				var endSplit=end.replace(":","");
				var startSum = parseInt(startSplit);
				var endSum = parseInt(endSplit);
				if(endSum<=startSum){
					$("#endWorkTime").val("");
					alert("结束时间应大于开始时间");
					return;
				}
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


<form id="conference-managementForm" method="post" action="map-mapHolidays-save.do" class="form-horizontal">


    <div class="form-group">
    <label class="control-label col-md-1" for="conference-info_address">时间</label>
	<div class="col-sm-5">
	<input type="text" name="mapTime" readonly="readonly" style="background-color:white;" class="form-control required "onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
    </div>
  	</div>
  	
  	 
  	<div class="form-group">
  	<label class="control-label col-md-1" for="conference-info_address">状态</label>
	<div class="col-sm-5">
	<label><input type="radio" name="status"  value="0" checked="checked" > 休息</label>
	<label><input type="radio" name="status"  value="1" > 工作</label>
    </div>
    </div>
    
     <div class="form-group">
    <label class="control-label col-md-1" for="conference-info_address">备注:</label>
	<div class="col-sm-5">
	  	<textarea name="mapNotes"  maxlength="400"
		style="height: 155px;" class="form-control " value="${mapNotes}"></textarea>
    </div>
  	</div>
    
    
 	<div class="form-group-separator"></div>
  	<div class="form-group">
	<div class="col-sm-1"></div>
	<div class="col-sm-1">
	<button id="submitButton"  class="btn btn-secondary">
	<i class="fa fa-save"></i><span>&nbsp;&nbsp;保&nbsp;存</span>
	</button>
	</div>
	
	<div class="col-sm-1"><button type="button" class="btn btn-warning" onclick="location='map-mapHolidays-list.do'">
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
