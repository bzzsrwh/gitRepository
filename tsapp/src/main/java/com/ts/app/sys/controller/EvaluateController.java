package com.ts.app.sys.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ts.app.sys.constants.Constants;
import com.ts.app.sys.domain.Article;
import com.ts.app.sys.domain.Evaluate;
import com.ts.app.sys.service.ArticleService;
import com.ts.app.sys.service.EvaluateService;

/**
 * 评价controller
 */
@Controller
public class EvaluateController extends BaseController {

	@Autowired
	private EvaluateService evaluateService;
	
	@Autowired
    private ArticleService articleService;  
	
	/**
	 * 评价列表
	 */
	@RequestMapping("/evaluateController/list")
	@ResponseBody
	public List list(Integer articleid){
		Map<String,Object> filterMap = new HashMap<String,Object>();
		filterMap.put("articleid", articleid);
		 List  articleList= evaluateService.queryListByArticeId2(filterMap);
		return articleList;
	}
	
	/**
	 * 添加评价
	 */
	@RequestMapping("/evaluateController/doInsert")
	@ResponseBody
	public Map<String,String> doInster(Evaluate Evaluate){
		Map<String,String> retMap = new HashMap<String,String>();
		retMap.put("msg", "评价成功");
		retMap.put("flag", "1");
		
		try{
			Integer createuserid = getLoginUid();
			Evaluate.setCreateuserid(createuserid);
			Evaluate.setCreatedate(new Date());
			evaluateService.insert(Evaluate);
			
			Integer articleid = Evaluate.getArticleid();
			Article article = articleService.selectByPrimaryKey(articleid);
			
			Integer evaluatenum = article.getEvaluatenum();
			if(evaluatenum==null){
				evaluatenum = 0;
			}
			evaluatenum=evaluatenum+1;
			article.setEvaluatenum(evaluatenum);
			
			articleService.updateByPrimaryKeySelective(article);
			
		}catch(Exception e){
			retMap.put("msg", "评价失败");
			retMap.put("flag", "0");
		}
		
		return retMap;
	}
	
	/**
	 * 获得我的未读取的评价消息(我发布的帖子)
	 * @param articleid
	 * @return
	 */
	@RequestMapping("/evaluateController/getMyUnRead")
	@ResponseBody
	public Map<String,Object> getMyUnRead(){
		Integer createuserid = getLoginUid();
		Map<String,Object> filterMap = new HashMap<String,Object>();
		filterMap.put("createUserId", createuserid);
		
		Integer  MyUnReadCount= 	evaluateService.getMyUnRead(filterMap);
		
		Integer  getMyUnReadReply= 	evaluateService.getMyUnReadReply(filterMap);
		
		
		Map<String,Object> retMap = new HashMap<String,Object>();
		retMap.put("MyUnReadCount", MyUnReadCount);
		retMap.put("getMyUnReadReply", getMyUnReadReply);
		retMap.put("flag", "1");
		
		return retMap;
	}
	
	/**
	 *改变评论消息状态
	 * @param articleid
	 * @return
	 */
	@RequestMapping("/evaluateController/changegetMyUnRead")
	@ResponseBody
	public Map<String,Object> changegetMyUnRead(){
		Map<String,Object> retMap = new HashMap<String,Object>();
		retMap.put("msg", "操作成功");
		retMap.put(Constants.SUCCESS, true);
		Integer createuserid = getLoginUid();
		Map<String,Object> filterMap = new HashMap<String,Object>();
		filterMap.put("createUserId", createuserid);
		
		evaluateService.changegetMyUnRead(filterMap);
		
		return retMap;
	}
	
	/**
	 * 改变回复消息消息状态
	 * @param articleid
	 * @return
	 */
	@RequestMapping("/evaluateController/changeMyUnReadReply")
	@ResponseBody
	public Map<String,Object> changeMyUnReadReply(){
		
		Map<String,Object> retMap = new HashMap<String,Object>();
		retMap.put("msg", "操作成功");
		retMap.put(Constants.SUCCESS, true);
		
		Integer createuserid = getLoginUid();
		Map<String,Object> filterMap = new HashMap<String,Object>();
		filterMap.put("createUserId", createuserid);
		
		evaluateService.changeMyUnReadReply(filterMap);
		
		return retMap;
	}
	
}
