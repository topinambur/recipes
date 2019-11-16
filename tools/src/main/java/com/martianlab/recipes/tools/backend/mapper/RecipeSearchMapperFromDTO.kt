package com.martianlab.recipes.tools.backend.mapper

import com.martianlab.recipes.entities.*
import com.martianlab.recipes.tools.backend.dto.CategoryDTO
import com.martianlab.recipes.tools.backend.dto.CommentDTO
import com.martianlab.recipes.tools.backend.dto.RecipeCookingDTO
import com.martianlab.recipes.tools.backend.dto.RecipeDTO
import com.martianlab.recipes.tools.backend.dto.RecipeIngredientDTO
import com.martianlab.recipes.tools.backend.dto.response.RecipeSearchResponseBodyDTO
import java.text.SimpleDateFormat


internal object RecipeSearchMapperFromDTO {

    fun mapToRecipeList( obj : RecipeSearchResponseBodyDTO ) : List<Recipe>{
        val tags = /*obj.categoryList?.map { it.toTag() }?.toSet() ?:*/ setOf<RecipeTag>()
        val ingredients = obj.ingredientList?.map { it.toIngredient() } ?: listOf()
        val stages = obj.recipeCookingList?.map { it.toStage() } ?: listOf()

        return obj.recipeList
            ?.map { it.toRecipe() }
            ?.map { it.copy(tags = tags, ingredients = ingredients, stages = stages) }
            ?: listOf()
    }

    fun mapToCategoryList( obj : RecipeSearchResponseBodyDTO ) : List<Category>{
        val total = obj.total

        return obj.categoryList
            ?.map { it.toCategory(total) }
            ?: listOf()
    }

    fun mapToCategory( obj : RecipeSearchResponseBodyDTO ) : Category? {
        val total = obj.total

        return obj.categoryList
            ?.map { it.toCategory(total) }?.first()
    }

    fun map( obj : RecipeDTO ) : Recipe{
        return Recipe(
            id = obj.id.toLong(),
            title = obj.name,
            text = obj.description,
            imageURL = obj.imageURL,
            complexity = obj.complexity,
            personCount = obj.numberPersons,
            rating = obj.rating?.rate?.toDouble() ?: 0.0,
            ratingVotes = obj.rating?.votes ?: 0,
            comments = obj.comments.map { it.toComment(obj.id.toLong()) },
            stages = listOf(),
            tags = setOf(),
            ingredients = listOf()
        )
    }

    fun map( obj : CommentDTO, recipeId : Long  ) : RecipeComment =
        RecipeComment(
            id = obj.id,
            recipeId = recipeId,
            authorName = obj.author,
            authorId = 0L,
            date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse( obj.createdAt ), //2015-07-26 14:19:52
            text = obj.text,
            parentCommentId = obj.parentId,
            photoURLs = listOf()
        )

    fun map( obj : RecipeCookingDTO ) : RecipeStage =
        RecipeStage(
            id = obj.id,
            recipeId = obj.recipeId,
            imageURL = obj.imageURL,
            text = obj.description,
            step = obj.position
        )

    fun map( obj : RecipeIngredientDTO) : RecipeIngredient =
        RecipeIngredient(
            id = obj.id,
            recipeId = obj.recipeId,
            title = obj.name,
            quantity = obj.quantity,
            measureUnit = obj.measureUnit,
            position = obj.position

        )

    fun map( obj : CategoryDTO, recipeId : Long  ) : RecipeTag =
        RecipeTag(
            id = obj.id,
            recipeId = recipeId,
            title = obj.category
        )

    fun map( obj : CategoryDTO, total: Int ) : Category =
        Category(
            id = obj.id,
            title = obj.category,
            imageUrl = obj.imageURL,
            sort = obj.sort,
            total = total
        )

}
fun RecipeSearchResponseBodyDTO.toCategory() : Category? = RecipeSearchMapperFromDTO.mapToCategory(this)

fun RecipeSearchResponseBodyDTO.toCategoryList() : List<Category> = RecipeSearchMapperFromDTO.mapToCategoryList(this)

fun RecipeSearchResponseBodyDTO.toRecipeList() : List<Recipe> = RecipeSearchMapperFromDTO.mapToRecipeList(this)

fun RecipeDTO.toRecipe() : Recipe = RecipeSearchMapperFromDTO.map(this)

fun CommentDTO.toComment(recipeId : Long ) : RecipeComment = RecipeSearchMapperFromDTO.map(this, recipeId)

fun RecipeCookingDTO.toStage() : RecipeStage = RecipeSearchMapperFromDTO.map(this)

fun RecipeIngredientDTO.toIngredient() : RecipeIngredient = RecipeSearchMapperFromDTO.map(this)

fun CategoryDTO.toTag( recipeId : Long ) : RecipeTag = RecipeSearchMapperFromDTO.map(this, recipeId )

fun CategoryDTO.toCategory( total : Int ) : Category = RecipeSearchMapperFromDTO.map(this, total )
