package com.thunderlight.sdkDemo.ui.service

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thunderlight.sdkDemo.ui.service.model.MainServicesItem
import com.thunderlight.sdkdemo.R
import com.thunderlight.thundersmartsdk.constant.RequestType


class MainServicesViewModel : ViewModel() {

    var menuList = MutableLiveData<ArrayList<MainServicesItem>>()

    fun getMenuItems() {
        val list = ArrayList<MainServicesItem>()
        list.add(MainServicesItem(RequestType.REQUEST_TYPE_SALE, "خرید", R.drawable.ic_buy))
        list.add(MainServicesItem(RequestType.REQUEST_TYPE_BALANCE, "موجودی", R.drawable.ic_wallet))
        list.add(MainServicesItem(RequestType.REQUEST_TYPE_CHARGE_PIN, "شارژ", R.drawable.ic_sim))
        list.add(MainServicesItem(RequestType.REQUEST_TYPE_BILL, "قبض", R.drawable.ic_bill))

        list.add(MainServicesItem(RequestType.REQUEST_TYPE_DO_KEY_CHANGE, "تراکنش پیکربندی", R.drawable.ic_configuration))
        list.add(MainServicesItem(RequestType.REQUEST_TYPE_INQUIRY_TRANSACTION, "استعلام تراکنش", R.drawable.ic_inquiry))
        list.add(MainServicesItem(RequestType.REQUEST_TYPE_PRINT_BITMAP, "چاپ bitmap", R.drawable.ic_printer))
        list.add(MainServicesItem(RequestType.REQUEST_TYPE_INQUIRY_POS_DATA, "اطلاعات دستگاه", R.drawable.ic_merchant_inquiry))

        menuList.postValue(list)
    }
}