package com.mossle.domap.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.hsqldb.lib.StringUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mossle.Constants;
import com.mossle.api.store.StoreConnector;
import com.mossle.api.store.StoreDTO;
import com.mossle.api.tenant.TenantHolder;
import com.mossle.api.user.UserConnector;
import com.mossle.api.user.UserDTO;
import com.mossle.api.userauth.UserAuthConnector;
import com.mossle.api.userauth.UserAuthDTO;
import com.mossle.core.auth.CurrentUserHolder;
import com.mossle.core.export.Exportor;
import com.mossle.core.export.TableModel;
import com.mossle.core.page.Page;
import com.mossle.core.query.PropertyFilter;
import com.mossle.core.spring.MessageHelper;
import com.mossle.core.store.MultipartFileDataSource;
import com.mossle.core.util.StringUtils;
import com.mossle.domap.Util.MapDataDto;
import com.mossle.domap.domain.MapException;
import com.mossle.domap.domain.MapGroup;
import com.mossle.domap.domain.MapHolidays;
import com.mossle.domap.domain.MapInfo;
import com.mossle.domap.domain.MapUser;
import com.mossle.domap.manager.MapGroupManager;
import com.mossle.domap.manager.MapHolidaysManager;
import com.mossle.domap.manager.MapInfoManager;
import com.mossle.domap.service.MapDaoService;
import com.mossle.domap.service.MapService;
import com.mossle.employee.persistence.domain.EmployeeLeaveInfo;
import com.mossle.internal.store.persistence.domain.StoreInfo;
import com.mossle.internal.store.persistence.manager.StoreInfoManager;
import com.mossle.org.persistence.domain.OrgDepartment;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("domap")
public class MapPCController {

	private MapService mapService;
	private MapDaoService mapDaoService;
	private JdbcTemplate jdbcTemplate;
	private MapGroupManager mapGroupManager;
	private MapInfoManager mapInfoManager;
	private MessageHelper messageHelper;
	private MapHolidaysManager mapHolidaysManager;
	private Exportor exportor;
	private TenantHolder tenantHolder;
	private CurrentUserHolder currentUserHolder;
	private UserAuthConnector userAuthConnector;
	private UserConnector userConnector;
	private StoreInfoManager storeInfoManager;

	@RequestMapping("map-mapInfo-list")
	public String getDomapInfoList(@ModelAttribute Page page, @RequestParam Map<String, Object> parameterMap,
			Model model) {
		String startDateStr = (String) parameterMap.get("filter_EQS_startCurrentTime");
		String endDateStr = (String) parameterMap.get("filter_EQS_endCurrentTime");
		String userName = (String) parameterMap.get("filter_LIKES_userName");
		String groupName = (String) parameterMap.get("filter_LIKES_groupName");
		String departmentId = (String) parameterMap.get("filter_EQS_departmentName");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateNow = sdf.format(new Date());
		String hql = "from MapInfo where currentTime != '" + dateNow + "' ";
//		String hql = "from MapInfo where 1=1 ";
		if (startDateStr != null && !"".equals(startDateStr)) {
			hql = hql + " and currentTime >= '" + startDateStr + "'";
		}
		if (endDateStr != null && !"".equals(endDateStr)) {
			hql = hql + " and currentTime <= '" + endDateStr + "'";
		}
		if (userName != null && !"".equals(userName)) {
			hql = hql + " and  userName like '%" + userName + "%'";
		}
		if (groupName != null && !"".equals(groupName)) {
			hql = hql + " and  groupName like '%" + groupName + "%'";
		}
		if (departmentId != null && !"".equals(departmentId)) {
			hql = hql + " and  departmentId = " + departmentId;
		}

		hql = hql + " order by groupName,departmentId,userId,currentTime desc";
		page = mapInfoManager.pagedQuery(hql, page.getPageNo(), page.getPageSize());

		List<MapInfo> mapInfoList = (List<MapInfo>) page.getResult();
		List<MapInfo> ListRet = new ArrayList<>();
		for (MapInfo mapInfo : mapInfoList) {
			if (mapInfo.getStartPhoto() != null) {
				MapException mapException = new MapException();
				mapException = mapDaoService.getMapExceptionBy(mapInfo.getStartPhoto().getId());
				mapInfo.setStartPhoto(mapException);
			}
			if (mapInfo.getEndPhoto() != null) {
				MapException mapException = new MapException();
				mapException = mapDaoService.getMapExceptionBy(mapInfo.getEndPhoto().getId());
				mapInfo.setEndPhoto(mapException);
			}
			ListRet.add(mapInfo);
		}
		page.setResult(ListRet);
		model.addAttribute("page", page);
		return "domap/map-mapInfo-list";
	}

	@RequestMapping("map-mapInfoPer-list")
	public String getDomapInfoById(@ModelAttribute Page page, @RequestParam Map<String, Object> parameterMap,
			Model model) {
		String startDateStr = (String) parameterMap.get("filter_EQS_startCurrentTime");
		String endDateStr = (String) parameterMap.get("filter_EQS_endCurrentTime");
		String userName = (String) parameterMap.get("filter_LIKES_userName");
		String groupName = (String) parameterMap.get("filter_LIKES_groupName");
		String departmentId = (String) parameterMap.get("filter_EQS_departmentName");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateNow = sdf.format(new Date());
		String tenantId = tenantHolder.getTenantId();
		String date = "";
		String user = currentUserHolder.getUserId();
		UserDTO userDto = userConnector.findById(user);
		// 1 可通过 名字 及部门 及时间 2 可通过时间 3通过名字 及时间
		String type = "";
		// 权限判断
		UserAuthDTO userAuthDTO = userAuthConnector.findById(user, tenantId);
		if (userAuthDTO.isAdmin()
				|| Integer.valueOf(userDto.getPositionName()) >= Integer.valueOf(Constants.LEVEL_90)) {
			// 管理员 主任
			type = "1";
		} else {
			// 副主任以下
			if (userAuthDTO.getDepartmentNames() == null || userAuthDTO.getDepartmentNames().size() == 0
					|| userDto.getPositionName().equals(Constants.LEVEL_10)) {
				// 无所属部门 职员
				type = "2";
			} else {
				type = "3";

			}
		}
		String hql = "from MapInfo where currentTime != '" + dateNow + "' ";
//		String hql = "from MapInfo where 1=1 ";
		if (startDateStr != null && !"".equals(startDateStr)) {
			hql = hql + " and currentTime >= '" + startDateStr + "'";
		}
		if (endDateStr != null && !"".equals(endDateStr)) {
			hql = hql + " and currentTime <= '" + endDateStr + "'";
		}
		if (userName != null && !"".equals(userName)) {
			hql = hql + " and  userName like '%" + userName + "%'";
		}
		if (groupName != null && !"".equals(groupName)) {
			hql = hql + " and  groupName like '%" + groupName + "%'";
		}
		if (departmentId != null && !"".equals(departmentId)) {
			hql = hql + " and  departmentId = " + departmentId;
		}
		if (type == "2") {
			hql = hql + " and userId = " + user;
		}
		if (type == "3") {
			String departmentIdByUser = mapDaoService.getDepartmentId(user);
			if (!"".equals(departmentIdByUser)) {
				hql = hql + " and  departmentId = " + departmentIdByUser;
			}
		}
		hql = hql + " order by groupName,departmentId,userId,currentTime  desc";
		page = mapInfoManager.pagedQuery(hql, page.getPageNo(), page.getPageSize());

		List<MapInfo> mapInfoList = (List<MapInfo>) page.getResult();
		List<MapInfo> ListRet = new ArrayList<>();
		for (MapInfo mapInfo : mapInfoList) {
			if (mapInfo.getStartPhoto() != null) {
				MapException mapException = new MapException();
				mapException = mapDaoService.getMapExceptionBy(mapInfo.getStartPhoto().getId());
				mapInfo.setStartPhoto(mapException);
			}
			if (mapInfo.getEndPhoto() != null) {
				MapException mapException = new MapException();
				mapException = mapDaoService.getMapExceptionBy(mapInfo.getEndPhoto().getId());
				mapInfo.setEndPhoto(mapException);
			}
			ListRet.add(mapInfo);
		}
		page.setResult(ListRet);
		model.addAttribute("page", page);
		model.addAttribute("type", type);

		return "domap/map-mapInfoPer-list";
	}

	@RequestMapping("map-mapInfo-leave")
	public String getLeaveMapInfo(@RequestParam Long userId, @RequestParam String currentTime,
			RedirectAttributes redirectAttributes) {
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String human_Task_Id = mapDaoService.getLeaveByDay(userId, sft.parse(currentTime));
			if (human_Task_Id == null || "".equals(human_Task_Id)) {
				messageHelper.addFlashMessage(redirectAttributes, "core.success.update", "暂无请假记录");
				return "redirect:/domap/map-mapInfo-list.do";
			} else {
				redirectAttributes.addAttribute("humanTaskId", human_Task_Id);
				return "redirect:/operation/task-operation-publicViewTaskForm.do";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			messageHelper.addFlashMessage(redirectAttributes, "core.success.update", "暂无请假记录");
			return "redirect:/domap/map-mapInfo-list.do";
		}

	}

	@RequestMapping("map-mapInfo-out")
	public String getOutMapInfo(@RequestParam Long userId, @RequestParam String currentTime,
			RedirectAttributes redirectAttributes) {
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
		try {
			String human_Task_Id = mapDaoService.getOutByDay(userId, sft.parse(currentTime));
			if (human_Task_Id == null || "".equals(human_Task_Id)) {
				messageHelper.addFlashMessage(redirectAttributes, "core.success.update", "暂无出差记录");
				return "redirect:/domap/map-mapInfo-list.do";
			} else {
				redirectAttributes.addAttribute("humanTaskId", human_Task_Id);
				return "redirect:/operation/task-operation-publicViewTaskForm.do";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			messageHelper.addFlashMessage(redirectAttributes, "core.success.update", "暂无出差记录");
			return "redirect:/domap/map-mapInfo-list.do";
		}

	}

	@RequestMapping("map-mapInfo-input")
	public String getDomapInfoInfo(@RequestParam Long id, Model model) {
		List<MapInfo> mapInfoList = mapDaoService.getMapInfoById(id);

		for (MapInfo mapInfo : mapInfoList) {
			model.addAttribute("id", mapInfo.getId());
			model.addAttribute("userId", mapInfo.getUserId());
			model.addAttribute("userName", mapInfo.getUserName());
			model.addAttribute("groupName", mapInfo.getGroupName());
			model.addAttribute("currentTime", mapInfo.getCurrentTime());
			model.addAttribute("departmentId", mapInfo.getDepartmentId());
			model.addAttribute("startTime", mapInfo.getStartTime());
			model.addAttribute("endTime", mapInfo.getEndTime());
			model.addAttribute("overTime", mapInfo.getOverTime());
			model.addAttribute("startWorkTime", mapInfo.getStartWorkTime());
			model.addAttribute("endWorkTime", mapInfo.getEndWorkTime());
			model.addAttribute("startSite", mapInfo.getStartSite());
			model.addAttribute("endSite", mapInfo.getEndSite());
			model.addAttribute("startStatus", mapInfo.getStartStatus());
			model.addAttribute("endStatus", mapInfo.getEndStatus());
			model.addAttribute("startOut", mapInfo.getStartOut());
			model.addAttribute("endOut", mapInfo.getEndOut());
			model.addAttribute("startOutReason", mapInfo.getStartOutReason());
			model.addAttribute("endOutReason", mapInfo.getEndOutReason());
			model.addAttribute("choose", mapInfo.getChoose());
		}
		return "domap/map-mapInfo-input";
	}

	@RequestMapping("map-mapInfo-update")
	public String domapInfoUpdate(@ModelAttribute MapInfo mapInfo, RedirectAttributes redirectAttributes) {
		int choose = mapInfo.getChoose();
		Long userId = mapInfo.getUserId();
		if (choose == 0 || choose == 1) {
			String startTime = mapInfo.getStartTime();
			String endTime = mapInfo.getEndTime();
			String overTime = mapInfo.getOverTime();

			// 不缺卡计算工时和加班时间
			if (mapInfo.getStartStatus() != 0 && mapInfo.getEndStatus() != 0) {
				// 上班时间
				String[] startWorkTimeArr = mapInfo.getStartWorkTime().split(":");
				int startWorkTimeH = Integer.parseInt(startWorkTimeArr[0]);
				int startWorkTimeM = Integer.parseInt(startWorkTimeArr[1]);
				int startWorkTimeSum = startWorkTimeH * 60 + startWorkTimeM;
				// 下班时间
				String[] workTimeArr = mapInfo.getEndWorkTime().split(":");
				int endWorkTimeH = Integer.parseInt(workTimeArr[0]);
				int endWorkTimeM = Integer.parseInt(workTimeArr[1]);
				int endWorkTimeSum = endWorkTimeH * 60 + endWorkTimeM;

				// 全天上班时间
				int allTimeSum = endWorkTimeSum - startWorkTimeSum;
				int allTimeH = allTimeSum / 60;
				int allTimeM = allTimeSum % 60;
				String allTimeHStr = String.valueOf(allTimeH);
				String allTimeMStr = String.valueOf(allTimeM);
				if (allTimeH < 10) {
					allTimeHStr = "0" + allTimeH;
				}
				if (allTimeM < 10) {
					allTimeMStr = "0" + allTimeM;
				}
				String allTimeReturn = "";
				allTimeReturn = allTimeHStr + ":" + allTimeMStr;

				// 获取开始的加班的时间点
				// 返回加班的时间XX:XX
				String overWorkTimeReturn = "";
				if (!"".equals(overTime) && overTime != null) {
					String[] overTimeArr = overTime.split(":");
					int overDateH = Integer.parseInt(overTimeArr[0]);
					int overDateM = Integer.parseInt(overTimeArr[1]);
					int overDateSum = overDateH * 60 + overDateM;
					if (endWorkTimeSum > overDateSum) {
						if (startWorkTimeSum <= overDateSum) {
							int overDateNum = endWorkTimeSum - overDateSum;
							int overDateMinH = overDateNum / 60;
							int overDateMinM = overDateNum % 60;
							String overDateMinHStr = String.valueOf(overDateMinH);
							String overDateMinMStr = String.valueOf(overDateMinM);
							if (overDateMinH < 10) {
								overDateMinHStr = "0" + overDateMinH;
							}
							if (overDateMinM < 10) {
								overDateMinMStr = "0" + overDateMinM;
							}
							overWorkTimeReturn = overDateMinHStr + ":" + overDateMinMStr;
						} else {
							overWorkTimeReturn = allTimeReturn;
						}
					}
				}

				mapInfo.setStartTime(startTime);
				mapInfo.setEndTime(endTime);
				mapInfo.setOverWorkTime(overWorkTimeReturn);
				mapInfo.setAllTime(allTimeReturn);
			} else {
				mapInfo.setStartTime(startTime);
				mapInfo.setEndTime(endTime);
				mapInfo.setOverWorkTime("");
				mapInfo.setAllTime("");
			}
		} else if (choose == 2 || choose == 3 || choose == 4 || choose == 6) {
			mapInfo.setStartWorkTime("");
			mapInfo.setStartOut(0);
			mapInfo.setStartOutReason("");
			mapInfo.setStartStatus(0);
			mapInfo.setEndWorkTime("");
			mapInfo.setEndOut(0);
			mapInfo.setEndOutReason("");
			mapInfo.setEndStatus(0);
			mapInfo.setOverWorkTime("");
			mapInfo.setAllTime("");
		} else if (choose == 5) {
			// 不缺卡时候操作 添加工时和加班时间
			if (!"".equals(mapInfo.getStartWorkTime()) && !"".equals(mapInfo.getEndWorkTime())) {
				// 上班时间
				String[] startWorkTimeArr = mapInfo.getStartWorkTime().split(":");
				int startWorkTimeH = Integer.parseInt(startWorkTimeArr[0]);
				int startWorkTimeM = Integer.parseInt(startWorkTimeArr[1]);
				int startWorkTimeSum = startWorkTimeH * 60 + startWorkTimeM;
				// 下班时间
				String[] workTimeArr = mapInfo.getEndWorkTime().split(":");
				int endWorkTimeH = Integer.parseInt(workTimeArr[0]);
				int endWorkTimeM = Integer.parseInt(workTimeArr[1]);
				int endWorkTimeSum = endWorkTimeH * 60 + endWorkTimeM;
				// 全天上班时间
				int allTimeSum = endWorkTimeSum - startWorkTimeSum;
				int allTimeH = allTimeSum / 60;
				int allTimeM = allTimeSum % 60;
				String allTimeHStr = String.valueOf(allTimeH);
				String allTimeMStr = String.valueOf(allTimeM);
				if (allTimeH < 10) {
					allTimeHStr = "0" + allTimeH;
				}
				if (allTimeM < 10) {
					allTimeMStr = "0" + allTimeM;
				}
				String allTimeReturn = "";
				allTimeReturn = allTimeHStr + ":" + allTimeMStr;

				// 加班时间操作
				String overWorkTimeReturn = "";
				String overTime = mapInfo.getOverTime();
				if ("".equals(overTime)) {
					mapInfo.setOverTime(overWorkTimeReturn);
				} else {
					mapInfo.setOverTime(allTimeReturn);
				}
				mapInfo.setAllTime(allTimeReturn);
			} else {
				mapInfo.setOverWorkTime("");
				mapInfo.setAllTime("");
			}
		}
		mapDaoService.updateMapInfoById(mapInfo);
		messageHelper.addFlashMessage(redirectAttributes, "core.success.update", "操作成功");
		return "redirect:/domap/map-mapInfo-list.do";
	}

	@RequestMapping("map-mapInfo-export")
	public void getDomapInfoExport(@ModelAttribute Page page, @RequestParam Map<String, Object> parameterMap,
			HttpServletRequest request, HttpServletResponse response) {
		String startDateStr = (String) parameterMap.get("filter_EQS_startCurrentTime");
		String endDateStr = (String) parameterMap.get("filter_EQS_endCurrentTime");
		String userName = (String) parameterMap.get("filter_LIKES_userName");
		String groupName = (String) parameterMap.get("filter_LIKES_groupName");
		String departmentId = (String) parameterMap.get("filter_EQS_departmentName");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateNow = sdf.format(new Date());
		String hql = "from MapInfo where currentTime != '" + dateNow + "' ";
//		String hql = "from MapInfo where 1=1";
		if (startDateStr != null && !"".equals(startDateStr)) {
			hql = hql + " and currentTime >= '" + startDateStr + "'";
		}
		if (endDateStr != null && !"".equals(endDateStr)) {
			hql = hql + " and currentTime <= '" + endDateStr + "'";
		}
		if (userName != null && !"".equals(userName)) {
			hql = hql + " and  userName like '%" + userName + "%'";
		}
		if (groupName != null && !"".equals(groupName)) {
			hql = hql + " and  groupName like '%" + groupName + "%'";
		}
		if (departmentId != null && !"".equals(departmentId)) {
			hql = hql + " and  departmentId = " + departmentId;
		}
		hql = hql + " order by groupName,departmentId,userId,currentTime  desc";
		List<MapInfo> mapInfoList = mapInfoManager.find(hql);
		List<MapDataDto> mapDataDtoList = new ArrayList<MapDataDto>();
		// 获取用户所在的部门名称
		List<OrgDepartment> departmentList = mapDaoService.getAllDepartment();
		Map<String, String> departMentMap = new HashMap<>();
		for (OrgDepartment orgDepartment : departmentList) {
			departMentMap.put(orgDepartment.getId().toString(), orgDepartment.getName());
		}

		for (MapInfo mapInfo : mapInfoList) {
			MapDataDto dto = new MapDataDto();
			dto.setUserName(mapInfo.getUserName());
			dto.setGroupName(mapInfo.getGroupName());
			dto.setCurrentTime(mapService.getSftYMD(mapInfo.getCurrentTime()));
			if (departMentMap.containsKey(mapInfo.getDepartmentId())) {
				dto.setDepartmentName(departMentMap.get(mapInfo.getDepartmentId()));
			} else {
				dto.setDepartmentName("");
			}

			int chooseR = mapInfo.getChoose();
			if (chooseR == 0) {
				dto.setStartTime(mapInfo.getStartTime());
				dto.setStartWorkTime(mapInfo.getStartWorkTime());
				dto.setStartSite(mapInfo.getStartSite());
				if (mapInfo.getStartOut() == 0) {
					dto.setStartOut("本地");
					dto.setStartOutReason("");
				} else if (mapInfo.getStartOut() == 1) {
					dto.setStartOut("外勤");
					dto.setStartOutReason(mapInfo.getStartOutReason());
				}
				dto.setEndTime(mapInfo.getEndTime());
				dto.setEndWorkTime(mapInfo.getEndWorkTime());
				dto.setEndSite(mapInfo.getEndSite());
				if (mapInfo.getEndOut() == 0) {
					dto.setEndOut("本地");
					dto.setEndOutReason("");
				} else if (mapInfo.getEndOut() == 1) {
					dto.setEndOut("外勤");
					dto.setEndOutReason(mapInfo.getEndOutReason());
				}
				if (mapInfo.getStartStatus() == 0 && mapInfo.getEndStatus() == 0) {
					dto.setChoose("缺卡  缺卡");
					dto.setStartChoose("缺卡");
					dto.setEndChoose("缺卡");
				} else if (mapInfo.getStartStatus() == 0 && mapInfo.getEndStatus() == 1) {
					dto.setChoose("缺卡");
					dto.setStartChoose("缺卡");
					dto.setEndChoose("正常");
				} else if (mapInfo.getStartStatus() == 0 && mapInfo.getEndStatus() == 2) {
					dto.setChoose("缺卡  早退");
					dto.setStartChoose("缺卡");
					dto.setEndChoose("早退");
				} else if (mapInfo.getStartStatus() == 1 && mapInfo.getEndStatus() == 0) {
					dto.setChoose("缺卡");
					dto.setStartChoose("");
					dto.setEndChoose("缺卡");
				} else if (mapInfo.getStartStatus() == 1 && mapInfo.getEndStatus() == 1) {
					dto.setChoose("正常");
					dto.setStartChoose("正常");
					dto.setEndChoose("正常");
				} else if (mapInfo.getStartStatus() == 1 && mapInfo.getEndStatus() == 2) {
					dto.setChoose("早退");
					dto.setStartChoose("正常");
					dto.setEndChoose("早退");
				} else if (mapInfo.getStartStatus() == 2 && mapInfo.getEndStatus() == 0) {
					dto.setChoose("迟到  缺卡");
					dto.setStartChoose("迟到");
					dto.setEndChoose("缺卡");
				} else if (mapInfo.getStartStatus() == 2 && mapInfo.getEndStatus() == 1) {
					dto.setChoose("迟到");
					dto.setStartChoose("迟到");
					dto.setEndChoose("正常");
				} else if (mapInfo.getStartStatus() == 2 && mapInfo.getEndStatus() == 2) {
					dto.setChoose("迟到  早退");
					dto.setStartChoose("迟到");
					dto.setEndChoose("早退");
				}
				dto.setAllTime(mapInfo.getAllTime());
				dto.setOverWorkTime(mapInfo.getOverWorkTime());
			} else if (chooseR == 1) {
				dto.setStartTime(mapInfo.getStartTime());
				dto.setStartWorkTime(mapInfo.getStartWorkTime());
				dto.setStartSite(mapInfo.getStartSite());
				if (mapInfo.getStartOut() == 0) {
					dto.setStartOut("本地");
					dto.setStartOutReason("");
				} else if (mapInfo.getStartOut() == 1) {
					dto.setStartOut("外勤");
					dto.setStartOutReason(mapInfo.getStartOutReason());
				}
				dto.setEndTime(mapInfo.getEndTime());
				dto.setEndWorkTime(mapInfo.getEndWorkTime());
				dto.setEndSite(mapInfo.getEndSite());
				if (mapInfo.getEndOut() == 0) {
					dto.setEndOut("本地");
					dto.setEndOutReason("");
				} else if (mapInfo.getEndOut() == 1) {
					dto.setEndOut("外勤");
					dto.setEndOutReason(mapInfo.getEndOutReason());
				}
				dto.setChoose("请假(非全天)");
				if (mapInfo.getStartStatus() == 0) {
					dto.setStartChoose("缺卡");
				} else if (mapInfo.getStartStatus() == 1) {
					dto.setStartChoose("正常");
				} else if (mapInfo.getStartStatus() == 2) {
					dto.setStartChoose("迟到");
				}
				if (mapInfo.getEndStatus() == 0) {
					dto.setEndChoose("缺卡");
				} else if (mapInfo.getEndStatus() == 1) {
					dto.setEndChoose("正常");
				} else if (mapInfo.getEndStatus() == 2) {
					dto.setEndChoose("早退");
				}
				dto.setStartChoose("");
				dto.setEndChoose("");
				dto.setAllTime(mapInfo.getAllTime());
				dto.setOverWorkTime(mapInfo.getOverWorkTime());
			} else if (chooseR == 2) {
				dto.setStartTime("");
				dto.setStartWorkTime("");
				dto.setStartSite("");
				dto.setStartOut("");
				dto.setStartOutReason("");
				dto.setEndTime("");
				dto.setEndWorkTime("");
				dto.setEndSite("");
				dto.setEndOut("");
				dto.setEndOutReason("");
				dto.setChoose("旷工");
				dto.setAllTime("");
				dto.setOverWorkTime("");
				dto.setStartChoose("");
				dto.setEndChoose("");
			} else if (chooseR == 3) {
				dto.setStartTime("");
				dto.setStartWorkTime("");
				dto.setStartSite("");
				dto.setStartOut("");
				dto.setStartOutReason("");
				dto.setEndTime("");
				dto.setEndWorkTime("");
				dto.setEndSite("");
				dto.setEndOut("");
				dto.setEndOutReason("");
				dto.setChoose("请假");
				dto.setAllTime("");
				dto.setOverWorkTime("");
				dto.setStartChoose("");
				dto.setEndChoose("");
			} else if (chooseR == 4) {
				dto.setStartTime("");
				dto.setStartWorkTime("");
				dto.setStartSite("");
				dto.setStartOut("");
				dto.setStartOutReason("");
				dto.setEndTime("");
				dto.setEndWorkTime("");
				dto.setEndSite("");
				dto.setEndOut("");
				dto.setEndOutReason("");
				dto.setChoose("出差");
				dto.setAllTime("");
				dto.setOverWorkTime("");
				dto.setStartChoose("");
				dto.setEndChoose("");
			} else if (chooseR == 5) {
				dto.setStartTime("");
				dto.setStartWorkTime(mapInfo.getStartWorkTime());
				dto.setStartSite(mapInfo.getStartSite());
				if (mapInfo.getStartOut() == 0) {
					dto.setStartOut("本地");
					dto.setStartOutReason("");
				} else if (mapInfo.getStartOut() == 1) {
					dto.setStartOut("外勤");
					dto.setStartOutReason(mapInfo.getStartOutReason());
				}
				dto.setStartChoose("正常");
				dto.setEndTime("");
				if(!"".equals(mapInfo.getEndWorkTime())){
					dto.setEndWorkTime(mapInfo.getEndWorkTime());
					dto.setEndSite(mapInfo.getEndSite());
					if (mapInfo.getEndOut() == 0) {
						dto.setEndOut("本地");
						dto.setEndOutReason("");
					} else if (mapInfo.getEndOut() == 1) {
						dto.setEndOut("外勤");
						dto.setEndOutReason(mapInfo.getEndOutReason());
					}
					dto.setEndChoose("正常");
					
				}else{
					dto.setEndWorkTime("");
					dto.setEndOut("");
					dto.setEndOutReason("");
					dto.setEndChoose("");
				}
				dto.setChoose("休息日加班");
				dto.setAllTime(mapInfo.getAllTime());
				dto.setOverWorkTime(mapInfo.getOverWorkTime());
			} else if (chooseR == 6) {
				dto.setStartTime("");
				dto.setStartWorkTime("");
				dto.setStartSite("");
				dto.setStartOut("");
				dto.setStartOutReason("");
				dto.setEndTime("");
				dto.setEndWorkTime("");
				dto.setEndSite("");
				dto.setEndOut("");
				dto.setEndOutReason("");
				dto.setChoose("休息");
				dto.setAllTime("");
				dto.setOverWorkTime("");
				dto.setStartChoose("");
				dto.setEndChoose("");
			}
			mapDataDtoList.add(dto);
		}
		TableModel tableModel = new TableModel();
		tableModel.setName("考勤表");
		tableModel.addViewNmaes("时间", "姓名", "部门", "所属规则", "状态", "上班时间", "签到时间", "签到地点", "签到外勤", "外勤理由", "签到状态", "下班时间",
				"签退时间", "签退地点", "签退外勤", "外勤理由", "签退状态", "工时", "加班时间");
		tableModel.addHeaders("currentTime", "userName", "departmentName", "groupName", "choose", "startTime",
				"startWorkTime", "startSite", "startOut", "startOutReason", "startChoose", "endTime", "endWorkTime",
				"endSite", "endOut", "endOutReason", "endChoose", "allTime", "overWorkTime");
		tableModel.setData(mapDataDtoList);
		try {
			exportor.export(request, response, tableModel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 分组操作
	@RequestMapping("map-mapGroup-list")
	public String MapGroupList(@ModelAttribute Page page, Model model) {
		List<MapGroup> groupList = mapDaoService.getALLMapGroup();
		String hql = "from MapGroup";
		page = mapGroupManager.pagedQuery(hql, page.getPageNo(), page.getPageSize());
		model.addAttribute("page", page);
		return "domap/map-mapGroup-list";
	}

	@RequestMapping("map-mapGroup-input")
	public String MapGroupInput(Model model) {
		//每个人对应一个考勤组接口
//		List<MapUser> userList = mapDaoService.getAllMapUserInfo();
//		String userId ="";
//		for (MapUser mapUser : userList) {
//			if(!"".equals(userId)){
//				userId = userId+","+mapUser.getUserId();
//			}else{
//				userId=mapUser.getUserId().toString();
//			}
//		}
//		model.addAttribute("otherUserId",userId);
		return "domap/map-mapGroup-input";
	}

	@RequestMapping("map-mapGroup-info")
	public String MapGroupinfo(Long groupId, Model model) {
		List<MapGroup> groupList = mapDaoService.getMapGroupById(groupId);
		for (MapGroup mapGroup : groupList) {
			model.addAttribute("groupId", mapGroup.getGroupId());
			model.addAttribute("groupName", mapGroup.getGroupName());
			model.addAttribute("groupConter", mapGroup.getGroupConter());
			model.addAttribute("centerSite", mapGroup.getCenterSite());
			model.addAttribute("radius", mapGroup.getRadius());
			model.addAttribute("groupWeek", mapGroup.getGroupWeek());
			model.addAttribute("startTime", mapGroup.getStartTime());
			model.addAttribute("endTime", mapGroup.getEndTime());
			model.addAttribute("overTime", mapGroup.getOverTime());
			model.addAttribute("alertTime", mapGroup.getAlertTime());
		}
		// 获取本考勤组对应的用户ID
		List<MapUser> userList = mapDaoService.getMapUserInfo(groupId);
		String userId = "";
		for (MapUser mapUser : userList) {
			userId = userId + "," + mapUser.getUserId();
		}
		String userIdReturn = "";
		if (!"".equals(userId)) {
			userIdReturn = userId.substring(1, userId.length());
		}
		model.addAttribute("userId", userIdReturn);
		
		
		//每个人对应一个考勤组接口
		// 获取除去本考勤组之外其他的用户ID
//		List<MapUser> otherUserList = mapDaoService.getOtherMapUserInfo(groupId);
//		String otherUserId = "";
//		for (MapUser mapUser : otherUserList) {
//			if(!"".equals(otherUserId)){
//				otherUserId = otherUserId+","+mapUser.getUserId();
//			}else{
//				otherUserId = mapUser.getUserId().toString();
//			}
//			otherUserId = otherUserId + "," + mapUser.getUserId();
//		}
//		model.addAttribute("otherUserId", otherUserId);

		return "domap/map-mapGroup-input";
	}

	@RequestMapping("map-mapGroup-save")
	public String MapGroupinfoSave(MapGroup mapGroup, String[] groupWeek, String userId,
			RedirectAttributes redirectAttributes) {
		// 以下为计算坐标数据
		double radius = mapGroup.getRadius();
		String groupConter = mapGroup.getGroupConter();
		String[] groupConterArr = groupConter.split(",");
		if (groupConterArr.length != 2) {
			messageHelper.addFlashMessage(redirectAttributes, "core.success.update", "操作失败");
			return "redirect:/domap/map-mapGroup-list.do";
		}

		// 将数组转换成,隔离的String
		if (groupWeek != null) {
			String groupWeekStr = org.apache.commons.lang.StringUtils.join(groupWeek, ",");
			mapGroup.setGroupWeek(groupWeekStr);
		} else {
			mapGroup.setGroupWeek("");
		}
		// 添加值考勤表
		mapDaoService.MapGroupsave(mapGroup);
		// 获取刚添加值考勤表的ID
		// String groupId =
		// mapDaoService.MapGroupUpdate(mapGroup.getGroupName());
		// get id 就是在获取库中的ID
		String groupId = mapGroup.getGroupId().toString();
		// 删除MapUser所有用户ID的考勤数据
		if (!"".equals(userId)) {
			mapDaoService.deleteAllMapUser(userId);
			// 拼insert value值
			StringBuilder strB = new StringBuilder();
			String[] idArr = userId.split(",");
			for (int i = 0; i < idArr.length; i++) {
				strB.append("(" + idArr[i] + "," + groupId + "),");
			}
			String value = strB.substring(0, strB.length() - 1);
			// 添加数据至MapUser
			mapDaoService.insertMapUser(value);
		}

		messageHelper.addFlashMessage(redirectAttributes, "core.success.update", "操作成功");
		return "redirect:/domap/map-mapGroup-list.do";
	}

	@RequestMapping("map-mapGroup-update")
	public String MapGroupinfoUpdate(MapGroup mapGroup, String[] groupWeek, String userId,
			RedirectAttributes redirectAttributes) {
		// 以下为计算坐标数据
		double radius = mapGroup.getRadius();
		String groupConter = mapGroup.getGroupConter();
		String[] groupConterArr = groupConter.split(",");
		if (groupConterArr.length != 2) {
			messageHelper.addFlashMessage(redirectAttributes, "core.success.update", "操作失败");
			return "redirect:/domap/map-mapGroup-list.do";
		}

		// 将数组转换成,隔离的String
		if (groupWeek != null) {
			String groupWeekStr = org.apache.commons.lang.StringUtils.join(groupWeek, ",");
			mapGroup.setGroupWeek(groupWeekStr);
		} else {
			mapGroup.setGroupWeek("");
		}
		// 修改考勤表
		mapDaoService.MapGroupUpdate(mapGroup);
		// 删除此考勤组所有数据
		mapDaoService.deleteAllMapUserByGroupId(mapGroup.getGroupId());
		// 删除MapUser所有用户ID的考勤数据
		if (!"".equals(userId) && userId != null) {
			mapDaoService.deleteAllMapUser(userId);
			// 拼insert value值
			StringBuilder strB = new StringBuilder();
			String[] idArr = userId.split(",");
			for (int i = 0; i < idArr.length; i++) {
				strB.append("(" + idArr[i] + "," + mapGroup.getGroupId() + "),");
			}
			String value = strB.substring(0, strB.length() - 1);
			// 添加数据至MapUser
			mapDaoService.insertMapUser(value);
		}

		messageHelper.addFlashMessage(redirectAttributes, "core.success.update", "操作成功");
		return "redirect:/domap/map-mapGroup-list.do";
	}

	@RequestMapping("map-mapGroup-delete")
	public String MapGroupinfoDelect(Long groupId, RedirectAttributes redirectAttributes) {
		// 删除此考勤组所有数据
		mapDaoService.deleteAllMapUserByGroupId(groupId);

		mapDaoService.MapGroupdelete(groupId);

		messageHelper.addFlashMessage(redirectAttributes, "core.success.update", "操作成功");
		return "redirect:/domap/map-mapGroup-list.do";
	}

	// 法定假日 或公司共通节日
	@RequestMapping("map-mapHolidays-list")
	public String MapHolidaysList(@ModelAttribute Page page, @RequestParam Map<String, Object> parameterMap,
			Model model) {
		String startTimeStr = (String) parameterMap.get("filter_EQS_startCurrentTime");
		String endTimeStr = (String) parameterMap.get("filter_EQS_endCurrentTime");
		String hql = "from MapHolidays where 1=1";
		if (startTimeStr != null && !"".equals(startTimeStr)) {
			hql = hql + " and mapTime>= '" + startTimeStr + "'";

		}
		if (endTimeStr != null && !"".equals(endTimeStr)) {
			hql = hql + " and mapTime<= '" + endTimeStr + "'";
		}
		hql = hql + " order by mapTime desc";
		page = mapHolidaysManager.pagedQuery(hql, page.getPageNo(), page.getPageSize());
		model.addAttribute("page", page);

		return "domap/map-mapHolidays-list";
	}

	@RequestMapping("map-mapHolidays-input")
	public String MapHolidaysInput() {
		return "domap/map-mapHolidays-input";
	}

	@RequestMapping("map-mapHolidaysInfo-update")
	public String MapHolidaysUpdateList(Long mapId, Model model) {
		List<MapHolidays> holidaysList = mapDaoService.getMapHolidaysById(mapId);
		for (MapHolidays mapHolidays : holidaysList) {
			model.addAttribute("mapId", mapHolidays.getMapId());
			model.addAttribute("mapTime", mapHolidays.getMapTime());
			model.addAttribute("status", mapHolidays.getStatus());
			model.addAttribute("mapNotes", mapHolidays.getMapNotes());
		}
		return "domap/map-mapHolidaysInfo-update";
	}

	@RequestMapping("map-mapHolidays-save")
	public String MapHolidaysSave(@ModelAttribute MapHolidays mapHolidays, RedirectAttributes redirectAttributes) {
		List<MapHolidays> holidaysList = mapDaoService.getMapHolidaysByTime(mapHolidays.getMapTime());
		if (holidaysList.isEmpty()) {
			mapDaoService.saveMapHolidays(mapHolidays);
			messageHelper.addFlashMessage(redirectAttributes, "core.success.update", "操作成功");
		} else {
			messageHelper.addFlashMessage(redirectAttributes, "core.success.update", "已存在,操作失败");
		}
		return "redirect:/domap/map-mapHolidays-list.do";
	}

	@RequestMapping("map-mapHolidays-delete")
	public String MapHolidaysDelect(Long mapId, RedirectAttributes redirectAttributes) {
		mapDaoService.deleteMapHolidays(mapId);
		messageHelper.addFlashMessage(redirectAttributes, "core.success.update", "操作成功");
		return "redirect:/domap/map-mapHolidays-list.do";
	}

	@RequestMapping("map-mapHolidays-update")
	public String MapHolidaysUpdate(@ModelAttribute MapHolidays mapHolidays, RedirectAttributes redirectAttributes) {
		mapDaoService.updateMapHolidays(mapHolidays);
		messageHelper.addFlashMessage(redirectAttributes, "core.success.update", "操作成功");
		return "redirect:/domap/map-mapHolidays-list.do";
	}

	// /**
	// * 文件上传
	// *
	// * @param multipartFile
	// * @return
	// */
	//
	// @RequestMapping("map-info-file")
	// @ResponseBody
	// public JSONObject setFile(@RequestParam MultipartFile multipartFile) {
	// String path = "";
	// JSONObject json = new JSONObject();
	// DataSource dataSource = new MultipartFileDataSource(multipartFile);
	// try {
	// // 照片统一设定为管理员上传的
	// StoreDTO storeDto = storeConnector.saveStore("default/domap/" +
	// "740300885950464", dataSource, "1");
	// path = storeDto.getDisplayName();
	// } catch (Exception e1) {
	//
	// json.put("code", 404);
	// json.put("data", "1");
	// }
	// json.put("code", 200);
	// json.put("data", path);
	//
	// return json;
	// }

	/**
	 * ] 查看图片
	 * 
	 * @param key
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("store-file-view")
	public String AllfileView(@QueryParam("key") String key, Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<StoreInfo> storeInfos = storeInfoManager.findBy("path", key);
		String type = StringUtils.exChange(getSuffix(key));
		model.addAttribute("pdfflg", storeInfos.get(0).getHtmlState());
		model.addAttribute("key", key);
		model.addAttribute("type", type);
		return "store/store-file-view";
	}

	public String getSuffix(String type) {
		int lastIndex = type.lastIndexOf(".") + 1;

		if (lastIndex != -1) {
			return type.substring(lastIndex);
		} else {
			return type;
		}
	}

	@Resource
	public void setMapService(MapService mapService) {
		this.mapService = mapService;
	}

	@Resource
	public void setMapDaoService(MapDaoService mapDaoService) {
		this.mapDaoService = mapDaoService;
	}

	@Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Resource
	public void setMapGroupManager(MapGroupManager mapGroupManager) {
		this.mapGroupManager = mapGroupManager;
	}

	@Resource
	public void setMapInfoManager(MapInfoManager mapInfoManager) {
		this.mapInfoManager = mapInfoManager;
	}

	@Resource
	public void setMessageHelper(MessageHelper messageHelper) {
		this.messageHelper = messageHelper;
	}

	@Resource
	public void setMapHolidaysManager(MapHolidaysManager mapHolidaysManager) {
		this.mapHolidaysManager = mapHolidaysManager;
	}

	@Resource
	public void setExportor(Exportor exportor) {
		this.exportor = exportor;
	}

	@Resource
	public void setStoreInfoManager(StoreInfoManager storeInfoManager) {
		this.storeInfoManager = storeInfoManager;
	}

	@Resource
	public void setTenantHolder(TenantHolder tenantHolder) {
		this.tenantHolder = tenantHolder;
	}

	@Resource
	public void setCurrentUserHolder(CurrentUserHolder currentUserHolder) {
		this.currentUserHolder = currentUserHolder;
	}

	@Resource
	public void setUserAuthConnector(UserAuthConnector userAuthConnector) {
		this.userAuthConnector = userAuthConnector;
	}

	@Resource
	public void setUserConnector(UserConnector userConnector) {
		this.userConnector = userConnector;
	}

}
