package com.karat.sessionbeans;

import java.util.List;

import javax.ejb.Local;

import com.karat.jpamodel.Category;
import com.karat.models.CategoryMarkup;

@Local
public interface CategoryControllerLocal {
	List<Category> getAllCategories();
	Category getCategoryById(int id);
	List<Category> getAllMainCategories();
	boolean updateCategory(Category category);
	boolean insertCategory(Category category);
	List<CategoryMarkup> getMainCategoriesMarkupByUserId(Integer userId);
	void removeCategory(int categoryId);
	boolean doHaveChildCateg(int categoryId);
	List<CategoryMarkup> getChildCategoriesMarkup(int parentId, int userId);
	List<Category> getChildCategories(int parentId);
	Category getParentCategoryById(int categoryId);
}
