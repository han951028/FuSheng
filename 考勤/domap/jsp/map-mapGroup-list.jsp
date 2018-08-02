<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="/taglibs.jsp"%>
<%
	pageContext.setAttribute("currentHeader", "domap");
%>
<%
	pageContext.setAttribute("currentMenu", "domap");
%>
<%
	pageContext.setAttribute("currentMenuId", "718");
%>
<!doctype html>
<html lang="en">

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
       	'filter_LIKES_groupName': '${param.filter_LIKES_groupName}'         
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
	$(".del").click(function(){
		return confirm("是否确认删除");
		if(confirm("是否确认删除")){
			submit_function();
		}else{
			return false;
		}
	})
})
    </script>
  </head>

   <body class="page-body">
     <%@include file="/header/mainHeader.jsp" %>


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
	   <div>
	    <div class="pull-left btn-group" role="group">
		  <button class="btn btn-success btn-sm" onclick="location.href='map-mapGroup-input.do'"><i class="fa-plus">&nbsp;&nbsp;新&nbsp;建</i> </button>
		  
		  <!--
		<button class="btn btn-default a-export" onclick="table.exportExcel()">导出</button>
		  -->
		</div>

	    <div class="clearfix"></div>
	  </div>

<form id="cms-articleGridForm" name="cms-articleGridForm" method='post' action="" class="m-form-blank">
      <div class="panel panel-default">
        <div class="panel-heading">
		  <i class="glyphicon glyphicon-list"></i>
		  <spring:message code="scope-info.scope-info.list.title" text="列表"/>
		</div>


  <table id="cmsArticleGrid" class="table table-striped table-hover table-bordered dataTable"> 
    <thead>
      <tr>
     <!--    <th width="10" class="table-check"><input type="checkbox" name="checkAll" onchange="toggleSelectedItems(this.checked)"></th> -->
        <th class="sorting"  width="10%">考勤组名称</th>
        <th class="sorting" width="100px">办公地点</th>
        <th class="sorting" width="100px">考勤日期</th>
        <th class="sorting"  width="120px">上班时间</th>
        <th class="sorting"  width="120px">下班时间</th>
        <th class="sorting"  width="120px">加班开始时间</th>
         <th width="100">&nbsp;</th>
      </tr>
    </thead>

    <tbody>
      <c:forEach items="${page.result}" var="item">
      <tr>
        <td>${item.groupName}</td>
		<td>${item.centerSite}</td>
		<td>${item.groupWeek}</td>
		<td>${item.startTime}</td>
		<td>${item.endTime}</td>
		<td>${item.overTime}</td>
        <td>
          	<a href="map-mapGroup-info.do?groupId=${item.groupId}">修改</a>
         	<a class="del" href="map-mapGroup-delete.do?groupId=${item.groupId} ">删除</a> 
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

  </body>

</html>

