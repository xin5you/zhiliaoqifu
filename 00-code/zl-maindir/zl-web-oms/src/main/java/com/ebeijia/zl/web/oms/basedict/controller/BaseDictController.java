package com.ebeijia.zl.web.oms.basedict.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ebeijia.zl.common.core.domain.BaseDict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ebeijia.zl.basics.billingtype.service.BaseDictService;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.enums.DataStatEnum;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "baseDict")
public class BaseDictController {
	Logger logger = LoggerFactory.getLogger(BaseDictController.class);

	@Autowired
	private BaseDictService baseDictService;

	@Autowired
	@Qualifier("jedisClusterUtils")
	private JedisClusterUtils jedisClusterUtils;

	@RequestMapping(value = "/listBaseDict")
	public ModelAndView listBaseDict(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("baseDict/listBaseDict");
		String operStatus = StringUtil.nullToString(req.getParameter("operStatus"));
		PageInfo<BaseDict> pageList = null;
		BaseDict baseDict = new BaseDict();
		baseDict.setDictName(StringUtil.nullToString(req.getParameter("dictName")));
		baseDict.setDictCode(StringUtil.nullToString(req.getParameter("dictCode")));

		try {
			int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
			int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
			pageList = baseDictService.getBaseDictListPage(startNum, pageSize, baseDict);

		} catch (Exception e) {
			logger.error("## 查询字典列表信息出错", e);
		}

		mv.addObject("pageInfo", pageList);
		mv.addObject("operStatus", operStatus);
		mv.addObject("baseDict", baseDict);
		return mv;
	}

	@RequestMapping(value = "/intoEditBaseDict")
	public ModelAndView intoEditBaseDict(HttpServletRequest req, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("baseDict/editBaseDict");
		String dictId = StringUtil.nullToString(req.getParameter("dictId"));
		BaseDict baseDict = baseDictService.getById(dictId);
		mv.addObject("baseDict", baseDict);
		return mv;
	}

	@RequestMapping(value = "/editBaseDictCommit")
	@ResponseBody
	public Map<String, Object> editBaseDictCommit(HttpServletRequest req, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String dictId = StringUtil.nullToString(req.getParameter("dictId"));
		String dictCode = StringUtil.nullToString(req.getParameter("dictCode"));

		boolean flag = true;
		List<BaseDict> baseDictList = baseDictService.getBaseDictList(new BaseDict());
		for (BaseDict dict : baseDictList) {
			if (dict.getDictCode().equalsIgnoreCase(dictCode)) {
				if (!dict.getDictId().equals(dictId)) {
					flag = false;
					break;
				}
			}
		}

		if (!flag) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "字典代码已存在，请重新输入");
			return resultMap;
		}

		BaseDict baseDict = getBaseDict(req);
		if (!baseDictService.updateById(baseDict)) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("msg", "编辑字典信息失败");
		}
		jedisClusterUtils.hset("TB_BASE_DICT_KV", "OMS_BATCH_OPEN_ACCOUNT_EXCEL_PATH", baseDict.getDictValue());

		resultMap.put("status", Boolean.TRUE);
		return resultMap;
	}

	private BaseDict getBaseDict(HttpServletRequest req) {
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute(Constants.SESSION_USER);

		String dictId = StringUtil.nullToString(req.getParameter("dictId"));
		String dictCode = StringUtil.nullToString(req.getParameter("dictCode"));
		String isdefault = StringUtil.nullToString(req.getParameter("isdefault"));
		String seq = StringUtil.nullToString(req.getParameter("seq"));
		String dictName = StringUtil.nullToString(req.getParameter("dictName"));
		String pid = StringUtil.nullToString(req.getParameter("pid"));
		String dictType = StringUtil.nullToString(req.getParameter("dictType"));
		String dictValue = StringUtil.nullToString(req.getParameter("dictValue"));
		String remarks = StringUtil.nullToString(req.getParameter("remarks"));

		BaseDict baseDict = null;
		if (!StringUtil.isNullOrEmpty(dictId)) {
			baseDict = baseDictService.getById(dictId);
		} else {
			baseDict = new BaseDict();
			baseDict.setDictId(IdUtil.getNextId());
			baseDict.setCreateUser(user.getId());
			baseDict.setCreateTime(System.currentTimeMillis());
			baseDict.setIsdefault("1");
			baseDict.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
			baseDict.setLockVersion(0);
		}
		baseDict.setDictCode(dictCode);
		baseDict.setRemarks(remarks);
		baseDict.setDictName(dictName);
		baseDict.setDictType(dictType);
		baseDict.setDictValue(dictValue);
		baseDict.setIsdefault(isdefault);
		baseDict.setSeq(seq);
		baseDict.setPid(pid);
		baseDict.setUpdateUser(user.getId());
		baseDict.setUpdateTime(System.currentTimeMillis());
		return baseDict;
	}

}
