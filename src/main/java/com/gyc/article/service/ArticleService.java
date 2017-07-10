package com.gyc.article.service;

import java.util.List;

import com.gyc.dto.article;
import com.gyc.dto.category;
import com.gyc.dto.collectArticle;

public interface ArticleService {
	
	public List<category> getCategories(String type) throws Exception;

	public List<article> getArticles(Integer category_id, int limit, int offset);

	public article getArticle(Integer article_id, Integer user_id);

	public List<article> getArticlesNew();

	public void collectArticle(collectArticle collectArticle);

	public List<article> getArticlesByKeyWords(String keywords);

	public List<article> getArticlesByArticletype(String type, int limit);

	public List<article> getNewArticles(String type, int limit, int offset);

	public void cancleCollectArticle(collectArticle collectArticle);

	public List<article> getCollectarticles(Integer user_id, int limit, int offset) throws Exception;

}
