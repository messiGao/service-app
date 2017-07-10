package com.gyc.article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gyc.article.service.ArticleService;
import com.gyc.common.Constants;
import com.gyc.common.RedisUtils;
import com.gyc.dto.article;
import com.gyc.dto.category;
import com.gyc.dto.collectArticle;
import com.gyc.interceptor.Auth;

@RestController

public class ArticleController {
	private static final Logger Logger = LoggerFactory.getLogger(ArticleController.class);

	@Autowired
	ArticleService articleService;

	@RequestMapping("/categories/{type}/{hasArticles}")
	@ResponseBody
	/* 获取文档分类 */
	public Map<String, Object> getCategories(HttpServletRequest req, @PathVariable String type,
			@PathVariable String hasArticles) throws Exception {
		Logger.info("获取文档分类开始");

		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<category> categories = articleService.getCategories(type);
			tempMap.put("code", Constants.SUCCESS);
			data.put("categories", categories);

			// 如果hasArticles为0，不需返回文章详情
			if ("0".equals(hasArticles)) {
				tempMap.put("data", data);
				return tempMap;
			} else {
            // 如果hasArticles为1，需返回文章详情最新10条
				List<article> articles = articleService.getArticlesNew();
				data.put("articles", articles);
				tempMap.put("data", data);
				return tempMap;
			}
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "get categories error");
			Logger.error("获取文档分类开始出现异常" + e);
			return tempMap;
		}
	}

	@RequestMapping("/articles/{category_id}/{limit}/{offset}")
	/* 获取分类下文章 */
	public Map<String, Object> getArticles(HttpServletRequest req, @PathVariable Integer category_id,
			@PathVariable int limit, @PathVariable int offset) throws Exception {
		Logger.info("获取分类下文章开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<article> articles = articleService.getArticles(category_id, limit, offset);
			tempMap.put("code", Constants.SUCCESS);
			data.put("articles", articles);
			tempMap.put("data", data);
			Logger.info("获取分类下文章结束");
			return tempMap;
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "获取分类下文章失败");
			Logger.error("获取分类下文章结束"+category_id);
			return tempMap;
		}
	}

	@RequestMapping("/article/{article_id}/{user_id}")
	/* 获取文章 */
	public Map<String, Object> getArticle(HttpServletRequest req, @PathVariable Integer article_id,
			@PathVariable Integer user_id) throws Exception {
		Logger.info("获取文章详情开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			article article = articleService.getArticle(article_id,user_id);
			tempMap.put("code", Constants.SUCCESS);
			data.put("articles", article);
			tempMap.put("data", data);
			Logger.info("获取文章详情结束");
			return tempMap;
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "获取文章详情失败");
			Logger.error("获取文章详情失败"+article_id);
			return tempMap;
		}
	}
	
	@RequestMapping("/newarticles/{type}/{limit}/{offset}")
	/* 获取该类型下最新5篇文章 */
	public Map<String, Object> getNewArticles(HttpServletRequest req,
			@PathVariable String type,
			@PathVariable int limit,
			@PathVariable int offset
			) throws Exception {
		Logger.info("获取某分类下最新文章详情开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<article> articles = articleService.getNewArticles(type,limit,offset);
			tempMap.put("code", Constants.SUCCESS);
			data.put("articles", articles);
			tempMap.put("data", data);
			Logger.info("获取文章详情结束");
			return tempMap;
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "获取文章详情失败");
			Logger.error("获取文章详情失败"+e);
			return tempMap;
		}
	}
	
	@RequestMapping("/bannerarticles/{type}/{limit}")
	/* 获取BANNER文章 */
	public Map<String, Object> getArticlesByArticletype(HttpServletRequest req,
			@PathVariable String type,
			@PathVariable int limit
			) throws Exception {
		Logger.info("获取BANNER文章开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<article> articles = articleService.getArticlesByArticletype(type,limit);
			tempMap.put("code", Constants.SUCCESS);
			data.put("articles", articles);
			tempMap.put("data", data);
			Logger.info("获取BANNER文章结束");
			return tempMap;
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "获取BANNER文章失败");
			Logger.error("获取BANNER文章"+e);
			return tempMap;
		}
	}

	/* 收藏文章 */
	@RequestMapping(value = "/articleCollect", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> articleCollect(HttpServletRequest req) throws Exception{
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Logger.info("收藏文章开始");
		try{
			String user_id = (String)req.getParameter("userId");
			String article_id = (String)req.getParameter("articleId");
			collectArticle collectArticle = new collectArticle();
			collectArticle.setArticle_id(new Integer(article_id));
			collectArticle.setUser_id(new Integer(user_id));
			articleService.collectArticle(collectArticle);
			tempMap.put("code", Constants.SUCCESS);
		}catch(Exception e){
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "收藏文章失败");
			Logger.error("收藏文章失败");
		}
		return tempMap;
	}
	
	/* 取消收藏 */
	@RequestMapping(value = "/articleCollectCancling", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public Map<String, Object> cancleCollectArticle(HttpServletRequest req) throws Exception{
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Logger.info("收藏文章开始");
		try{
			String article_id = (String)req.getParameter("articleId");
			String user_id = (String)req.getParameter("userId");
			collectArticle collectArticle = new collectArticle();
			collectArticle.setArticle_id(new Integer(article_id));
			collectArticle.setUser_id(new Integer(user_id));
			articleService.cancleCollectArticle(collectArticle);
			tempMap.put("code", Constants.SUCCESS);
		}catch(Exception e){
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "收藏文章失败");
			Logger.error("收藏文章失败");
		}
		return tempMap;
	}
	
	
	@RequestMapping("/searchArticles/{keywords}")
	/* 通过关键字搜索文章 */
	public Map<String, Object> searchArticles(HttpServletRequest req,
			@PathVariable String keywords
			) throws Exception {
		Logger.info("通过关键字搜索文章开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			if(keywords==null){
				tempMap.put("code", Constants.REMOTEFAIL);
				tempMap.put("msg", " 关键字不可为空");
				return tempMap;
			}
			
			List<article> articles = articleService.getArticlesByKeyWords(keywords);
			tempMap.put("code", Constants.SUCCESS);
			data.put("articles", articles);
			tempMap.put("data", data);
			Logger.info("通过关键字搜索文章结束");
			return tempMap;
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "通过关键字搜索文章失败");
			Logger.error("通过关键字搜索文章失败"+e);
			return tempMap;
		}
	}
	
	@RequestMapping("/collectarticles/{user_id}/{limit}/{offset}")
	/* 我的收藏文章列表 */
	public Map<String, Object> getCollectarticles(HttpServletRequest req, @PathVariable Integer user_id,
			@PathVariable int limit, @PathVariable int offset) throws Exception {
		Logger.info("获取分类下文章开始");
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			List<article> articles = articleService.getCollectarticles(user_id, limit, offset);
			tempMap.put("code", Constants.SUCCESS);
			data.put("articles", articles);
			tempMap.put("data", data);
			Logger.info("获取分类下文章结束");
			return tempMap;
		} catch (Exception e) {
			tempMap.put("code", Constants.REMOTEFAIL);
			tempMap.put("msg", "获取分类下文章失败");
			Logger.error("获取我的收藏文章列表失败"+e);
			return tempMap;
		}
	}

}
