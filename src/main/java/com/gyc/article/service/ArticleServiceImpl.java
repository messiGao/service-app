package com.gyc.article.service;

import com.gyc.dto.article;
import com.gyc.dto.collectArticle;
import com.gyc.dto.category;
import com.gyc.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gyc.article.mapper.ArticleMapper;

@Service
public class ArticleServiceImpl implements ArticleService{
    @Autowired
    private ArticleMapper articleMapper;
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	private static String ENCODING = "UTF-8";
	
	@Override
	public List<category> getCategories(String type) throws Exception {
        category category = new category();
        category.setType(type);
		return (List<category>)articleMapper.getCategories(category);
	}

	@Override
	public List<article> getArticles(Integer category_id, int limit, int offset) {
		Map<String,Object> articeMap = new HashMap<String,Object>();
		articeMap.put("category_id", category_id);
		articeMap.put("limit", limit);
		articeMap.put("offset", offset);
		
		return (List<article>)articleMapper.getArticles(articeMap);
	}

	@Override
	public article getArticle(Integer article_id,Integer user_id) {
		article article = new article();
		article.setId(article_id);
		article = articleMapper.getArticle(article);
		
		collectArticle collectArticle = new collectArticle();
		collectArticle.setArticle_id(article_id);
		collectArticle.setUser_id(user_id);
		//再获取是否收藏信息
		List<collectArticle> collectList = articleMapper.getArticleCollect(collectArticle);
		if(collectList.isEmpty()){
			article.setCollect_sign(false);
		}else{
			article.setCollect_sign(true);
		}
		//再获取对应的分类名称
		category category = articleMapper.getCategorieById(article.getCategory_id());
		if(category!=null){
			article.setCategory_name(category.getName());
		}
		return article;
	}

	@Override
	public List<article> getArticlesNew() {
		return articleMapper.getArticleNew();
	}

	@Override
	public void collectArticle(collectArticle collectArticle) {
		collectArticle.setUpdated_at(System.currentTimeMillis()/1000);
		collectArticle.setCreated_at(System.currentTimeMillis()/1000);
		articleMapper.collectArticle(collectArticle);
	}

	@Override
	public List<article> getNewArticles(String type, int limit,int offset) {
		Map<String,Object> articleMap = new HashMap<String,Object>();
		articleMap.put("type", type);
		articleMap.put("limit", limit);
		articleMap.put("offset", offset);
		List<article> artilces = articleMapper.getNewArticlesByType(articleMap);
		return artilces;
	}

	@Override
	public List<article> getArticlesByKeyWords(String keywords) {
		String[] keywordArray = keywords.split(" ");
		article article = new article();
		Set<article> set = new HashSet<article>();  
		for(String name:keywordArray){
			if("".equals(name)){
				continue;
			}
			article.setName("%"+name+"%");
			List<article> artilceList = articleMapper.getArticlesByKeyWord(article);
			set.addAll(artilceList);
		}
		List artilces = new ArrayList<article>(set); 
		return artilces;
	}

	@Override
	public List<article> getArticlesByArticletype(String type, int limit) {
		Map<String,Object> articeMap = new HashMap<String,Object>();
		articeMap.put("type", type);
		articeMap.put("limit", limit);
		articeMap.put("offset", 0);
		
		return (List<article>)articleMapper.getArticles(articeMap);
	}

	@Override
	public void cancleCollectArticle(com.gyc.dto.collectArticle collectArticle) {
		articleMapper.deleteCollectArticle(collectArticle);
	}

	@Override
	public List<article> getCollectarticles(Integer user_id, int limit, int offset) throws Exception {
		Map<String,Object> articeMap = new HashMap<String,Object>();
		articeMap.put("user_id", user_id);
		articeMap.put("limit", limit);
		articeMap.put("offset", offset);
		return (List<article>)articleMapper.getCollectarticles(articeMap);
	}


	
}
