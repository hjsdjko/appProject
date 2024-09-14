package com.design.appproject.logic.viewmodel.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.design.appproject.bean.MenuItemBean
import com.design.appproject.logic.repository.HomeRepository
import java.io.File
/**菜单viewmodel*/
class ListModel : ViewModel() {

    private val pageData = MutableLiveData<List<Any>>()

    val pageLiveData = Transformations.switchMap(pageData) { request ->
        pageData.value?.let {
            HomeRepository.page<MenuItemBean>(it[0] as String, it[1] as Map<String, String>)
        }
    }

    fun page(tableName: String, map: Map<String,String>) {
        pageData.value = listOf(tableName, map)
    }


    private val listData = MutableLiveData<List<Any>>()

    val listLiveData = Transformations.switchMap(listData) { request ->
        listData.value?.let {
            HomeRepository.list<MenuItemBean>(it[0] as String, it[1] as Map<String, String>)
        }
    }

    fun list(tableName: String, map: Map<String,String>) {
        listData.value = listOf(tableName, map)
    }
}