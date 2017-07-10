package com.gyc.article.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gyc.dto.article;
import com.gyc.dto.category;
import com.gyc.dto.collectArticle;
import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleMapper {
	public List<category> getCategories(category category);

	public List<article> getArticles(Map<String, Object> articeMap);
	
	public article getArticle(article article);

	public List<article> getArticleNew();

	public void collectArticle(collectArticle collectArticle);

	public List<article> getNewArticlesByType(Map<String, Object> articleMap);

	public List<article> getArticlesByKeyWord(article article);

	public void deleteCollectArticle(collectArticle collectArticle);

	public List<collectArticle> getArticleCollect(collectArticle collectArticle);

	public category getCategorieById(Integer category_id);

	public List<article> getCollectarticles(Map<String, Object> articeMap);
	
}
