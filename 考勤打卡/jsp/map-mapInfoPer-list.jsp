<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%
	pageContext.setAttribute("currentHeader", "domap");
%>
<%
	pageContext.setAttribute("currentMenu", "domap");
%>
<%
	pageContext.setAttribute("currentMenuId", "711");
%>
<!doctype html>
<html lang="en">
<style>
.imp{
color:red;
}
#watchFile{
		display: none;
		position: fixed;
		width:100%;
		height: 100%;
		left: 0;
		top: 0;
		background: rgba(0,0,0,0.3);
		z-index: 999;
	}
	#watchFile .watchFileCon{
		position: absolute;
		left: 5%;
		top: 10%;
		padding-bottom: 0px;
		overflow:hidden;
		background:rgba(166,166,166,0);
		border: none;
		border-radius: 10px;
	}
	#watchFile .tips{
		width: 200px;
		height: 300px;
		position: absolute;
		right: 30px;
		top: 30px;
		text-align: center;
		z-index: 1;
		cursor: move;
	}
	#watchFile .tips p{
		border: 1px solid #ccc;
		background: #fff;
		padding: 10px;
		border-radius: 8px;
		color: #000;
	}
</style>
  <head>
    <%@include file="/common/meta.jsp"%>
    <title><spring:message code="dev.map-group-save.title" text="列表"/></title>
    <%@include file="/common/s3.jsp"%>
    <script type="text/javascript">
var config = {
    id: 'cms-articleGrid',
    pageNo: ${page.pageNo},
    pageSize: ${page.pageSize},
    totalCount: ${page.totalCount},
    resultSize: ${page.resultSize},
    pageCount: ${page.pageCount},
    orderBy: '${page.orderBy == null ? "" : page.orderBy}',
    asc: ${page.asc},
    params: {
    	'filter_LIKES_userName': '${param.filter_LIKES_userName}',
        'filter_EQS_startCurrentTime': '${param.filter_EQS_startCurrentTime}',
        'filter_EQS_endCurrentTime': '${param.filter_EQS_endCurrentTime}',
        'filter_LIKES_groupName': '${param.filter_LIKES_groupName}'     ,
        'filter_EQS_departmentName': '${param.filter_EQS_departmentName}'   
    },
	selectedItemClass: 'selectedItem',
	gridFormId: 'domap-infoGridForm',
	//exportUrl: 'domap-article-export.do'
};

var table;

$(function() {
	table = new Table(config);
    table.configPagination('.m-pagination');
    table.configPageInfo('.m-page-info');
    table.configPageSize('.m-page-size');
});
$(function(){
	$("#buttonId").click(function(){
		var start =$("#startCurrentTime").val();
		var startSplit=start.replace(/-/g,"");
		var end =$("#endCurrentTime").val();
		var endSplit=end.replace(/-/g,"");
		var startSum = parseInt(startSplit);
		var endSum = parseInt(endSplit);
		if(endSum<=startSum){
			$("#endCurrentTime").val("");
			alert("结束日期应大于开始日期");
			return;
		}
	})
	$("#buttonExport").click(function(){
		var start =$("#startCurrentTime").val();
		var startSplit=start.replace(/-/g,"");
		var end =$("#endCurrentTime").val();
		var endSplit=end.replace(/-/g,"");
		var startSum = parseInt(startSplit);
		var endSum = parseInt(endSplit);
		if(endSum<=startSum){
			$("#endCurrentTime").val("");
			alert("结束日期应大于开始日期");
			return;
		}
	})

})
    </script>
  </head>

   <body class="page-body">
     <%@include file="/header/mainHeader.jsp" %>

<div class="panel panel-default">
  <div class="panel-heading">
	<i class="glyphicon glyphicon-list"></i>
    查询
	<div class="pull-right ctrl">
	  <a class="btn btn-default btn-xs"><i id="cms-articleSearchIcon" class="glyphicon glyphicon-chevron-up"></i></a>
    </div>
  </div>
 
  <div class="panel-body">

		  <form id="forms" name="domap-userInfo" method="post"  class="form-inline" action="map-mapInfoPer-list.do">
		  <c:if test="${type==1}">
			  	<div class="form-group">
	                    <label for="startCurrentTime" >开始日期:</label>
	                    <div class="input-group">
		    			<input id="startCurrentTime" type="text" name="filter_EQS_startCurrentTime" 
		    			<fmt:parseDate value="${param.filter_EQS_startCurrentTime}" pattern="yyyy-MM-dd" var="dateStart"/>
		    			value="<fmt:formatDate value='${dateStart}' pattern='yyyy-MM-dd'/>" readonly="readonly" style="background-color:white;cursor:default;"
		    			class="form-control required" 
		    			onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
		    			<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
		  		</div>
		
		  		</div>
		  		<div class="form-group" style="margin-left:140px">
		  		
	                    <label for="endCurrentTime" >结束日期:</label>
	                    <div class="input-group">
		    			<input id="endCurrentTime" type="text" name="filter_EQS_endCurrentTime" 
		    			<fmt:parseDate value="${param.filter_EQS_endCurrentTime}" pattern="yyyy-MM-dd" var="dateEnd"/>
		    			value="<fmt:formatDate value='${dateEnd}' pattern='yyyy-MM-dd'/>" readonly="readonly" style="background-color:white;cursor:default;"
		    			class="form-control required" 
		    			
		    			onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
		    			<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
		  		</div>
	            </div>
	              <br>
	              <br> 
			    <label for="domap-domapInfo-userName"><spring:message code='domap-domapInfo.domap-domapInfo-userName' text=''/>姓名:</label>
			    <input type="text" id="domap-domapInfo-userName" name="filter_LIKES_userName" value="${param.filter_LIKES_userName}"maxlength="20" class="form-control">
			  		   &nbsp;
			    <label for="domap-domapInfo-userGroup"><spring:message code='domap-domapInfo.domap-domapInfo-userGroup' text=''/>考勤组:</label>
			    <input type="text" id="domap-domapInfo-userGroup" name="filter_LIKES_groupName" value="${param.filter_LIKES_groupName}" maxlength="20" class="form-control">
			  		   &nbsp;
			  	<label for="domap-domapInfo-departmentName"><spring:message code='domap-domapInfo.domap-domapInfo-departmentName' text=''/>所属部门:</label>
	      		<tags:getDepartments selectName="filter_EQS_departmentName" selectId="" selectValue="${param.filter_EQS_departmentName}" selectCalss="form-control" />  
		  </c:if>
		   <c:if test="${type==2}">
		   		<div class="form-group">
	                    <label for="startCurrentTime" >开始日期:</label>
	                    <div class="input-group">
		    			<input id="startCurrentTime" type="text" name="filter_EQS_startCurrentTime" 
		    			<fmt:parseDate value="${param.filter_EQS_startCurrentTime}" pattern="yyyy-MM-dd" var="dateStart"/>
		    			value="<fmt:formatDate value='${dateStart}' pattern='yyyy-MM-dd'/>" readonly="readonly" style="background-color:white;cursor:default;"
		    			class="form-control required" 
		    			onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
		    			<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
		  		</div>
		
		  		</div>
		  		<div class="form-group" style="margin-left:140px">
		  		
	                    <label for="endCurrentTime" >结束日期:</label>
	                    <div class="input-group">
		    			<input id="endCurrentTime" type="text" name="filter_EQS_endCurrentTime" 
		    			<fmt:parseDate value="${param.filter_EQS_endCurrentTime}" pattern="yyyy-MM-dd" var="dateEnd"/>
		    			value="<fmt:formatDate value='${dateEnd}' pattern='yyyy-MM-dd'/>" readonly="readonly" style="background-color:white;cursor:default;"
		    			class="form-control required" 
		    			
		    			onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
		    			<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
		  		</div>
	            </div>
		   </c:if>
		   <c:if test="${type==3}">
		  		<div class="form-group">
	                    <label for="startCurrentTime" >开始日期:</label>
	                    <div class="input-group">
		    			<input id="startCurrentTime" type="text" name="filter_EQS_startCurrentTime" 
		    			<fmt:parseDate value="${param.filter_EQS_startCurrentTime}" pattern="yyyy-MM-dd" var="dateStart"/>
		    			value="<fmt:formatDate value='${dateStart}' pattern='yyyy-MM-dd'/>" readonly="readonly" style="background-color:white;cursor:default;"
		    			class="form-control required" 
		    			onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
		    			<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
		  		</div>
		  		</div>
		  		<div class="form-group" style="margin-left:10px">
		  		
	                    <label for="endCurrentTime" >结束日期:</label>
	                    <div class="input-group">
		    			<input id="endCurrentTime" type="text" name="filter_EQS_endCurrentTime" 
		    			<fmt:parseDate value="${param.filter_EQS_endCurrentTime}" pattern="yyyy-MM-dd" var="dateEnd"/>
		    			value="<fmt:formatDate value='${dateEnd}' pattern='yyyy-MM-dd'/>" readonly="readonly" style="background-color:white;cursor:default;"
		    			class="form-control required" 
		    			
		    			onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})">
		    			<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
		  		</div>
	            </div>
			    <label for="domap-domapInfo-userName"><spring:message code='domap-domapInfo.domap-domapInfo-userName' text=''/>&nbsp;&nbsp;姓名:</label>
			    
			    <input type="text" id="domap-domapInfo-userName" name="filter_LIKES_userName" value="${param.filter_LIKES_userName}"maxlength="20" class="form-control">
		   </c:if>
		 <button id="buttonId" class="btn btn-primary " type="submit" style="margin-bottom: 2px;margin-left:40px"  >						
				<i class="fa-search">&nbsp;&nbsp;查&nbsp;询</i>
		 </button>
		 </form>

		</div>
	  </div>

    
      <div>

		<div class="pull-right">
		  每页显示
		  <select class="m-page-size form-control" style="display:inline;width:auto;">
		    <option value="10">10</option>
		    <option value="20">20</option>
		    <option value="50">50</option>
		  </select>
		  条
        </div>

	    <div class="clearfix"></div>
	  </div>

<form id="cms-articleGridForm" name="cms-articleGridForm" method='post' action="cms-article-remove.do" class="m-form-blank">
      <div class="panel panel-default">
        <div class="panel-heading">
		  <i class="glyphicon glyphicon-list"></i>
		  <spring:message code="scope-info.scope-info.list.title" text="列表"/>
		</div>


  <table id="cmsArticleGrid" class="table table-striped table-hover table-bordered dataTable"> 
    <thead>
      <tr>
     <!--    <th width="10" class="table-check"><input type="checkbox" name="checkAll" onchange="toggleSelectedItems(this.checked)"></th> -->
      	<th class="sorting"  width="120px">签到日期</th>
        <th class="sorting"  width="100px">姓名</th>
        <th class="sorting"  width="100px">部门</th>
        <th class="sorting"  width="100px">所属规则</th>
        <th class="sorting"  width="120px">状态</th>
        <th class="sorting"  width="100px">签到时间</th>
        <th class="sorting"  width="100px">状态</th>
        <th class="sorting"  width="100px">签退时间</th>
        <th class="sorting"  width="100px">状态</th>
        <th class="sorting"  width="150px">手机异常</th>
        <th class="sorting"  width="100px">工时</th>
      </tr>
    </thead>

    <tbody>
      <c:forEach items="${page.result}" var="item">
      <tr>
     	<td><fmt:formatDate value='${item.currentTime}' pattern='yyyy-MM-dd'/></td>
        <td>${item.userName} </td>
        <td><tags:getDepartmentName departmentId="${item.departmentId}"/></td>
		<td title="考勤信息"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content="上班时间：${item.startTime}<br>下班时间：${item.endTime}<br>加班时间：${item.overTime}">${item.groupName}</td>
		
		<c:if test="${item.choose == 0}">
				<c:if test="${item.startStatus==0 && item.endStatus==0}">
				<td class="imp">缺卡,缺卡</td>
				</c:if>
				<c:if test="${item.startStatus==0 && item.endStatus==1}">
				<td class="imp">缺卡</td>
				</c:if>
				<c:if test="${item.startStatus==0 && item.endStatus==2}">
				<td class="imp">缺卡,早退</td>
				</c:if>
				<c:if test="${item.startStatus==1 && item.endStatus==0}">
				<td class="imp">缺卡</td>
				</c:if>
				<c:if test="${item.startStatus==1 && item.endStatus==1}">
				<td>正常</td>
				</c:if>
				<c:if test="${item.startStatus==1 && item.endStatus==2}">
				<td class="imp">早退</td>
				</c:if>
				<c:if test="${item.startStatus==2 && item.endStatus==0}">
				<td class="imp">迟到,缺卡</td>
				</c:if>
				<c:if test="${item.startStatus==2 && item.endStatus==1}">
				<td class="imp">迟到</td>
				</c:if>
				<c:if test="${item.startStatus==2 && item.endStatus==2}">
				<td class="imp">迟到,早退</td>
				</c:if>
				<td>${item.startWorkTime}</td>
				<c:if test="${item.startStatus==0}">
				<td class="imp">缺卡</td>
				</c:if>
				<c:if test="${item.startStatus==1}">
				<td  title="签到详情"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content='<c:if test="${item.startOut==0}">签到外勤:本地</c:if>
		            							<c:if test="${item.startOut==1}">签到外勤:外勤<br>外勤理由：${item.startOutReason}</c:if><br>签到地点:${item.startSite}'>正常</td>
				</c:if>
				<c:if test="${item.startStatus==2}">
				<td class="imp" title="签到详情"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content='<c:if test="${item.startOut==0}">签到外勤:本地</c:if>
		            							<c:if test="${item.startOut==1}">签到外勤:外勤<br>外勤理由：${item.startOutReason}</c:if><br>签到地点:${item.startSite}'>迟到</td>
				</c:if> 
				
				<td>${item.endWorkTime}</td>
				<c:if test="${item.endStatus==0}">
				<td class="imp">缺卡</td>
				</c:if>
				<c:if test="${item.endStatus==1}">
				<td  title="签退详情"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content='<c:if test="${item.endOut==0}">签退状态:本地</c:if>
		            							<c:if test="${item.endOut==1}">签退状态:外勤<br>外勤理由：${item.endOutReason}</c:if><br>签退地点:${item.endSite}'>正常</td>
				</c:if>
				<c:if test="${item.endStatus==2}">
				<td class="imp" title="签退详情"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content='<c:if test="${item.endOut==0}">签退状态:本地</c:if>
		            							<c:if test="${item.endOut==1}">签退状态:外勤<br>外勤理由：${item.endOutReason}</c:if><br>签退地点:${item.endSite}'>早退</td>
				</c:if> 
				
				<c:if test="${!empty item.startPhoto || !empty item.endPhoto}">
					<c:if test="${!empty item.startPhoto && empty item.endPhoto}">
					<td title="异常信息"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content="代签人：<tags:user userId="${item.startPhoto.exId}" /><br>代签手机：${item.startPhoto.phoneName}"><a class="btn btn-default btn-sm processImg" dataHref="../store/store-file-view.do?key=${item.startPhoto.photoPath}" >预览</a> </td>
					</c:if>
					<c:if test="${empty item.startPhoto && !empty item.endPhoto}">
					<td title="异常信息"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content="代签人：<tags:user userId="${item.endPhoto.exId}" /><br>代签手机：${item.endPhoto.phoneName}"><a class="btn btn-default btn-sm processImg" dataHref="../store/store-file-view.do?key=${item.endPhoto.photoPath}">预览</a> </td>
					</c:if>
					<c:if test="${!empty item.startPhoto && !empty item.endPhoto}">
					<td title="异常信息"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content="签到代签人：<tags:user userId="${item.startPhoto.exId}" /><br>代签手机：${item.startPhoto.phoneName}<br> 签退代签人：<tags:user userId="${item.endPhoto.exId}" /><br>代签手机：${item.endPhoto.phoneName}"> <a class="btn btn-default btn-sm processImg" dataHref="../store/store-file-view.do?key=${item.startPhoto.photoPath}" >预览</a> &nbsp;&nbsp;
					<a class="btn btn-default btn-sm processImg" dataHref="../store/store-file-view.do?key=${item.endPhoto.photoPath}" >预览</a> </td>
					</c:if>
				</c:if>
				
				
				<c:if test="${empty item.startPhoto && empty item.endPhoto}">
				<td>无</td>
				</c:if>
				
		</c:if>
		<c:if test="${item.choose == 1}">
				<td>请假(非全天)</td>
				<td>${item.startWorkTime}</td>
				<c:if test="${item.startStatus==0}">
				<td class="imp">缺卡</td>
				</c:if>
				<c:if test="${item.startStatus==1}">
				<td  title="签到详情"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content='<c:if test="${item.startOut==0}">签到外勤:本地</c:if>
		            							<c:if test="${item.startOut==1}">签到外勤:外勤<br>外勤理由：${item.startOutReason}</c:if><br>签到地点:${item.startSite}'>正常</td>
				</c:if>
				<c:if test="${item.startStatus==2}">
				<td class="imp" title="签到详情"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content='<c:if test="${item.startOut==0}">签到外勤:本地</c:if>
		            							<c:if test="${item.startOut==1}">签到外勤:外勤<br>外勤理由：${item.startOutReason}</c:if><br>签到地点:${item.startSite}'>迟到</td>
				</c:if> 
				
				<td>${item.endWorkTime}</td>
				<c:if test="${item.endStatus==0}">
				<td class="imp">缺卡</td>
				</c:if>
				<c:if test="${item.endStatus==1}">
				<td  title="签退详情"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content='<c:if test="${item.endOut==0}">签退状态:本地</c:if>
		            							<c:if test="${item.endOut==1}">签退状态:外勤<br>外勤理由：${item.endOutReason}</c:if><br>签退地点:${item.endSite}'>正常</td>
				</c:if>
				<c:if test="${item.endStatus==2}">
				<td class="imp" title="签退详情"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content='<c:if test="${item.endOut==0}">签退状态:本地</c:if>
		            							<c:if test="${item.endOut==1}">签退状态:外勤<br>外勤理由：${item.endOutReason}</c:if><br>签退地点:${item.endSite}'>早退</td>
				</c:if>
				
				<c:if test="${!empty item.startPhoto || !empty item.endPhoto}">
					<c:if test="${!empty item.startPhoto && empty item.endPhoto}">
					<td title="异常信息"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content="代签人：<tags:user userId="${item.startPhoto.exId}" /><br>代签手机：${item.startPhoto.phoneName}"><a class="btn btn-default btn-sm processImg" dataHref="../store/store-file-view.do?key=${item.startPhoto.photoPath}" >预览</a> </td>
					</c:if>
					<c:if test="${empty item.startPhoto && !empty item.endPhoto}">
					<td title="异常信息"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content="代签人：<tags:user userId="${item.endPhoto.exId}" /><br>代签手机：${item.endPhoto.phoneName}"><a class="btn btn-default btn-sm processImg" dataHref="../store/store-file-view.do?key=${item.endPhoto.photoPath}">预览</a> </td>
					</c:if>
					<c:if test="${!empty item.startPhoto && !empty item.endPhoto}">
					<td title="异常信息"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content="签到代签人：<tags:user userId="${item.startPhoto.exId}" /><br>代签手机：${item.startPhoto.phoneName}<br> 签退代签人：<tags:user userId="${item.endPhoto.exId}" /><br>代签手机：${item.endPhoto.phoneName}"> <a class="btn btn-default btn-sm processImg" dataHref="../store/store-file-view.do?key=${item.startPhoto.photoPath}" >预览</a> &nbsp;&nbsp;
					<a class="btn btn-default btn-sm processImg" dataHref="../store/store-file-view.do?key=${item.endPhoto.photoPath}" >预览</a> </td>
					</c:if>
				</c:if>
				<c:if test="${empty item.startPhoto && empty item.endPhoto}">
				<td>无</td>
				</c:if>
		</c:if>
		<c:if test="${item.choose == 2}">
				<td class="imp">旷工</td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
		</c:if>
		<c:if test="${item.choose == 3}">
				<td><a href="../domap/map-mapInfo-leave.do?userId=${item.userId}&currentTime=${item.currentTime}" >请假</a></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
		</c:if>
		<c:if test="${item.choose == 4}">
				<td><a href="../domap/map-mapInfo-out.do?userId=${item.userId}&currentTime=${item.currentTime}" >出差</a></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
		</c:if>
		<c:if test="${item.choose == 5}">
				<td>加班</td>
				<td>${item.startWorkTime}</td>
				<td  title="签到详情"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content='<c:if test="${item.startOut==0}">签到外勤:本地</c:if>
		            							<c:if test="${item.startOut==1}">签到外勤:外勤<br>外勤理由：${item.startOutReason}</c:if><br>签到地点:${item.startSite}'>正常</td>
				<c:if test="${!empty item.endWorkTime}">
				<td>${item.endWorkTime}</td>
				<td title="签退详情"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content='<c:if test="${item.endOut==0}">签退状态:本地</c:if>
		            							<c:if test="${item.endOut==1}">签退状态:外勤<br>外勤理由：${item.endOutReason}</c:if><br>签退地点:${item.endSite}'>正常</td>
				</c:if>
				<c:if test="${empty item.endWorkTime}">
				<td></td>
				<td></td>
				</c:if>
				
				<c:if test="${!empty item.startPhoto || !empty item.endPhoto}">
					<c:if test="${!empty item.startPhoto && empty item.endPhoto}">
					<td title="异常信息"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content="代签人：<tags:user userId="${item.startPhoto.exId}" /><br>代签手机：${item.startPhoto.phoneName}"><a class="btn btn-default btn-sm processImg" dataHref="../store/store-file-view.do?key=${item.startPhoto.photoPath}" >预览</a> </td>
					</c:if>
					<c:if test="${empty item.startPhoto && !empty item.endPhoto}">
					<td title="异常信息"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content="代签人：<tags:user userId="${item.endPhoto.exId}" /><br>代签手机：${item.endPhoto.phoneName}"><a class="btn btn-default btn-sm processImg" dataHref="../store/store-file-view.do?key=${item.endPhoto.photoPath}">预览</a> </td>
					</c:if>
					<c:if test="${!empty item.startPhoto && !empty item.endPhoto}">
					<td title="异常信息"data-container="body" data-toggle="popover" data-placement="top" data-trigger="hover" data-html="true"
		            							data-content="签到代签人：<tags:user userId="${item.startPhoto.exId}" /><br>代签手机：${item.startPhoto.phoneName}<br> 签退代签人：<tags:user userId="${item.endPhoto.exId}" /><br>代签手机：${item.endPhoto.phoneName}"> <a class="btn btn-default btn-sm processImg" dataHref="../store/store-file-view.do?key=${item.startPhoto.photoPath}" >预览</a> &nbsp;&nbsp;
					<a class="btn btn-default btn-sm processImg" dataHref="../store/store-file-view.do?key=${item.endPhoto.photoPath}" >预览</a> </td>
					</c:if>
				</c:if>
				<c:if test="${empty item.startPhoto && empty item.endPhoto}">
				<td>无</td>
				</c:if>
				
		</c:if>
		<c:if test="${item.choose == 6}">
				<td>休息</td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
				<td></td>
		</c:if>
		<td>${item.allTime}</td>
		
        <td>
          <%-- <a href="cms-article-publish.do?id=${item.id}">发布</a> 
          <a href="cms-article-image.do?id=${item.id}" class="a-update"><spring:message code="core.list.edit" text="编辑"/></a>--%>
        </td>
      </tr>
      </c:forEach>
    </tbody>
  </table>


      </div>
</form>

	  <div>
	    <div class="m-page-info pull-left">
		  共100条记录 显示1到10条记录
		</div>

		<div class="btn-group m-pagination pull-right">
		  <button class="btn btn-default">&lt;</button>
		  <button class="btn btn-default">1</button>
		  <button class="btn btn-default">&gt;</button>
		</div>

	    <div class="clearfix"></div>
      </div>

      <div class="m-spacer"></div>

<%@include file="/header/mainFooter.jsp" %>
    <div id="watchFile">
		<div class="tips">
			<p><span style="font-size: 16px;color: red;">温馨提示</span><br/>点击两侧透明处可关闭预览</p>
			<img alt="" src="${tenantPrefix}/s/spig.png">
		</div>
		<iframe class="watchFileCon" src=""  width="90%" height="80%"></iframe>
	</div>
	
  </body>
  <script>
$(function(){

	var moveTips = $('#watchFile .tips')[0];
		moveTips.onmousedown = function(ev){
			var oevent = ev || event;
			var distanceX = oevent.clientX - moveTips.offsetLeft;
			var distanceY = oevent.clientY - moveTips.offsetTop;
			document.onmousemove = function(ev){
				var oevent = ev || event;
				moveTips.style.left = oevent.clientX - distanceX + 'px';
				moveTips.style.top = oevent.clientY - distanceY + 'px';
				return false;
			};
			document.onmouseup = function(){
				document.onmousemove = null;
				document.onmouseup = null;
				return false;
			};
			return false;
		};
		$(document).on('click','.processImg',function(){
			var dataHref = $(this).attr('dataHref');
			if(dataHref){
				if($('#watchFile').length > 0){
					$('#watchFile .watchFileCon').attr('src',dataHref);
					$('#watchFile').show();
				} else{
					window.open(dataHref);
				}	
			}
		})
		$(document).on('click','#watchFile',function(e){
			if($('.watchFileCon',this).length > 0 && $(e.target).attr('id') === 'watchFile'){
				$('#watchFile .watchFileCon').attr('src','');
				$('#watchFile').hide();
			} else{
				
			}
		})


})
			
    </script>
</html>

