package com.fundit.model

/**
 * Created by Nivida new on 17-Jul-17.
 */
class CategoryResponse : AppModel(){

    var data: ArrayList<Category> = ArrayList()

    class Category{
        var id: String = ""
        var name: String = ""
        var parent_id: String = ""
        var image: String = ""
    }
}