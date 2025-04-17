package com.example.roomdao30.AllViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdao30.AllEntitys.BookShop
import com.example.roomdao30.AllEntitys.Product
import com.example.roomdao30.AllRelation.ProductWithRating
import com.example.roomdao30.AllRepositorys.BookShopRepository
import com.example.roomdao30.AllRepositorys.ProductRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class BookShopViewModel:ViewModel() {
    private val shopRepository = BookShopRepository()

  //  private val requestRatingsForProduct = MutableLiveData<List<ProductWithRating>>()

//    val requestProductList: LiveData<List<ProductWithRating>>
//        get() = requestRatingsForProduct
   private val getShopMutableLiveData = MutableLiveData<BookShop?>()



    val updateShopLiveData: LiveData<BookShop?>
        get() = getShopMutableLiveData

// //   private val requestAuthorsForClient = MutableLiveData<List<ClientWithAuthor>>()
//
//    val requestAuthorList: LiveData<List<ClientWithAuthor>>
//        get() = requestAuthorsForClient

    private val shopMutableLiveData = MutableLiveData<List<BookShop>>()

    val recordShopData: LiveData<List<BookShop>>
        get() = shopMutableLiveData

    private val saveSuccessLiveEvent = MutableLiveData<Unit>()

    val saveSuccessLiveData: LiveData<Unit>
        get() = saveSuccessLiveEvent

    private val saveErrorLiveEvent = MutableLiveData<Int>()

    val saveErrorLiveData: LiveData<Int>
        get() = saveErrorLiveEvent
    fun save(shop: BookShop) {
        viewModelScope.launch {
            try {
                if (shop.id == 0) {
                    shopRepository.saveShop(shop)
                } else {
                   shopRepository.updateShop(shop)
                }
                saveSuccessLiveEvent.postValue(Unit)
            } catch (t: Throwable) {
                Timber.e(t, "Ошибка сохранения продукта")
                showError(t)
            }
        }
    }
    fun updateShop(shop: BookShop) {
        viewModelScope.launch {
            try {
              shopRepository.updateShop(shop)
                getShopMutableLiveData.postValue(shop)


            } catch (t: Throwable) {
                Timber.e(t, "Ошибка обновления продукта")
                showError(t)

            }

        }
    }
    fun loadList() {
        viewModelScope.launch {
            try {
                shopMutableLiveData.postValue(shopRepository.getAllShop())
            } catch (t: Throwable) {
          